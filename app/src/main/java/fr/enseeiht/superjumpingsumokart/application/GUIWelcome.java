package fr.enseeiht.superjumpingsumokart.application;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDevice;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import org.artoolkit.ar.base.NativeInterface;

import java.util.ArrayList;
import java.util.List;

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
    private WifiConnector wifiConnector = null;
    private ARDiscoveryDevice currentDevice = null;
    private List<ARDiscoveryDeviceService> devicesList = new ArrayList<>();

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
        exitBtn.setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(GUIWelcome.this, R.string.no_drone_connected, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     */
    private void wifiConnectionBtnAction() {
        if (devicesList != null && devicesList.size() > 0) {
            final Dialog wifiConnectionChoiceDialog = new Dialog(GUIWelcome.this);
            wifiConnectionChoiceDialog.setContentView(R.layout.wifi_connections_list);
            final ListView wifiConnectionsListView = (ListView) findViewById(R.id.wifiConnectionChoiceListView);
            final WifiConnectionListViewAdapter wifiConnectionListViewAdapter = new WifiConnectionListViewAdapter(GUIWelcome.this, R.layout.wifi_connection_item, devicesList);
            wifiConnectionsListView.setAdapter(wifiConnectionListViewAdapter);
            wifiConnectionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    wifiConnectionListViewAdapter.defineCurrentDevice(position);
                }
            });
        } else {
            Toast.makeText(GUIWelcome.this, R.string.no_connection_available, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     */
    private void btConnectionBtnAction() {
        //TODO
        Toast.makeText(GUIWelcome.this, "TODO", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */
    private void setCircuitBtnAction() {
        //TODO
        Toast.makeText(GUIWelcome.this, "TODO", Toast.LENGTH_SHORT).show();
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

    public void setDevicesList(List<ARDiscoveryDeviceService> devicesList) {
        this.devicesList = devicesList;
    }

    private class WifiConnectionListViewAdapter extends ArrayAdapter<ARDiscoveryDeviceService>{

        List<ARDiscoveryDeviceService> discoveryDeviceServicesList;
        TextView nameField;
        TextView idField;

        WifiConnectionListViewAdapter(Context context, int resource, List<ARDiscoveryDeviceService> discoveryDeviceServicesList) {
            super(context, -1, -1);
            this.discoveryDeviceServicesList = discoveryDeviceServicesList;
        }

        @Override
        public View getView(int positition, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.wifi_connection_item, parent, false);
            nameField = (TextView) findViewById(R.id.wifiConnectionItemNameField);
            idField = (TextView) findViewById(R.id.wifiConnectionItemIDField);
            nameField.setText(discoveryDeviceServicesList.get(positition).getName());
            nameField.setText(discoveryDeviceServicesList.get(positition).getProductID());
            return itemView;
        }

        void defineCurrentDevice(int position){
            ARDiscoveryDeviceService  arDiscoveryDeviceService= getItem(position);
            ((GUIWelcome) getContext()).currentDevice = wifiConnector.createDevice(arDiscoveryDeviceService);
        }
    }
}
