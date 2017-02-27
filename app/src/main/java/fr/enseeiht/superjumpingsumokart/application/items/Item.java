package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.application.Drone;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * Interface for items used in the game {@link Item}.
 * It defines the function to apply the effect of the item on a drone and how to
 * draw the icon of the item on the {@link fr.enseeiht.superjumpingsumokart.arpack.GUIGame} activity.
 * Created by Vivian on 27/01/2017.
 */

public abstract class Item {

    /**
     * Threshold for the distance in order to know if the object is in contact with the device or not.
     */
    private final static double THRESHOLD_DISTANCE = 2;

    /**
     * Name of the {@link Item}.
     */
    private final String NAME;

    /**
     * The marker's symbol that the item will be associated with.
     */
    private DetectionTask.symbols symbol;

    /**
     * Get the marker that the item is associated with.
     * @return the marker's symbol.
     */
    public DetectionTask.symbols getSymbol() {
        return symbol;
    }

    /**
     * Associate a symbol to the item.
     * @param symbol the symbol
     */
    public void setSymbol(DetectionTask.symbols symbol) {
        this.symbol = symbol;
    }
    // TODO - Add 3D model

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     *
     * @param name name given to the Item.
     */
    public Item(String name) {
        NAME = name;
    }

    /**
     * Method used to use an {@link Item}. (Matthieu Michel - 15/02/2017)
     *
     * @param droneController using the {@link Item}.
     */
    public abstract void useItem(DroneController droneController);

    /**
     * Method used to apply the effect of an {@link Item} on a drone via its controller. (Matthieu Michel - 02/02/2017).
     */
    public abstract void applyEffect(DroneController droneController);

    /**
     * Put the object image on a {@link ImageButton} (Romain Verset - 02/02/2017).
     *
     * @param ib The {@link ImageButton} on which the image has to be displayed;
     */
    public abstract void assignResource(ImageButton ib);


    /**
     * Get the name of the Item. (Matthieu Michel - 02/02/2017).
     *
     * @return name of the Item.
     */
    public String getName() {
        return this.NAME;
    }




}
