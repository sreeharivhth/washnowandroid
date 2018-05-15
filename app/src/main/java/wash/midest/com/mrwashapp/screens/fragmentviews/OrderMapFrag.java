package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.RegistrationActivity;
import wash.midest.com.mrwashapp.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderMapFrag extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int PERMISSIONS_REQUEST_LOCATION = 2773;
    private String TAG = "OrderMapFrag";
    private Unbinder mUnbinder;
    @BindView(R.id.placeOrderDetailMap)
    MapView mMapView;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    private LocationManager mLocationManager;
    private Marker mCurrLocationMarker;
    private Location mLastLocation;
    private GoogleMap mGoogleMap;
    private OnLocationSelected mChangedLocationToUpdate;
    private static Fragment mParentFrag;
    private boolean isLocationReceived;
    private PendingIntent mPendingIntent;

    public OrderMapFrag() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/
        isPermissionRequired();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mLocationManager!=null ){
            try {
                mLocationManager.removeUpdates(this);
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


    void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_PHONE_STATE)) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            //Permission already granted. Proceed with functionalities
            postPermissionGranted();
        }
    }


    private void postPermissionGranted() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

//        mLocationManager.requestSingleUpdate();

        mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        /*mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/

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
        View view =  inflater.inflate(R.layout.fragment_order_map, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mMapView.onCreate(savedInstanceState);
        isLocationReceived=false;
        setHasOptionsMenu(true);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        onAttachToParentFragment(mParentFrag);
        mProgressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.location_set, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_done:
                getActivity().getSupportFragmentManager().popBackStack();
                /*getChildFragmentManager().popBackStack();*/
                return false;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        /*LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney")
        );
        
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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
        });
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
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
            mChangedLocationToUpdate.updatedLocation(latLng);

        }else{

            Log.d(TAG,"Inside Else part");
            if(mLocationManager!=null){
                mLocationManager.removeUpdates(this);
                mLocationManager=null;

            }
            isLocationReceived=true;
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
        public void updatedLocation(LatLng location);
    }
}
