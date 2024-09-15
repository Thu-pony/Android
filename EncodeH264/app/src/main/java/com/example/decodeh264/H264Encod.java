package com.example.decodeh264;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.util.Log;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.time.LocalDate;

public class H264Encod {
    private int width;
    private int height;
    private MediaCodec mediaCodec;
    private int index = 1;
    private final static String TAG = "pony";
    private Context mContext;
    public H264Encod(int width, int height, Context context) {
        this.width = width;
        this.height = height;
        this.mContext = context;
        try {
            mediaCodec = MediaCodec.createEncoderByType("video/avc");
            MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);
           format.setInteger(MediaFormat.KEY_BIT_RATE, width * height);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
            // 后面是编码, 1)
            format.setInteger("prepend-sps-pps-to-idr-frames", 1);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void encodeOneFrame(byte[] bytes) {
        // 输入和输出
        int inIndex = mediaCodec.dequeueInputBuffer(10000);
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        if (inIndex > 0) {
            ByteBuffer byteBuffer = mediaCodec.getInputBuffer(inIndex);
            byteBuffer.clear();
            byteBuffer.put(bytes);
            mediaCodec.queueInputBuffer(inIndex,0,bytes.length,computePts(index),0);
            index++;
        }
        int outIndex = mediaCodec.dequeueOutputBuffer(info,10000);
        if (outIndex > 0) {
            ByteBuffer byteBuffer = mediaCodec.getOutputBuffer(outIndex);
            byte[] ba = new byte[info.size];
            byteBuffer.get(ba);
            FileUtils.writeBytes(ba,mContext);
            FileUtils.writeContent(ba,mContext);
            Log.d(TAG,"编码成功");

            mediaCodec.releaseOutputBuffer(outIndex,false);

        }
    }
    private int computePts(int index) {
        return 1000000 / 15 * index;
    }
}
