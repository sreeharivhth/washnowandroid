package wash.midest.com.mrwashapp.screens;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wash.midest.com.mrwashapp.R;

public class SplashActivity extends BaseActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

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
        //Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        Intent i = new Intent(SplashActivity.this, SubSplashActivity.class);
        startActivity(i);
        finish();
    }
}
