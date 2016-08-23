package zsoltpazmandy.tutorme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * The activity displays a list of all the modules available in the Module Library and a counter
 * on the top of the screen. It uses an asynchronous task to read a snapshot of the database.
 *
 */
public class ViewLibrary extends AppCompatActivity {

    private ArrayList<HashMap<String, Object>> modules;
    private ArrayList<String> modulesNamesList;
    private ListView listView;
    private ListAdapter libraryAdapter;
    private int counterInt;
    private HashMap<String, Object> userMap;
    private HashMap<String, Object> moduleMap;
    private TextView displayTop;
    private AsyncGetModules getMods;
    private TextView counter;
    private String counterText;
    private ArrayList<String> moduleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        displayTop = (TextView) findViewById(R.id.display_top);
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        listView = (ListView) findViewById(R.id.library_listview);
        getMods = new AsyncGetModules();
        getMods.execute();
    }

    private void setUpList() {
        counter = (TextView) findViewById(R.id.counter);
        counterText = "" + counterInt;
        counter.setText(counterText);
        libraryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, modulesNamesList);
        listView.setAdapter(libraryAdapter);
    }

    private void setUpListenerOnItems() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moduleInfo = new ArrayList<String>();

                // ID of Module as a String (Right now redundant, as Names are also PKs of DB)
                moduleInfo.add(modules.get(position).get("id").toString());

                // ID of authorJSON of Module (to be used to retrieve User data once there's a DB
                // with name, info, ratings & stats)
                moduleInfo.add(modules.get(position).get("author").toString());

                // PRO/free
                moduleInfo.add(modules.get(position).get("pro").toString());

                // Name of module (must be unique)
                moduleInfo.add(modules.get(position).get("name").toString());

                // Description of module written by authorJSON
                moduleInfo.add(modules.get(position).get("description").toString());

                // Array of IDs of Ratings (once Ratings objects implemented, will serve
                // to provide Ratings value (in range 1-5 to be displayed in the Module Library)
                moduleInfo.add("" + 1);

                // array of IDs of the module's trainers - to be implemented:
                moduleInfo.add("" + 1);

                moduleInfo.add(modules.get(position).get("noOfSlides").toString());

                Intent showModulePop = new Intent(ViewLibrary.this, ViewLibPopUpModDisplay.class);
                showModulePop.putExtra("User", userMap);
                showModulePop.putExtra("Module", modules.get(position));
                showModulePop.putStringArrayListExtra("Module Info", moduleInfo);
                startActivityForResult(showModulePop, 1);
            }
        });
    }

    class AsyncGetModules extends AsyncTask<String, ArrayList<HashMap<String, Object>>, String> {

        @Override
        protected String doInBackground(String... uid) {

            final DatabaseReference modulesRoot = FirebaseDatabase.getInstance().getReference().child("/modules");
            modulesRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterator i = dataSnapshot.getChildren().iterator();

                    modules = new ArrayList<HashMap<String, Object>>();
                    while (i.hasNext()) {

                        HashMap<String, Object> currentMod = new HashMap<String, Object>();

                        DataSnapshot temp = (DataSnapshot) i.next();

                        currentMod.put("name", temp.child("name").getValue().toString());
                        currentMod.put("description", temp.child("description").getValue().toString());
                        currentMod.put("id", temp.child("id").getValue().toString());
                        currentMod.put("author", temp.child("author").getValue().toString());
                        currentMod.put("pro", temp.child("pro").getValue().toString());
                        currentMod.put("author", temp.child("author").getValue().toString());
                        currentMod.put("noOfSlides", temp.child("noOfSlides").getValue().toString());
                        currentMod.put("authorName", temp.child("authorName").getValue().toString());

                        for (int j = 1; j <= Integer.parseInt(temp.child("noOfSlides").getValue().toString()); j++) {
                            currentMod.put("Slide_" + j, temp.child("Slide_" + j).getValue().toString());
                        }

                        GenericTypeIndicator<Map<String, String>> reviewMapGen = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> reviewMap = temp.child("reviews").getValue(reviewMapGen);

                        currentMod.put("reviews",reviewMap);
                        GenericTypeIndicator<Map<String, String>> trainersMapGen = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> trainersMap = temp.child("trainers").getValue(trainersMapGen);

                        currentMod.put("trainers",trainersMap);
                        GenericTypeIndicator<Map<String, String>> typesMapGen = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> typesMap = temp.child("typesOfSlides").getValue(typesMapGen);

                        currentMod.put("typesOfSlides",typesMap);

                        modules.add(currentMod);
                    }
                    publishProgress(modules);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    publishProgress(modules);
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<HashMap<String, Object>>... moduleMap) {
            super.onProgressUpdate(moduleMap);

            modulesNamesList = new ArrayList<>();

            for (HashMap<String, Object> oneModule : moduleMap[0]) {
                modulesNamesList.add(oneModule.get("name").toString());
            }
            counterInt = modules.size();
            setUpList();
            setUpListenerOnItems();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            userMap = (HashMap<String, Object>) data.getSerializableExtra("User");
            moduleMap = (HashMap<String, Object>) data.getSerializableExtra("Module");
            // asking if user wants to begin the newly added module
            AlertDialog.Builder alert = new AlertDialog.Builder(ViewLibrary.this);
            alert.setTitle("Begin new module now?");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent startLearning = new Intent(ViewLibrary.this, ModuleProgress.class);
                    startLearning.putExtra("User", userMap);
                    startLearning.putExtra("Module", moduleMap);
                    startActivity(startLearning);
                    finish();
                    dialog.dismiss();
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent returnToHome = new Intent(ViewLibrary.this, Home.class);
        returnToHome.putExtra("User", userMap);

        startActivity(returnToHome);
        finish();
    }
}
