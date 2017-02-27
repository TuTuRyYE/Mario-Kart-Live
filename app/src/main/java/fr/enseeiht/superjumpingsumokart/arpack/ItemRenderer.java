package fr.enseeiht.superjumpingsumokart.arpack;

import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import fr.enseeiht.superjumpingsumokart.application.items.Banana;
import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Renderer to render on the {@link GUIGame} {@link android.opengl.GLSurfaceView}.
 * Created by JorgeEnrique on 2/02/2017.
 */

public final class ItemRenderer extends ARRenderer {

    final static HashMap<DetectionTask.Symbol, Integer> SYMBOLS_HASH_MAP = new HashMap<>();


    private final static String ITEM_RENDERER_TAG = "ItemRenderer";

    /**
     * Markers can be configured here.
     */
    public boolean configureARScene() {
        Log.d(ITEM_RENDERER_TAG, "configureARScene() called.");
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.HIRO,ARToolKit.getInstance().addMarker("multiple;Data/patt.hiro;80"));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.KANJI, ARToolKit.getInstance().addMarker("multiple;Data/patt.kanji;80"));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.KANJI, ARToolKit.getInstance().addMarker("single;Data/patt.a;80"));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.KANJI, ARToolKit.getInstance().addMarker("single;Data/patt.b;80"));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.KANJI, ARToolKit.getInstance().addMarker("single;Data/patt.c;80"));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.KANJI, ARToolKit.getInstance().addMarker("single;Data/patt.d;80"));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.KANJI, ARToolKit.getInstance().addMarker("single;Data/patt.f;80"));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.KANJI, ARToolKit.getInstance().addMarker("single;Data/patt.g;80"));
        int magicBoxId = SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.A);
        ARToolKit.getInstance().addModel("Data/models/box.obj", "single;Data/patt.a;80", 0, 20.0f, true);

        return true;
    }

    public void defineModelAtSymbol(Item item, DetectionTask.Symbol symbol) {
        String symbolString = symbol.name().toLowerCase();
        int id = SYMBOLS_HASH_MAP.get(symbol);
        if (item instanceof Banana) {
            ARToolKit.getInstance().addModel("Data/models/giantbanana.obj", "single;Dara/patt.".concat(symbolString).concat(";80"), id, 20.0f, true);
        } /*else if (item instanceof FakeBox) {
            ARToolKit.getInstance().addModel("Data/models/box.obj", "single;Data/patt.".concat(symbolString).concat(";80"), id, 20.0f, false);
        }*/

    }

    public void deleteModelAtSymbole(DetectionTask.Symbol symbol) {
        String symbolString = symbol.name().toLowerCase();
        int id = SYMBOLS_HASH_MAP.get(symbol);
        ARToolKit.getInstance().addModel("Data/models/void.obj", "single;Dara/patt.".concat(symbolString).concat(";80"), id, .0f, false);
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
