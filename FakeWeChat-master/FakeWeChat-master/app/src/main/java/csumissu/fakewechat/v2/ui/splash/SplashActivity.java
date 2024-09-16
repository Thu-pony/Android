package csumissu.fakewechat.v2.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import csumissu.fakewechat.R;
import csumissu.fakewechat.v2.base.BaseActivity;
import csumissu.fakewechat.v2.ui.main.MainActivity;
import csumissu.fakewechat.v2.util.PermissionUtils;
import csumissu.fakewechat.widget.ProgressWheel;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class SplashActivity extends BaseActivity implements SplashContract.View {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int MSG_INIT = 0;
    private static final int MSG_LOOP = 1;
    private static final int MSG_MAIN = 2;
    private static final int MSG_WAIT = 3;
    private final MyHandler mHandler = new MyHandler(this);
    private boolean mLoadFinished = false;
    @BindView(R.id.pw_spinner)
    ProgressWheel mProgressWheel;
    @Inject
    SplashPresenter mPresenter;
    private static final int REQUEST_PERMISSIONS = 133;
    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initInject();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!requirePermissions()) {
            mPresenter.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeMessages(MSG_LOOP);
        mHandler.removeMessages(MSG_WAIT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (permissions.length == grantResults.length) {
                mPresenter.start();
            } else {
                requirePermissions();
            }
        }
    }

    @Override
    public void enterMainActivity() {
        mLoadFinished = true;
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            mHandler.sendEmptyMessage(MSG_INIT);
            mHandler.sendEmptyMessage(MSG_LOOP);
        }
    }

    @Override
    public void showError(String message) {
        showMessage(message);
        finish();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        Log.i(TAG, "set presenter " + presenter);
    }

    @OnClick(R.id.pw_spinner)
    public void skipLoading() {
        if (mLoadFinished) {
            mHandler.sendEmptyMessage(MSG_MAIN);
        }
    }

    private void initInject() {
        Log.i(TAG, "init inject");
        DaggerSplashComponent.builder()
                .splashModule(new SplashModule(this))
                .appComponent(getApplicationComponent())
                .build()
                .inject(this);
        Log.i(TAG, "mPresenter " + mPresenter);
    }

    private boolean requirePermissions() {
        return PermissionUtils.requirePermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<SplashActivity> iActivity;
        private int iProgress = 0;
        private int iWaitTimes = 0;

        MyHandler(SplashActivity activity) {
            iActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = iActivity.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case MSG_INIT:
                    iWaitTimes = 0;
                    iProgress = 0;
                    activity.mLoadFinished = false;
                    break;
                case MSG_LOOP:
                    if (iProgress > 360) {
                        sendEmptyMessage(MSG_WAIT);
                        break;
                    }
                    iProgress += 3;
                    activity.mProgressWheel.setProgress(iProgress);
                    sendEmptyMessageDelayed(MSG_LOOP, 10);
                    break;
                case MSG_MAIN:
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                    break;
                case MSG_WAIT:
                    if (activity.mLoadFinished) {
                        sendEmptyMessage(MSG_MAIN);
                        break;
                    }
                    if (iWaitTimes < 5) {
                        iWaitTimes++;
                        sendEmptyMessageDelayed(MSG_WAIT, 200);
                    } else {
                        activity.showMessage(R.string.load_owner_failed);
                        activity.finish();
                    }
                    break;
            }
        }
    }
}
