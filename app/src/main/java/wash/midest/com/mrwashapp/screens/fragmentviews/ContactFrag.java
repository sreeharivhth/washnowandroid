package wash.midest.com.mrwashapp.screens.fragmentviews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @OnClick(R.id.phone_num)
    void makeCall(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String phoneNum = "tel:"+mPhone.getText();
        callIntent.setData(Uri.parse(phoneNum));
        startActivity(callIntent);
    }

    @OnClick(R.id.email)
    void sendMail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        String[] TO = {mEmail.getText().toString()};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mr Wash Query");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Query details");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }


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