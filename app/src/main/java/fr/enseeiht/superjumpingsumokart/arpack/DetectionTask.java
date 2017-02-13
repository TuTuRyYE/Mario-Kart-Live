package fr.enseeiht.superjumpingsumokart.arpack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v8.renderscript.Type;
import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by romain on 06/02/17.
 */

public class DetectionTask extends AsyncTask<byte[], Void, Boolean> {

    private final static String DETECTION_TASK_TAG = "DetectionTask";
    public static RenderScript rs;

    private ScriptC_rbgaToNv21 script;
    ScriptIntrinsicYuvToRGB script2;

    private final GUIGame GUI_GAME;
    private Bitmap bitmapToProcess;
    private Bitmap bitmapToDisplay;
    byte[] b;

    private Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs)).setX(640*480*3/2);
    private Type.Builder argb888Type = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(640).setY(480);



    public DetectionTask(GUIGame guiGame) {
        GUI_GAME = guiGame;
        script = new ScriptC_rbgaToNv21(rs);
        script2 = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
    }

    @Override
    protected Boolean doInBackground(byte[]... frames) {
        long startTime = SystemClock.currentThreadTimeMillis();
        bitmapToProcess = BitmapFactory.decodeStream(new ByteArrayInputStream(frames[0]));

        //METHOD 1 ~60ms
        //ARToolKit.getInstance().convertAndDetect(getNV21(640, 480, bitmapToProcess));

        //METHOD 2 ~~ 45ms
        Allocation argb8888In = Allocation.createFromBitmap(rs, bitmapToProcess);
        Allocation nv21Out = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

        script.set_nv21ByteArray(nv21Out);
        script.forEach_convertToNV21(argb8888In);
        byte[] yuvBytes = new byte[640*480*3/2];
        nv21Out.copyTo(yuvBytes);
        YuvImage yuvImage = new YuvImage(yuvBytes, ImageFormat.NV21, 640, 480, null);
        ByteArrayOutputStream o = new ByteArrayOutputStream(640*480*3/2);
        yuvImage.compressToJpeg(new Rect(0, 0, 640, 480), 50, o);
        bitmapToDisplay = BitmapFactory.decodeStream(new ByteArrayInputStream(o.toByteArray()));


        //Allocation nv21In = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);
        //Allocation argb8888Out = Allocation.createTyped(rs, argb888Type.create(), Allocation.USAGE_SCRIPT);

        //nv21In.copyFrom(nv21Out);
        //script2.setInput(nv21Out);
        //script2.forEach(argb8888Out);
        //argb8888Out.copyTo(bitmapToProcess);
//        ARToolKit.getInstance().convertAndDetect(nv21Allocation.getByteBuffer().array());
//        if (ARToolKit.getInstance().queryMarkerVisible(0)) {
//            Log.d(DETECTION_TASK_TAG, "Marker detected, distance to the camera = " + ARToolKit.getInstance().queryMarkerTransformation(0)[14]);
//        }
//        Log.d(DETECTION_TASK_TAG, "Detection task time : " + Long.toString((SystemClock.currentThreadTimeMillis() - startTime)));
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        GUI_GAME.updateCameraSurfaceView(bitmapToDisplay, null);
        GUI_GAME.renderAR();
    }

    private byte[] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {
        long startTime = SystemClock.currentThreadTimeMillis();
        int [] argb = new int[inputWidth * inputHeight];

        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);

        byte [] yuv = new byte[inputWidth*inputHeight*3/2];
        encodeYUV420SP(yuv, argb, inputWidth, inputHeight);
        Log.d(DETECTION_TASK_TAG, "GetNV21 : " + Long.toString((SystemClock.currentThreadTimeMillis() - startTime)));

        return yuv;
    }

    private void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;

        int yIndex = 0;
        int uvIndex = frameSize;

        int R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff);

                // well known RGB to YUV algorithm
                Y = ( (  66 * R + 129 * G +  25 * B + 128) >> 8) +  16;
                U = ( ( -38 * R -  74 * G + 112 * B + 128) >> 8) + 128;
                V = ( ( 112 * R -  94 * G -  18 * B + 128) >> 8) + 128;

                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
                //    pixel AND every other scanline.
                yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[uvIndex++] = (byte)((V<0) ? 0 : ((V > 255) ? 255 : V));
                    yuv420sp[uvIndex++] = (byte)((U<0) ? 0 : ((U > 255) ? 255 : U));
                }

                index ++;
            }
        }
    }
}
