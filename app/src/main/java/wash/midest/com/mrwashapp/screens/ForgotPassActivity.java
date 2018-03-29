package wash.midest.com.mrwashapp.screens;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
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

public class ForgotPassActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getName();
    @BindView(R.id.forgot_pass_email) TextInputEditText mEmail;
    @BindView(R.id.send_otp_btn) Button mBtnSendOtp;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.otpView)PinEntryEditText mOtpEditText;
    private boolean mIsProgressShown =false;
    private boolean isOTPShown=false;
    private String mUserId;
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        ButterKnife.bind(this);
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

    @OnClick(R.id.send_otp_btn)
    void isValidOtpAction(){

        boolean isValid=true;
        if(TextUtils.isEmpty(mEmail.getText().toString().trim())){
            mEmail.setError(getResources().getString(R.string.email_error));
            isValid=false;
        }
        if(isValid){
            if(!isOTPShown) {
                proceedAPICall();
            }else if(! (TextUtils.isEmpty(mOtpEditText.getText().toString().trim())
                        && mOtpEditText.getText().length()==4)){
                processVerifyCodeAPI();
            }else{
                mOtpEditText.setError(getString(R.string.otp_error));
            }
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
        requestParams.put(mApiConstants.API_EMAIL,mEmail.getText().toString().trim());
        requestParams.put(mApiConstants.API_APPID,appId);

        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().forgotPassAPI( requestParams )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralPojo>() {
                    @Override
                    public void onNext(GeneralPojo generalPojo) {

                        alterProgressBar();
                        int statusCode = generalPojo.getStatusCode();
                        //Check for error
                        if(statusCode!=mApiConstants.SUCCESS){
                            String errorMessage = generalPojo.getError().getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                showErrorAlert(errorMessage);
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }else{
                            mUserId = generalPojo.getData().getUserId();

                            if(!TextUtils.isEmpty(mUserId)){
                                mSharedPreference.setPreferenceString(mSharedPreference.USER_ID,mUserId);
                                showPinEntry();
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
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

    void showPinEntry(){
        mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mOtpEditText.setVisibility(View.VISIBLE);
        mOtpEditText.requestFocus();
        mBtnSendOtp.setText(getString(R.string.verify_otp));
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
        //Make is otp shown as true
        isOTPShown=true;
        //Disable the email entry field
        mEmail.setEnabled(false);
    }

    void processVerifyCodeAPI(){
        if(!mAppUtils.isNetworkConnected(this)){
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        String appId = mApiConstants.APPID_VAL;
        HashMap<String,String> requestParams=new HashMap<>();
        requestParams.put(mApiConstants.API_USERID,mUserId);
        requestParams.put(mApiConstants.API_APPID,appId);
        requestParams.put(mApiConstants.API_CODE,mOtpEditText.getText().toString());

        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().forgotPassCodeVerifyAPI( requestParams )
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
                            String userId = generalPojo.getData().getUserId();
                            //Proceed with
                            Log.d(TAG,"userId received ==> "+userId);
                            showErrorAlert(getString(R.string.forgot_pass_status_msg));
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
}
