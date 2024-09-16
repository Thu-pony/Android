package csumissu.fakewechat.v2;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class WechatApp extends Application {

    private static final String TAG = WechatApp.class.getSimpleName();
    private AppComponent mAppComponent;
    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        Log.i(TAG, "onCreate()");
        enableStrictMode();
        initLeakCanary();
        initAppComponent();
        AppProps.init(this);
    }

    public AppComponent component() {
        return mAppComponent;
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    private void enableStrictMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    private void initLeakCanary() {
        mRefWatcher = LeakCanary.install(this);
    }

    private void initAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

}
