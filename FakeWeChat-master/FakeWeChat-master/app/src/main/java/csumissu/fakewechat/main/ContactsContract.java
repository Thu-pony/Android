package csumissu.fakewechat.main;

import csumissu.fakewechat.base.BasePresenter;
import csumissu.fakewechat.base.BaseView;
import csumissu.fakewechat.data.FriendshipResult;

/**
 * @author sunyaxi
 * @date 2016/6/30
 */
public interface ContactsContract {

    interface Presenter extends BasePresenter {

        void loadAllFriends();

    }

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showFriends(FriendshipResult result);
    }

}
