package zsoltpazmandy.tutorme;

import android.content.Context;
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
import java.util.List;

/**
 * Created by zsolt on 29/06/16.
 * <p/>
 * All Module management functions are found here
 */
public class Functions {

    public JSONObject getModuleRecordsJSON(Context context) throws JSONException {

        FileInputStream fileInput = null;

        JSONObject moduleRecordsJSON = new JSONObject();

        try {

            fileInput = context.openFileInput("module_records");
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String oneBigString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                oneBigString += read_data;
                data = new char[100];
            }

            // if records file is found all in order

            moduleRecordsJSON = new JSONObject(oneBigString);

        } catch (FileNotFoundException FNFE) {

            // if the file does not exist, it's created from scratch

            JSONObject newModuleRecords = new JSONObject();
            newModuleRecords.put("IDs", null);
            setModuleRecordsJSON(context, newModuleRecords.toString());
            return getModuleRecordsJSON(context);

        } catch (JSONException | IOException e) {

            // if the file exists, but there is no data found inside, it is initialised

            JSONObject newModuleRecords = new JSONObject();
            newModuleRecords.put("IDs", null);
            setModuleRecordsJSON(context, newModuleRecords.toString());
            return getModuleRecordsJSON(context);
        }
        return moduleRecordsJSON;
    }

    public JSONObject getModuleJSON(Context context, String name) throws IOException, JSONException {
        JSONObject returnObject = new JSONObject();

        FileInputStream fileInput = null;

        List<Integer> IDs = getIDs(context);

        for (int i = 0; i < moduleCount(context); i++) {

            fileInput = context.openFileInput("module" + IDs.get(i));
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }

            JSONObject currentObj = new JSONObject(moduleString);

            if (currentObj.getString("Name").equals(name)) {
                returnObject = currentObj;
            }

        }
        return returnObject;
    }

    public ArrayList<String> getModuleNames(Context context) throws IOException, JSONException {
        ArrayList<String> namesToReturn = new ArrayList<>();

        FileInputStream fileInput = null;

        for (int i = 0; i < moduleCount(context); i++) {

            fileInput = context.openFileInput("module" + getIDs(context).get(i));
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }

            JSONObject currentObj = new JSONObject(moduleString);
            namesToReturn.add(currentObj.getString("Name"));
        }
        return namesToReturn;
    }

    public List<Integer> getIDs(Context context) throws JSONException, IOException {

        ArrayList<Integer> IDsToReturn = new ArrayList<>();

        String temp;

        for (int i = 0; i < moduleCount(context); i++) {
            temp = getModuleRecordsJSON(context).getString("IDs");
            temp = temp.replace("[", "").replace("]", "");
            IDsToReturn.add(Integer.parseInt(temp.split(",")[i]));
        }

        return IDsToReturn;
    }

    public int moduleCount(Context context) throws JSONException, IOException {
        JSONObject moduleRecordsJSON = getModuleRecordsJSON(context);
        int amountOfModules = 0;

        try {
            amountOfModules = moduleRecordsJSON.getJSONArray("IDs").length();
        } catch (JSONException e) {
            try {
                if (moduleRecordsJSON.getInt("IDs") == 1) {
                    amountOfModules = 1;
                }
            } catch (JSONException e2) {
                setModuleRecordsJSON(context, moduleRecordsJSON.put("IDs", null).toString());
                amountOfModules = 0;
            }
        }

        return amountOfModules;
    }

    public void saveModule(Context context, JSONObject module) {
        try {

            module.put("ID", moduleCount(context) + 1);

            updateModuleRecords(context, module);

            FileOutputStream fou = context.openFileOutput("module" + module.get("ID").toString(), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(module.toString());
            osw.flush();
            osw.close();
            Toast.makeText(context, "Module successfully added to Library", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModuleRecordsJSON(Context context, String moduleRecordsString) {

        try {

            FileOutputStream fou = context.openFileOutput("module_records", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(moduleRecordsString);
            osw.flush();
            osw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateModuleRecords(Context context, JSONObject module) throws JSONException {

        FileInputStream fileInput = null;

        try {

            fileInput = context.openFileInput("module_records");
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
            moduleRecordsJSON.accumulate("IDs", module.getInt("ID"));
            setModuleRecordsJSON(context, moduleRecordsJSON.toString());

        } catch (FileNotFoundException FNFE) {

            JSONObject newModuleRecords = new JSONObject();
            newModuleRecords.accumulate("IDs", null);
            setModuleRecordsJSON(context, newModuleRecords.toString());

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isNameTaken(Context context, String moduleName) throws IOException, JSONException {

        FileInputStream fileInput = null;

        ArrayList<String> allModuleNames = new ArrayList<>();

        JSONObject currentModule = new JSONObject();

        List<Integer> IDs = getIDs(context);

        for (int i = 0; i < moduleCount(context); i++) {

            fileInput = context.openFileInput("module" + IDs.get(i));
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
                currentModule = new JSONObject(moduleString);
                allModuleNames.add(currentModule.getString("Name"));
            }

        }

        return !currentModule.equals(null) && allModuleNames.contains(moduleName);
    }

    public void purgeLibrary(Context context) throws IOException, JSONException {

        List<Integer> IDs = getIDs(context);

        for (int i = 0; i < moduleCount(context); i++) {
            FileOutputStream fou = context.openFileOutput("module" + IDs.get(i), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write("");
            osw.flush();
            osw.close();
        }
    }

    public void resetCounter(Context context) throws IOException, JSONException {

        if (moduleCount(context) > 0) { // it's already at zero, no need to reset
            setModuleRecordsJSON(context, "");
        }
    }

}
