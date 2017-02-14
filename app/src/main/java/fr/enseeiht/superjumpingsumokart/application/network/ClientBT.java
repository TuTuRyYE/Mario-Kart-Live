package fr.enseeiht.superjumpingsumokart.application.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Lucas on 07/02/2017.
 */

public class ClientBT extends Thread {
    private BluetoothSocket btSocket;
    private final BluetoothDevice btDevice;
    private BluetoothAdapter btAdapter;
    private boolean isConnected;
    private CommunicationBT comClient;

    public CommunicationBT getComClient() {
        return comClient;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public ClientBT(BluetoothDevice device, BluetoothAdapter btAdapter) {
        // On utilise un objet temporaire car btSocket et btDevice sont "final"
        BluetoothSocket tmp = null;
        btDevice = device;
        this.btAdapter = btAdapter;
        isConnected = false;

        // If the BT is disconnected, we force it to connect
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
            Log.d("CLIENT", "BT connected");
        }

        while (!btAdapter.isEnabled()){}

        // On récupère un objet BluetoothSocket grâce à l'objet BluetoothDevice
        try {
            // MON_UUID est l'UUID (comprenez identifiant serveur) de l'application. Cette valeur est nécessaire côté serveur également !
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00002415-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) { }
        btSocket = tmp;
        btAdapter.cancelDiscovery();
    }


    public void run() {

        // On annule la découverte des périphériques (inutile puisqu'on est en train d'essayer de se connecter) TODO
        btAdapter.cancelDiscovery();
        try {
            // connexion
            Log.d("CLIENT", "trying to connect");
            btSocket.connect();
            Log.d("CLIENT", "connected to server");
        } catch (IOException connectException) {
            // If impossible to connect, we close the socket and kill the thread
            Log.d("CLIENT",connectException.getMessage());
            Log.d("CLIENT", "impossible to connect");
            try {
                btSocket =(BluetoothSocket) btDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(btDevice,1);
                btSocket.connect();
            } catch (Exception e) { }
            return;
        }
        isConnected = true;

        // We launch the BT communication threads
        CommunicationBT.initInstance(btSocket);
        comClient = CommunicationBT.getInstance();
        comClient.start();

        Log.d("CLIENT", "communication launched");
    }

    // Annule toute connexion en cours et tue le thread
    public void cancel() {
        try {
            btSocket.close();
        } catch (IOException e) { }
    }
}
