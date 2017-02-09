package fr.enseeiht.superjumpingsumokart.arpack;

import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by JorgeEnrique on 2/02/2017.
 */

public class ItemRenderer extends ARRenderer {

    private final static String ITEM_RENDERER_TAG = "ItemRenderer";

    private int markerID = -1;
    private Cube cube = new Cube(40.0f, 0.0f, 0.0f, 20.0f);
    float refCub[] = null; //Last position reference
    private float angle = 45.0f;

    /**
     * Markers can be configured here.
     */
    @Override
    public boolean configureARScene() {
        Log.d(ITEM_RENDERER_TAG, "configureARScene() called.");
        markerID = ARToolKit.getInstance().addMarker("single;Data/hiro.patt;80");
        if (markerID < 0) return false;
        return true;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (ARToolKit.getInstance().getProjectionMatrix() != null){
            super.onDrawFrame(gl);
            Log.d(ITEM_RENDERER_TAG, "onDrawFrame() called.");
            draw(gl);
        }
    }

    /**
     * Override the draw function from ARRenderer.
     */
    @Override
    public void draw(GL10 gl) {

        Log.d(ITEM_RENDERER_TAG, "draw() function called.");

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Apply the ARToolKit projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glFrontFace(GL10.GL_CW);

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        // If the marker is visible, apply its transformation, and draw a cube
        if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {
            refCub = ARToolKit.getInstance().queryMarkerTransformation(markerID);
            gl.glLoadMatrixf(ARToolKit.getInstance().queryMarkerTransformation(markerID), 0);
            gl.glPushMatrix();
            gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
            gl.glTranslatef(50.0f, 50.0f, 50.0f);
            cube.draw(gl);
            gl.glPopMatrix();
        }else {
            //Log.i("SimpleRenderer","marker not found");
            if(refCub != null){
                gl.glLoadMatrixf(refCub, 0);
                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                gl.glTranslatef(50.0f, 50.0f, 50.0f);
                cube.draw(gl);
                gl.glPopMatrix();

            }
        }

    }

}
