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

public class H264Encoder extends  Thread{
    // 数据源
    private  static final  String TAG = "pony";
    private MediaProjection mediaProjection;
    private int width;
    private int height;
    // 编码器
    private MediaCodec mediaCodec;
    private Context mContext;

    public H264Encoder(MediaProjection mediaProjection, Context context) {
        this.mContext = context;
        this.mediaProjection = mediaProjection;
        this.width = 720;
        this.height = 1280;
        try {
            mediaCodec = MediaCodec.createEncoderByType("video/avc");
            MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 20);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 30);
            format.setInteger(MediaFormat.KEY_BIT_RATE, width * height);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            // 后面是编码
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            // 提供虚拟场地
            Surface surface = mediaCodec.createInputSurface();
            this.mediaProjection.createVirtualDisplay("pony-pony", width, height, 2, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, surface, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        Log.d(TAG,"开始编码 -01");
        mediaCodec.start();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        // 输入不要管， 输入已经被实现了
        while (true) {
            int outindex = mediaCodec.dequeueOutputBuffer(info,10000);
            if (outindex >= 0) {
                ByteBuffer byteBuffer = mediaCodec.getOutputBuffer(outindex);
                byte[] ba = new byte[info.size];
                byteBuffer.get(ba); //将bytebuffer -> ba
                Log.d(TAG,"开始编码");
                mediaCodec.releaseOutputBuffer(outindex,false);
                FileUtils.writeBytes(ba,mContext);
                FileUtils.writeContent(ba,mContext);

            }
    }
}
}
