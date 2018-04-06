package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderMapFrag extends Fragment implements OnMapReadyCallback{

    private String TAG=OrderMapFrag.class.getName();
    private Unbinder mUnbinder;
    @BindView(R.id.placeOrderDetailMap)MapView mMapView;

    public OrderMapFrag() {
   }

    public static OrderMapFrag newInstance() {
        OrderMapFrag fragment = new OrderMapFrag();
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
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney")
        );

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
