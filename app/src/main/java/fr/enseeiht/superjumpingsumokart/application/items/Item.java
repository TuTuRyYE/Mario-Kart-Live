package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * @author Matthieu Michel.
 *
 * Interface for items used in the game.
 * It defines the function to apply the effect of the item on a drone and how to
 * draw the icon of the item on the {@link fr.enseeiht.superjumpingsumokart.arpack.GUIGame} activity.
 */

public abstract class Item {

    /**
     * Name of the {@link Item}.
     */
    private final String NAME;

    /**
     * Default constructor of the class {@link Item}.
     * @param name name given to the Item.
     */
    public Item(String name) {
        NAME = name;
    }

    /**
     * Method used to use an {@link Item}.
     * @param droneController using the {@link Item}.
     * @param marker The marker on which the item shoulb be used (if the item requires a marker).
     * @return True if the item has been used, false otherwise.
     */
    public abstract boolean useItem(DroneController droneController, DetectionTask.Symbol marker);

    /**
     * Method used to apply the effect of an {@link Item} on a drone via its controller.
     */
    public abstract void applyEffect(DroneController droneController);

    /**
     * Put the object image on a {@link ImageButton}.
     * @param ib The {@link ImageButton} on which the image has to be displayed.
     */
    public abstract void assignResource(ImageButton ib);

    /**
     * Get the name of the Item.
     * @return The name of the Item.
     */
    public String getName() {
        return this.NAME;
    }

}
