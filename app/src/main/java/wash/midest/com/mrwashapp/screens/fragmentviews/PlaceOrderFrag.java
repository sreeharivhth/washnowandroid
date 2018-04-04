package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
public class PlaceOrderFrag extends Fragment implements OnMapReadyCallback{

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
    private Unbinder mUnbinder;
    private SupportMapFragment mMapFragment;
    @BindView(R.id.placeOrderMap)MapView mMapView;

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

        return view;
    }

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

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
