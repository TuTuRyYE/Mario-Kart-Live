package fr.enseeiht.superjumpingsumokart.application;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import fr.enseeiht.superjumpingsumokart.application.items.Banana;
import fr.enseeiht.superjumpingsumokart.application.items.Box;
import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.application.items.MagicBox;
import fr.enseeiht.superjumpingsumokart.application.items.RedShell;
import fr.enseeiht.superjumpingsumokart.application.network.BluetoothCommunication;
import fr.enseeiht.superjumpingsumokart.application.network.BluetoothCommunicationListener;
import fr.enseeiht.superjumpingsumokart.arpack.GUIGame;
/**
 * @author Vivian Guy, Matthieu Michel, Romain Verset.
 * This class is used to manage the game.
 */
public class Game implements BluetoothCommunicationListener, GuiGameListener{

    /**
     * The logging tag. Useful for debugging.
     */
    private final static String GAME_TAG = "GAME";
    /**

     * {@link GUIGame}, the interface of the Game.
     */
    private GUIGame guiGame;
    /**
     * {@link ArrayList} of {@link Item} present on the circuit.
     */
    private ArrayList<Item> currentItems;
    /**
     * Boolean to check if the race is started or not.
     */
    private final ArrayList<GameListener> GAME_LISTENERS = new ArrayList<>();

    private Drone drone, otherDrone;

    private boolean trackInitialised = false, videoStreamAvailable = false, droneControllerReady = false;
    private boolean ready = false, otherReady = false;
    private boolean started = false;
    private boolean finished = false;
    private BluetoothCommunication comBT;

    /**
     * Default constructor of the class {@link Game} (Vivian - 07/02/2017).
     * @param guiGame interface of the {@link Game}
     */
    public Game(GUIGame guiGame, BluetoothCommunication comBT, boolean isServer) {
        // Add markers for boxes
        if (isServer) {
            createCircuit();
            Circuit.getInstance().addMarker(1, new Vector3D(0,0,0)); // position to change when markers are placed
            Circuit.getInstance().addMarker(2, new Vector3D(0,0,0)); // position to change when markers are placed
            Circuit.getInstance().addMarker(3, new Vector3D(0,0,0)); // position to change when markers are placed
            Circuit.getInstance().addMarker(4, new Vector3D(0,0,0)); // position to change when markers are
            //TODO
            trackInitialised = true;
        }
        currentItems = setMagicBoxes();
        this.guiGame = guiGame;
        registerGameListener(guiGame);
        this.started = false;
        this.comBT = comBT;
        if (comBT != null) {
            this.comBT = comBT;
            comBT.setGame(this);
            registerGameListener(comBT);
            Log.d(GAME_TAG, "2 players game created.");
            comBT.sendCircuit();
        } else {
            otherReady = true;
            Log.d(GAME_TAG, "1 player game created.");
        }
        checkReadyAndStartRace();
    }

    private void checkReadyAndStartRace() {
        Log.d(GAME_TAG, "checkReadyAndStartRace called");
        if (trackInitialised) {
            Log.d(GAME_TAG, "trackInitialised");
        }
        if (droneControllerReady) {
            Log.d(GAME_TAG, "droneControllerReady");
        }
        if (videoStreamAvailable) {
            Log.d(GAME_TAG, "videoStreamAvailable");
        }
        if (trackInitialised && droneControllerReady && videoStreamAvailable) {
            ready =  true;
            for (GameListener gl : GAME_LISTENERS) {
                gl.onPlayerReady();
            }
        }
        if (ready && otherReady) {
            Log.d(GAME_TAG, "player and other player are ready to start the race");
            started = true;
            for (GameListener gl : GAME_LISTENERS) {
                gl.onStartRace();
            }
        }
    }

