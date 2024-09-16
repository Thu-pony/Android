package csumissu.fakewechat.v2;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import javax.inject.Singleton;

import csumissu.fakewechat.v2.api.ApiClient;
import csumissu.fakewechat.v2.api.ApiService;
import csumissu.fakewechat.v2.dagger.ForApplication;
import csumissu.fakewechat.v2.dagger.Local;
import csumissu.fakewechat.v2.dagger.ReadableDb;
import csumissu.fakewechat.v2.dagger.Remote;
import csumissu.fakewechat.v2.dagger.WritableDb;
import csumissu.fakewechat.v2.dao.DaoMaster;
import csumissu.fakewechat.v2.dao.DaoSession;
import csumissu.fakewechat.v2.model.WeiboDataSource;
import csumissu.fakewechat.v2.model.local.WeiboLocalDataSource;
import csumissu.fakewechat.v2.model.remote.WeiboRemoteDataSource;
import dagger.Module;
import dagger.Provides;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
@Module
public class AppModule {

    private final WechatApp mApplication;
    private DaoMaster.DevOpenHelper mOpenHelper;

    public AppModule(WechatApp application) {
        mApplication = application;
        mOpenHelper = new DaoMaster.DevOpenHelper(mApplication, "wechat-db");
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    @WritableDb
    DaoSession provideWritableDaoSession() {
        Database db = mOpenHelper.getWritableDb();
        return new DaoMaster(db).newSession();
    }

    @Provides
    @Singleton
    @ReadableDb
    DaoSession provideReadableDaoSession() {
        Database db = mOpenHelper.getReadableDb();
        return new DaoMaster(db).newSession();
    }

    @Provides
    @Singleton
    @Local
    WeiboDataSource provideUserLocalDataSource(@ForApplication Context context,
                                               @ReadableDb DaoSession readableDao,
                                               @WritableDb DaoSession writableDao) {
        return new WeiboLocalDataSource(context, readableDao, writableDao);
    }

    @Provides
    @Singleton
    ApiService provideApiService() {
        return ApiClient.create(ApiService.class);
    }

    @Provides
    @Singleton
    @Remote
    WeiboDataSource provideUserRemoteDataSource(ApiService apiService) {
        return new WeiboRemoteDataSource(apiService);
    }

}
