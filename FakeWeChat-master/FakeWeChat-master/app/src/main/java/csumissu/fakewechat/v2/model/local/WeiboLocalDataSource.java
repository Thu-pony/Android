package csumissu.fakewechat.v2.model.local;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import csumissu.fakewechat.v2.AppProps;
import csumissu.fakewechat.v2.api.ApiConstants;
import csumissu.fakewechat.v2.bean.Status;
import csumissu.fakewechat.v2.bean.User;
import csumissu.fakewechat.v2.dagger.ForApplication;
import csumissu.fakewechat.v2.dagger.ReadableDb;
import csumissu.fakewechat.v2.dagger.WritableDb;
import csumissu.fakewechat.v2.dao.DaoSession;
import csumissu.fakewechat.v2.dao.UserDao;
import csumissu.fakewechat.v2.model.WeiboDataSource;
import csumissu.fakewechat.v2.util.DiskCache;
import csumissu.fakewechat.v2.util.JsonUtils;
import rx.Observable;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
@Singleton
public class WeiboLocalDataSource implements WeiboDataSource {

    private static final String TAG = WeiboLocalDataSource.class.getSimpleName();
    private static final String CACHE_KEY = String.valueOf(TAG.hashCode());
    private static final int CACHE_INDEX = 0;
    private DiskCache mDiskCache;
    private DaoSession mReadableDao;
    private DaoSession mWritableDao;

    public WeiboLocalDataSource(@ForApplication Context context,
                                @ReadableDb DaoSession readableDao,
                                @WritableDb DaoSession writableDao) {
        mDiskCache = new DiskCache(context, "weibo", 1, 10 * 1024 * 1024);
        mReadableDao = readableDao;
        mWritableDao = writableDao;
    }

    /** 使用SharedPreference管理登录用户数据 */
    @Override
    public Observable<User> getOwner() {
        User user = AppProps.getOwner();
        return user == null ? Observable.empty() : Observable.just(user);
    }

    /** 使用GreenDao管理朋友用户数据 */
    @Override
    public Observable<List<User>> getFriends() {
        List<User> users = mReadableDao.getUserDao().queryBuilder()
                .orderAsc(UserDao.Properties.Pinyin)
                .list();
        Log.i(TAG, "get local friends " + users);
        return users.isEmpty() ? Observable.empty() : Observable.just(users);
    }

    /** 使用文件缓存管理微博数据 */
    @Override
    public Observable<List<Status>> getAllStatuses() {
        String json = mDiskCache.get(CACHE_KEY, CACHE_INDEX);
        List<Status> result = JsonUtils.fromJson(json, new TypeToken<List<Status>>() {}.getType());
        return result == null ? Observable.empty() : Observable.just(result);
    }

    @Override
    public Observable<List<Status>> getStatuses(int pageNum) {
        String json = mDiskCache.get(CACHE_KEY, CACHE_INDEX);
        List<Status> result = JsonUtils.fromJson(json, new TypeToken<List<Status>>() {}.getType());
        if (result == null || result.isEmpty()) {
            return Observable.empty();
        }

        int totalCount = result.size();
        int startIndex = Math.max((pageNum - 1) * ApiConstants.PAGE_SIZE, 0);
        int endIndex = Math.min(pageNum * ApiConstants.PAGE_SIZE, totalCount);

        List<Status> subList = null;
        if (startIndex < totalCount) {
            subList = new ArrayList<>(result.subList(startIndex, endIndex));
        }
        return subList == null ? Observable.empty() : Observable.just(subList);
    }

    public void saveOwner(User user) {
        Log.i(TAG, "save owner " + user);
        AppProps.saveOwner(user);
    }

    public void saveFriends(List<User> users) {
        Log.i(TAG, "save friends " + users);
        if (users == null || users.isEmpty()) {
            mWritableDao.getUserDao().deleteAll();
        } else {
            mWritableDao.getUserDao().insertOrReplaceInTx(users);
        }
    }

    public void saveStatuses(List<Status> statuses) {
        Log.i(TAG, "save statuses " + statuses);
        if (statuses == null || statuses.isEmpty()) {
            mDiskCache.remove(CACHE_KEY);
        } else {
            mDiskCache.put(CACHE_KEY, CACHE_INDEX, JsonUtils.toJson(statuses));
        }
    }

}
