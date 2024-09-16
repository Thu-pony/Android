package csumissu.fakewechat.v2.ui.contact;

import csumissu.fakewechat.v2.AppComponent;
import csumissu.fakewechat.v2.dagger.FragmentScope;
import dagger.Component;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = ContactModule.class)
interface ContactComponent {

    void inject(ContactFragment fragment);

}
