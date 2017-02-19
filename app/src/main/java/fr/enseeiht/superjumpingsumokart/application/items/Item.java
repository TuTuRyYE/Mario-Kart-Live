package fr.enseeiht.superjumpingsumokart.application.items;

import android.widget.ImageButton;

import fr.enseeiht.superjumpingsumokart.application.Drone;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;

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
     * Item position.
     * Initialised at (0, 0, 0) and then set, if necessary when the {@link Item} is used.
     */
    private Vector3D position;

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
     * Check if the {@link Drone} is in contact with an {@link Item} during the race. (Matthieu Michel - 02/02/2017).
     * The distance is check according to a threshold.
     *
     * @param player the drone.
     * @return true if the drone touches the Item otherwise return false.
     */
    public boolean isInContact(Drone player) {
        //boolean to be returned
        boolean contact = false;
        //get the coordinates of the Item.
        double xItem = this.position.getX();
        double yItem = this.position.getY();
        double zItem = this.position.getZ();
        //get the coordinates of the Drone
        double xPlayer = this.position.getX();
        double yPlayer = this.position.getY();
        double zPlayer = this.position.getZ();

        //compute the distance between the Item and the drone
        double eucDistance = Math.sqrt((Math.pow((xItem - xPlayer), 2)) + Math.pow((yItem - yPlayer), 2) + Math.pow((zItem - zPlayer), 2));

        if (eucDistance <= THRESHOLD_DISTANCE) {
            contact = true;
        }
        return contact;
    }

    /**
     * Get the name of the Item. (Matthieu Michel - 02/02/2017).
     *
     * @return name of the Item.
     */
    public String getName() {
        return this.NAME;
    }

    /**
     * Get the position of the Item. (Matthieu Michel - 02/02/2017).
     *
     * @return position of the Item.
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Set the position of the Item. (Matthieu Michel - 02/02/2017).
     *
     * @param position position of the Item.
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

}
