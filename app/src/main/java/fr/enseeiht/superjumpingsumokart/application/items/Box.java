package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;

/**
 * Created by michelmatthieu on 02/02/2017.
 */

public class Box extends Item {

    private final static String  NAME = "box";

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     */
    public Box() {
        super(NAME);
    }


    @Override
    public void applyEffect(DroneController droneController) {
        droneController.stopMotion();
        droneController.jump();
        droneController.getGUI_GAME().getGame().onItemTouched(this);
    }

    @Override
    public void useItem(DroneController droneController) {
        this.setPosition(droneController.getDRONE().getCurrentPosition());
        droneController.getGUI_GAME().getGame().onItemUsed(this);
    }

    @Override
    public void assignResource(ImageButton ib) {
        ib.setImageResource(R.drawable.null_object);
    }

}
