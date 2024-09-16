package csumissu.fakewechat.v2.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * Writes a file to Disk.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform this operation using another thread.
     *
     * @param file The file to write to Disk.
     */
    public static void writeToFile(File file, String fileContent) {
        if (exists(file)) {
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
    public static String readFileContent(File file) {
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
    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    /**
     * Warning: Deletes the content of a directory.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform the operation using another thread.
     *
     * @param directory The directory which its content will be deleted.
     */
    public static void clearDirectory(File directory) {
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

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file == null) {
                return size;
            }
            File[] files = file.listFiles();
            if (files == null) {
                return size;
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    size += getFileSize(f);
                } else {
                    size += f.length();
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "get folder size failed " + file.getPath(), e);
        }
        return size;
    }
}
