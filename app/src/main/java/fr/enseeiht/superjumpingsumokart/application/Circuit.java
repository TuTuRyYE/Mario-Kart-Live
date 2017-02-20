package fr.enseeiht.superjumpingsumokart.application;
import android.util.Log;
import java.util.HashMap;

import fr.enseeiht.superjumpingsumokart.application.network.BluetoothCommunication;

/**
 * Created by Vivian on 27/01/2017.
 */
public class Circuit {
    /**
     * The logging tag. Useful for debugging.
     */
    private final static String CIRCUIT_TAG = "CIRCUIT";
    /**
     * The name of the circuit
     */
    private String name;
    /**
     * The coordinates of the end point of the start line. The other point of the line is (0,0,0)
     */
    private Vector3D startPoint;
    /**
     * A table of the coordinates of the two points of the end line. The first point must be the most left and if it is not possible the highest up possible
     */
    private Vector3D[] endPoints = new Vector3D[2];
    /**
     * The markers present on the circuit. Each marker is defined by one unique id and its position
     */
    private HashMap<Integer,Vector3D> markersID;
    /**
     * The number of laps a player has to do to complete the circuit
     */
    private int lapsNumber;
    /**
     * The coordinates of the startline and endline (for the moment they're written manually here)
     */
    private final static double STARTPOINTX = 2.0;
    /**
     * The coordinates of the startline and endline (for the moment they're written manually here)
     */
    private final static double STARTPOINTY = 0;
    /**
     * The coordinates of the startline and endline (for the moment they're written manually here)
     */
    private final static double STARTPOINTZ = 0;
    /**
     * The coordinates of the startline and endline (for the moment they're written manually here)
     */
    private final static double ENDPOINT1X = 10;
    /**
     * The coordinates of the startline and endline (for the moment they're written manually here)
     */
    private final static double ENDPOINT1Y = 10;
    /**
     * The coordinates of the startline and endline (for the moment they're written manually here)
     */
    private final static double ENDPOINT1Z = 0;
    /**
     * The coordinates of the startline and endline (for the moment they're written manually here)
     */
    private final static double ENDPOINT2X = 20;
    /**
     * The coordinates of the startline and endline (for the moment they're written manually here)
     */
    private final static double ENDPOINT2Y = 10;
    /**
     * The coordinates of the startline and endline (for the moment they're written manually here)
     */
    private final static double ENDPOINT2Z = 0;

    private static Circuit circuitInstance;

    /**
     * Default Constructor of the class {@link DroneController} .
     * It binds the {@link}
     * By default, there are the markers of the start (id 0) and end lines (id -1 and -2)
     * @param laps number of laps a player have to do to complete the circuit
     */
    public Circuit(int laps) {
        // Number of laps
        this.lapsNumber = laps;
        // startline and endline
        Vector3D startPosition = new Vector3D(STARTPOINTX, STARTPOINTY, STARTPOINTZ);
        Vector3D endPoint1 = new Vector3D(ENDPOINT1X, ENDPOINT1Y, ENDPOINT1Z);
        Vector3D endPoint2 = new Vector3D(ENDPOINT2X, ENDPOINT2Y, ENDPOINT2Z);
        this.startPoint = startPosition;
        this.endPoints = new Vector3D[]{endPoint1, endPoint2};
        // default markers
        HashMap<Integer, Vector3D> markers = new HashMap<>();
        markers.put(0, startPoint); // /!\ IDs can be changed (-> change in the gdoc markers)
        markers.put(-1, endPoints[0]);
        markers.put(-2, endPoints[1]);
        this.markersID = markers;
        Log.d(CIRCUIT_TAG, "startline and endline markers added");
    }

    public static void initInstance(int laps) {
        if (circuitInstance == null) {
            circuitInstance = new Circuit(laps);
        }
    }

    public static Circuit getInstance() {
        return circuitInstance;
    }

    /**
     * Get the starting Point .
     * @return the startpoint.
     */
    public Vector3D getStartPoint() {
        return startPoint;
    }
    /**
     * Set the starting Point.
     * @param startPoint the new startPoint.
     */
    public void setStartPoint(Vector3D startPoint) {
        this.startPoint = startPoint;
    }
    /**
     * Get the two  end Points.
     * @return array of 2 end Point.
     */
    public Vector3D[] getEndPoints() {
        return endPoints;
    }
    /**
     * Set the array of end  Point.
     * @param endPoints the new endPoint.
     */
    public void setEndPoints(Vector3D[] endPoints) {
        this.endPoints = endPoints;
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
     * Add markers to the list of markers present on the circuit
     * @param markerID the id of the marker
     * @param position the position of the marker in metre from the other startPoint (0,0,0)
     */
    public void addMarker(int markerID, Vector3D position) {
        this.markersID.put(markerID, position);
    }
    /**
     * Remove a marker from the list of markers present on the circuit
     * @param markerID the id of the marker to be removed
     */
    public void removeMarker(int markerID){
        this.markersID.remove(markerID);
    }
    /**
     * Get the list of markers.
     * @return the list of markers.
     */
    public HashMap<Integer, Vector3D> getMarkersID() {
        return markersID;
    }
    /**
     * Set the list of markers.
     * @param markersID the new list of markers.
     */
    public void setMarkersID(HashMap<Integer, Vector3D> markersID) {
        this.markersID = markersID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}