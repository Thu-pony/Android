package csumissu.fakewechat.v2.ui.splash;

import csumissu.fakewechat.v2.dagger.ActivityScope;
import dagger.Module;
import dagger.Provides;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
@Module
class SplashModule {

    private final SplashContract.View mView;

    SplashModule(SplashContract.View view) {
        mView = view;
    }

    @Provides
    @ActivityScope
    SplashContract.View provideView() {
        return mView;
    }

}
