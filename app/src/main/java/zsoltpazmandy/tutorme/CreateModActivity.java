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
        //END--------------------------------------------------------

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


                JSONObject module = new JSONObject();
                try {

                    module.put("Name", moduleNameEdit.getText().toString().trim());
                    moduleNameEdit.setEnabled(false);
                    module.put("Description", moduleDescEdit.getText().toString().trim());
                    moduleDescEdit.setEnabled(false);
                    module.put("Pro", false);

                    // populating Rev & Trainer arrays, with fake IDs for now
                    module.accumulate("Reviews", 1);
                    module.accumulate("Reviews", 2);
                    module.accumulate("Reviews", 3);
                    module.accumulate("Reviews", 4);
                    module.accumulate("Trainers", 1);
                    module.accumulate("Trainers", 2);
                    module.accumulate("Trainers", 3);
                    module.accumulate("Trainers", 4);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent startAddingSlides = new Intent(CreateModActivity.this, AddSlide.class);
                startAddingSlides.putExtra("Module frame ready", module.toString());
                startActivityForResult(startAddingSlides, 1);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {

            JSONObject module = null;

            try {
                module = new JSONObject(data.getStringExtra("Module complete"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            saveModule(module);

            Toast.makeText(this, "Module added to the library", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveModule(JSONObject module) {
        try {

            module.put("ID", moduleCount() + 1);

            updateModuleRecords(module);

            FileOutputStream fou = openFileOutput(module.get("ID").toString(), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(module.toString());
            osw.flush();
            osw.close();
            Toast.makeText(this, "Module successfully added to Library", Toast.LENGTH_SHORT).show();
            System.out.println(module.toString());
            System.out.println(getModuleRecordsJSON().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateModuleRecords(JSONObject module) throws JSONException {

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

            moduleRecordsJSON.accumulate("Modules", module);
            moduleRecordsJSON.accumulate("IDs", module.getInt("ID"));
            setModuleRecordsJSON(moduleRecordsJSON.toString());

        } catch (FileNotFoundException FNFE) {

            JSONObject newModuleRecords = new JSONObject();
            newModuleRecords.accumulate("Modules", module);
            newModuleRecords.accumulate("IDs", 1);
            setModuleRecordsJSON(newModuleRecords.toString());

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    public JSONObject getModuleRecordsJSON() throws JSONException {
        FileInputStream fileInput = null;

        JSONObject moduleRecordsJSON = new JSONObject();

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

            moduleRecordsJSON = new JSONObject(oneBigString);
            return moduleRecordsJSON;

        } catch (FileNotFoundException FNFE) {

            JSONObject newModuleRecords = new JSONObject();
            newModuleRecords.put("Modules", null);
            newModuleRecords.put("IDs", null);
            setModuleRecordsJSON(newModuleRecords.toString());
            return newModuleRecords;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return moduleRecordsJSON;
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


    public int moduleCount() throws JSONException, IOException {
        JSONObject moduleRecordsJSON = getModuleRecordsJSON();
        int amountOfModules = 0;

        try {
            amountOfModules = moduleRecordsJSON.getJSONArray("IDs").length();
        } catch (JSONException e) {
            try {
                if (moduleRecordsJSON.getInt("IDs") == 1) {
                    amountOfModules = 1;
                }
            } catch (JSONException e2) {
                setModuleRecordsJSON(moduleRecordsJSON.put("IDs", null).toString());
                amountOfModules = 0;
            }
        }

        return amountOfModules;
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
            moduleRecordsJSON.put("Modules", null);
            moduleRecordsJSON.put("IDs", null);

            FileOutputStream fou = openFileOutput("module_records", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(moduleRecordsJSON.toString());
            osw.flush();
            osw.close();
        }
    }

    public void purgeLibrary() throws IOException, JSONException {

        for (int i = 0; i <= moduleCount(); i++) {
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
