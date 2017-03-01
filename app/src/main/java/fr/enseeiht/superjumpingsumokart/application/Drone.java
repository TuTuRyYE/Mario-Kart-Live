package fr.enseeiht.superjumpingsumokart.application;


import fr.enseeiht.superjumpingsumokart.application.items.*;

import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * Created by Vivian on 27/01/2017.
 * Class representing a drone
 */

public class Drone {
    /**
     * The name of the drone.
     */
    private String name;


    /**
     * The current item the drone has.
     */
    private Item currentItem;

    /**
     * The current number of lap the drone has done on a given circuit.
     */
    private int currentLap;

    /**
     * Get the last marker seen
     * @return the marker
     */
    public DetectionTask.Symbol getLastMarkerSeen() {
        return lastMarkerSeen;
    }

    /**
     * Set the last marker seen
     * @param lastMarkerSeen the marker
     */
    public void setLastMarkerSeen(DetectionTask.Symbol lastMarkerSeen) {
        this.lastMarkerSeen = lastMarkerSeen;
    }

    /**
     * The last marker detected
     */
    private DetectionTask.Symbol lastMarkerSeen;

    /**
     * Constructor for the class {@link Drone}.
     * @param name the name/id of the drone.
     */
    Drone(String name) {
        this.name = name;
        this.currentItem = new NullItem();

        this.currentLap = 0;
    }

    /**
     * Get the name of the drone.
     * @return the name/id of the drone.
     */
    public String getName() {

        return name;
    }

    /**
     * Set the name of the drone.
     * @param name the new name of the drone.
     */
    public void setName(String name) {

        this.name = name;
    }


    /**
     * Get the {@link Item} of the drone.
     * @return the current item of the drone.
     */
    public Item getCurrentItem() {
        return currentItem;
    }

    /**
     * Set the {@link Item} of the drone.
     * @param currentItem the new item of the drone.
     */
    public void setCurrentItem(Item currentItem) {

        this.currentItem = currentItem;
    }

    /**
     * Get the number of lap the drone has done.
     * @return the number of lap the drone has done.
     */
    public int getCurrentLap() {
        return currentLap;
    }

    /**
     * Set the number of lap the drone has done.
     * @param currentLap the number of lap the drone has done.
     */
    public void setCurrentLap(int currentLap) {
        this.currentLap = currentLap;
    }
}
