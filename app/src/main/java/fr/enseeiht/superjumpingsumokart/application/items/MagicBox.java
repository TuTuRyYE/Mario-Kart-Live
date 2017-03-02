package fr.enseeiht.superjumpingsumokart.application.items;

import android.util.Log;
import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;

/**
 * Implementation of magic Box {@link Item}.
 * Created by Vivian on 06/02/2017.
 */


public class MagicBox extends Item {

    /**
     * The logging tag. Useful for debugging.
     */
    private final static String ITEM_TAG = "Item";
    /**
     * Name of the {@link Item}.
     */
    private final static String NAME = "magicBox";

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     *
     */
    public MagicBox() {
        super(NAME);
    }

    @Override
    public boolean useItem(DroneController droneController) {
        return true;
    }

    @Override
    public void applyEffect(DroneController droneController) {
        Log.d(ITEM_TAG, "A magic box has been touched");
        int rand = 1+ (int) Math.floor(Math.random() * 5);

        Item item;
        switch (rand) {
            case 1 :
                item = new Banana();
                break;
            case 2 :
                item = new Box();
                break;
            case 3 :
                item = new RedShell();
                break;
            case 4 :
                item = new Mushroom();
                break;
            case 5 :
                item = new Blooper();
                break;
            default:
                item = new NullItem();
        }
        droneController.getDrone().setCurrentItem(item);
        Log.d(ITEM_TAG, "A, "+ item.getName()+" has been assigned to the droneController");
    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.magicbox);
    }
}
