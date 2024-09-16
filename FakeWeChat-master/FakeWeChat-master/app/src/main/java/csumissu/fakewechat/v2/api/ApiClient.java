package csumissu.fakewechat.v2.api;

import csumissu.fakewechat.v2.util.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class ApiClient {

    public static <T> T create(Class<T> service) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(chain -> {
            Request finalRequest = chain.request().newBuilder()
                    .build();
            return chain.proceed(finalRequest);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.API_HOST)
                .client(httpClientBuilder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JsonUtils.createConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(service);
    }
}
