package zsoltpazmandy.tutorme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditSelectedModule extends AppCompatActivity {

    JSONObject user = null;
    JSONObject module = null;
    Module f = new Module();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_selected_module);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User u = new User(getApplicationContext());

        try {
            user = new JSONObject(getIntent().getStringExtra("User String"));
            module = new JSONObject(getIntent().getStringExtra("Module"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView topText = (TextView) findViewById(R.id.edit_selected_module_top_hint);
        TextView slideHint = (TextView) findViewById(R.id.edit_selected_module_slides_hint);
        final Button addSlideButt = (Button) findViewById(R.id.edit_selected_module_add_slide_butt);
        final Button editSlideButt = (Button) findViewById(R.id.edit_selected_module_edit_slides_butt);
        final Button moveSlideButt = (Button) findViewById(R.id.edit_selected_module_move_slides_butt);
        final Button deleteSlideButt = (Button) findViewById(R.id.edit_selected_module_del_slide_butt);
        ListView slidesOfModuleListView = (ListView) findViewById(R.id.edit_selected_module_slideslist_view);

        try {
            assert topText != null;
            topText.setText(module.getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert editSlideButt != null;
        editSlideButt.setEnabled(false);
        assert moveSlideButt != null;
        moveSlideButt.setEnabled(false);
        assert deleteSlideButt != null;
        deleteSlideButt.setEnabled(false);

        int noOfSlides = 0;
        try {
            noOfSlides = module.getInt("No. of Slides");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<String> slidesNames = new ArrayList<>();

        for (int i = 1; i <= noOfSlides; i++) {
            try {
                slidesNames.add(module.getString("Slide " + i).substring(0, 40));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter slidesNamesAdapter = new ArrayAdapter<String>(this,
                R.layout.slide_select_custom_listview_layout,R.id.list_item, slidesNames);

        assert slidesOfModuleListView != null;
        slidesOfModuleListView.setAdapter(slidesNamesAdapter);

        slidesOfModuleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);

                addSlideButt.setEnabled(false);
                editSlideButt.setEnabled(true);
                moveSlideButt.setEnabled(true);
                deleteSlideButt.setEnabled(true);

                return false;
            }
        });
        slidesOfModuleListView.clearChoices();
}

}
