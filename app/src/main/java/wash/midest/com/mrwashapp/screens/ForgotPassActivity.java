package wash.midest.com.mrwashapp.screens;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
            proceedAPICall();
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
                            String userId = generalPojo.getData().getUserId();

                            if(!TextUtils.isEmpty(userId)){
                                mSharedPreference.setPreferenceString(mSharedPreference.USER_ID,userId);
                            }
                            showPinEntry();
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
        mOtpEditText.setVisibility(View.VISIBLE);
        mBtnSendOtp.setText(getString(R.string.verify_otp));
        isOTPShown=true;

    }
}
