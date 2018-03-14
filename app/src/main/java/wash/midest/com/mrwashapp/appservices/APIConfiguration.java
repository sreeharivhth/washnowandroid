package wash.midest.com.mrwashapp.appservices;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
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

    @POST("member/registration")
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    Observable<RegistrationPojo> fetchRegistrationInformation(@Body HashMap<String, String> userDetails);
}
