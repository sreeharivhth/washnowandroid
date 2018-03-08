package wash.midest.com.mrwashapp.screens;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.uiwidgets.PinEntryEditText;

public class OtpActivity extends BaseActivity {

    @BindView(R.id.otpView) PinEntryEditText mOtpEditText;
    @BindView(R.id.verify_btn) Button mBtnVerify;
    @BindView(R.id.changeEmail) TextView mChangeEmail;
    @BindView(R.id.resendOTP) TextView mResendOTP;

    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        setActionBarTitleInCenter(getString(R.string.app_title),true);

        mOtpEditText.requestFocus();
        mInputMethodManager = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mOtpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {}
            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == "1234".length()) {
                    mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
    }

    @OnClick(R.id.verify_btn)
    protected void onVerifyAction(){
        if(!TextUtils.isEmpty(mOtpEditText.getText())){
            doLandingAction();
        }else{
            mOtpEditText.setError(getString(R.string.otp_error));
            mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @OnClick(R.id.resendOTP)
    protected void onResend(){

    }

    @OnClick(R.id.changeEmail)
    protected void onChangeEmail(){
        finish();
    }

    private void doLandingAction(){
        Intent i = new Intent(OtpActivity.this, LandingActivity.class);
        startActivity(i);
    }
}
