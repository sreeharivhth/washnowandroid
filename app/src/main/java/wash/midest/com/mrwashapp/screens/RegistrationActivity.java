package wash.midest.com.mrwashapp.screens;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.utils.AppUtils;

public class RegistrationActivity extends BaseActivity {

    @BindView(R.id.first_name) TextInputEditText mFName;
    @BindView(R.id.last_name) TextInputEditText mLName;
    @BindView(R.id.phone) TextInputEditText mPhone;
    @BindView(R.id.email) TextInputEditText mEmail;
    @BindView(R.id.password) TextInputEditText mPassword;
    @BindView(R.id.registration_btn) Button mBtnReg;
    private final String mMobileInitial ="+974 ";
    private final int mMobileMax =13;
    private AppUtils mAppUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        setActionBarTitleInCenter(getString(R.string.app_title),true);
        mPhone.setText(mMobileInitial);
        Selection.setSelection(mPhone.getText(), mPhone.getText().length());
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith(mMobileInitial)){
                    mPhone.setText(mMobileInitial);
                    Selection.setSelection(mPhone.getText(), mPhone.getText().length());
                }
            }
        });
        mAppUtils=new AppUtils();
    }

    @OnClick(R.id.registration_btn)
    protected void isValidRegAction(){
        boolean isValid=true;
        if(TextUtils.isEmpty(mFName.getText().toString().trim())){
            mFName.setError(getResources().getString(R.string.first_name_error));
            isValid=false;
        }
        if(TextUtils.isEmpty(mLName.getText().toString().trim())){
            mLName.setError(getResources().getString(R.string.last_name_error));
            isValid=false;
        }
        if(mPhone.length() != mMobileMax){
            mPhone.setError(getResources().getString(R.string.phone_error));
            isValid=false;
        }
        if(TextUtils.isEmpty(mEmail.getText().toString().trim())){
            mEmail.setError(getResources().getString(R.string.email_error));
            isValid=false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString().trim()).matches()){
            mEmail.setError(getResources().getString(R.string.email_error));
            isValid=false;
        }

        if(TextUtils.isEmpty(mPassword.getText().toString().trim())){
            mPassword.setError(getResources().getString(R.string.password_error));
            isValid=false;
        }else if(!isValidPassword()){
            mPassword.setError(getResources().getString(R.string.password_criteria));
            isValid=false;
        }
        if(isValid){
            doRegAction();
        }
    }
    private boolean isValidPassword(){
        boolean isValid=true;
        if(!TextUtils.isEmpty(mPassword.getText().toString().trim())){

            
            String passwordEntered=mPassword.getText().toString().trim();
            isValid = mAppUtils.isValidPassword(passwordEntered);
            return isValid;
        }else{
            return false;
        }
    }
    private void doRegAction(){
        Intent i = new Intent(RegistrationActivity.this, LandingActivity.class);
        startActivity(i);
    }
}
