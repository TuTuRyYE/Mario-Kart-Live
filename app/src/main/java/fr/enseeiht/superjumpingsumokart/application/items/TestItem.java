package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;

/**
 * Created by michelmatthieu on 02/02/2017.
 */

public class TestItem extends Item {

    private final static String  NAME = "testItem";
    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     */
    public TestItem() {
        super(NAME);
    }


    @Override
    public void applyEffect(DroneController droneController) {

    }

    @Override
    public void useItem(DroneController droneController) {

    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.null_object);
    }
}
