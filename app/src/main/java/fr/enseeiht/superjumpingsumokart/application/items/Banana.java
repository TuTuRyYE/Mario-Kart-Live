package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;

/**
 * Created by michelmatthieu on 02/02/2017.
 */

public class Banana extends Item {

    private final static String  NAME = "banana";
    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).

     */
    public Banana () {
        super(NAME);
        
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
