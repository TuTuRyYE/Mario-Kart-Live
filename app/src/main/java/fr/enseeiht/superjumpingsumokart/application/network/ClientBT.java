package fr.enseeiht.superjumpingsumokart.application.network;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
/**
 * Created by Lucas on 07/02/2017.
 */
public class ClientBT extends Thread {
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
    public ClientBT() {
        // We use a temporary object because btSocket is final
        BluetoothSocket tmpSocket = null;
        BluetoothDevice tmpDevice = null;
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        isConnected = false;
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
        } catch (IOException e) { }
        btSocket = tmpSocket;
    }


    @Override
    public void run() {
        try {
            // connexion
            Log.d("CLIENT", "trying to connect");
            btSocket.connect();
            Log.d("CLIENT", "connected to server");
            btAdapter.cancelDiscovery();
        } catch (IOException connectException) {
            // If impossible to connect, we close the socket and kill the thread
            Log.d("CLIENT",connectException.getMessage());
            Log.d("CLIENT", "impossible to connect");
            try {
                btSocket =(BluetoothSocket) btDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(btDevice,1);
                btSocket.connect();
                btAdapter.cancelDiscovery();
            } catch (Exception e) { }
            return;
        }
        isConnected = true;
        // We cancel the bluetooth discovery TODO

        // We launch the BT communication thread
        CommunicationBT.initInstance(btSocket);
        this.comClient = CommunicationBT.getInstance();
        this.isComBTCreated = true;
        comClient.start();
        Log.d("CLIENT", "communication launched");
    }

    /**
     * Close the connection
     */
    public void cancel() {
        try {
            btSocket.close();
        } catch (IOException e) { }
    }
}