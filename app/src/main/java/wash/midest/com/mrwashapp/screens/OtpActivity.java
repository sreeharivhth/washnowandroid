package wash.midest.com.mrwashapp.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APIServiceFactory;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.uiwidgets.PinEntryEditText;

public class OtpActivity extends BaseActivity {

    @BindView(R.id.otpView) PinEntryEditText mOtpEditText;
    @BindView(R.id.verify_btn) Button mBtnVerify;
    @BindView(R.id.changeEmail) TextView mChangeEmail;
    @BindView(R.id.resendOTP) TextView mResendOTP;
    private InputMethodManager mInputMethodManager;
    private final int CHANGE_DETAILS = 220;
    private String mEmail="";
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private boolean mIsProgressShown =false;
    private String TAG=OtpActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        setActionBarTitleInCenter(getString(R.string.app_title),true);

        if(null!=savedInstanceState){
            //i.putExtra("EMAIL",mEmail.getText().toString().trim());
            mEmail=savedInstanceState.getString("EMAIL");
        }
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
                if (s.length() == 4) {
                    mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
    }
    void alterProgressBar(){
        if(mIsProgressShown)
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            mIsProgressShown =false;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }else{
            mProgressBar.setVisibility(View.VISIBLE);
            mIsProgressShown =true;
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @OnClick(R.id.verify_btn)
    protected void onVerifyAction(){
        if(!TextUtils.isEmpty(mOtpEditText.getText()) && mOtpEditText.getText().length()==4){
            proceedAPICall();
            alterProgressBar();
        }else{
            mOtpEditText.setError(getString(R.string.otp_error));
            mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    void proceedAPICall(){
        if(!mAppUtils.isNetworkConnected(this)){
            return;
        }else{
            showErrorAlert(getString(R.string.network_error));
        }

        String appId = mApiConstants.APPID_VAL;
        HashMap<String,String> requestParams=new HashMap<>();
        requestParams.put(mApiConstants.API_EMAIL,mEmail);
        requestParams.put(mApiConstants.API_CODE,mOtpEditText.getText().toString());
        requestParams.put(mApiConstants.API_APPID,appId);

        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().verifyEmailAPI( requestParams )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralPojo>() {
                    @Override
                    public void onNext(GeneralPojo generalPojo) {

                        alterProgressBar();
                        int statusCode = (int) generalPojo.getStatusCode();
                        //Check for error
                        if(statusCode!=mApiConstants.SUCCESS){
                            String errorMessage = generalPojo.getError().getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                showErrorAlert(errorMessage);
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }else{
                            String memberId = generalPojo.getData().getMemberId();
                            String token = generalPojo.getData().getToken();
                            String active = generalPojo.getData().getActive();

                            if(!TextUtils.isEmpty(memberId)){
                                mSharedPreference.setPreferenceString(mSharedPreference.USER_ID,memberId);
                            }
                            if(!TextUtils.isEmpty(token)){
                                mSharedPreference.setPreferenceString(mSharedPreference.TOKEN_SESSION,token);
                            }
                            if(!TextUtils.isEmpty(active)){
                                mSharedPreference.setPreferenceString(mSharedPreference.ACTIVE_STATUS,active);
                            }

                            /*//Either call new required web services or push Landing screen
                            pushLanding();*/
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        alterProgressBar();
                        showErrorAlert(getString(R.string.general_error_server));
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG, TAG+"### The API service Observable has ended!");
                    }
                });
    }

    @OnClick(R.id.resendOTP)
    protected void onResend(){
        //TODO
        showToast(getResources().getString(R.string.coming_soon));
    }

    @OnClick(R.id.changeEmail)
    protected void onChangeEmail(){
        Intent i = new Intent();
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    private void doLandingAction(){
        Intent i = new Intent(OtpActivity.this, LandingActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back_btn_title){
                //close keyboard if visible
                try {
                    mInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally { }
                //close the screen
                finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
