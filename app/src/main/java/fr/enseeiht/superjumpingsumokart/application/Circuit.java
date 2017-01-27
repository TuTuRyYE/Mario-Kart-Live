package fr.enseeiht.superjumpingsumokart.application;

/**
 * Created by Vivian on 27/01/2017.
 */

public class Circuit {
    private String name;
    private Vector3D startPoint;
    private Vector3D[] endPoints = new Vector3D[2];
    // add markers
    private int laps;

    public Circuit(Vector3D startPoint, Vector3D[] endPoints, int laps) {
        this.startPoint = startPoint;
        this.endPoints = endPoints;
        this.laps = laps;
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
        return laps;
    }

    public void setLaps(int laps) {
        this.laps = laps;
    }

    // TODO addMarker and removeMarker
}
