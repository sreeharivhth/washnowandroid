package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.models.Gents;
import wash.midest.com.mrwashapp.models.Others;
import wash.midest.com.mrwashapp.models.WashTypes;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.customviews.PriceListRowView;
import wash.midest.com.mrwashapp.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PriceListFrag extends BaseFrag implements APICallBack{

    @BindView(R.id.list_gents)
    RecyclerView listRecyclerGents;

    @BindView(R.id.list_others)
    RecyclerView listRecyclerOthers;

    @BindView(R.id.list_ladies)
    RecyclerView listRecyclerLadies;

    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    private ListAdapter mListAdapter;
    private static final String TAG="PriceListFrag";
    private static String SERVICE_DATA="ServiceData";
    private static String SELECTED_INDEX="SelectedIndex";
    private Unbinder mUnbinder;
    private ArrayList<String> mServiceTypes;
    private ArrayList<WashTypes> mAllService;
    @BindView(R.id.serviceType)
    Spinner mSpinner;
    private final String CURRENCY = "QR";
    private int mSelectedIndex=-1;

    public PriceListFrag() {
        // Required empty public constructor
    }

    public static PriceListFrag newInstance(ArrayList<WashTypes> serviceTypes,int selectedPos){

        PriceListFrag priceListFrag=new PriceListFrag();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SERVICE_DATA, serviceTypes);
        bundle.putInt(SELECTED_INDEX,selectedPos);
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
        mSelectedIndex=getArguments().getInt(SELECTED_INDEX);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRecyclerGents.setLayoutManager(layoutManager);
        if(mAllService!=null && mAllService.size()>0){
            addListComponents();
        }
        return view;
    }

    @OnItemSelected(R.id.serviceType)
    void spinnerSelectAction(Spinner spinner, int position){
        try {
            String itemSelected = (String) spinner.getItemAtPosition(position);
            WashTypes washTypes = mAllService.get(position);
            int idSel = washTypes.getId();
            Log.d(TAG,"itemSelected === "+itemSelected);
            Log.d(TAG,"Selected ID === "+idSel);
            getPriceListData(idSel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getPriceListData(int type){
        Log.d(TAG,"type selected = "+type);
        if(!mAppUtils.isNetworkConnected(getActivity())){
            showMessage(getString(R.string.network_error),R.string.ok);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> requestParams=new HashMap<>();
        //TODO keep actual mToken, once handled from backend
        requestParams.put(mApiConstants.API_ACCESSTOKEN,"");
        requestParams.put(mApiConstants.API_TYPE,String.valueOf(type));
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.getPriceList(PriceListFrag.this,requestParams);
    }

    void addListComponents(){
        mServiceTypes=new ArrayList<>();
        for(int count=0;count<mAllService.size();count++){
            mServiceTypes.add(mAllService.get(count).getWashType());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mServiceTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);

        if(mSelectedIndex!=-1){
            mSpinner.setSelection(mSelectedIndex);
            Log.d(TAG,"mSelectedIndex = "+mSelectedIndex);
        }else{
            Log.d(TAG,"mSelectedIndex = "+mSelectedIndex);
        }
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        if(isSuccess) {
            List<Gents> gents = ((GeneralPojo) responseObj).getData().getGents();
            //List<Gents> gents = dataList.get(0).getGents();
            List<Others> others = ((GeneralPojo) responseObj).getData().getOthers();

            mListAdapter = new ListAdapter(gents);
            listRecyclerGents.setAdapter(mListAdapter);
            mListAdapter.notifyDataSetChanged();
        }else{
            showMessage(errorMsg,R.string.ok);
        }
    }
    /**
     * Adapter for representing list view
     */
    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
        private List<Gents> dataList;
        private ListAdapter(List<Gents> data)
        {
            this.dataList = data;
        }
        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_list_item, parent, false);
            ViewHolder viewHolder;
            viewHolder = new ViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position)
        {
            String itemName = dataList.get(holder.getAdapterPosition()).getItemName();
            holder.textItem.setText(itemName);

            String itemPrice = dataList.get(holder.getAdapterPosition()).getPrice();
            holder.textPrice.setText(itemPrice+" "+CURRENCY);

        }
        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.item_name)
            TextView textItem;

            @BindView(R.id.item_price)
            TextView textPrice;

            ViewHolder(View itemView)
            {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
