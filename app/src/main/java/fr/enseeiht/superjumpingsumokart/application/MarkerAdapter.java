package fr.enseeiht.superjumpingsumokart.application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import fr.enseeiht.superjumpingsumokart.R;

/**
 * Created by Vivian on 17/02/2017.
 */

public class MarkerAdapter extends ArrayAdapter<String[]> {



    public MarkerAdapter(Context context, ArrayList<String[]> markers) {
        super(context, 0, markers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_marker,parent, false);
        }

        MarkerViewHolder viewHolder= (MarkerViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MarkerViewHolder();
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);
            viewHolder.x = (TextView) convertView.findViewById(R.id.x);
            viewHolder.y = (TextView) convertView.findViewById(R.id.y);
            viewHolder.z = (TextView) convertView.findViewById(R.id.z);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        String[] st = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.id.setText(st[0]);
        viewHolder.x.setText(st[1]);
        viewHolder.y.setText(st[2]);
        viewHolder.z.setText(st[3]);



        return convertView;
    }

    private class MarkerViewHolder{
        public TextView id;
        public TextView x;
        public TextView y;
        public TextView z;
    }
}
