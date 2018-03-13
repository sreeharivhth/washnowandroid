package wash.midest.com.mrwashapp.screens;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.utils.AppUtils;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.email) TextInputEditText mEmail;
    @BindView(R.id.password) TextInputEditText mPassword;
    @BindView(R.id.login_btn) Button mBtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setActionBarTitleInCenter(getString(R.string.app_title),true);

    }

    @OnClick(R.id.login_btn)
    protected void isValidLoginAction(){
        boolean isValid=true;
        if(TextUtils.isEmpty(mEmail.getText().toString().trim())){
            mEmail.setError(getResources().getString(R.string.email_error));
            isValid=false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString().trim()).matches()){
            mEmail.setError(getResources().getString(R.string.email_error));
            isValid=false;
        }
        if(TextUtils.isEmpty(mPassword.getText().toString().trim())){
            mPassword.setError(getResources().getString(R.string.password_error));
            isValid=false;
        }
        else if(!isValidPassword()){
            mPassword.setError(getResources().getString(R.string.password_criteria));
            isValid=false;
        }

        if(isValid){
            proceedWithLogin();
        }
    }

    private boolean isValidPassword(){
        String passwordEntered=mPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(passwordEntered)){
            return mAppUtils.isValidPassword(passwordEntered);
        }else{
            return false;
        }
    }

    private void proceedWithLogin(){
        Intent i = new Intent(LoginActivity.this, LandingActivity.class);
        startActivity(i);
    }
}
