package wash.midest.com.mrwashapp.screens;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.utils.AppUtils;

public class SplashActivity extends BaseActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                checkDB();

            }
        }, SPLASH_TIME_OUT);
    }
    private void checkDB(){
        //Check user registration and proceed
        boolean isPermissionRequired = new AppUtils().isVersionGreaterThanM(getApplicationContext());

        if(isPermissionRequired) {
            checkPermission();
        }else{
            postPermissionGranted();
        }
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
        Intent i = new Intent(SplashActivity.this, SubSplashActivity.class);
        startActivity(i);
        finish();
    }

    void closeApp(){
        finishAffinity();
    }

}
