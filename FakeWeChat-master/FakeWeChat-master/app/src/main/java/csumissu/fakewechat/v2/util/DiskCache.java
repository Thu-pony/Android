package csumissu.fakewechat.v2.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class DiskCache {

    private static final String TAG = DiskCache.class.getSimpleName();
    private DiskLruCache mDiskLruCache;
    private int mValueCount = 1;

    public DiskCache(Context context, String directory, int valueCount, long maxSize) {
        File cacheDir = getDiskCacheDir(context, directory);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        Log.v(TAG, "cache directory is " + cacheDir.getAbsolutePath());
        mValueCount = valueCount;
        try {
            mDiskLruCache = DiskLruCache.open(cacheDir, AppUtils.getAppVersion(context),
                    valueCount, maxSize);
        } catch (IOException e) {
            Log.wtf(TAG, e);
        }
    }

    public String get(String key, int index) {
        String result = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(EncryptUtils.getMD5(key));
            if (snapshot != null) {
                result = snapshot.getString(index);
                snapshot.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "get key=" + key + " at index=" + index + " failed", e);
        }
        return result;
    }

    public void put(String key, int index, String value) {
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(EncryptUtils.getMD5(key));
            if (editor != null && index < mValueCount) {
                editor.set(index, value);
                for (int i = index + 1; i < mValueCount; i++) {
                    editor.set(i, "");
                }
                editor.commit();
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            Log.e(TAG, "put key=" + key + " at index=" + index + " failed", e);
        }
    }

    public void remove(String key) {
        try {
            mDiskLruCache.remove(EncryptUtils.getMD5(key));
        } catch (IOException e) {
            Log.e(TAG, "remove key=" + key + " failed", e);
        }
    }

    private static File getDiskCacheDir(Context context, String uniqueName) {
        File cacheDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && !Environment.isExternalStorageRemovable()) {
            cacheDir = context.getExternalCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }
        if (TextUtils.isEmpty(uniqueName)) {
            return cacheDir;
        } else {
            return new File(cacheDir, uniqueName);
        }
    }

    public static String getCacheSize(Context context) {
        long cacheSize = FileUtils.getFileSize(getDiskCacheDir(context, null));
        return Formatter.formatShortFileSize(context, cacheSize);
    }

    public static void clearCache(Context context) {
        FileUtils.clearDirectory(getDiskCacheDir(context, null));
    }
}
