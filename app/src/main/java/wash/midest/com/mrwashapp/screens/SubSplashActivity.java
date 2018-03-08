package wash.midest.com.mrwashapp.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wash.midest.com.mrwashapp.R;

public class SubSplashActivity extends BaseActivity {

    @BindView(R.id.btnRegister)Button mRegBtn;
    @BindView(R.id.btnLogin)Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_splash);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnRegister)
    void actionRegistration(){
        Intent i = new Intent(SubSplashActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnLogin)
    void actionLogin(){
        Intent i = new Intent(SubSplashActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
