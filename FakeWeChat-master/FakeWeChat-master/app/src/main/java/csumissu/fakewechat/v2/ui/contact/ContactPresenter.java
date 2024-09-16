package csumissu.fakewechat.v2.ui.contact;

import android.util.Log;

import javax.inject.Inject;

import csumissu.fakewechat.v2.model.WeiboRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
class ContactPresenter implements ContactContract.Presenter {

    private static final String TAG = ContactPresenter.class.getSimpleName();
    private WeiboRepository mWeiboRepository;
    private ContactContract.View mView;

    @Inject
    ContactPresenter(WeiboRepository weiboRepository, ContactContract.View view) {
        mWeiboRepository = weiboRepository;
        mView = view;
    }

    @Inject
    public void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mWeiboRepository.getFriends()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> mView.showLoading(true))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> mView.showLoading(false))
                .subscribe(users -> {
                    mView.showFriends(users);
                }, throwable -> {
                    Log.w(TAG, "get friends failed", throwable);
                    mView.showError(throwable.getMessage());
                });
    }
}
