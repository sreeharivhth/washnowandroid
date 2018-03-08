package wash.midest.com.mrwashapp.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wash.midest.com.mrwashapp.R;

public class OtpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
    }


    private void doLandingAction(){
        Intent i = new Intent(OtpActivity.this, LandingActivity.class);
        startActivity(i);
    }
}
