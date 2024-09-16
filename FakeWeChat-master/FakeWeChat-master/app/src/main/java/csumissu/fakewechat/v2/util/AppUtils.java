package csumissu.fakewechat.v2.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class AppUtils {

    private static final String TAG = AppUtils.class.getSimpleName();

    public static int getAppVersion(Context context) {
        PackageInfo info = getPackageInfo(context, context.getPackageName());
        return info != null ? info.versionCode : 1;
    }

    public static String getVersionName(Context context) {
        PackageInfo info = getPackageInfo(context, context.getPackageName());
        return info != null ? info.versionName : null;
    }

    public static PackageInfo getPackageInfo(Context context, String pkgName) {
        try {
            return context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Unknown package name " + pkgName, e);
        }
        return null;
    }
}
