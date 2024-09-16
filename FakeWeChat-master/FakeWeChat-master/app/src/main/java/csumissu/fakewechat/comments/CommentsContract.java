package csumissu.fakewechat.comments;

import java.util.List;

import csumissu.fakewechat.base.BasePresenter;
import csumissu.fakewechat.base.BaseView;
import csumissu.fakewechat.data.Status;

/**
 * @author sunyaxi
 * @date 2016/5/23
 */
public interface CommentsContract {

    interface Presenter extends BasePresenter {

        void loadAllStatuses();

        void loadStatuses(int page);

    }

    interface View extends BaseView<Presenter> {

        void setRefreshIndicator(boolean active);

        void setLoadingIndicator(boolean active);

        void showStatuses(List<Status> statuses);

        void showError(String message);

    }
}
