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

    JSONObject user;
    boolean lang1exists = false;
    boolean lang2exists;
    boolean lang3exists;

    User u;
    TextView ageLabel;
    Spinner ageSpinner;
    TextView locationLabel;
    Spinner locationSpinner;
    TextView languages1Label;
    Spinner languages1Spinner;
    TextView languages2Label;
    Spinner languages2Spinner;
    TextView languages3Label;
    Spinner languages3Spinner;

    TextView interestsLabel;
    CheckBox languagesCheck;
    CheckBox travelCheck;
    CheckBox sportsCheck;
    CheckBox historyCheck;
    CheckBox musicCheck;
    CheckBox scienceCheck;
    CheckBox artsCheck;
    CheckBox foodCheck;
    CheckBox healthCheck;
    CheckBox computersCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        u = new User(getApplicationContext());

        user = new JSONObject();
        try {
            user = new JSONObject(getIntent().getStringExtra("User String"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setUpAge();
        setUpLocation();
        setUpLanguages();
        setUpInterests();



        Button saveProfileButt = (Button) findViewById(R.id.save_profile_butt);
        assert saveProfileButt != null;

        final JSONObject userUpdate = user;
        final boolean finalLang1exists = lang1exists;
        final boolean finalLang2exists = lang2exists;
        final boolean finalLang3exists = lang3exists;
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
                                                           ArrayList<String> interests = new ArrayList<>();

                                                           if (languagesCheck.isChecked()) {
                                                               interests.add("0");
                                                           }
                                                           if (travelCheck.isChecked()) {
                                                               interests.add("1");
                                                           }
                                                           if (sportsCheck.isChecked()) {
                                                               interests.add("2");
                                                           }
                                                           if (historyCheck.isChecked()) {
                                                               interests.add("3");
                                                           }
                                                           if (musicCheck.isChecked()) {
                                                               interests.add("4");
                                                           }
                                                           if (scienceCheck.isChecked()) {
                                                               interests.add("5");
                                                           }
                                                           if (artsCheck.isChecked()) {
                                                               interests.add("6");
                                                           }
                                                           if (foodCheck.isChecked()) {
                                                               interests.add("7");
                                                           }
                                                           if (healthCheck.isChecked()) {
                                                               interests.add("8");
                                                           }
                                                           if (computersCheck.isChecked()) {
                                                               interests.add("9");
                                                           }

                                                           try {
                                                               userUpdate.remove("Interests");
                                                               userUpdate.accumulate("Interests", "x");
                                                               userUpdate.accumulate("Interests", "x");

                                                               for (int i = 0; i < interests.size(); i++) {
                                                                   userUpdate.accumulate("Interests", interests.get(i));
                                                               }
                                                           } catch (JSONException e) {
                                                               userUpdate.accumulate("Interests", "x"); // padding from beginning with 2x, so always JSONArray
                                                               userUpdate.accumulate("Interests", "x");

                                                               for (int i = 0; i < interests.size(); i++) {
                                                                   userUpdate.accumulate("Interests", interests.get(i));
                                                               }
                                                           }

                                                       } catch (JSONException e) {
                                                           e.printStackTrace();
                                                       }

                                                       User u = new User(getApplicationContext());

                                                       if (u.saveUser(getApplicationContext(), userUpdate)) {

                                                           // storing languages information is handled from within the User class
                                                           u.setLanguages(getApplicationContext(), userUpdate, languages1Spinner.getSelectedItemPosition(), languages2Spinner.getSelectedItemPosition(), languages3Spinner.getSelectedItemPosition());
                                                           Toast.makeText(ProfileSetup.this, "Profile saved.", Toast.LENGTH_SHORT).show();
                                                           Intent homeStart = new Intent(ProfileSetup.this, Home.class);
                                                           homeStart.putExtra("User", userUpdate.toString());
                                                           startActivity(homeStart);
                                                           finish();

                                                       } else {
                                                           Toast.makeText(ProfileSetup.this, "Saving profile failed.", Toast.LENGTH_SHORT).show();

                                                       }

                                                   }
                                               }
                                           }

        );

    }



    private void setUpInterests() {

        interestsLabel = (TextView) findViewById(R.id.interests_label);
        languagesCheck = (CheckBox) findViewById(R.id.language_check);
        travelCheck = (CheckBox) findViewById(R.id.travel_check);
        sportsCheck = (CheckBox) findViewById(R.id.sports_check);
        historyCheck = (CheckBox) findViewById(R.id.history_check);
        musicCheck = (CheckBox) findViewById(R.id.music_check);
        scienceCheck = (CheckBox) findViewById(R.id.science_check);
        artsCheck = (CheckBox) findViewById(R.id.arts_check);
        foodCheck = (CheckBox) findViewById(R.id.food_check);
        healthCheck = (CheckBox) findViewById(R.id.health_check);
        computersCheck = (CheckBox) findViewById(R.id.computers_check);

        int[] checkedInterests = u.getInterests(getApplicationContext(), user);

        for (int i : checkedInterests) {
            switch (i) {
                case 1:
                    languagesCheck.setChecked(true);
                    break;
                case 2:
                    travelCheck.setChecked(true);
                    break;
                case 3:
                    sportsCheck.setChecked(true);
                    break;
                case 4:
                    historyCheck.setChecked(true);
                    break;
                case 5:
                    musicCheck.setChecked(true);
                    break;
                case 6:
                    scienceCheck.setChecked(true);
                    break;
                case 7:
                    artsCheck.setChecked(true);
                    break;
                case 8:
                    foodCheck.setChecked(true);
                    break;
                case 9:
                    healthCheck.setChecked(true);
                    break;
                case 10:
                    computersCheck.setChecked(true);
                    break;
            }
        }
    }

    private void setUpLanguages() {
        languages1Label = (TextView) findViewById(R.id.languages1_label);
        languages1Spinner = (Spinner) findViewById(R.id.languages1_spinner);
        ArrayAdapter<CharSequence> languages1SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languages1SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages1Spinner.setAdapter(languages1SpinnerAdapter);

        lang1exists = false;
        lang2exists = false;
        lang3exists = false;

        if (!u.getLanguages(getApplicationContext(), user).equals(null)) {
            lang1exists = true;
            languages1Spinner.setSelection(u.getLanguages(getApplicationContext(), user)[0]);
        }

        languages2Label = (TextView) findViewById(R.id.languages2_label);
        languages2Spinner = (Spinner) findViewById(R.id.languages2_spinner);
        ArrayAdapter<CharSequence> languages2SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languages2SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages2Spinner.setAdapter(languages2SpinnerAdapter);

        if (!u.getLanguages(getApplicationContext(), user).equals(null) &&
                !("" + u.getLanguages(getApplicationContext(), user)[1]).equals("")) {
            lang2exists = true;
            languages2Spinner.setSelection(u.getLanguages(getApplicationContext(), user)[1]);
        }

        languages3Label = (TextView) findViewById(R.id.languages3_label);
        languages3Spinner = (Spinner) findViewById(R.id.languages3_spinner);
        ArrayAdapter<CharSequence> languages3SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languages3SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages3Spinner.setAdapter(languages3SpinnerAdapter);

        if (!u.getLanguages(getApplicationContext(), user).equals(null) &&
                !("" + u.getLanguages(getApplicationContext(), user)[2]).equals("")) {
            lang3exists = true;
            languages3Spinner.setSelection(u.getLanguages(getApplicationContext(), user)[2]);
        }
    }

    private void setUpLocation() {
        locationLabel = (TextView) findViewById(R.id.location_label);
        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        ArrayAdapter<CharSequence> locationSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationSpinnerAdapter);

        try {
            if (!user.getString("Location").equals("")) {
                locationSpinner.setSelection(Integer.parseInt(user.getString("Location")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpAge() {
        ageLabel = (TextView) findViewById(R.id.age_label);
        ageSpinner = (Spinner) findViewById(R.id.age_spinner);
        ArrayAdapter<CharSequence> ageSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.ages, android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageSpinnerAdapter);

        if (u.getAge(getApplicationContext(), user) != 0) {
            ageSpinner.setSelection(u.getAge(getApplicationContext(), user));
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("Modifying")) {
            super.onBackPressed();
            Toast.makeText(this, "Profile NOT updated.", Toast.LENGTH_SHORT).show();
            Intent backHome = new Intent(ProfileSetup.this, Home.class);
            backHome.putExtra("User", user.toString());
            startActivity(backHome);
            finish();
        } else {
            Toast.makeText(ProfileSetup.this, "Please save your profile first", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
