package fr.enseeiht.superjumpingsumokart.application.circuit;

import java.util.HashMap;

import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * @author Vivian Guy
 * Modelises a circuit in Super Jumping Sumo Kart.
 * Borders of the track are materialised by ARToolkit markers.
 * Circuit is a Singleton.
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
     * The markers present on the circuit. Each marker is defined by its order in the circuit and its {@link fr.enseeiht.superjumpingsumokart.arpack.DetectionTask.Symbol}.
     */
    private HashMap<Integer ,DetectionTask.Symbol> markers;


    /**
     * The number of laps a player has to do to complete the circuit.
     */
    private int lapsNumber;

    /**
     * The number of checkPoint to check to complete a circuit's lap.
     */
    private int checkPointToCheck;


    /**
     * The markers containing an item on the circuit.
     */
    private HashMap<DetectionTask.Symbol,Item> objects;

    /**
     * Instance of the circuit.
     */
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
     * Initialises the singleton instance of {@link Circuit}.
     * @param laps The number of laps for the circuit.
     * @param checkPointToCheck The number of check points to complete a lap.
     */
    public static void initInstance(int laps, int checkPointToCheck) {
        if (circuitInstance == null) {
            circuitInstance = new Circuit(laps, checkPointToCheck);
        }
    }

    /**
     * Add markers to the list of markers present on the circuit.
     * @param symbol the symbol of the marker added.
     */
    public void addMarker(DetectionTask.Symbol symbol) {
        // Markers are ordered thanks to the key
         this.markers.put(this.markers.size() + 1, symbol);
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
     * Remove an object from the list of objects present on the circuit.
     * @param symbol the symbol of the marker associated to the object deleted.
     */
    public void removeObject(DetectionTask.Symbol symbol){ this.objects.remove(symbol); }


    //   GETTER AND SETTER

    /**
     * Get the number of check point to check to complete the circuit.
     * @return the number of check point.
     */
    public int getCheckpointToCheck() {
        return checkPointToCheck;
    }

    /**
     * Get the instance of the circuit.
     * @return The singleton instance of the circuit.
     */
    public static Circuit getInstance() {
        return circuitInstance;
    }

    /**
     * Set the instance of the circuit
     * @param circuitInstance the instance of the circuit
     */
    public static void setCircuitInstance(Circuit circuitInstance) {
        Circuit.circuitInstance = circuitInstance;
    }

    /**
     * Get the number of laps.
     * @return the number of laps to complete the circuit.
     */
    public int getLaps() {
        return lapsNumber;
    }

    /**
     * Get the list of markers.
     * @return the list of markers.
     */
    public HashMap<Integer, DetectionTask.Symbol> getMarkers() {
        return markers;
    }

    /**
     * Get the name of the circuit.
     * @return the name of the circuit.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the circuit.
     * @param name the name of the circuit.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the number of check point to complete a circuit's lap
     * @param checkPointToCheck
     */
    public void setCheckPointToCheck(int checkPointToCheck) {
        this.checkPointToCheck = checkPointToCheck;
    }

    /**
     * Get the list of objects present on the circuit
     * @return the list of objects present on the circuit
     */
    public HashMap<DetectionTask.Symbol, Item> getObjects() {
        return objects;
    }
}