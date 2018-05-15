package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;



/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOrderFrag extends Fragment implements OnMapReadyCallback,OrderMapFrag.OnLocationSelected{

    private static String DATA="DATA";
    private static String SERVICES="SERVICES";
    private String TAG=PlaceOrderFrag.class.getName();
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ArrayList mServicesList;
    @BindView(R.id.pickDate) TextView mTxtPickDate;
    @BindView(R.id.pickTime) TextView mTxtPickTime;
    @BindView(R.id.deliveryDate) TextView mTxtDeliveryDate;
    @BindView(R.id.deliveryTime) TextView mTxtDeliveryTime;
    @BindView(R.id.servicesSpinner) Spinner mServicesPicker;
    @BindView(R.id.address) TextInputEditText mAddress;
    private Unbinder mUnbinder;
    @BindView(R.id.placeOrderMap)MapView mMapView;
    /*@BindView(R.id.checkbox)
    CheckBox mCheckBox;*/
    private LatLng mLocation;
    private GoogleMap mGoogleMap;
    /*boolean mEnableMap;*/
    private Marker mCurrLocationMarker;
    private boolean mIsRestoredFromBackstack;
    private int PLACE_ORDER_STACK_NUMBER=2;

    public PlaceOrderFrag() {
    }

    public static PlaceOrderFrag newInstance(int count, ArrayList servicesList) {
        PlaceOrderFrag fragment = new PlaceOrderFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA, count);
        bundle.putStringArrayList(SERVICES,servicesList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsRestoredFromBackstack = false;
        Log.d(TAG,"PlaceOrderFrag onCreate called ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_place_order, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mServicesList = getArguments().getStringArrayList(SERVICES);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mServicesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mServicesPicker.setAdapter(dataAdapter);
        String currentDate = getCurrentDateTime(false);
        String currentTime = getCurrentDateTime(true);

        mTxtPickDate.setText(currentDate);
        mTxtPickTime.setText(currentTime);

        mTxtDeliveryDate.setText(currentDate);
        mTxtDeliveryTime.setText(currentTime);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {
                    int backStackCount = getFragmentManager().getBackStackEntryCount();
                    if(backStackCount==PLACE_ORDER_STACK_NUMBER){
                        updateLocation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Toast.makeText(getActivity(),R.string.tapmap_for_location,Toast.LENGTH_SHORT).show();
        return view;
    }

    void updateLocation(){
        Log.d(TAG,"updateLocation  === ");
        if(mLocation!=null && mGoogleMap!=null ){
            Log.d(TAG,"updateLocation mLocation and mGoogleMap are valid");
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            //LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLocation);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation,16));
            Log.d(TAG,"!!updateLocation  Updated marker !!");
        }else{
            Log.d(TAG,"updateLocation Map or location is null ");
        }
    }

    /*@OnClick(R.id.checkbox)
    void onCheckClicked(){
        if(mCheckBox.isChecked()){
            mEnableMap=true;
            mAddress.setEnabled(false);
        }else{
            mEnableMap=false;
            mAddress.setEnabled(true);
        }
    }*/

    @OnItemSelected(R.id.servicesSpinner)
    void spinnerSelectAction(Spinner spinner, int position){
        String itemSelected = (String) spinner.getItemAtPosition(position);
        Log.d(TAG,"itemSelected === "+itemSelected);
    }

    @OnClick(R.id.pickDate)
    void pickDate(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Log.d(TAG,"Date = "+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.pickTime)
    void pickTime(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        /*txtTime.setText(hourOfDay + ":" + minute);*/
                        Log.d(TAG,"Time = "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    String getCurrentDateTime(boolean isTime){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        SimpleDateFormat df;
        if(isTime){
            df = new SimpleDateFormat("HH:mm");
        }else{
            df = new SimpleDateFormat("yyyy-MM-dd ");
        }
        return df.format(c.getTime());
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG,"onMapReady called");
        double qatarLat = 25.240530;
        double qatarLon = 51.126810;
        LatLng latLng = new LatLng(qatarLat,qatarLon);
        mGoogleMap=googleMap;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Click to tag location also");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));
        /*mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));*/
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                onMapClickEvent();
            }
        });
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                onMapClickEvent();
                return true;
            }
        });
    }

    @OnClick(R.id.placeOrderMap)
    void onMapClickEvent(){
        //Avoided below dut to neglecting backstack by android
        //FragmentManager childFragMan = getChildFragmentManager();
        /*if(mEnableMap) {*/
            if (isLocationEnabled()) {
                FragmentManager childFragMan = getActivity().getSupportFragmentManager();
                FragmentTransaction childFragTrans = childFragMan.beginTransaction();
                OrderMapFrag frag = OrderMapFrag.newInstance(this);
                childFragTrans.add(R.id.place_order_frag, frag);
                childFragTrans.addToBackStack("OrderMapFrag");
                childFragTrans.commit();
                frag.setUserVisibleHint(true);
                this.setUserVisibleHint(false);
            }
        /*}*/
    }

    boolean isLocationEnabled(){
        boolean status=false;
        boolean gps_enabled=false;
        boolean network_enabled=false;
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!gps_enabled && !network_enabled){
            status=false;
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();


        }else{
            status=true;
        }
        return status;
    }
    @Override
    public void updatedLocation(LatLng location) {
        mLocation=location;
        Log.d(TAG," updatedLocation PlaceOrderFrag lat = "+location.latitude+" || lon = "+location.longitude);
        /*if(isResumed() && getUserVisibleHint()){

        }else{
            Log.d(TAG," PlaceOrderFrag  not onResumed");
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG," PlaceOrderFrag onResume() === ");
        if(mIsRestoredFromBackstack)
        {

        }else{
            Log.d(TAG," onResume mIsRestoredFromBackstack === "+mIsRestoredFromBackstack);
        }
    }



    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
