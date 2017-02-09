package fr.enseeiht.superjumpingsumokart.application.network;

import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import fr.enseeiht.superjumpingsumokart.application.Game;

/**
 * Created by Lucas on 07/02/2017.
 */

/* Manages the bluetooth communication for an appeared device */
public class CommunicationBT extends Thread implements Serializable {
    private final BluetoothSocket btSocket;
    private InputStream btInputStream;
    private OutputStream btOutputStream;
    private Game game;

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
                this.game.receiveMessage(receivedMsg);
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
}
