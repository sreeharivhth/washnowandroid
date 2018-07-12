package wash.midest.com.mrwashapp.screens;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
import wash.midest.com.mrwashapp.appservices.APIServiceFactory;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.PromotionData;
import wash.midest.com.mrwashapp.screens.fragmentviews.offers.OfferFrag;
import wash.midest.com.mrwashapp.utils.AppUtils;

public class SplashActivity extends BaseActivity implements APICallBack{

    private static final String TAG = "SplashActivity";
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    private boolean mIsProgressShown =false;
    private GeneralListDataPojo mGeneralPojo;
    private PromotionData promotionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                checkDB();

            }
        }, SPLASH_TIME_OUT);
    }
    private void checkDB(){
        //Check user registration and proceed
        /*boolean isPermissionRequired = new AppUtils().isVersionGreaterThanM(getApplicationContext());

        if(isPermissionRequired) {
            checkPermission();
        }else{
            postPermissionGranted();
        }*/
        postPermissionGranted();
    }

    void checkPermission(){
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_PHONE_STATE)) {

                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else {
            //Permission already granted. Proceed with functionalities
            postPermissionGranted();
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

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SplashActivity.this);
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

    void postPermissionGranted(){

        String isActive = mSharedPreference.getPreferenceString(mSharedPreference.ACTIVE_STATUS);

        /*String memberID = mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID);
        Log.d(TAG,"memberID = "+memberID);*/

        if(!TextUtils.isEmpty(isActive) && isActive.equalsIgnoreCase("1")){
            processServicesAPI();
        }else{
            Intent i = new Intent(SplashActivity.this, SubSplashActivity.class);
            startActivity(i);
            finish();
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

    void processServicesAPI(){
        if(!mAppUtils.isNetworkConnected(this)){
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        alterProgressBar();
        Log.d(TAG," processServicesAPI()");
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
                            Log.d(TAG," onNext error statusCode = "+statusCode);
                            String errorMessage = generalPojo.getError().get(0).getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                showErrorAlert(errorMessage);
                            }else{
                                showErrorAlert(getString(R.string.general_error_server));
                            }
                        }else{
                            Log.d(TAG," onNext success statusCode = "+statusCode);
                            mGeneralPojo = generalPojo;
                            //Send data using RxEvent Bus
                            //doLandingAction(generalPojo);
                            getOfferData();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, " processServicesAPI Error "+e.toString());
                        alterProgressBar();
                        showErrorAlert(getString(R.string.general_error_server));
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "### The API service Observable has ended!");
                    }
                });
    }

    void getOfferData(){
        if(!mAppUtils.isNetworkConnected(this)){
            showErrorAlert(getString(R.string.network_error));
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> requestParams=new HashMap<>();
        requestParams.put(mApiConstants.API_MEMBERID,mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.getOffersResponse(this,requestParams);
    }

    void closeApp(){
        finishAffinity();
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        promotionData = new PromotionData();
        if (isSuccess) {
            List<Data>list = ((GeneralListDataPojo) responseObj).getData();
            promotionData.setPromotionData(list);
        }
        doLandingAction();
    }

    private void doLandingAction(){
        Log.d(TAG,TAG+" doLandingAction");
        Intent i = new Intent(SplashActivity.this, LandingActivity.class);
        i.putExtra("LandingData",mGeneralPojo);
        i.putExtra("PromotionData",promotionData);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
