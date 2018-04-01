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

import org.w3c.dom.Text;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APIServiceFactory;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.mrwashapp.MrWashApp;
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
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        setActionBarTitleInCenter(getString(R.string.app_title),true);

        Bundle bundle = getIntent().getExtras();
        mEmail=bundle.getString("EMAIL");
        /*if(null!=savedInstanceState){
            //i.putExtra("EMAIL",mEmail.getText().toString().trim());
            mEmail=savedInstanceState.getString("EMAIL");
        }*/
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
        if(!( TextUtils.isEmpty(mOtpEditText.getText()) && mOtpEditText.getText().length()==4)){
            proceedAPICall();

        }else{
            mOtpEditText.setError(getString(R.string.otp_error));
            mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    void proceedAPICall(){
        if(!mAppUtils.isNetworkConnected(this)){
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        alterProgressBar();
        Log.d(TAG,TAG+" proceedAPICall()");
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
                            Log.d(TAG,TAG+" onNext error statusCode = "+statusCode);
                            String errorMessage = generalPojo.getError().get(0).getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                showErrorAlert(errorMessage);
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }else{
                            Log.d(TAG,TAG+" onNext statusCode  = "+statusCode );
                            String isVerified = generalPojo.getData().get(0).getIsVerified();
                            String isActive = generalPojo.getData().get(0).getActive();

                            if (isVerified.equalsIgnoreCase(mApiConstants.STATUS_1) && isActive.equalsIgnoreCase(mApiConstants.STATUS_1)) {
                                Log.d(TAG,TAG+" onNext statusCode  = "+statusCode +" || isVerified = 1 && isActive = 1");
                                String memberId = generalPojo.getData().get(0).getMemberId();
                                mToken = generalPojo.getData().get(0).getToken();

                                if(!TextUtils.isEmpty(memberId)){
                                    mSharedPreference.setPreferenceString(mSharedPreference.USER_ID,memberId);
                                }
                                if(!TextUtils.isEmpty(mToken)){
                                    mSharedPreference.setPreferenceString(mSharedPreference.TOKEN_SESSION,mToken);
                                }
                                if(!TextUtils.isEmpty(isActive)){
                                    mSharedPreference.setPreferenceString(mSharedPreference.ACTIVE_STATUS,isActive);
                                }
                                //Done with OTP
                                //Get the services list from server
                                processServicesAPI();

                            }else{
                                Log.d(TAG,TAG+" onNext statusCode  = "+statusCode +" || isVerified != 1 OR isActive != 1");
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, TAG+"onError "+e.toString());
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

        if(!mAppUtils.isNetworkConnected(this)){
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        alterProgressBar();
        Log.d(TAG,TAG+" proceedAPICall()");
        String appId = mApiConstants.APPID_VAL;
        HashMap<String,String> requestParams=new HashMap<>();
        requestParams.put(mApiConstants.API_EMAIL,mEmail);
        requestParams.put(mApiConstants.API_APPID,appId);

        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().resendOTPAPI( requestParams )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralPojo>() {
                    @Override
                    public void onNext(GeneralPojo generalPojo) {
                        alterProgressBar();
                        int statusCode = (int) generalPojo.getStatusCode();
                        //Check for error
                        if(statusCode!=mApiConstants.SUCCESS){
                            Log.d(TAG,TAG+" onNext error statusCode = "+statusCode);
                            String errorMessage = generalPojo.getError().get(0).getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                showErrorAlert(errorMessage);
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }else{
                            Log.d(TAG,TAG+" onNext statusCode  = "+statusCode );
                            String emailSent = generalPojo.getData().get(0).getEmail();

                            if(!TextUtils.isEmpty(emailSent)){
                                String msg = getString(R.string.opt_sent_to)+emailSent;
                                showErrorAlert(msg);
                            }else{
                                String msg = getString(R.string.opt_sent_to)+" "+mEmail;
                                showErrorAlert(msg);
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, TAG+" onResend onError "+e.toString());
                        alterProgressBar();
                        showErrorAlert(getString(R.string.general_error_server));
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG, TAG+"### The API service Observable has ended!");
                    }
                });
    }

    @OnClick(R.id.changeEmail)
    protected void onChangeEmail(){
        Intent i = new Intent();
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    private void doLandingAction(GeneralListDataPojo generalPojo){
        Log.d(TAG,TAG+" doLandingAction");
        /*((MrWashApp) getApplication())
                .getRxEventBus()
                .send(generalPojo);*/
        Intent i = new Intent(OtpActivity.this, LandingActivity.class);
        i.putExtra("LandingData",generalPojo);
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

    void processServicesAPI(){
        if(!mAppUtils.isNetworkConnected(this)){
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        alterProgressBar();
        Log.d(TAG,TAG+" processServicesAPI()");
        HashMap<String,String> requestParams=new HashMap<>();
        //requestParams.put(mApiConstants.API_ACCESSTOKEN,mToken);
        requestParams.put(mApiConstants.API_ACCESSTOKEN,"");

        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().servicesAPI( requestParams )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {
                        alterProgressBar();
                        int statusCode = generalPojo.getStatusCode();
                        if(statusCode!=mApiConstants.SUCCESS){
                            Log.d(TAG,TAG+" onNext error statusCode = "+statusCode);
                            String errorMessage = generalPojo.getError().get(0).getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                showErrorAlert(errorMessage);
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }else{
                            Log.d(TAG,TAG+" onNext success statusCode = "+statusCode);
                            //Send data using RxEvent Bus
                            doLandingAction(generalPojo);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, TAG+" processServicesAPI Error "+e.toString());
                        alterProgressBar();
                        showErrorAlert(getString(R.string.general_error_server));
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG, TAG+"### The API service Observable has ended!");
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
