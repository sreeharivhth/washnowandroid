package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APIConstants;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.WashTypes;
import wash.midest.com.mrwashapp.screens.ViewPagerFrag;
import wash.midest.com.mrwashapp.uiwidgets.LandingHorizontalView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LandingFrag extends Fragment {

    private String TAG=LandingFrag.class.getName();
    private LinearLayout mScrollLinearView;
    private GeneralListDataPojo mGeneralPojo;
    private static String LANDING_DATA="LandingData";
    private APIConstants mApiConstants;

    public static LandingFrag newInstance(GeneralListDataPojo generalPojo) {
        LandingFrag fragment = new LandingFrag();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LANDING_DATA, generalPojo);
        fragment.setArguments(bundle);

        return fragment;
    }
    public LandingFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_landing, container, false);
        mGeneralPojo= getArguments().getParcelable(LANDING_DATA);
        mApiConstants=new APIConstants();
        ViewPager pager = (ViewPager) view.findViewById(R.id.viewpager_home);
        pager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager()));
        mScrollLinearView=(LinearLayout) view.findViewById(R.id.landingScrollLinearView);
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
                    return ViewPagerFrag.newInstance("def","def");
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
            Log.d(TAG,TAG+" populateWashTypes null!=mServicesData");
            Log.d(TAG,TAG+" populateWashTypes mServicesData.getData().size() ="+mGeneralPojo.getData().size());
            for(int count=0;count<mGeneralPojo.getData().size();count++){
                Data serviceData = mGeneralPojo.getData().get(count);
                if(serviceData.getActive().equalsIgnoreCase(mApiConstants.STATUS_1)){
                    WashTypes washType=new WashTypes();
                    washType.setTime("@ "+serviceData.getDeliveryTime()+" hrs");
                    washType.setWashType(serviceData.getName());
                    types.add(washType);
                }
            }
            Log.d(TAG,TAG+" populateWashTypes Adding horizontal news");
            addHorizontalViews(types.size(),types);
        }else{
            Log.d(TAG,TAG+" mServicesData is null");
        }
        /*WashTypes washType1=new WashTypes();
        washType1.setTime("@24hrs");
        washType1.setWashType("QUICK WASH");

        WashTypes washType2=new WashTypes();
        washType2.setTime("@48hrs");
        washType2.setWashType("NORMAL WASH");

        WashTypes washType3=new WashTypes();
        washType3.setTime("@24hrs");
        washType3.setWashType("QUICK IRON");

        WashTypes washType4=new WashTypes();
        washType4.setTime("@48hrs");
        washType4.setWashType("NORMAL IRON");

        types.add(washType1);
        types.add(washType2);
        types.add(washType3);
        types.add(washType4);*/
    }

    private void addHorizontalViews(int viewCount,ArrayList<WashTypes> types){
        for(int count=0;count<viewCount;count++){
            LandingHorizontalView horizontalView=new LandingHorizontalView(getActivity(),count,types.get(count));
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
}
