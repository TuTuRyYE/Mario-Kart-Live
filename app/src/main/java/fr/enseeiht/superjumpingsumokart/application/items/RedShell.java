package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;

/**
 * Created by Lucas on 17/02/2017.
 */

public class RedShell extends Item {
    /**
     * The logging tag. Useful for debugging.
     */
    private final static String ITEM_TAG = "ITEM";
    /**
     * Name of the {@link Item}.
     */
    private final static String  NAME = "redshell";
    /**
     * Default constructor of the class {@link Item}. (Lucas Pascal) - 17/02/2017).

     */
    public RedShell () {
        super(NAME);

    }

    @Override
    public void applyEffect(DroneController droneController) {
        Log.d(ITEM_TAG,"You've been hit by a red shell!");
        droneController.slow();
        droneController.spin();
        droneController.getGuiGame().getGame().onItemTouched(this);
    }

    @Override
    public void useItem(DroneController droneController) {
        this.setPosition(droneController.getDrone().getCurrentPosition());
        Log.d(ITEM_TAG,"A red shell has been thrown!");
    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.redshell);
    }
}
