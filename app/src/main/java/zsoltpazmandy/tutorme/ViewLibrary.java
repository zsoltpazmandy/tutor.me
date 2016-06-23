package zsoltpazmandy.tutorme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

        ArrayList<String> modulesNamesList = new ArrayList<>();

        try {
            modulesNamesList = getModuleNames(modCount);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        ListAdapter libraryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, modulesNamesList);

        assert listView != null;
        listView.setAdapter(libraryAdapter);

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
}
















