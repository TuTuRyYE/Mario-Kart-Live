package fr.enseeiht.superjumpingsumokart.application;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.network.ClientBT;
import fr.enseeiht.superjumpingsumokart.application.network.CommunicationBT;
import fr.enseeiht.superjumpingsumokart.application.network.ServerBT;
import fr.enseeiht.superjumpingsumokart.application.network.WifiConnector;
import fr.enseeiht.superjumpingsumokart.arpack.GUIGame;
/**
 * @author Romain Verset
 * The activity used as home screen for the application. From there it is possible to connect to a
 * drone, to launch a race, to edit circuits and to connect with another SuperJumpingSumoKart
 * application using Bluetooth.
 */
public class GUIWelcome extends Activity implements BluetoothAdapter.LeScanCallback{
    // Static block to load libraries (ARToolkit + ParrotSDK3)

    public final static int DEVICE_SERVICE_CONNECTED = 0;
    public final static int DEVICE_SERVICE_DISCONNECTED = 1;

    public final Handler GUI_WELCOME_HANDLER = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DEVICE_SERVICE_CONNECTED :
                    enableWifiConnectionBtn();
                    if (msg.obj instanceof List) {
                        devicesList = (List<ARDiscoveryDeviceService>) msg.obj;
                    } else {
                        Log.d(GUI_WELCOME_TAG, "Object in msg can not be cast in List<ARDiscoveryDeviceService>");
                    }
                    break;
                case DEVICE_SERVICE_DISCONNECTED :
                    disableWifiConnectionBtn();
                default :
                    break;
            }
        }
    };

    // Static block to load libraries ParrotSDK3
    static {
        ARSDK.loadSDKLibs();
    }
    /**
     * The logging tag. Useful for debugging.
     */
    private final static String GUI_WELCOME_TAG = "GUIWelcome";
    // Buttons in the GUI
    private Button startRaceBtn;
    private ToggleButton wifiConnectionBtn;
    private Button btConnectionBtn;
    private Button btJoinBtn;
    private Button setCircuitBtn;
    private Button exitBtn;
    // Connection and device variables
    private WifiConnector wifiConnector = null;
    private ARDiscoveryDeviceService currentDeviceService = null;
    private List<ARDiscoveryDeviceService> devicesList = new ArrayList<>();
    private BluetoothAdapter btAdapter;
    private BluetoothDevice btDevice;
    private CommunicationBT com;


    // à déplacer au besoin...
    public CommunicationBT getCom() {
        return com;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initializes the GUI from layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_welcome);
        // Initializes remote connection
        wifiConnector = new WifiConnector(GUIWelcome.this);
        // Initializes the views of the GUI
        startRaceBtn = (Button) findViewById(R.id.startRaceBtn);
        wifiConnectionBtn = (ToggleButton) findViewById(R.id.connectWifiBtn);
        wifiConnectionBtn.setEnabled(false);
        btConnectionBtn = (Button) findViewById(R.id.connectBluetoothBtn);
        btJoinBtn = (Button) findViewById(R.id.joinBluetoothBtn);
        setCircuitBtn = (Button) findViewById(R.id.setCircuitBtn);
        exitBtn = (Button) findViewById(R.id.exitBtn);
        // Defines action listener
        startRaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRaceBtnAction();
            }
        });
        wifiConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiConnectionBtnAction();
            }
        });
        btConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btConnectionBtnAction();
            }
        });
        btJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btJoinBtnAction();
            }
        });
        setCircuitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCircuitBtnAction();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitBtnAction();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        disableWifiConnectionBtn();
    }

    /**
     * Switch the current {@link GUIWelcome} {@link android.app.Activity} for a {@link GUIGame} {@link android.app.Activity} (Romain Verset - 31/01/2017).
     * This switch requires to have a drone connected with the application.
     */
    private void startRaceBtnAction(){
       // if (currentDeviceService != null) {
            Intent i = new Intent(GUIWelcome.this, GUIGame.class);
            i.putExtra("currentDeviceService", currentDeviceService);
        if (com != null){
            Log.d(GUI_WELCOME_TAG,"com ok");
        }
            Log.d(GUI_WELCOME_TAG, "Launching a GUIGame Activity...");
            startActivity(i);
       // } else {
         //   Toast.makeText(GUIWelcome.this, R.string.no_drone_connected, Toast.LENGTH_SHORT).show();
        //}
    }
    /**
     * Enables to connect with a Jumping Sumo drone (Romain Verset - 31/01/2017).
     * <b>Your cell phone has to be connected to the acces point provided by the Jumping Sumo drone.<b/>
     */
    private void wifiConnectionBtnAction() {
        try {
            if (wifiConnectionBtn.isChecked()) {
                currentDeviceService = devicesList.get(0);
                Log.d(GUI_WELCOME_TAG, "New device service bound to the application : " + currentDeviceService.toString());
            } else {
                Log.d(GUI_WELCOME_TAG, "Device service unbound from the application : " + currentDeviceService.toString());
                currentDeviceService = null;
            }
        } catch (NullPointerException npe) {
            Log.d(GUI_WELCOME_TAG, "Unable to bind the device service");
        }
    }
    /**
     * //TODO
     */
    private void btConnectionBtnAction() {
        //TODO
        // We verify that the device include BT
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            // The phone doesn't include bluetooth
        }
        ServerBT server = new ServerBT(btAdapter);
        server.start();
        btAdapter.stopLeScan(this);
        if (this.com == null){
            Log.d(GUI_WELCOME_TAG,"com nullllll");
        }
        this.com = server.getComServer();
    }
    /**
     * //TODO
     */
    private void btJoinBtnAction() {
        //TODO
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            // The phone doesn't include bluetooth
        }
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        //Seul les deux telephones sont appareillés, le seul device que l'on peut trouver est donc celui qu'on veut
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                btDevice = device;
            }
        }
        ClientBT client = new ClientBT(btDevice,btAdapter);
        client.start();
        if (com == null) {
            Log.d(GUI_WELCOME_TAG, "com  null after get");
        }
    }
    /**
     * //TODO
     */
    private void setCircuitBtnAction() {
        //TODO
        Toast.makeText(GUIWelcome.this, "TODO", Toast.LENGTH_SHORT).show();
    }
    /**
     * Default action to do when the exit button is clicked (Romain Verset - 31/01/2017).
     * It closes the eventual connection between the application and the drone and cleans
     * all variables used to avoid memory leak.
     */
    private void exitBtnAction() {
        if (wifiConnector != null) {
            wifiConnector.stop();
            wifiConnector = null;
        }
        currentDeviceService = null;
        devicesList = null;
        finish();
    }

    /**
     * Disable the WIFI connection button (Romain Verset - 31/01/2017).
     */
    private void disableWifiConnectionBtn() {
        wifiConnectionBtn.setEnabled(false);
        wifiConnectionBtn.setChecked(false);
    }
    /**
     * Enable the WIFI connection button (Romain Verset - 31/01/2017).
     */
    private void enableWifiConnectionBtn() {
        wifiConnectionBtn.setEnabled(true);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

    }
}