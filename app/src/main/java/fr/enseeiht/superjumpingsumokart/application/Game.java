package fr.enseeiht.superjumpingsumokart.application;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;

import fr.enseeiht.superjumpingsumokart.application.items.Banana;
import fr.enseeiht.superjumpingsumokart.application.items.Box;
import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.application.items.MagicBox;
import fr.enseeiht.superjumpingsumokart.arpack.GUIGame;

/**
 * Created by Vivian on 27/01/2017.
 */

public class Game {
    /**
     * The logging tag. Useful for debugging.
     */
    private final static String GAME_TAG = "GAME";

    private Circuit circuit;
    private GUIGame guiGame;
    // InputStream, OutputStream
    private ArrayList<Item> currentItems;
    private boolean isStarted;

    public Game(GUIGame guiGame) {
        this.circuit = createCircuit();


        // Add markers for boxes
        circuit.addMarker(1, new Vector3D(0,0,0)); // position to change when markers are placed
        circuit.addMarker(2, new Vector3D(0,0,0)); // position to change when markers are placed
        circuit.addMarker(3, new Vector3D(0,0,0)); // position to change when markers are placed
        circuit.addMarker(4, new Vector3D(0,0,0)); // position to change when markers are placed

        this.currentItems = setMagicBoxes();
        this.guiGame = guiGame;
        this.isStarted = false;
        Log.d(GAME_TAG, "Game created for drone " + guiGame.getController().getDRONE().getName());
    }

    public ArrayList<Item> setMagicBoxes() {
        ArrayList<Item> result = new ArrayList<>();

        HashMap<Integer, Vector3D> markersID = this.circuit.getMarkersID();
        Vector3D position1 = markersID.get(1);
        Vector3D position2 = markersID.get(2);
        Vector3D position3 = markersID.get(3);
        Vector3D position4 = markersID.get(4);


        int numberOfBoxesPerLine = 2;
        double x,y, distanceX, distanceY;

        // For the first line of boxes
            distanceX = Math.abs(position1.getX() - position2.getX());
            distanceY = Math.abs(position1.getY() - position2.getY());

            for(int i=1; i<=numberOfBoxesPerLine; i++) {
                x =  position2.getX() - i*distanceX/numberOfBoxesPerLine;
                y = position2.getY() + i*distanceY/numberOfBoxesPerLine;
                result.add(new MagicBox(new Vector3D(x, y, 0)));
            }

        // For the second line of boxes
            distanceX = Math.abs(position3.getX() - position4.getX());
            distanceY = Math.abs(position3.getY() - position4.getY());

            for(int i=1; i<=numberOfBoxesPerLine; i++) {
                x =  position4.getX() - i*distanceX/numberOfBoxesPerLine;
                y = position4.getY() + i*distanceY/numberOfBoxesPerLine;
                result.add(new MagicBox(new Vector3D(x, y, 0)));
            }
        Log.d(GAME_TAG, numberOfBoxesPerLine + "lines of boxes set");
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
        Log.d(GAME_TAG, "start fonction called");
        // TODO wait for every player to be ready
        this.isStarted = true;
    }

    public void stop(DroneController controller){
        Log.d(GAME_TAG, "stop fonction called");
        // Stop the drone
            controller.stopMotion();

        // TODO Send to each player a message saying that the game in finished

        this.currentItems = null;
        this.circuit = null;

    }

    public int getNumberPlayer() {
        // TODO Return the number of players
        return 1;
    }
}
