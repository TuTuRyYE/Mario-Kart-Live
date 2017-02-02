package fr.enseeiht.superjumpingsumokart.arpack;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.camera.CameraEventListener;
import org.artoolkit.ar.base.camera.CaptureCameraPreview;
import org.artoolkit.ar.base.rendering.ARRenderer;

/**
 * Created by JorgeEnrique on 2/02/2017.
 */

public class ARController implements CameraEventListener {

    private final static String AR_CONTROLLER_TAG = "ARController";

    private CaptureCameraPreview preview;

    private byte[] framePreview = new byte[0];

    private GLSurfaceView glView;

    protected ARRenderer renderer;

    protected int pattSize = 16;

    protected int pattCountMax = 25;

    private boolean firstUpdate = false;


    void ARController (){
        renderer = new Renderer();
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
    }
}


