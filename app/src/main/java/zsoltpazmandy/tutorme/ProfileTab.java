package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.HashMap;

public class ProfileTab extends Fragment {

    private JSONObject user = new JSONObject();
    private User u;
    private Module f;
    private Cloud c;

    private ImageView avatar = null;
    private Button editProfileButt = null;
    private Button accountSettingsButt = null;
    private Button logoutButt = null;
    private TextView username = null;
    private TextView rating = null;
    private TextView authored = null;
    private TextView location = null;
    private TextView language1 = null;
    private TextView language2 = null;
    private TextView language3 = null;
    private TextView age = null;
    private TextView interests = null;
    private CheckBox languagesCheck = null;
    private CheckBox travelCheck = null;
    private CheckBox sportsCheck = null;
    private CheckBox historyCheck = null;
    private CheckBox musicCheck = null;
    private CheckBox scienceCheck = null;
    private CheckBox artsCheck = null;
    private CheckBox foodCheck = null;
    private CheckBox healthCheck = null;
    private CheckBox computersCheck = null;
    private EditText userEdit = null;

    private EditText authoredEdit = null;
    private EditText locationEdit = null;
    private EditText language1Edit = null;
    private EditText language2Edit = null;
    private EditText language3Edit = null;
    private EditText ageEdit = null;

    private FirebaseAuth mAuth;

    HashMap<String, Object> userMap = null;

    public ProfileTab() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        if ((getActivity().getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            return;
        }

//        try {
//            this.user = new JSONObject(getActivity().getIntent().getStringExtra("User"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        userMap = (HashMap<String, Object>) getActivity()
                .getIntent()
                .getSerializableExtra("User");

        u = new User(getActivity().getApplicationContext());
        f = new Module();
        c = new Cloud();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_tab, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpElements();
        modifyElements();
        setupProfileTab();
    }

    private void setUpElements() {
        avatar = (ImageView) getActivity().findViewById(R.id.avatar);
        editProfileButt = (Button) getActivity().findViewById(R.id.edit_profile_butt);
        accountSettingsButt = (Button) getActivity().findViewById(R.id.account_settings_butt);
        logoutButt = (Button) getActivity().findViewById(R.id.logoout_butt);
        username = (TextView) getActivity().findViewById(R.id.profile_tab_textview_username);
        rating = (TextView) getActivity().findViewById(R.id.profile_tab_textview_rating);
        authored = (TextView) getActivity().findViewById(R.id.profile_tab_textview_authored);
        location = (TextView) getActivity().findViewById(R.id.profile_tab_textview_location);
        language1 = (TextView) getActivity().findViewById(R.id.profile_tab_textview_language1);
        language2 = (TextView) getActivity().findViewById(R.id.profile_tab_textview_language2);
        language3 = (TextView) getActivity().findViewById(R.id.profile_tab_textview_language3);
        age = (TextView) getActivity().findViewById(R.id.profile_tab_textview_age);
        interests = (TextView) getActivity().findViewById(R.id.profile_tab_textview_interest);
        languagesCheck = (CheckBox) getActivity().findViewById(R.id.language_check);
        travelCheck = (CheckBox) getActivity().findViewById(R.id.travel_check);
        sportsCheck = (CheckBox) getActivity().findViewById(R.id.sports_check);
        historyCheck = (CheckBox) getActivity().findViewById(R.id.history_check);
        musicCheck = (CheckBox) getActivity().findViewById(R.id.music_check);
        scienceCheck = (CheckBox) getActivity().findViewById(R.id.science_check);
        artsCheck = (CheckBox) getActivity().findViewById(R.id.arts_check);
        foodCheck = (CheckBox) getActivity().findViewById(R.id.food_check);
        healthCheck = (CheckBox) getActivity().findViewById(R.id.health_check);
        computersCheck = (CheckBox) getActivity().findViewById(R.id.computers_check);
        userEdit = (EditText) getActivity().findViewById(R.id.profile_tab_edittext_username);
        authoredEdit = (EditText) getActivity().findViewById(R.id.profile_tab_edittext_authored);
        locationEdit = (EditText) getActivity().findViewById(R.id.profile_tab_edittext_location);
        language1Edit = (EditText) getActivity().findViewById(R.id.profile_tab_edittext_language1);
        language2Edit = (EditText) getActivity().findViewById(R.id.profile_tab_edittext_language2);
        language3Edit = (EditText) getActivity().findViewById(R.id.profile_tab_edittext_language3);
        ageEdit = (EditText) getActivity().findViewById(R.id.profile_tab_edittext_age);
    }

