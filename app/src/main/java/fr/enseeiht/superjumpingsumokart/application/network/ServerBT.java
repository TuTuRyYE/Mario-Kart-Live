package fr.enseeiht.superjumpingsumokart.application.network;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.util.UUID;
/**
 * Created by Lucas on 07/02/2017.
 */
public class ServerBT extends Thread {
    /**
     * The server socket where will be hosted the bluetooth communication.
     */
    private final BluetoothServerSocket btServerSocket;
    /**
     * The local bluetooth adapter.
     */
    private BluetoothAdapter btAdapter;
    /**
     * Certify that the bluetooth connexion is established.
     */
    private boolean isConnected;
    /**
     * The bluetooth communication.
     */
    private CommunicationBT comServer;
    /**
     * Get the bluetooth communication.
     * @return the bluetooth communication.
     */
    private boolean comLaunched = false;
    public CommunicationBT getComServer() {
        while (!comLaunched){}
        return comServer;
    }
    /**
     * Get the state of the bluetooth connexion.
     * @return the state of the bluetooth connexion.
     */
    public boolean isConnected() {
        return isConnected;
    }
    /**
     * Create the server for the bluetooth connexion.
     * @param btAdapter the local  bluetooth adapter.
     */
    public ServerBT(BluetoothAdapter btAdapter) {
        BluetoothServerSocket tmp;
        tmp = null;
        this.btAdapter = btAdapter;
        isConnected = false;
        // If the BT is disconnected, we force it to connect
        if (!this.btAdapter.isEnabled()) {
            this.btAdapter.enable();
            Log.v("SERVER", "BT connected");
        }
        while (!this.btAdapter.isEnabled()){}
        try {
            tmp = this.btAdapter.listenUsingRfcommWithServiceRecord("My Server", UUID.fromString("00002415-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {        }
        btServerSocket = tmp;
    }

    @Override
    public void run() {
        BluetoothSocket socket = null;
        Log.v("SERVER", "waiting for connections");
        // We wait for a client attempting to connect
        while (!isConnected) {
            if (btServerSocket != null) {
                Log.d("SERVER", " server socket initialized");
            }
            else{Log.d("SERVER", "not connected");}
            try {
                Log.d("SERVER","trying to connect...");
                socket = btServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // We verify that the connexion is established
            if (socket != null) {
                Log.v("SERVER", "connected to client");
                try {
                    isConnected = true;
                    // We close the socket
                    btServerSocket.close();
                } catch (IOException e) {
                }
                break;
            }
        }
        // We launch the BT communication threads
        CommunicationBT.initInstance(socket);
        this.comServer = CommunicationBT.getInstance();
        comServer.start();
        comLaunched = true;
        Log.v("SERVER", "communication launched");
    }


    /**
     * Close the connection
     */
    public void cancel() {
        try {
            btServerSocket.close();
        } catch (IOException e) {
        }
    }
}