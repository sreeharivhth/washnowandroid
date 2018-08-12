package wash.midest.com.mrwashapp.screens.fragmentviews.placeorderfrag;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
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
public class PlaceOrderFrag extends BaseFrag implements OnMapReadyCallback, OrderMapFrag.OnLocationSelected, APICallBack {

    //https://stackoverflow.com/questions/16706076/different-resolution-support-android
    //https://stackoverflow.com/questions/32860815/how-to-define-dimens-xml-for-every-different-screen-size-in-android
    private static final int CASE_0 = 0;
    private static final int CASE_1 = 1;
    @BindView(R.id.pickDate)
    TextView mTxtPickDate;
    @BindView(R.id.pickTime)
    TextView mTxtPickTime;
    @BindView(R.id.deliveryDate)
    TextView mTxtDeliveryDate;
    @BindView(R.id.deliveryTime)
    TextView mTxtDeliveryTime;
    @BindView(R.id.location)
    TextView mTxtLocation;
    @BindView(R.id.locationHeadId)
    TextView mLocationHead;
    @BindView(R.id.promocode)
    TextInputEditText mPromoCode;

    @BindView(R.id.promoLayout)
    TextInputLayout mPromoLayout;
    /*@BindView(R.id.landmark)
    TextInputEditText mTxtLandmark;
    @BindView(R.id.house_flat)
    TextInputEditText mTxtHouseFlat;*/
    @BindView(R.id.servicesSpinner)
    Spinner mServicesPicker;
    @BindView(R.id.placeorder_btn)
    Button mBtnPlaceOrder;
    @BindView(R.id.placeOrderMap)
    MapView mMapView;
    @BindView(R.id.locationSeparator)
    View mLocationSeparator;
    private int mServiceTypeDefault=-1;
    private int mServiceDefaultIndex=-1;
    private static String SERVICE_TYPE = "SERVICE_TYPE";
    private static String DATA = "DATA";
    private static String COUPON = "CODE";
    private static String SERVICES = "SERVICES";
    private GeneralListDataPojo mServicesList;
    private ArrayList mServiceNames;
    private ArrayList mDeliveryTime;
    private ArrayList mPickTime;
    private ArrayList mServiceId;
    private int mSelectedServiceID;
    private int mSelectedDeliveryTimeMin;
    private int mPickDifferenceHRS;
    private static final int PERMISSIONS_REQUEST_LOCATION = 3981;
    private Unbinder mUnbinder;
    private LatLng mLocation;
    private String mGoogleLocationAdd;
    private GoogleMap mGoogleMap;
    private int PLACE_ORDER_STACK_NUMBER = 2;
    private Calendar mCPickTime;
    private Calendar mCPickDate;
    private Calendar mCDeliveryTime;
    private Calendar mCDeliveryDate;
    private String mSelectedService;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    private long mSelectedPickTime;
    private long mSelectedDeliveryTime;
    private boolean isMsgShown = false;
    private boolean isFirstTime;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String mCouponCode;
    private String tempGoogleAddress="";
    private String tempHomeAddress="";
    private String tempLandmark="";
    private LatLng tempLocation;
    private static final String TAG="PlaceOrderFrag";
    private boolean isVisible;
    DatePickerDialog datePickerDialog;
    int selectedDay;
    int selectedMonth;
    int selectedYear;

    public PlaceOrderFrag() {
    }

