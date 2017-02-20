package fr.enseeiht.superjumpingsumokart.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.R;

public class GUICircuit extends Activity {

    /**
     * The logging tag. Useful for debugging.
     */
    private static String GUI_CIRCUIT_TAG = "GUICircuit";

    private Button createNewCircuitBtn;
    private Button choseSelectedBtn;
    private Button deleteCircuitBtn;
    private Button modifyCircuitBtn;
    private ListView existingCircuitsListView;

    private ArrayList<String[]> existingCircuits;

    private ArrayAdapter adapter;

    private Integer itemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guicircuit);

        // Get the objects from the layout
            this.createNewCircuitBtn = (Button) findViewById(R.id.createCircuitButton);
            this.choseSelectedBtn = (Button) findViewById(R.id.choseSelectedButton);
            this.existingCircuitsListView = (ListView) findViewById(R.id.existingCircuitsList);
            this.modifyCircuitBtn = (Button) findViewById(R.id.modifyCircuitBtn);
            this.deleteCircuitBtn = (Button) findViewById(R.id.deleteCircuitBtn);


            existingCircuits = new ArrayList<>();

        // Adapter for the listView
        adapter = new CircuitAdapter(GUICircuit.this, existingCircuits);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_circuit, existingCircuitsListView, false);
        existingCircuitsListView.addHeaderView(header, null, false);
        existingCircuitsListView.setAdapter(adapter);

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
                            //TODO create the instance of circuit according to the selected circuit
                            break;
                    }
                    return true;
                }
            });

        existingCircuitsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = i;
                adapterView.setSelection(i);
            }
        });

        deleteCircuitBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_CIRCUIT_TAG, "deleteCircuit pressed");
                        //TODO delete the circuit
                        break;
                }
                return true;
            }
        });

        modifyCircuitBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_CIRCUIT_TAG, "modifyCircuit pressed");
                        //TODO sent to an activity to modify the circuit
                        break;
                }
                return true;
            }
        });

    }


    protected void setExistingCircuitsList() {
        String path = GUICircuit.this.getFilesDir()+"/Circuits";
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            try {
                FileInputStream fis = new FileInputStream(files[i]);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                //TODO
                line = bufferedReader.readLine();
                String[] lineSplit = line.split("/");
                adapter.add(new String[]{lineSplit[0], lineSplit[1]});

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(GUI_CIRCUIT_TAG, sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
