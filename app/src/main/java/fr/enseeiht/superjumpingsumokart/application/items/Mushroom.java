package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;

/**
 * @author Romain Verset.
 * Implementation of a boosting Mushroom.
 */

public class Mushroom extends Item {

    private final static String ITEM_TAG = "Item.Mushroom";

    private final static String NAME = "mushroom";

    public Mushroom() {
        super(NAME);
    }

    @Override
    public boolean useItem(DroneController droneController) {
        Log.d(ITEM_TAG, "You used a mushroom !");
        droneController.boost();
        return true;
    }

    @Override
    public void applyEffect(DroneController droneController) {
        // Nothing to do
    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.mushroom);
    }
}
