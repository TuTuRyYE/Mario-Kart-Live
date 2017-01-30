package fr.enseeiht.superjumpingsumokart.application;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.artoolkit.ar.base.FPSCounter;
import org.artoolkit.ar.base.NativeInterface;

import com.parrot.arsdk.ARSDK;

import fr.enseeiht.superjumpingsumokart.R;

public class GUIWelcom extends AppCompatActivity {

    Button startRaceBtn;
    Button wifiConnexionBtn;
    Button btConnexionBtn;
    Button setCircuitBtn;

    static {
        ARSDK.loadSDKLibs();
        NativeInterface.loadNativeLibrary();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FPSCounter fpsCounter = new FPSCounter();
        fpsCounter.reset();
        setContentView(R.layout.activity_gui_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.startRaceBtn = (Button) findViewById(R.id.startRaceBtn);
        this.wifiConnexionBtn = (Button) findViewById(R.id.wifiConnexionBtn);
        this.btConnexionBtn = (Button) findViewById(R.id.btConnexionBtn);
        this.setCircuitBtn = (Button) findViewById(R.id.setCircuitBtn);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // TODO Add Listeners

}
