package csumissu.fakewechat.main;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import csumissu.fakewechat.data.source.EntityRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static csumissu.fakewechat.util.Preconditions.checkNotNull;

/**
 * @author sunyaxi
 * @date 2016/6/30
 */
public class ContactsPresenter implements ContactsContract.Presenter {

    private ContactsContract.View mView;
    private final EntityRepository mRepository;

    public ContactsPresenter(@NonNull ContactsContract.View view,
                             @NonNull EntityRepository repository) {
        mView = checkNotNull(view);
        mRepository = checkNotNull(repository);
    }

    @Override
    public void loadAllFriends() {
        mRepository.getFriends()
                .compose(((RxFragment) mView).bindUntilEvent(FragmentEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> mView.showLoading(true))
                .doOnTerminate(() -> mView.showLoading(false))
                .subscribe(result -> {
                    mView.showFriends(result);
                });
    }

    @Override
    public void start() {
        loadAllFriends();
    }
}