    /**
     * Generate magic boxes on the circuit (Vivian - 07/02/2017).
     * @return the {@link ArrayList} of {@link MagicBox} generate on the circuit.
     */
    private ArrayList<Item> setMagicBoxes() {
        ArrayList<Item> result = new ArrayList<>();
        HashMap<Integer, Vector3D> markersID = Circuit.getInstance().getMarkersID();
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
    /**
     * Get the {@link GUIGame} associated to the Game (Vivian - 07/02/2017).
     * @return the {@link GUIGame} of the {@link Game}.
     */
    public GUIGame getGuiGame() {
        return guiGame;
    }

    /**
     * Set the {@link GUIGame} associated to the Game (Vivian - 07/02/2017).
     * @param guiGame of the {@link Game}.
     */
    public void setGuiGame(GUIGame guiGame) {
        this.guiGame = guiGame;
    }
    /**
     * Get currentItems {@link ArrayList} present on the circuit (Vivian - 07/02/2017).
     * @return currentItems {@link ArrayList}.
     */
    public ArrayList<Item> getCurrentItems() {
        return currentItems;
    }

    /**
     * Set Items present on {@link Circuit} (Vivian - 07/02/2017).
     * @param currentItems present
     */
    public void setCurrentItems(ArrayList<Item> currentItems) {
        this.currentItems = currentItems;
    }

    /**
     * Add an {@link Item} to the list of {@link Item} present on the race (Matthieu Michel - 15/02/2017).
     * @param item added by the player.
     */
    private void addItem(Item item){
        this.currentItems.add(item);
    }
    /**
     * Remove an {@link Item} to the list of {@link Item} present on the race when an {@link Item} as been touched by the {@link Drone}(Matthieu Michel - 15/02/2017).
     * @param item added by the player.
     */
    private void removeItem(Item item){
        this.currentItems.remove(item);
    }
    /**
     * Check the current status of the {@link Game} (Vivian - 07/02/2017).
     * @return true if the {@link Game} if started otherwise false.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Create the {@link Circuit} (Vivian - 07/02/2017).
     */
    private void createCircuit () {
        int laps = 1; // Number of laps for the game
        Circuit.initInstance(laps);
    }
    /**
     * Get the number of player on the {@link Game} (Vivian - 07/02/2017).
     * @return number of Player.
     */
    public int getNumberPlayer() {
        int numberOfPlayer;
        if (comBT != null){
            numberOfPlayer = 2;
        }
        else {
            numberOfPlayer = 1;
        }
        Log.d(GAME_TAG, "Number of players: " + numberOfPlayer);
        return numberOfPlayer;
    }


    public boolean isReady() {
        return (Circuit.getInstance() !=null && !started);
    }

    public int getLapsNumber() {
        return Circuit.getInstance().getLaps();
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public boolean isFinished() {
        return finished;
    }

    public void registerGameListener(GameListener gameListener) {
        GAME_LISTENERS.add(gameListener);
    }

    public void unregisterGameListener(GameListener gameListener) {
        GAME_LISTENERS.remove(gameListener);
    }

    @Override
    public void onSecondPlayerReady() {
        this.otherReady = true;
        otherDrone = new Drone("BAD_JUMPY");
        checkReadyAndStartRace();
    }

    @Override
    public void onSecondStartRace() {
        guiGame.onStartRace();
    }

    @Override
    public void onSecondPlayerLapFinished() {
        otherDrone.setCurrentLap(otherDrone.getCurrentLap() + 1);
    }

    @Override
    public void onSecondPlayerUsesItem(String msg) {
        String[] msgSplit = msg.split("/");
        String name = msgSplit[0];
        switch (name) {
            case "redshell":
                Log.d(GAME_TAG,"You've been hit by a shell!");
                double xRedShell = Double.parseDouble(msgSplit[1]);
                double yRedShell = Double.parseDouble(msgSplit[2]);
                double zRedShell = Double.parseDouble(msgSplit[3]);
                RedShell redShell = new RedShell();
                redShell.setPosition(new Vector3D(xRedShell, yRedShell, zRedShell));
                currentItems.add(redShell);
                redShell.applyEffect(guiGame.getController());
                break;
            case "banana":
                Log.d(GAME_TAG,"A banana has been put on the circuit by second player");
                double xBanana = Double.parseDouble(msgSplit[1]);
                double yBanana = Double.parseDouble(msgSplit[2]);
                double zBanana = Double.parseDouble(msgSplit[3]);

                Banana banana = new Banana();

                banana.setPosition(new Vector3D(xBanana, yBanana, zBanana));
                currentItems.add(banana);
                break;
            case "box":
                Log.d(GAME_TAG,"A box has been put on the circuit by second player");
                double xBox = Double.parseDouble(msgSplit[1]);
                double yBox = Double.parseDouble(msgSplit[2]);
                double zBox = Double.parseDouble(msgSplit[3]);
                Box box = new Box();
                box.setPosition(new Vector3D(xBox, yBox, zBox));
                currentItems.add(box);
                break;
            case "magicbox":
                Log.d(GAME_TAG,"A magicbox has been taken on the circuit by second player");
                double xMagicBox = Double.parseDouble(msgSplit[1]);
                double yMagicBox = Double.parseDouble(msgSplit[2]);
                double zMagicBox = Double.parseDouble(msgSplit[3]);
                Vector3D position = new Vector3D(xMagicBox, yMagicBox, zMagicBox);
                boolean found = false;
                int ind = 0;
                Item currentItem;
                while (!found && ind <= currentItems.size()) {
                    currentItem = currentItems.get(ind);
                    if (currentItem.getPosition().equals(position)) {
                        found = true;
                        currentItems.remove(ind);
                    } else {
                        ind++;
                    }
                }
                break;
        }
    }

    @Override
    public void onSecondPlayerTouchedItem(String msg){
        String[] msgSplit = msg.split("/");
        double x = Double.parseDouble(msgSplit[1]);
        double y = Double.parseDouble(msgSplit[2]);
        double z = Double.parseDouble(msgSplit[3]);
        Vector3D position = new Vector3D(x, y, z);
        boolean found = false;
        int ind = 0;
        Item currentItem;
        while (!found && ind <= currentItems.size()) {
            currentItem = currentItems.get(ind);
            if (currentItem.getPosition().equals(position)) {
                found = true;
                currentItems.remove(ind);
            } else {
                ind++;
            }
        }
    }

    @Override
    public void onSecondPlayerUpdatedPosition(String msg) {
        String[] msgSplit = msg.split("/");
        double x = Double.parseDouble(msgSplit[0]);
        double y = Double.parseDouble(msgSplit[1]);
        double z = Double.parseDouble(msgSplit[2]);
        Vector3D position = new Vector3D(x, y, z);
        otherDrone.setCurrentPosition(position);
    }

    @Override
    public void onPositionUpdated(Vector3D position) {
        drone.setCurrentPosition(position);
    }

    @Override
    public void onItemUsed(Item item) {
        Log.d(GAME_TAG,"Information received from Item : item has been put on the circuit");
        addItem(item);
        for(GameListener listener  : this.GAME_LISTENERS) {
            listener.onPlayerUseItem(item,drone.getLastMarkerSeen());
            Log.d(GAME_TAG,"transmitting the information to the listener");
        }
    }

    @Override
    public void onItemTouched(Item item) {
        Log.d(GAME_TAG,"Information received from Item : item has been touched");
        removeItem(item);
        for(GameListener listener  : this.GAME_LISTENERS) {
            listener.onItemTouched(item);
            Log.d(GAME_TAG,"transmitting the information to the listener");
        }

    }
    @Override
    public void onSecondPlayerFinished() {
        guiGame.notifyDefeat();
        for (GameListener gl : GAME_LISTENERS) {
            unregisterGameListener(gl);
        }
        finished = true;
    }

    @Override
    public void onSecondPlayerGaveUp() {
        guiGame.notifyVictory();
        for (GameListener gl : GAME_LISTENERS) {
            unregisterGameListener(gl);
        }
        finished = true;
    }

    @Override
    public void onPlayerFinished() {
        if (comBT != null) {
            for (GameListener gl : GAME_LISTENERS) {
                gl.onPlayerFinished();
            }
        }
        for (GameListener gl : GAME_LISTENERS) {
            unregisterGameListener(gl);
        }
        finished = true;
    }

    @Override
    public void onPlayerGaveUp() {
        if (comBT != null) {
            for (GameListener gl : GAME_LISTENERS) {
                gl.onPlayerGaveUp();
            }
        }
        for (GameListener gl : GAME_LISTENERS) {
            unregisterGameListener(gl);
        }
        finished = true;
    }

    @Override
    public void onCircuitReceived() {
        trackInitialised = true;
        checkReadyAndStartRace();
    }

    @Override
    public void onBluetoothCommunicationClosed() {

    }

    @Override
    public void onDroneControllerReady() {
        droneControllerReady = true;
        checkReadyAndStartRace();
    }

    @Override
    public void onVideoStreamAvailable() {
        videoStreamAvailable = true;
        checkReadyAndStartRace();
    }
}