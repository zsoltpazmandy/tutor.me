package zsoltpazmandy.tutorme;

import android.content.Context;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ViewLibrary extends AppCompatActivity {

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
        int modCount = 0;
        try {

            FileInputStream fileInput = openFileInput("module_records");
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleRecordsString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleRecordsString += read_data;
                data = new char[100];
            }

            JSONObject moduleRecordsJSON = new JSONObject(moduleRecordsString);

            modCount = moduleRecordsJSON.getInt("Modules");
            String count = "" + modCount;

            counter.setText(count);

        } catch (IOException | JSONException e1) {
            e1.printStackTrace();
        }

        ListView listView = (ListView) findViewById(R.id.library_listview);

        ArrayList<String> modulesNamesList = null;

        try {
            modulesNamesList = getModuleNames(modCount);
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

                    JSONObject moduleSelectedJSON = getModuleJSON(moduleSelected);

                    ArrayList<String> moduleInfo = new ArrayList<String>();

                    // ID of Module as a String (Right now redundant, as Names are also PKs of DB)
                    moduleInfo.add(moduleSelectedJSON.getString("ID"));

                    // ID of author of Module (to be used to retrieve User data once there's a DB
                    // with name, info, ratings & stats)
                    moduleInfo.add(moduleSelectedJSON.getString("AuthorID"));

                    // PRO/free
                    moduleInfo.add(moduleSelectedJSON.getString("PRO"));

                    // Name of module (must be unique)
                    moduleInfo.add(moduleSelectedJSON.getString("Name"));

                    // Description of module written by author
                    moduleInfo.add(moduleSelectedJSON.getString("Description"));

                    // Array of IDs of Ratings (once Ratings objects implemented, will serve
                    // to provide Ratings value (in range 1-5 to be displayed in the Module Library)
                    moduleInfo.add(moduleSelectedJSON.getJSONArray("Reviews").toString());

                    // array of IDs of the module's trainers - to be implemented:
                    moduleInfo.add(moduleSelectedJSON.getJSONArray("Trainers").toString());

                    moduleInfo.add("" + moduleSelectedJSON.getInt("No. of Slides"));

                    // all slides stored in the module's JSON are also copied in the arraylist
                    // might implement later to show previews of module content before in
                    // popup view. for now: not in use:
                    for (int i = 1; i <= moduleSelectedJSON.getInt("No. of Slides"); i++) {
                        moduleInfo.add(moduleSelectedJSON.getString("Slide #" + i));
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

    public ArrayList<String> getModuleNames(int modCount) throws IOException, JSONException {

        ArrayList<String> moduleNames = new ArrayList<>();

        for (int i = 1; i <= modCount; i++) {

            FileInputStream fileInput = openFileInput(String.valueOf(i));
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";

            int size;
            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }

            JSONObject moduleJSON = new JSONObject(moduleString);
            moduleNames.add(moduleJSON.getString("Name"));

        }
        return moduleNames;
    }

    public int moduleCount() throws JSONException, IOException {
        JSONObject moduleRecordsJSON = getModuleRecordsJSON();
        return moduleRecordsJSON.getInt("Modules");
    }

    public JSONObject getModuleRecordsJSON() throws JSONException {
        FileInputStream fileInput = null;
        try {

            fileInput = openFileInput("module_records");
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String oneBigString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                oneBigString += read_data;
                data = new char[100];
            }

            JSONObject moduleRecordsJSON = new JSONObject(oneBigString);
            return moduleRecordsJSON;

        } catch (FileNotFoundException FNFE) {

            JSONObject newModuleRecords = new JSONObject();
            newModuleRecords.put("Modules", 0);
            setModuleRecordsJSON(newModuleRecords.toString());

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return getModuleRecordsJSON();
    }

    public void setModuleRecordsJSON(String moduleRecordsString) {

        try {

            FileOutputStream fou = openFileOutput("module_records", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(moduleRecordsString);
            osw.flush();
            osw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getModuleJSON(String moduleName) throws JSONException, IOException {

        for (int i = 1; i <= moduleCount(); i++) {

            FileInputStream fileInput = openFileInput(String.valueOf(i));
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";

            int size;
            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }

            JSONObject moduleJSON = new JSONObject(moduleString);

            if (moduleJSON.getString("Name").equals(moduleName)) {
                return moduleJSON;
            }

        }

        return null;
    }

}
















