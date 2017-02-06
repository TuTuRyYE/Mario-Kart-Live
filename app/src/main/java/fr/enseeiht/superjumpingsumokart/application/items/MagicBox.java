package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;

/**
 * Created by Vivian on 06/02/2017.
 */


public class MagicBox extends Item {

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
    public void applyEffect(DroneController sender, DroneController receiver) {
        int rand = (int) Math.floor(Math.random()*2);
        Item item;
        if (rand == 1) { //Banana
            item = new Banana();
        }
        if (rand == 2) { //Box
            item = new Box();
        }
        else {
            item = null;
        }
        sender.getDRONE().setCurrentItem(item);
    }

    @Override
    public void assignResource(ImageButton ib) {

    }
}
