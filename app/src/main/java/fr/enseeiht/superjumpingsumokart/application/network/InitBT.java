package fr.enseeiht.superjumpingsumokart.application.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Lucas on 07/02/2017.
 */

/* Initi*/
public class InitBT extends AppCompatActivity implements Serializable{
    private transient BluetoothDevice btDevice;
    private transient BluetoothAdapter btAdapter;
    public static CommunicationBT com;
    public static CommunicationBT comCl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            // The phone doesn't include bluetooth
        }

        // If the BT is disconnected, we force it to connect
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
        }

        // Return the set of BluetoothDevice objects that are bonded (paired) to the local adapter.
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

        // only our two phones are peared, so the one we want is the only one peared
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                btDevice = device;
            }
        }
    }

}
