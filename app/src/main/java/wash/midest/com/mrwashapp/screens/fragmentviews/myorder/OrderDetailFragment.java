package wash.midest.com.mrwashapp.screens.fragmentviews.myorder;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.ItemDetail;
import wash.midest.com.mrwashapp.models.OrderDetail;
import wash.midest.com.mrwashapp.screens.customviews.OrderDetailRow;
import wash.midest.com.mrwashapp.uiwidgets.LandingHorizontalView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailFragment extends DialogFragment {

    private String TAG=OrderDetailFragment.class.getSimpleName();
    private Unbinder mUnbinder;
    @BindView(R.id.detailLinearLayout)LinearLayout mLinearLayout;
    @BindView(R.id.detailOrderNum)TextView mOrderNumber;
    @BindView(R.id.btnDoneId) Button btnDone;
    final static String DATA="DATA";
    private GeneralListDataPojo mDataList;

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }
    }

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    public static OrderDetailFragment getInstance(GeneralListDataPojo dataList){
        OrderDetailFragment orderDetailFragment=new OrderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DATA, dataList);
        orderDetailFragment.setArguments(bundle);
        return orderDetailFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view =  inflater.inflate(R.layout.fragment_order_detail, container, false);
        mUnbinder=ButterKnife.bind(this,root_view);
        mDataList = getArguments().getParcelable(DATA);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        populateData(mDataList);
        return root_view;
    }

    void closeDialog(){
        this.dismiss();
    }

    private void populateData(GeneralListDataPojo mDataList ) {
        List<Data> dataList =  mDataList.getData();
        List<ItemDetail> itemDetails= dataList.get(0).getItemDetails();
        OrderDetail orderDetails = (dataList.get(0).getOrderDetails()).get(0);
        String orderID =    orderDetails.getOrderId()+"";
        if(!TextUtils.isEmpty(orderID)){
            mOrderNumber.setText("Order # "+orderID);
        }
        if(null != itemDetails){
            OrderDetailRow detailRowHead = new OrderDetailRow(getActivity(),"Item",
                    "Count","Price");
            mLinearLayout.addView(detailRowHead);

            for(int count=0;count<itemDetails.size();count++) {
                ItemDetail itemDetail = itemDetails.get(count);

                OrderDetailRow detailRow = new OrderDetailRow(getActivity(),itemDetail.getProductName(),
                        itemDetail.getCount(),itemDetail.getAmount()+"");
                mLinearLayout.addView(detailRow);
            }
            String total = dataList.get(0).getNetAmount()+"";
            OrderDetailRow detailRow = new OrderDetailRow(getActivity(),"",
                    "Total",total+" QR");
            mLinearLayout.addView(detailRow);
        }
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
