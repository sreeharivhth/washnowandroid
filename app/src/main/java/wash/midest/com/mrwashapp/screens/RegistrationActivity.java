package wash.midest.com.mrwashapp.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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
import wash.midest.com.mrwashapp.appservices.APIConfiguration;
import wash.midest.com.mrwashapp.appservices.APIConstants;
import wash.midest.com.mrwashapp.appservices.APIServiceFactory;
import wash.midest.com.mrwashapp.appservices.RegistrationObj;
import wash.midest.com.mrwashapp.models.RegistrationPojo;

public class RegistrationActivity extends BaseActivity {

    private static final String TAG = RegistrationActivity.class.getName();
    @BindView(R.id.first_name) TextInputEditText mFName;
    @BindView(R.id.last_name) TextInputEditText mLName;
    @BindView(R.id.phone) TextInputEditText mPhone;
    @BindView(R.id.email) TextInputEditText mEmail;
    @BindView(R.id.password) TextInputEditText mPassword;
    @BindView(R.id.registration_btn) Button mBtnReg;
    private final String mMobileInitial ="+974 ";
    private final int mMobileMax =13;
    private final int CHANGE_DETAILS = 220;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    private boolean mIsProgressShown =false;

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
        String passwordEntered=mPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(passwordEntered)){
            return mAppUtils.isValidPassword(passwordEntered);
        }else{
            return false;
        }
    }

    private void doRegAction(){

        //Net connection and data retrieval
        /*showOTPScreen();*/
        alterProgressBar();
        connectToService();

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

    private void showOTPScreen(){

        Intent i = new Intent(RegistrationActivity.this, OtpActivity.class);
        startActivityForResult(i,CHANGE_DETAILS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHANGE_DETAILS){
            if (resultCode == Activity.RESULT_OK) {
                mEmail.requestFocus();
            }else{
                mFName.requestFocus();
            }
        }
    }
    private void connectToService(){
        String fn = mFName.getText().toString().trim();
        String ln = mLName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String pass = mPassword.getText().toString().trim();
        //String mob = mPhone.getText().toString().trim();
        String mob = "88888888";
        String imei = "112233";
        String appid = APIConfiguration.APPID;

        APIConstants apiConstants=new APIConstants();

        HashMap<String,String> params=new HashMap<>();
        params.put(apiConstants.API_FIRSTNAME,fn);
        params.put(apiConstants.API_LASTNAME,ln);
        params.put(apiConstants.API_EMAIL,email);
        params.put(apiConstants.API_PASSWORD,pass);
        params.put(apiConstants.API_MOBILE,mob);
        params.put(apiConstants.API_IMEI,imei);
        params.put(apiConstants.API_APPID,appid);
        params.put(apiConstants.API_DIALINGCODE,APIConfiguration.DIALINGCODE);

        RegistrationObj registrationObj=new RegistrationObj(fn,ln,email,pass,mob,imei,appid,APIConfiguration.DIALINGCODE);

        APIServiceFactory serviceFactory = new APIServiceFactory();
        // start service call using RxJava2
        serviceFactory.getAPIConfiguration().fetchRegistrationInformation( params )
                .subscribeOn(Schedulers.io()) //Asynchronously subscribes Observable to perform action in I/O Thread.
                .observeOn(AndroidSchedulers.mainThread()) // To perform its emissions and response on UiThread(or)MainThread.
                .subscribe(new DisposableObserver<RegistrationPojo>() { // It would dispose the subscription automatically. If you wish to handle it use io.reactivex.Observer
                    @Override
                    public void onNext(RegistrationPojo registrationPojo) {
                        // Output

                        /*Log.d(TAG,TAG+"### registrationPojo.getStatus() = "+registrationPojo.getStatus());

                        Log.d(TAG,TAG+"### registrationPojo.getData().getEmail() = "+registrationPojo.getData().getEmail());*/
                        alterProgressBar();

                    }
                    @Override
                    public void onError(Throwable e) {
                        /*hideProgress();
                        responseView.setText("Error occurred! Check your Logcat!");
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace(); // Just to see complete log information. we can comment if not necessary!
                        */
                        /*Log.e(TAG,TAG+"### Error in network ="+e.toString());*/
                        alterProgressBar();
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG, TAG+"### The API service Observable has ended!");
                    }
                });
    }
}
