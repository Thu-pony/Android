package csumissu.fakewechat.v2.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.support.annotation.NonNull;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class ProcessUtils {

    /**
     * 获取指定pid的进程名
     */
    public static String getProcessName(@NonNull Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : am.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }

    /**
     * 获取当前pid所在的进程名
     */
    public static String getProcessName(@NonNull Context context) {
        return getProcessName(context, Process.myPid());
    }

}
