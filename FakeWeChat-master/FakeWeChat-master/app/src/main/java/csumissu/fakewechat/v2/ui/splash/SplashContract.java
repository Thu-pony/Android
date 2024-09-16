package csumissu.fakewechat.v2.ui.splash;

import csumissu.fakewechat.v2.base.BasePresenter;
import csumissu.fakewechat.v2.base.BaseView;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
interface SplashContract {

    interface View extends BaseView<Presenter> {
        void enterMainActivity();

        void showLoading(boolean show);

        void showError(String message);
    }

    interface Presenter extends BasePresenter {

    }
}
