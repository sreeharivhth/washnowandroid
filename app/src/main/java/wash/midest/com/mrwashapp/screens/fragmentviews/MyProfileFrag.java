package wash.midest.com.mrwashapp.screens.fragmentviews;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
        mProgressBar.setVisibility(View.VISIBLE);
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
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        if(isSuccess) {
            GeneralPojo responsePojo = (GeneralPojo) responseObj;
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
            showMessage(errorMsg,R.string.ok);
        }
    }
}
