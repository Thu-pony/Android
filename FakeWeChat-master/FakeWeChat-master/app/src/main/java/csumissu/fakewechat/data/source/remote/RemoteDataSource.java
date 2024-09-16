package csumissu.fakewechat.data.source.remote;

import android.util.Log;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import csumissu.fakewechat.data.FriendshipResult;
import csumissu.fakewechat.data.Status;
import csumissu.fakewechat.data.StatusResult;
import csumissu.fakewechat.data.User;
import csumissu.fakewechat.data.source.EntityDataSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * @author sunyaxi
 * @date 2016/5/20
 */
public class RemoteDataSource implements EntityDataSource {

    private static final String TAG = RemoteDataSource.class.getSimpleName();
    private static RemoteDataSource INSTANCE;
    private WeiboApi mWeiboApi;

    private RemoteDataSource() {
        Gson gson = new GsonBuilder()
                .setDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
                .serializeNulls()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mWeiboApi = retrofit.create(WeiboApi.class);
    }

    public synchronized static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<User> getOwner() {
        return mWeiboApi.getUser(WeiboApi.ACCESS_TOKEN, WeiboApi.OWNER_UID);
    }

    @Override
    public void saveOwner(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<FriendshipResult> getFriends() {
        return mWeiboApi.getFriends(WeiboApi.ACCESS_TOKEN, WeiboApi.OWNER_UID)
                .map(result -> {
                    if (result.getUsers() == null) {
                        result.setUsers(new ArrayList<>());
                    }
                    return result;
                })
                .map(result -> {
                    for (User user : result.getUsers()) {
                        String pinyin = PinyinHelper.convertToPinyinString(
                                user.getName().trim(), "", PinyinFormat.WITHOUT_TONE);
                        user.setPinyin(pinyin);
                    }
                    Collections.sort(result.getUsers(),
                            (u1, u2) -> u1.getPinyin().compareTo(u2.getPinyin()));
                    return result;
                })
                .map(result -> {
                    HashMap<Character, Integer> map = result.getLetterPositionMap();
                    List<User> users = result.getUsers();
                    for (int i = 0; i < users.size(); i++) {
                        char c = users.get(i).getPinyin().charAt(0);
                        if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                            c = '#';
                        }
                        if (!map.containsKey(c)) {
                            map.put(c, i);
                        }
                    }
                    return result;
                });
    }

    @Override
    public void saveFriends(FriendshipResult result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<StatusResult> getStatusResult() {
        return mWeiboApi.getPublicStatuses(WeiboApi.ACCESS_TOKEN, 200);
    }

    @Override
    public void saveStatusResult(StatusResult statusResult) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<List<Status>> getStatuses(int count, int page) {
        return getStatusResult().map(statusResult -> {
            int size = statusResult.getStatuses().size();
            int startIndex = Math.max(0, Math.min(count * page, size - 1));
            int endIndex = Math.max(startIndex, Math.min(count * (page + 1), size));
            Log.d(TAG, "size=" + size + ", start=" + startIndex + ", end=" + endIndex);
            return statusResult.getStatuses().subList(startIndex, endIndex);
        });

    }
}
