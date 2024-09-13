package com.example.mediacodech264;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class H264Player implements Runnable {
    private final String TAG = "Mediacodec";
    // 数据源
    private String path;
    private Context context;

    public String getPath() {
        return path;
    }

    public H264Player(Context context, Surface surface) {
        this.context = context;
        this.surface = surface;
        try {
            this.mediaCodec = MediaCodec.createDecoderByType("video/avc");
            //传输参数 视频传输
            //自己的参数
            MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc",364,364);
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE,25);
            //mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(sps)); // SPS
            //mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(pps));
            this.mediaCodec.configure(mediaFormat,surface,null,0);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"不支持");
        }
        Log.d(TAG,"支持");
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Surface getSurface() {
        return surface;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public MediaCodec getMediaCodec() {
        return mediaCodec;
    }

    public void setMediaCodec(MediaCodec mediaCodec) {
        this.mediaCodec = mediaCodec;
    }

    //surface
    private Surface surface;
    //解码器
    MediaCodec mediaCodec;
    public void play() {
        mediaCodec.start();
        new Thread(this).start();
    }

    @Override
    public void run() {
        //解码
        try {
             decodeH264();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decodeH264() {
        byte[] bytes = null;
        try {
            //数据
            bytes = getBytes();
        }catch (Exception e) {
            e.printStackTrace();
        }
        int stratindex = 0;
        MediaCodec.BufferInfo Info = new MediaCodec.BufferInfo();

        while (true) {
            int nextframe = findframe(bytes, stratindex + 2, bytes.length);
            int index = mediaCodec.dequeueInputBuffer(10000);
            if (index > 0) {
                ByteBuffer byteBuffer = mediaCodec.getInputBuffer(index);
                //按照帧 来丢 一帧来丢
                int length = nextframe - stratindex;
                //Log.d(TAG, String.valueOf(bytes[0]));
                Log.d(TAG,"start = " + stratindex);
                Log.d(TAG,"length = " + length);
                byteBuffer.put(bytes, stratindex, length);
                Log.d(TAG,"输入 " + length);
                mediaCodec.queueInputBuffer(index,0,length,0,0);
                stratindex = nextframe;
            }
            //解码完成了么
           int  outIndex = mediaCodec.dequeueOutputBuffer(Info,10000);
            if (outIndex >= 0) {
                mediaCodec.releaseOutputBuffer(outIndex, true);
            }
        }
    }
    private int findframe(byte[] bytes, int start, int totalsize) {
        for (int i = start; i <= totalsize - 4; i++) {
            if (((bytes[i] == 0x00) && (bytes[i + 1] == 0x00) && (bytes[i + 2] == 0x00) && (bytes[i + 3] == 0x01) )|| ((bytes[i] == 0x00) && (bytes[i + 1] == 0x00) && (bytes[i + 2] == 0x01))) {
                return i;
            }
        }
        return totalsize;
    }
    private byte[] getBytes() {
        InputStream is = null;
        Resources res = context.getResources();
        is = res.openRawResource(R.raw.out);
        is = new DataInputStream(is);

//        try {
//
//            is = new DataInputStream(new FileInputStream(new File(path)));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        int len;
        int size = 1024;
        byte[] buf;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[1024];
        while (true) {
            try {
                if (((len = is.read(buf,0,size)) == -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bos.write(buf,0,len);
        }
        buf = bos.toByteArray();
        Log.d(TAG, String.valueOf(buf.length));
        return buf;
    }
}
