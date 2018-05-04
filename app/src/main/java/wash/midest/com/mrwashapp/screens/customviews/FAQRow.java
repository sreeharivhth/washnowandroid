package wash.midest.com.mrwashapp.screens.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import wash.midest.com.mrwashapp.R;

/**
 * Created by Sreehari.KV on 4/28/2018.
 */

public class FAQRow extends LinearLayout {

    private TextView mFaqQstn;
    private TextView mFaqAns;
    private String mQstn;
    private String mAns;

    public FAQRow(Context context) {
        super(context);
        init();
    }

    public FAQRow(Context context,String qstn,String ans) {
        super(context);
        mQstn = qstn;
        mAns=ans;
        init();
    }

    public FAQRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FAQRow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){

        LayoutInflater.from(getContext()).inflate(R.layout.faq_row_view, this, true);
        mFaqQstn=findViewById(R.id.faq_question);
        mFaqAns=findViewById(R.id.faq_answer);

        if(!TextUtils.isEmpty(mQstn)){
            mFaqQstn.setText(mQstn);
        }
        if(!TextUtils.isEmpty(mAns)){
            mFaqAns.setText(mAns);
        }
    }
}
