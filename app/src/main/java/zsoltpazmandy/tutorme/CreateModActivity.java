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

public class CreateModActivity extends AppCompatActivity {

    JSONObject tempAuth2 = null;
    JSONObject authorJSON;

    private User u;

    private String name;
    private String description;
    private int pro;
    private String author;
    private ArrayList<Integer> reviews;
    private ArrayList<Integer> trainers;
    private ArrayList<Integer> typesOfSlides;
    private int noOfSlides;
    private int ID;

    private ArrayList<HashMap<String, String>> modules = null;
    private ArrayList<String> modulesNamesList = null;
    private ArrayList<String> IDsTakenList = null;
    private int counterInt;

    private HashMap<String, Object> userMap = null;
    private HashMap<String, Object> moduleMap = null;

    Button nextButt = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        u = new User(getApplicationContext());

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        final Module f = new Module();

//        try {
//            this.authorJSON = new JSONObject(getIntent().getStringExtra("User"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        moduleMap = new HashMap<>();

        TextView moduleNameTag = (TextView) findViewById(R.id.moduleNameTag);
        final EditText moduleNameEdit = (EditText) findViewById(R.id.moduleNameEdit);
        TextView moduleDescTag = (TextView) findViewById(R.id.moduleDescriptionTag);
        final EditText moduleDescEdit = (EditText) findViewById(R.id.moduleDescriptionEdit);


        Button reset = (Button) findViewById(R.id.resetLib);
        assert reset != null;
//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    f.purgeLibrary(getApplicationContext());
//                    f.resetCounter(getApplicationContext());
//                    Toast.makeText(CreateModActivity.this, "Library purged, counter reset", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        final JSONObject tempAuth = authorJSON;
//        tempAuth2 = authorJSON;

        nextButt = (Button) findViewById(R.id.moduleBeginButton);
        assert nextButt != null;
        nextButt.setText("Next");
        nextButt.setEnabled(false);
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


//                JSONObject module = new JSONObject();

//                    module.put("Name", moduleNameEdit.getText().toString().trim());
                moduleNameEdit.setEnabled(false);
//                    module.put("Description", moduleDescEdit.getText().toString().trim());
                moduleDescEdit.setEnabled(false);
//                    module.put("PRO", 0);

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
//                    module.put("ID", IDpadded);
                moduleMap.put("id", IDpadded);
                moduleMap.put("author", userMap.get("id"));
                moduleMap.put("authorName", userMap.get("username"));

//                    module.put("Author", u.getUsername(getApplicationContext(), tempAuth));
                // populating Rev & Trainer arrays, with fake IDs for now


                HashMap<String, String> reviews = new HashMap<String, String>();
                reviews.put("none", "none");

                HashMap<String, String> trainers = new HashMap<String, String>();
                trainers.put(userMap.get("id").toString(), "true");

                moduleMap.put("reviews", reviews);
                moduleMap.put("trainers", trainers);

//                    module.accumulate("Reviews", 1);
//                    int authID = authorJSON.getInt("ID");
//                    module.accumulate("Trainers", authID);


                Intent startAddingSlides = new Intent(CreateModActivity.this, AddSlide.class);
                startAddingSlides.putExtra("Module frame ready", moduleMap);
                startActivityForResult(startAddingSlides, 1);


            }
        });

        AsyncGetIDforThisModule getID = new AsyncGetIDforThisModule();
        getID.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Module f = new Module();

        if (resultCode == 1) {

//            JSONObject module = null;

//            try {
//                module = new JSONObject(data.getStringExtra("Module complete"));

                moduleMap = (HashMap<String, Object>) data.getSerializableExtra("Module complete");

                HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                moduleMap.put("noOfSlides", typesMap.keySet().size());

//                int amountOfSlides = 0;
//
//                String temp = null;
//
//                try {
//                    temp = module.getString("Types of Slides");
//                    temp = temp.replace("[", "").replace("]", "");
//
//                    amountOfSlides = temp.split(",").length;
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                module.put("No. of Slides", amountOfSlides);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


            // saving module locally:
//            f.saveModuleLocally(getApplicationContext(), module);
            // saving module in the cloud:

            Cloud c = new Cloud();
            c.saveModuleHashMapInCloud(moduleMap);
            c.addToAuthored(userMap, moduleMap);
            c.addToTraining(userMap, moduleMap.get("id").toString());

//            uploadToCloud(getApplicationContext(), moduleMap);

            // updating user data by adding to its Training field locally
//            try {
//                updateUserData(module.getString("ID"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }



            // updating user data by adding to its Training field in the cloud
//            try {
//                Cloud c = new Cloud();
//                c.addToTraining(module.getString("ID"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }



            Toast.makeText(getApplicationContext(), "Module added to the library", Toast.LENGTH_SHORT).show();
            Intent returnHome = new Intent(CreateModActivity.this, Home.class);
            returnHome.putExtra("User", userMap);
            startActivity(returnHome);

            finish();
        }
    }

//    private void updateUserData(String moduleID) {
//        u.addToTraining(getApplicationContext(), tempAuth2, moduleID);
//        try {
//            u.saveUserLocally(getApplicationContext(), authorJSON.accumulate("Authored", moduleID));
//            Cloud c = new Cloud();
//            c.addToAuthored(moduleID);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    private void uploadToCloud(Context context, JSONObject module) {
//        Cloud c = new Cloud();
//        c.saveModuleInCloud(context, module);
//
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Module cancelled.", Toast.LENGTH_SHORT).show();
        Intent backHome = new Intent(CreateModActivity.this, Home.class);
        backHome.putExtra("User", userMap);
        startActivity(backHome);
        finish();
    }

    class AsyncGetIDforThisModule extends AsyncTask<String, ArrayList<HashMap<String, String>>, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... uid) {

            final DatabaseReference modulesRoot = FirebaseDatabase.getInstance().getReference().child("/modules");
            modulesRoot.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterator i = dataSnapshot.getChildren().iterator();

                    modules = new ArrayList<HashMap<String, String>>();

                    while (i.hasNext()) {

                        HashMap<String, String> modMap = new HashMap<String, String>();

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
        protected void onProgressUpdate(ArrayList<HashMap<String, String>>... moduleMap) {
            super.onProgressUpdate(moduleMap);

            modulesNamesList = new ArrayList<>();
            IDsTakenList = new ArrayList<>();

            for (HashMap<String, String> oneModule : moduleMap[0]) {
                modulesNamesList.add(oneModule.get("name"));
                IDsTakenList.add(oneModule.get("id"));
            }
            setUpFields();
        }
    }

    public boolean isNameTaken(String name) {
        boolean isTaken = false;

        for (String s : modulesNamesList) {
            if (name.equals(s))
                isTaken = true;
        }

        return isTaken;
    }

    private void setUpFields() {
        counterInt = modules.size();
        nextButt.setEnabled(true);
    }
}
