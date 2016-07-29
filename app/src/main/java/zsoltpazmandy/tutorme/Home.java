package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Home extends AppCompatActivity {

    JSONObject user = new JSONObject();
    User u;
    Module f;
    TabHost tabHost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        try {
            this.user = new JSONObject(getIntent().getStringExtra("User"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        u = new User(getApplicationContext());
        f = new Module();

        setupTabs();

        setupProfileTab();
        try {
            setupLearningTab();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setupTrainingTab();

    }

    public void setupTabs() {
        tabHost = (TabHost) findViewById(R.id.home_tabhost);

        tabHost.setup();

        TabHost.TabSpec profileTab = tabHost.newTabSpec("Profile");
        profileTab.setIndicator("Profile");
        profileTab.setContent(R.id.home_profile_tab);

        tabHost.addTab(profileTab);

        TabHost.TabSpec learningTab = tabHost.newTabSpec("Learning");
        learningTab.setIndicator("Learning");
        learningTab.setContent(R.id.home_learning_tab);

        tabHost.addTab(learningTab);

        TabHost.TabSpec trainingTab = tabHost.newTabSpec("Training");
        trainingTab.setIndicator("Training");
        trainingTab.setContent(R.id.home_training_tab);

        tabHost.addTab(trainingTab);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }

//        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
//            @Override
//            public void onTabChanged(String tabId) {
//                int tab = tabHost.getCurrentTab();
//
//                View tab1 = tabHost.getTabWidget().getChildAt(0);
//                View tab2 = tabHost.getTabWidget().getChildAt(1);
//                View tab3 = tabHost.getTabWidget().getChildAt(2);
//
//                if(tabHost.getTabWidget().getChildAt(tab).equals(tab1)){
//                    tab1.setBackgroundColor(Color.parseColor("#303F9F"));
//                    tab2.setBackgroundColor(Color.parseColor("#3F51B5"));
//                    tab3.setBackgroundColor(Color.parseColor("#3F51B5"));
//                }
//
//                if(tabHost.getTabWidget().getChildAt(tab).equals(tab2)){
//                    tab1.setBackgroundColor(Color.parseColor("#3F51B5"));
//                    tab2.setBackgroundColor(Color.parseColor("#303F9F"));
//                    tab3.setBackgroundColor(Color.parseColor("#3F51B5"));
//                }
//
//                if(tabHost.getTabWidget().getChildAt(tab).equals(tab3)){
//                    tab1.setBackgroundColor(Color.parseColor("#3F51B5"));
//                    tab2.setBackgroundColor(Color.parseColor("#3F51B5"));
//                    tab3.setBackgroundColor(Color.parseColor("#303F9F"));
//                }
//            }
//
//
//        });


    }

    public void setupProfileTab() {

        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        Button editProfileButt = (Button) findViewById(R.id.edit_profile_butt);
        Button accountSettingsButt = (Button) findViewById(R.id.account_settings_butt);
        Button logoutButt = (Button) findViewById(R.id.logoout_butt);

        TextView username = (TextView) findViewById(R.id.profile_tab_textview_username);
        TextView rating = (TextView) findViewById(R.id.profile_tab_textview_rating);
        TextView authored = (TextView) findViewById(R.id.profile_tab_textview_authored);
        TextView location = (TextView) findViewById(R.id.profile_tab_textview_location);
        TextView language1 = (TextView) findViewById(R.id.profile_tab_textview_language1);
        TextView language2 = (TextView) findViewById(R.id.profile_tab_textview_language2);
        TextView language3 = (TextView) findViewById(R.id.profile_tab_textview_language3);
        TextView age = (TextView) findViewById(R.id.profile_tab_textview_age);
        TextView interests = (TextView) findViewById(R.id.profile_tab_textview_interest);
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
        EditText userEdit = (EditText) findViewById(R.id.profile_tab_edittext_username);
        userEdit.setMaxWidth(userEdit.getWidth());

        EditText authoredEdit = (EditText) findViewById(R.id.profile_tab_edittext_authored);
        authoredEdit.setMaxWidth(authoredEdit.getWidth());
        EditText locationEdit = (EditText) findViewById(R.id.profile_tab_edittext_location);
        locationEdit.setMaxWidth(userEdit.getWidth());
        EditText language1Edit = (EditText) findViewById(R.id.profile_tab_edittext_language1);
        language1Edit.setMaxWidth(userEdit.getWidth());
        EditText language2Edit = (EditText) findViewById(R.id.profile_tab_edittext_language2);
        language2Edit.setMaxWidth(userEdit.getWidth());
        EditText language3Edit = (EditText) findViewById(R.id.profile_tab_edittext_language3);
        language3Edit.setMaxWidth(userEdit.getWidth());
        EditText ageEdit = (EditText) findViewById(R.id.profile_tab_edittext_age);
        ageEdit.setMaxWidth(userEdit.getWidth());

        languagesCheck.setEnabled(false);
        travelCheck.setEnabled(false);
        sportsCheck.setEnabled(false);
        historyCheck.setEnabled(false);
        musicCheck.setEnabled(false);
        scienceCheck.setEnabled(false);
        artsCheck.setEnabled(false);
        foodCheck.setEnabled(false);
        healthCheck.setEnabled(false);
        computersCheck.setEnabled(false);
        userEdit.setEnabled(false);
        authoredEdit.setEnabled(false);
        locationEdit.setEnabled(false);
        language1Edit.setEnabled(false);
        language2Edit.setEnabled(false);
        language3Edit.setEnabled(false);
        ageEdit.setEnabled(false);


        assert avatar != null;
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Change avatar operation not implemented yet", Toast.LENGTH_SHORT).show();

            }
        });

        assert accountSettingsButt != null;
        accountSettingsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Changing account settings operation not implemented yet", Toast.LENGTH_SHORT).show();

            }
        });

        assert editProfileButt != null;
        editProfileButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(Home.this, ProfileSetup.class);
                editProfile.putExtra("User String", user.toString());
                editProfile.putExtra("Modifying",1);
                startActivity(editProfile);
                finish();
            }
        });

        assert logoutButt != null;
        logoutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logoutIntent = new Intent(Home.this, MainActivity.class);
                startActivity(logoutIntent);
                finish();
            }
        });

        List<Integer> moduleIDs = new ArrayList<>();

        try {
            moduleIDs = f.getIDs(getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int counter = 0;

        for (int id : moduleIDs) {

            try {
                if (u.getUsername(getApplicationContext(), user).equals(f.getModuleByID(getApplicationContext(), id).getString("Author"))) {
                    counter++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        authoredEdit.setText("" + counter);


        userEdit.setText(u.getUsername(getApplicationContext(), user));
        locationEdit.setText(u.decodeCountry(u.getLocation(getApplicationContext(), user)));
        int[] languages;

        languages = u.getLanguages(getApplicationContext(), user);

        language1Edit.setText(u.decodeLanguage(languages[0]));

        if (languages[1] != 0) {
            language2Edit.setText(u.decodeLanguage(languages[1]));
        }
        if (languages[2] != 0) {
            language3Edit.setText(u.decodeLanguage(languages[2]));
        }

        if (u.getAge(getApplicationContext(), user) != 0) {
            ageEdit.setText("" + u.getAge(getApplicationContext(), user));
        } else {
            ageEdit.setText("?");
        }

        int[] interestIDs = new int[0];

        interestIDs = u.getInterests(getApplicationContext(), user);

        if (!("" + interestIDs[0]).equals(""))
            for (int interestID : interestIDs) {
                switch (interestID) {
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

    public void setupLearningTab() throws JSONException {

        TextView learningView = (TextView) findViewById(R.id.learning_tab_currently_learning_top);

        ArrayList<Integer> learningIDs = new ArrayList<>();

        for (int i : u.getLearning(getApplicationContext(), user)) {
            if (i != 0) {
                learningIDs.add(i);
            }
        }

        if (learningIDs.size() == 0) {
            assert learningView != null;
            learningView.setText(R.string.no_module_taken_yet);
        } else {
            assert learningView != null;
            learningView.setText(R.string.i_m_currently_learning);

            List<Integer> currentModules = u.getLearning(getApplicationContext(), user);

            ListView learningList = (ListView) findViewById(R.id.learning_tab_currently_learning_list);

            ArrayList<String> learningModules = new ArrayList<>();

            for (int i : currentModules) {
                try {
                    double progress = user.getInt("Progress" + i);
                    double totalSlides = f.getSlideCount(getApplicationContext(), i);
                    double tempDouble = (progress / totalSlides) * 100;
                    long percentCompleted = Math.round(tempDouble);
                    learningModules.add(f.getModuleByID(getApplicationContext(), i).getString("Name") + "\n(" + percentCompleted + "%)");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            learningModules = modulesTakenDesc(learningModules);

            final ListAdapter currentModulesAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, learningModules);

            assert learningList != null;
            learningList.setAdapter(currentModulesAdapter);

            learningList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedModule = String.valueOf(currentModulesAdapter.getItem(position));
                    selectedModule = selectedModule.split("\n")[0]; // get rid of progress tag

                    JSONObject module = null;

                    try {
                        module = f.getModuleByName(getApplicationContext(), selectedModule);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent openModule = new Intent(Home.this, ModuleProgress.class);
                    openModule.putExtra("User", user.toString());
                    openModule.putExtra("Module", module.toString());
                    startActivity(openModule);
                    finish();
                }
            });
        }

        Button browseAllButt = (Button) findViewById(R.id.viewLibButt);
        assert browseAllButt != null;
        browseAllButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLibrary = new Intent(Home.this, ViewLibrary.class);
                openLibrary.putExtra("User String", user.toString());
                startActivity(openLibrary);
                finish();
            }
        });


    }

    public void setupTrainingTab() {

        Button createButt = (Button) findViewById(R.id.createModButt);
        assert createButt != null;
        Button editModButt = (Button) findViewById(R.id.training_tab_edit_module_butt);
        assert editModButt != null;
        Button populateFake = (Button) findViewById(R.id.training_tab_populate_library_butt);
        assert populateFake != null;

        JSONObject user = new JSONObject();

        try {
            user = new JSONObject(getIntent().getStringExtra("User"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject forwardUser = user;

        createButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createModule = new Intent(Home.this, CreateModActivity.class);
                createModule.putExtra("User", forwardUser.toString());
                startActivity(createModule);
                finish();
            }
        });

        // disable button if the user has not authored any modules
        if (u.getModulesAuthoredBy(getApplicationContext(), user).size() == 0) {
            editModButt.setEnabled(false);
        }


        final JSONObject finalUser = user;
        editModButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editModIntent = new Intent(Home.this, EditModules.class);
                editModIntent.putExtra("User String", finalUser.toString());
                startActivity(editModIntent);
                finish();
            }
        });

        populateFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    f.populateLibrary(getApplicationContext(), finalUser);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject updatedUser = null;
                try {
                    updatedUser = u.getUser(getApplicationContext(), finalUser.getInt("ID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent restart = new Intent(Home.this, Home.class);
                restart.putExtra("User", updatedUser.toString());
                startActivity(restart);
                finish();
            }
        });
    }

    public ArrayList<String> modulesTakenDesc(ArrayList<String> modules) {

        ArrayList<String> orderedDesc = new ArrayList<>();

        TreeSet<Integer> tempTree = new TreeSet<>();

        for (String s : modules) {

            String temp = s.substring(s.length() - 5, s.length() - 2).replace("(", "").replace(")", "").replace("%", "").replace("\n", "");
            int i = Integer.parseInt(temp);
            tempTree.add(i);
        }

        NavigableSet<Integer> result = tempTree.descendingSet();

        for (int i : result) {
            for (int j = 0; j < modules.size(); j++) {
                String temp2 = modules.get(j).substring(modules.get(j).length() - 5, modules.get(j).length() - 2).replace("(", "").replace(")", "").replace("%", "").replace("\n", "");
                if (Integer.parseInt(temp2) == i) {
                    orderedDesc.add(modules.get(j));
                }
            }
        }

        return orderedDesc;
    }

    boolean wantsToQuit = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuit) {
            super.onBackPressed();
            return;
        }

        this.wantsToQuit = true;
        Toast.makeText(this, "Press 'Back' once more to quit.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wantsToQuit = false;
            }
        }, 1000);
    }

}
