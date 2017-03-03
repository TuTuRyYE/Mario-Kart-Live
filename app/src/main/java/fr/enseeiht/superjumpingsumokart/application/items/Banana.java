package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.circuit.Circuit;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * @author Matthieu Michel.
 * Implementation of Banana {@link Item}.
 */

public class Banana extends Item {

    /**
     * The logging tag. Useful for debugging.
     */
    private final static String ITEM_TAG = "Item.Banana";

    /**
     * Name of the {@link Item}.
     */
    private final static String NAME = "banana";

    /**
     * Default constructor of the class {@link Item}.
     */
    public Banana() {
        super(NAME);
    }

    @Override
    public boolean useItem(DroneController controller, DetectionTask.Symbol symbol) {
        if (symbol != null) {
            Circuit.getInstance().addObject(symbol, this);
            Log.d(ITEM_TAG, "A banana has been put on the circuit");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void applyEffect(DroneController droneController) {
        Log.d(ITEM_TAG, "A banana has been touched");
        droneController.slow();
        droneController.spin();

    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setBackgroundResource(R.drawable.banana);
    }
}
