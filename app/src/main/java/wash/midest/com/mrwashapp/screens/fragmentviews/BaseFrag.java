package wash.midest.com.mrwashapp.screens.fragmentviews;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.Toast;

import wash.midest.com.mrwashapp.appservices.APIConstants;
import wash.midest.com.mrwashapp.localstorage.AppSharedPreferences;
import wash.midest.com.mrwashapp.screens.uiutility.AlertCallBack;
import wash.midest.com.mrwashapp.utils.AppUtils;

/**
 * Created by Sreehari.KV on 4/21/2018.
 */

public class BaseFrag extends Fragment implements AlertCallBack{

    protected AppUtils mAppUtils;
    protected AlertDialog mCallBackAlertDialog;
    protected APIConstants mApiConstants;
    protected AppSharedPreferences mSharedPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppUtils=new AppUtils();
        mApiConstants=new APIConstants();
        mSharedPreference=AppSharedPreferences.getInstnace(getActivity());
    }

    /**
     * Show alert message with call back
     * @param message message to show in alert
     * @param positiveBtnText positive button text
     * @param negativeBtnText negative button text
     */
    protected void showMessage(String message,int positiveBtnText,int negativeBtnText,final int caseNum){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(message);

        dialogBuilder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                handlePositiveAlertCallBack(caseNum);
            }
        });
        dialogBuilder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                handleNegativeAlertCallBack(caseNum);
            }
        });
        mCallBackAlertDialog = dialogBuilder.create();
        mCallBackAlertDialog.setCancelable(false);
        mCallBackAlertDialog.show();
    }

    public void showMessage(String message,int positiveBtnText,final int caseNum){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(message);

        dialogBuilder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                handlePositiveAlertCallBack(caseNum);
            }
        });
        mCallBackAlertDialog = dialogBuilder.create();
        mCallBackAlertDialog.setCancelable(false);
        mCallBackAlertDialog.show();
    }
    @Override
    public void handleNegativeAlertCallBack(int caseNum) {

    }

    @Override
    public void handlePositiveAlertCallBack(int caseNum) {

    }

    protected void showToast(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),msg,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }
    protected void showToast(final String msg,final int duration){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),msg,duration);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

        protected void showErrorAlert(String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
