package fr.enseeiht.superjumpingsumokart.application.network;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.BluetoothLeScanner;
import android.util.Log;
import java.io.IOException;
import java.util.UUID;

import fr.enseeiht.superjumpingsumokart.application.GUIWelcome;

/**
 * Created by Lucas on 07/02/2017.
 */
public class ServerBT extends Thread implements BluetoothAdapter.LeScanCallback {
    /**
     * The server socket where will be hosted the bluetooth communication.
     */
    private BluetoothServerSocket btServerSocket;
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

    private GUIWelcome GUI_WELCOME;
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
     */
    public ServerBT(GUIWelcome guiWelcome) {
        this.isConnected = false;
        GUI_WELCOME = guiWelcome;
    }

    @Override
    public void run() {
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the BT is disconnected, we force it to connect
        if (!this.btAdapter.isEnabled()) {
            this.btAdapter.enable();
            Log.v("SERVER", "BT connection...");
        }
        // We wait until the bluetooth is actually connected
        while (!this.btAdapter.isEnabled()){}
        Log.v("SERVER", "BT connected");
        try {
            btServerSocket = this.btAdapter.listenUsingRfcommWithServiceRecord("My Server", UUID.fromString("00002415-0000-1000-8000-00805F9B34FB"));

        } catch (IOException e) {        }
        // We cancel the bluetooth discovery
        btAdapter.cancelDiscovery();

        BluetoothSocket socket = null;
        Log.v("SERVER", "waiting for connections");
        GUI_WELCOME.GUI_WELCOME_HANDLER.sendEmptyMessage(GUIWelcome.BLUETOOTH_SERVER_READY);
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
                    GUI_WELCOME.GUI_WELCOME_HANDLER.sendEmptyMessage(GUIWelcome.BLUETOOTH_SERVER_GOT_CONNECTION);
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
            GUI_WELCOME.GUI_WELCOME_HANDLER.sendEmptyMessage(GUIWelcome.BLUETOOTH_SERVER_SHUTTED_DOWN);
        } catch (IOException e) {
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        btAdapter.stopLeScan(this);
    }
}