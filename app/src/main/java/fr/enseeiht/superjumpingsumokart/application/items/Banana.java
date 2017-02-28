package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.Circuit;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * Implementation of Banana {@link Item}.
 * Created by michelmatthieu on 02/02/2017.
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
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     */
    public Banana() {
        super(NAME);
    }

    @Override
    public boolean useItem(DroneController droneController) {
        DetectionTask.Symbol lastMarkerSeen = droneController.getDrone().getLastMarkerSeen();
        if (lastMarkerSeen != null) {
            Circuit.getInstance().addObject(lastMarkerSeen, this);
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
        ib.setImageResource(R.drawable.banana);
    }
}
