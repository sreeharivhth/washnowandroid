package wash.midest.com.mrwashapp.appservices;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
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

    /*public void processPriceList(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();

        *//*requestParams.put(mApiConstants.API_EMAIL,email);
        requestParams.put(mApiConstants.API_PASSWORD,password);
        requestParams.put(mApiConstants.API_APPID,appId);*//*
        serviceFactory.getAPIConfiguration().getPriceList(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralPojo>() {
                    @Override
                    public void onNext(GeneralPojo vehicles) {
                        *//*Log.d(TAG, "### The API service success");
                        callBack.processedResponse(vehicles,true,null);*//*
                    }
                    @Override
                    public void onError(Throwable e) {
                        *//*Log.e(TAG, "### The API service error");
                        callBack.processedResponse(null,false,"Error in processing request. Want to try again");*//*
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }*/

    public void processFAQ(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();

        /*requestParams.put(mApiConstants.API_EMAIL,email);
        requestParams.put(mApiConstants.API_PASSWORD,password);
        requestParams.put(mApiConstants.API_APPID,appId);*/
        serviceFactory.getAPIConfiguration().getFAQ(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {
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

    public void processMyProfile(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().getMyProfile(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {
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

    public void updateMyProfile(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().updateMyProfile(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {
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

    public void getContactUS(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().contactUs(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {
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

    public void getPriceList(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().priceList(requestParams)
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

    public void getMyOrder(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().myOrder(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {
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


    public void getOrderDetails(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().orderDetails(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {
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

    public void postGenerateOrder(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().generateOrder(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {
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
                        Log.e(TAG, "### The API service error"+e.toString());
                        callBack.processedResponse(null,false,"Error in processing request. Want to try again");
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getOffersResponse(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().getMyOffers(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GeneralListDataPojo>() {
                    @Override
                    public void onSuccess(GeneralListDataPojo generalPojo) {
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
                        Log.e(TAG, "### The API service error"+e.toString());
                        callBack.processedResponse(null,false,"Error in processing request. Want to try again");
                    }
                });

    }

    public void validatePromoCode(final APICallBack callBack, HashMap<String,String> requestParams){
        APIServiceFactory serviceFactory = new APIServiceFactory();
        serviceFactory.getAPIConfiguration().validateCode(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GeneralListDataPojo>() {
                    @Override
                    public void onNext(GeneralListDataPojo generalPojo) {
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
                        Log.e(TAG, "### The API service error"+e.toString());
                        callBack.processedResponse(null,false,"Error in processing request. Want to try again");
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }
}
