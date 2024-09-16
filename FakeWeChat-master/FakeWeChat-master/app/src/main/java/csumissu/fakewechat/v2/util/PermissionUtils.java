package csumissu.fakewechat.v2.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class PermissionUtils {

    public static boolean requirePermissions(@NonNull Activity activity,
                                      @NonNull String[] permissionArray,
                                      int requestCode) {
        List<String> permissions = new ArrayList<>();
        for (String permission : permissionArray) {
            if (!checkPermission(activity, permission)) {
                permissions.add(permission);
            }
        }
        if (permissions.isEmpty()) {
            return false;
        }
        ActivityCompat.requestPermissions(activity,
                permissions.toArray(new String[permissions.size()]),
                requestCode);
        return true;
    }

    public static boolean checkPermission(@NonNull Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

}
