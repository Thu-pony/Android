package csumissu.fakewechat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import csumissu.fakewechat.main.source.TuringRequest;
import csumissu.fakewechat.main.source.TuringRobotApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author sunyaxi
 * @date 2016/7/5
 */
public class TuringRobotTest {

    TuringRobotApi robotApi;

    @Before
    public void setUp() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(TuringRobotApi.HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        robotApi = retrofit.create(TuringRobotApi.class);
    }

    @Test
    public void testAsk() throws Exception {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("key", TuringRobotApi.API_KEY);
//        map.put("info", "你多大了?");
//        map.put("userid", 1234);

        TuringRequest request = new TuringRequest("看下新闻!");
        robotApi.ask(request).subscribe(System.out::println);
        synchronized (this) {
            wait(5000);
        }
        System.out.println("finish");
    }

}
