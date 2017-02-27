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
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;
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
            //TODO
            trackInitialised = true;
        }
        currentItems = null;
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
        int checkPointToCheck = 1;
        Circuit.initInstance(laps,checkPointToCheck);
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
        DetectionTask.symbols itemMarker;
        switch (name) {
            case "redshell":
                Log.d(GAME_TAG,"You've been hit by a shell!");
                RedShell redShell = new RedShell();
                currentItems.add(redShell);
                redShell.applyEffect(guiGame.getController());
                break;
            case "banana":
                Log.d(GAME_TAG,"A banana has been put on the circuit by second player");
                itemMarker = DetectionTask.symbols.valueOf(msgSplit[1]);
                Banana banana = new Banana();
                currentItems.add(banana);
                Circuit.getInstance().addObject(itemMarker,banana);
                break;
            case "box":
                Log.d(GAME_TAG,"A box has been put on the circuit by second player");
                itemMarker = DetectionTask.symbols.valueOf(msgSplit[1]);
                Box box = new Box();
                currentItems.add(box);
                Circuit.getInstance().addObject(itemMarker,box);
                break;
        }
    }

    @Override
    public void onSecondPlayerTouchedItem(String msg){
        String[] msgSplit = msg.split("/");
        boolean found = false;
        DetectionTask.symbols itemMarker;
        itemMarker = DetectionTask.symbols.valueOf(msgSplit[1]);
        int ind = 0;
        Item currentItem;
        while (!found && ind <= currentItems.size()) {
            currentItem = currentItems.get(ind);
            if (currentItem.getSymbol().name().equals(itemMarker.name())) {
                found = true;
                currentItems.remove(ind);
            } else {
                ind++;
            }
        }
    }


    @Override
    public void onItemUsed(Item item) {
        Log.d(GAME_TAG,"Information received from Item : item has been put on the circuit");
        addItem(item);
        for(GameListener listener  : this.GAME_LISTENERS) {
            listener.onPlayerUseItem(item,drone.getLastMarkerSeen());
        }
    }

    @Override
    public void onSymbolTouched(DetectionTask.Symbol symbol) {
        Item item = Circuit.getInstance().getObjects().get(symbol);
        if (item != null) {
            for (GameListener listener : this.GAME_LISTENERS) {
                listener.onItemTouched(item);
            }
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