package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Build;
import android.os.Bundle;
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

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APIConstants;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.PromotionData;
import wash.midest.com.mrwashapp.models.WashTypes;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.ViewPagerFrag;
import wash.midest.com.mrwashapp.screens.fragmentviews.placeorderfrag.PlaceOrderFrag;
import wash.midest.com.mrwashapp.uiwidgets.LandingHorizontalView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LandingFrag extends Fragment implements LandingHorizontalView.ButtonClicked, LandingHorizontalView.PriceListClicked{

    private String TAG="LandingFrag";
    @BindView(R.id.landingScrollLinearView) LinearLayout mScrollLinearView;
    @BindView(R.id.viewpager_home) ViewPager mPager;
    private GeneralListDataPojo mGeneralPojo;
    private PromotionData mPromotionData;
    private static String LANDING_DATA="LandingData";
    private static String PROMOTION_DATA="PromotionData";
    private APIConstants mApiConstants;
    private ArrayList<String> mServices;
    private Unbinder mUnbinder;
    private boolean isVisible;

    public static LandingFrag newInstance(GeneralListDataPojo generalPojo, PromotionData promotionData) {
        LandingFrag fragment = new LandingFrag();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LANDING_DATA, generalPojo);
        bundle.putParcelable(PROMOTION_DATA, promotionData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public LandingFrag() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_landing, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.app_title));
        mGeneralPojo = getArguments().getParcelable(LANDING_DATA);
        mPromotionData = getArguments().getParcelable(PROMOTION_DATA);
        mApiConstants=new APIConstants();

        if(null!=mPromotionData){
            populatePager();
        }
        /*mScrollLinearView= view.findViewById(R.id.landingScrollLinearView);*/
        populateWashTypes();
        return view;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Data> promoData;
        LinkedList<String> dataOne;
        LinkedList<String> dataTwo;
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        public ViewPagerAdapter(FragmentManager fragmentManager,List<Data> _promoData){
            super(fragmentManager);
            promoData = _promoData;
            int size = promoData.size();
            Log.d(TAG,"mPromotionData size = "+size);
            dataOne = new LinkedList<>();
            dataTwo = new LinkedList<>();

            for(int count=0;count<size;count++){
                Data individualData = promoData.get(count);
                dataOne.add(individualData.getOffer()+" QR Off");
                dataTwo.add(individualData.getTitle()+" with code: "+individualData.getCode());
            }
        }
        @Override
        public Fragment getItem(int position) {

            return ViewPagerFrag.newInstance(dataOne.get(position),dataTwo.get(position));
            /*switch (position){
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
            }*/
        }
        @Override
        public int getCount() {
            return promoData.size();
        }
    }
    private void populateWashTypes(){
        Log.d(TAG, " populateWashTypes");

        ArrayList<WashTypes> types = new ArrayList<>();
        if(null!=mGeneralPojo){
            mServices=new ArrayList<String>();
            Log.d(TAG, " populateWashTypes null!=mServicesData");
            Log.d(TAG, " populateWashTypes mServicesData.getData().size() ="+mGeneralPojo.getData().size());
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
            Log.d(TAG," populateWashTypes Adding horizontal news");
            addHorizontalViews(types.size(),types);
        }else{
            Log.d(TAG," mServicesData is null");
        }
    }

    void populatePager(){
        mPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),mPromotionData.getPromotionData()));
    }

    private void addHorizontalViews(int viewCount,ArrayList<WashTypes> types){
        for(int count=0;count<viewCount;count++){
            LandingHorizontalView horizontalView=new LandingHorizontalView(getActivity(),count,types.get(count),this,this);
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
        Log.d(TAG,"Button clicked on index = "+index);
        //Avoided below dut to neglecting backstack by android
        FragmentManager childFragMan = getActivity().getSupportFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        PlaceOrderFrag fragB = PlaceOrderFrag.newInstance(index,mGeneralPojo,null);
        childFragTrans.add(R.id.landing_fragment_id, fragB);
        childFragTrans.addToBackStack("PlaceOrderFrag");
        childFragTrans.commit();
        /*setUserVisibleHint(false);
        fragB.setUserVisibleHint(true);*/
        /*fragB.setUserVisibleHint(true);
        this.setUserVisibleHint(false);*/
    }

    @Override
    public void onPriceListClicked(int index) {
        Log.d(TAG,"onPriceListClicked clicked on index = "+index);
        if(index!=-1){
            ((LandingActivity) getActivity()).pushPriceListWithIndex(index);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
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
        if(isVisibleToUser){
            Log.d(TAG,TAG+" is visible");
        }else{
            Log.d(TAG,TAG+" is NOT visible");
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
