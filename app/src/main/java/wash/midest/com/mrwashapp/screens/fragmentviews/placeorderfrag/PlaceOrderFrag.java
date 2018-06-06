package wash.midest.com.mrwashapp.screens.fragmentviews.placeorderfrag;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    @BindView(R.id.landmark)
    TextInputEditText mTxtLandmark;
    @BindView(R.id.house_flat)
    TextInputEditText mTxtHouseFlat;
    @BindView(R.id.servicesSpinner)
    Spinner mServicesPicker;
    @BindView(R.id.placeorder_btn)
    Button mBtnPlaceOrder;
    @BindView(R.id.placeOrderMap)
    MapView mMapView;

    private static String DATA = "DATA";
    private static String SERVICES = "SERVICES";
    private String TAG = PlaceOrderFrag.class.getName();
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

    public PlaceOrderFrag() {
    }

    public static PlaceOrderFrag newInstance(int count, GeneralListDataPojo servicesList) {
        PlaceOrderFrag fragment = new PlaceOrderFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA, count);
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
        mTxtLocation.setEnabled(false);
        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.place_order_title));
        mServicesList = getArguments().getParcelable(SERVICES);
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
        final Calendar c1 = Calendar.getInstance();
        mYear = c1.get(Calendar.YEAR);
        mMonth = c1.get(Calendar.MONTH);
        mDay = c1.get(Calendar.DAY_OF_MONTH);
        c1.set(mYear, mMonth, mDay, 0, 0, 0);
        mCPickDate = mCDeliveryDate = c1;
        final Calendar c2 = Calendar.getInstance();
        mHour = c2.get(Calendar.HOUR_OF_DAY);
        mMinute = c2.get(Calendar.MINUTE);
        c2.set(0, 0, 0, mHour, mMinute, 0);
        mCPickTime = mCDeliveryTime = c2;
        return view;
    }

    void updateLocation() {
        Log.d(TAG, "updateLocation  === ");
        mGoogleLocationAdd = mSharedPreference.getPreferenceString(mSharedPreference.SELECTED_ADDERSS);
        if (!TextUtils.isEmpty(mGoogleLocationAdd)) {
            if (isFirstTime && !isMsgShown) {
                showToast("We have kept your last order address as default address. You can change these as well by clicking on map", Toast.LENGTH_LONG);
                isMsgShown = true;
            }
        }
        double lat = mSharedPreference.getPreferenceDouble(mSharedPreference.LAT_SELECTED, 0.0);
        double lon = mSharedPreference.getPreferenceDouble(mSharedPreference.LON_SELECTED, 0.0);
        LatLng latLng = new LatLng(lat, lon);
        mLocation = latLng;

        String houseSelected = mSharedPreference.getPreferenceString(mSharedPreference.HOUSE_FLAT);
        if (!TextUtils.isEmpty(houseSelected)) {
            mTxtHouseFlat.setText(houseSelected);
        }
        String landmarkSelected = mSharedPreference.getPreferenceString(mSharedPreference.LANDMARK);
        if (!TextUtils.isEmpty(landmarkSelected)) {
            mTxtLandmark.setText(landmarkSelected);
        }
        mTxtLocation.setText(mGoogleLocationAdd);
    }

    @OnItemSelected(R.id.servicesSpinner)
    void spinnerSelectAction(Spinner spinner, int position) {
        mSelectedService = (String) spinner.getItemAtPosition(position);
        mSelectedDeliveryTimeMin = Integer.valueOf(String.valueOf(mDeliveryTime.get(position)));
        mPickDifferenceHRS = Integer.valueOf(String.valueOf(mPickTime.get(position)));
        mSelectedServiceID = Integer.valueOf(String.valueOf(mServiceId.get(position)));
        Log.d(TAG, "mSelectedDeliveryTimeMin = " + mSelectedDeliveryTimeMin);
        Log.d(TAG, "itemSelected === " + mSelectedService + "  ||  position =" + position);
    }

    @OnClick({R.id.pickDate})
    void pickDate() {
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Log.d(TAG, "PICK || Date = " + dayOfMonth + "-" + (monthOfYear) + "-" + year);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        mCPickDate = cal;
                        mTxtPickDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick({R.id.deliveryDate})
    void pickDateDelivery() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Log.d(TAG, "DELIVERY || Date = " + dayOfMonth + "-" + (monthOfYear) + "-" + year);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        mCDeliveryDate = cal;
                        mTxtDeliveryDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick({R.id.pickTime})
    void pickTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        Log.d(TAG, "Time = " + hourOfDay + ":" + minute);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(0, 0, 0, hourOfDay, minute, 0);
                        mCPickTime = cal;
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                        mTxtPickTime.setText(df.format(mCPickTime.getTime()));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @OnClick({R.id.deliveryTime})
    void pickDeliveryTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        Log.d(TAG, "Time = " + hourOfDay + ":" + minute);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(0, 0, 0, hourOfDay, minute, 0);
                        mCDeliveryTime = cal;
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                        mTxtDeliveryTime.setText(df.format(mCDeliveryTime.getTime()));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    String getCurrentDateTime(boolean isTime) {
        Calendar c = Calendar.getInstance();
        Log.d(TAG, "Current time => " + c.getTime());
        SimpleDateFormat df;
        if (isTime) {
            df = new SimpleDateFormat("HH:mm");
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd ");
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
            df = new SimpleDateFormat("HH:mm");
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd ");
        }
        return df.format(c.getTime());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady called");
        LatLng latLng = null;
        if (null != mLocation) {
            latLng = mLocation;
        } else {
            double qatarLat = 25.240530;
            double qatarLon = 51.126810;
            latLng = new LatLng(qatarLat, qatarLon);
        }
        mGoogleMap = googleMap;
        mGoogleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
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

    @OnClick({R.id.placeOrderMap, R.id.location, R.id.locationHeadId})
    void onMapClickEvent() {
        isFirstTime = false;
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
    }

    @OnClick(R.id.placeorder_btn)
    void proceedPlaceOrder() {
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
            showMessage(getString(R.string.pick_condition_1), R.string.ok);
            return;
        } else {
            //Check if pick up is valid
            if (isPickUpValid(pickupDate)) {
                //Pick up is valid , proceed with pick and delivery difference
                Log.d(TAG, "Pick up is valid , proceed with pick and delivery difference");
                if (isDeliveryValid(pickupDate, deliveryDate)) {

                    if (TextUtils.isEmpty(mTxtLocation.getText().toString())) {
                        if (TextUtils.isEmpty(mTxtLandmark.getText().toString())
                                ||
                                TextUtils.isEmpty(mTxtHouseFlat.getText().toString()
                                )) {
                            showMessage("If google address is not present, House and Landmark is mandatory to place order",
                                    R.string.ok);
                        } else {
                            Log.d(TAG, "Delivery and Pick up are Valid , can proceed with order placing");
                            //showToast("Can proceed with order");
                            saveAddressLocation(pickupDate.getTime(), deliveryDate.getTime());
                        }
                    } else {
                        Log.d(TAG, "Delivery and Pick up are Valid , can proceed with order placing");
                        saveAddressLocation(pickupDate.getTime(), deliveryDate.getTime());
                    }
                } else {
                    showMessage("Delivery and Pick up time difference should be more than " + mSelectedDeliveryTimeMin + " hours for " + mSelectedService + " !", R.string.ok);
                }
            }
        }
    }

    private void saveAddressLocation(long pickTime, long deliveryTime) {
        Log.d(TAG, "saveAddressLocation() ");
        mSelectedPickTime = pickTime;
        mSelectedDeliveryTime = deliveryTime;
        if (!TextUtils.isEmpty(mTxtHouseFlat.getText().toString())) {
            mSharedPreference.setPreferenceString(mSharedPreference.ADDRESS, mTxtHouseFlat.getText().toString());
        } else {
            mSharedPreference.setPreferenceString(mSharedPreference.ADDRESS, "");
        }
        if (!TextUtils.isEmpty(mTxtLandmark.getText().toString())) {
            mSharedPreference.setPreferenceString(mSharedPreference.LANDMARK, mTxtLandmark.getText().toString());
        } else {
            mSharedPreference.setPreferenceString(mSharedPreference.LANDMARK, "");
        }
        Log.d(TAG, "Updated address");
        if (null != mLocation) {
            mSharedPreference.setPreferenceDouble(mSharedPreference.COORDINATES_LAT, mLocation.latitude);
            mSharedPreference.setPreferenceDouble(mSharedPreference.COORDINATES_LON, mLocation.longitude);
            mSharedPreference.setPreferenceBool(mSharedPreference.LOCATION_PRESENT, true);
            Log.d(TAG, "saveAddressLocation() Locations saved");
            processPlaceOrderAPI();
        } else {
            Log.d(TAG, "saveAddressLocation() mLocation is NULL");
        }
    }

    private boolean isPickUpValid(Date pickupDate) {
        boolean status;
        DateDifference diffCurrentPick = new AppUtils().printDifference(Calendar.getInstance().getTime(), pickupDate);
        if (diffCurrentPick.differenceInHours >= mPickDifferenceHRS) {
            status = true;
            return status;
        } else {
            status = false;
            showMessage("Pick up time should be more than " + mPickDifferenceHRS + " from current time!", R.string.ok);
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
        /*String imei = mAppUtils.getDeviceIMEI(getActivity());*/
        String imei = "1122009955";
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put(mApiConstants.API_MEMBERID, mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        requestParams.put(mApiConstants.API_SERVICE_TYPE, String.valueOf(mSelectedServiceID));
        requestParams.put(mApiConstants.API_PICKUP_TIME, String.valueOf(mSelectedPickTime));
        requestParams.put(mApiConstants.API_DELIVERY_TIME, String.valueOf(mSelectedDeliveryTime));
        requestParams.put(mApiConstants.API_ADDRESS, mGoogleLocationAdd);
        requestParams.put(mApiConstants.API_BUILDINGNAME, mSharedPreference.getPreferenceString(mSharedPreference.HOUSE_FLAT));
        requestParams.put(mApiConstants.API_LANDMARK, mSharedPreference.getPreferenceString(mSharedPreference.LANDMARK));
        double lat = mSharedPreference.getPreferenceDouble(mSharedPreference.LAT_SELECTED, 0.0);
        double lon = mSharedPreference.getPreferenceDouble(mSharedPreference.LON_SELECTED, 0.0);
        requestParams.put(mApiConstants.API_LATITIDE, String.valueOf(lat));
        requestParams.put(mApiConstants.API_LONGITUDE, String.valueOf(lon));
        requestParams.put(mApiConstants.API_PROMOCODE_ID, "0");
        requestParams.put(mApiConstants.API_IMEI, imei);
        APIProcessor apiProcessor = new APIProcessor();
        apiProcessor.postGenerateOrder(this, requestParams);
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        if (isSuccess) {
            List<Data> dataList = ((GeneralListDataPojo) responseObj).getData();
            if (dataList.size() > 0) {
                if (!TextUtils.isEmpty(dataList.get(0).getOrderId())) {
                    Log.d(TAG, "Order generated with order id = " + dataList.get(0).getOrderId());
                    showMessage("Order successfully generated with order id : " + dataList.get(0).getOrderId(), R.string.ok);
                } else {
                    Log.d(TAG, "Order generated");
                    showMessage("Order successfully generated ", R.string.ok);
                }
            } else {
                showMessage("Could not create order.", R.string.ok);
            }
        } else {
            showMessage(errorMsg, R.string.ok);
        }
    }
}
