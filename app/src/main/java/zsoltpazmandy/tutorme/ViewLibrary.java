package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ViewLibrary extends AppCompatActivity {

    final Functions f = new Functions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView displayTop = (TextView) findViewById(R.id.display_top);
        assert displayTop != null;

        TextView counter = (TextView) findViewById(R.id.counter);
        assert counter != null;
        try {

            String counterText = "" + f.moduleCount(getApplicationContext());

            counter.setText(counterText);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        try {
            f.getIDs(getApplicationContext());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) findViewById(R.id.library_listview);

        ArrayList<String> modulesNamesList = null;

        try {
            modulesNamesList = f.getModuleNames(getApplicationContext());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        assert modulesNamesList != null;
        final ListAdapter libraryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, modulesNamesList);

        assert listView != null;
        listView.setAdapter(libraryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String moduleSelected = String.valueOf(libraryAdapter.getItem(position));

                try {

                    JSONObject moduleSelectedJSON = f.getModuleJSON(getApplicationContext(), moduleSelected);

                    ArrayList<String> moduleInfo = new ArrayList<String>();

                    // ID of Module as a String (Right now redundant, as Names are also PKs of DB)
                    moduleInfo.add(moduleSelectedJSON.getString("ID"));

                    // ID of author of Module (to be used to retrieve User data once there's a DB
                    // with name, info, ratings & stats)
                    moduleInfo.add(moduleSelectedJSON.getString("Author"));

                    // PRO/free
                    moduleInfo.add("" + moduleSelectedJSON.getInt("PRO"));

                    // Name of module (must be unique)
                    moduleInfo.add(moduleSelectedJSON.getString("Name"));

                    // Description of module written by author
                    moduleInfo.add(moduleSelectedJSON.getString("Description"));

                    // Array of IDs of Ratings (once Ratings objects implemented, will serve
                    // to provide Ratings value (in range 1-5 to be displayed in the Module Library)
                    moduleInfo.add("" + moduleSelectedJSON.getInt("Reviews"));

                    // array of IDs of the module's trainers - to be implemented:
                    moduleInfo.add("" + moduleSelectedJSON.getInt("Trainers"));

                    moduleInfo.add("" + moduleSelectedJSON.getInt("No. of Slides"));

                    // all slides stored in the module's JSON are also copied in the arraylist
                    // might implement later to show previews of module content before in
                    // popup view. for now: not in use:
                    for (int i = 1; i <= moduleSelectedJSON.getInt("No. of Slides"); i++) {
                        moduleInfo.add(moduleSelectedJSON.getString("Slide " + i));
                    }

                    Intent showModulePop = new Intent(ViewLibrary.this, ViewLibPopUpModDisplay.class);

                    showModulePop.putStringArrayListExtra("Module Info", moduleInfo);

                    startActivityForResult(showModulePop, 1);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }


            }
        });

    }

}
















