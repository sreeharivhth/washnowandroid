package wash.midest.com.mrwashapp.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.mrwashapp.MrWashApp;

public class SubSplashActivity extends BaseActivity {

    @BindView(R.id.btnRegister)Button mRegBtn;
    @BindView(R.id.btnLogin)Button mLoginBtn;
    GeneralListDataPojo generalListDataPojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_splash);
        ButterKnife.bind(this);

        generalListDataPojo=new GeneralListDataPojo();
        generalListDataPojo.setStatus("STATUS is success");

    }

    @OnClick(R.id.btnRegister)
    void actionRegistration(){
        if(!MrWashApp.getMrWashApp().isAppActive()){
            return;
        }
        Intent i = new Intent(SubSplashActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnLogin)
    void actionLogin(){
        if(!MrWashApp.getMrWashApp().isAppActive()){
            return;
        }
        Intent i = new Intent(SubSplashActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
