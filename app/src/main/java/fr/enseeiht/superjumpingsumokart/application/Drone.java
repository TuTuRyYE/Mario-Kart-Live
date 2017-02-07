package fr.enseeiht.superjumpingsumokart.application;

import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by Vivian on 27/01/2017.
 * Class representing a drone
 */

public class Drone {
    private String name;
    private Vector3D currentPosition;
    private Item currentItem;
    private Vector3D currentSpeed;
    private int currentLap;
    private ArrayList<Integer> markersSeen;

    Drone(String name, Vector3D currentPosition, Item currentItem, Vector3D speed) {
        this.name = name;
        this.currentPosition = currentPosition;
        this.currentItem = currentItem;
        this.currentSpeed = speed;
        this.currentLap = 0;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Vector3D getCurrentPosition() {

        return currentPosition;
    }

    public void setCurrentPosition(Vector3D currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Item  getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {

        this.currentItem = currentItem;
    }

    public Vector3D getSpeed() {

        return currentSpeed;
    }

    public void setSpeed(Vector3D speed) {

        this.currentSpeed = speed;
    }

    public int getCurrentLap() {
        return currentLap;
    }

    public void setCurrentLap(int currentLap) {
        this.currentLap = currentLap;
    }

    public ArrayList<Integer> getMarkersSeen() {
        return markersSeen;
    }

    public void setMarkersSeen(ArrayList<Integer> markersSeen) {
        this.markersSeen = markersSeen;
    }
}
