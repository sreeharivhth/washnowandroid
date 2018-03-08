package wash.midest.com.mrwashapp.screens;

import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView mActionBarTitle;
    private ImageView mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

    }

    protected void setActionBarTitleInCenter(String title,boolean displayBackButton){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        /*customActionBarView = View.inflate(getApplicationContext(), R.layout.action_bar_layout, null);
        ButterKnife.bind(this,customActionBarView);*/
        /*ButterKnife.bind(this,customActionBarView);*/
        /*mActionBarTitle=ButterKnife.findById(this,R.id.action_bar_title);*/
        //View customActionBarView = View.inflate(getApplicationContext(), R.layout.action_bar_layout, null);
        actionBar.setCustomView(R.layout.action_bar_layout);
        mActionBarTitle=findViewById(R.id.action_bar_title);
        mActionBarTitle.setText(title);

        if(displayBackButton){
            mBackBtn=findViewById(R.id.back_btn_title);
            mBackBtn.setOnClickListener(this);
        }else{
            mBackBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v==mBackBtn){
            finish();
        }
    }
}
