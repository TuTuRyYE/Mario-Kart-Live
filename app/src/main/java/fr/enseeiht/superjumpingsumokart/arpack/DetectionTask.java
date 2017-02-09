package fr.enseeiht.superjumpingsumokart.arpack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

/**
 * Created by romain on 06/02/17.
 */

public class DetectionTask extends AsyncTask<byte[], Void, Boolean> {

    private final static String DETECTION_TASK_TAG = "DetectionTask";

    private GUIGame guiGame;
    private Bitmap bitmapToProcess;

    public DetectionTask(GUIGame guiGame) {
        this.guiGame = guiGame;
    }

    @Override
    protected Boolean doInBackground(byte[]... frames) {
        long startTime = SystemClock.currentThreadTimeMillis();
        //guiGameCurrentFrame = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(frames[0])), guiGame.getDisplayWidth(), guiGame.getDisplayHeight(), true);
        bitmapToProcess = BitmapFactory.decodeStream(new ByteArrayInputStream(frames[0]));
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmapToProcess.getRowBytes() * bitmapToProcess.getHeight());
        bitmapToProcess.copyPixelsToBuffer(byteBuffer);
        ARToolKit.getInstance().convertAndDetect(byteBuffer.array());
        Log.d(DETECTION_TASK_TAG, "convertAndDetect + bmp conversion time : " + Long.toString((SystemClock.currentThreadTimeMillis() - startTime)));
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        guiGame.updateCameraSurfaceView(bitmapToProcess);
        guiGame.renderAR();
    }

    private byte[] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {
        long startTime = SystemClock.currentThreadTimeMillis();
        int [] argb = new int[inputWidth * inputHeight];

        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);

        byte [] yuv = new byte[inputWidth*inputHeight*3/2];
        encodeYUV420SP(yuv, argb, inputWidth, inputHeight);

        scaled.recycle();
        Log.d(DETECTION_TASK_TAG, "GetNV21 : " + Long.toString((SystemClock.currentThreadTimeMillis() - startTime)));

        return yuv;
    }

    private void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;

        int yIndex = 0;
        int uvIndex = frameSize;

        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
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
                    yuv420sp[uvIndex++] = (byte)((U<0) ? 0 : ((U > 255) ? 255 : U));
                    yuv420sp[uvIndex++] = (byte)((V<0) ? 0 : ((V > 255) ? 255 : V));
                }

                index ++;
            }
        }
    }
}
