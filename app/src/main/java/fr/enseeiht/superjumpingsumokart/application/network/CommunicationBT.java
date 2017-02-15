package fr.enseeiht.superjumpingsumokart.application.network;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import fr.enseeiht.superjumpingsumokart.application.Game;
import fr.enseeiht.superjumpingsumokart.application.GameListener;
import fr.enseeiht.superjumpingsumokart.application.Vector3D;
import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by Lucas on 07/02/2017.
 */
/* Manages the bluetooth communication for an appeared device */
public class CommunicationBT extends Thread implements Serializable, GameListener {
    private final BluetoothSocket btSocket;
    private InputStream btInputStream;
    private OutputStream btOutputStream;
    private Game game;
    private final ArrayList<CommunicationBTListener> COMMUNICATION_BT_LISTENERS = new ArrayList<>();
  //  private Handler handlerGame;
    public CommunicationBT(BluetoothSocket socket) {
        btSocket = socket;
        // Initialization of the streams
        try {
            btInputStream = socket.getInputStream();
            btOutputStream = socket.getOutputStream();
        } catch (IOException e) { }
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
                Log.d("COMMUNICATIONBT", "Message received");
                Log.d("COMMUNICATIONBT", receivedMsg);
                //Create message
                Message mes = new Message();
                //Create bundle
       /*         Bundle bundle = new Bundle();
                bundle.putByteArray("0", data);
                mes.setData(bundle);
//                handlerGame.sendMessage(mes);
*/
            } catch (IOException e) {
                break;
            }
        }
    }
    /* Writing on the outputstream */
    public void write(byte[] bytes) {
        try {
            btOutputStream.write(bytes);
        } catch (IOException e) { }
    }
    /* Closes the socket */
    public void cancel() {
        try {
            btSocket.close();
        } catch (IOException e) { }
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public void registerCommunicationBTListener(CommunicationBTListener gameListener) {
        COMMUNICATION_BT_LISTENERS.add(gameListener);
    }

    public void unregisterGameListener(CommunicationBTListener gameListener) {
        COMMUNICATION_BT_LISTENERS.remove(gameListener);
    }

    @Override
    public void onPlayerReady() {
        // Create message
            String dataString = "isReady";
            byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
                write(dataBytes);

    }

    @Override
    public void onPlayerFinished() {
        // Create message
            String dataString = "hasFinished";
            byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
            write(dataBytes);
    }

    @Override
    public void onPlayerUseItem(Item item) {
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
    }

    @Override
    public void onPlayerGiveUp() {
        // Create message
            String dataString = "hasGiveUp";
            byte[] dataBytes = dataString.getBytes(Charset.forName("UTF-8"));
        // Send the message
            write(dataBytes);
    }

    @Override
    public void onItemTouched(Item item) {
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
    }
}