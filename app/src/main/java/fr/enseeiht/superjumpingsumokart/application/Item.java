package fr.enseeiht.superjumpingsumokart.application;

import android.widget.ImageButton;

/**
 * Created by Vivian on 27/01/2017.
 */

public abstract class Item {

    /**
     * Threshold for the distance mesure to know if the object is in contact with the device or not.
     */
    protected final static double thresholdDistance = 2;
    /**
     * Item name.
     */
    protected final String NAME;
    // 3D model to add
    /**
     * Item position relative to the startItem (x,y,z).
     */
    protected Vector3D position;

    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     * @param name name given to the Item.
     * @param position position of the Item.
     */
    public Item(String name, Vector3D position) {
        this.NAME = name;
        this.position = position;
    }

    /**
     * Get the name of the Item. (Matthieu Michel - 02/02/2017).
     * @return name of the Item.
     */
    public String getName() {
        return this.NAME;
    }


    /**
     * Get the position of the Item. (Matthieu Michel - 02/02/2017).
     * @return position of the Item.
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Set the position of the Item. (Matthieu Michel - 02/02/2017).
     * @param position position of the Item.
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }



    /**
     * Check if the {@link Drone} is in contact with an {@link Item} during the race. (Matthieu Michel - 02/02/2017).
     * The distance is check according to a threshold.
     * @param player the drone.
     * @return true if the drone touches the Item otherwise return false.
     */
    public boolean isInContact (Drone player) {
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
        double eucDistance = Math.sqrt((Math.pow((xItem-xPlayer),2))+Math.pow((yItem-yPlayer),2)+Math.pow((zItem-zPlayer),2));

        if (eucDistance<=thresholdDistance) {
            contact = true;
        }
        return contact;
    }



    /**
     * Method used to apply the Effect of an {@link Item} on the device. (Matthieu Michel - 02/02/2017).
     * @param sender the device sending the trap.
     * @param receiver the device undergoing the trap.
     */
    public abstract void applyEffect(DroneController sender,DroneController receiver);

    public abstract void assignResource(ImageButton ib);

}
