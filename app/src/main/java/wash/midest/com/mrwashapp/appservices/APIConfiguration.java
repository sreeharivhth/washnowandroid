package wash.midest.com.mrwashapp.appservices;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import wash.midest.com.mrwashapp.models.RegistrationPojo;

/**
 * Created by Sreehari.KV on 3/14/2018.
 */

public interface APIConfiguration {

    String BASE_URL = "http://www.qmyse.com/";
    String APPID = "110";
    String DIALINGCODE = "978";
    /*@Headers({"Accept: application/json"})*/
    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    /*"Content-Type:application/json;charset=UTF-8;x-www-form-urlencoded"*/
    @FormUrlEncoded
    @POST("member/registration")
    /*Observable<RegistrationPojo> fetchRegistrationInformation(@Body RegistrationObj params);*/

    Observable<RegistrationPojo> fetchRegistrationInformation(@FieldMap(encoded = true) Map<String, String> params);


    /*String BASE_URL = "http://www.qmyse.com/";
    String APPID = "110";
    //@FormUrlEncoded
    @GET("site/pingpong")
    @Headers({"Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"})
    Observable<RegistrationPojo> fetchRegistrationInformation();*/
}
