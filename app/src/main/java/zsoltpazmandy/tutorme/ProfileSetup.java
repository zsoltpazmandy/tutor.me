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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileSetup extends AppCompatActivity {

    boolean lang1exists = false;
    boolean lang2exists;
    boolean lang3exists;

    User u = null;

    private boolean firstSetup;

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

    private HashMap<String, Object> userMap = null;

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

        if (getIntent().hasExtra("Modifying")) {
            firstSetup = false;
        } else {
            firstSetup = true;
        }

        u = new User();

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        setUpAge();
        setUpLocation();
        setUpLanguages();
        setUpInterests();


        Button saveProfileButt = (Button) findViewById(R.id.save_profile_butt);
        assert saveProfileButt != null;

        final boolean finalLang1exists = lang1exists;
        final boolean finalLang2exists = lang2exists;
        final boolean finalLang3exists = lang3exists;
        saveProfileButt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locationSpinner.getSelectedItemPosition() == 0 || languages1Spinner.getSelectedItemPosition() == 0) {
                            Toast.makeText(ProfileSetup.this, "Some of the required fields are missing", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            String id = mAuth.getCurrentUser().getUid();

                            if (!getIntent().hasExtra("Modifying")) {
                                userMap = u.buildUserHashMap(
                                        id,
                                        userMap.get("username").toString(),
                                        userMap.get("email").toString(),
                                        String.valueOf(ageSpinner.getSelectedItemPosition()),
                                        u.decodeLanguage(languages1Spinner.getSelectedItemPosition()),
                                        u.decodeLanguage(languages2Spinner.getSelectedItemPosition()),
                                        u.decodeLanguage(languages3Spinner.getSelectedItemPosition()),
                                        u.decodeCountry(locationSpinner.getSelectedItemPosition()));
                            } else {
                                userMap.put("language1", u.decodeLanguage(languages1Spinner.getSelectedItemPosition()));
                                userMap.put("language2", u.decodeLanguage(languages2Spinner.getSelectedItemPosition()));
                                userMap.put("language3", u.decodeLanguage(languages3Spinner.getSelectedItemPosition()));
                                userMap.put("age", String.valueOf(ageSpinner.getSelectedItemPosition()));
                                userMap.put("location", u.decodeCountry(locationSpinner.getSelectedItemPosition()));
                            }

                            ArrayList<String> interests = new ArrayList<String>();

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
                            userMap.put("interests", interests);

                            Toast.makeText(ProfileSetup.this, "Profile saved.", Toast.LENGTH_SHORT).show();
                            Intent homeStart = new Intent(ProfileSetup.this, Home.class);
                            Cloud c = new Cloud();
                            c.saveUserHashMapInCloud(userMap);
                            homeStart.putExtra("User", userMap);
                            startActivity(homeStart);
                            finish();

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

        ArrayList<String> interests = (ArrayList<String>) userMap.get("interests");
        for (String s : interests) {
            switch (s) {
                case "0":
                    languagesCheck.setChecked(true);
                    break;
                case "1":
                    travelCheck.setChecked(true);
                    break;
                case "2":
                    sportsCheck.setChecked(true);
                    break;
                case "3":
                    historyCheck.setChecked(true);
                    break;
                case "4":
                    musicCheck.setChecked(true);
                    break;
                case "5":
                    scienceCheck.setChecked(true);
                    break;
                case "6":
                    artsCheck.setChecked(true);
                    break;
                case "7":
                    foodCheck.setChecked(true);
                    break;
                case "8":
                    healthCheck.setChecked(true);
                    break;
                case "9":
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

        if (!firstSetup) {
            for (int i = 0; i < languages1SpinnerAdapter.getCount(); i++) {
                if (u.decodeLanguage(i).equals(userMap.get("language1")))
                    languages1Spinner.setSelection(i);
            }
        }

        languages2Label = (TextView) findViewById(R.id.languages2_label);
        languages2Spinner = (Spinner) findViewById(R.id.languages2_spinner);
        ArrayAdapter<CharSequence> languages2SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languages2SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages2Spinner.setAdapter(languages2SpinnerAdapter);

        if (userMap.containsKey("language2"))
            if (!userMap.get("language2").equals("")) {
                for (int i = 0; i < languages2SpinnerAdapter.getCount(); i++) {
                    if (u.decodeLanguage(i).equals(userMap.get("language2")))
                        languages2Spinner.setSelection(i);
                }
            }

        languages3Label = (TextView) findViewById(R.id.languages3_label);
        languages3Spinner = (Spinner) findViewById(R.id.languages3_spinner);
        ArrayAdapter<CharSequence> languages3SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languages3SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages3Spinner.setAdapter(languages3SpinnerAdapter);

        if (userMap.containsKey("language3"))
            if (!userMap.get("language3").equals("")) {
                for (int i = 0; i < languages3SpinnerAdapter.getCount(); i++) {
                    if (u.decodeLanguage(i).equals(userMap.get("language3")))
                        languages3Spinner.setSelection(i);
                }
            }
    }

    private void setUpLocation() {
        locationLabel = (TextView) findViewById(R.id.location_label);
        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        ArrayAdapter<CharSequence> locationSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationSpinnerAdapter);

        if (!firstSetup) {
            for (int i = 0; i < locationSpinnerAdapter.getCount(); i++) {
                if (u.decodeCountry(i).equals(userMap.get("location"))) {
                    locationSpinner.setSelection(i);
                }
            }
        }
    }

    private void setUpAge() {
        ageLabel = (TextView) findViewById(R.id.age_label);
        ageSpinner = (Spinner) findViewById(R.id.age_spinner);
        ArrayAdapter<CharSequence> ageSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.ages, android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageSpinnerAdapter);

        if (userMap.containsKey("age"))
            if (!userMap.get("age").equals("")) {
                ageSpinner.setSelection(Integer.parseInt(userMap.get("age").toString()));
            }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("Modifying")) {
            super.onBackPressed();
            Toast.makeText(this, "Profile NOT updated.", Toast.LENGTH_SHORT).show();
            Intent backHome = new Intent(ProfileSetup.this, Home.class);
            backHome.putExtra("User", userMap);
            startActivity(backHome);
            finish();
        } else {
            Toast.makeText(ProfileSetup.this, "Please save your profile first", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
