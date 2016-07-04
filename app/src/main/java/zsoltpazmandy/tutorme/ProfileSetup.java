package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent userIntent = getIntent();

        JSONObject user = new JSONObject();

        try {

            user = new JSONObject(getIntent().getStringExtra("User String"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


// USERNAME
        TextView usernameLabel = (TextView) findViewById(R.id.username_label);
        TextView usernameField = (TextView) findViewById(R.id.username_field);
        try {
            assert usernameField != null;
            usernameField.setText(user.getString("Username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        usernameField.setEnabled(false);
// AGE
        TextView ageLabel = (TextView) findViewById(R.id.age_label);
        final Spinner ageSpinner = (Spinner) findViewById(R.id.age_spinner);
        ArrayAdapter<CharSequence> ageSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.ages, android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageSpinnerAdapter);

// LOCATION
        TextView locationLabel = (TextView) findViewById(R.id.location_label);
        final Spinner locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        ArrayAdapter<CharSequence> locationSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationSpinnerAdapter);
        locationSpinner.setAdapter(locationSpinnerAdapter);

// LANGUAGES
        TextView languages1Label = (TextView) findViewById(R.id.languages1_label);
        final Spinner languages1Spinner = (Spinner) findViewById(R.id.languages1_spinner);
        ArrayAdapter<CharSequence> languages1SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languages1SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages1Spinner.setAdapter(languages1SpinnerAdapter);

        TextView languages2Label = (TextView) findViewById(R.id.languages2_label);
        final Spinner languages2Spinner = (Spinner) findViewById(R.id.languages2_spinner);
        ArrayAdapter<CharSequence> languages2SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languages2SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages2Spinner.setAdapter(languages2SpinnerAdapter);

        TextView languages3Label = (TextView) findViewById(R.id.languages3_label);
        final Spinner languages3Spinner = (Spinner) findViewById(R.id.languages3_spinner);
        ArrayAdapter<CharSequence> languages3SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languages3SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages3Spinner.setAdapter(languages3SpinnerAdapter);

// INTERESTS
        TextView interestsLabel = (TextView) findViewById(R.id.interests_label);
        final CheckBox languagesCheck = (CheckBox) findViewById(R.id.language_check);
        final CheckBox travelCheck = (CheckBox) findViewById(R.id.travel_check);
        final CheckBox sportsCheck = (CheckBox) findViewById(R.id.sports_check);
        final CheckBox historyCheck = (CheckBox) findViewById(R.id.history_check);
        final CheckBox musicCheck = (CheckBox) findViewById(R.id.music_check);
        final CheckBox scienceCheck = (CheckBox) findViewById(R.id.science_check);
        final CheckBox artsCheck = (CheckBox) findViewById(R.id.arts_check);
        final CheckBox foodCheck = (CheckBox) findViewById(R.id.food_check);
        final CheckBox healthCheck = (CheckBox) findViewById(R.id.health_check);
        final CheckBox computersCheck = (CheckBox) findViewById(R.id.computers_check);

// SAVE PROFILE BUTTON
        Button saveProfileButt = (Button) findViewById(R.id.save_profile_butt);
        assert saveProfileButt != null;

        final JSONObject userUpdate = user;

        saveProfileButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationSpinner.getSelectedItemPosition() == 0 || languages1Spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(ProfileSetup.this, "Some of the required fields are missing", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // SAVING PROFILE

                    try {
                        userUpdate.put("Age", ageSpinner.getSelectedItemPosition());
                        userUpdate.put("Location", locationSpinner.getSelectedItemPosition());
                        userUpdate.accumulate("Languages", "x"); // padding from beginning with "x", so it's always a JSONArray
                        userUpdate.accumulate("Languages", languages1Spinner.getSelectedItemPosition());
                        if(languages2Spinner.getSelectedItemPosition()!=0) {
                            userUpdate.accumulate("Languages", languages2Spinner.getSelectedItemPosition());
                        }
                        if(languages3Spinner.getSelectedItemPosition()!=0) {
                            userUpdate.accumulate("Languages", languages3Spinner.getSelectedItemPosition());
                        }

                        ArrayList<String> interests = new ArrayList<>();

                        if(languagesCheck.isChecked()){
                            interests.add("0");
                        }
                        if(travelCheck.isChecked()){
                            interests.add("1");
                        }
                        if(sportsCheck.isChecked()){
                            interests.add("2");
                        }
                        if(historyCheck.isChecked()){
                            interests.add("3");
                        }
                        if(musicCheck.isChecked()){
                            interests.add("4");
                        }
                        if(scienceCheck.isChecked()){
                            interests.add("5");
                        }
                        if(artsCheck.isChecked()){
                            interests.add("6");
                        }
                        if(foodCheck.isChecked()){
                            interests.add("7");
                        }
                        if(healthCheck.isChecked()){
                            interests.add("8");
                        }
                        if(computersCheck.isChecked()){
                            interests.add("9");
                        }

                        userUpdate.accumulate("Interests", "x"); // padding from beginning with 2x, so always JSONArray
                        userUpdate.accumulate("Interests", "x");

                        for(int i = 0; i < interests.size(); i++){
                            userUpdate.accumulate("Interests", interests.get(i));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    User u = new User(getApplicationContext());

                    if(u.saveUser(getApplicationContext(), userUpdate)){
                        Toast.makeText(ProfileSetup.this, "Profile saved.", Toast.LENGTH_SHORT).show();
                        Intent homeStart = new Intent(ProfileSetup.this, Home.class);
                        homeStart.putExtra("User",userUpdate.toString());
                        startActivity(homeStart);
                        finish();

                    } else{
                        Toast.makeText(ProfileSetup.this, "Saving profile failed.", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

    }


}
