package com.example.anyiassistor;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketService extends Service {

    private static final String TAG = "WebSocketService";

    private final IBinder binder = new LocalBinder();
    private Context mContext;

    private WebsockteManger websockteManger;

//   private TextToSpeech tts;
//    @Override
//    public void onInit(int status) {
//        Log.d("ANYI", "status " + status);
//        if (status == TextToSpeech.SUCCESS) {
//            int result = tts.setLanguage(Locale.CHINESE); // 设置语言为中文
//            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Toast.makeText(this, "语言不支持", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "语言支持", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void speak(String text) {
//        if(tts != null) {
//            Log.d(TAG,"speak ");
//            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
//        }
//    }
    public class LocalBinder extends Binder {
        WebSocketService getService() {
            // Return this instance of WebSocketService so clients can call public methods
            return WebSocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("ForegroundServiceChannel",
                    "Foreground Service Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        //Android9.0会报Caused by: java.lang.SecurityException: Permission Denial:
        //android.permission.FOREGROUND_SERVICE---AndroidManifest.xml中需要申请此权限
        startForeground(1,getNotification());
       // tts = new TextToSpeech(this,this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 连接 WebSocket
        websockteManger = WebsockteManger.getInstance(this);
        return START_STICKY;
    }

    //这个必须加，不能设置为null
    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("测试服务")
                .setContentText("我正在运行");//标题和内容可以不加
        //设置Notification的ChannelID,否则不能正常显示
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("ForegroundServiceChannel");
        }
        Notification notification = builder.build();
        return notification;
    }
    @Override
    public void onDestroy() {
        Log.d(TAG,"WEB SOCKET DESTORY");
        super.onDestroy();
        websockteManger.closeWebSocket();
        // 断开 WebSocket 连接
  //      if (webSocket != null) {
    //        webSocket.cancel();
      //  }
//        if (tts != null) {
//            tts.stop();
//            tts.shutdown();
//        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}


