package wash.midest.com.mrwashapp.appservices;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import wash.midest.com.mrwashapp.models.GeneralListDataPojo;
import wash.midest.com.mrwashapp.models.GeneralPojo;

/**
 * Created by Sreehari.KV on 3/14/2018.
 */

public interface APIConfiguration {

    String BASE_URL = "http://www.qmyse.com/";

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/registration")
    Observable<GeneralPojo> registrationAPI(@FieldMap(encoded = true) Map<String, String> params);



    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("api/login")
    Observable<GeneralPojo> loginAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/verify-email")
    Observable<GeneralPojo> verifyEmailAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("api/forgot-password")
    Observable<GeneralPojo> forgotPassAPI(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("api/verify-forgot-password")
    Observable<GeneralPojo> forgotPassCodeVerifyAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("api/services")
    Observable<GeneralListDataPojo> servicesAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/resend-verification-code")
    Observable<GeneralPojo> resendOTPAPI(@FieldMap(encoded = true) Map<String, String> params);
}
