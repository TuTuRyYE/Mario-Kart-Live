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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

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
    private Button deleteMarkerBtn;

    private Integer itemSelected;

    private static ArrayList<String[]> markers;


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
        zText = (EditText) findViewById(R.id.zText);
        addMarkerBtn = (Button) findViewById(R.id.addMarkerBtn);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
        deleteMarkerBtn = (Button) findViewById(R.id.deleteMarkerBtn);



        markers = new ArrayList<>();

        // Adapter for the listView
        final ArrayAdapter adapter = new MarkerAdapter(GUICreateCircuit.this, markers);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_markers, listMarkers, false);
        listMarkers.addHeaderView(header, null, false);
        listMarkers.setAdapter(adapter);

        // Default marker
        adapter.add(new String[]{"0", "0", "0", "0"});



        // Set buttons listener
        addMarkerBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_CREATE_CIRCUIT_TAG, "addMarkerBtn pressed");
                        String x = xText.getText().toString();
                        String y = yText.getText().toString();
                        String z = zText.getText().toString();
                        String id = idText.getText().toString();
                        if (!x.equals("") && !y.equals("") && !z.equals("") && !id.equals("")) {
                            // Add the marker to markers list
                            adapter.add(new String[]{id, x, y, z});
                            Log.d(GUI_CREATE_CIRCUIT_TAG, "marker added to the list");
                            xText.setText("");
                            yText.setText("");
                            zText.setText("");
                            idText.setText("");
                            Log.d(GUI_CREATE_CIRCUIT_TAG, "EditTexts reset");
                            Toast.makeText(GUICreateCircuit.this, "Marker added", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(GUICreateCircuit.this, "You must enter marker's setting first", Toast.LENGTH_SHORT).show();
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
                        Log.d(GUI_CREATE_CIRCUIT_TAG, "confirmBtn pressed");
                        createCircuitFile();
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
                        Log.d(GUI_CREATE_CIRCUIT_TAG, "deleteMarker pressed");
                        if (itemSelected != null) {
                            if (itemSelected == 0) {
                                Toast.makeText(GUICreateCircuit.this, "You can't delete this marker !", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                adapter.remove(markers.get(itemSelected));
                                itemSelected = null;
                            }
                        }
                        else {
                            Toast.makeText(GUICreateCircuit.this, "You must select a marker first", Toast.LENGTH_SHORT).show();
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

    protected void createCircuitFile() {
        // Create the directory
        String directoryName = "Circuits";
        File CircuitsDir = new File(GUICreateCircuit.this.getFilesDir() + "/" + directoryName);
        if (!CircuitsDir.exists()) { // create the folder if it doesn't exist
            CircuitsDir.mkdir();
        }
        String path = GUICreateCircuit.this.getFilesDir() + "/" + directoryName;
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
                    // Creating the file and the instance of the circuit
                    Circuit.initInstance(Integer.parseInt(lapTxt));
                    outputStream = new FileOutputStream(circuitFile);
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
                        Circuit.getInstance().addMarker(markerID, new Vector3D(x, y, z));
                    }
                    outputStream.close();
                    Log.d(GUI_CREATE_CIRCUIT_TAG, "Circuit file created");
                    Toast.makeText(GUICreateCircuit.this, "Circuit created!", Toast.LENGTH_SHORT).show();
                    // Go back to GUIWelcome
                    Intent i = new Intent(GUICreateCircuit.this, GUIWelcome.class);
                    Log.d(GUI_CREATE_CIRCUIT_TAG, "Launching a GUIWelcom Activity...");
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else { // A circuit with the same name already exists
                Toast.makeText(GUICreateCircuit.this, "A circuit with this name already exists !", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (lapTxt.equals("0")) {
                Toast.makeText(GUICreateCircuit.this, "Lap number must be different to 0 !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GUICreateCircuit.this, "You have to put a circuit name and a lap number !", Toast.LENGTH_SHORT).show();
            }
        }

    }

    static public ArrayList<String[]> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<String[]> markers) {
        this.markers = markers;
    }
}
