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
import android.os.Looper;
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
    private Handler handlerGame;

    private static CommunicationBT comBTInstance;

/*    public Handler handlerComBT = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage (Message msg) {
            Bundle bundle = msg.getData();
            byte[] byteMsg = bundle.getByteArray("0");
            comBTInstance.write(byteMsg);
        }
    };*/


    private CommunicationBT(BluetoothSocket socket) {
        btSocket = socket;

        // Initialization of the streams
        try {
            btInputStream = socket.getInputStream();
            btOutputStream = socket.getOutputStream();
        } catch (IOException e) { }
    }

    public static void initInstance(BluetoothSocket socket) {
        if (comBTInstance == null) {
            comBTInstance = new CommunicationBT(socket);
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
                Log.d("COMMUNICATIONBT", "Message received");
                Log.d("COMMUNICATIONBT", receivedMsg);
                //Create message
                Message mes = new Message();
                //Create bundle
                Bundle bundle = new Bundle();
                bundle.putByteArray("0", data);
                mes.setData(bundle);
                if (handlerGame == null) {
                    Log.d("COMMUNICATIONBT", "yolo");
                }

                    // handlerGame.sendMessage(mes);


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

    public void setHandlerGame(Handler handlerGame) {
        this.handlerGame = handlerGame;
    }
/*
    public Handler getHandlerComBT() {
        return handlerComBT;
    }*/
}
