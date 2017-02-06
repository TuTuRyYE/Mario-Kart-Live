package fr.enseeiht.superjumpingsumokart.arpack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.NativeInterface;
import org.artoolkit.ar.base.rendering.ARRenderer;

/**
 * Created by JorgeEnrique on 2/02/2017.
 */

public class ARController {

    private final static String AR_CONTROLLER_TAG = "ARController";

    static {
        loadedNative = NativeInterface.loadNativeLibrary();
        //if (!loadedNative) Log.e(AR_CONTROLLER_TAG, "Loading native library failed!");
        //else Log.i(AR_CONTROLLER_TAG, "Loaded native library.");
    }



    private int frameWidth;
    private int frameHeight;

    /**
     * Array of RGB color values containing the debug video image data.
     */
    private byte[] debugImageData;

    /**
     * Array of int color values containing the debug video image data.
     */
    private int[] debugImageColors;
    private Bitmap debugBitmap = null;

    protected ARRenderer renderer;

    private boolean firstUpdate = false;

    /**
     * Set to true only once the native library has been loaded.
     */
    private static boolean loadedNative = false;
    private static boolean initedNative = false;

    /**
     * For any square template (pattern) markers, the number of rows
     * and columns in the template. May not be less than 16 or more than AR_PATT_SIZE1_MAX.
     */
    protected int pattSize = 16;

    /**
     * For any square template (pattern) markers, the maximum number
     * of markers that may be loaded for a single matching pass. Must be > 0.
     */
    protected int pattCountMax = 25;

    public void startAR(Context context){
        NativeInterface.arwInitialiseAR();
        if(ARToolKit.getInstance().initialiseNative(context.getCacheDir().getAbsolutePath())){

            return;
        }
    }

    public ARRenderer getARRender (){
        return renderer;
    }

    public void stopAR (){
        ARToolKit.getInstance().cleanup();
    }

    /*


    void ARController (){
    }

    public void initializeAR(int VideoWidth, int VideoHeight){
        NativeInterface.arwInitialiseAR();
        //ARToolKit.getInstance().nativeInitialised();
        initedNative = true;
        frameHeight = 480;
        frameWidth = 640;

        debugImageData = new byte[frameWidth * frameHeight * 4];
        debugImageColors = new int[frameWidth * frameHeight];
        debugBitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);

        renderer = new ItemRenderer();
        firstUpdate = true;
    }

    public Bitmap updateDebugBitmap() {

        if (!initedNative) return null;

        if (!NativeInterface.arwUpdateDebugTexture(debugImageData, true)) {
            return null;
        }

        int w = debugBitmap.getWidth();
        int h = debugBitmap.getHeight();

        int idx1, idx2;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                idx1 = (y * w + x) * 4;
                idx2 = (y * w + x);
                debugImageColors[idx2] = Color.argb(255 /*debugImageData[idx1+3] */ /*, debugImageData[idx1], debugImageData[idx1 + 1], debugImageData[idx1 + 2]);
                //debugImageColors[idx2] = Color.argb(255, 255, 0, 0);
            }
        }

        debugBitmap.setPixels(debugImageColors, 0, w, 0, 0, w, h);

        return debugBitmap;

    }

    public void receiveFrame(byte[] frame){

        debugImageData = frame;
        if (firstUpdate) {
            // ARToolKit has been initialised. The renderer can now add markers, etc...
            if (renderer.configureARScene()) {
                Log.i(AR_CONTROLLER_TAG, "Scene configured successfully");
            } else {
                // Error
                Log.e(AR_CONTROLLER_TAG, "Error configuring scene. Cannot continue.");
            }
            firstUpdate = false;
        }

        if (!initedNative){
            if(frame == null) {
                if(!NativeInterface.arwCapture()) {
                    if(NativeInterface.arwUpdateAR()) {
                        //callGlView();
                        updateDebugBitmap();

                    }
                }
            }
        }
    }


    */


    /*
    private CaptureCameraPreview preview;

    private byte[] framePreview = new byte[0];

    private GLSurfaceView glView;

    protected ARRenderer renderer;

    protected int pattSize = 16;

    protected int pattCountMax = 25;

    private boolean firstUpdate = false;


    void ARController (){
        renderer = new ItemRenderer();
    }

    public void resume (Context context){

        // Create the GL view
        glView = new GLSurfaceView(context);
        glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glView.getHolder().setFormat(PixelFormat.TRANSLUCENT); // Needs to be a translucent surface so the camera preview shows through.
        glView.setRenderer(renderer);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // Only render when we have a frame (must call requestRender()).
        glView.setZOrderMediaOverlay(true); // Request that GL view's SurfaceView be on top of other SurfaceViews (including CameraPreview's SurfaceView).

        if (glView != null) glView.onResume();
    }

    @Override
    public void cameraPreviewStarted(int width, int height, int rate, int cameraIndex, boolean cameraIsFrontFacing) {
        if (ARToolKit.getInstance().initialiseAR(width, height, "Data/camera_para.dat", 0, false)) { // Expects Data to be already in the cache dir. This can be done with the AssetUnpacker.
            Log.i(AR_CONTROLLER_TAG, "Camera initialised");
        } else {
            // Error
            Log.e(AR_CONTROLLER_TAG, "Error initialising camera. Cannot continue.");
        }

        firstUpdate = true;
    }

    public void receiveFrame(byte[] frame){
        this.framePreview = frame;
    }

    public void cameraPreviewFrame(byte[] frame) {

        if (firstUpdate) {
            // ARToolKit has been initialised. The renderer can now add markers, etc...
            if (renderer.configureARScene()) {
                Log.i(AR_CONTROLLER_TAG, "Scene configured successfully");
            } else {
                // Error
                Log.e(AR_CONTROLLER_TAG, "Error configuring scene. Cannot continue.");
            }
            firstUpdate = false;
        }

        if (ARToolKit.getInstance().convertAndDetect(frame)) {

            // Update the renderer as the frame has changed
            if (glView != null) glView.requestRender();

            onFrameProcessed();
        }

    }

    public void onFrameProcessed() {
    }

    @Override
    public void cameraPreviewStopped() {
        ARToolKit.getInstance().cleanup();
    }*/
}


