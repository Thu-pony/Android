package com.example.mediacodech264;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;

import javax.xml.parsers.SAXParser;


public class MainActivity extends AppCompatActivity {
    private H264Player h264Player;
    private Context mContext;
    private boolean checkPermission() {
        // 检查Android版本是否为6.0及以上，因为只有这些版本需要动态请求权限

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否已经授予了WRITE_EXTERNAL_STORAGE权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予权限，申请权限

                requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                return false; // 权限还没有被授予
            }
        }
        return true; // 权限已经被授予
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("111", String.valueOf(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)));
        Log.d("111", String.valueOf(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)));
        Log.d("111", String.valueOf(checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ininSurface();
    }

    private void ininSurface() {
        SurfaceView surface = (SurfaceView) findViewById(R.id.surfaceview);
        surface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
              Surface surface =   holder.getSurface();
              h264Player = new H264Player(mContext,surface);
              h264Player.play();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });

    }

}