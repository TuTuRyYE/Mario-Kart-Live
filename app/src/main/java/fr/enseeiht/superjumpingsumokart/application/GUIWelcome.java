package fr.enseeiht.superjumpingsumokart.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import org.artoolkit.ar.base.NativeInterface;

import java.util.ArrayList;
import java.util.List;

import fr.enseeiht.superjumpingsumokart.application.network.WifiConnector;
import fr.enseeiht.superjumpingsumokart.arpack.GUIGame;
import fr.enseeiht.superjumpingsumokart.R;

/**
 * @author Romain Verset
 * The activity used as home screen for the application. From there it is possible to connect to a
 * drone, to launch a race, to edit circuits and to connect with another SuperJumpingSumoKart
 * application using Bluetooth.
 */
public class GUIWelcome extends Activity {

    // Static block to load libraries (ARToolkit + ParrotSDK3)
    static {
        ARSDK.loadSDKLibs();
        NativeInterface.loadNativeLibrary();
    }

    /**
     * The logging tag. Useful for debugging.
     */
    private final static String GUI_WELCOME_TAG = "GUIWelcome";

    // Buttons in the GUI
    private Button startRaceBtn;
    private ToggleButton wifiConnectionBtn;
    private Button btConnectionBtn;
    private Button setCircuitBtn;
    private Button exitBtn;

    // Connection and device variables
    private WifiConnector wifiConnector = null;
    private ARDiscoveryDeviceService currentDeviceService = null;
    private List<ARDiscoveryDeviceService> devicesList = new ArrayList<>();

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

    /**
     * Switch the current {@link GUIWelcome} {@link android.app.Activity} for a {@link GUIGame} {@link android.app.Activity} (Romain Verset - 31/01/2017).
     * This switch requires to have a drone connected with the application.
     */
    private void startRaceBtnAction(){
        if (currentDeviceService != null) {
            Intent i = new Intent(GUIWelcome.this, GUIGame.class);
            i.putExtra("currentDeviceService", currentDeviceService);
            Log.d(GUI_WELCOME_TAG, "Starting a GUIGame Activity...");
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
    private void btConnectionBtnAction() {
        //TODO
        Toast.makeText(GUIWelcome.this, "TODO", Toast.LENGTH_SHORT).show();
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
     * Updates the list of available devices (Romain Verset - 31/01/2017).
     * @param devicesList The new list of availables devices.
     */
    public void setDevicesList(List<ARDiscoveryDeviceService> devicesList) {
        this.devicesList = devicesList;
    }

    /**
     * Disable the WIFI connection button (Romain Verset - 31/01/2017).
     */
    public void disableWifiConnectionBtn() {
        wifiConnectionBtn.setEnabled(false);
        wifiConnectionBtn.setChecked(false);
    }

    /**
     * Enable the WIFI connection button (Romain Verset - 31/01/2017).
     */
    public void enableWifiConnectionBtn() {
        wifiConnectionBtn.setEnabled(true);
    }

}
