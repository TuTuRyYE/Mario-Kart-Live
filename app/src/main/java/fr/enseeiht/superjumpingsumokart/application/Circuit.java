package fr.enseeiht.superjumpingsumokart.application;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.application.network.BluetoothCommunication;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * Modelises a circuit in Super Jumping Sumo Kart.
 * Borders of the track are materialised by ARToolkit markers. The first point of the start line is
 * always (0, 0, 0).
 * Created by Vivian on 27/01/2017.
 */
public class Circuit {
    /**
     * The logging tag. Useful for debugging.
     */
    private final static String CIRCUIT_TAG = "CIRCUIT";
    /**
     * The name of the circuit.
     */
    private String name;

    /**
     * The markers present on the circuit. Each marker is defined by its order in the circuit and its symbol.
     */
    private HashMap<Integer ,DetectionTask.Symbol> markers;


    /**
     * The number of laps a player has to do to complete the circuit.
     */
    private int lapsNumber;

    public int getCheckPointToCheck() {
        return checkPointToCheck;
    }

    /**
     * The markers containing an item on the circuit.
     */
    private HashMap<DetectionTask.Symbol,Item> objects;

    /**
     * The number of checkPoint to check to complete a circuit's lap.
     */
    private int checkPointToCheck;

    public HashMap<DetectionTask.Symbol, Item> getObjects() {
        return objects;
    }

    private static Circuit circuitInstance;

    /**
     * Constructor for singleton circuit.
     * @param laps .
     * @param checkPointToCheck .
     */
   private Circuit(int laps, int checkPointToCheck) {
        this.lapsNumber = laps;
        this.checkPointToCheck = checkPointToCheck;
        this.markers = new HashMap<>();
       this.objects = new HashMap<>();
    }

    /**
     * Set the singleton instance of {@link Circuit}.
     * @param circuit
     */
    public static void setInstance(Circuit circuit) {
        circuitInstance = circuit;
    }

    /**
     * Initialises the singleton instance of {@link Circuit}.
     * @param laps The number of laps for the circuit.
     */
    public static void initInstance(int laps, int checkPointToCheck) {
        if (circuitInstance == null) {
            circuitInstance = new Circuit(laps, checkPointToCheck);
        }
    }

    /**
     *
     * @return The singleton instance of {@link Circuit}.
     */
    public static Circuit getInstance() {
        return circuitInstance;
    }

    /**
     * Get the number of laps.
     * @return the number of laps it takes to complete the circuit.
     */
    public int getLaps() {
        return lapsNumber;
    }
    /**
     * Set the lap Number.
     * @param laps the new number of laps to complete the circuit.
     */
    public void setLaps(int laps) {
        this.lapsNumber = laps;
    }

    /**
     * Add markers to the list of markers present on the circuit.
     * @param symbol the symbol of the marker.
     */
    public void addMarker(DetectionTask.Symbol symbol) {
        this.markers.put(this.markers.size() + 1, symbol);
    }

    public void removeMarker(int position) {
        DetectionTask.Symbol symbol;
        // remove the marker
            this.markers.remove(position);
        // Adapt the position of others markers
            for (int i=position; i<markers.size(); i++) {
                symbol = this.markers.get(i);
                this.markers.remove(i);
                this.markers.put(i ,symbol);
            }

    }

    /**
     * Add an object to the list of objects present on the circuit.
     * @param symbol the symbol of the marker.
     * @param item the object.
     */
    public void addObject(DetectionTask.Symbol symbol,Item item){
        this.objects.put(symbol,item);
    }

    /**
     * Remove an object of the circuit.
     * @param symbol the symbol of the marker associated to the object.
     */
    public void removeObject(DetectionTask.Symbol symbol){ this.objects.remove(symbol); }
    /**
     * Get the list of markers.
     * @return the list of markers.
     */
    public HashMap<Integer, DetectionTask.Symbol> getMarkers() {
        return markers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCheckPointToCheck(int checkPointToCheck) {
        this.checkPointToCheck = checkPointToCheck;
    }
}