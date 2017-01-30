package fr.enseeiht.superjumpingsumokart.application;

/**
 * Created by Vivian on 27/01/2017.
 */

public class Item {
    private String name;
    // 3D model to add
    private Vector3D position;

    public Item(String name, Vector3D position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void action(Drone sender, Drone receiver) {
        // TODO
    }

    public boolean isInContact (Drone player) {
        return player.getCurrentPosition().equals(this.position);
    }

    public void applyEffect(Drone sender,Drone receiver) {
        // TODO
    }
}
