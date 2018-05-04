package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIConstants;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.screens.LandingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFrag extends BaseFrag implements APICallBack{

    @BindView(R.id.first_name)TextInputEditText mFName;
    @BindView(R.id.last_name) TextInputEditText mLName;
    @BindView(R.id.phone) TextInputEditText mPhone;
    @BindView(R.id.email) TextInputEditText mEmail;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    private Menu menu;
    private boolean isEditing;
    private boolean mEditService;
    private Unbinder mUnbinder;

    public MyProfileFrag() {
        // Required empty public constructor
    }

    public static MyProfileFrag newInstance(){
        MyProfileFrag myProfileFrag = new MyProfileFrag();
        return myProfileFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);
        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.action_profile));
        mUnbinder= ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        mFName.setEnabled(false);
        mPhone.setEnabled(false);
        mLName.setEnabled(false);
        mEmail.setEnabled(false);

        mProgressBar.setVisibility(View.VISIBLE);
        mEditService=false;
        HashMap<String,String> requestParams=new HashMap<>();
        //TODO keep actual mToken, once handled from backend
        String appId = mApiConstants.APPID_VAL;
        requestParams.put(mApiConstants.API_APPID,appId);
        requestParams.put(mApiConstants.API_MEMBERID,mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.processMyProfile(MyProfileFrag.this,requestParams);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.profile_one, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_edit:
                if(isEditing){
                    if(isValidEntry()){
                        isEditing=false;
                        editCompleted();
                    }
                }else{
                    isEditing=true;
                    processEdit();
                }

                return false;

            default:
                break;
        }

        return false;
    }
    void processEdit(){
        alterEntryFields(true);
        MenuItem editMenu = menu.findItem(R.id.action_edit);
        editMenu.setTitle("Done");
    }

    void editCompleted(){
        alterEntryFields(false);
        MenuItem editMenu = menu.findItem(R.id.action_edit);
        editMenu.setTitle("Edit");
        mEditService=true;
        processEditService();
    }

    void alterEntryFields(boolean status){
        mFName.setEnabled(status);
        mPhone.setEnabled(status);
        mLName.setEnabled(status);
        mEmail.setEnabled(status);
    }
    void processEditService(){
        mProgressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> requestParams=new HashMap<>();
        //TODO keep actual mToken, once handled from backend
        String appId = mApiConstants.APPID_VAL;
        requestParams.put(mApiConstants.API_APPID,appId);
        requestParams.put(mApiConstants.API_FIRSTNAME,mFName.getText().toString().trim());
        requestParams.put(mApiConstants.API_LASTNAME,mLName.getText().toString().trim());
        requestParams.put(mApiConstants.API_MOBILE,mPhone.getText().toString().trim());
        requestParams.put(mApiConstants.API_MEMBERID,mSharedPreference.getPreferenceString(mSharedPreference.MEMBER_ID));
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.updateMyProfile(MyProfileFrag.this,requestParams);
    }
    boolean isValidEntry(){
        boolean isValid=true;
        if(TextUtils.isEmpty(mFName.getText().toString().trim())){
            mFName.setError(getResources().getString(R.string.first_name_error));
            isValid=false;
        }
        if(TextUtils.isEmpty(mLName.getText().toString().trim())){
            mLName.setError(getResources().getString(R.string.last_name_error));
            isValid=false;
        }
        if(TextUtils.isEmpty(mPhone.getText().toString().trim())){
            mPhone.setError(getResources().getString(R.string.phone_error));
            isValid=false;
        }
        return isValid;
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        if(isSuccess) {
            GeneralPojo responsePojo = (GeneralPojo) responseObj;
            if(!mEditService){
                Data data = responsePojo.getData().get(0);
                String fn = data.getFirstName();
                if(!TextUtils.isEmpty(fn))
                    mFName.setText(fn);
                String ln = data.getLastName();
                if(!TextUtils.isEmpty(ln))
                    mLName.setText(ln);
                String email = data.getEmail();
                if(!TextUtils.isEmpty(email))
                    mEmail.setText(email);
                String phone = data.getMobile();
                if(!TextUtils.isEmpty(phone))
                    mPhone.setText(phone);
            }else{
                showMessage(getString(R.string.profile_updated),R.string.ok);
            }
        }else{
            showMessage(errorMsg,R.string.ok);
        }
    }
}
