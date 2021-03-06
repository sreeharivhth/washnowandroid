package wash.midest.com.mrwashapp.screens;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
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
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
import wash.midest.com.mrwashapp.appservices.APIServiceFactory;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.mrwashapp.MrWashApp;
import wash.midest.com.mrwashapp.screens.fragmentviews.MyProfileFrag;

public class LoginActivity extends BaseActivity implements APICallBack{

    private static final String TAG = LoginActivity.class.getName();
    @BindView(R.id.email) TextInputEditText mEmail;
    @BindView(R.id.password) TextInputEditText mPassword;
    @BindView(R.id.login_btn) Button mBtnLogin;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.txtForgotPasword)TextView mForgotPassword;
    private boolean mIsProgressShown =false;
    private String mToken;

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
    @OnClick(R.id.txtForgotPasword)
    void forgotPasswordAction(){
        //TODO server returning 500 error for forgot password api
        startActivity(new Intent(this,ForgotPassActivity.class));
        /*showToast("Coming Soon...");*/
    }

    private boolean isValidPassword(){
        String passwordEntered=mPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(passwordEntered)){
            return mAppUtils.isValidPassword(passwordEntered);
        }else{
            return false;
        }
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

    void proceedWithLogin(){

        if(!MrWashApp.getMrWashApp().isAppActive()){
            return;
        }
        if(!mAppUtils.isNetworkConnected(this)){
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        alterProgressBar();
        String email = mEmail.getText().toString().trim();
        String password= mPassword.getText().toString().trim();
        String appId = mApiConstants.APPID_VAL;

        HashMap<String,String> requestParams=new HashMap<>();
        requestParams.put(mApiConstants.API_EMAIL,email);
        requestParams.put(mApiConstants.API_PASSWORD,password);
        requestParams.put(mApiConstants.API_APPID,appId);

        APIServiceFactory serviceFactory = new APIServiceFactory();

        serviceFactory.getAPIConfiguration().loginAPI( requestParams )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {

                        alterProgressBar();
                        int statusCode = (int) generalPojo.getStatusCode();
                        //Check for error
                        if(statusCode!=mApiConstants.SUCCESS){
                            String errorMessage = generalPojo.getError().get(0).getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                showErrorAlert(errorMessage);
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }else{
                            String memberID = generalPojo.getData().get(0).getMemberId();
                            mToken = generalPojo.getData().get(0).getToken();

                            if(!TextUtils.isEmpty(memberID)){
                                mSharedPreference.setPreferenceString(mSharedPreference.MEMBER_ID,memberID);
                            }
                            if(!TextUtils.isEmpty(mToken)){
                                mSharedPreference.setPreferenceString(mSharedPreference.TOKEN_SESSION,mToken);
                            }
                            String activeStatus="1";
                            mSharedPreference.setPreferenceString(mSharedPreference.ACTIVE_STATUS,activeStatus);

                            requestProfileDetails();
                            //processServicesAPI();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        alterProgressBar();
                        showErrorAlert(getString(R.string.general_error_server));
                        Log.e(TAG, TAG+"proceedWithLogin Error "+e.toString());
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG, TAG+"### The API service Observable has ended!");
                    }
                });
    }

    void requestProfileDetails(){

        alterProgressBar();
        HashMap<String,String> requestParams=new HashMap<>();
        //TODO keep actual mToken, once handled from backend
        String appId = mApiConstants.APPID_VAL;
        requestParams.put(mApiConstants.API_APPID,appId);
        requestParams.put(mApiConstants.API_MEMBERID,mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.processMyProfile(LoginActivity.this,requestParams);
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        alterProgressBar();
        if(!MrWashApp.getMrWashApp().isAppActive()){
            return;
        }
        try {
            if (isSuccess) {
                GeneralListDataPojo responsePojo = (GeneralListDataPojo) responseObj;
                Data data = responsePojo.getData().get(0);
                String fn = data.getFirstName();
                String ln = data.getLastName();
                if ((!TextUtils.isEmpty(fn))  && (!TextUtils.isEmpty(ln)))
                {
                    mSharedPreference.setPreferenceString(mSharedPreference.USER_NAME, fn+" "+ln);
                }
                String email = data.getEmail();
                if (!TextUtils.isEmpty(email))
                {
                    mSharedPreference.setPreferenceString(mSharedPreference.USER_EMAIL, email);
                }
                String phone = data.getMobile();
                if (!TextUtils.isEmpty(phone))
                {
                    mSharedPreference.setPreferenceString(mSharedPreference.USER_MOBILE, phone);
                }
            }
        } catch(Exception e) {
            Log.d(TAG,"Error in retrieving, populating user data!");
        }finally{
            //Even if processing is success or not, proceed with services api
            processServicesAPI();
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
        //TODO keep actual mToken, once handled fromserver side
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
                        Log.e(TAG, TAG+"onError "+e.toString());
                        alterProgressBar();
                        showErrorAlert(getString(R.string.general_error_server));
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG, TAG+"### The API service Observable has ended!");
                    }
                });
    }
    void doLandingAction(GeneralListDataPojo generalPojo){
        if(!MrWashApp.getMrWashApp().isAppActive()){
            return;
        }
        Intent i = new Intent(LoginActivity.this, LandingActivity.class);
        i.putExtra("LandingData",generalPojo);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


}
