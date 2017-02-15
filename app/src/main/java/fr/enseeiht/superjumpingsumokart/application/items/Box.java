package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;

/**
 * Created by michelmatthieu on 02/02/2017.
 */

public class Box extends Item {


    /**
     * The logging tag. Useful for debugging.
     */
    private final static String ITEM_TAG = "ITEM";

    /**
     * Name of the {@link Item}.
     */
    private final static String  NAME = "box";

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     */
    public Box() {
        super(NAME);
    }


    @Override
    public void applyEffect(DroneController droneController) {
        Log.d(ITEM_TAG,"A TNT box has been touched");
        droneController.stopMotion();
        droneController.jump();
        droneController.getGUI_GAME().getGame().onItemTouched(this);
    }

    @Override
    public void useItem(DroneController droneController) {
        this.setPosition(droneController.getDrone().getCurrentPosition());
        droneController.getGUI_GAME().getGame().onItemUsed(this);
        Log.d(ITEM_TAG,"A TNT box has been put on the circuit");
    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.null_object);
    }

}
