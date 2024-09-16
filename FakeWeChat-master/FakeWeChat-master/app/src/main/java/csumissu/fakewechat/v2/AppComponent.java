package csumissu.fakewechat.v2;

import android.content.Context;

import javax.inject.Singleton;

import csumissu.fakewechat.v2.dagger.ForApplication;
import csumissu.fakewechat.v2.dagger.ReadableDb;
import csumissu.fakewechat.v2.dagger.WritableDb;
import csumissu.fakewechat.v2.dao.DaoSession;
import csumissu.fakewechat.v2.model.WeiboRepository;
import dagger.Component;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(WechatApp application);

    @ForApplication
    Context getApplicationContext();

    WeiboRepository getWeiboRepository();

}
