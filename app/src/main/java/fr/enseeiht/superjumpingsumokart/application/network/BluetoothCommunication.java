package fr.enseeiht.superjumpingsumokart.application.network;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import fr.enseeiht.superjumpingsumokart.application.Circuit;
import fr.enseeiht.superjumpingsumokart.application.Game;
import fr.enseeiht.superjumpingsumokart.application.GameListener;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;
import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Bluetooth communication channel used in the application so that to phones can send messages to
 * each other.
 * It manages the bluetooth communication between two paired devices and run in a separate thread
 * in order to avoid UI freezes.
 * Created by Lucas on 07/02/2017.
 */
public final class BluetoothCommunication extends Thread implements GameListener {

    /**
     * Logging tag. Useful for debugging.
     */
    private final static String BLUETOOTH_COMMUNICATION_TAG = "BluetoothCommunication";
    /**
     * The singleton instance of {@link BluetoothCommunication}.
     */
    private static BluetoothCommunication btComInstance = null;
    /**
     * The socket used for communications.
     */
    private final BluetoothSocket BT_SOCKET;
    /**
     * List of instances listening to the singleton of {@link BluetoothCommunication}.
     */
    private final ArrayList<BluetoothCommunicationListener> COMMUNICATION_BT_LISTENERS = new ArrayList<>();
    /**
     * The {@link InputStream} on which messages are received.
     */
    private InputStream btInputStream;

    /**
     * The {@link OutputStream} on which messages are sent.
     */
    private OutputStream btOutputStream;

    /**
     * A reference to a {@link Game} instance.
     */
    private Game game;

    /**
     * Default constructor of {@link BluetoothCommunication}.
     *
     * @param socket Socket used for communications.
     */
    private BluetoothCommunication(BluetoothSocket socket) {
        BT_SOCKET = socket;
        // Initialisation of the streams
        try {
            btInputStream = socket.getInputStream();
            btOutputStream = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(BLUETOOTH_COMMUNICATION_TAG, "IOException while opening streams : " + e.getMessage());
        }
    }

    /**
     * Initialises the singleton instance of {@link BluetoothCommunication}.
     *
     * @param socket Socket used for communications.
     */
    static void initInstance(BluetoothSocket socket) {
        if (btComInstance == null) {
            btComInstance = new BluetoothCommunication(socket);
        }
    }

    /**
     * @return The singleton instance of {@link BluetoothCommunication}.
     */
    public static BluetoothCommunication getInstance() {
        return btComInstance;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
        // Permanent listening on the input stream.
        while (true) {
            try {
                // Reading on the input stream.
                bytes = btInputStream.read(buffer);

                // Copies it.
                byte[] data = new byte[bytes];
                System.arraycopy(buffer, 0, data, 0, bytes);
                String receivedMsg = new String(data, Charset.forName("UTF-8"));

                // Dispatches the message according to its key.
                dispatchMessage(receivedMsg);

            } catch (IOException e) {
                Log.d(BLUETOOTH_COMMUNICATION_TAG, "IOException : + " + e.getMessage());
                cancel();
                break;
            }
        }
    }

