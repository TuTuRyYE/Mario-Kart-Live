package fr.enseeiht.superjumpingsumokart.application;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.R;

/**
 * Created by Vivian on 20/02/2017.
 * Circuit adapter for the listView in GUICircuit
 */

public class CircuitAdapter extends ArrayAdapter<String[]> {

    /**
     * Position of the current circuit.
     */
    public static int selectedPos = -1;


    public CircuitAdapter(Context context, ArrayList<String[]> circuits) {
        super(context, 0, circuits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Set the layout for the View
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_circuit,parent, false);
            }

        // Set the viewHolder
            CircuitAdapter.CircuitViewHolder viewHolder= (fr.enseeiht.superjumpingsumokart.application.CircuitAdapter.CircuitViewHolder) convertView.getTag();
            if(viewHolder == null){
                viewHolder = new fr.enseeiht.superjumpingsumokart.application.CircuitAdapter.CircuitViewHolder();
                viewHolder.circuitName = (TextView) convertView.findViewById(R.id.nameCircuitView);
                viewHolder.numberLaps = (TextView) convertView.findViewById(R.id.numberLapsView);

                convertView.setTag(viewHolder);
            }

        // Get the item [position] of the listView
            String[] currentCircuit = getItem(position);

        viewHolder.circuitName.setHeight(50);
        viewHolder.circuitName.setMinHeight(50);
        viewHolder.numberLaps.setHeight(50);
        viewHolder.numberLaps.setMinHeight(50);

        // Fill the View
            viewHolder.circuitName.setText(currentCircuit[0]);
            viewHolder.numberLaps.setText(currentCircuit[1]);
            return convertView;
    }

    /**
     * ViewHolder for the CircuitAdapter
     */
    private class CircuitViewHolder{
        public TextView circuitName;
        public TextView numberLaps;
    }
}
