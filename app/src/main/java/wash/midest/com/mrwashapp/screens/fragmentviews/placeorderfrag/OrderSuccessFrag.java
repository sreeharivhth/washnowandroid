package wash.midest.com.mrwashapp.screens.fragmentviews.placeorderfrag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.mrwashapp.MrWashApp;
import wash.midest.com.mrwashapp.screens.LandingActivity;
import wash.midest.com.mrwashapp.screens.fragmentviews.BaseFrag;

public class OrderSuccessFrag extends BaseFrag {

    private Unbinder mUnbinder;
    @BindView(R.id.btn_close)
    Button btnClose;

    @BindView(R.id.my_order_link)
    TextView txtMyOrder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_success,container,false);
        mUnbinder = ButterKnife.bind(this, view);
        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.order_confirmation));

        return view;
    }

    @OnClick(R.id.my_order_link)
    void onOrderClick(){
        if(!MrWashApp.getMrWashApp().isAppActive()){
            return;
        }
        ((LandingActivity)getActivity()).pushOrderDetailsFrag();
    }
    @OnClick(R.id.btn_close)
    void close(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public static OrderSuccessFrag newInstance() {
        OrderSuccessFrag fragment = new OrderSuccessFrag();
        return fragment;
    }
}
