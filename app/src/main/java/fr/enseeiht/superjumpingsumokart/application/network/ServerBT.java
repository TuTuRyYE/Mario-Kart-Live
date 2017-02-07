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

/**
 * Created by Lucas on 07/02/2017.
 */

public class ServerBT extends Thread {
    private final BluetoothServerSocket btServerSocket;
    private BluetoothAdapter btAdapter;
    private boolean isConnected;


    public ServerBT(BluetoothAdapter BA) {
        BluetoothServerSocket tmp;
        tmp = null;
        btAdapter = BA;
        isConnected = false;
        try {
            tmp = btAdapter.listenUsingRfcommWithServiceRecord("My Server", UUID.fromString("00002415-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {        }
        btServerSocket = tmp;
    }

    @Override
    public void run() {

        // We verify that the device include BT
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            // The phone doesn't include bluetooth
        }

        // If the BT is disconnected, we force it to connect
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
        }


        BluetoothSocket socket = null;
        while (!isConnected) {
            // On attend que le client se connecte
            try {
                socket = btServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // On vérifie si le client c'est connecté
            if (socket != null) {
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
        CommunicationBT comServer = new CommunicationBT(socket);
        CommunicationBT comClient = new CommunicationBT(socket);
        comServer.start();

    }


    public void cancel() {
        try {
            btServerSocket.close();
        } catch (IOException e) {
        }
    }

}
