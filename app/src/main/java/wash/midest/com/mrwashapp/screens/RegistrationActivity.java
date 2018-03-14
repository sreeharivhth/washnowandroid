package wash.midest.com.mrwashapp.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APIServiceFactory;
import wash.midest.com.mrwashapp.models.RegistrationPojo;
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
        showOTPScreen();
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
        APIServiceFactory serviceFactory = new APIServiceFactory();
        // start service call using RxJava2
        serviceFactory.getAPIConfiguration().fetchRegistrationInformation()
                .subscribeOn(Schedulers.io()) //Asynchronously subscribes Observable to perform action in I/O Thread.
                .observeOn(AndroidSchedulers.mainThread()) // To perform its emissions and response on UiThread(or)MainThread.
                .subscribe(new DisposableObserver<RegistrationPojo>() { // It would dispose the subscription automatically. If you wish to handle it use io.reactivex.Observer
                    @Override
                    public void onNext(RegistrationPojo gist) {
                        StringBuilder sb = new StringBuilder();
                        // Output
                        /*for (Map.Entry<String, GistRepo> entry : gist.files.entrySet()) {
                            sb.append(entry.getKey());
                            sb.append(" - ");
                            sb.append("Length of file ");
                            sb.append(entry.getValue().content.length());
                            sb.append("\n");
                        }
                        hideProgress();
                        responseView.setText(sb.toString());*/
                    }
                    @Override
                    public void onError(Throwable e) {
                        /*hideProgress();
                        responseView.setText("Error occurred! Check your Logcat!");
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace(); // Just to see complete log information. we can comment if not necessary!
                        */
                    }
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "The Gist service Observable has ended!");
                    }
                });
    }
}
