package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;

/**
 * Implementation of magic Box {@link Item}.
 * Created by Vivian on 06/02/2017.
 */


public class MagicBox extends Item {

    /**
     * The logging tag. Useful for debugging.
     */
    private final static String ITEM_TAG = "ITEM";
    /**
     * Name of the {@link Item}.
     */
    private final static String NAME = "magicBox";

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     *
     * @param position position of the magic box on the circuit
     */
    public MagicBox(Vector3D position) {
        super(NAME);
        this.setPosition(position);
    }

    @Override
    public void useItem(DroneController droneController) {
    }

    @Override
    public void applyEffect(DroneController droneController) {
        Log.d(ITEM_TAG, "A magic box has been touched");
        int rand = (int) Math.floor(Math.random() * 2);
        Item item;
        if (rand == 1) { //Banana
            item = new Banana();
        } else if (rand == 2) { //Box
            item = new Box();
        } else {
            item = null;
        }

        droneController.getDrone().setCurrentItem(item);
        Log.d(ITEM_TAG, "An item has been assigned to the droneController");
    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.magicbox);
    }
}
