package csumissu.fakewechat.main.source;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author sunyaxi
 * @date 2016/7/5
 */
public interface TuringRobotApi {

    public static final String HOST = "http://www.tuling123.com/";
    public static final String API_KEY = "2a0137fce0c2c9c26e83cee8e4f9bc15";

    @POST("openapi/api")
    Observable<TuringResponse> ask(@Body TuringRequest request);

}
