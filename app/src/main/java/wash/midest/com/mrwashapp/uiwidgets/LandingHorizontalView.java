package wash.midest.com.mrwashapp.uiwidgets;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.WashTypes;

/**
 * Created by Sreehari.KV on 3/13/2018.
 */

public class LandingHorizontalView extends LinearLayout implements View.OnClickListener{

    private WashTypes mTypeContent;
    private Button mButton;
    private ButtonClicked mListener;
    private PriceListClicked mPriceListListener;
    private TextView mPriceList;

    public LandingHorizontalView(Context context){
        super(context);
        initialize();
    }

    public LandingHorizontalView(Context context, int count, WashTypes typeContent,ButtonClicked listener,
                                 PriceListClicked priceListListener){
        super(context);
        mTypeContent = typeContent;
        mListener = listener;
        mPriceListListener = priceListListener;
        initialize(count);
    }

    private void initialize(){
        LayoutInflater.from(getContext()).inflate(R.layout.landing_hori_view, this, true);
    }
    private void initialize(int count){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.landing_hori_view, this, true);
        mPriceList=findViewById(R.id.txt_price_list);
        mPriceList.setTag(count);
        mPriceList.setOnClickListener(this);
        mButton= findViewById(R.id.btn_place_order);
        mButton.setTag(count);
        mButton.setOnClickListener(this);
        if(count%2!=0){
            Button btn = view.findViewById(R.id.btn_place_order);
            /*btn.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_dark));*/
            btn.setBackgroundResource(R.drawable.button_bg_rounded_corners_dark);
        }
        TextView washType = view.findViewById(R.id.txt_washtype);
        if(!TextUtils.isEmpty(mTypeContent.getWashType())){
            washType.setText(mTypeContent.getWashType());
        }

        TextView washTime = view.findViewById(R.id.txt_timerequired);
        if(!TextUtils.isEmpty(mTypeContent.getTime())){
            washTime.setText(mTypeContent.getTime());
        }
    }

    @Override
    public void onClick(View v) {
        if(v instanceof  Button){
            int count = (int) v.getTag();
            mListener.onButtonClicked(count);
        }else if(v instanceof TextView){
            int count = (int) v.getTag();
            mPriceListListener.onPriceListClicked(count);
        }

    }

    public interface ButtonClicked{
        public void onButtonClicked(int index);
    }

    public interface PriceListClicked{
        public void onPriceListClicked(int index);
    }
}
