package wash.midest.com.mrwashapp.appservices;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import wash.midest.com.mrwashapp.R;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.GeneralPojo;

/**
 * Created by Sreehari.KV on 4/21/2018.
 */

public class APIProcessor {

    //----------------------  Class Variables -------------------------------//

    private final String TAG=APIProcessor.class.getName();
    private final APIConstants mApiConstants = new APIConstants();

    //--------------------------------  Methods  --------------------------------//

    public void processPriceList(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();

        /*requestParams.put(mApiConstants.API_EMAIL,email);
        requestParams.put(mApiConstants.API_PASSWORD,password);
        requestParams.put(mApiConstants.API_APPID,appId);*/
        serviceFactory.getAPIConfiguration().getPriceList(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralPojo>() {
                    @Override
                    public void onNext(GeneralPojo vehicles) {
                        /*Log.d(TAG, "### The API service success");
                        callBack.processedResponse(vehicles,true,null);*/
                    }
                    @Override
                    public void onError(Throwable e) {
                        /*Log.e(TAG, "### The API service error");
                        callBack.processedResponse(null,false,"Error in processing request. Want to try again");*/
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void processFAQ(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();

        /*requestParams.put(mApiConstants.API_EMAIL,email);
        requestParams.put(mApiConstants.API_PASSWORD,password);
        requestParams.put(mApiConstants.API_APPID,appId);*/
        serviceFactory.getAPIConfiguration().getFAQ(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralPojo>() {
                    @Override
                    public void onNext(GeneralPojo generalPojo) {
                        int statusCode = generalPojo.getStatusCode();
                        //Check for error
                        if(statusCode!=mApiConstants.SUCCESS){
                            String errorMessage = generalPojo.getError().get(0).getErrMessage();
                            if(!TextUtils.isEmpty(errorMessage)){
                                callBack.processedResponse(null,false,errorMessage);
                            }else {
                                callBack.processedResponse(null,false,"Error in processing request. Please try later");
                            }
                        }else{
                            Log.d(TAG, "### The API service success");
                            callBack.processedResponse(generalPojo,true,null);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "### The API service error");
                        callBack.processedResponse(null,false,"Error in processing request. Want to try again");
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

}
