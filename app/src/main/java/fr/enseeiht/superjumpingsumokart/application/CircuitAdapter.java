package fr.enseeiht.superjumpingsumokart.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.R;

/**
 * Created by Vivian on 20/02/2017.
 */

public class CircuitAdapter extends ArrayAdapter<String[]> {


        public CircuitAdapter(Context context, ArrayList<String[]> circuits) {
            super(context, 0, circuits);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_circuit,parent, false);
            }

            fr.enseeiht.superjumpingsumokart.application.CircuitAdapter.CircuitViewHolder viewHolder= (fr.enseeiht.superjumpingsumokart.application.CircuitAdapter.CircuitViewHolder) convertView.getTag();
            if(viewHolder == null){
                viewHolder = new fr.enseeiht.superjumpingsumokart.application.CircuitAdapter.CircuitViewHolder();
                viewHolder.circuitName = (TextView) convertView.findViewById(R.id.nameCircuitView);
                viewHolder.numberLaps = (TextView) convertView.findViewById(R.id.numberLapsView);
                convertView.setTag(viewHolder);
            }

            //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
            String[] currentCircuit = getItem(position);

            //il ne reste plus qu'à remplir notre vue
            viewHolder.circuitName.setText(currentCircuit[0]);
            viewHolder.numberLaps.setText(currentCircuit[1]);

            return convertView;
        }

        private class CircuitViewHolder{
            public TextView circuitName;
            public TextView numberLaps;
        }
    }
