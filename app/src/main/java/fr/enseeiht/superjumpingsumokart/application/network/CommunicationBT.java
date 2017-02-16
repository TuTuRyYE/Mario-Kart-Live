package fr.enseeiht.superjumpingsumokart.application.network;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.os.Parcelable;
import android.util.Log;

import fr.enseeiht.superjumpingsumokart.application.Game;
import fr.enseeiht.superjumpingsumokart.application.GameListener;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;
import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by Lucas on 07/02/2017.
 */
/* Manages the bluetooth communication for an appeared device */
public class CommunicationBT extends Thread implements  GameListener {

    private final static String COMMUNICATION_BT_TAG = "CommunicationBT";
    private final BluetoothSocket BT_SOCKET;
    private InputStream btInputStream;
    private OutputStream btOutputStream;
    private Game game;
    private final ArrayList<CommunicationBTListener> COMMUNICATION_BT_LISTENERS = new ArrayList<>();
    private static CommunicationBT comBTInstance;

    public CommunicationBT(BluetoothSocket socket) {
        BT_SOCKET = socket;
        // Initialization of the streams
        try {
            btInputStream = socket.getInputStream();
            btOutputStream = socket.getOutputStream();
        } catch (IOException e) { }
    }

    public static void initInstance(BluetoothSocket bs) {
        if (comBTInstance != null) {
            comBTInstance = new CommunicationBT(bs);
        }
    }

    public static CommunicationBT getInstance() {
        return comBTInstance;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
        // Permanent listening on the inputstream
        while (true) {
            try {
                // reading on the inputstream
                bytes = btInputStream.read(buffer);
                // copy it
                byte[] data = new byte[bytes];
                System.arraycopy(buffer, 0, data, 0, bytes);
                String receivedMsg = new String(data, Charset.forName("UTF-8"));


                // Dispatch the message in function of its key
                    String[] msgSplit = receivedMsg.split("/");
                    String key = msgSplit[0];
                    Log.d(COMMUNICATION_BT_TAG, "Message " + key + " received");
                    switch (key) {
                        case "isReady" :
                            for (CommunicationBTListener listener : this.COMMUNICATION_BT_LISTENERS) {
                                listener.onSecondPlayerReady();
                            }
                            break;
                        case "hasFinished" :
                            for (CommunicationBTListener listener : this.COMMUNICATION_BT_LISTENERS) {
                                listener.onSecondPlayerFinished();
                            }
                            break;
                        case "hasFinishedLap" :
                            for (CommunicationBTListener listener : this.COMMUNICATION_BT_LISTENERS) {
                                listener.onSecondPlayerLapFinished();
                            }
                            break;
                        case "itemUsed" :
                            for (CommunicationBTListener listener : this.COMMUNICATION_BT_LISTENERS) {
                                String itemInfos;
                                if (msgSplit.length == 2) { // if the object hasn't a position
                                    itemInfos = msgSplit[1];
                                }
                                else { // si the object has a position
                                    itemInfos = msgSplit[1] + "/" + msgSplit[2] + "/" + msgSplit[3] + "/" + msgSplit[4];
                                }
                                listener.onSecondPlayerUsesItem(itemInfos);
                            }
                            break;
                        case "hasGiveUp" :
                            for (CommunicationBTListener listener : this.COMMUNICATION_BT_LISTENERS) {
                                listener.onSecondPlayerGaveUp();
                            }
                            break;
                        case "hasTouchedItem" :
                            for (CommunicationBTListener listener : this.COMMUNICATION_BT_LISTENERS) {
                                String itemInfos;
                                if (msgSplit.length == 2) { // if the object hasn't a position
                                    itemInfos = msgSplit[1];
                                }
                                else { // si the object has a position
                                    itemInfos = msgSplit[1] + "/" + msgSplit[2] + "/" + msgSplit[3] + "/" + msgSplit[4];
                                }
                                listener.onSecondPlayerTouchedItem(itemInfos);
                            }
                            break;
                        case "raceBegins" :
                            for (CommunicationBTListener listener : this.COMMUNICATION_BT_LISTENERS) {
                                listener.onSecondStartRace();
                            }
                            break;
                        case "updatedPosition" :
                            for (CommunicationBTListener listener : this.COMMUNICATION_BT_LISTENERS) {
                                String newPosition = msgSplit[1] + "/" + msgSplit[2] + "/" + msgSplit[3];
                                listener.onSecondPlayerUpdatedPosition(newPosition);
                            }
                            break;
                    }
            } catch (IOException e) {
                Log.d(COMMUNICATION_BT_TAG, "IOException : + " + e.getMessage());
                break;
            }
        }
    }
    /* Writing on the outputstream */
    public void write(byte[] bytes) {
        try {
            btOutputStream.write(bytes);
        } catch (IOException e) {
            Log.d(COMMUNICATION_BT_TAG, "IOException : + " + e.getMessage());
        }
    }
    /* Closes the socket */
    public void cancel() {
        try {
            BT_SOCKET.close();
            unregisterGameListener(game);
        } catch (IOException e) {
            Log.d(COMMUNICATION_BT_TAG, "IOException : + " + e.getMessage());
        }
    }
    public void setGame(Game game) {
        if (game != null) {
            this.game = game;
            if (!COMMUNICATION_BT_LISTENERS.contains(game)) {
                registerCommunicationBTListener(game);
            }
        }
    }

