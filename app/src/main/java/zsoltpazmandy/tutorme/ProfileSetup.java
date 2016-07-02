package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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
        Spinner ageSpinner = (Spinner) findViewById(R.id.age_spinner);
        ArrayAdapter<CharSequence> ageSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.ages, android.R.layout.simple_spinner_dropdown_item);
        ageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageSpinnerAdapter);
// LOCATION
        TextView locationLabel = (TextView) findViewById(R.id.location_label);
        Spinner locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        ArrayAdapter<CharSequence> locationSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_dropdown_item);
        locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationSpinnerAdapter);
// LANGUAGES
        TextView languagesLabel = (TextView) findViewById(R.id.languages_label);
        Spinner languagesSpinner = (Spinner) findViewById(R.id.languages_spinner);
        ArrayAdapter<CharSequence> languagesSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languagesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagesSpinner.setAdapter(languagesSpinnerAdapter);
// INTERESTS
        TextView interestsLabel = (TextView) findViewById(R.id.interests_label);
        CheckBox languagesCheck = (CheckBox) findViewById(R.id.language_check);
        CheckBox travelCheck = (CheckBox) findViewById(R.id.travel_check);
        CheckBox sportsCheck = (CheckBox) findViewById(R.id.sports_check);
        CheckBox historyCheck = (CheckBox) findViewById(R.id.history_check);
        CheckBox musicCheck = (CheckBox) findViewById(R.id.music_check);
        CheckBox scienceCheck = (CheckBox) findViewById(R.id.science_check);
        CheckBox artsCheck = (CheckBox) findViewById(R.id.arts_check);
        CheckBox foodCheck = (CheckBox) findViewById(R.id.food_check);
        CheckBox healthCheck = (CheckBox) findViewById(R.id.health_check);
        CheckBox computersCheck = (CheckBox) findViewById(R.id.computers_check);
    }

}
