package fr.enseeiht.superjumpingsumokart.application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    private int selectedPos = -1, oldSelectedPos = -1;

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
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_circuit, existingCircuitsListView, false);
        existingCircuitsListView.addHeaderView(header, null, false);
        existingCircuitsListView.setAdapter(adapter);


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
                        if (CircuitAdapter.selectedPos >=  0) {
                            existingCircuitsListView.getChildAt(CircuitAdapter.selectedPos).setBackgroundColor(Color.TRANSPARENT);
                        }
                        existingCircuitsListView.getChildAt(selectedPos).setBackgroundColor(Color.BLUE);
                        CircuitAdapter.selectedPos = selectedPos;
                        Log.d(GUI_CIRCUIT_TAG, "Chose selected circuit pressed");
                        // Get the selected circuit
                        String[] circuitSelected = (String[]) existingCircuitsListView.getItemAtPosition(selectedPos);
                        String circuitName = circuitSelected[0];
                        Log.d(GUI_CIRCUIT_TAG, "Name circuit selected: " + circuitName);
                        Circuit.initInstance(Integer.parseInt(circuitSelected[1]));
                        Circuit.getInstance().setName(circuitName);
                        // Get the corresponding file
                        String filePath = GUICircuit.this.getFilesDir() + "/Circuits/" + circuitName;
                        File circuitFile = new File(filePath);
                        try {
                            FileInputStream fis = new FileInputStream(circuitFile);
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader bufferedReader = new BufferedReader(isr);
                            String line;
                            String[] lineSplit;
                            Double x, y, z;
                            bufferedReader.readLine(); // skip the first line
                            while ((line = bufferedReader.readLine()) != null) {
                                lineSplit = line.split(" ");
                                x = Double.parseDouble(lineSplit[1]);
                                y = Double.parseDouble(lineSplit[2]);
                                z = Double.parseDouble(lineSplit[3]);
                                Circuit.getInstance().addMarker(Integer.parseInt(lineSplit[0]), new Vector3D(x, y, z));
                                Log.d(GUI_CIRCUIT_TAG, "Marker " + lineSplit[0] + ":" + x + " " + y + " " + z + " added");
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(GUICircuit.this, "Circuit selected", Toast.LENGTH_SHORT).show();
                        // Go Back to GUIWelcome
                        Intent i = new Intent(GUICircuit.this, GUIWelcome.class);
                        Log.d(GUI_CIRCUIT_TAG, "Launching a GUIWelcome Activity...");
                        startActivity(i);


                        break;
                }
                return true;
            }
        });

        existingCircuitsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPos = i;
                if (oldSelectedPos != CircuitAdapter.selectedPos && oldSelectedPos >= 0) {
                    existingCircuitsListView.getChildAt(oldSelectedPos).setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.RED);
                oldSelectedPos = selectedPos;
                if (CircuitAdapter.selectedPos >= 0) {
                    existingCircuitsListView.getChildAt(CircuitAdapter.selectedPos).setBackgroundColor(Color.BLUE);
                }
            }
        });

        deleteCircuitBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_CIRCUIT_TAG, "deleteCircuit pressed");
                        // Get the circuit
                        String[] circuitSelected = (String[]) existingCircuitsListView.getItemAtPosition(selectedPos);
                        String circuitName = circuitSelected[0];
                        // Get the corresponding file
                        String filePath = GUICircuit.this.getFilesDir() + "/Circuits/" + circuitName;
                        File circuitFile = new File(filePath);
                        boolean isDeleted = circuitFile.delete();
                        if (isDeleted) {
                            adapter.remove(circuitSelected);
                            adapter.notifyDataSetChanged();
                            Log.d(GUI_CIRCUIT_TAG, "Circuit " + circuitName + " deleted");
                        }
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
                        Intent i = new Intent(GUICircuit.this, GUIModifyCircuit.class);
                        String[] circuitSelected = (String[]) existingCircuitsListView.getItemAtPosition(selectedPos);
                        String circuitName = circuitSelected[0];
                        i.putExtra("circuitName", circuitName);
                        Log.d(GUI_CIRCUIT_TAG, "Launching a GUIModifyCircuit Activity...");
                        startActivity(i);
                        break;
                }
                return true;
            }
        });

    }


    protected void setExistingCircuitsList() {
        String path = GUICircuit.this.getFilesDir() + "/Circuits";
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            try {
                FileInputStream fis = new FileInputStream(files[i]);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String line;
                line = bufferedReader.readLine();
                String[] lineSplit = line.split("/");
                // Check if the file is not in the list
                boolean found = false;
                int k = 0;
                while (!found && k<existingCircuits.size()) {
                    if (files[i].getName().equals(existingCircuits.get(k)[0])) {
                        found = true;
                    }
                    k++;
                }
                if (!found) {
                    adapter.add(new String[]{lineSplit[0], lineSplit[1]});
                    Log.d(GUI_CIRCUIT_TAG, "circuit add to the list");
                }
                //adapter.add(new String[]{lineSplit[0], lineSplit[1]});
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        existingCircuitsListView.setAdapter(adapter);
        setExistingCircuitsList();
        // HighLightCircuit();
    }
}
