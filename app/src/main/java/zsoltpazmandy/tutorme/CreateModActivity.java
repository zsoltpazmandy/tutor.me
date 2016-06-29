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

public class CreateModActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Functions f = new Functions();

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

        Functions f = new Functions();

        if (resultCode == 1) {

            JSONObject module = null;

            try {
                module = new JSONObject(data.getStringExtra("Module complete"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            f.saveModule(getApplicationContext(), module);

            Toast.makeText(getApplicationContext(), "Module added to the library", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Module cancelled.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
