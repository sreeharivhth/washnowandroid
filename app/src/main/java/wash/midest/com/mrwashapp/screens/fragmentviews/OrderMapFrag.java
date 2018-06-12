package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.location.LocationRequest;
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
public class OrderMapFrag extends BaseFrag implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int PERMISSIONS_REQUEST_LOCATION = 2773;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String TAG = "OrderMapFrag";
    private Unbinder mUnbinder;
    @BindView(R.id.placeOrderDetailMap)
    MapView mMapView;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    /*@BindView(R.id.txtLocationAddress)
    TextView mTxtAddress;
    @BindView(R.id.cardView)
    CardView mCardAddress;*/
    @BindView(R.id.house_new)
    TextInputEditText mHouseAdd;
    @BindView(R.id.landmark_new)
    TextInputEditText mLandmark;
    /*@BindView(R.id.getCurrentLocation)
    ImageView mGetCurrentLoc;*/
    /*@BindView(R.id.centerMarker)
    ImageView mCenterMarker;*/
    @BindView(R.id.searchLayout)
    LinearLayout mSearchLayout;
    @BindView(R.id.currentAddress)
    TextView mCurrentAddressRetrieved;
    @BindView(R.id.confirm_btn)
    Button mConfirm;
    private LocationManager mLocationManager;
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
    GoogleApiClient mGoogleApiClient;

    public OrderMapFrag() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(this);
                mLocationManager = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    void isPermissionRequired() {
        boolean isPermissionRequired = new AppUtils().isVersionGreaterThanM(getActivity().getApplicationContext());
        if (isPermissionRequired) {
            checkPermission();
        } else {
            postPermissionGranted();
        }
    }

    //||
    //                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

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


        /*if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            //Permission already granted. Proceed with functionalities
            postPermissionGranted();
        }*/
    }


    /*@OnClick(R.id.getCurrentLocation)
    void getCurrentLocation(){
        mProgressBar.setVisibility(View.VISIBLE);
        mCenterMarker.setVisibility(View.VISIBLE);
        isPermissionRequired();
    }*/

    /*@SuppressLint("MissingPermission")*/
    private void postPermissionGranted() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        /*mGoogleMap.setMyLocationEnabled(true);*/
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
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

        /*mTxtAddress.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTxtAddress.setSingleLine(true);
        mTxtAddress.setMarqueeRepeatLimit(-1);
        mTxtAddress.setSelected(true);*/
        isPermissionRequired();
        isLocationReceived = false;
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

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.location_set, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_done:
                getActivity().getSupportFragmentManager().popBackStack();
                //getChildFragmentManager().popBackStack();

                mSharedPreference.setPreferenceDouble(mSharedPreference.LAT_SELECTED, mLatSelected);
                mSharedPreference.setPreferenceDouble(mSharedPreference.LON_SELECTED, mLonSelected);
                mSharedPreference.setPreferenceString(mSharedPreference.SELECTED_ADDERSS, mSelectedAddress);

                if (!TextUtils.isEmpty(mHouseAdd.getText())) {
                    mSharedPreference.setPreferenceString(mSharedPreference.HOUSE_FLAT, mHouseAdd.getText().toString());
                } else {
                    mSharedPreference.setPreferenceString(mSharedPreference.HOUSE_FLAT, "");
                }
                if (!TextUtils.isEmpty(mLandmark.getText())) {
                    mSharedPreference.setPreferenceString(mSharedPreference.LANDMARK, mLandmark.getText().toString());
                } else {
                    mSharedPreference.setPreferenceString(mSharedPreference.LANDMARK, "");
                }
                return false;
            default:
                break;
        }
        return false;
    }*/

    @OnClick(R.id.confirm_btn)
    void onSubmission(){
        //mSharedPreference.setPreferenceDouble(mSharedPreference.LAT_SELECTED, mLatSelected);
        //mSharedPreference.setPreferenceDouble(mSharedPreference.LON_SELECTED, mLonSelected);
        //mSharedPreference.setPreferenceString(mSharedPreference.SELECTED_ADDERSS, mSelectedAddress);
        String tempHouse="";
        String tempLandmark="";
        if (!TextUtils.isEmpty(mHouseAdd.getText())) {
            //mSharedPreference.setPreferenceString(mSharedPreference.HOUSE_FLAT, mHouseAdd.getText().toString());
            tempHouse = mHouseAdd.getText().toString();
        }
        if (!TextUtils.isEmpty(mLandmark.getText())) {
            //mSharedPreference.setPreferenceString(mSharedPreference.LANDMARK, mLandmark.getText().toString());
            tempLandmark = mLandmark.getText().toString();
        }
        mChangedLocationToUpdate.updatedLocation(new LatLng(mLatSelected,mLonSelected),mSelectedAddress,tempHouse,tempLandmark);

        /*public String TEMP_SELECTED_ADDERSS="TEMP_SELECTED_ADDERSS";
        public String TEMP_LAT_SELECTED="TEMP_LAT_SELECTED";
        public String TEMP_LON_SELECTED="TEMP_LON_SELECTED";*/
        getActivity().getSupportFragmentManager().popBackStack();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            //Not in api-23, no need to prompt
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                isPermissionRequired();
                return false;
            }
        });

        if (mMapView != null &&
                mMapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            rlp.setMargins(5, 5, 5, 5);
        }

        /*if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);*/
        //mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);

        //Enable zoom in / out
        /*mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);*/
        /*LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney")
        );
        
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        /*mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                // Setting the position for the marker
                markerOptions.position(latLng);
                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                // Clears the previously touched position
                mGoogleMap.clear();
                // Animating to the touched position
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                // Placing a marker on the touched position
                mGoogleMap.addMarker(markerOptions);
                mChangedLocationToUpdate.updatedLocation(latLng);
            }
        });*/
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick({R.id.searchLayout})
    void addressClickEvent() {
        try{

            /*PlacePicker.IntentBuilder placeIntent = new PlacePicker.IntentBuilder();
            Intent intent = placeIntent.build(getActivity());*/

        Intent intent =
                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .build(getActivity());

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
            /*
            if (!place.getAddress().toString().contains(place.getName())) {

                Log.d(TAG,"PLACE SELECTED = "+place.getName() + ", " + place.getAddress());
                mTxtAddress.setText(place.getName() + ", " + place.getAddress());
            } else {
                mTxtAddress.setText(place.getAddress());
            }

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16);
            mGoogleMap.animateCamera(cameraUpdate);

            Log.d(TAG,"mGoogleMap.animateCamera animated to new : latitude= "+place.getLatLng().latitude+" || longitude:"+place.getLatLng().longitude);*/

            /*mCenterMarker.setVisibility(View.VISIBLE);*/
            initCameraIdle(place.getLatLng().latitude,place.getLatLng().longitude);

        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            showToast("Error in retrieving place info");

        }

        /*switch (requestCode){
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:

                break;

                default:
                    super.onActivityResult(requestCode, resultCode, data);
        }*/
        /*if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {


        }*/
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,"isLocationReceived = "+isLocationReceived);
        mProgressBar.setVisibility(View.GONE);
        if(!isLocationReceived){
            Log.d(TAG,"Inside if loop ");
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            mGoogleMap.clear();
            Log.d(TAG, TAG+"LAT="+String.valueOf(location.getLatitude())+"||LON="+String.valueOf(location.getLongitude()));
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

            //move map camera

            //For timebeing commented to checkl Place order picker handling
            //mChangedLocationToUpdate.updatedLocation(latLng);

            initCameraIdle(mLastLocation.getLatitude(),mLastLocation.getLongitude());

        }else{

            Log.d(TAG,"Inside Else part");
            if(mLocationManager!=null){
                mLocationManager.removeUpdates(this);
                mLocationManager=null;

            }
            isLocationReceived=true;
        }

    }

    private void initCameraIdle(final double latitude, final double longitude) {
        Log.d(TAG,"OrderMapFrag initCameraIdle");
        /*mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
            }
        });*/

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16));

        LatLng latLng = new LatLng(latitude,longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        mCurrLocationMarker.setPosition(new LatLng(latitude,longitude));

        getAddressFromLoc(latitude, longitude);
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
                //mTxtAddress.setText(mSelectedAddress.toString());
                mCurrentAddressRetrieved.setText(mSelectedAddress.toString());
            } else {
                //mTxtAddress.setText("Searching Current Address");
                mCurrentAddressRetrieved.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
            //printToast("Could not get address..!");
            showToast("Could not get address..!", Toast.LENGTH_LONG);

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
}
