package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wash.midest.com.mrwashapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOrderFrag extends Fragment {


    public PlaceOrderFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_order, container, false);
    }

}