    /**
     * Parse a message and then dispatch notifications to listeners in {@link BluetoothCommunication#COMMUNICATION_BT_LISTENERS}.
     *
     * @param receivedMsg The received message to parse.
     */
    private void dispatchMessage(String receivedMsg) {
        String[] msgSplit = receivedMsg.split("/");
        String key = msgSplit[0];
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "Message " + key + " received");
        switch (key) {
            case "isReady":
                for (BluetoothCommunicationListener listener : this.COMMUNICATION_BT_LISTENERS) {
                    listener.onSecondPlayerReady();
                }
                break;
            case "hasFinished":
                for (BluetoothCommunicationListener listener : this.COMMUNICATION_BT_LISTENERS) {
                    listener.onSecondPlayerFinished();
                }
                break;
            case "hasFinishedLap":
                for (BluetoothCommunicationListener listener : this.COMMUNICATION_BT_LISTENERS) {
                    listener.onSecondPlayerLapFinished();
                }
                break;
            case "itemUsed":
                for (BluetoothCommunicationListener listener : this.COMMUNICATION_BT_LISTENERS) {
                    String itemInfos;
                    if (msgSplit.length == 2) { // if the object hasn't a position
                        itemInfos = msgSplit[1];
                    } else { // if the object has a position
                        itemInfos = msgSplit[1] + "/" + msgSplit[2] + "/" + msgSplit[3] + "/" + msgSplit[4];
                    }
                    listener.onSecondPlayerUsesItem(itemInfos);
                }
                break;
            case "circuit":
                // We create a new circuit and assign its parameters

                Log.d(BLUETOOTH_COMMUNICATION_TAG, "Circuit string received : " + receivedMsg);

                int lapsNumber = Integer.parseInt(msgSplit[1]);
                String name = msgSplit[2];

                String[] posStart = msgSplit[3].split(":");
                String[] posEnd1 = msgSplit[4].split(":");
                String[] posEnd2 = msgSplit[5].split(":");

                Vector3D startPoint = new Vector3D(Double.parseDouble(posStart[0]),
                Double.parseDouble(posStart[1]),Double.parseDouble(posStart[2]));
                Vector3D endPoint1 = new Vector3D(Double.parseDouble(posEnd1[0]),
                Double.parseDouble(posEnd1[1]),Double.parseDouble(posEnd1[2]));
                Vector3D endPoint2 = new Vector3D(Double.parseDouble(posEnd2[0]),
                        Double.parseDouble(posEnd2[1]),Double.parseDouble(posEnd2[2]));

                HashMap<Integer, Vector3D> markers = new HashMap<>();
                int i;
                for (i=6; i<=msgSplit.length; i++){
                    String[] hashSplit = msgSplit[i].split(":");
                    int id = Integer.parseInt(hashSplit[0]);
                    double x = Double.parseDouble(hashSplit[1]);
                    double y = Double.parseDouble(hashSplit[2]);
                    double z = Double.parseDouble(hashSplit[3]);
                    markers.put(id,new Vector3D(x,y,z));
                }

                Vector3D[] endPoints = {endPoint1, endPoint2};

                Circuit c = new Circuit(name, lapsNumber, startPoint, endPoints, markers);

                // We add the circuit to the game
                Circuit.setInstance(c);
                for (BluetoothCommunicationListener bcl : COMMUNICATION_BT_LISTENERS) {
                    bcl.onCircuitReceived();
                }
                break;
                    case "hasGiveUp":
                for (BluetoothCommunicationListener listener : this.COMMUNICATION_BT_LISTENERS) {
                    listener.onSecondPlayerGaveUp();
                }
                break;
            case "hasTouchedItem":
                for (BluetoothCommunicationListener listener : this.COMMUNICATION_BT_LISTENERS) {
                    String itemInfos;
                    if (msgSplit.length == 2) { // if the object hasn't a position
                        itemInfos = msgSplit[1];
                    } else { // si the object has a position
                        itemInfos = msgSplit[1] + "/" + msgSplit[2] + "/" + msgSplit[3] + "/" + msgSplit[4];
                    }
                    listener.onSecondPlayerTouchedItem(itemInfos);
                }
                break;
            case "raceBegins":
                for (BluetoothCommunicationListener listener : this.COMMUNICATION_BT_LISTENERS) {
                    listener.onSecondStartRace();
                }
                break;
            case "updatedPosition":
                for (BluetoothCommunicationListener listener : this.COMMUNICATION_BT_LISTENERS) {
                    String newPosition = msgSplit[1] + "/" + msgSplit[2] + "/" + msgSplit[3];
                    listener.onSecondPlayerUpdatedPosition(newPosition);
                }
                break;
        }
    }

    /**
     * Write a message on the outputsteam of the socket.
     *
     * @param bytes The bytes of the message.
     */
    private void write(byte[] bytes) {
        try {
            btOutputStream.write(bytes);
        } catch (IOException e) {
            Log.d(BLUETOOTH_COMMUNICATION_TAG, "IOException : + " + e.getMessage());
            cancel();
        }
    }

    /**
     * Closes the connection.
     */
    private void cancel() {
        try {
            BT_SOCKET.close();
            unregisterGameListener(game);
        } catch (IOException e) {
            Log.d(BLUETOOTH_COMMUNICATION_TAG, "IOException while closing socket : + " + e.getMessage());
        }
    }

    public void sendCircuit() {
        Circuit c = Circuit.getInstance();
        String dataMsg = "circuit/" + Integer.toString(c.getLaps()) + "/" + c.getName();
        String x, y;
        x = Double.toString(c.getStartPoint().getX());
        y = Double.toString(c.getStartPoint().getY());
        dataMsg = dataMsg.concat("/" + x + ":" + y + ":" + 0);
        x = Double.toString(c.getEndPoints()[0].getX());
        y = Double.toString(c.getEndPoints()[0].getY());
        dataMsg = dataMsg.concat("/" + x + ":" + y + ":" + 0);
        x = Double.toString(c.getEndPoints()[0].getX());
        y = Double.toString(c.getEndPoints()[0].getY());
        dataMsg = dataMsg.concat("/" + x + ":" + y + ":" + 0);
        for (Integer i : c.getMarkersID().keySet()) {
            x = Double.toString(c.getMarkersID().get(i).getX());
            y = Double.toString(c.getMarkersID().get(i).getY());
            dataMsg = dataMsg.concat("/"+ Integer.toString(i) + ":" + x + ":" + y + ":" + 0);
        }
        byte[] dataMsgBytes = dataMsg.getBytes(Charset.forName("UTF-8"));
        write(dataMsgBytes);
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "Circuit send to client, string : " + dataMsg);
    }

    private void registerCommunicationBTListener(BluetoothCommunicationListener gameListener) {
        COMMUNICATION_BT_LISTENERS.add(gameListener);
    }

    private void unregisterGameListener(BluetoothCommunicationListener gameListener) {
        COMMUNICATION_BT_LISTENERS.remove(gameListener);
    }

    @Override
    public void onPlayerReady() {
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerReady called");
        // Create message
        String dataString = "isReady";
        byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
        write(dataBytes);
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerReady sent to the other phone");

    }

    @Override
    public void onPlayerFinished() {
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerFinished called");
        // Create message
        String dataString = "hasFinished";
        byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
        write(dataBytes);
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerFinished sent to the other phone");
    }

    @Override
    public void onPlayerFinishedLap() {
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerFinishedLap called");
        // Creates message
        String dataString = "hasFinishedLap";
        byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
        write(dataBytes);
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerFinishedLap sent to the other phone");
    }

    @Override
    public void onPlayerUseItem(final Item item) {
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerUseItem called");
        // Creates message
        String dataString;
        String name = item.getName();
        Vector3D position = item.getPosition();
        if (position != null) {
            dataString = "itemUsed" + "/" + name + "/" + position.getX() + "/" + position.getY() + "/" + position.getZ();
        } else {
            dataString = "itemUsed" + "/" + name;
        }
        byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Sends the message
        write(dataBytes);
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerUseItem sent to the other phone");
    }

    @Override
    public void onPlayerGaveUp() {
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerGaveUp called");
        // Create message
        String dataString = "hasGiveUp";
        byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
        write(dataBytes);
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onPlayerGaveUp sent to the other phone");
    }

    @Override
    public void onItemTouched(final Item item) {
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onItemTouched called");
        // Create message
        String dataString;
        String name = item.getName();
        Vector3D position = item.getPosition();
        if (position != null) {
            dataString = "hasTouchedItem" + "/" + name + "/" + position.getX() + "/" + position.getY() + "/" + position.getZ();
        } else {
            dataString = "hasTouchedItem" + "/" + name;
        }
        byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
        write(dataBytes);
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onItemTouched sent to the other phone");
    }

    @Override
    public void onStartRace() {
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onStartRace called");
        // Create message
        String dataString = "raceBegins";
        byte[] dateBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
        write(dateBytes);
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onStartRace sent to the other phone");
    }

    @Override
    public void onUpdatedPosition(final Vector3D position) {
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onUpdatedPosition called");
        // Create message
        String dataString = "updatedPosition" + "/" + position.getX() + "/" + position.getY() + "/" + position.getZ();
        byte[] dateBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
        write(dateBytes);
        Log.d(BLUETOOTH_COMMUNICATION_TAG, "onUpdatedPosition sent to the other phone");
    }

    /**
     * @param game The game to listen events from.
     */
    public void setGame(Game game) {
        if (game != null) {
            this.game = game;
            if (!COMMUNICATION_BT_LISTENERS.contains(game)) {
                registerCommunicationBTListener(game);
            }
        }
    }
}