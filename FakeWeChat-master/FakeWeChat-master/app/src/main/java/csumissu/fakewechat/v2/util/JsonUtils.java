package csumissu.fakewechat.v2.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.greendao.annotation.NotNull;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class JsonUtils {

    private static Gson sGson;

    static {
        sGson = new GsonBuilder()
                .setDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
                .serializeNulls()
                .create();
    }

    public static Converter.Factory createConverterFactory() {
        return GsonConverterFactory.create(sGson);
    }

    public static String toJson(Object object) {
        return sGson.toJson(object);
    }

    public static <T> T fromJson(String json, @NotNull Class<T> type) {
        return sGson.fromJson(json, type);
    }

    public static <T> List<T> fromJson(String json, Type type) {
        return sGson.fromJson(json, type);
    }

}
