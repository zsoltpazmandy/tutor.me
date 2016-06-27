package zsoltpazmandy.tutorme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class CreateModActivity extends AppCompatActivity {

    ArrayList<String> module = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView moduleNameTag = (TextView) findViewById(R.id.moduleNameTag);
        final EditText moduleNameEdit = (EditText) findViewById(R.id.moduleNameEdit);
        TextView moduleDescTag = (TextView) findViewById(R.id.moduleDescriptionTag);
        final EditText moduleDescEdit = (EditText) findViewById(R.id.moduleDescriptionEdit);


        // THIS IS A TEMPORARY FEATURE: it purges the entire local DB
        Button reset = (Button) findViewById(R.id.resetLib);
        assert reset != null;
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    purgeLibrary();
                    resetCounter();
                    Toast.makeText(CreateModActivity.this, "Library purged, counter reset", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //-----------------------------

        Button nextButt = (Button) findViewById(R.id.moduleBeginButton);
        assert nextButt != null;
        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (moduleNameEdit.getText().length() == 0) {
                    Toast.makeText(CreateModActivity.this, "Please enter a name for your module", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {

                    if (isNameTaken(moduleNameEdit.getText().toString().trim())) {
                        Toast.makeText(CreateModActivity.this, "A module with that name already exists. Please choose a different name.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                if (moduleDescEdit.getText().length() == 0) {
                    Toast.makeText(CreateModActivity.this, "Please enter a description for your module", Toast.LENGTH_SHORT).show();
                    return;
                }


                try {
                    incrementModCount();
                    module.add("" + moduleCount());
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

                module.add("1");                                // AuthorID int
                module.add("0");                                // PRO      bool/ ID       int
                module.add(moduleNameEdit.getText().toString().trim());
                moduleNameEdit.setEnabled(false);
                module.add(moduleDescEdit.getText().toString().trim());
                moduleDescEdit.setEnabled(false);
                module.add("1#2#3#4");                          // stores review IDs separated by # delimiter
                module.add("1#2#3#4");                          // stores IDs of module's trainers separated by # delimiter

                Intent startAddingSlides = new Intent(CreateModActivity.this, AddSlide.class);
                startAddingSlides.putStringArrayListExtra("Module frame", module);
                startActivityForResult(startAddingSlides, 1);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {

            module.clear();
            module.addAll(data.getStringArrayListExtra("Module complete"));

            try {
                JSONObject moduleJSON = JSONify(module);
                saveModule(moduleJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }


//      UNCOMMENT, RUN & CREATE A MODULE TO ERASE EVERYTHING & RESET COUNTER

//            try {
//                purgeLibrary();
//                resetCounter();
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }

            Toast.makeText(this, "Module added to the library", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveModule(JSONObject moduleJSON) {
        try {

            FileOutputStream fou = openFileOutput(moduleJSON.get("ID").toString(), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(moduleJSON.toString());
            osw.flush();
            osw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject JSONify(ArrayList<String> module) throws JSONException {
        JSONObject moduleJSON = new JSONObject();
        moduleJSON.put("ID", Integer.parseInt(module.get(0)));
        moduleJSON.put("AuthorID", Integer.parseInt(module.get(1)));
        moduleJSON.put("PRO", Integer.parseInt(module.get(2)));
        moduleJSON.put("Name", module.get(3).trim());
        moduleJSON.put("Description", module.get(4));

        String temp = module.get(5);
        String[] revs = temp.split("#");

        for (String s : revs) {
            moduleJSON.accumulate("Reviews", Integer.parseInt(s));
        }

        temp = module.get(6);
        String[] trainers = temp.split("#");

        for (String s : trainers) {
            moduleJSON.accumulate("Trainers", Integer.parseInt(s));
        }

        int amountOfSlides = module.size() - 7;
        moduleJSON.put("No. of Slides", amountOfSlides);

        for (int i = 0; i < amountOfSlides; i++) {
            int num = i + 1;
            moduleJSON.put("Slide #" + num, module.get(7 + i));
        }
        return moduleJSON;
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

    public void incrementModCount() throws JSONException, IOException {

        int counter = moduleCount() + 1;
        JSONObject moduleRecordsJSON = getModuleRecordsJSON();
        moduleRecordsJSON.put("Modules", counter);
        setModuleRecordsJSON(moduleRecordsJSON.toString());
    }

    public int moduleCount() throws JSONException, IOException {
        JSONObject moduleRecordsJSON = getModuleRecordsJSON();
        return moduleRecordsJSON.getInt("Modules");
    }

    public boolean isNameTaken(String moduleName) throws IOException, JSONException {

        int modCount = moduleCount();

        FileInputStream fileInput = null;

        ArrayList<String> allModuleNames = new ArrayList<>();

        JSONObject moduleJSON = new JSONObject();

        for (int i = 1; i <= modCount; i++) {

            fileInput = openFileInput(String.valueOf(i));
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }

            if (moduleString.length() > 0) {
                moduleJSON = new JSONObject(moduleString);
                allModuleNames.add(moduleJSON.getString("Name"));
            }

        }

        if (moduleJSON.equals(null)) {
            return false;
        }

        return allModuleNames.contains(moduleName);
    }

    public void resetCounter() throws IOException, JSONException {

        if (moduleCount() > 0) { // it's already at zero, no need to reset

            FileInputStream fileInput = null;

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
            moduleRecordsJSON.put("Modules", 0);

            FileOutputStream fou = openFileOutput("module_records", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(moduleRecordsJSON.toString());
            osw.flush();
            osw.close();
        }
    }

    public void purgeLibrary() throws IOException, JSONException {
        for (int i = 1; i <= moduleCount(); i++) {
            FileOutputStream fou = openFileOutput("" + i, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write("");
            osw.flush();
            osw.close();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Module cancelled.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
