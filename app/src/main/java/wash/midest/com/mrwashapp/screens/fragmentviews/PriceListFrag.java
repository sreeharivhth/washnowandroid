package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;

import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
import wash.midest.com.mrwashapp.screens.customviews.PriceListRowView;
import wash.midest.com.mrwashapp.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PriceListFrag extends BaseFrag implements APICallBack{

    private LinearLayout listComponent;

    public PriceListFrag() {
        // Required empty public constructor
    }

    public static PriceListFrag newInstance(){
        PriceListFrag priceListFrag=new PriceListFrag();
        return priceListFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_price_list, container, false);
        listComponent=(LinearLayout)view.findViewById(R.id.list_component);
        addListComponents();
        return view;
    }

    void getPriceListData(){
        if(!mAppUtils.isNetworkConnected(getActivity())){
            showMessage(getString(R.string.network_error),R.string.retry,R.string.cancel);
            /*mSwipeRefresh.setRefreshing(false);*/
            return;
        }
        HashMap<String,String> requestParams=new HashMap<>();
        new APIProcessor().processPriceList(this,requestParams);
        //processPriceList
    }

    void addListComponents(){
        /*for(int i=0;i<4;i++){
            PriceListRowView priceListRowView=new PriceListRowView(getContext());
            listComponent.addView(priceListRowView);
        }*/
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {

    }
}
