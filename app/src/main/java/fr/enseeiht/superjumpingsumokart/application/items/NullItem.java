package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * @author Matthieu Michel.
 * Implementation of null {@link Item}.
 * This item symbolizes the absence of item.
 */

public class NullItem extends Item {

    /**
     * Name of the {@link Item}.
     */
    private final static String NAME = "nullItem";

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 06/02/2017).
     */
    public NullItem() {
        super(NAME);
    }


    @Override
    public boolean useItem(DroneController droneController, DetectionTask.Symbol symbol) {
        return false;
    }

    @Override
    public void applyEffect(DroneController droneController) {

    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.null_object);
    }
}
