package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.arpack.GUIGame;

/**
 * @author Romain Verset.
 * Implements a Blooper item.
 */

public class Blooper extends Item {

    private final static String ITEM_TAG = "Item.Blooper";

    private final static String NAME = "blooper";

    private final GUIGame GUI_GAME;

    public Blooper (GUIGame guiGame){
        super(NAME);
        GUI_GAME = guiGame;
    }

    @Override
    public boolean useItem(DroneController droneController) {
        Log.d(ITEM_TAG, "Used a blooper");
        return false;
    }

    @Override
    public void applyEffect(DroneController droneController) {
        GUI_GAME.UPDATER.sendEmptyMessage(GUIGame.ANIMATE_BLOOPER);
    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.squid);
    }
}
