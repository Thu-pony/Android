package csumissu.fakewechat.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author sunyaxi
 * @date 2016/5/20
 */
@SuppressWarnings("ALL")
public class FileManager {

    private static final String TAG = FileManager.class.getSimpleName();
    private static FileManager INSTANCE;

    private FileManager() {
    }

    public synchronized static FileManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileManager();
        }
        return INSTANCE;
    }

    /**
     * Writes a file to Disk.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform this operation using another thread.
     *
     * @param file The file to write to Disk.
     */
    public void writeToFile(File file, String fileContent) {
        if (file.exists()) {
            file.delete();
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(fileContent);
        } catch (IOException e) {
            Log.w(TAG, e);
        } finally {
            closeQuietly(writer);
        }

    }

    /**
     * Reads a content from a file.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform the operation using another thread.
     *
     * @param file The file to read from.
     * @return A string with the content of the file.
     */
    public String readFileContent(File file) {
        StringBuilder fileContentBuilder = new StringBuilder();
        if (file.exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String stringLine;
                while ((stringLine = bufferedReader.readLine()) != null) {
                    fileContentBuilder.append(stringLine).append("\n");
                }
            } catch (IOException e) {
                Log.w(TAG, e);
            } finally {
                closeQuietly(bufferedReader);
            }
        }

        return fileContentBuilder.toString();
    }

    /**
     * Returns a boolean indicating whether this file can be found on the underlying file system.
     *
     * @param file The file to check existence.
     * @return true if this file exists, false otherwise.
     */
    public boolean exists(File file) {
        return file.exists();
    }

    /**
     * Warning: Deletes the content of a directory.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform the operation using another thread.
     *
     * @param directory The directory which its content will be deleted.
     */
    public void clearDirectory(File directory) {
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    clearDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
    }

    /**
     * Write a value to a user preferences file.
     *
     * @param context {@link android.content.Context} to retrieve android user preferences.
     * @param key     A string for the key that will be used to retrieve the value in the future.
     * @param value   A String representing the value to be inserted.
     */
    public void writeToPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Get a value from a user preferences file.
     *
     * @param context {@link android.content.Context} to retrieve android user preferences.
     * @param key     A key that will be used to retrieve the value from the preference file.
     * @return A String representing the value retrieved from the preferences file.
     */
    public String getFromPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    private void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
            }
        }
    }
}
