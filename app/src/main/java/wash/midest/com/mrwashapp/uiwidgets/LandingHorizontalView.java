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

public class LandingHorizontalView extends LinearLayout {

    WashTypes mTypeContent;

    public LandingHorizontalView(Context context){
        super(context);
        initialize();
    }

    public LandingHorizontalView(Context context, int count, WashTypes typeContent){
        super(context);
        mTypeContent = typeContent;
        initialize(count);
    }

    private void initialize(){
        LayoutInflater.from(getContext()).inflate(R.layout.landing_hori_view, this, true);
    }
    private void initialize(int count){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.landing_hori_view, this, true);
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
}
