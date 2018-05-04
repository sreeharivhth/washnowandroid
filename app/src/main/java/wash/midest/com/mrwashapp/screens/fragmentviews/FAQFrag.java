package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIConstants;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.customviews.PriceListRowView;
import wash.midest.com.mrwashapp.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFrag extends BaseFrag implements APICallBack {

    private ListAdapter mListAdapter;
    @BindView(R.id.faqRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    private Unbinder mUnbinder;
    private List<Data> data;

    public FAQFrag() {
        // Required empty public constructor

    }

    public static FAQFrag newInstance(){

        FAQFrag faqFrag=new FAQFrag();
        return faqFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_faq, container, false);
        mUnbinder=ButterKnife.bind(this, view);
        //set Title
        LandingActivity landingActivity = (LandingActivity) getActivity();
        landingActivity.setFragmentTitle(getActivity().getString(R.string.faq));

        mProgressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> requestParams=new HashMap<>();
        //TODO keep actual mToken, once handled from backend
        requestParams.put(new APIConstants().API_ACCESSTOKEN,"");
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.processFAQ(FAQFrag.this,requestParams);

        return view;
    }
    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        if(isSuccess){

            GeneralPojo pojo=(GeneralPojo)responseObj;
            data =  pojo.getData();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(layoutManager);
                    if(data!=null && data.size()>0){
                        mListAdapter = new ListAdapter(data);
                        mRecyclerView.setAdapter(mListAdapter);
                        mListAdapter.notifyDataSetChanged();
                    }
                }
            });
        }else{
            showMessage(errorMsg,R.string.ok);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_row_view, parent, false);
            ViewHolder viewHolder;
            viewHolder = new ViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position)
        {
            String question = dataList.get(holder.getAdapterPosition()).getQuestion();
            holder.textQuestion.setText(question);

            String answer = dataList.get(holder.getAdapterPosition()).getAnswer();
            holder.textAnswer.setText(answer);

            /*holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    invokeMapActivity(position);
                }
            });*/
        }
        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.faq_question)
            TextView textQuestion;
            @BindView(R.id.faq_answer)
            TextView textAnswer;
            ViewHolder(View itemView)
            {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
