package fr.enseeiht.superjumpingsumokart.application;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.CyclicBarrier;

import fr.enseeiht.superjumpingsumokart.application.items.Banana;
import fr.enseeiht.superjumpingsumokart.application.items.Box;
import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.application.items.MagicBox;
import fr.enseeiht.superjumpingsumokart.application.network.CommunicationBT;
import fr.enseeiht.superjumpingsumokart.arpack.GUIGame;

/**
 *  * @author Vivian Guy, Matthieu Michel.
 * This class is used to manage the game.
 */

public class Game extends HandlerThread{
    /**
     * The logging tag. Useful for debugging.
     */
    private final static String GAME_TAG = "GAME";

    private String name;


    private Circuit circuit;

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
    private boolean isStarted;

    private boolean otherIsReady;


    public Handler handlerComBT;

    public Handler handlerGame = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            byte[] byteReceived = bundle.getByteArray("0");
            String receivedMsg = new String(byteReceived, Charset.forName("UTF-8"));
            Log.d(GAME_TAG, receivedMsg);
            String[] receivedMsgSplit = receivedMsg.split("/");
            String key = receivedMsgSplit[0];
            switch (key) {

                case "item": // the other drone used an item
                    String name = receivedMsgSplit[1];
                    switch (name) {
                        case "banana":
                            double xBanana = Double.parseDouble(receivedMsgSplit[2]);
                            double yBanana = Double.parseDouble(receivedMsgSplit[3]);
                            double zBanana = Double.parseDouble(receivedMsgSplit[4]);

                            Banana banana = new Banana();
                            banana.setPosition(new Vector3D(xBanana,yBanana,zBanana));
                            currentItems.add(banana);
                            break;
                        case "box" :
                            double xBox = Double.parseDouble(receivedMsgSplit[2]);
                            double yBox = Double.parseDouble(receivedMsgSplit[3]);
                            double zBox = Double.parseDouble(receivedMsgSplit[4]);

                            Box box = new Box();
                            box.setPosition(new Vector3D(xBox,yBox,zBox));
                            currentItems.add(box);
                            break;

                        case "magicbox" :
                            double xMagicBox = Double.parseDouble(receivedMsgSplit[2]);
                            double yMagicBox = Double.parseDouble(receivedMsgSplit[3]);
                            double zMagicBox = Double.parseDouble(receivedMsgSplit[4]);

                            Vector3D position = new Vector3D(xMagicBox,yMagicBox,zMagicBox);

                            boolean found = false;
                            int ind =0;
                            Item currentItem;
                            while( !found && ind<=currentItems.size()) {
                                currentItem = currentItems.get(ind);
                                if(currentItem.getPosition().equals(position)) {
                                    found = true;
                                    currentItems.remove(ind);
                                }
                                else {ind++;};
                            }
                            break;


                    }
                case "isReady" : // the other drone is ready to start
                    otherIsReady = true;
                    Log.d(GAME_TAG,"isReady received");

                    break;
                case "finished" : // the other drone has finished
                    String nameFinished = receivedMsgSplit[1];
                    stop(nameFinished);
                case "position" : // received the position of the other drone
                    double xMagicBox = Double.parseDouble(receivedMsgSplit[2]);
                    double yMagicBox = Double.parseDouble(receivedMsgSplit[3]);
                    double zMagicBox = Double.parseDouble(receivedMsgSplit[4]);
            }


        }
    };

    /**
     * Default constructor of the class {@link Game} (Vivian - 07/02/2017).
     * @param guiGame interface of the {@link Game}
     */
    public Game(GUIGame guiGame, String name) {
        super(name);
        this.circuit = createCircuit();
        // Add markers for boxes
        circuit.addMarker(1, new Vector3D(0,0,0)); // position to change when markers are placed
        circuit.addMarker(2, new Vector3D(0,0,0)); // position to change when markers are placed
        circuit.addMarker(3, new Vector3D(0,0,0)); // position to change when markers are placed
        circuit.addMarker(4, new Vector3D(0,0,0)); // position to change when markers are placed

        this.currentItems = setMagicBoxes();
        this.guiGame = guiGame;
        this.isStarted = false;
        this.otherIsReady = false;
        Log.d(GAME_TAG, "Game created for drone " + guiGame.getController().getDRONE().getName());

        CommunicationBT comBT = CommunicationBT.getInstance();
        Log.d(GAME_TAG,"instace recup");
        if (comBT != null) {
            comBT.setHandlerGame(handlerGame);

        }
    }

    /**
     * Generate magic boxes on the circuit (Vivian - 07/02/2017).
     * @return the {@link ArrayList} of {@link MagicBox} generate on the circuit.
     */

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
     * Get the {@link Circuit} associated to the Game (Vivian - 07/02/2017).
     * @return the {@link Circuit} of the Game.
     */
    public Circuit getCircuit() {
        return circuit;
    }
    /**
     * Set the {@link Circuit} associated to the Game (Vivian - 07/02/2017).
     * @param circuit of the {@link Game}.
     */
    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
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

    public void addItem(Item it) {
        this.currentItems.add(it);
    }

    /**
     * Check if the current status of the {@link Game} (Vivian - 07/02/2017).
     * @return true if the {@link Game} if started otherwise false.
     */
    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    /**
     * Create the {@link Circuit} (Vivian - 07/02/2017).
     * @return {@link Circuit} created.
     */
    public Circuit createCircuit () {
        int laps = 1; // Number of laps for the game
        return new Circuit(laps);
    }

    /**
     * Start the {@link Game} (Vivian - 07/02/2017).
     */
    public void start() {
        Log.d(GAME_TAG, "start function called");
        // TODO wait for every player to be ready
        if (getNumberPlayer() == 1) {
            this.isStarted =true;
        }
        else {

        }

    }

    /**
     * Stop the Game
     * @param String the name of winner drone (Vivian - 07/02/2017).
     */
    public void stop(String nameFinished){
        Log.d(GAME_TAG, "stop fonction called");
        // Stop the drone
            guiGame.getController().stopMotion();

        // Say the name of the winner

        // TODO Send to each player a message saying that the game in finished

        this.currentItems = null;
        this.circuit = null;

    }

    /**
     * Get the number of player on the {@link Game} (Vivian - 07/02/2017).
     * @return number of Player.
     */
    public int getNumberPlayer() {
        int numberOfPlayer;
        if (CommunicationBT.getInstance() != null){
            numberOfPlayer = 2;
        }
        else {
            numberOfPlayer = 1;
        }
        Log.d(GAME_TAG, "Number of players: " + numberOfPlayer);
        return numberOfPlayer;
    }

    public boolean iAmReady() {
        boolean result;
        if(this.circuit !=null && !this.isStarted()) {
            result = true;
            Log.d(GAME_TAG, "iAmReady");
        }
        else {
            result = false;
            Log.d(GAME_TAG, "not iAmReady");
        }
        return result;

    }

    public boolean raceReady() {
        boolean iAmReady= iAmReady();
        boolean result = iAmReady;
        CommunicationBT comBT = CommunicationBT.getInstance();
        if (comBT != null) {
            result = result && otherIsReady;
        }
        if (result) {
            Log.d(GAME_TAG, "raceReady");
        }
        else {
            Log.d(GAME_TAG, "not raceReady");
        }
        return result;
    }


    public void setHandlerComBT(Handler handlerComBT) {
        this.handlerComBT = handlerComBT;
    }


}
