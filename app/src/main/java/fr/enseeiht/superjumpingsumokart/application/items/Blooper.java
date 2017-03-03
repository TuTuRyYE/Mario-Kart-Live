package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;
import fr.enseeiht.superjumpingsumokart.arpack.GUIGame;

/**
 * @author Romain Verset.
 * Implements a Blooper item.
 */

public class Blooper extends Item {

    /**
     * The logging tag. Useful for debugging.
     */
    private final static String ITEM_TAG = "Item.Blooper";

    /**
     * Name of the {@link Item}.
     */
    private final static String NAME = "blooper";

    /**
     * Reference of the {@link GUIGame}.
     */
    private final GUIGame GUI_GAME;

    /**
     * Default constructor of the class {@link Item}.
     */
    public Blooper (GUIGame guiGame){
        super(NAME);
        GUI_GAME = guiGame;
    }

    @Override
    public boolean useItem(DroneController controller, DetectionTask.Symbol symbol) {
        Log.d(ITEM_TAG, "Used a blooper");
        return true;
    }

    @Override
    public void applyEffect(DroneController droneController) {
        GUI_GAME.UPDATER.sendEmptyMessage(GUIGame.ANIMATE_BLOOPER);
    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setBackgroundResource(R.drawable.squid);
    }
}
