package fr.enseeiht.superjumpingsumokart.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import fr.enseeiht.superjumpingsumokart.R;


public class GUICreateCircuit extends Activity {

    private static String GUI_CREATE_CIRCUIT_TAG = "GUICreateCircuit";

    private EditText circuitNameText;
    private EditText lapsText;
    private ListView listMarkers;
    private EditText idText;
    private EditText xText;
    private EditText yText;
    private EditText zText;
    private Button addMarkerBtn;
    private Button confirmBtn;

    private ArrayList<String[]> markers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guicreatecircuit);

        // Get the objects of the layout
        circuitNameText = (EditText) findViewById(R.id.circuitNameText);
        lapsText = (EditText) findViewById(R.id.lapsText);
        listMarkers = (ListView) findViewById(R.id.listMarkers);
        idText = (EditText) findViewById(R.id.idText);
        xText = (EditText) findViewById(R.id.xText);
        yText = (EditText) findViewById(R.id.yText);
        zText= (EditText) findViewById(R.id.zText);
        addMarkerBtn = (Button) findViewById(R.id.addMarkerBtn);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);


        markers = new ArrayList<>();

        // Adapter for the listView
        final ArrayAdapter adapter = new MarkerAdapter(GUICreateCircuit.this, markers);
        listMarkers.setAdapter(adapter);

        Log.d(GUI_CREATE_CIRCUIT_TAG, getFilesDir().getAbsolutePath());



        // Set buttons listener
            addMarkerBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Add the marker to markers list
                                Log.d(GUI_CREATE_CIRCUIT_TAG, "addMarkerBtn pressed");
                                String x = xText.getText().toString();
                                String y = yText.getText().toString();
                                String z = zText.getText().toString();
                                String id = idText.getText().toString();
                                //markers.add(new String[]{id,x,y,z});
                                adapter.add(new String[]{id,x,y,z});
                                Log.d(GUI_CREATE_CIRCUIT_TAG, "marker added to the list");
                                xText.setText("X");
                                yText.setText("Y");
                                zText.setText("Z");
                                idText.setText("ID");
                                Log.d(GUI_CREATE_CIRCUIT_TAG, "EditTexts reset");
                                Toast.makeText(GUICreateCircuit.this, "Marker added", Toast.LENGTH_SHORT).show();

                            // TODO Add the marker to the listView
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
                            Log.d(GUI_CREATE_CIRCUIT_TAG, "confirmBtn pressed");
                            createCircuitFile();
                            break;
                    }
                    return true;
                }
            });

    }

    protected void createCircuitFile () {
        String txtName = circuitNameText.getText().toString();
        String defaultName = getResources().getString(R.string.confirm_btn);
        String lapTxt = lapsText.getText().toString();
        String lapDefault = getResources().getString(R.string.lapsText);
        if (!txtName.equals(defaultName) && !txtName.isEmpty() && !lapTxt.equals(lapDefault) && !lapTxt.equals("0") && !lapTxt.isEmpty()) { // if the user has put a circuit name and a number of lap
            File circuitFile = new File(GUICreateCircuit.this.getFilesDir(), circuitNameText.getText().toString());
            FileOutputStream outputStream;
            String stringToWrite;

            try {
                // Creating the file and the instance of the circuit
                    Circuit.initInstance(Integer.parseInt(lapTxt));
                    outputStream = openFileOutput(circuitNameText.getText().toString(), MODE_APPEND);
                    String firstLine = txtName + "/" + lapTxt + "\n";
                    Circuit.getInstance().setName(txtName);
                    outputStream.write(firstLine.getBytes());
                    for (String[] s : markers) {
                        stringToWrite = s[0] + " " + s[1] + " " + s[2] + " " + s[3] + "\n";
                        outputStream.write(stringToWrite.getBytes());
                        int markerID = Integer.parseInt(s[0]);
                        double x = Double.parseDouble(s[1]);
                        double y = Double.parseDouble(s[2]);
                        double z = Double.parseDouble(s[3]);
                        Circuit.getInstance().addMarker(markerID, new Vector3D(x,y,z));
                    }
                    outputStream.close();

                // Go back to GUIWelcome
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            if (lapTxt.equals("0")) {
                Toast.makeText(GUICreateCircuit.this, "Lap number must be different to 0 !", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(GUICreateCircuit.this, "You have to put a circuit name and a lap number !", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
