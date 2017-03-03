package fr.enseeiht.superjumpingsumokart.application;


import fr.enseeiht.superjumpingsumokart.application.items.*;

import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * @author Vivian Guy.
 * Class representing a drone in the application.
 * It represents a drone as a player, not as a device.
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
     * The current number of lap made by the drone on the {@link fr.enseeiht.superjumpingsumokart.application.circuit.Circuit#circuitInstance}.
     */
    private int currentLap;

    /**
     * The current number of checkpoint validated by the drone on the {@link fr.enseeiht.superjumpingsumokart.application.circuit.Circuit#circuitInstance}.
     */
    private int currentCheckpoint;

    /**
     * The last marker seen by the drone.
     */
    private DetectionTask.Symbol lastMarkerSeen;

    /**
     * Constructor for the class {@link Drone}.
     * @param name the name/id of the drone.
     */
    Drone(String name) {
        this.name = name;
        this.currentItem = new NullItem();
        this.currentCheckpoint = 0;
        this.currentLap = 0;
    }

    /**
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
    void setCurrentLap(int currentLap) {
        this.currentLap = currentLap;
    }

    /**
     *
     * @return The current number of checkpoints validated by the drone.
     */
    public int getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    /**
     * Set the number of checkpoint validated by the drone.
     * @param currentCheckpoint the number of checkpoint validated by the drone.
     */
    void setCurrentCheckpoint(int currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }

    /**
     * @return the marker last marker seen by the drone.
     */
    public DetectionTask.Symbol getLastMarkerSeen() {
        return lastMarkerSeen;
    }

    /**
     * @param lastMarkerSeen the marker to put as last marker seen.
     */
    public void setLastMarkerSeen(DetectionTask.Symbol lastMarkerSeen) {
        this.lastMarkerSeen = lastMarkerSeen;
    }
}
