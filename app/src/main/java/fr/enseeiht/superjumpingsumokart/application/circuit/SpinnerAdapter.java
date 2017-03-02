package fr.enseeiht.superjumpingsumokart.application.circuit;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * @author Vivian Guy.
 * Adapter for the the Spinner of GUICreateCircuit and GUIModifyCircuit
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    /**
     * Constructor.
     * @param theContext the context for the spinner
     * @param objects the list of objects displayed on the spinner
     * @param theLayoutResId the layout used to display the objects
     */
        public SpinnerAdapter(Context theContext, ArrayList<String> objects, int theLayoutResId) {
            super(theContext, theLayoutResId, objects);
        }

        @Override
        public int getCount() {
            // don't display last item. It is used as hint.
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }