package fr.enseeiht.superjumpingsumokart.application;

import org.artoolkit.ar.base.ARToolKit;

import java.util.HashMap;

/**
 * Created by Vivian on 27/01/2017.
 */

public class Circuit {
    private String name;
    private Vector3D startPoint;
    private Vector3D[] endPoints = new Vector3D[2];
    private HashMap<Integer,Vector3D> markersID;
    private int lapsNumbers;

    private final static double STARTPOINTX = 2.0;
    private final static double STARTPOINTY = 0;
    private final static double STARTPOINTZ = 0;

    private final static double ENDPOINT1X = 10;
    private final static double ENDPOINT1Y = 10;
    private final static double ENDPOINT1Z = 0;

    private final static double ENDPOINT2X = 20;
    private final static double ENDPOINT2Y = 10;
    private final static double ENDPOINT2Z = 0;


    public Circuit(int laps) {

        this.lapsNumbers = laps;
        HashMap<Integer, Vector3D> markers = new HashMap<>();
        markers.put(0, startPoint); // /!\ IDs can be changed
        markers.put(-1, endPoints[0]);
        markers.put(-2, endPoints[1]);
        this.markersID = markers;

        Vector3D startPosition = new Vector3D(STARTPOINTX, STARTPOINTY, STARTPOINTZ);
        Vector3D endPoint1 = new Vector3D(ENDPOINT1X, ENDPOINT1Y, ENDPOINT1Z);
        Vector3D endPoint2 = new Vector3D(ENDPOINT2X, ENDPOINT2Y, ENDPOINT2Z);
        this.startPoint = startPosition;
        this.endPoints = new Vector3D[]{endPoint1, endPoint2};
    }

    public Vector3D getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Vector3D startPoint) {
        this.startPoint = startPoint;
    }

    public Vector3D[] getEndPoints() {
        return endPoints;
    }

    public void setEndPoints(Vector3D[] endPoints) {
        this.endPoints = endPoints;
    }

    public int getLaps() {
        return lapsNumbers;
    }

    public void setLaps(int laps) {
        this.lapsNumbers = laps;
    }

    public void addMarker(int markerID, Vector3D position) {
        this.markersID.put(markerID, position);
    }

    public void removeMarker(int markerID){
        this.markersID.remove(markerID);
    }

    public HashMap<Integer, Vector3D> getMarkersID() {
        return markersID;
    }

    public void setMarkersID(HashMap<Integer, Vector3D> markersID) {
        this.markersID = markersID;
    }
}
