package wash.midest.com.mrwashapp.appservices;

/**
 * Created by Sreehari.KV on 4/21/2018.
 */

public interface APICallBack {
    public void processedResponse(Object responseObj,boolean isSuccess,String errorMsg);
}
