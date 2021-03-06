package wash.midest.com.mrwashapp.screens;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APIConstants;
import wash.midest.com.mrwashapp.localstorage.AppSharedPreferences;
import wash.midest.com.mrwashapp.mrwashapp.MrWashApp;
import wash.midest.com.mrwashapp.utils.AppUtils;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView mActionBarTitle;
    private ImageView mBackBtn;
    protected AppUtils mAppUtils;
    protected APIConstants mApiConstants;
    protected AppSharedPreferences mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mAppUtils= new AppUtils();
        mApiConstants=new APIConstants();
        mSharedPreference=AppSharedPreferences.getInstnace(this);
    }

    protected void setActionBarTitleInCenter(String title,boolean displayBackButton){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setCustomView(R.layout.action_bar_layout);
        mActionBarTitle=findViewById(R.id.action_bar_title);
        mActionBarTitle.setText(title);

        if(displayBackButton){
            mBackBtn=findViewById(R.id.back_btn_title);
            mBackBtn.setOnClickListener(this);
        }else{
            mBackBtn.setVisibility(View.GONE);
        }
    }

    protected void showToast(final String msg){

        if(MrWashApp.getMrWashApp().isAppActive()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            });
        }

    }

    void showErrorAlert(String message){
        if(MrWashApp.getMrWashApp().isAppActive()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage(message);
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
    }
    protected Context getAppContext(){
        return getApplicationContext();
    }

    @Override
    public void onClick(View v) {
        if(v==mBackBtn){
            finish();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
