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
import android.widget.Toast;

import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.R;

/**
 * @author Vivian GUY
 * Activity to modify a circuit
 */

public class GUIModifyCircuit extends Activity {

    /**
     * The logging tag. Useful for debugging.
     */
        private static String GUI_MODIFY_CIRCUIT_TAG = "GUIModifyCircuit";

    /**
     * Buttons of the GUI
     */
        private EditText circuitNameText;
        private EditText lapsText;
        private ListView listMarkers;
        private EditText idText;
        private EditText xText;
        private EditText yText;
        private EditText zText;
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
        private static ArrayList<String[]> markers;

    /**
     * The adapter for the listView listMarkers
     */
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guimodify_circuit);

        // Get the objects of the layout
            circuitNameText = (EditText) findViewById(R.id.circuitNameText);
            lapsText = (EditText) findViewById(R.id.lapsText);
            listMarkers = (ListView) findViewById(R.id.listMarkers);
            idText = (EditText) findViewById(R.id.idText);
            xText = (EditText) findViewById(R.id.xText);
            yText = (EditText) findViewById(R.id.yText);
            zText = (EditText) findViewById(R.id.zText);
            addMarkerBtn = (Button) findViewById(R.id.addMarkerBtn);
            confirmBtn = (Button) findViewById(R.id.confirmBtn);
            deleteMarkerBtn = (Button) findViewById(R.id.deleteMarkerBtn);


        // List of markers for the circuit
            markers = new ArrayList<>();

        // Adapter for the listView
            adapter = new MarkerAdapter(GUIModifyCircuit.this, markers);
            LayoutInflater inflater = getLayoutInflater();
            ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_markers, listMarkers, false);
            listMarkers.addHeaderView(header, null, false);
            listMarkers.setAdapter(adapter);

        // Set the circuit's markers
            final String circuitName = (String) getIntent().getExtras().get("circuitName");
            setMarkersList(circuitName);




        // Set buttons listener

            /**
             * Button to add a marker to the list of markers
             */
            addMarkerBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(GUI_MODIFY_CIRCUIT_TAG, "addMarkerBtn pressed");
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Get the texts from the EditTexts objects
                                String x = xText.getText().toString();
                                String y = yText.getText().toString();
                                String z = zText.getText().toString();
                                String id = idText.getText().toString();
                                if (!x.equals("") && !y.equals("") && !z.equals("") && !id.equals("")) { // if the EditTexts are not empty
                                    // Add the marker to markers list
                                        adapter.add(new String[]{id, x, y, z});
                                        Log.d(GUI_MODIFY_CIRCUIT_TAG, "marker added to the list");

                                    // Reset the EditTexts
                                        xText.setText("");
                                        yText.setText("");
                                        zText.setText("");
                                        idText.setText("");
                                        Log.d(GUI_MODIFY_CIRCUIT_TAG, "EditTexts reset");

                                    // Inform the user that the marker is added
                                        Toast.makeText(GUIModifyCircuit.this, "Marker added", Toast.LENGTH_SHORT).show();
                            }
                            else { // if a EditText is empty
                                Toast.makeText(GUIModifyCircuit.this, "You must enter marker's setting first", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                    return true;
                }
            });

            /**
             * Button to the confirm and modify the circuit. It instanced to circuit with the markers described in the list
             */
            confirmBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(GUI_MODIFY_CIRCUIT_TAG, "confirmBtn pressed");
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Modify the file corresponding to the circuit circuitName
                                modifyCircuitFile(circuitName);
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
                    Log.d(GUI_MODIFY_CIRCUIT_TAG, "deleteMarker pressed");
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (itemSelected != null) { // if an item is selected
                                if (itemSelected == 0) { // if the item selected is the default marker (startline)
                                    Toast.makeText(GUIModifyCircuit.this, "You can't delete this marker !", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // Remove the selected item
                                        adapter.remove(markers.get(itemSelected));
                                    // Reset the item selected
                                        itemSelected = null;
                                }
                            }
                            else { // if no item is selected
                                Toast.makeText(GUIModifyCircuit.this, "You must select a marker first", Toast.LENGTH_SHORT).show();
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
                    Log.d(GUI_MODIFY_CIRCUIT_TAG, "item " + i + " selected");
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
     * Modify the circuitFile with the name circuitName.
     * @param circuitName the name of the circuit to modify
     */
    protected void modifyCircuitFile(String circuitName) {
        // Get the file
            String filePath = GUIModifyCircuit.this.getFilesDir() + "/Circuits/" + circuitName;
            File oldCircuitFile = new File(filePath);

        // Delete the file
            oldCircuitFile.delete();

        // Create a file with the new markers, name and laps number
            String path = GUIModifyCircuit.this.getFilesDir() + "/Circuits";
            String txtName = circuitNameText.getText().toString();
            String lapTxt = lapsText.getText().toString();
            if (!txtName.isEmpty() && !lapTxt.equals("0") && !lapTxt.isEmpty()) { // if the user has put a circuit name and a number of lap
                File circuitFile = new File(path, circuitNameText.getText().toString());
                if (!circuitFile.exists()) { // if a file with the same name doesn't exist
                    FileOutputStream outputStream;
                    String stringToWrite;
                    try {
                        // Create the file of the circuit
                            outputStream = new FileOutputStream(circuitFile);
                            String firstLine = txtName + "/" + lapTxt + "\n";
                            outputStream.write(firstLine.getBytes());
                            for (String[] s : markers) {
                                stringToWrite = s[0] + " " + s[1] + " " + s[2] + " " + s[3] + "\n";
                                outputStream.write(stringToWrite.getBytes());
                            }
                            outputStream.close();
                            Log.d(GUI_MODIFY_CIRCUIT_TAG, "Circuit file created");

                        // Inform the user that the circuit file has been created (and so modified)
                            Toast.makeText(GUIModifyCircuit.this, "Circuit modified!", Toast.LENGTH_SHORT).show();

                        // Go back to GUIWelcome Activity
                            Intent i = new Intent(GUIModifyCircuit.this, GUIWelcome.class);
                            Log.d(GUI_MODIFY_CIRCUIT_TAG, "Launching a GUIWelcome Activity...");
                            startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            else { // A circuit with the same name already exists
                Toast.makeText(GUIModifyCircuit.this, "A circuit with this name already exists !", Toast.LENGTH_SHORT).show();
            }
        } else {// The user's input aren't correct
            if (lapTxt.equals("0")) {
                Toast.makeText(GUIModifyCircuit.this, "Lap number must be different to 0 !", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(GUIModifyCircuit.this, "You have to put a circuit name and a lap number !", Toast.LENGTH_SHORT).show();
            }
        }


    }

    /**
     * Set the list of markers for the circuit with name circuitName
     * @param circuitName the game of the circuit
     */
    private void setMarkersList(String circuitName) {

        // Get the file
            String filePath = GUIModifyCircuit.this.getFilesDir() + "/Circuits/" + circuitName;
            File circuitFile = new File(filePath);

        // Read the file
            try {
                FileInputStream fis = new FileInputStream(circuitFile);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String line;
                String[] lineSplit;
                String x,y,z,id;

                // First line
                    line = bufferedReader.readLine();
                    lineSplit = line.split("/");
                    circuitNameText.setText(lineSplit[0]);
                    lapsText.setText(lineSplit[1]);

                // Other line (markers) with the following format: id x y z
                    while ((line = bufferedReader.readLine()) != null) {
                        // Get the marker info
                            lineSplit = line.split(" ");
                            id = lineSplit[0];
                            x = lineSplit[1];
                            y = lineSplit[2];
                            z = lineSplit[3];

                        // Add the marker to markers list
                            adapter.add(new String[]{id, x, y, z});
                            Log.d(GUI_MODIFY_CIRCUIT_TAG, "Marker " + lineSplit[0] + ":" + x + " " + y + " " + z + " added");
                    }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
            }

    }

}
