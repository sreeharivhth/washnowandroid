package wash.midest.com.mrwashapp.uiwidgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import wash.midest.com.mrwashapp.R;

/**
 * Created by Sreehari.KV on 3/13/2018.
 */

public class LandingHorizontalView extends LinearLayout {

    public LandingHorizontalView(Context context){
        super(context);
        initialize();
    }

    private void initialize(){
        LayoutInflater.from(getContext()).inflate(R.layout.landing_hori_view, this, true);

    }

}
