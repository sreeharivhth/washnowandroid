package wash.midest.com.mrwashapp.screens.fragmentviews.myorder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderFrag extends BaseFrag implements APICallBack{

    private GeneralListDataPojo mGeneralPojo;
    private static String LANDING_DATA="Data";
    @BindView(R.id.order_list)
    RecyclerView listRecycler;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    private static final String TAG="MyOrderFrag";
    private Unbinder mUnbinder;
    private ListAdapter mListAdapter;
    private boolean isVisible;
    private boolean isDetailAPI;

    public MyOrderFrag() {
        // Required empty public constructor
    }

    public static MyOrderFrag newInstance(){
        MyOrderFrag fragment=new MyOrderFrag();
        Bundle bundle = new Bundle();
        /*bundle.putParcelable(LANDING_DATA, generalPojo);*/
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_order, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.action_orders));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRecycler.setLayoutManager(layoutManager);

        getPriceListData();

        return view;
    }

    void getPriceListData(){
        isDetailAPI=false;
        if(!mAppUtils.isNetworkConnected(getActivity())){
            showMessage(getString(R.string.network_error),R.string.ok,0);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> requestParams=new HashMap<>();
        //TODO keep actual mToken, once handled from backend
        requestParams.put(mApiConstants.API_MEMBERID,mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.getMyOrder(MyOrderFrag.this,requestParams);
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        if(isVisible){
            mProgressBar.setVisibility(View.GONE);
            if(isSuccess) {
                List<Data> dataList = ((GeneralListDataPojo) responseObj).getData();

                if(isDetailAPI){
                    for (int count =0;count<dataList.size();count++) {
                        Log.d(TAG,""+dataList.get(count).getCategoryName());
                        Log.d(TAG,""+dataList.get(count).getProductName());
                        Log.d(TAG,""+dataList.get(count).getCount());
                        Log.d(TAG,""+dataList.get(count).getAmount());
                    }

                    isDetailAPI=false;
                }else{

                    if(dataList.size()>0){
                        mListAdapter = new ListAdapter(dataList);
                        listRecycler.setAdapter(mListAdapter);
                        mListAdapter.notifyDataSetChanged();
                    }else{
                        showMessage("No orders made till now ",R.string.ok,0);
                    }
                }
            }else{
                showMessage(errorMsg,R.string.ok,0);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_list, parent, false);
            ViewHolder viewHolder;
            viewHolder = new ViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position)
        {
            final String orderId = dataList.get(holder.getAdapterPosition()).getOrderId();
            holder.textOrder_num.setText("Order# : "+orderId);

            String time = dataList.get(holder.getAdapterPosition()).getOrderDate();
            time = getFormatedDate(time);
            holder.textDate.setText("Order Date : "+time);

            String status = dataList.get(holder.getAdapterPosition()).getStatus();
            holder.textStatus.setText("Order Status : "+status);

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Log.d(TAG,"Clicked orderId ="+orderId);
                    processDetail(orderId);
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
            @BindView(R.id.order_num)
            TextView textOrder_num;

            @BindView(R.id.date)
            TextView textDate;

            @BindView(R.id.status)
            TextView textStatus;

            ViewHolder(View itemView)
            {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
        private String getFormatedDate(String inputDate){
            //2018-06-06 11:02:17
            String date="";
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date newDate = format.parse(inputDate);

                format = new SimpleDateFormat("dd MMM yyyy");
                date = format.format(newDate);
            } catch (ParseException e) {
                Log.e(TAG,"Date ParseException = "+e.toString());
            }
            return date;
        }
    }

    void processDetail(String orderNum){
        isDetailAPI=true;
        HashMap<String,String> requestParams=new HashMap<>();
        //requestParams.put(mApiConstants.API_ORDER_ID, orderNum);
        requestParams.put(mApiConstants.API_ORDER_ID, "47");
        requestParams.put(mApiConstants.API_MEMBERID,mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.getOrderDetails(MyOrderFrag.this,requestParams);
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
