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

    @Headers({"Accept:application/json",
            "Content-Type:application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("member/registration")
    Observable<RegistrationPojo> fetchRegistrationInformation(@FieldMap(encoded = true) Map<String, String> params);
}
