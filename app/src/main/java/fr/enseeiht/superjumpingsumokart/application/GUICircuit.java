package fr.enseeiht.superjumpingsumokart.application;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import fr.enseeiht.superjumpingsumokart.R;

public class GUICircuit extends Activity {

    /**
     * The logging tag. Useful for debugging.
     */
    private static String GUI_CIRCUIT_TAG = "GUICircuit";

    private Button createNewCircuitBtn;
    private Button choseSelectedBtn;
    private ListView existingCircuitsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guicircuit);

        // Get the objects from the layout
            this.createNewCircuitBtn = (Button) findViewById(R.id.createCircuitButton);
            this.choseSelectedBtn = (Button) findViewById(R.id.choseSelectedButton);
            this.existingCircuitsList = (ListView) findViewById(R.id.existingCircuitsList);

        // Set the existing circuits list
            setExistingCircuitsList();

        // Set Buttons Listener
            createNewCircuitBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d(GUI_CIRCUIT_TAG, "Create new circuit pressed");
                            Intent i = new Intent(GUICircuit.this, GUICreateCircuit.class);
                            Log.d(GUI_CIRCUIT_TAG, "Launching a GUICreateCircuit Activity...");
                            startActivity(i);
                            break;
                    }
                    return true;
                }
            });

            choseSelectedBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d(GUI_CIRCUIT_TAG, "Chose selected circuit pressed");
                            //TODO create the instance of circuit acording to the selected circuit
                            break;
                    }
                    return true;
                }
            });



    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(GUI_CIRCUIT_TAG, "Resuming GUIGame activity");
    }

    protected void setExistingCircuitsList() {
        // TODO
    }
}
