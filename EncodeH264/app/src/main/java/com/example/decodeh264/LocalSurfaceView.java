package com.example.decodeh264;



import android.content.Context;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import androidx.annotation.NonNull;

import java.io.IOException;

public class LocalSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private Camera mCamera;
    private Camera.Size size;
    private H264Encod h264Encod;
    private byte[] buffer;
    private Context mContext;
    public LocalSurfaceView(Context context) {
        super(context);
        this.mContext = context;
    }
    public LocalSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        this.mContext = context;
    }


    private void startReview() {
        mCamera =  Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = mCamera.getParameters();
        size = parameters.getPreviewSize();
        try {
            mCamera.setPreviewDisplay(getHolder());
            mCamera.setDisplayOrientation(90);
            buffer = new byte[size.height * size.width * 3 / 2];
            mCamera.addCallbackBuffer(buffer);
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.startPreview();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // 竖着的
        // 宽高交换
        if (h264Encod == null) {
            this.h264Encod = new H264Encod(size.width, size.height, mContext);
        }
        h264Encod.encodeOneFrame(data);
        mCamera.addCallbackBuffer(data);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        startReview();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
