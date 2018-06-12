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
    Observable<GeneralListDataPojo> registrationAPI(@FieldMap(encoded = true) Map<String, String> params);



    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/login")
    Observable<GeneralListDataPojo> loginAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/verify-email")
    Observable<GeneralListDataPojo> verifyEmailAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/forgot-password")
    Observable<GeneralListDataPojo> forgotPassAPI(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/verify-forgot-password")
    Observable<GeneralListDataPojo> forgotPassCodeVerifyAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("api/services")
    Observable<GeneralListDataPojo> servicesAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/resend-verification-code")
    Observable<GeneralListDataPojo> resendOTPAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/update-password")
    Observable<GeneralListDataPojo> updateNewPassAPI(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/update-password")
    Observable<GeneralListDataPojo> getPriceList(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("api/faq")
    Observable<GeneralListDataPojo> getFAQ(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/my-profile")
    Observable<GeneralListDataPojo> getMyProfile(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/update-my-profile")
    Observable<GeneralListDataPojo> updateMyProfile(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("api/contactus")
    Observable<GeneralListDataPojo> contactUs(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("api/pricelist")
    Observable<GeneralPojo> priceList(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/my-orders")
    Observable<GeneralListDataPojo> myOrder(@FieldMap(encoded = true) Map<String, String> params);


    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("order/generate")
    Observable<GeneralListDataPojo> generateOrder(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/my-offers")
    Observable<GeneralListDataPojo> getMyOffers(@FieldMap(encoded = true) Map<String, String> params);

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("order/validate-promo-code")
    Observable<GeneralListDataPojo> validateCode(@FieldMap(encoded = true) Map<String, String> params);
}
