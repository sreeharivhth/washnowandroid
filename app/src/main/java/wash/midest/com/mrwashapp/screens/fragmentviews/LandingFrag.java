package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APIConstants;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.WashTypes;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.ViewPagerFrag;
import wash.midest.com.mrwashapp.uiwidgets.LandingHorizontalView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LandingFrag extends Fragment implements LandingHorizontalView.ButtonClicked{

    private String TAG=LandingFrag.class.getName();
    @BindView(R.id.landingScrollLinearView) LinearLayout mScrollLinearView;
    @BindView(R.id.viewpager_home) ViewPager mPager;

    private GeneralListDataPojo mGeneralPojo;
    private static String LANDING_DATA="LandingData";
    private APIConstants mApiConstants;
    private ArrayList<String> mServices;
    private Unbinder mUnbinder;


    public static LandingFrag newInstance(GeneralListDataPojo generalPojo) {
        LandingFrag fragment = new LandingFrag();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LANDING_DATA, generalPojo);
        fragment.setArguments(bundle);
        return fragment;
    }
    public LandingFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_landing, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.app_title));

        mGeneralPojo= getArguments().getParcelable(LANDING_DATA);
        mApiConstants=new APIConstants();
        /*ViewPager pager =  view.findViewById(R.id.viewpager_home);*/
        mPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        /*mScrollLinearView= view.findViewById(R.id.landingScrollLinearView);*/
        populateWashTypes();
        return view;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return ViewPagerFrag.newInstance("20","ON FIRST ORDER");
                case 1:
                    return ViewPagerFrag.newInstance("30","ON SECOND ORDER");
                case 2:
                    return ViewPagerFrag.newInstance("40","ON THIRD ORDER");
                case 3:
                    return ViewPagerFrag.newInstance("50","ON FOURTH ORDER");
                default:
                    return ViewPagerFrag.newInstance("10","ON FIRST ORDER");
            }
        }
        @Override
        public int getCount() {
            return 4;
        }
    }
    private void populateWashTypes(){
        Log.d(TAG,TAG+" populateWashTypes");

        ArrayList<WashTypes> types = new ArrayList<>();
        if(null!=mGeneralPojo){
            mServices=new ArrayList<String>();
            Log.d(TAG,TAG+" populateWashTypes null!=mServicesData");
            Log.d(TAG,TAG+" populateWashTypes mServicesData.getData().size() ="+mGeneralPojo.getData().size());
            for(int count=0;count<mGeneralPojo.getData().size();count++){
                Data serviceData = mGeneralPojo.getData().get(count);
                if(serviceData.getActive().equalsIgnoreCase(mApiConstants.STATUS_1)){
                    WashTypes washType=new WashTypes();
                    washType.setTime("@ "+serviceData.getDeliveryTime()+" hrs");
                    washType.setWashType(serviceData.getName());
                    types.add(washType);
                    //Add types into mServices for PlaceOrderFrag
                    mServices.add(serviceData.getName());
                }
            }
            Log.d(TAG,TAG+" populateWashTypes Adding horizontal news");
            addHorizontalViews(types.size(),types);
        }else{
            Log.d(TAG,TAG+" mServicesData is null");
        }
    }

    private void addHorizontalViews(int viewCount,ArrayList<WashTypes> types){
        for(int count=0;count<viewCount;count++){
            LandingHorizontalView horizontalView=new LandingHorizontalView(getActivity(),count,types.get(count),this);
            if(count%2!=0){
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        horizontalView.setBackground(getActivity().getDrawable(R.drawable.list_background_one));
                    }else{
                        horizontalView.setBackgroundResource(R.drawable.list_background_one);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        horizontalView.setBackground(getActivity().getDrawable(R.drawable.list_background_two));
                    }else{
                        horizontalView.setBackgroundResource(R.drawable.list_background_two);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mScrollLinearView.addView(horizontalView);
        }
    }

    @Override
    public void onButtonClicked(int index) {
        Log.d(TAG,TAG+"Button clicked on index = "+index);
        //Avoided below dut to neglecting backstack by android
        //FragmentManager childFragMan = getChildFragmentManager();
        FragmentManager childFragMan = getActivity().getSupportFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        PlaceOrderFrag fragB = PlaceOrderFrag.newInstance(index,mServices);
        childFragTrans.add(R.id.landing_fragment_id, fragB);
        childFragTrans.addToBackStack("PlaceOrderFrag");
        childFragTrans.commit();
        fragB.setUserVisibleHint(true);
        this.setUserVisibleHint(false);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
