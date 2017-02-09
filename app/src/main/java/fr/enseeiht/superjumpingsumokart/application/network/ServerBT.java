package fr.enseeiht.superjumpingsumokart.application.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import static android.net.Uri.decode;

/**
 * Created by Lucas on 07/02/2017.
 */

public class ServerBT extends Thread {
    private final BluetoothServerSocket btServerSocket;
    private BluetoothAdapter btAdapter;
    private boolean isConnected;
    private CommunicationBT comServer;

    public CommunicationBT getComServer() {
        return comServer;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public ServerBT(BluetoothAdapter BA) {
        BluetoothServerSocket tmp;
        tmp = null;
        btAdapter = BA;
        isConnected = false;


        // If the BT is disconnected, we force it to connect
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
            Log.v("SERVER", "BT connected");
        }
        while (!btAdapter.isEnabled()){}

        try {
            tmp = btAdapter.listenUsingRfcommWithServiceRecord("My Server", UUID.fromString("00002415-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {        }
        btServerSocket = tmp;
    }

    @Override
    public void run() {


        BluetoothSocket socket = null;
        Log.v("SERVER", "waiting for connections");
        while (!isConnected) {


            if (btServerSocket != null) {
                Log.d("coucou", "connecté");
            }
            else{Log.d("coucou", "pas connecté");}


            // On attend que le client se connecte
            try {
                socket = btServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // On vérifie si le client s'est connecté
            if (socket != null) {
                Log.v("SERVER", "connected to client");
                try {
                    isConnected = true;
                    // on bloque les autres arrivées sur le socket
                    btServerSocket.close();
                    // on créé les threads de communication bluetooth

                } catch (IOException e) {
                }
                break;
            }
        }

        // We launch the BT communication threads
        this.comServer = new CommunicationBT(socket);
        comServer.start();
        String test = "coucou";
        Log.d("envoieMessage",test.getBytes().toString());
        comServer.write(test.getBytes());
        Log.v("SERVER", "communication launched");


    }


    public void cancel() {
        try {
            btServerSocket.close();
        } catch (IOException e) {
        }
    }

}
