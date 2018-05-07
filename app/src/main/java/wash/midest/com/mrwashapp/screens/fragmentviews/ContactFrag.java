package wash.midest.com.mrwashapp.screens.fragmentviews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.appservices.APICallBack;
import wash.midest.com.mrwashapp.appservices.APIProcessor;
import wash.midest.com.mrwashapp.models.Data;
import wash.midest.com.mrwashapp.models.GeneralPojo;
import wash.midest.com.mrwashapp.screens.LandingActivity;

/**
 * Created by Sreehari.KV on 5/7/2018.
 */

public class ContactFrag extends BaseFrag implements APICallBack{

    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.phone_num)
    TextView mPhone;
    @BindView(R.id.contact_address)
    TextView mAddress;

    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBar;
    private Unbinder mUnbinder;

    public ContactFrag(){
    }

    public static ContactFrag newInstance(){
        ContactFrag contactFrag=new ContactFrag();
        return contactFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_contact_us, container, false);
        ((LandingActivity) getActivity()).setFragmentTitle(getActivity().getString(R.string.action_contact_us));
        mUnbinder= ButterKnife.bind(this, view);

        HashMap<String,String> requestParams=new HashMap<>();
        String appId = mApiConstants.APPID_VAL;
        requestParams.put(mApiConstants.API_ACCESSTOKEN,appId);
        mProgressBar.setVisibility(View.VISIBLE);
        APIProcessor apiProcessor=new APIProcessor();
        apiProcessor.getContactUS(ContactFrag.this,requestParams);
        return view;
    }

    @Override
    public void processedResponse(Object responseObj, boolean isSuccess, String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        if(isSuccess) {
            GeneralPojo responsePojo = (GeneralPojo) responseObj;
            Data data = ((GeneralPojo) responseObj).getData().get(0);

            mEmail.setText(data.getEmail());
            mPhone.setText(data.getPhone());
            mAddress.setText(data.getAddress());

        }else{
            showMessage(errorMsg,R.string.ok);
        }
    }
}
