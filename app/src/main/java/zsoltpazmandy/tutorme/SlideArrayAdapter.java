package zsoltpazmandy.tutorme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zsolt on 12/07/16.
 */
public class SlideArrayAdapter extends ArrayAdapter<String> {

    public SlideArrayAdapter(Context context, ArrayList<String> slides) {
        super(context, R.layout.slide_row, slides);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View v = li.inflate(R.layout.slide_row, parent, false);

        String slide = getItem(position);

        TextView item = (TextView) v.findViewById(R.id.slide_item);
        item.setText(slide);
        return v;
    }
}