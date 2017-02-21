package fr.enseeiht.superjumpingsumokart.application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import static java.lang.Thread.sleep;

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
                        CircuitAdapter.selectedPos = selectedPos - 1;
                        Log.d(GUI_CIRCUIT_TAG, "Chose selected circuit pressed");
                        // Get the selected circuit
                        String[] circuitSelected = (String[]) existingCircuitsListView.getItemAtPosition(itemSelected);
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
                        HighLightCircuit();
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
//                if (itemSelected != null) {
//                    if (itemSelected == i) {
//                        if (!(existingCircuitsListView.getChildAt(i).getDrawingCacheBackgroundColor() == Color.BLUE)) {
//                            Log.d(GUI_CIRCUIT_TAG, "yolo1");
//                        existingCircuitsListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
//                        itemSelected = null;
//                        }
//                        else {
//                            itemSelected = null;
//                        }
//                    } else {
//                        if (!(existingCircuitsListView.getChildAt(i).getDrawingCacheBackgroundColor() == Color.BLUE)) {
//                            existingCircuitsListView.getChildAt(itemSelected).setBackgroundColor(Color.TRANSPARENT);
//                        }
//                        itemSelected = i;
//                        existingCircuitsListView.getChildAt(i).setBackgroundColor(Color.RED);
//                    }
//                } else {
//                    itemSelected = i;
//                    Log.d(GUI_CIRCUIT_TAG, "item selected: " + itemSelected);
//                    existingCircuitsListView.getChildAt(i).setBackgroundColor(Color.RED);
//                }
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
                adapter.add(new String[]{lineSplit[0], lineSplit[1]});
                Log.d(GUI_CIRCUIT_TAG, "circuit add to the list");
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    protected void HighLightCircuit() {
        // Highlight the current circuit instance if it exists

        Log.d(GUI_CIRCUIT_TAG, "Size: " + existingCircuits.size());
        if (Circuit.getInstance() != null) {
            String name = Circuit.getInstance().getName();
            boolean found = false;
            int i = 0;
            Log.d(GUI_CIRCUIT_TAG, "Child count: " + existingCircuitsListView.getChildCount());
            while (!found && i < existingCircuits.size()) {
                Log.d(GUI_CIRCUIT_TAG, "Circuit name: " + name);
                Log.d(GUI_CIRCUIT_TAG, "Name in the list: " + existingCircuits.get(i)[0]);
                if (existingCircuits.get(i)[0].equals(name)) {
                    Log.d(GUI_CIRCUIT_TAG, "i :" + i);
                    Log.d(GUI_CIRCUIT_TAG, "circuit found");
                    found = true;
                    if (existingCircuitsListView .getChildAt(1)== null) {
                        Log.d(GUI_CIRCUIT_TAG, "listview null 1");
                    }
                    if (existingCircuitsListView .getChildAt(0)== null) {
                        Log.d(GUI_CIRCUIT_TAG, "listview null 0");
                    }
                    existingCircuitsListView.getChildAt(i+1).setBackgroundColor(Color.BLUE);

                }
                i++;
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        existingCircuitsListView.setAdapter(adapter);
        setExistingCircuitsList();
        HighLightCircuit();
    }
}
