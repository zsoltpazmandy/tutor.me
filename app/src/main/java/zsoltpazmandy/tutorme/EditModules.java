package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class EditModules extends AppCompatActivity {

    JSONObject user = null;
    private ArrayList<String> IDsAuthoredByThisUser = null;
    private ArrayList<HashMap<String, Object>> myModules = null;
    private ListAdapter modulesListAdapter = null;
    private ListView modulesList = null;
    private ArrayList<String> NamesOfModsAuthoredByThis = null;
    AsyncGetMyModules getModules = null;
    private ArrayList<String> allModuleNames = null;

    private HashMap<String, Object> userMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_modules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IDsAuthoredByThisUser = new ArrayList<>();
        NamesOfModsAuthoredByThis = new ArrayList<>();

        allModuleNames = new ArrayList<>();

        User u = new User(getApplicationContext());

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        TextView topHint = (TextView) findViewById(R.id.edit_modules_top_hint_textview);
        Button doneButt = (Button) findViewById(R.id.edit_modules_butt);
        doneButt.setText("Cancel");
        assert doneButt != null;
        doneButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(EditModules.this, Home.class);
                backHome.putExtra("User", userMap);
                startActivity(backHome);
                finish();
            }
        });

        getMyAuthoredNames();
        getModules = new AsyncGetMyModules();
        getModules.execute();

    }

    private void getMyAuthoredNames() {
//        String raw = "";
//        boolean isArray = false;
//        try {
//            raw = user.getString("Authored");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        String[] temp = new String[1];
//
//        if (raw.contains(",")) {
//            temp = raw.split(",");
//            isArray = true;
//        } else {
//            temp[0] = raw;
//        }
//
//        for (String s : temp) {
//            if (isArray) {
//                s = s.replace("[", "").replace("]", "").replace(",", "");
//                s = s.substring(1, s.length() - 1);
//            }
//            IDsAuthoredByThisUser.add(s);
//        }
        HashMap<String, String> authoredMap = (HashMap<String, String>) userMap.get("authored");
        Set<String> IDset = authoredMap.keySet();
        IDsAuthoredByThisUser.addAll(IDset);

        for (String s : IDset) {
            NamesOfModsAuthoredByThis.add(authoredMap.get(s));
        }


    }

    private void setUpList() {

        modulesListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, NamesOfModsAuthoredByThis);

        modulesList = (ListView) findViewById(R.id.edit_module_listview);

        assert modulesList != null;
        modulesList.setAdapter(modulesListAdapter);

    }

    private void setUpListener() {
        modulesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent editThisModule = new Intent(EditModules.this, EditSelectedModule.class);
                editThisModule.putExtra("User", userMap);
                editThisModule.putExtra("Module", myModules.get(position));
                editThisModule.putExtra("All Module Names", allModuleNames);
                startActivity(editThisModule);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent backHome = new Intent(EditModules.this, Home.class);
        backHome.putExtra("User", userMap);
        startActivity(backHome);
        finish();
    }

    class AsyncGetMyModules extends AsyncTask<String, ArrayList<HashMap<String, Object>>, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... uid) {

            final DatabaseReference modulesRoot = FirebaseDatabase.getInstance().getReference().child("/modules");
            modulesRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    myModules = new ArrayList<HashMap<String, Object>>();

                    for (String s : IDsAuthoredByThisUser) {
                        HashMap<String, Object> modMap = new HashMap<String, Object>();

                        modMap.put("name", dataSnapshot.child(s).child("name").getValue().toString());
                        modMap.put("description", dataSnapshot.child(s).child("description").getValue().toString());
                        modMap.put("id", dataSnapshot.child(s).child("id").getValue().toString());
                        modMap.put("author", dataSnapshot.child(s).child("author").getValue().toString());
                        modMap.put("pro", dataSnapshot.child(s).child("pro").getValue().toString());
                        modMap.put("noOfSlides", dataSnapshot.child(s).child("noOfSlides").getValue().toString());
                        modMap.put("authorName", dataSnapshot.child(s).child("authorName").getValue().toString());

                        HashMap<String, String> typeMap = new HashMap<String, String>();
                        for (int i = 1; i <= Integer.parseInt(dataSnapshot.child(s).child("noOfSlides").getValue().toString()); i++) {
                            typeMap.put("Slide_" + i, dataSnapshot.child(s).child("typesOfSlides").child("Slide_" + i).getValue().toString());
                        }
                        modMap.put("typesOfSlides", typeMap);

                        for (int i = 1; i <= Integer.parseInt(dataSnapshot.child(s).child("noOfSlides").getValue().toString()); i++) {
                            modMap.put("Slide_" + i, dataSnapshot.child(s).child("Slide_" + i).getValue().toString());
                        }

                        myModules.add(modMap);
                    }

                    Iterator i = dataSnapshot.getChildren().iterator();
                    while (i.hasNext()) {
                        DataSnapshot current = (DataSnapshot) i.next();
                        allModuleNames.add(current.child("name").getValue().toString());
                    }

                    HashMap<String, Object> temp = new HashMap<String, Object>();
                    temp.put("TEMP", allModuleNames);

                    myModules.add(temp);

                    publishProgress(myModules);
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

            HashMap<String, Object> temp = new HashMap<String, Object>();

            NamesOfModsAuthoredByThis = new ArrayList<>();
            for (HashMap<String, Object> oneMod : moduleMap[0]) {
                if (oneMod.containsKey("TEMP")) {
                    temp = oneMod;
                    break;
                }
                NamesOfModsAuthoredByThis.add(oneMod.get("name").toString());
            }

            allModuleNames = (ArrayList<String>) temp.get("TEMP");

            setUpList();
            setUpListener();

        }

    }

}