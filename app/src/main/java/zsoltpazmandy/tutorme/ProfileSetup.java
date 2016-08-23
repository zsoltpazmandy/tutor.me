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


/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 *
 * This activity is used in two different ways within the application:
 *
 *      1. at first-time registration: in order to complete the existing user directory-stub in
 *          the database which initially only contains the users's unique ID, username and email address.
 *          In this case the present class is responsible for the completion of the userprofile in the
 *          online database using an instance of the Cloud class.
 *          This use of the activity is marked with no extra on the incoming intent, (i.e. the
 *          lack of the "Modifying" extra), which triggers the firstSetup boolean.
 *          An instance of the User class is used:
 *              1. to create a HashMap of the user's data (aka userMap)
 *              2. to perform certain operations on displayed data (e.g. decoding language and
 *              location information: they are stored as integers, and User class's decode* functions
 *              help translating them to intelligible data; see User.java)
 *
 *      2. any time an existing user wishes to edit their profile data, they are sent to this activity
 *          from the main Home screen of the application. This means that at this stage the user
 *          must have used this activity at least once already. This use of the activity is marked
 *          using the "Modyfying" extra on the incoming intent, which triggers the firstSetup boolean.
 *          If the activity is accessed by an already set up user account, the previously saved
 *          information is loaded up and the fields (spinners) are populated respectively.
 *
 *  The user's profile is set up by gathering some required and some optional information.
 *      Required info: First Language & Location. These are required in order for the system to
 *      be able to more accurately link users by taking into account whether or not they have at least
 *      one common language and whether their timezones are more or less close, so as to facilitate
 *      seamless communication between them.
 *
 *      Optional fields: further languages spoken, age and interests. These are there to further
 *      refine the algorithm that links users, other than purely based on common language and
 *      location information, based on their common preferences as well.
 *
 *      The ProfileSetup activity cannot be left unless the required information is set to
 *      anything other than the base values i.e. [please select]
 *
 *  Upon successful completion of fields, the user is taken to the main Home activity of the application
 *  by pressing the Save button
 */
public class ProfileSetup extends AppCompatActivity {

    private boolean firstSetup;
    private User user;
    private Cloud cloud;
    private TextView ageLabel;
    private Spinner ageSpinner;
    private TextView locationLabel;
    private Spinner locationSpinner;
    private TextView languages1Label;
    private Spinner languages1Spinner;
    private TextView languages2Label;
    private Spinner languages2Spinner;
    private TextView languages3Label;
    private Spinner languages3Spinner;
    private TextView interestsLabel;
    private CheckBox languagesCheck;
    private CheckBox travelCheck;
    private CheckBox sportsCheck;
    private CheckBox historyCheck;
    private CheckBox musicCheck;
    private CheckBox scienceCheck;
    private CheckBox artsCheck;
    private CheckBox foodCheck;
    private CheckBox healthCheck;
    private CheckBox computersCheck;

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

        // triggering firstSetup if no "Modifying" extra
        if (getIntent().hasExtra("Modifying")) {
            firstSetup = false;
        } else {
            firstSetup = true;
        }

        user = new User();
        cloud = new Cloud();

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        setUpAge();
        setUpLocation();
        setUpLanguages();
        setUpInterests();
        addSaveProfileButtListener();
    }

    private void addSaveProfileButtListener() {
        Button saveProfileButt = (Button) findViewById(R.id.save_profile_butt);
        saveProfileButt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // checking whether the required fields have been set
                        if (locationSpinner.getSelectedItemPosition() == 0 || languages1Spinner.getSelectedItemPosition() == 0) {
                            Toast.makeText(ProfileSetup.this, "Some of the required fields are missing", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            String id = mAuth.getCurrentUser().getUid();

                            if (firstSetup) {
                                userMap = user.buildUserHashMap(
                                        id,
                                        userMap.get("username").toString(),
                                        userMap.get("email").toString(),
                                        String.valueOf(ageSpinner.getSelectedItemPosition()),
                                        user.decodeLanguage(languages1Spinner.getSelectedItemPosition()),
                                        user.decodeLanguage(languages2Spinner.getSelectedItemPosition()),
                                        user.decodeLanguage(languages3Spinner.getSelectedItemPosition()),
                                        user.decodeCountry(locationSpinner.getSelectedItemPosition()));
                            } else {
                                userMap.put("language1", user.decodeLanguage(languages1Spinner.getSelectedItemPosition()));
                                userMap.put("language2", user.decodeLanguage(languages2Spinner.getSelectedItemPosition()));
                                userMap.put("language3", user.decodeLanguage(languages3Spinner.getSelectedItemPosition()));
                                userMap.put("age", String.valueOf(ageSpinner.getSelectedItemPosition()));
                                userMap.put("location", user.decodeCountry(locationSpinner.getSelectedItemPosition()));
                            }

                            // list of interest codes stored as integers (need to be decoded if visualised)
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
                            cloud.saveUserHashMapInCloud(userMap);
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


        // populating interest fields if such data has ever been registered
        try {
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
        } catch (NullPointerException e){

        }
    }


    private void setUpLanguages() {
        languages1Label = (TextView) findViewById(R.id.languages1_label);
        languages1Spinner = (Spinner) findViewById(R.id.languages1_spinner);
        ArrayAdapter<CharSequence> languages1SpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        languages1SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages1Spinner.setAdapter(languages1SpinnerAdapter);

        if (!firstSetup) {
            for (int i = 0; i < languages1SpinnerAdapter.getCount(); i++) {
                if (user.decodeLanguage(i).equals(userMap.get("language1")))
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
                    if (user.decodeLanguage(i).equals(userMap.get("language2")))
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
                    if (user.decodeLanguage(i).equals(userMap.get("language3")))
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
                if (user.decodeCountry(i).equals(userMap.get("location"))) {
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

    /**
     * Pressing the back button is disallowed if the User is setting up their profile for the first
     * time.
     */
    @Override
    public void onBackPressed() {
        if (!firstSetup) {
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
