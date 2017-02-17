package fr.enseeiht.superjumpingsumokart.application.network;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import fr.enseeiht.superjumpingsumokart.application.GUIWelcome;

/**
 * Created by Lucas on 07/02/2017.
 */
public class ClientBT extends Thread implements BluetoothAdapter.LeScanCallback{
    /**
     * The socket where will be hosted the bluetooth communication.
     */
    private BluetoothSocket btSocket;
    /**
     * The local device.
     */
    private BluetoothDevice btDevice;
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
    private CommunicationBT comClient;

    private final GUIWelcome GUI_WELCOME;

    private boolean isComBTCreated = false;

    /**
     * Get the bluetooth communication.
     * @return the bluetooth communication.
     */
    public CommunicationBT getComClient() {
        while (!isComBTCreated){

        }
        return comClient;
    }

    /**
     * Get the state of the bluetooth connexion.
     * @return the state of the bluetooth connexion.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Create the client for the bluetooth connexion.
     */
    public ClientBT(GUIWelcome guiWelcome) {
        isConnected = false;
        GUI_WELCOME = guiWelcome;

    }


    @Override
    public void run() {
        BluetoothSocket tmpSocket = null;
        BluetoothDevice tmpDevice = null;
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        //Seul les deux telephones sont appareillés, le seul device que l'on peut trouver est donc celui qu'on veut
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                tmpDevice = device;
            }
            this.btDevice = tmpDevice;
        }
        // If the BT is disconnected, we force it to connect
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
            Log.d("CLIENT", "BT connected");
        }
        // We get a BluetoothSocket object thanks to the BluetoothDevice.
        try {
            // The UUID is the login for the server. It is the same on the server's side.
            tmpSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString("00002415-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {
        }
        btSocket = tmpSocket;
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
        // We launch the BT communication thread
        CommunicationBT.initInstance(btSocket);
        this.comClient = CommunicationBT.getInstance();
        this.isComBTCreated = true;
        comClient.start();
        GUI_WELCOME.GUI_WELCOME_HANDLER.sendEmptyMessage(GUIWelcome.BLUETOOTH_CLIENT_JOINED_GAME);
        Log.d("CLIENT", "communication launched");
    }


    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        btAdapter.stopLeScan(this);
    }


    /**
     * Close the connection
     */
    public void cancel() {
        try {
            btSocket.close();
        } catch (IOException e) { }
        GUI_WELCOME.GUI_WELCOME_HANDLER.sendEmptyMessage(GUIWelcome.BLUETOOTH_CLIENT_SHUTTED_DOWN);
    }
}