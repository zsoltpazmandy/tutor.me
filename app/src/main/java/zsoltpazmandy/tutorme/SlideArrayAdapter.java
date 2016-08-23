package zsoltpazmandy.tutorme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * Used to build EditSelectedModule's list of slides in a module.
 *
 */
public class SlideArrayAdapter extends ArrayAdapter<String> {

    public SlideArrayAdapter(Context context, ArrayList<String> slides) {
        super(context, R.layout.slide_row, slides);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.slide_row, parent, false);

        String slide = getItem(position);

        TextView item = (TextView) view.findViewById(R.id.slide_item);
        item.setText(slide);
        return view;
    }
}