    public static PlaceOrderFrag newInstance(int count, GeneralListDataPojo servicesList,String couponCode) {
        PlaceOrderFrag fragment = new PlaceOrderFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA, count);
        if(!TextUtils.isEmpty(couponCode)){
            bundle.putString(COUPON,couponCode);
        }
        bundle.putParcelable(SERVICES, servicesList);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PlaceOrderFrag newInstance(int count, GeneralListDataPojo servicesList,int serviceSelected) {
        PlaceOrderFrag fragment = new PlaceOrderFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA, count);
        bundle.putInt(SERVICE_TYPE, serviceSelected);
        bundle.putParcelable(SERVICES, servicesList);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(false);
        mServiceNames = new ArrayList();
        mDeliveryTime = new ArrayList();
        mPickTime = new ArrayList();
        mServiceId = new ArrayList();
        Log.d(TAG, "PlaceOrderFrag onCreate called ");
        isFirstTime = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.place_order_title));
        mServicesList = getArguments().getParcelable(SERVICES);
        try {
            mServiceTypeDefault = getArguments().getInt(SERVICE_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mCouponCode = getArguments().getString(COUPON);
            Log.d(TAG, "Coupon Code = " + mCouponCode);
            if (!TextUtils.isEmpty(mCouponCode)) {
                mPromoLayout.setVisibility(View.VISIBLE);
                mPromoCode.setText(mCouponCode);
                mPromoCode.setEnabled(false);
            } else {
                mPromoLayout.setVisibility(View.GONE);
                mCouponCode = null;
            }
        } catch (Exception e) {
            mCouponCode = null;
            mPromoLayout.setVisibility(View.GONE);
            Log.e(TAG, "Coupon code not available = " + e.toString());
        }
        for (int count = 0; count < mServicesList.getData().size(); count++) {
            Data serviceData = mServicesList.getData().get(count);
            if (serviceData.getActive().equalsIgnoreCase(mApiConstants.STATUS_1)) {
                mServiceNames.add(serviceData.getName());
                mDeliveryTime.add(serviceData.getDeliveryTime());
                mPickTime.add(serviceData.getPickupTime());
                mServiceId.add(serviceData.getId());
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mServiceNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mServicesPicker.setAdapter(dataAdapter);
        //set service selected
        if (mServiceTypeDefault != 0 || mServiceTypeDefault != -1){
            mServicesPicker.setSelection(mServiceTypeDefault);
        }
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
        }
        mMapView.getMapAsync(this);
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {
                    int backStackCount = getFragmentManager().getBackStackEntryCount();
                    Log.d(TAG, "PlaceOrderFrag addOnBackStackChangedListener backStackCount =" + backStackCount);
                    if (backStackCount == PLACE_ORDER_STACK_NUMBER) {
                        updateLocation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    void updateLocation() {
        Log.d(TAG, "updateLocation  === ");
        if (!TextUtils.isEmpty(tempGoogleAddress)) {
            if (isMsgShown) {
                mTxtLocation.setText(tempGoogleAddress);
                mLocation = tempLocation;
                reloadMap();
            }
        }
    }

    @Override
    public void updatedLocation(LatLng location,String googleAddress,String house,String landmark) {
        tempGoogleAddress = googleAddress;
        tempHomeAddress = house;
        tempLandmark = landmark;
        tempLocation = location;

    }

    @OnItemSelected(R.id.servicesSpinner)
    void spinnerSelectAction(Spinner spinner, int position) {
        mSelectedService = (String) spinner.getItemAtPosition(position);
        mSelectedDeliveryTimeMin = Integer.valueOf(String.valueOf(mDeliveryTime.get(position)));
        mPickDifferenceHRS = Integer.valueOf(String.valueOf(mPickTime.get(position)));
        mSelectedServiceID = Integer.valueOf(String.valueOf(mServiceId.get(position)));
        Log.d(TAG,"mPickDifferenceHRS = "+mPickDifferenceHRS);
        Log.d(TAG, "mSelectedDeliveryTimeMin = " + mSelectedDeliveryTimeMin);
        Log.d(TAG, "itemSelected === " + mSelectedService + "  ||  position =" + position);

        Calendar c1 = Calendar.getInstance();
        mYear = c1.get(Calendar.YEAR);
        mMonth = c1.get(Calendar.MONTH);
        mDay = c1.get(Calendar.DAY_OF_MONTH);
        c1.set(mYear, mMonth, mDay, 0, 0, 0);

        Calendar c2 = Calendar.getInstance();
        mHour = c2.get(Calendar.HOUR_OF_DAY);
        mMinute = c2.get(Calendar.MINUTE);

        int updatedMins = mHour+mPickDifferenceHRS;
        c2.set(0, 0, 0, updatedMins, mMinute, 0);
        mCPickTime = c2;

        updatedMins = updatedMins+1;
        c2.set(0, 0, 0, updatedMins, mMinute, 0);
        mCDeliveryTime = c2;

        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        mTxtPickTime.setText(df.format(mCPickTime.getTime()));
        mTxtDeliveryTime.setText(df.format(mCDeliveryTime.getTime()));

        Calendar cDelivery = null;
        try {
            String date = getStringDateFormatted(mYear, mMonth, mDay);
            mTxtPickDate.setText(date);

            cDelivery = Calendar.getInstance();
            int delYear = cDelivery.get(Calendar.YEAR);
            int delMonth = cDelivery.get(Calendar.MONTH);
            int delDate = cDelivery.get(Calendar.DAY_OF_MONTH);
            /*cDelivery.add(Calendar.DAY_OF_MONTH,1);*/
            //String deliveryDate = getStringDateFormatted(delYear, delMonth, cDelivery.get(Calendar.DAY_OF_MONTH));
            String deliveryDate = getStringDateFormatted(delYear, delMonth, delDate);
            mTxtDeliveryDate.setText(deliveryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mCPickDate = c1;
        mCDeliveryDate  = cDelivery;
    }

    @OnClick({R.id.pickDate})
    void pickDate() {
        // Get Current Date
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(mYear, mMonth, mDay, 0, 0, 0);

        datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            Log.d(TAG, "PICK || Date = " + dayOfMonth + "-" + (monthOfYear) + "-" + year);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(0);
                            cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                            mCPickDate = cal;
                            String date = getStringDateFormatted(year, monthOfYear, dayOfMonth);
                            mTxtPickDate.setText(date);

                            selectedDay = datePickerDialog.getSelectedDay().getDay();
                            selectedMonth = datePickerDialog.getSelectedDay().getMonth();
                            selectedYear = datePickerDialog.getSelectedDay().getYear();

                            pickTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccentPicker));
//        datePickerDialog.set(R.attr.colorPrimary);
        datePickerDialog.setMinDate(cal);
        datePickerDialog.setTitle("Select Pick Up Date");
        datePickerDialog.show(getActivity().getFragmentManager(),"pickDate");

    }

    @OnClick({R.id.pickTime})
    void pickTime() {
        Calendar cal = Calendar.getInstance();
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minPickTime = hourOfDay+mPickDifferenceHRS;
        TimePickerDialog timePickerDialog =
                TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view,
                                                  int hourOfDay,
                                                  int minute,
                                                  int second) {
                                Log.d("TAG", "inside OnTimeSetListener");
                                Log.d(TAG, "Time = " + hourOfDay + ":" + minute);
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(0);
                                cal.set(0, 0, 0, hourOfDay, minute, 0);
                                mCPickTime = cal;
                                SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
                                mTxtPickTime.setText(df.format(mCPickTime.getTime()));
                                /*pickDateDelivery();*/
                            }
                        }
                        ,hourOfDay,minPickTime,false);
        /*timePickerDialog.setMinTime(new Timepoint(minPickTime,0,0));*/
        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccentPicker));
        timePickerDialog.show(getActivity().getFragmentManager(),"pickTime");
        timePickerDialog.setTitle("Select Pick Up Time");
    }

    @OnClick({R.id.deliveryDate})
    void pickDateDelivery() {
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        if(selectedDay==0|| selectedMonth==0 ||selectedYear==0){

            cal.set(mYear, mMonth, mDay, 0, 0, 0);

        }else{
            cal.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
        }

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            Log.d(TAG, "DELIVERY || Date = " + dayOfMonth + "-" + (monthOfYear) + "-" + year);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(0);
                            cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                            mCDeliveryDate = cal;
                            String date = getStringDateFormatted(year, monthOfYear, dayOfMonth);
                            mTxtDeliveryDate.setText(date);
                            pickDeliveryTime();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        /*mTxtPickDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);*/
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccentPicker));
        datePickerDialog.setMinDate(cal);
        datePickerDialog.setTitle("Select Delivery Date");
        datePickerDialog.show(getActivity().getFragmentManager(),"pickDateDelivery");
    }

    @OnClick({R.id.deliveryTime})
    void pickDeliveryTime() {
        Calendar cal = Calendar.getInstance();
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minDeliveryTime = hourOfDay+mPickDifferenceHRS;//Kept same as pickup difference
        //Validity between pick and delivery difference will be calculated in Button click
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        Log.d(TAG, "Time = " + hourOfDay + ":" + minute);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(0, 0, 0, hourOfDay, minute, 0);
                        mCDeliveryTime = cal;
                        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
                        mTxtDeliveryTime.setText(df.format(mCDeliveryTime.getTime()));
                    }
                }, mHour, minDeliveryTime, false);
        /*timePickerDialog.setMinTime(new Timepoint(minDeliveryTime,0,0));*/
        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccentPicker));
        timePickerDialog.show(getActivity().getFragmentManager(),"pickDeliveryTime");
        timePickerDialog.setTitle("Select Delivery Time");
    }

    private String getStringDateFormatted(int year, int monthOfYear, int dayOfMonth) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String inputDate = new String(year+"-"+(monthOfYear + 1)+"-"+dayOfMonth+" 00:00:00");

        Date newDate = dateFormat.parse(inputDate);
        dateFormat = new SimpleDateFormat("dd MMM yyyy");
        return dateFormat.format(newDate);
    }


    String getCurrentDateTime(boolean isTime) {
        Calendar c = Calendar.getInstance();
        Log.d(TAG, "Current time => " + c.getTime());
        SimpleDateFormat df;
        if (isTime) {
            df = new SimpleDateFormat("hh:mm a");
        } else {
            df = new SimpleDateFormat("dd MMM yyyy");
        }
        return df.format(c.getTime());
    }

    String getDeliveryDateTime(boolean isTime) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 1);
        c.add(Calendar.DAY_OF_WEEK, 1);
        Log.d(TAG, "Current time => " + c.getTime());
        SimpleDateFormat df;
        if (isTime) {
            df = new SimpleDateFormat("hh:mm a");
        } else {
            df = new SimpleDateFormat("dd MMM yyyy");
        }
        return df.format(c.getTime());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady called");
        mGoogleMap = googleMap;
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
        /*mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);*/
    }

    void reloadMap(){
        try {
            LatLng latLng;
            if (null != mLocation) {
                latLng = mLocation;
            } else {
                double qatarLat = 25.240530;
                double qatarLon = 51.126810;
                latLng = new LatLng(qatarLat, qatarLon);
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } catch (Exception e) {
            Log.e(TAG,"reloadMap() EXCE = "+e.toString());
        }
    }

    @OnClick({R.id.placeOrderMap, R.id.locationHeadId , R.id.locationSeparator ,R.id.location})
    void onMapClickEvent() {
        Log.d(TAG,"onMapClickEvent() called !!!");
        if(isFirstTime){
            isMsgShown = true;
            isFirstTime = false;

            /*mGoogleLocationAdd = mSharedPreference.getPreferenceString(mSharedPreference.SELECTED_ADDERSS);*/
            if(!TextUtils.isEmpty(mSharedPreference.getPreferenceString(mSharedPreference.ADDRESS)))
            {
                showMessage("Would you like to show the details of last order?",R.string.ok,R.string.cancel, CASE_1);
            }
            else
            {
                pushMapFrag();
            }
        }else{
            pushMapFrag();
        }
    }

    void pushMapFrag(){
        if (isLocationEnabled()) {
            FragmentManager childFragMan = getActivity().getSupportFragmentManager();
            FragmentTransaction childFragTrans = childFragMan.beginTransaction();
            OrderMapFrag frag = OrderMapFrag.newInstance(this);
            childFragTrans.add(R.id.place_order_frag, frag,"OrderMapFrag");
            childFragTrans.addToBackStack(null);
            childFragTrans.commit();
            frag.setUserVisibleHint(true);

        }
    }

    boolean isLocationEnabled() {
        boolean status;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
        if (!gps_enabled && !network_enabled) {
            status = false;
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
        } else {
            isPermissionRequired();
            status = true;
        }
        return status;
    }
    private void isPermissionRequired() {
        boolean isPermissionRequired = new AppUtils().isVersionGreaterThanM(getActivity().getApplicationContext());
        /*if (isPermissionRequired) {
            checkPermission();
        } else {
            postPermissionGranted();
        }*/
        postPermissionGranted();
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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

    private void postPermissionGranted() {
        /*if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/

    }

    @OnClick(R.id.placeorder_btn)
    void proceedPlaceOrder() {

        if (TextUtils.isEmpty(mTxtLocation.getText().toString())) {
            if (TextUtils.isEmpty(tempLandmark)
                    ||
                    TextUtils.isEmpty(tempHomeAddress)) {
                showMessage("If Location is not present, Please select map and enter House and Landmark to place order!",
                        R.string.ok, CASE_0);

                return;
            } /*else {
                Log.d(TAG, "Delivery and Pick up are Valid , can proceed with order placing");
                //showToast("Can proceed with order");
                //saveAddressLocation(pickupDate.getTime(), deliveryDate.getTime());
            }*/
        } /*else {
            Log.d(TAG, "Delivery and Pick up are Valid , can proceed with order placing");
            //saveAddressLocation(pickupDate.getTime(), deliveryDate.getTime());
        }*/


        Calendar delivery = Calendar.getInstance();
        delivery.set(Calendar.HOUR_OF_DAY, mCDeliveryTime.get(Calendar.HOUR_OF_DAY));
        delivery.set(Calendar.MINUTE, mCDeliveryTime.get(Calendar.MINUTE));
        delivery.set(Calendar.DAY_OF_MONTH, mCDeliveryDate.get(Calendar.DAY_OF_MONTH));
        delivery.set(Calendar.MONTH, mCDeliveryDate.get(Calendar.MONTH));
        delivery.set(Calendar.YEAR, mCDeliveryDate.get(Calendar.YEAR));

        Calendar pickUp = Calendar.getInstance();
        pickUp.set(Calendar.HOUR_OF_DAY, mCPickTime.get(Calendar.HOUR_OF_DAY));
        pickUp.set(Calendar.MINUTE, mCPickTime.get(Calendar.MINUTE));
        pickUp.set(Calendar.DAY_OF_MONTH, mCPickDate.get(Calendar.DAY_OF_MONTH));
        pickUp.set(Calendar.MONTH, mCPickDate.get(Calendar.MONTH));
        pickUp.set(Calendar.YEAR, mCPickDate.get(Calendar.YEAR));

        Date deliveryDate = delivery.getTime();
        Date pickupDate = pickUp.getTime();
        if (pickupDate.after(deliveryDate)) {
            Log.d(TAG, "Pick up is after delivery! Invalid ");
            showMessage(getString(R.string.pick_condition_1), R.string.ok, CASE_0);
            return;
        } else {
            //Check if pick up is valid
            if (isPickUpValid(pickupDate)) {
                //Pick up is valid , proceed with pick and delivery difference
                Log.d(TAG, "Pick up is valid , proceed with pick and delivery difference");
                if (isDeliveryValid(pickupDate, deliveryDate)) {
                    saveAddressLocation(pickupDate.getTime(), deliveryDate.getTime());
                } else {
                    showMessage("Delivery and Pick up time difference should be more than " + mSelectedDeliveryTimeMin + " hours for " + mSelectedService + " !",
                            R.string.ok, CASE_0);
                }
            }
        }
    }

    private void saveAddressLocation(long pickTime, long deliveryTime) {
        Log.d(TAG, "saveAddressLocation() ");
        mSelectedPickTime = pickTime;
        mSelectedDeliveryTime = deliveryTime;
        if (!TextUtils.isEmpty(tempGoogleAddress)) {
            mSharedPreference.setPreferenceString(mSharedPreference.ADDRESS, tempGoogleAddress);
        } else if(!TextUtils.isEmpty(mSharedPreference.getPreferenceString(mSharedPreference.ADDRESS))){
            mSharedPreference.setPreferenceString(mSharedPreference.ADDRESS, mSharedPreference.getPreferenceString(mSharedPreference.ADDRESS));
        }else{
            mSharedPreference.setPreferenceString(mSharedPreference.ADDRESS, "");
        }
        if (!TextUtils.isEmpty(tempLandmark)) {
            mSharedPreference.setPreferenceString(mSharedPreference.LANDMARK, tempLandmark);
        } else {
            mSharedPreference.setPreferenceString(mSharedPreference.LANDMARK, "");
        }
        if (!TextUtils.isEmpty(tempHomeAddress)) {
            mSharedPreference.setPreferenceString(mSharedPreference.HOUSE_FLAT, tempHomeAddress);
        } else {
            mSharedPreference.setPreferenceString(mSharedPreference.HOUSE_FLAT, "");
        }
        Log.d(TAG, "Updated address");
        if (null != mLocation) {
            mSharedPreference.setPreferenceDouble(mSharedPreference.COORDINATES_LAT, mLocation.latitude);
            mSharedPreference.setPreferenceDouble(mSharedPreference.COORDINATES_LON, mLocation.longitude);
            mSharedPreference.setPreferenceBool(mSharedPreference.LOCATION_PRESENT, true);
            Log.d(TAG, "saveAddressLocation() Locations saved");

        } else {
            Log.d(TAG, "saveAddressLocation() mLocation is NULL");
        }
        processPlaceOrderAPI();
    }

    private boolean isPickUpValid(Date pickupDate) {
        boolean status;
        DateDifference diffCurrentPick = new AppUtils().printDifference(Calendar.getInstance().getTime(), pickupDate);
        if (diffCurrentPick.differenceInHours >= mPickDifferenceHRS) {
            status = true;
            return status;
        } else {
            status = false;
            showMessage("Pick up time should be more than " + mPickDifferenceHRS + " from current time!", R.string.ok, CASE_0);
            return status;
        }
    }

    private boolean isDeliveryValid(Date pickupDate, Date deliveryDate) {
        boolean status = false;
        DateDifference dateDifference = new AppUtils().printDifference(pickupDate, deliveryDate);
        if (dateDifference.differenceInHours >= mSelectedDeliveryTimeMin) {
            status = true;
            return status;
        } else {
            status = false;
            return status;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    void processPlaceOrderAPI() {
        if (!mAppUtils.isNetworkConnected(getActivity())) {
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        /*String imei = mAppUtils.getDeviceIMEI(getActivity());*/
        String imei = "1122009955";
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put(mApiConstants.API_MEMBERID, mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        requestParams.put(mApiConstants.API_SERVICE_TYPE, String.valueOf(mSelectedServiceID));
        requestParams.put(mApiConstants.API_PICKUP_TIME, String.valueOf(mSelectedPickTime));
        requestParams.put(mApiConstants.API_DELIVERY_TIME, String.valueOf(mSelectedDeliveryTime));
        requestParams.put(mApiConstants.API_ADDRESS, mSharedPreference.getPreferenceString(mSharedPreference.ADDRESS));
        requestParams.put(mApiConstants.API_BUILDINGNAME, mSharedPreference.getPreferenceString(mSharedPreference.HOUSE_FLAT));
        requestParams.put(mApiConstants.API_LANDMARK, mSharedPreference.getPreferenceString(mSharedPreference.LANDMARK));
        double lat = mSharedPreference.getPreferenceDouble(mSharedPreference.COORDINATES_LAT, 0.0);
        double lon = mSharedPreference.getPreferenceDouble(mSharedPreference.COORDINATES_LON, 0.0);
        requestParams.put(mApiConstants.API_LATITIDE, String.valueOf(lat));
        requestParams.put(mApiConstants.API_LONGITUDE, String.valueOf(lon));

        if(!TextUtils.isEmpty(mCouponCode))
            requestParams.put(mApiConstants.API_PROMOCODE_ID, mCouponCode);
        else
            requestParams.put(mApiConstants.API_PROMOCODE_ID, "0");
        requestParams.put(mApiConstants.API_IMEI, imei);
        APIProcessor apiProcessor = new APIProcessor();
        apiProcessor.postGenerateOrder(this, requestParams);
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        if(isVisible){
            mProgressBar.setVisibility(View.INVISIBLE);
            if (isSuccess) {
                List<Data> dataList = ((GeneralListDataPojo) responseObj).getData();
                if (dataList.size() > 0) {

                    if (!TextUtils.isEmpty(dataList.get(0).getOrderId())) {
                        Log.d(TAG, "Order generated with order id = " + dataList.get(0).getOrderId());
                        //showMessage("Order successfully generated with order id : " + dataList.get(0).getOrderId(), R.string.ok, CASE_0);
                    } else {
                        Log.d(TAG, "Order generated");
                        //showMessage("Order successfully generated ", R.string.ok, CASE_0);
                    }

                    pushOrderSuccessFrag();

                } else {
                    showMessage("Could not create order.", R.string.ok, CASE_0);
                }
            } else {
                showMessage(errorMsg, R.string.ok, CASE_0);
            }
        }
    }

    void pushOrderSuccessFrag(){
        FragmentManager childFragMan = getActivity().getSupportFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        OrderSuccessFrag frag = OrderSuccessFrag.newInstance();
        childFragTrans.add(R.id.place_order_frag, frag);
        childFragTrans.addToBackStack("OrderSuccessFrag");
        childFragTrans.commit();
        frag.setUserVisibleHint(true);
        this.setUserVisibleHint(false);

    }
    @Override
    public void handleNegativeAlertCallBack(int caseNum) {
        Log.d(TAG,"handleNegativeAlertCallBack");
        if(caseNum == CASE_1) {
            pushMapFrag();
        }
    }

    @Override
    public void handlePositiveAlertCallBack(int caseNum) {
        Log.d(TAG,"handlePositiveAlertCallBack");
        if(caseNum == CASE_1){
            mGoogleLocationAdd = mSharedPreference.getPreferenceString(mSharedPreference.ADDRESS);
            tempLandmark = mSharedPreference.getPreferenceString(mSharedPreference.LANDMARK);
            tempHomeAddress = mSharedPreference.getPreferenceString(mSharedPreference.HOUSE_FLAT);
            mTxtLocation.setText(mGoogleLocationAdd);
            double lat = mSharedPreference.getPreferenceDouble(mSharedPreference.COORDINATES_LAT, 0.0);
            double lon = mSharedPreference.getPreferenceDouble(mSharedPreference.COORDINATES_LON, 0.0);
            LatLng latLng = new LatLng(lat, lon);

            mLocation = latLng;
            reloadMap();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        isVisible=true;
        Log.d(TAG,"onResume "+TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible=false;
        Log.d(TAG,"onPause "+TAG);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,TAG+"121 setUserVisibleHint called ");
        if(isVisibleToUser){
            Log.d(TAG,TAG+"121 is visible");
            try {
                ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.place_order_title));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart "+TAG);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG,"onViewStateRestored "+TAG);
    }
}
