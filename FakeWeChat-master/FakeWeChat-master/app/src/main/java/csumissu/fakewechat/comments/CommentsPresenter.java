package csumissu.fakewechat.comments;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import csumissu.fakewechat.data.source.EntityRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static csumissu.fakewechat.util.Preconditions.checkNotNull;

/**
 * @author sunyaxi
 * @date 2016/5/23
 */
public class CommentsPresenter implements CommentsContract.Presenter {

    private final CommentsContract.View mView;
    private final EntityRepository mRepository;

    public CommentsPresenter(@NonNull CommentsContract.View view,
                             @NonNull EntityRepository repository) {
        mView = checkNotNull(view);
        mRepository = checkNotNull(repository);
    }

    @Override
    public void start() {
        loadStatuses(0);
    }

    @Override
    public void loadAllStatuses() {
        mRepository.getStatusResult()
                .compose(((RxFragment) mView).bindUntilEvent(FragmentEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> mView.setRefreshIndicator(true))
                .doOnTerminate(() -> mView.setRefreshIndicator(false))
                .subscribe(statusResult -> {
                    loadStatuses(0, false);
                }, throwable -> {
                    mView.showError("loadAllStatuses failed! " + throwable.getMessage());
                });
    }

    @Override
    public void loadStatuses(int page) {
        loadStatuses(page, true);
    }

    private void loadStatuses(int page, final boolean showLoadingUI) {
        mRepository.getStatuses(20, page)
                .compose(((RxFragment) mView).bindUntilEvent(FragmentEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    if (showLoadingUI) {
                        mView.setLoadingIndicator(true);
                    }
                })
                .doOnTerminate(() -> {
                    if (showLoadingUI) {
                        mView.setLoadingIndicator(false);
                    }
                })
                .subscribe(mView::showStatuses, throwable -> {
                    mView.showError("loadStatuses failed! " + throwable.getMessage());
                });
    }

}
