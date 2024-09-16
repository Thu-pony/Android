package csumissu.fakewechat.v2.ui.contact;

import java.util.List;

import csumissu.fakewechat.v2.base.BasePresenter;
import csumissu.fakewechat.v2.base.BaseView;
import csumissu.fakewechat.v2.bean.User;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
interface ContactContract {

    interface View extends BaseView<Presenter> {
        void showLoading(boolean show);

        void showError(String message);

        void showFriends(List<User> users);
    }

    interface Presenter extends BasePresenter {

    }
}
