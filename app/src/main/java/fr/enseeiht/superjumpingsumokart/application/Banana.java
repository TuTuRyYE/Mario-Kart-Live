package fr.enseeiht.superjumpingsumokart.application;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;

/**
 * Created by michelmatthieu on 02/02/2017.
 */

public class Banana extends Item {

    private final static String  NAME = "banana";
    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     * @param position position of the Item.
     */
    public Banana (Vector3D position) {
        super(NAME, position);
    }

    @Override
    public void applyEffect(DroneController sender, DroneController receiver) {
        sender.slow();
        sender.spin();
    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.banane);
    }
}
