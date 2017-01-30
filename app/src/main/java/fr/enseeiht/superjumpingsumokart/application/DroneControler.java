package fr.enseeiht.superjumpingsumokart.application;

import fr.enseeiht.superjumpingsumokart.GUIGame;

/**
 * Created by Vivian on 27/01/2017.
 */

public class DroneControler {
    private Drone drone;
    private GUIGame guiGame;

    public DroneControler(Drone drone, GUIGame guiGame) {
        this.drone = drone;
        this.guiGame = guiGame;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public GUIGame getGuiGame() {
        return guiGame;
    }

    public void setGuiGame(GUIGame guiGame) {
        this.guiGame = guiGame;
    }

    public void moveForward(){
        //TODO
    }

    public void moveBackward(){
        //TODO
    }

    public void turnLeft(){
        //TODO
    }

    public void turnRight(){
        //TODO
    }

    public void useItem(){
        //TODO
    }

    public void jump(){
        //TODO
    }

    public void receiveItem(Item item){
        //TODO
    }

    public void getFrame(){
        //TODO
    }
}
