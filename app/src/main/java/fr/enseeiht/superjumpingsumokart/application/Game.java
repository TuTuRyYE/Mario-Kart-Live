package fr.enseeiht.superjumpingsumokart.application;

import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by Vivian on 27/01/2017.
 */

public class Game {
    private Drone[] drones;
    private Circuit circuit;
    private ArrayList<Item> currentItems;

    public Game(Drone[] drones, Circuit circuit, ArrayList<Item> currentItems) {
        this.drones = drones;
        this.circuit = circuit;
        this.currentItems = currentItems;
    }

    public Drone[] getDrones() {
        return drones;
    }

    public void setDrones(Drone[] drones) {
        this.drones = drones;
    }

    public Circuit getCircuit() {
        return circuit;
    }


    public ArrayList<Item> getCurrentItems() {
        return currentItems;
    }

    public void setCurrentItems(ArrayList<Item> currentItems) {
        this.currentItems = currentItems;
    }

    public void start() {
        //TODO
    }

    public void stop(){
        //TODO
    }

    public void createCircuit(Vector3D startPoint, Vector3D[] endPoints, int laps) { //TODO Add markers
        this.circuit = new Circuit(startPoint, endPoints, laps);
    }
}
