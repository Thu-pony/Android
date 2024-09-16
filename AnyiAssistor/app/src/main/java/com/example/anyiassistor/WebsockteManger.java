package com.example.anyiassistor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class WebsockteManger {
    private static final String TAG = "WebSocketManager";
    private static WebsockteManger instance;
    private WebSocket webSocket;
    private WebSocketListener mListener;
    private final Handler handler = new Handler(Looper.getMainLooper());
    public static final String ACTION_WEBSOCKET_MESSAGE = "com.example.websocket.ACTION_WEBSOCKET_MESSAGE";
    public static final String ACTION_WEBSOCKET_MESSAGE_Device = "com.example.websocket.ACTION_WEBSOCKET_MESSAGE_DEVICE";
    private static final long HEARTBEAT_INTERVAL = 30000; // 30秒
    private MediaPlayer mediaPlayer;
    private SharedPreferences mSharedPreferences;

    private String device_code = "";
    private boolean mConnected = false;
    private Context context;
    private String openid;
    private  WebsockteManger(Context context) {
        this.context = context.getApplicationContext();
        mSharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        openid = mSharedPreferences.getString("openid",null);
        initWebSocket();
    }
    public static WebsockteManger getInstance(Context context) {
        if (instance == null) {
            synchronized (WebsockteManger.class) {
                if (instance == null) {
                    instance = new WebsockteManger(context);
                }
            }
        }
        return instance;
    }
    @SuppressLint("InvalidWakeLockTag")
    private void initWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(API_Adaress.Websocket)
                .build();
        mListener= new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                super.onOpen(webSocket, response);
                Log.d(TAG, "WebSocket connected");
                mConnected = true;
                startHeartbeat();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                Log.d(TAG, "Received message: " + text);
                parseMessage(text);
                // 在这里处理接收到的消息
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosing(webSocket, code, reason);
                Log.d(TAG,"Websocket connection closing ");
                mConnected = false;
                stopHeartbeat();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                super.onFailure(webSocket, t, response);
                mConnected = false;
                //stopHeartbeat();
                Log.e(TAG, "WebSocket connection failed: " + t.getMessage());
                try {
                    Thread.sleep(3000); // 休眠3秒后重连
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 重新连接
                initWebSocket();

            }
            // 在这里处理连接失败的情况
        };
        webSocket = client.newWebSocket(request, mListener);

    }
    private void startHeartbeat() {
        handler.postDelayed(heartbeatRunnable, HEARTBEAT_INTERVAL);
    }
    private final Runnable heartbeatRunnable = new Runnable() {
        @Override
        public void run() {
            device_code = mSharedPreferences.getString("device_code","");
            JSONObject json = new JSONObject();
            try {
                json.put("cmd", "Z");
                json.put("token", openid);
                json.put("codes", device_code);
                if (webSocket != null) {
                    webSocket.send(json.toString());
                    //playSystemNotificationSound();
                    Log.d(TAG, "心跳数据已经发送" + json.toString());
                    handler.postDelayed(this, HEARTBEAT_INTERVAL);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };
    private void playSystemNotificationSound() {
      /*  Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        if (notification != null) {
            ringtone = RingtoneManager.getRingtone(mContext, notification);
            ringtone.play();
        } */
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context,R.raw.notice);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopAudio();
                }
            });
        }
        mediaPlayer.start();
    }
    private  void stopAudio(){
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void stopHeartbeat() {
        handler.removeCallbacks(heartbeatRunnable);
    }
    private void parseMessage(String text) {
        int Device = 0;
        int order = 0;
        try {
            JSONObject jsonObject = new JSONObject(text);
            String order_id = jsonObject.optString("order_id","NOT_FOUND");
            if (!order_id.equals("NOT_FOUND"))order = 1;
            String codes = jsonObject.optString("codes", "NOT_FOUND");
            if (!codes.equals("NOT_FOUND"))Device = 1;
            if (Device == 0 && order == 0)return;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        if (order == 1) {
            OrderBean tempOrder = gson.fromJson(text,OrderBean.class);
            Log.d(TAG, "发送广播");
            Intent intent = new Intent(WebsockteManger.ACTION_WEBSOCKET_MESSAGE);
            intent.putExtra("new_order", tempOrder);
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
            playSystemNotificationSound();
        }
        else if(Device == 1){
            DeviceBean tempDevice = gson.fromJson(text,DeviceBean.class);
            Log.d(TAG,"发送广播 + 设备信息 " + text);
            Intent intent = new Intent(WebsockteManger.ACTION_WEBSOCKET_MESSAGE_Device);
            intent.putExtra("device", tempDevice);
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
        }

    }
    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        } else {
            Log.e(TAG, "WebSocket is not connected");
        }
    }

    public void closeWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Client closed connection");
        }
    }
}
