package wash.midest.com.mrwashapp.screens;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.utils.AppUtils;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView mActionBarTitle;
    private ImageView mBackBtn;
    protected AppUtils mAppUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mAppUtils= new AppUtils();

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

    protected void showToast(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

    protected Context getAppContext(){
        return getApplicationContext();
    }

    @Override
    public void onClick(View v) {
        if(v==mBackBtn){
            finish();
        }
    }
}
