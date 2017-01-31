package fr.enseeiht.superjumpingsumokart.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDevice;

import org.artoolkit.ar.base.NativeInterface;

import fr.enseeiht.superjumpingsumokart.GUIGame;
import fr.enseeiht.superjumpingsumokart.R;

public class GUIWelcome extends AppCompatActivity {

    private final static String GUI_WELCOME_TAG = "GUIWelcome";

    private Button startRaceBtn;
    private Button wifiConnectionBtn;
    private Button btConnectionBtn;
    private Button setCircuitBtn;
    private Button exitBtn;

    // Connection and device variables
    private WifiConnector wifiConnector;
    private ARDiscoveryDevice currentDevice;

    static {
        ARSDK.loadSDKLibs();
        NativeInterface.loadNativeLibrary();
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
        wifiConnectionBtn = (Button) findViewById(R.id.connectWifiBtn);
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
        setCircuitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitBtnAction();
            }
        });

    }

    /**
     * Switch the current {@link GUIWelcome} {@link android.app.Activity} for a {@link GUIGame} {@link android.app.Activity}.
     * This switch requires to have a drone connected with the application.
     */
    private void startRaceBtnAction(){
        if (currentDevice != null) {
            Intent i = new Intent(this, GUIGame.class);
            Log.d(GUI_WELCOME_TAG, "Starting a GUIGame Activity...");
            startActivity(i);
        } else {
            Toast.makeText(GUIWelcome.this, R.string.no_drone_connected, Toast.LENGTH_SHORT);
        }
    }

    /**
     *
     */
    private void wifiConnectionBtnAction() {
    }

    /**
     *
     */
    private void btConnectionBtnAction() {

    }

    /**
     *
     */
    private void setCircuitBtnAction() {

    }

    /**
     * Default action to do when the exit button is clicked.
     * It closes the eventual connection between the application and the drone and cleans
     * all variables used to avoid memory leak.
     */
    private void exitBtnAction() {
        if (wifiConnector != null) {
            wifiConnector.stop();
            wifiConnector = null;
        }
        finish();
    }

}
