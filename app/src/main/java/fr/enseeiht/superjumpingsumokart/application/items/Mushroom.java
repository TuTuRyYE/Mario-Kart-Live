package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * @author Romain Verset.
 * Implementation of a boosting Mushroom.
 */

public class Mushroom extends Item {

    /**
     * The logging tag. Useful for debugging.
     */
    private final static String ITEM_TAG = "Item.Mushroom";

    /**
     * Name of the {@link Item}.
     */
    private final static String NAME = "mushroom";

    /**
     * Default constructor of the class {@link Item}.
     */
    public Mushroom() {
        super(NAME);
    }

    @Override
    public boolean useItem(DroneController droneController, DetectionTask.Symbol symbol) {
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
        ib.setBackgroundResource(R.drawable.mushroom);
    }
}
