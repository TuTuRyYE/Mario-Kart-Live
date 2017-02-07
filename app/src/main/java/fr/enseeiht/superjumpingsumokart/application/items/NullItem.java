package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;

/**
 * Created by michelmatthieu on 06/02/2017.
 */

public class NullItem extends Item {

    private final static String  NAME = "nullItem";

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 06/02/2017).
     */
    public NullItem() {
        super(NAME);
    }

    @Override
    public void applyEffect(DroneController sender, DroneController receiver) {

    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.nul);
    }
}
