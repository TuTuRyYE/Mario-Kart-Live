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
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.HIRO,ARToolKit.getInstance().addMarker("multi;Data/patt.hiro;80"));
        Log.d(ITEM_RENDERER_TAG,Integer.toString(SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.HIRO)));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.KANJI, ARToolKit.getInstance().addMarker("multi;Data/patt.kanji;80"));
        Log.d(ITEM_RENDERER_TAG,Integer.toString(SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.KANJI)));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.A, ARToolKit.getInstance().addMarker("single;Data/patt.a;80"));
        Log.d(ITEM_RENDERER_TAG,Integer.toString(SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.A)));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.B, ARToolKit.getInstance().addMarker("single;Data/patt.b;80"));
        Log.d(ITEM_RENDERER_TAG,Integer.toString(SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.B)));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.C, ARToolKit.getInstance().addMarker("single;Data/patt.c;80"));
        Log.d(ITEM_RENDERER_TAG,Integer.toString(SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.C)));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.D, ARToolKit.getInstance().addMarker("single;Data/patt.d;80"));
        Log.d(ITEM_RENDERER_TAG,Integer.toString(SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.D)));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.F, ARToolKit.getInstance().addMarker("single;Data/patt.f;80"));
        Log.d(ITEM_RENDERER_TAG,Integer.toString(SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.F)));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.G, ARToolKit.getInstance().addMarker("single;Data/patt.g;80"));
        Log.d(ITEM_RENDERER_TAG,Integer.toString(SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.G)));
        int magicBoxId = SYMBOLS_HASH_MAP.get(DetectionTask.Symbol.A);
        ARToolKit.getInstance().addModel("Data/models/box.obj", "single;Data/patt.hiro;80", magicBoxId, 100.0f, true);
        return true;
    }

    public void defineModelAtSymbol(Item item, DetectionTask.Symbol symbol) {
        Log.d(ITEM_RENDERER_TAG, "Defined model for symbol : " + symbol.name());
        String symbolString = symbol.name().toLowerCase();
        int id = SYMBOLS_HASH_MAP.get(symbol);
        if (item instanceof Banana) {
            ARToolKit.getInstance().addModel("Data/models/giantbanana.obj", "single;Dara/patt.".concat(symbolString).concat(";80"), id, 20.0f, true);
        } /*else if (item instanceof FakeBox) {
            ARToolKit.getInstance().addModel("Data/models/box.obj", "single;Data/patt.".concat(symbolString).concat(";80"), id, 20.0f, false);
        }*/

    }

    public void deleteModelAtSymbol(DetectionTask.Symbol symbol) {
        Log.d(ITEM_RENDERER_TAG, "Deleted model for symbol : " + symbol.name());
        String symbolString = symbol.name().toLowerCase();
        int id = SYMBOLS_HASH_MAP.get(symbol);
        ARToolKit.getInstance().addModel("Data/models/void.obj", "single;Dara/patt.".concat(symbolString).concat(";80"), id, .0f, false);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (ARToolKit.getInstance().getProjectionMatrix() != null) {
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
