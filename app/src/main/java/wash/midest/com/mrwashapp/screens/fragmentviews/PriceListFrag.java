package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.models.WashTypes;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.customviews.PriceListRowView;
import wash.midest.com.mrwashapp.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PriceListFrag extends BaseFrag implements APICallBack{

    private LinearLayout listComponent;
    private static final String TAG="PriceListFrag";
    private static String SERVICE_DATA="ServiceData";
    private Unbinder mUnbinder;
    private ArrayList<String> mServiceTypes;
    private ArrayList<WashTypes> mAllService;
    @BindView(R.id.serviceType)
    Spinner mSpinner;

    public PriceListFrag() {
        // Required empty public constructor
    }

    public static PriceListFrag newInstance(ArrayList<WashTypes> serviceTypes){

        PriceListFrag priceListFrag=new PriceListFrag();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SERVICE_DATA, serviceTypes);
        priceListFrag.setArguments(bundle);
        return priceListFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_price_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.action_price_list));

        mAllService= getArguments().getParcelableArrayList(SERVICE_DATA);
        listComponent=(LinearLayout)view.findViewById(R.id.list_component);

        if(mAllService!=null && mAllService.size()>0){
            addListComponents();
        }
        return view;
    }

    void getPriceListData(int type){
        Log.d(TAG,"type selected = "+type);
        if(!mAppUtils.isNetworkConnected(getActivity())){
            //showMessage(getString(R.string.network_error),R.string.retry,R.string.cancel);
            showMessage(getString(R.string.network_error),R.string.ok);
            return;
        }
        HashMap<String,String> requestParams=new HashMap<>();
        //TODO keep actual mToken, once handled from backend
        requestParams.put(mApiConstants.API_ACCESSTOKEN,"");
        requestParams.put(mApiConstants.API_TYPE,String.valueOf(type));
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.getPriceList(PriceListFrag.this,requestParams);
    }

    @OnItemSelected(R.id.serviceType)
    void spinnerSelectAction(Spinner spinner, int position){
        String itemSelected = (String) spinner.getItemAtPosition(position);

        WashTypes washTypes = mAllService.get(position);
        int idSel = washTypes.getId();
        Log.d(TAG,"itemSelected === "+itemSelected);
        Log.d(TAG,"Selected ID === "+idSel);
        getPriceListData(idSel);
    }

    void addListComponents(){
        mServiceTypes=new ArrayList<>();
        for(int count=0;count<mAllService.size();count++){
            mServiceTypes.add(mAllService.get(count).getWashType());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mServiceTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        if(isSuccess) {
            GeneralPojo responsePojo = (GeneralPojo) responseObj;
            /*Data data = ((GeneralPojo) responseObj).getData().get(0);*/

        }else{
            showMessage(errorMsg,R.string.ok);
        }
    }
}
