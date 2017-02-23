package fr.enseeiht.superjumpingsumokart.arpack;

import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Renderer to render on the {@link GUIGame} {@link android.opengl.GLSurfaceView}.
 * Created by JorgeEnrique on 2/02/2017.
 */

class ItemRenderer extends ARRenderer {

    private final static String ITEM_RENDERER_TAG = "ItemRenderer";

    /**
     * Markers can be configured here.
     */
    public boolean configureARScene() {
        Log.d(ITEM_RENDERER_TAG, "configureARScene() called.");
        //markerID = ARToolKit.getInstance().addMarker("single;Data/hiro.patt;80");
        ARToolKit.getInstance().addModel("Data/models/giantbanana.obj", "single;Data/hiro.patt;80", 0, 20f, true);
        //ARToolKit.getInstance().addModel("Data/models/Ferrari_Modena_Spider.obj", "single;Data/patt.kanji;80", 1);
        return true;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (ARToolKit.getInstance().getProjectionMatrix() != null) {
            Log.d(ITEM_RENDERER_TAG, "onDrawFrame() called.");
            draw(gl);
        }
    }

    /**
     * Override the draw function from ARRenderer.
     */
    @Override
    public void draw(GL10 gl) {
        ARToolKit.getInstance().drawModels();
    }

}
