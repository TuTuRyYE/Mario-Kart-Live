package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.Game;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;

/**
 * Created by michelmatthieu on 02/02/2017.
 */

public class Banana extends Item {


    /**
     * The logging tag. Useful for debugging.
     */
    private final static String ITEM_TAG = "ITEM";
    /**
     * Name of the {@link Item}.
     */
    private final static String  NAME = "banana";
    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).

     */
    public Banana () {
        super(NAME);
        
    }

    @Override
    public void applyEffect(DroneController droneController) {
        Log.d(ITEM_TAG,"A banana has been touched");
        droneController.slow();
        droneController.spin();
        droneController.getGUI_GAME().getGame().onItemTouched(this);
    }

    @Override
    public void useItem(DroneController droneController) {
        this.setPosition(droneController.getDrone().getCurrentPosition());
        droneController.getGUI_GAME().getGame().onItemUsed(this);
        Log.d(ITEM_TAG,"A banana has been put on the circuit");

    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.banane);
    }
}
