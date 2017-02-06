package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.application.DroneController;

/**
 * Created by michelmatthieu on 06/02/2017.
 */

public class NullItem extends Item {

    private final static String  NAME = "banana";

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 06/02/2017).
     *
     * @param name name given to the Item.
     */
    public NullItem() {
        super(NAME);
    }

    @Override
    public void applyEffect(DroneController sender, DroneController receiver) {

    }

    @Override
    public void assignResource(ImageButton ib) {

    }
}
