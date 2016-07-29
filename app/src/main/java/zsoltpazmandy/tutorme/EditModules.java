package zsoltpazmandy.tutorme;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class EditModules extends AppCompatActivity {

    JSONObject user = null;
    JSONObject moduleRecords = null;
    Module f = new Module();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_modules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User u = new User(getApplicationContext());

        try {
            user = new JSONObject(getIntent().getStringExtra("User String"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView topHint = (TextView) findViewById(R.id.edit_modules_top_hint_textview);
        Button doneButt = (Button) findViewById(R.id.edit_modules_butt);
        doneButt.setText("Cancel");
        assert doneButt != null;
        doneButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(EditModules.this, Home.class);
                backHome.putExtra("User", user.toString());
                startActivity(backHome);
                finish();
            }
        });

        ArrayList<Integer> usersOwnModules = u.getModulesAuthoredBy(getApplicationContext(), user);

        JSONObject currentModule = null;

        ArrayList<String> usersModulesNames = new ArrayList<>();
        for (int i = 0; i < usersOwnModules.size(); i++) {
            currentModule = f.getModuleByID(getApplicationContext(), usersOwnModules.get(i));
            try {
                usersModulesNames.add(currentModule.getString("Name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final ListAdapter modulesListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, usersModulesNames);

        ListView modulesList = (ListView) findViewById(R.id.edit_module_listview);

        assert modulesList != null;
        modulesList.setAdapter(modulesListAdapter);

        modulesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nameOfModuleSelected = String.valueOf(modulesListAdapter.getItem(position));
                JSONObject moduleToEdit = null;
                try {
                    moduleToEdit = f.getModuleByName(getApplicationContext(), nameOfModuleSelected);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent editThisModule = new Intent(EditModules.this, EditSelectedModule.class);
                editThisModule.putExtra("User String", user.toString());
                editThisModule.putExtra("Module", moduleToEdit.toString());
                startActivity(editThisModule);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent backHome = new Intent(EditModules.this, Home.class);
        backHome.putExtra("User", user.toString());
        startActivity(backHome);
        finish();
    }
}