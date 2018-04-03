package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;



/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOrderFrag extends Fragment implements View.OnClickListener{


    private int index;
    private static String DATA="DATA";
    private String TAG=PlaceOrderFrag.class.getName();
    private int mYear, mMonth, mDay, mHour, mMinute;

    public PlaceOrderFrag() {
    }

    public static PlaceOrderFrag newInstance(int count) {
        PlaceOrderFrag fragment = new PlaceOrderFrag();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA, count);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_place_order, container, false);
        TextView txtPickDate = view.findViewById(R.id.pickDate);
        txtPickDate.setOnClickListener(this);
        TextView txtPickTime = view.findViewById(R.id.pickTime);
        txtPickTime.setOnClickListener(this);
        //https://www.journaldev.com/9976/android-date-time-picker-dialog

        index = getArguments().getInt(DATA);

        /*if(index!=-1){
            txtView.setText("Index received = "+index);
        }*/
        return view;
    }

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
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.pickDate){
            pickDate();
        }else if(v.getId()==R.id.pickTime){
            pickTime();
        }
    }
}
