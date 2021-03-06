package wash.midest.com.mrwashapp.appservices;



import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wash.midest.com.mrwashapp.utils.AppUtils;

/**
 * Created by Sreehari.KV on 3/14/2018.
 */

public class APIServiceFactory {

    public APIConfiguration getAPIConfiguration(){
        OkHttpClient okHttpClient = getOkHttpClient();
        Retrofit retrofit = provideAPIRetrofit(okHttpClient);
        return retrofit.create(APIConfiguration.class);
    }
    /**
     * This will gives the {@link OkHttpClient} instance with 30 seconds timeout for Read and Connect timeout limitation.
     * and also It has set {@link okhttp3.logging.HttpLoggingInterceptor.Logger} information to print the all information
     * about your service call like URL, Header if available, body content, Http Type, etc,,,.
     *
     * @return
     */
    private OkHttpClient getOkHttpClient() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // 60 seconds Connection Timeout
                .readTimeout(60, TimeUnit.SECONDS) // 60 seconds Read Timeout
                .addInterceptor(httpLoggingInterceptor) // Logger for Api call
                .build();
    }


    /**
     * It returns retrofit instance to make API call for given URL with RxJava2 support.
     *
     * @param okHttpClient - it provide all timeout info, logger info and all necessary
     *                     information to {@link Retrofit} by {@link OkHttpClient}
     * @return
     */
    private Retrofit provideAPIRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // provides RxJava2 webservice call support here.
                .client(okHttpClient) // Sets OkHttpClient.
                .addConverterFactory(providesGsonConverterFactory()) // Set Gson converter here
                .baseUrl(APIConfiguration.BASE_URL).build();
    }

    /**
     * It provide Gson instance to convert Json to Pojo.
     *
     * @return
     */
    private GsonConverterFactory providesGsonConverterFactory() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return GsonConverterFactory.create(gsonBuilder.create());
    }

    private static final Interceptor cache_control_interceptor = new Interceptor() {
        @Override public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            /*if (new AppUtils().isNetworkConnected()) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }*/

            return null;
        }};
}
