package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.RegistrationActivity;
import wash.midest.com.mrwashapp.utils.AppUtils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderMapFrag extends BaseFrag implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_LOCATION = 2773;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String TAG = "OrderMapFrag";
    private Unbinder mUnbinder;
    @BindView(R.id.placeOrderDetailMap)
    MapView mMapView;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    @BindView(R.id.house_new)
    TextInputEditText mHouseAdd;
    @BindView(R.id.landmark_new)
    TextInputEditText mLandmark;
    @BindView(R.id.searchLayout)
    LinearLayout mSearchLayout;
    @BindView(R.id.currentAddress)
    TextView mCurrentAddressRetrieved;
    @BindView(R.id.confirm_btn)
    Button mConfirm;

    private boolean isLocationClicked;
    private Marker mCurrLocationMarker;
    private Location mLastLocation;
    private GoogleMap mGoogleMap;
    private OnLocationSelected mChangedLocationToUpdate;
    private static Fragment mParentFrag;
    private boolean isLocationReceived;
    private PendingIntent mPendingIntent;
    private String mSelectedAddress;
    private double mLatSelected;
    private double mLonSelected;
    LocationRequest mLocationRequest;
    LocationReceiver mLocationReceiver;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private boolean isVisible;
    private FusedLocationProviderClient mfusedLocationProviderclient;

    public OrderMapFrag() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    void isPermissionRequired(boolean locationClicked) {
        isLocationClicked = locationClicked;
        boolean isPermissionRequired = new AppUtils().isVersionGreaterThanM(getActivity().getApplicationContext());
        if (isPermissionRequired) {
            checkPermission();
        } else {
            postPermissionGranted();
        }
    }

    void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getActivity().checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && getActivity().checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_LOCATION);
        } else {
            postPermissionGranted();
        }
    }
    private void postPermissionGranted() {
        Log.d(TAG,"postPermissionGranted called");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);
        mfusedLocationProviderclient = LocationServices.getFusedLocationProviderClient(getActivity());
        Log.d(TAG,"mfusedLocationProviderclient.requestLocationUpdates");
        if(isLocationClicked){
            Log.d(TAG,"Location requested , isLocationClicked ="+isLocationClicked);
            mfusedLocationProviderclient.requestLocationUpdates(mLocationRequest,mLocationReceiver,Looper.myLooper());
        }
        else if(isLocationReceived){
            mfusedLocationProviderclient.removeLocationUpdates(mLocationReceiver);
            Log.d(TAG,"Location already received,So not requesting again");
        }else{
            mfusedLocationProviderclient.requestLocationUpdates(mLocationRequest,mLocationReceiver,Looper.myLooper());
            Log.d(TAG,"Location requested");
        }
    }

    class LocationReceiver extends LocationCallback
    {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(TAG,"mfusedLocationProviderclient onLocationResult called");
            onLocationChanged(locationResult.getLastLocation());
        }
    }
    public static OrderMapFrag newInstance(Fragment parentFrag) {
        OrderMapFrag fragment = new OrderMapFrag();
        mParentFrag = parentFrag;
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_map, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mMapView.onCreate(savedInstanceState);
        mLocationReceiver = new LocationReceiver();
        setHasOptionsMenu(true);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        onAttachToParentFragment(mParentFrag);
        return view;
    }

    @OnClick(R.id.confirm_btn)
    void onSubmission(){
        String tempHouse="";
        String tempLandmark="";
        if (!TextUtils.isEmpty(mHouseAdd.getText())) {
            tempHouse = mHouseAdd.getText().toString();
        }
        if (!TextUtils.isEmpty(mLandmark.getText())) {
            tempLandmark = mLandmark.getText().toString();
        }
        mChangedLocationToUpdate.updatedLocation(new LatLng(mLatSelected,mLonSelected),mSelectedAddress,tempHouse,tempLandmark);
        getActivity().getSupportFragmentManager().popBackStack();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            mGoogleMap.setMyLocationEnabled(true);
        }
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                isPermissionRequired(true);
                return true;
            }
        });
        if (mMapView != null &&
                mMapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            rlp.setMargins(5, 5, 5, 5);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            ) {
                        mGoogleMap.setMyLocationEnabled(true);
                        postPermissionGranted();
                    }
                } else {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Identifying location is required for us to show your current area");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            checkPermission();
                        }
                    });
                    alertBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Not able to proceed due to permission denial!", Toast.LENGTH_LONG).show();
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();
                }
                return;
            }
        }
    }
    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({R.id.searchLayout})
    void addressClickEvent() {
        try{
           Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .build(getActivity());
            try {
                intent.putExtra("primary_color", getResources().getColor(R.color.colorPrimary));
                intent.putExtra("primary_color_dark", getResources().getColor(R.color.colorPrimaryDark));
            } catch (Exception e) {
                e.printStackTrace();
            }
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
    } catch (GooglePlayServicesRepairableException e) {
        showToast("Google Play Service Repair");
    } catch (GooglePlayServicesNotAvailableException e) {
        showToast("Google Play Service Not Available");
    }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"OrderMapFrag onActivityResult getting called");
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(getActivity(), data);
            initCameraIdle(place.getLatLng().latitude,place.getLatLng().longitude);
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            //showToast("Error in retrieving place info, Please try searching again or enter House and Landmark to proceed");
            showErrorAlert("Error in retrieving place info, Please try searching again or enter House and Landmark to proceed");
        }
    }

    public void onLocationChanged(Location location) {
        if(isLocationReceived){
            Log.d(TAG,"Location already received,So not requesting again");
            mfusedLocationProviderclient.removeLocationUpdates(mLocationReceiver);
        }
        Log.d(TAG,"isLocationReceived = "+isLocationReceived);
        //mProgressBar.setVisibility(View.GONE);
        if(isLocationClicked || !isLocationReceived){
            isLocationReceived=true;
            mLastLocation = location;
            Log.d(TAG,"Inside if loop ");
            initCameraIdle(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        }
    }

    private void initCameraIdle(final double latitude, final double longitude) {
        //OnResume getting called after onActivityResult, so isVisible is false
        /*if(isVisible){*/
            Log.d(TAG,"OrderMapFrag initCameraIdle");
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            mGoogleMap.clear();
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16));
            LatLng latLng = new LatLng(latitude,longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            mCurrLocationMarker.setPosition(new LatLng(latitude,longitude));
            getAddressFromLoc(latitude, longitude);
        /*}*/
    }

    private void getAddressFromLoc(double latitude, double longitude) {
        Log.d(TAG,"OrderMapFrag getAddressFromLoc");
        Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuffer str = new StringBuffer();
                if(!TextUtils.isEmpty(address.getFeatureName()))
                str.append(address.getFeatureName()+", ");
                if(!TextUtils.isEmpty(address.getSubLocality()))
                str.append( address.getSubLocality()+", " );
                if(!TextUtils.isEmpty(address.getThoroughfare()))
                str.append( address.getThoroughfare() +", ");
                if(!TextUtils.isEmpty(address.getLocality()))
                str.append( address.getLocality() +", ");
                if(!TextUtils.isEmpty(address.getAdminArea()))
                str.append(address.getAdminArea() +", ");
                if(!TextUtils.isEmpty(address.getCountryName()))
                str.append(address.getCountryName() );
                mSelectedAddress = str.toString();
                mLatSelected = latitude;
                mLonSelected = longitude;
                Log.d(TAG,"Update Address = "+mSelectedAddress.toString());
                mCurrentAddressRetrieved.setText(mSelectedAddress.toString());
            } else {
                mCurrentAddressRetrieved.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Could not get address..!", Toast.LENGTH_LONG);
        }
    }

    public void onAttachToParentFragment(Fragment fragment)
    {
        try
        {
            mChangedLocationToUpdate = (OnLocationSelected)fragment;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnLocationSelected");
        }
    }

    public interface OnLocationSelected
    {
        public void updatedLocation(LatLng location,String googleAddress,String house,String landmark);
    }
    @Override
    public void onResume() {
        super.onResume();
        isVisible=true;
        Log.d(TAG,"onResume "+TAG);
        isPermissionRequired(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible=false;
        Log.d(TAG,"onPause "+TAG);
    }
}

