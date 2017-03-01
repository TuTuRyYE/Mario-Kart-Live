package fr.enseeiht.superjumpingsumokart.arpack;

import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import fr.enseeiht.superjumpingsumokart.application.items.Banana;
import fr.enseeiht.superjumpingsumokart.application.items.Box;
import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * @author Romain Verset, JorgeEnrique.
 * Renderer to render on the {@link GUIGame} {@link android.opengl.GLSurfaceView}.
 */

public final class ItemRenderer extends ARRenderer {

    final static HashMap<DetectionTask.Symbol, Integer> SYMBOLS_HASH_MAP = new HashMap<>();


    private final static String ITEM_RENDERER_TAG = "ItemRenderer";

    /**
     * Markers can be configured here.
     */
    public boolean configureARScene() {
        Log.d(ITEM_RENDERER_TAG, "configureARScene() called.");
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.HIRO,ARToolKit.getInstance().addMarker("single;Data/patt.hiro;80"));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.KANJI, ARToolKit.getInstance().addMarker("single;Data/patt.kanji;80"));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.A, ARToolKit.getInstance().addModel("Data/models/maxbox.obj", "single;Data/patt.a;80", DetectionTask.Symbol.A.ordinal(), 2.0f, false));
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.B, ARToolKit.getInstance().addModel("Data/models/giantbanana.obj", "single;Data/patt.a;80", DetectionTask.Symbol.B.ordinal(), 20.0f, true));
        deleteModelAtSymbol(DetectionTask.Symbol.B);
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.C, ARToolKit.getInstance().addModel("Data/models/giantbanana.obj", "single;Data/patt.a;80", DetectionTask.Symbol.C.ordinal(), 20.0f, true));
        deleteModelAtSymbol(DetectionTask.Symbol.C);
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.D, ARToolKit.getInstance().addModel("Data/models/giantbanana.obj", "single;Data/patt.a;80", DetectionTask.Symbol.D.ordinal(), 20.0f, true));
        deleteModelAtSymbol(DetectionTask.Symbol.D);
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.F, ARToolKit.getInstance().addModel("Data/models/giantbanana.obj", "single;Data/patt.a;80", DetectionTask.Symbol.F.ordinal(), 20.0f, true));
        deleteModelAtSymbol(DetectionTask.Symbol.F);
        SYMBOLS_HASH_MAP.put(DetectionTask.Symbol.G, ARToolKit.getInstance().addModel("Data/models/giantbanana.obj", "single;Data/patt.a;80", DetectionTask.Symbol.G.ordinal(), 20.0f, true));
        deleteModelAtSymbol(DetectionTask.Symbol.G);
        return true;
    }

    public void defineModelAtSymbol(Item item, DetectionTask.Symbol symbol) {
        Log.d(ITEM_RENDERER_TAG, "Defined model for symbol : " + symbol.name());
        String symbolString = symbol.name().toLowerCase();
        if (item instanceof Banana) {
            SYMBOLS_HASH_MAP.put(symbol, ARToolKit.getInstance().addModel("Data/models/giantbanana.obj", "single;Data/patt.".concat(symbolString).concat(";80"), symbol.ordinal(), 20.0f, true));
        } else if (item instanceof Box) {
            SYMBOLS_HASH_MAP.put(symbol, ARToolKit.getInstance().addModel("Data/models/maxbox.obj", "single;Data/patt.".concat(symbolString).concat(";80"), symbol.ordinal(), 2.5f, false));
        }

    }

    public void deleteModelAtSymbol(DetectionTask.Symbol symbol) {
        Log.d(ITEM_RENDERER_TAG, "Deleted model for symbol : " + symbol.name());
        ARToolKit.getInstance().disableModel(symbol.ordinal());
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
