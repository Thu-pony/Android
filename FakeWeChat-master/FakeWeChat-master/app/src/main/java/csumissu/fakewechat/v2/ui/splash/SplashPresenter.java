package csumissu.fakewechat.v2.ui.splash;

import android.util.Log;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import csumissu.fakewechat.v2.model.WeiboRepository;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
class SplashPresenter implements SplashContract.Presenter {

    private static final String TAG = SplashPresenter.class.getSimpleName();
    private final WeiboRepository mWeiboRepository;
    private final SplashContract.View mSplashView;

    @Inject
    SplashPresenter(WeiboRepository weiboRepository, SplashContract.View view) {
        Log.i(TAG, "splash presenter constructor");
        mWeiboRepository = weiboRepository;
        mSplashView = view;
    }

    @Inject
    void setupListeners() {
        Log.i(TAG, "splash presenter setup listeners");
        mSplashView.setPresenter(this);
    }

    @Override
    public void start() {
        mWeiboRepository.getOwner()
                .compose(((RxAppCompatActivity) mSplashView).bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> mSplashView.showLoading(true))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> mSplashView.showLoading(false))
                .subscribe(user -> {
                    mSplashView.enterMainActivity();
                }, throwable -> {
                    Log.w(TAG, "get owner failed", throwable);
                    mSplashView.showError(throwable.getMessage());
                });
    }
}
