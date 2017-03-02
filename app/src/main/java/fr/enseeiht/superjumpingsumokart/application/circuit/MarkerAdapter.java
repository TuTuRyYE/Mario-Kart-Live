package fr.enseeiht.superjumpingsumokart.application.circuit;

import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.R;

/**
 *@author Vivian Guy.
 * Adapter for the ListView of GUICreateCircuit and GUIModifyCircuit.
 */

public class MarkerAdapter extends ArrayAdapter<String> {



    public MarkerAdapter(Context context, ArrayList<String> markers) {
        super(context, 0, markers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Set the layout for the View
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_marker,parent, false);
            }

        // Set the viewHolder
            MarkerViewHolder viewHolder= (MarkerViewHolder) convertView.getTag();
            if(viewHolder == null){
                viewHolder = new MarkerViewHolder();
                viewHolder.id = (TextView) convertView.findViewById(R.id.id);
                convertView.setTag(viewHolder);
            }

        viewHolder.id.setHeight(50);
        viewHolder.id.setMinHeight(50);

        // Get the item [position] of the listView
            String st = getItem(position);

        // Fill the View
            viewHolder.id.setText(st);

        return convertView;
    }

    /**
     * ViewHolder for the MarkerAdapter.
     */
    private class MarkerViewHolder{
        public TextView id;

    }
}
