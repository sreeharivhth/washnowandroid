package wash.midest.com.mrwashapp.screens.fragmentviews.placeorderfrag;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.DateDifference;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.fragmentviews.BaseFrag;
import wash.midest.com.mrwashapp.screens.fragmentviews.OrderMapFrag;
import wash.midest.com.mrwashapp.utils.AppUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOrderFrag extends BaseFrag implements OnMapReadyCallback,OrderMapFrag.OnLocationSelected,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static String DATA="DATA";
    private static String SERVICES="SERVICES";
    private String TAG=PlaceOrderFrag.class.getName();
    //private int mYear, mMonth, mDay, mHour, mMinute;
    private GeneralListDataPojo mServicesList;
    private ArrayList mServiceNames;
    private ArrayList mDeliveryTime;
    private ArrayList mPickTime;
    private int mSelectedDeliveryTimeMin;
    private int mPickDifferenceHRS;
    private static final int PERMISSIONS_REQUEST_LOCATION = 3981;
    @BindView(R.id.pickDate) TextView mTxtPickDate;
    @BindView(R.id.pickTime) TextView mTxtPickTime;
    @BindView(R.id.deliveryDate) TextView mTxtDeliveryDate;
    @BindView(R.id.deliveryTime) TextView mTxtDeliveryTime;
    @BindView(R.id.location) TextView mTxtLocation;
    @BindView(R.id.landmark) TextInputEditText mTxtLandmark;
    @BindView(R.id.house_flat) TextInputEditText mTxtHouseFlat;
    @BindView(R.id.servicesSpinner) Spinner mServicesPicker;
    @BindView(R.id.placeorder_btn) Button mBtnPlaceOrder;
    @BindView(R.id.placeOrderMap)MapView mMapView;
    private Unbinder mUnbinder;
    /*@BindView(R.id.checkbox)
    CheckBox mCheckBox;*/
    private LatLng mLocation;
    private String mGoogleLocationAdd;
    private GoogleMap mGoogleMap;
    /*boolean mEnableMap;*/
    private Marker mCurrLocationMarker;
    private boolean mIsRestoredFromBackstack;
    private int PLACE_ORDER_STACK_NUMBER=2;
    private Calendar mCPickTime;
    private Calendar mCPickDate;
    private Calendar mCDeliveryTime;
    private Calendar mCDeliveryDate;
    private String mSelectedService;
    private LocationManager mLocationManager;
    private boolean isLocationReceived;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    private Location mLastLocation;
    private boolean isLocationPresent=false;
    private boolean isFirstTime;

    public PlaceOrderFrag() {
    }

    public static PlaceOrderFrag newInstance(int count, GeneralListDataPojo servicesList) {
        PlaceOrderFrag fragment = new PlaceOrderFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA, count);
        bundle.putParcelable(SERVICES,servicesList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsRestoredFromBackstack = false;
        mServiceNames=new ArrayList();
        mDeliveryTime=new ArrayList();
        mPickTime =new ArrayList();
        Log.d(TAG,"PlaceOrderFrag onCreate called ");
        isLocationPresent = mSharedPreference.getPreferenceBool(mSharedPreference.LOCATION_PRESENT);
        isFirstTime=true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_place_order, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mTxtLocation.setEnabled(false);
        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.place_order_title));
        mServicesList = getArguments().getParcelable(SERVICES);

        for(int count=0;count<mServicesList.getData().size();count++){
            Data serviceData = mServicesList.getData().get(count);
            if(serviceData.getActive().equalsIgnoreCase(mApiConstants.STATUS_1)){
                mServiceNames.add(serviceData.getName());
                mDeliveryTime.add(serviceData.getDeliveryTime());
                mPickTime.add(serviceData.getPickupTime());
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mServiceNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mServicesPicker.setAdapter(dataAdapter);
        String currentDate = getCurrentDateTime(false);
        String currentTime = getCurrentDateTime(true);

        mTxtPickDate.setText(currentDate);
        mTxtPickTime.setText(currentTime);

        String deliveryDate = getDeliveryDateTime(false);
        String deliveryTime = getDeliveryDateTime(true);

        mTxtDeliveryDate.setText(deliveryDate);
        mTxtDeliveryTime.setText(deliveryTime);

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
                    Log.d(TAG,"PlaceOrderFrag :: getFragmentManager().addOnBackStackChangedListener called ");
                    int backStackCount = getFragmentManager().getBackStackEntryCount();
                    if(backStackCount==PLACE_ORDER_STACK_NUMBER){
                        updateLocation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Toast.makeText(getActivity(),R.string.tapmap_for_location,Toast.LENGTH_SHORT).show();
        return view;
    }

    void updateLocation(){
        Log.d(TAG,"updateLocation  === ");

        String houseSelected = mSharedPreference.getPreferenceString(mSharedPreference.HOUSE_FLAT);
        if(!TextUtils.isEmpty(houseSelected)){
            mTxtHouseFlat.setText(houseSelected);
        }
        String landmarkSelected = mSharedPreference.getPreferenceString(mSharedPreference.LANDMARK);
        if(!TextUtils.isEmpty(landmarkSelected)){
            mTxtLandmark.setText(landmarkSelected);
        }
        double lat = mSharedPreference.getPreferenceDouble(mSharedPreference.LAT_SELECTED,0.0);
        double lon = mSharedPreference.getPreferenceDouble(mSharedPreference.LON_SELECTED,0.0);
        LatLng latLng=new LatLng(lat,lon);
        mLocation = latLng;
        mGoogleLocationAdd = mSharedPreference.getPreferenceString(mSharedPreference.SELECTED_ADDERSS);

        mTxtLocation.setText(mGoogleLocationAdd);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLocation);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation,16));

        /*if(mLocation!=null && mGoogleMap!=null ){
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

            getAddressFromLocation(mLocation.latitude, mLocation.longitude);
        }else{
            Log.d(TAG,"updateLocation Map or location is null ");
        }*/
    }

    /*@OnClick(R.id.checkbox)
    void onCheckClicked(){
        if(mCheckBox.isChecked()){
            mEnableMap=true;
            mHouseFlat.setEnabled(false);
        }else{
            mEnableMap=false;
            mHouseFlat.setEnabled(true);
        }
    }*/

    @OnItemSelected(R.id.servicesSpinner)
    void spinnerSelectAction(Spinner spinner, int position){
        mSelectedService = (String) spinner.getItemAtPosition(position);
        mSelectedDeliveryTimeMin=Integer.valueOf(String.valueOf(mDeliveryTime.get(position)));
        mPickDifferenceHRS=Integer.valueOf(String.valueOf(mPickTime.get(position)));
        Log.d(TAG,"mSelectedDeliveryTimeMin = "+mSelectedDeliveryTimeMin);
        Log.d(TAG,"itemSelected === "+mSelectedService+"  ||  position ="+position);
    }

    @OnClick({R.id.pickDate})
    void pickDate(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        c.set(mYear, mMonth , mDay, 0, 0, 0);
        mCPickDate=c;
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Log.d(TAG,"PICK || Date = "+dayOfMonth + "-" + (monthOfYear ) + "-" + year);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear , dayOfMonth , 0, 0, 0);
                        mCPickDate=cal;
                        mTxtPickDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick({R.id.deliveryDate})
    void pickDateDelivery(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        c.set(mYear, mMonth , mDay, 0, 0, 0);
        mCDeliveryDate=c;
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Log.d(TAG,"DELIVERY || Date = "+dayOfMonth + "-" + (monthOfYear ) + "-" + year);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear , dayOfMonth, 0, 0, 0);
                        mCDeliveryDate=cal;
                       mTxtDeliveryDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick({R.id.pickTime})
    void pickTime(){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        c.set(0, 0, 0, mHour, mMinute, 0);
        mCPickTime=c;
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        Log.d(TAG,"Time = "+hourOfDay + ":" + minute);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(0, 0, 0, hourOfDay, minute, 0);
                        mCPickTime=cal;
                        SimpleDateFormat df=new SimpleDateFormat("HH:mm");
                        mTxtPickTime.setText(df.format(mCPickTime.getTime()));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @OnClick({R.id.deliveryTime})
    void pickDeliveryTime(){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        c.set(0, 0, 0, mHour, mMinute, 0);
        mCDeliveryTime=c;
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        Log.d(TAG,"Time = "+hourOfDay + ":" + minute);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(0, 0, 0, hourOfDay, minute, 0);
                        mCDeliveryTime=cal;
                        SimpleDateFormat df=new SimpleDateFormat("HH:mm");
                        mTxtDeliveryTime.setText(df.format(mCDeliveryTime.getTime()));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    String getCurrentDateTime(boolean isTime){
        Calendar c = Calendar.getInstance();
        Log.d(TAG,"Current time => "+c.getTime());
        SimpleDateFormat df;
        if(isTime){
            df = new SimpleDateFormat("HH:mm");
        }else{
            df = new SimpleDateFormat("yyyy-MM-dd ");
        }
        return df.format(c.getTime());
    }


    String getDeliveryDateTime(boolean isTime){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY,1);
        c.add(Calendar.DAY_OF_WEEK,1);
        Log.d(TAG,"Current time => "+c.getTime());
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
        LatLng latLng=null;
        if(null!=mLocation){
            latLng = mLocation;
        }else{
            double qatarLat = 25.240530;
            double qatarLon = 51.126810;
            latLng = new LatLng(qatarLat,qatarLon);
        }
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
        isFirstTime=false;
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
            isPermissionRequired();
            status=true;
        }
        return status;
    }
    /*@Override*/
    public void updatedLocation(LatLng location) {
        mLocation=location;
        /*isLocationPresent=true;*/
        Log.d(TAG," updatedLocation PlaceOrderFrag lat = "+location.latitude+" || lon = "+location.longitude);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG," PlaceOrderFrag onResume() ===11 ");

    }

    private void isPermissionRequired() {
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
        /*if(!isLocationPresent) {
            //mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        }else{
            //showToast("We have taken your previous address as pick up and delivery address. You can change if required for this order!");
            String storedAddress = mSharedPreference.getPreferenceString(mSharedPreference.ADDRESS);
            double latitude = mSharedPreference.getPreferenceDouble(mSharedPreference.COORDINATES_LAT,0.0);
            double longitude = mSharedPreference.getPreferenceDouble(mSharedPreference.COORDINATES_LON,0.0);
            if(!TextUtils.isEmpty(storedAddress)){
                mTxtHouseFlat.setText(storedAddress);
            }
            if(latitude!=0.0 && longitude!=0.0){
                setLocationInMap(latitude,longitude);
                mLocation=new LatLng(latitude,longitude);
            }
        }*/
    }

    private void setLocationInMap(double latitude,double longitude){
        /*LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Address");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));*/
    }

    @OnClick(R.id.placeorder_btn)
    void proceedPlaceOrder(){

        if(TextUtils.isEmpty(mTxtHouseFlat.getText().toString())){
            showMessage("Please enter address",R.string.ok);
            return;
        }
        Calendar delivery=Calendar.getInstance();
        delivery.set(Calendar.HOUR_OF_DAY, mCDeliveryTime.get(Calendar.HOUR_OF_DAY));
        delivery.set(Calendar.MINUTE, mCDeliveryTime.get(Calendar.MINUTE));
        delivery.set(Calendar.DAY_OF_MONTH, mCDeliveryDate.get(Calendar.DAY_OF_MONTH));
        delivery.set(Calendar.MONTH, mCDeliveryDate.get(Calendar.MONTH));
        delivery.set(Calendar.YEAR, mCDeliveryDate.get(Calendar.YEAR));

        Calendar pickUp=Calendar.getInstance();
        pickUp.set(Calendar.HOUR_OF_DAY, mCPickTime.get(Calendar.HOUR_OF_DAY));
        pickUp.set(Calendar.MINUTE, mCPickTime.get(Calendar.MINUTE));
        pickUp.set(Calendar.DAY_OF_MONTH, mCPickDate.get(Calendar.DAY_OF_MONTH));
        pickUp.set(Calendar.MONTH, mCPickDate.get(Calendar.MONTH));
        pickUp.set(Calendar.YEAR, mCPickDate.get(Calendar.YEAR));

        /*Log.d(TAG,"mCDeliveryTime.get(Calendar.HOUR_OF_DAY) = "+mCDeliveryTime.get(Calendar.HOUR_OF_DAY));
        Log.d(TAG,"mCDeliveryTime.get(Calendar.MINUTE) = "+mCDeliveryTime.get(Calendar.MINUTE));
        Log.d(TAG,"mCDeliveryDate.get(Calendar.DAY_OF_MONTH) = "+mCDeliveryDate.get(Calendar.DAY_OF_MONTH));
        Log.d(TAG,"mCDeliveryDate.get(Calendar.MONTH) = "+mCDeliveryDate.get(Calendar.MONTH));
        Log.d(TAG,"mCDeliveryDate.get(Calendar.YEAR) = "+mCDeliveryDate.get(Calendar.YEAR));
        Log.d(TAG,"====================================================================");
        Log.d(TAG,"mCPickTime.get(Calendar.HOUR_OF_DAY) = "+mCPickTime.get(Calendar.HOUR_OF_DAY));
        Log.d(TAG,"mCPickTime.get(Calendar.MINUTE) = "+mCPickTime.get(Calendar.MINUTE));
        Log.d(TAG,"mCPickDate.get(Calendar.DAY_OF_MONTH) = "+mCPickDate.get(Calendar.DAY_OF_MONTH));
        Log.d(TAG,"mCPickDate.get(Calendar.MONTH) = "+mCPickDate.get(Calendar.MONTH));
        Log.d(TAG,"mCPickDate.get(Calendar.YEAR) = "+mCPickDate.get(Calendar.YEAR));*/

        Date deliveryDate=delivery.getTime();
        Date pickupDate=pickUp.getTime();
        if(pickupDate.after(deliveryDate)){
            Log.d(TAG,"Pick up is after delivery! Invalid ");
            showMessage(getString(R.string.pick_condition_1),R.string.ok);
            return;
        }else{
            //DateDifference  dateDifference = new AppUtils().printDifference(pickupDate,deliveryDate);

            /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currDateString = df.format(new Date());*/

            //Check if pick up is valid
            if(isPickUpValid(pickupDate)){
                //Pick up is valid , proceed with pick and delivery difference
                Log.d(TAG,"Pick up is valid , proceed with pick and delivery difference");
                if(isDeliveryValid(pickupDate,deliveryDate)){
                    Log.d(TAG,"Delivery and Pick up are Valid , can proceed with order placing");
                    showToast("Can proceed with order");
                    saveAddressLocation();
                }else{
                    showMessage("Delivery and Pick up time difference should be more than "+mSelectedDeliveryTimeMin+" hours for "+mSelectedService + " !",R.string.ok);
                }
            }
        }
    }

    private void saveAddressLocation(){
        Log.d(TAG,"saveAddressLocation() ");
        mSharedPreference.setPreferenceString(mSharedPreference.ADDRESS, mTxtHouseFlat.getText().toString());
        Log.d(TAG,"Updated address");
        if(null !=mLocation){
            mSharedPreference.setPreferenceDouble(mSharedPreference.COORDINATES_LAT,mLocation.latitude);
            mSharedPreference.setPreferenceDouble(mSharedPreference.COORDINATES_LON,mLocation.longitude);

            mSharedPreference.setPreferenceBool(mSharedPreference.LOCATION_PRESENT,true);

            Log.d(TAG,"saveAddressLocation() Locations saved");
        }else{
            Log.d(TAG,"saveAddressLocation() mLocation is NULL");
        }
    }

    private boolean isPickUpValid(Date  pickupDate){
        boolean status;
        DateDifference  diffCurrentPick = new AppUtils().printDifference(Calendar.getInstance().getTime(),pickupDate);
        if(diffCurrentPick.differenceInHours>=mPickDifferenceHRS)
        {
            status=true;
            return status;
        }else{
            status=false;
            showMessage("Pick up time should be more than "+mPickDifferenceHRS+" from current time!",R.string.ok);
            return status;
        }
    }

    private boolean isDeliveryValid(Date pickupDate,Date deliveryDate){
        boolean status=false;
        DateDifference  dateDifference = new AppUtils().printDifference(pickupDate,deliveryDate);
        if(dateDifference.differenceInHours>=mSelectedDeliveryTimeMin){
            status=true;
            return status;
        }else{
            status=false;
            return status;
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onLocationChanged(Location location) {

        /*Log.d(TAG,"isLocationReceived = "+isLocationReceived);
        mProgressBar.setVisibility(View.GONE);

        if(!isLocationReceived){*/
            /*Log.d(TAG,"Inside if loop ");
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            mGoogleMap.clear();
            Log.d(TAG, TAG+"LAT="+String.valueOf(location.getLatitude())+"||LON="+String.valueOf(location.getLongitude()));

            setLocationInMap(location.getLatitude(), location.getLongitude());*/
            /*LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));*/

            /*getAddressFromLocation(location.getLatitude(), location.getLongitude());*/
        /*}else{
            Log.d(TAG,"Inside Else part");
            if(mLocationManager!=null){
                mLocationManager.removeUpdates(this);
                mLocationManager=null;
            }
            isLocationReceived=true;
        }*/
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                /*Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
                }*/

                Address address = addresses.get(0);
                StringBuffer str = new StringBuffer();
                str.append(address.getFeatureName()+", ");
                //str.append("Name:"  + address.getLocality());
                //str.append("Sub-Admin Ares: " + address.getSubAdminArea() );
                str.append( address.getSubLocality()+", " );
                //str.append("Admin Area: " + address.getAdminArea() );
                str.append( address.getThoroughfare() +", ");
                str.append( address.getLocality() +", ");
                str.append(address.getAdminArea() +", ");
                str.append(address.getCountryName() );
                //str.append("Country Code: " + address.getCountryCode() );
                String strAddress = str.toString();
                Log.d(TAG,"Address retrieved == "+strAddress.toString());
                mTxtHouseFlat.setText(strAddress.toString());
            } else {
                mTxtHouseFlat.setText("Searching Current Address");
            }
        } catch (IOException e) {
            e.printStackTrace();
            //printToast("Could not get address..!");
            showToast("Error in Could not get address..!");
        }
    }
}
