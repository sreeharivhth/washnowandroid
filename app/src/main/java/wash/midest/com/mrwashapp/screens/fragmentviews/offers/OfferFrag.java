package wash.midest.com.mrwashapp.screens.fragmentviews.offers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.fragmentviews.BaseFrag;
import wash.midest.com.mrwashapp.screens.fragmentviews.myorder.MyOrderFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferFrag extends BaseFrag implements APICallBack {

    private static final String TAG="OfferFrag";
    private GeneralListDataPojo mGeneralPojo;
    private static String LANDING_DATA="Data";
    private ListAdapter mListAdapter;
    @BindView(R.id.offer_list)
    RecyclerView listRecycler;
    private Unbinder mUnbinder;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    boolean isPromoCodeServiceAction;
    private String mPromoCode;
    private boolean isVisible;

    public OfferFrag() {
        // Required empty public constructor
    }

    public static OfferFrag newInstance(){
        OfferFrag fragment=new OfferFrag();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_offer, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        ((LandingActivity)getActivity()).setFragmentTitle(getActivity().getString(R.string.action_offers));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRecycler.setLayoutManager(layoutManager);
        getOfferData();
        return view;
    }

    void getOfferData(){
        isPromoCodeServiceAction=false;
        if(!mAppUtils.isNetworkConnected(getActivity())){
            showMessage(getString(R.string.network_error),R.string.ok,0);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> requestParams=new HashMap<>();
        requestParams.put(mApiConstants.API_MEMBERID,mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.getOffersResponse(OfferFrag.this,requestParams);
    }

    void validateCode(String promoCode,String codeID){
        isPromoCodeServiceAction=true;
        if(!mAppUtils.isNetworkConnected(getActivity())){
            showMessage(getString(R.string.network_error),R.string.ok,0);
            return;
        }
        mPromoCode=promoCode;
        /*String imei = mAppUtils.getDeviceIMEI(getActivity());*/
        String imei = "1122009955";
        mProgressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> requestParams=new HashMap<>();
        requestParams.put(mApiConstants.API_MEMBERID,mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        requestParams.put(mApiConstants.API_PROMOCODE_ID,codeID);
        requestParams.put(mApiConstants.API_IMEI,imei);
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.validatePromoCode(OfferFrag.this,requestParams);
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        if(isVisible) {
            mProgressBar.setVisibility(View.GONE);
            if (isSuccess) {
                List<Data> dataList = ((GeneralListDataPojo) responseObj).getData();
                if (dataList.size() > 0) {
                    if (isPromoCodeServiceAction) {
                        pushOfferFrag(mPromoCode);
                    } else {
                        mListAdapter = new ListAdapter(dataList);
                        listRecycler.setAdapter(mListAdapter);
                        mListAdapter.notifyDataSetChanged();
                    }
                } else {
                    showMessage("No offers available !", R.string.ok, 0);
                }
            } else {
                if (isPromoCodeServiceAction) {
                    showErrorAlert(errorMsg);
                } else {
                    showMessage(errorMsg, R.string.ok, 0);
                }
            }
        }
    }

    /**
     * Adapter for representing list view
     */
    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
        private List<Data> dataList;
        private ListAdapter(List<Data> data)
        {
            this.dataList = data;
        }
        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_list_item, parent, false);
            ListAdapter.ViewHolder viewHolder;
            viewHolder = new ListAdapter.ViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position)
        {
            final String couponCode = dataList.get(holder.getAdapterPosition()).getCode();
            holder.textTitle.setText("Coupon Code : "+couponCode);

            StringBuffer buffer = new StringBuffer();
            buffer.append("Description : ");
            buffer.append(dataList.get(holder.getAdapterPosition()).getTitle() + " get ");
            buffer.append(dataList.get(holder.getAdapterPosition()).getOffer()+" QR Off");

            holder.textOfferDetail.setText(buffer.toString());

            final String codeID = dataList.get(holder.getAdapterPosition()).getPromoCodeId();
            Button applyBtn = holder.btnApply;
            applyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateCode(couponCode,codeID);
                }
            });
        }
        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.offer_title)
            TextView textTitle;

            @BindView(R.id.offer_detail)
            TextView textOfferDetail;

            @BindView(R.id.apply_btn)
            Button btnApply;

            ViewHolder(View itemView)
            {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
    private void pushOfferFrag(String offerCode){
        ((LandingActivity)getActivity()).pushMyOrderFrag(offerCode);
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
}
