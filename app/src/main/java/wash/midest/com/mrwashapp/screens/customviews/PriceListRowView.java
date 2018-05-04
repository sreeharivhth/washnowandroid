package wash.midest.com.mrwashapp.screens.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;

/**
 * Created by Sreehari.KV on 4/21/2018.
 */

public class PriceListRowView  extends LinearLayout{

    private RecyclerView mRecyclerListView;
    private List<Data> mData;
    private ListAdapter mListAdapter;

    public PriceListRowView(Context context,List<Data> data) {
        super(context);
        mData= data;

        init();
    }

    public PriceListRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public PriceListRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    void init(){

        LayoutInflater.from(getContext()).inflate(R.layout.price_list_component, this, true);
        mRecyclerListView=findViewById(R.id.expandableList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerListView.setLayoutManager(layoutManager);
        if(mData!=null && mData.size()>0){
            mListAdapter = new ListAdapter(mData);
            mRecyclerListView.setAdapter(mListAdapter);
        }
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

        private List<Data> dataList;
        private ListAdapter(List<Data> data){
            dataList=data;
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_expandable_list, parent, false);
            RecyclerView.ViewHolder viewHolder;
            viewHolder = new ViewHolder(view);
            return (ViewHolder) viewHolder;
        }

        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {

            String question = dataList.get(holder.getAdapterPosition()).getQuestion();
            holder.itemName.setText(question);

            String answer = dataList.get(holder.getAdapterPosition()).getAnswer();
            holder.itemPrice.setText(answer);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder
        {
            /*@BindView(R.id.vehicleType)
            TextView textViewType;
            @BindView(R.id.vehicleId) TextView textViewId;
            @BindView(R.id.listIcon)*/
            TextView itemName;
            TextView itemPrice;

            ViewHolder(View itemView)
            {
                super(itemView);
                /*ButterKnife.bind(this, itemView);*/
                itemName=findViewById(R.id.item_name);
                itemPrice=findViewById(R.id.item_price);
            }
        }
    }
}
