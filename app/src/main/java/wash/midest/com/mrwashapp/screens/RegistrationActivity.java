package wash.midest.com.mrwashapp.screens;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import wash.midest.com.mrwashapp.appservices.APIServiceFactory;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.utils.AppUtils;

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
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
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

    void checkPermission(){
        if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistrationActivity.this, Manifest.permission.READ_PHONE_STATE)) {

                ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else {
            //Permission already granted. Proceed with functionalities
            postPermissionGranted();
        }
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
            isPermissionRequired();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission already granted. Proceed with functionalities
                    postPermissionGranted();
                } else {

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RegistrationActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Reading phones identifier is required for us to verify this device and proceeding ! Without this application will close");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            checkPermission();
                        }
                    });
                    alertBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            closeApp();
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }
                break;
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

    private void showOTPScreen(){
        Log.d(TAG,TAG+" showOTPScreen()");
        Intent i = new Intent(RegistrationActivity.this, OtpActivity.class);
        i.putExtra("EMAIL",mEmail.getText().toString().trim());
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

    void isPermissionRequired(){

        boolean isPermissionRequired = new AppUtils().isVersionGreaterThanM(getApplicationContext());

        if(isPermissionRequired) {
            checkPermission();
        }else{
            postPermissionGranted();
        }
    }

    private void postPermissionGranted(){

        if(!mAppUtils.isNetworkConnected(this)){
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        alterProgressBar();
        String fn = mFName.getText().toString().trim();
        String ln = mLName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String pass = mPassword.getText().toString().trim();
        String mob =mPhone.getText().toString().trim().substring(4,13);
        //TODO , keep actual imei retrieval
        /*String imei = mAppUtils.getDeviceIMEI(this);*/
        String imei = "1122009955";
        Log.d(TAG,TAG+"Device Imei >> "+imei);
        String appid = mApiConstants.APPID_VAL;

        HashMap<String,String> requestParams=new HashMap<>();
        requestParams.put(mApiConstants.API_FIRSTNAME,fn);
        requestParams.put(mApiConstants.API_LASTNAME,ln);
        requestParams.put(mApiConstants.API_EMAIL,email);
        requestParams.put(mApiConstants.API_PASSWORD,pass);
        requestParams.put(mApiConstants.API_MOBILE,mob);
        requestParams.put(mApiConstants.API_IMEI,imei);
        requestParams.put(mApiConstants.API_APPID,appid);
        requestParams.put(mApiConstants.API_DIALINGCODE,mApiConstants.DIALINGCODE_VAL);

        Log.d(TAG,TAG+"postPermissionGranted Call to API>>>>>>>>>>>>>>");
        APIServiceFactory serviceFactory = new APIServiceFactory();
        // start service call using RxJava2
        serviceFactory.getAPIConfiguration().registrationAPI( requestParams )
                .subscribeOn(Schedulers.io()) //Asynchronously subscribes Observable to perform action in I/O Thread.
                .observeOn(AndroidSchedulers.mainThread()) // To perform its emissions and response on UiThread(or)MainThread.
                .subscribe(new DisposableObserver<GeneralPojo>() { // It would dispose the subscription automatically. If you wish to handle it use io.reactivex.Observer
                    @Override
                    public void onNext(GeneralPojo generalPojo) {
                        Log.d(TAG,TAG+" onNext");
                        alterProgressBar();
                        int statusCode = (int) generalPojo.getStatusCode();
                        //Check for error
                        if(statusCode!=mApiConstants.SUCCESS){
                            Log.d(TAG,TAG+" onNext ERROR statusCode = "+statusCode);
                            String errorMessage = generalPojo.getError().getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                showErrorAlert(errorMessage);
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }else{
                            Log.d(TAG,TAG+" onNext statusCode = "+statusCode);
                                String isVerified = generalPojo.getData().getIsVerified();
                                String isActive = generalPojo.getData().getActive();

                            if(!TextUtils.isEmpty(isVerified) && !TextUtils.isEmpty(isActive)) {

                                if (isVerified.equalsIgnoreCase(mApiConstants.STATUS_1) && isActive.equalsIgnoreCase(mApiConstants.STATUS_1)) {
                                    Log.d(TAG,TAG+" onNext  = isVerified = 1 && isActive = 1");
                                    //User registered and verified email, but would have deleted the app
                                    showErrorAlert(getString(R.string.already_registered));
                                    mSharedPreference.setPreferenceInt(mSharedPreference.VERIFIED_STATUS, 1);

                                } else if (isVerified.equalsIgnoreCase(mApiConstants.STATUS_1) && isActive.equalsIgnoreCase(mApiConstants.STATUS_0)) {
                                    Log.d(TAG,TAG+" onNext  = isVerified = 1 && isActive = 0");
                                    //When registered user is banned from backend due to xyz reason. Contact support.
                                    showErrorAlert(getString(R.string.user_blocked));
                                    mSharedPreference.setPreferenceInt(mSharedPreference.VERIFIED_STATUS, 1);

                                } else if (isVerified.equalsIgnoreCase(mApiConstants.STATUS_0) && isActive.equalsIgnoreCase(mApiConstants.STATUS_0)) {
                                    Log.d(TAG,TAG+" onNext  = isVerified = 0 && isActive = 0");
                                    //Email Not verified
                                    mSharedPreference.setPreferenceInt(mSharedPreference.VERIFIED_STATUS, 0);
                                    //show OTP screen
                                    showOTPScreen();
                                }
                            }else{
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
    void closeApp(){
        finishAffinity();
    }
}
