package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * The activity is launched when the user presses the Create Module button on the Training Tab.
 * The user is prompted to enter the selected name of the module of no more than 100 chatacters
 * and a maximum 1000 character long description that briefly describes the content of the module
 * and what a user enrolling could expect.
 *
 * Choosing the same name as an already existing module is disallowed, and not providing any of the
 * information is also disallowed, both fields must have some data entered.
 *
 * Pressing the Create Slides button takes the user to the AddSlide activity.
 */
public class CreateModActivity extends AppCompatActivity {

    private Cloud c;

    private ArrayList<HashMap<String, Object>> modules = null;
    private ArrayList<String> modulesNamesList = null;
    private int counterInt;

    private HashMap<String, Object> userMap = null;
    private HashMap<String, Object> moduleMap = null;
    private final int CREATE_MODULE_ADD_SLIDE = 1;

    private Button nextButt = null;
    private EditText moduleNameEdit = null;
    private EditText moduleDescEdit = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        c = new Cloud();

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        initVars();
        nextButtListener();

        AsyncGetIDforThisModule getID = new AsyncGetIDforThisModule();
        getID.execute();
    }

    private void initVars() {
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        moduleMap = new HashMap<>();

        TextView moduleNameTag = (TextView) findViewById(R.id.moduleNameTag);
        moduleNameEdit = (EditText) findViewById(R.id.moduleNameEdit);
        TextView moduleDescTag = (TextView) findViewById(R.id.moduleDescriptionTag);
        moduleDescEdit = (EditText) findViewById(R.id.moduleDescriptionEdit);

        nextButt = (Button) findViewById(R.id.moduleBeginButton);
        nextButt.setText("Next");
        nextButt.setEnabled(false);
    }

    private void nextButtListener() {
        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (moduleNameEdit.getText().length() == 0) {
                    Toast.makeText(CreateModActivity.this, "Please enter a name for your module", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isNameTaken(moduleNameEdit.getText().toString().trim())) {
                    Toast.makeText(CreateModActivity.this, "A module with that name already exists. Please choose a different name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (moduleDescEdit.getText().length() == 0) {
                    Toast.makeText(CreateModActivity.this, "Please enter a description for your module", Toast.LENGTH_SHORT).show();
                    return;
                }

                moduleNameEdit.setEnabled(false);
                moduleDescEdit.setEnabled(false);

                moduleMap.put("name", moduleNameEdit.getText().toString().trim());
                moduleMap.put("description", moduleDescEdit.getText().toString().trim());
                moduleMap.put("pro", false);

                counterInt++;
                String ID = "" + counterInt;
                String IDpadded = "";
                switch (ID.length()) {
                    case 1:
                        IDpadded = "00000" + ID;
                        break;
                    case 2:
                        IDpadded = "0000" + ID;
                        break;
                    case 3:
                        IDpadded = "000" + ID;
                        break;
                    case 4:
                        IDpadded = "00" + ID;
                        break;
                    case 5:
                        IDpadded = "0" + ID;
                        break;
                }
                moduleMap.put("id", IDpadded);
                moduleMap.put("author", userMap.get("id"));
                moduleMap.put("authorName", userMap.get("username"));

                HashMap<String, String> reviews = new HashMap<>();
                reviews.put("none", "none");

                HashMap<String, String> trainers = new HashMap<>();
                trainers.put(userMap.get("id").toString(), "true");

                HashMap<String, String> types = new HashMap<>();
                types.put("none", "none");

                moduleMap.put("reviews", reviews);
                moduleMap.put("trainers", trainers);
                moduleMap.put("typesOfSlides", types);
                moduleMap.put("noOfSlides", "0");

                Intent startAddingSlides = new Intent(CreateModActivity.this, AddSlide.class);
                startAddingSlides.putExtra("User", userMap);
                startAddingSlides.putExtra("Module", moduleMap);
                startAddingSlides.putExtra("New Module", true);
                startActivityForResult(startAddingSlides, CREATE_MODULE_ADD_SLIDE);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        moduleMap = (HashMap<String, Object>) data.getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) data.getSerializableExtra("User");

        if (requestCode == CREATE_MODULE_ADD_SLIDE) {
            if (resultCode == RESULT_OK) {
                HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                moduleMap.put("noOfSlides", "" + typesMap.keySet().size());

                c.overWriteModuleHashMapInCloud(moduleMap);
                c.addToAuthored(userMap, moduleMap);
                c.addToTraining(userMap, moduleMap.get("id").toString());

                Toast.makeText(getApplicationContext(), "Module added to the library", Toast.LENGTH_SHORT).show();
                Intent returnHome = new Intent(CreateModActivity.this, Home.class);
                returnHome.putExtra("User", userMap);
                startActivity(returnHome);

                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Adding slides to module cancelled.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Module cancelled.", Toast.LENGTH_SHORT).show();
        Intent backHome = new Intent(CreateModActivity.this, Home.class);
        backHome.putExtra("User", userMap);
        startActivity(backHome);
        finish();
    }

    private class AsyncGetIDforThisModule extends AsyncTask<String, ArrayList<HashMap<String, Object>>, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... uid) {

            final DatabaseReference modulesRoot = FirebaseDatabase.getInstance().getReference().child("/modules");
            modulesRoot.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterator i = dataSnapshot.getChildren().iterator();

                    modules = new ArrayList<>();

                    while (i.hasNext()) {

                        HashMap<String, Object> modMap = new HashMap<>();
                        DataSnapshot temp = (DataSnapshot) i.next();
                        modMap.put("name", temp.child("name").getValue().toString());
                        modMap.put("id", temp.child("id").getValue().toString());

                        modules.add(modMap);
                    }
                    publishProgress(modules);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<HashMap<String, Object>>... moduleMap) {
            super.onProgressUpdate(moduleMap);

            modulesNamesList = new ArrayList<>();
            ArrayList<String> IDsTakenList = new ArrayList<>();

            for (HashMap<String, Object> oneModule : moduleMap[0]) {
                modulesNamesList.add(oneModule.get("name").toString());
                IDsTakenList.add(oneModule.get("id").toString());
            }
            setUpFields();
        }
    }

    private boolean isNameTaken(String name) {
        for (String s : modulesNamesList) {
            if (name.equals(s))
                return true;
        }
        return false;
    }

    private void setUpFields() {
        counterInt = modules.size();
        nextButt.setEnabled(true);
    }
}
