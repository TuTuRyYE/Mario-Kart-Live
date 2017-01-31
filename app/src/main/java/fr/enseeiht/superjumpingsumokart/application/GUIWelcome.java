package fr.enseeiht.superjumpingsumokart.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.parrot.arsdk.ARSDK;
import org.artoolkit.ar.base.NativeInterface;

import fr.enseeiht.superjumpingsumokart.R;

public class GUIWelcome extends AppCompatActivity {

    private Button startRaceBtn;
    private Button wifiConnectionBtn;
    private Button btConnectionBtn;
    private Button setCircuitBtn;
    private Button exitBtn;

    private WifiConnector wifiConnector;

    static {
        ARSDK.loadSDKLibs();
        NativeInterface.loadNativeLibrary();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_welcome);
        this.startRaceBtn = (Button) findViewById(R.id.startRaceBtn);
        this.wifiConnectionBtn = (Button) findViewById(R.id.connectWifiBtn);
        this.btConnectionBtn = (Button) findViewById(R.id.connectBluetoothBtn);
        this.setCircuitBtn = (Button) findViewById(R.id.setCircuitBtn);
        this.exitBtn = (Button) findViewById(R.id.exitBtn);
        wifiConnector = new WifiConnector(getApplicationContext());
    }

    // TODO Add Listeners

}
