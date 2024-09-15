package com.example.decodeh264;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MediaProjectionService extends Service {
    private  static final  String TAG = "pony";
    private static final int NOTIFICATION_ID = 1;
    private MediaProjection mediaProjection;

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, createNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int resultCode = intent.getIntExtra("resultCode", -1);
        Intent data = intent.getParcelableExtra("data");
        Log.d(TAG,"开始前台服务 0");
        Log.d(TAG,"resultcode = " + resultCode);
        Log.d(TAG,data.toString());
        if (resultCode == -1 && data != null) {
            Log.d(TAG,"开始前台服务 1");
            MediaProjectionManager mediaProjectionManager =
                    (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);

            startScreenCapture(mediaProjection);
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification() {
        String notificationChannelId = "MEDIA_PROJECTION_CHANNEL";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    notificationChannelId,
                    "Screen Capture",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        return new NotificationCompat.Builder(this, notificationChannelId)
                .setContentTitle("Screen Capture")
                .setContentText("Recording the screen...")
                .setSmallIcon(R.drawable.ic_launcher_background)  // 你可以根据项目设置自己的图标
                .build();
    }

    private void startScreenCapture(MediaProjection mediaProjection) {
        // 在这里实现 MediaProjection 的屏幕录制逻辑
        // 比如设置 VirtualDisplay 和编码器等
        H264Encoder h264Encoder = new H264Encoder(mediaProjection, this);
        Log.d(TAG,"编码器创建完成");
        h264Encoder.start();
    }
}