    public void registerCommunicationBTListener(CommunicationBTListener gameListener) {
        COMMUNICATION_BT_LISTENERS.add(gameListener);
    }

    public void unregisterGameListener(CommunicationBTListener gameListener) {
        COMMUNICATION_BT_LISTENERS.remove(gameListener);
    }

    @Override
    public void onPlayerReady() {
        Log.d(COMMUNICATION_BT_TAG, "onPlayerReady called");
        // Create message
            String dataString = "isReady";
            byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
                write(dataBytes);
        Log.d(COMMUNICATION_BT_TAG, "onPlayerReady sent to the other phone");

    }

    @Override
    public void onPlayerFinished() {
        Log.d(COMMUNICATION_BT_TAG, "onPlayerFinished called");
        // Create message
            String dataString = "hasFinished";
            byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
            write(dataBytes);
        Log.d(COMMUNICATION_BT_TAG, "onPlayerFinished sent to the other phone");
    }

    @Override
    public void onPlayerFinishedLap() {
        Log.d(COMMUNICATION_BT_TAG, "onPlayerFinishedLap called");
        // Create message
            String dataString = "hasFinishedLap";
            byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
            write(dataBytes);
        Log.d(COMMUNICATION_BT_TAG, "onPlayerFinishedLap sent to the other phone");
    }

    @Override
    public void onPlayerUseItem(Item item) {
        Log.d(COMMUNICATION_BT_TAG, "onPlayerUseItem called");
        // Create message
            String dataString;
            String name = item.getName();
            Vector3D position = item.getPosition();
            if (position != null) {
                dataString = "itemUsed" + "/" + name + "/" + position.getX() + "/" + position.getY() + "/" + position.getZ();
            }
            else {
                dataString = "itemUsed" + "/" + name;
            }
            byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
            write(dataBytes);
        Log.d(COMMUNICATION_BT_TAG, "onPlayerUseItem sent to the other phone");
    }

    @Override
    public void onPlayerGaveUp() {
        Log.d(COMMUNICATION_BT_TAG, "onPlayerGaveUp called");
        // Create message
            String dataString = "hasGiveUp";
            byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
            write(dataBytes);
            Log.d(COMMUNICATION_BT_TAG, "onPlayerGaveUp sent to the other phone");
    }

    @Override
    public void onItemTouched(Item item) {
        Log.d(COMMUNICATION_BT_TAG, "onItemTouched called");
        // Create message
            String dataString;
            String name = item.getName();
            Vector3D position = item.getPosition();
            if (position != null) {
                dataString = "hasTouchedItem" + "/" + name + "/" + position.getX() + "/" + position.getY() + "/" + position.getZ();
            }
            else {
            dataString = "hasTouchedItem" + "/" + name;
            }
        byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
            write(dataBytes);
            Log.d(COMMUNICATION_BT_TAG, "onItemTouched sent to the other phone");
    }

    @Override
    public void onStartRace() {
        Log.d(COMMUNICATION_BT_TAG, "onStartRace called");
        // Create message
            String dataString = "raceBegins";
            byte[] dateBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
            write(dateBytes);
            Log.d(COMMUNICATION_BT_TAG, "onStartRace sent to the other phone");
    }

    @Override
    public void onUpdatedPosition(Vector3D position) {
        Log.d(COMMUNICATION_BT_TAG, "onUpdatedPosition called");
        // Create message
            String dataString = "updatedPosition" + "/" +  position.getX() + "/" + position.getY() + "/" + position.getZ();
            byte[] dateBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
            write(dateBytes);
        Log.d(COMMUNICATION_BT_TAG, "onUpdatedPosition sent to the other phone");
    }


}