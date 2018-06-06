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
import wash.midest.com.mrwashapp.models.Ladies;
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

    @BindView(R.id.headgents)
    TextView mGentsHead;

    @BindView(R.id.gents_seperator)
    View mGentsSeperator;

    @BindView(R.id.headothers)
    TextView mOthersHead;

    @BindView(R.id.others_seperator)
    View mOthersSeperator;

    @BindView(R.id.headladies)
    TextView mLadiesHead;

    private ListAdapterGents mListAdapterGents;

    private ListAdapterOthers mListAdapterOthers;

    private ListAdapterLadies mListAdapterLadies;

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

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        listRecyclerGents.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        listRecyclerOthers.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        layoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        listRecyclerLadies.setLayoutManager(layoutManager3);

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

    void hideSeperators(){
        mGentsHead.setVisibility(View.GONE);
        mGentsSeperator.setVisibility(View.GONE);
        mOthersHead.setVisibility(View.GONE);
        mOthersSeperator.setVisibility(View.GONE);
        mLadiesHead.setVisibility(View.GONE);
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
            hideSeperators();

            List<Gents> gents=new ArrayList<>();
            List<Others> others=new ArrayList<>();
            List<Ladies> ladies=new ArrayList<>();

            if(null!=mListAdapterGents){
                gents.clear();
                mListAdapterGents.notifyDataSetChanged();
            }
            if(null!=mListAdapterOthers){
                others.clear();
                mListAdapterOthers.notifyDataSetChanged();
            }
            if(null!=mListAdapterLadies){
                ladies.clear();
                mListAdapterLadies.notifyDataSetChanged();
            }

            gents = ((GeneralPojo) responseObj).getData().getGents();

            others = ((GeneralPojo) responseObj).getData().getOthers();

            ladies = ((GeneralPojo) responseObj).getData().getLadies();


            if(null !=gents && gents.size()>0){
                listRecyclerGents.setVisibility(View.VISIBLE);
                mGentsHead.setVisibility(View.VISIBLE);
                mGentsSeperator.setVisibility(View.VISIBLE);
                mListAdapterGents = new ListAdapterGents(gents);
                listRecyclerGents.setAdapter(mListAdapterGents);
                mListAdapterGents.notifyDataSetChanged();
            }else{
                listRecyclerGents.setVisibility(View.GONE);
            }
            if(null != others && others.size()>0){
                listRecyclerOthers.setVisibility(View.VISIBLE);
                mOthersHead.setVisibility(View.VISIBLE);
                mOthersSeperator.setVisibility(View.VISIBLE);
                mListAdapterOthers = new ListAdapterOthers(others);
                listRecyclerOthers.setAdapter(mListAdapterOthers);
                mListAdapterOthers.notifyDataSetChanged();
            }else{
                listRecyclerOthers.setVisibility(View.GONE);
            }
            if(null != ladies && ladies.size()>0){
                listRecyclerLadies.setVisibility(View.VISIBLE);
                mLadiesHead.setVisibility(View.VISIBLE);
                mListAdapterLadies = new ListAdapterLadies(ladies);
                listRecyclerLadies.setAdapter(mListAdapterLadies);
                mListAdapterLadies.notifyDataSetChanged();
            }else{
                listRecyclerLadies.setVisibility(View.GONE);
            }
        }else{
            showMessage(errorMsg,R.string.ok);
        }
    }
    /**
     * Adapter for representing list view Gents
     */
    public class ListAdapterGents extends RecyclerView.Adapter<ListAdapterGents.ViewHolder>{
        private List<Gents> dataList;
        private ListAdapterGents(List<Gents> data)
        {
            this.dataList = data;
        }
        @Override
        public ListAdapterGents.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_list_item, parent, false);
            ViewHolder viewHolder;
            viewHolder = new ViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ListAdapterGents.ViewHolder holder, final int position)
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

    /**
     * Adapter for representing list view Ladies
     */
    public class ListAdapterLadies extends RecyclerView.Adapter<ListAdapterLadies.ViewHolder>{
        private List<Ladies> dataList;
        private ListAdapterLadies(List<Ladies> data)
        {
            this.dataList = data;
        }
        @Override
        public ListAdapterLadies.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_list_item, parent, false);
            ViewHolder viewHolder;
            viewHolder = new ViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ListAdapterLadies.ViewHolder holder, final int position)
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

    /**
     * Adapter for representing list view Others
     */
    public class ListAdapterOthers extends RecyclerView.Adapter<ListAdapterOthers.ViewHolder>{
        private List<Others> dataList;
        private ListAdapterOthers(List<Others> data)
        {
            this.dataList = data;
        }
        @Override
        public ListAdapterOthers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_list_item, parent, false);
            ViewHolder viewHolder;
            viewHolder = new ViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ListAdapterOthers.ViewHolder holder, final int position)
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
