package csumissu.fakewechat.v2.ui.contact;

import csumissu.fakewechat.v2.dagger.FragmentScope;
import dagger.Module;
import dagger.Provides;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
@Module
class ContactModule {

    private ContactContract.View mView;

    ContactModule(ContactContract.View view) {
        mView = view;
    }

    @Provides
    @FragmentScope
    ContactContract.View provideView() {
        return mView;
    }

}
