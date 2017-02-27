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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

/**
 * @author Vivian GUY
 * Activity to create a new Circuit
 */

public class GUICreateCircuit extends Activity {

    /**
     * The logging tag. Useful for debugging.
     */
        private static String GUI_CREATE_CIRCUIT_TAG = "GUICreateCircuit";

    /**
     * Buttons in the GUI
     */
        private EditText circuitNameText;
        private EditText lapsText;
        private EditText checkPointToCheckText;
        private ListView listMarkers;
        private Spinner symbolText;
        private Button addMarkerBtn;
        private Button confirmBtn;
        private Button deleteMarkerBtn;

    /**
     * Item selected in the ListView listMarkers
     */
        private Integer itemSelected;

    /**
     * The list of markers for the circuit
     */
        private static ArrayList<String> markers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guicreatecircuit);

        // Get the objects from the layout
            circuitNameText = (EditText) findViewById(R.id.circuitNameText);
            lapsText = (EditText) findViewById(R.id.lapsText);
            checkPointToCheckText = (EditText) findViewById(R.id.nbCheckPointTxt);
            listMarkers = (ListView) findViewById(R.id.listMarkers);
            symbolText = (Spinner) findViewById(R.id.symbolText);
            addMarkerBtn = (Button) findViewById(R.id.addMarkerBtn);
            confirmBtn = (Button) findViewById(R.id.confirmBtn);
            deleteMarkerBtn = (Button) findViewById(R.id.deleteMarkerBtn);


        // List of markers for the circuit
            markers = new ArrayList<>();

        // Adapter for the listView
            final ArrayAdapter adapter = new MarkerAdapter(GUICreateCircuit.this, markers);
            LayoutInflater inflater = getLayoutInflater();
            ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_markers, listMarkers, false);
            listMarkers.addHeaderView(header, null, false);
            listMarkers.setAdapter(adapter);

        // Default marker for the circuit (start line)
            adapter.add("HIRO");
            adapter.add("HIRO");

        // List of symbols for the spinner
            final ArrayList<String> listSymbols = new ArrayList();
            listSymbols.add("A");
            listSymbols.add("B");
            listSymbols.add("C");
            listSymbols.add("D");
            listSymbols.add("F");
            listSymbols.add("G");
            listSymbols.add("KANJI");

        // Adapter for the spinner
            ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listSymbols);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            symbolText.setAdapter(spinnerAdapter);




        // Set buttons listener

            /**
             * Button to add a marker to the list of markers
             */
            addMarkerBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.d(GUI_CREATE_CIRCUIT_TAG, "addMarkerBtn pressed");
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // Get the texts from the EditTexts objects
                                    String symbol = symbolText.getSelectedItem().toString();
                                    if (!symbol.isEmpty()) { // if the Spinner is not empty
                                        // Add the marker to markers list
                                            adapter.add(symbol);
                                            // Remove the symbol from the list if it isn't "KANJI" (others symbol can appear only one time in the circuit)
                                            if (!symbol.equals("KANJI")) {
                                                listSymbols.remove(symbol);
                                            }
                                            Log.d(GUI_CREATE_CIRCUIT_TAG, "marker " + symbol + " added to the list");

                                        // Reset the EditTexts
                                            symbolText.setSelection(0);
                                            Log.d(GUI_CREATE_CIRCUIT_TAG, "Spinner reset");

                                        // Inform the user that the marker is added
                                            Toast.makeText(GUICreateCircuit.this, "Marker added", Toast.LENGTH_SHORT).show();
                                    }
                                    else { // if a EditText is empty
                                        Toast.makeText(GUICreateCircuit.this, "You must enter marker's setting first", Toast.LENGTH_SHORT).show();
                                    }
                                break;
                        }
                        return true;
                    }
                });

            /**
             * Button to the confirm the circuit. It instanced to circuit with the markers described in the list
             */
            confirmBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.d(GUI_CREATE_CIRCUIT_TAG, "confirmBtn pressed");
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // Create the file for the circuit and instanced it
                                    createCircuitFile();
                                break;
                        }
                        return true;
                    }
                });

            /**
             * Button to delete the selected marker
             */
            deleteMarkerBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.d(GUI_CREATE_CIRCUIT_TAG, "deleteMarker pressed");
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (itemSelected != null) { // if an item is selected
                                    if (itemSelected == 0) { // if the user tries to delete the default marker start line
                                        Toast.makeText(GUICreateCircuit.this, "You can't delete this marker !", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        // Remove the selected marker
                                            adapter.remove(markers.get(itemSelected));
                                        // Reset itemSelected
                                            itemSelected = null;
                                    }
                                }
                                else { // if the user hasn't selected an item
                                    Toast.makeText(GUICreateCircuit.this, "You must select a marker first", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                        return true;
                    }
                });

            /**
             * Listener to select an item of the listView listMarkers
            */
            listMarkers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d(GUI_CREATE_CIRCUIT_TAG, "item " + i + " selected");
                    if (itemSelected != null) { // if an item was previously selected
                        if (itemSelected == i) { // if the the previous selected item is the same as the selected
                            // Deselect the item by setting its color to none
                                listMarkers.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                            // Reset itemSelected
                                itemSelected = null;
                        }
                        else { // if the previous selected item isn't the same as the selected
                            // Set the previous selected item's color to none
                                listMarkers.getChildAt(itemSelected).setBackgroundColor(Color.TRANSPARENT);
                            // Select the item selected and color it in red
                                itemSelected = i;
                                listMarkers.getChildAt(i).setBackgroundColor(Color.RED);
                        }
                    }
                    else { // if no item was previously selected
                        // Select the item and set its color to red
                            itemSelected = i;
                            listMarkers.getChildAt(i).setBackgroundColor(Color.RED);
                    }
                }
            });

    }

    /**
     * Create the circuit file and instanced it
     */
    protected void createCircuitFile() {
        // Create the directory if it doesn't exist
            File CircuitsDir = new File(GUICreateCircuit.this.getFilesDir() + "/Circuits");
            if (!CircuitsDir.exists()) { // create the folder if it doesn't exist
                boolean isCreated = CircuitsDir.mkdir();
                if (isCreated) {
                    Log.d(GUI_CREATE_CIRCUIT_TAG, "file created");
                }
            }

        String path = GUICreateCircuit.this.getFilesDir() + "/Circuits";

        // Get the info about the circuit
            String txtName = circuitNameText.getText().toString();
            String lapTxt = lapsText.getText().toString();
            String checkPointTxt = checkPointToCheckText.getText().toString();

        // Create the file
        if (!txtName.isEmpty() && !lapTxt.equals("0") && !lapTxt.isEmpty() && !checkPointTxt.equals("0") && !checkPointTxt.isEmpty()) { // if the user has put a circuit name and a number of lap different from 0

            // Create the file
                File circuitFile = new File(path, circuitNameText.getText().toString());
                if (!circuitFile.exists()) { // if a file with the same name doesn't exist
                    FileOutputStream outputStream;
                    String stringToWrite;
                    try {
                        // Creating the file and the instance of the circuit
                            Circuit.initInstance(Integer.parseInt(lapTxt), Integer.parseInt(checkPointTxt));
                            outputStream = new FileOutputStream(circuitFile);
                            String firstLine = txtName + "/" + lapTxt + "/" + checkPointTxt + "\n";
                            Circuit.getInstance().setName(txtName);
                            Circuit.getInstance().setCheckPointToCheck(Integer.parseInt(checkPointTxt));
                            outputStream.write(firstLine.getBytes());
                            for (String s : markers) {
                                stringToWrite = s + "\n";
                                outputStream.write(stringToWrite.getBytes());
                                Circuit.getInstance().addMarker(DetectionTask.Symbol.valueOf(s));
                            }
                            String lastLines = "HIRO" + "\n" + "HIRO";
                            outputStream.write(lastLines.getBytes());
                            outputStream.close();
                            Log.d(GUI_CREATE_CIRCUIT_TAG, "Circuit file created");

                        // Inform the user that the file has been created
                            Toast.makeText(GUICreateCircuit.this, "Circuit created!", Toast.LENGTH_SHORT).show();

                        // Go back to GUIWelcome Activity
                            Intent i = new Intent(GUICreateCircuit.this, GUIWelcome.class);
                            Log.d(GUI_CREATE_CIRCUIT_TAG, "Launching a GUIWelcome Activity...");
                            startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            else { // A circuit with the same name already exists
                Toast.makeText(GUICreateCircuit.this, "A circuit with this name already exists !", Toast.LENGTH_SHORT).show();
            }
        } else { // The user's input aren't correct
            if (lapTxt.equals("0")) {
                Toast.makeText(GUICreateCircuit.this, "Lap number must be different to 0 !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GUICreateCircuit.this, "You have to put a circuit name and a lap number !", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
