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


public class GUIModifyCircuit extends Activity {

    private static String GUI_MODIFY_CIRCUIT_TAG = "GUIModifyCircuit";

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

    private Integer itemSelected;

    private static ArrayList<String[]> markers;

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



        markers = new ArrayList<>();

        // Adapter for the listView
        adapter = new MarkerAdapter(GUIModifyCircuit.this, markers);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_markers, listMarkers, false);
        listMarkers.addHeaderView(header, null, false);
        listMarkers.setAdapter(adapter);

        // Get the circuit's markers
        final String circuitName = (String) getIntent().getExtras().get("circuitName");
        setMarkersList(circuitName);




        // Set buttons listener
        addMarkerBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_MODIFY_CIRCUIT_TAG, "addMarkerBtn pressed");
                        String x = xText.getText().toString();
                        String y = yText.getText().toString();
                        String z = zText.getText().toString();
                        String id = idText.getText().toString();
                        if (!x.equals("") && !y.equals("") && !z.equals("") && !id.equals("")) {
                            // Add the marker to markers list
                            adapter.add(new String[]{id, x, y, z});
                            Log.d(GUI_MODIFY_CIRCUIT_TAG, "marker added to the list");
                            xText.setText("");
                            yText.setText("");
                            zText.setText("");
                            idText.setText("");
                            Log.d(GUI_MODIFY_CIRCUIT_TAG, "EditTexts reset");
                            Toast.makeText(GUIModifyCircuit.this, "Marker added", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(GUIModifyCircuit.this, "You must enter marker's setting first", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });

        confirmBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_MODIFY_CIRCUIT_TAG, "confirmBtn pressed");
                        modifyCircuitFile(circuitName);
                        break;
                }
                return true;
            }
        });

        deleteMarkerBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_MODIFY_CIRCUIT_TAG, "deleteMarker pressed");
                        if (itemSelected != null) {
                            if (itemSelected == 0) {
                                Toast.makeText(GUIModifyCircuit.this, "You can't delete this marker !", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                adapter.remove(markers.get(itemSelected));
                                itemSelected = null;
                            }
                        }
                        else {
                            Toast.makeText(GUIModifyCircuit.this, "You must select a marker first", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });


        listMarkers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (itemSelected != null) {
                    if (itemSelected == i) {
                        listMarkers.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        itemSelected = null;
                    }
                    else {
                        listMarkers.getChildAt(itemSelected).setBackgroundColor(Color.TRANSPARENT);
                        itemSelected = i;
                        listMarkers.getChildAt(i).setBackgroundColor(Color.RED);
                    }
                }
                else {
                    itemSelected = i;
                    listMarkers.getChildAt(i).setBackgroundColor(Color.RED);
                }
            }
        });

    }

    protected void modifyCircuitFile(String circuitName) {
        // Get the file
        String filePath = GUIModifyCircuit.this.getFilesDir() + "/Circuits/" + circuitName;
        File oldCircuitFile = new File(filePath);
        // Delete the file
        oldCircuitFile.delete();
        // Create a file with the new markers, name and laps number
        String path = GUIModifyCircuit.this.getFilesDir() + "/Circuits";
        String txtName = circuitNameText.getText().toString();
        String defaultName = getResources().getString(R.string.confirm_btn);
        String lapTxt = lapsText.getText().toString();
        String lapDefault = getResources().getString(R.string.lapsText);
        if (!txtName.equals(defaultName) && !txtName.isEmpty() && !lapTxt.equals(lapDefault) && !lapTxt.equals("0") && !lapTxt.isEmpty()) { // if the user has put a circuit name and a number of lap
            File circuitFile = new File(path, circuitNameText.getText().toString());
            if (!circuitFile.exists()) { // if a file with the same name doesn't exist
                FileOutputStream outputStream;
                String stringToWrite;

                try {
                    // Creating the file of the circuit
                    outputStream = new FileOutputStream(circuitFile);
                    String firstLine = txtName + "/" + lapTxt + "\n";
                    outputStream.write(firstLine.getBytes());
                    for (String[] s : markers) {
                        stringToWrite = s[0] + " " + s[1] + " " + s[2] + " " + s[3] + "\n";
                        outputStream.write(stringToWrite.getBytes());
                        int markerID = Integer.parseInt(s[0]);
                        double x = Double.parseDouble(s[1]);
                        double y = Double.parseDouble(s[2]);
                        double z = Double.parseDouble(s[3]);
                    }
                    outputStream.close();
                    Log.d(GUI_MODIFY_CIRCUIT_TAG, "Circuit file created");
                    Toast.makeText(GUIModifyCircuit.this, "Circuit modified!", Toast.LENGTH_SHORT).show();
                    // Go back to GUIWelcome
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
        } else {
            if (lapTxt.equals("0")) {
                Toast.makeText(GUIModifyCircuit.this, "Lap number must be different to 0 !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GUIModifyCircuit.this, "You have to put a circuit name and a lap number !", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void setMarkersList(String circuitName) {
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
            // Other line (markers)
        while ((line = bufferedReader.readLine()) != null) {
            lineSplit = line.split(" ");
            id = lineSplit[0];
            x = lineSplit[1];
            y = lineSplit[2];
            z = lineSplit[3];

                // Add the marker to markers list
            adapter.add(new String[]{id, x, y, z});
            Log.d(GUI_MODIFY_CIRCUIT_TAG, "marker added to the list");
            Log.d(GUI_MODIFY_CIRCUIT_TAG, "Marker " + lineSplit[0] + ":" + x + " " + y + " " + z + " added");
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
            e.printStackTrace();
        }
        xText.setText("");
        yText.setText("");
        zText.setText("");
        idText.setText("");
        Log.d(GUI_MODIFY_CIRCUIT_TAG, "EditTexts reset");

    }

}
