package wash.midest.com.mrwashapp.appservices;

import io.reactivex.Observable;
import retrofit2.http.GET;
import wash.midest.com.mrwashapp.models.RegistrationPojo;

/**
 * Created by Sreehari.KV on 3/14/2018.
 */

public interface APIConfiguration {

    String BASE_URL = "https://api.github.com/";

    @GET("gists/59488f02db24ebd83450289e0b0f9ff7")
    Observable<RegistrationPojo> fetchRegistrationInformation();
}
