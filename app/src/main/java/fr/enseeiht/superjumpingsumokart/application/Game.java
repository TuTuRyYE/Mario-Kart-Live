package fr.enseeiht.superjumpingsumokart.application;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

import fr.enseeiht.superjumpingsumokart.application.items.Banana;
import fr.enseeiht.superjumpingsumokart.application.items.Box;
import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.arpack.GUIGame;

/**
 * Created by Vivian on 27/01/2017.
 */

public class Game extends Thread{
    private Circuit circuit;
    private GUIGame guiGame;
    // InputStream, OutputStream
    private ArrayList<Item> currentItems;
    private boolean isStarted;


    public Game(GUIGame guiGame) {
        this.circuit = createCircuit();
        this.currentItems = setRandomItems(1); // For the moment, there are no items at the beginning
        this.guiGame = guiGame;
        this.isStarted = false;
    }

    public ArrayList<Item> setRandomItems(int numberOfItems) {
        ArrayList<Item> result = new ArrayList<Item>();
        for (int i=0; i<numberOfItems; i++) {
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
            item.setPosition(new Vector3D(0,0,0)); // TODO Set position randomly on the circuit
            result.add(item);
        }
        return result;
    }

    public GUIGame getGuiGame() {
        return guiGame;
    }

    public void setGuiGame(GUIGame guiGame) {
        this.guiGame = guiGame;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    public ArrayList<Item> getCurrentItems() {
        return currentItems;
    }

    public void setCurrentItems(ArrayList<Item> currentItems) {
        this.currentItems = currentItems;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public Circuit createCircuit () {
        int laps = 1; // Number of laps for the game
        return new Circuit(laps);
    }

    public void start() {
        // wait for every player to be ready
        this.isStarted = true;
    }

    public void stop(Drone drone){
        // Send to each player a message saying that the game in finished

        this.currentItems = null;
        this.circuit = null;

    }

    public int getNumberPlayer() {
        // Return the number of players
        return 1;
    }
}
