package wash.midest.com.mrwashapp.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
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
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.mrwashapp.MrWashApp;

public class NewPasswordActivity extends BaseActivity {

    private static final String TAG = NewPasswordActivity.class.getName();
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private boolean mIsProgressShown =false;
    @BindView(R.id.new_pass)    TextInputEditText mNewPass;
    @BindView(R.id.confirm_pass)    TextInputEditText mConfirmPass;
    @BindView(R.id.submit_btn)    Button mBtnSubmit;
    private String mMemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        ButterKnife.bind(this);
        mMemberId = getIntent().getExtras().getString("memberId");
        setActionBarTitleInCenter(getString(R.string.app_title),true);
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

    @OnClick(R.id.submit_btn)
    void submitAndValidate(){
        boolean isValid=true;
        if(TextUtils.isEmpty(mNewPass.getText().toString().trim())){
            mNewPass.setError(getResources().getString(R.string.password_error));
            isValid=false;
        }
        else if(!isValidPassword(mNewPass)){
            mNewPass.setError(getResources().getString(R.string.password_criteria));
            isValid=false;
        }
        else if(TextUtils.isEmpty(mConfirmPass.getText().toString().trim())){
            mConfirmPass.setError(getResources().getString(R.string.confirm_password_error));
            isValid=false;
        }
        else if(!isValidPassword(mConfirmPass)){
            mConfirmPass.setError(getResources().getString(R.string.password_criteria));
            isValid=false;
        }
        else if(!mNewPass.getText().toString().trim() .contentEquals(mConfirmPass.getText().toString().trim())){
            mConfirmPass.setError(getResources().getString(R.string.password_match_error));
            isValid=false;
        }
        if(isValid){
            proceedAPICall();
        }
    }

    private boolean isValidPassword(TextInputEditText passField){
        String passwordEntered=passField.getText().toString().trim();
        if(!TextUtils.isEmpty(passwordEntered)){
            return mAppUtils.isValidPassword(passwordEntered);
        }else{
            return false;
        }
    }

    void proceedAPICall(){
        if(!MrWashApp.getMrWashApp().isAppActive()){
            return;
        }
        if(!mAppUtils.isNetworkConnected(this)){
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        String appId = mApiConstants.APPID_VAL;
        HashMap<String,String> requestParams=new HashMap<>();
        requestParams.put(mApiConstants.API_PASSWORD,mNewPass.getText().toString().trim());
        requestParams.put(mApiConstants.API_PASSWORDCONFIRM,mConfirmPass.getText().toString().trim());
        requestParams.put(mApiConstants.API_APPID,appId);
        requestParams.put(mApiConstants.API_MEMBERID,mMemberId);

        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().updateNewPassAPI( requestParams )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {

                        alterProgressBar();
                        int statusCode = generalPojo.getStatusCode();
                        //Check for error
                        if(statusCode!=mApiConstants.SUCCESS){
                            String errorMessage = generalPojo.getError().get(0).getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                showErrorAlert(errorMessage);
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }else{
                            showAlert(getString(R.string.login_redirect));
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

    void showAlert(String message){
        if(!MrWashApp.getMrWashApp().isAppActive()){
            return;
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                clearTopActivitiesAndPushLogin();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    void clearTopActivitiesAndPushLogin(){
        if(!MrWashApp.getMrWashApp().isAppActive()){
            return;
        }
        Intent intent = new Intent(NewPasswordActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
