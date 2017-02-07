package fr.enseeiht.superjumpingsumokart.application;

import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by Vivian on 27/01/2017.
 * Class representing a drone
 */

public class Drone {
    /**
     * The name of the drone
     */
    private String name;

    /**
     * The current position of the drone in the 3D space
     */
    private Vector3D currentPosition;

    /**
     * The current item the drone has
     */
    private Item currentItem;

    /**
     * The current speed of the drone
     */
    private Vector3D currentSpeed;

    /**
     * The current number of lap the drone has done on a given circuit
     */
    private int currentLap;

    /**
     *  Marker's id the drone has seen during the lap
     */
    private ArrayList<Integer> markersSeen;

    /**
     * Constructor for the class {@link Drone}
     * @param name the name/id of the drone
     * @param currentPosition the position of the drone on a given time
     * @param currentItem the item the drone have
     * @param speed the current speed of the drone
     */
    Drone(String name, Vector3D currentPosition, Item currentItem, Vector3D speed) {
        this.name = name;
        this.currentPosition = currentPosition;
        this.currentItem = currentItem;
        this.currentSpeed = speed;
        this.currentLap = 0;
    }

    /**
     * Get the name of the drone
     * @return the name/id of the drone
     */
    public String getName() {

        return name;
    }

    /**
     * Set the name of the drone
     * @param name the new name of the drone
     */
    public void setName(String name) {

        this.name = name;
    }

    public Vector3D getCurrentPosition() {

        return currentPosition;
    }

    /**
     * Set the position of the drone
     * @param currentPosition the new currentPosition
     */
    public void setCurrentPosition(Vector3D currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * Get the {@link Item} of the drone
     * @return the current item of the drone
     */
    public Item getCurrentItem() {
        return currentItem;
    }

    /**
     * Set the {@link Item} of the drone
     * @param currentItem the new item of the drone
     */
    public void setCurrentItem(Item currentItem) {

        this.currentItem = currentItem;
    }

    /**
     * Get the speed of the drone
     * @return the speed of the drone
     */
    public Vector3D getSpeed() {

        return currentSpeed;
    }

    /**
     * Set the speed of the drone
     * @param speed the new speed of the drone
     */
    public void setSpeed(Vector3D speed) {

        this.currentSpeed = speed;
    }

    /**
     * Get the number of lap the drone has done
     * @return the number of lap the drone has done
     */
    public int getCurrentLap() {
        return currentLap;
    }

    /**
     * Set the number of lap the drone has done
     * @param currentLap the number of lap the drone has done
     */
    public void setCurrentLap(int currentLap) {
        this.currentLap = currentLap;
    }

    /**
     * Get the marker's ids the drone has seen during the current lap
     * @return the ids of the markers the drone has seen during the current lap
     */
    public ArrayList<Integer> getMarkersSeen() {
        return markersSeen;
    }

    /**
     * Set the marker's ids the drone has seen during the current lap
     * @param markersSeen the marker's ids the drone has seen during the current lap
     */
    public void setMarkersSeen(ArrayList<Integer> markersSeen) {
        this.markersSeen = markersSeen;
    }
}
