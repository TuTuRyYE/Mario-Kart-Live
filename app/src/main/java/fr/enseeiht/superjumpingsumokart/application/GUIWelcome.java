package fr.enseeiht.superjumpingsumokart.application;

import android.app.Activity;
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

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.network.BluetoothClient;
import fr.enseeiht.superjumpingsumokart.application.network.BluetoothServer;
import fr.enseeiht.superjumpingsumokart.application.network.WifiConnector;
import fr.enseeiht.superjumpingsumokart.arpack.GUIGame;
/**
 * @author Romain Verset
 * The activity used as home screen for the application. From there it is possible to connect to a
 * drone, to launch a race, to edit circuits and to connect with another SuperJumpingSumoKart
 * application using Bluetooth.
 */
public class GUIWelcome extends Activity {

    // Static block to load libraries ParrotSDK3
    static {
        ARSDK.loadSDKLibs();
    }

    public final static int DEVICE_SERVICE_CONNECTED = 0;
    public final static int DEVICE_SERVICE_DISCONNECTED = 1;
    public final static int BLUETOOTH_SERVER_READY = 3;
    public final static int BLUETOOTH_SERVER_GOT_CONNECTION = 4;
    public final static int BLUETOOTH_CLIENT_JOINED_GAME = 5;
    public final static int BLUETOOTH_SERVER_SHUTTED_DOWN = 6;

    public final static int BLUETOOTH_CLIENT_SHUTTED_DOWN = 7;

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
                    break;
                case BLUETOOTH_CLIENT_JOINED_GAME :
                    onClientConnected();
                    break;
                case BLUETOOTH_SERVER_READY :
                    onServerReady();
                    break;
                case BLUETOOTH_SERVER_GOT_CONNECTION :
                    onServerReceivedConnection();
                    break;
                case BLUETOOTH_SERVER_SHUTTED_DOWN:
                    onServerShutDown();
                break;
                case BLUETOOTH_CLIENT_SHUTTED_DOWN:
                    onClientShutDown();
                break;
                default :
                    break;
            }
        }
    };
    /**
     * The logging tag. Useful for debugging.
     */
    private final static String GUI_WELCOME_TAG = "GUIWelcome";

    // Buttons in the GUI
    private Button startRaceBtn;
    private ToggleButton wifiConnectionBtn;
    private Button btHostBtn;
    private Button btJoinBtn;
    private Button setCircuitBtn;
    private Button exitBtn;
    // Connection and device variables
    private WifiConnector wifiConnector = null;
    private BluetoothServer server = null;
    private BluetoothClient client = null;
    private ARDiscoveryDeviceService currentDeviceService = null;
    private List<ARDiscoveryDeviceService> devicesList = new ArrayList<>();

    private boolean isServer = true;
    private boolean serverHosting, clientConnected;

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
        btHostBtn = (Button) findViewById(R.id.connectBluetoothBtn);
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
        btHostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btHostBtnAction();
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
        if (currentDeviceService == null) {
            disableWifiConnectionBtn();
        }
    }

    /**
     * Switch the current {@link GUIWelcome} {@link android.app.Activity} for a {@link GUIGame} {@link android.app.Activity} (Romain Verset - 31/01/2017).
     * This switch requires to have a drone connected with the application.
     */
    private void startRaceBtnAction(){
        if (currentDeviceService != null) {
            Intent i = new Intent(GUIWelcome.this, GUIGame.class);
            i.putExtra("currentDeviceService", currentDeviceService);
            i.putExtra("isServer", isServer);
            Log.d(GUI_WELCOME_TAG, "Launching a GUIGame Activity...");
            startActivity(i);
        } else {
            Toast.makeText(GUIWelcome.this, R.string.no_drone_connected, Toast.LENGTH_SHORT).show();
        }
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
    private void btHostBtnAction() {
        if (!serverHosting) {
            server = new BluetoothServer(GUIWelcome.this);
            server.start();
            serverHosting = true;
        } else {
            onServerShutDown();
        }
    }
    /**
     * //TODO
     */
    private void btJoinBtnAction() {
        if (!clientConnected) {
            startRaceBtn.setEnabled(false);
            isServer = false;
            client = new BluetoothClient(GUIWelcome.this);
            client.start();
        } else {
            onClientShutDown();
        }
    }
    /**
     * //TODO
     */
    private void setCircuitBtnAction() {
        //TODO
        Intent i = new Intent(GUIWelcome.this, GUICircuit.class);
        Log.d(GUI_WELCOME_TAG, "Launching a GUICircuit Activity...");
        startActivity(i);

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
     * The button is allowed when the phone and the drone are on the same WiFi network.
     */
    private void disableWifiConnectionBtn() {
        wifiConnectionBtn.setChecked(false);
        wifiConnectionBtn.setEnabled(false);
    }

    /**
     * Enable the WIFI connection button (Romain Verset - 31/01/2017).
     * The button is allowed when the phone and the drone are on the same WiFi network.
     */
    private void enableWifiConnectionBtn() {
        wifiConnectionBtn.setEnabled(true);
    }

    /**
     * Callback called when the {@link BluetoothServer} is ready and waiting for a {@link BluetoothClient} (Romain Verset - 17/02/2017).
     */
    private void onServerReady() {
        this.btHostBtn.setBackgroundColor(getResources().getColor(R.color.waitingForClient));
        this.btHostBtn.setText(getResources().getString(R.string.hostBTButtonOn));
    }

    /**
     * Callback called when a {@link BluetoothClient} connects to the {@link BluetoothServer} (Romain Verset - 17/02/2017).
     */
    private void onServerReceivedConnection() {
        btHostBtn.setBackgroundColor(getResources().getColor(R.color.connected));
        this.btHostBtn.setText(getResources().getString(R.string.hostBTButtonOnAndPlayerConnected));
    }

    /**
     * Callback called when the {@link BluetoothClient} has sucessfully connected to a {@link BluetoothServer} (Romain Verset - 17/02/2017).
     */
    private void onClientConnected() {
        btJoinBtn.setBackgroundColor(getResources().getColor(R.color.connected));
        this.btJoinBtn.setText(getResources().getString(R.string.joinBTButtonOn));
        clientConnected = true;
    }

    /**
     * Callback called when the {@link BluetoothClient} is no longer available (Romain Verset - 17/02/2017).
     */
    private void onClientShutDown() {
        btJoinBtn.setBackgroundColor(getResources().getColor(R.color.notConnected));
        this.btJoinBtn.setText(getResources().getString(R.string.joinBTButtonOff));
        if (client != null) {
            client = null;
        }
        clientConnected = false;
    }

    /**
     * Callback called when the {@link BluetoothServer} is no longer available (Romain Verset - 17/02/2017).
     */
    public void onServerShutDown() {
        btHostBtn.setBackgroundResource(android.R.drawable.btn_default);
        btHostBtn.setText(getResources().getString(R.string.hostBTButtonOff));
        if (server != null) {
            server = null;
        }
        serverHosting = false;
    }

    public void enableStartARaceButton() {
        startRaceBtn.setEnabled(true);
    }
}