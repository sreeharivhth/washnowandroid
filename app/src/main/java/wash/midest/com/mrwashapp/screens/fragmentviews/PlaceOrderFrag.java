package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOrderFrag extends Fragment {


    private int index;
    private static String DATA="DATA";
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
        TextView txtView = view.findViewById(R.id.testTextView);

        index = getArguments().getInt(DATA);

        if(index!=-1){
            txtView.setText("Index received = "+index);
        }
        return view;
    }



}
