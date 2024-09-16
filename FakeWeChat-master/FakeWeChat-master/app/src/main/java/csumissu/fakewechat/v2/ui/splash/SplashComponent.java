package csumissu.fakewechat.v2.ui.splash;

import csumissu.fakewechat.v2.AppComponent;
import csumissu.fakewechat.v2.dagger.ActivityScope;
import dagger.Component;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SplashModule.class)
interface SplashComponent {

    void inject(SplashActivity activity);

}