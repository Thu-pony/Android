package csumissu.fakewechat;

import android.app.Activity;
import android.os.Process;

import java.util.Stack;

/**
 * @author sunyaxi
 * @date 2016/6/27
 */
public class AppManager {

    private static AppManager sInstance;
    private Stack<Activity> mActivityStack = new Stack<>();

    private AppManager() {
    }

    public static synchronized AppManager getInstance() {
        if (sInstance == null) {
            sInstance = new AppManager();
        }
        return sInstance;
    }

    public void addActivity(Activity Activity) {
        mActivityStack.add(Activity);
    }

    public Activity currentActivity() {
        return mActivityStack.lastElement();
    }

    public void finishActivity() {
        finishActivity(currentActivity());
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public void finishActivity(Class cls) {
        for (Activity activity : mActivityStack) {
            if (activity != null && activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    public void finishAllActivity() {
        for (Activity activity : mActivityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        mActivityStack.clear();
    }

    public Activity getActivity(Class cls) {
        for (Activity activity : mActivityStack) {
            if (activity != null && activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    public void appExit() {
        finishAllActivity();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

}