    public void modifyElements() {
        userEdit.setMaxWidth(userEdit.getWidth());
        authoredEdit.setMaxWidth(authoredEdit.getWidth());
        locationEdit.setMaxWidth(userEdit.getWidth());
        language1Edit.setMaxWidth(userEdit.getWidth());
        language2Edit.setMaxWidth(userEdit.getWidth());
        language3Edit.setMaxWidth(userEdit.getWidth());
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
    }

    public void setupProfileTab() {

        assert avatar != null;
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Change avatar operation not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        assert accountSettingsButt != null;
        accountSettingsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Changing account settings operation not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        assert editProfileButt != null;
        editProfileButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(getActivity(), ProfileSetup.class);
                editProfile.putExtra("User", userMap);
                editProfile.putExtra("Modifying", 1);
                startActivity(editProfile);
                getActivity().finish();
            }
        });

        assert logoutButt != null;
        logoutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent logoutIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(logoutIntent);
                getActivity().finish();
            }
        });

        int counter = 0;
        HashMap<String, Object> authored = (HashMap<String, Object>) userMap.get("authored");
        if(authored.size() != 0 && !authored.containsKey("none")){
            counter = authored.size();
        }
//        try {
//            if (user.getString("Authored").contains(",")) {
//                counter = user.getString("Authored").split(",").length;
//            } else {
//                if (user.getString("Authored").equals("")) {
//                    counter = 0;
//                } else {
//                    counter = 1;
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        authoredEdit.setText("" + counter);


//        userEdit.setText(u.getUsername(getActivity().getApplicationContext(), user));
        userEdit.setText(userMap.get("username").toString());
//        locationEdit.setText(u.getLocation(getActivity().getApplicationContext(), user));
        locationEdit.setText(userMap.get("location").toString());
//        String[] languages;

//        languages = u.getLanguages(getActivity().getApplicationContext(), user);

//        language1Edit.setText(languages[0]);
//
//        if (!languages[1].isEmpty()) {
//            language2Edit.setText(languages[1]);
//        }
//        if (!languages[2].isEmpty()) {
//            language3Edit.setText(languages[2]);
//        }

        language1Edit.setText(userMap.get("language1").toString());
        language2Edit.setText(userMap.get("language2").toString());
        language3Edit.setText(userMap.get("language3").toString());

//        if (!u.getAge(getActivity().getApplicationContext(), user).equals("")) {
//            ageEdit.setText(u.getAge(getActivity().getApplicationContext(), user));
//        } else {
//            ageEdit.setText("?");
//        }

        if(Integer.parseInt(userMap.get("age").toString())==0){
            ageEdit.setText("?");
        } else {
            ageEdit.setText(userMap.get("age").toString());
        }

//        String[] checkedInterests = u.getInterests(getActivity().getApplicationContext(), user);
//
//
//        for (String s : checkedInterests) {
//            Interests interest = Interests.valueOf(s.toUpperCase());
//
//            switch (interest) {
//                case LANGUAGES:
//                    languagesCheck.setChecked(true);
//                    break;
//                case TRAVELLING:
//                    travelCheck.setChecked(true);
//                    break;
//                case SPORTS:
//                    sportsCheck.setChecked(true);
//                    break;
//                case HISTORY:
//                    historyCheck.setChecked(true);
//                    break;
//                case MUSIC:
//                    musicCheck.setChecked(true);
//                    break;
//                case SCIENCE:
//                    scienceCheck.setChecked(true);
//                    break;
//                case ARTS:
//                    artsCheck.setChecked(true);
//                    break;
//                case FOOD:
//                    foodCheck.setChecked(true);
//                    break;
//                case HEALTH:
//                    healthCheck.setChecked(true);
//                    break;
//                case COMPUTERS:
//                    computersCheck.setChecked(true);
//                    break;
//            }
//        }
    }

//    public enum Interests {
//        LANGUAGES,
//        TRAVELLING,
//        SPORTS,
//        HISTORY,
//        MUSIC,
//        SCIENCE,
//        ARTS,
//        FOOD,
//        HEALTH,
//        COMPUTERS,
//    }


}
