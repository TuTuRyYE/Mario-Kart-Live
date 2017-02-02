package fr.enseeiht.superjumpingsumokart.application;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;

/**
 * Created by michelmatthieu on 02/02/2017.
 */

public class Box extends Item {

    private final static String  NAME = "box";

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     * @param position position of the Item.
     */
    public Box(Vector3D position) {
        super(NAME, position);
    }

    @Override
    public void applyEffect(DroneController sender, DroneController receiver) {
        sender.stopMotion();
        sender.jump();

    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.nul);
    }

}
