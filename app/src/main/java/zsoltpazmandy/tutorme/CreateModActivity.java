package zsoltpazmandy.tutorme;

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

import java.io.IOException;
import java.util.ArrayList;

public class CreateModActivity extends AppCompatActivity {

    JSONObject tempAuth2 = null;
    JSONObject authorJSON;

    private String name;
    private String description;
    private int pro;
    private String author;
    private ArrayList<Integer> reviews;
    private ArrayList<Integer> trainers;
    private ArrayList<Integer> typesOfSlides;
    private int noOfSlides;
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final User u = new User(getApplicationContext());

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        final Module f = new Module();

        try {
            this.authorJSON = new JSONObject(getIntent().getStringExtra("User"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        TextView moduleNameTag = (TextView) findViewById(R.id.moduleNameTag);
        final EditText moduleNameEdit = (EditText) findViewById(R.id.moduleNameEdit);
        TextView moduleDescTag = (TextView) findViewById(R.id.moduleDescriptionTag);
        final EditText moduleDescEdit = (EditText) findViewById(R.id.moduleDescriptionEdit);


        Button reset = (Button) findViewById(R.id.resetLib);
        assert reset != null;
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    f.purgeLibrary(getApplicationContext());
                    f.resetCounter(getApplicationContext());
                    Toast.makeText(CreateModActivity.this, "Library purged, counter reset", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final JSONObject tempAuth = authorJSON;
        tempAuth2 = authorJSON;

        Button nextButt = (Button) findViewById(R.id.moduleBeginButton);
        assert nextButt != null;
        nextButt.setText("Next");
        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (moduleNameEdit.getText().length() == 0) {
                    Toast.makeText(CreateModActivity.this, "Please enter a name for your module", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {

                    if (f.isNameTaken(getApplicationContext(), moduleNameEdit.getText().toString().trim())) {
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
                    module.put("PRO", 0);

                    module.put("Author", u.getUsername(getApplicationContext(), tempAuth));
                    // populating Rev & Trainer arrays, with fake IDs for now
                    module.accumulate("Reviews", 1);
                    int authID = authorJSON.getInt("ID");
                    module.accumulate("Trainers", authID);


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

        Module f = new Module();

        if (resultCode == 1) {

            JSONObject module = null;

            try {
                module = new JSONObject(data.getStringExtra("Module complete"));

                int amountOfSlides = 0;

                String temp = null;

                try {
                    temp = module.getString("Types of Slides");
                    temp = temp.replace("[", "").replace("]", "");

                    amountOfSlides = temp.split(",").length;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                module.put("No. of Slides", amountOfSlides);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            f.saveModule(getApplicationContext(), module);

            saveInCloud(module);

            Toast.makeText(getApplicationContext(), "Module added to the library", Toast.LENGTH_SHORT).show();

            Intent returnHome = new Intent(CreateModActivity.this, Home.class);
            returnHome.putExtra("User", tempAuth2.toString());
            startActivity(returnHome);

            finish();
        }
    }

    private void saveInCloud(JSONObject module) {
        Cloud c = new Cloud();
        User u = new User(getApplicationContext());

        try {
            name = module.getString("Name");

            description = module.getString("Description");
            pro = module.getInt("PRO");
            author = module.getString("Author");

            reviews = new ArrayList<Integer>();
            for (int i : u.getIntfromJSON(getApplicationContext(), module, "Reviews")) {
                reviews.add(i);
            }

            trainers = new ArrayList<>();
            for (int i : u.getIntfromJSON(getApplicationContext(), module, "Trainers")) {
                trainers.add(i);
            }

            typesOfSlides = new ArrayList<>();
            for (int i : u.getIntfromJSON(getApplicationContext(), module, "Types of Slides")) {
                typesOfSlides.add(i);
            }
            noOfSlides = module.getInt("No. of Slides");
            ID = module.getInt("ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        c.saveModule(name, description, pro, author, reviews, trainers, typesOfSlides, noOfSlides, ID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Module cancelled.", Toast.LENGTH_SHORT).show();
        Intent backHome = new Intent(CreateModActivity.this, Home.class);
        backHome.putExtra("User", authorJSON.toString());
        startActivity(backHome);
        finish();
    }
}
