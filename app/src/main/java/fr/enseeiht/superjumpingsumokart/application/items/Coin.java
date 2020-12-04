package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.circuit.Circuit;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * Implementation of Coin{@link Item}.
 * @author Marien AMATHIEU.
 */

public class Coin extends Item {

    /**
     * The logging tag. Useful for debugging.
     */
    private final static String ITEM_TAG = "Item.Coin";

    /**
     * Name of the {@link Item}.
     */
    private final static String NAME = "coin";

    /**
     * Default constructor of the class {@link Coin}.
     */
    public Coin() {
        super(NAME);
    }

    @Override
    public boolean useItem(DroneController droneController, DetectionTask.Symbol marker) {
        return true;
    }

    @Override
    public void applyEffect(DroneController droneController) {
        Log.d(ITEM_TAG, "A coin has been touched");
        //droneController.spin();
    }

    @Override
    public void assignResource(ImageButton ib) {
       // ib.setImageResource(R.drawable.coin);
    }
}


