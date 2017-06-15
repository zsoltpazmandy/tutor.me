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

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * ProfileTab fragment is the first of the three main tabs within the Home activity and is managed
 * by Home's custom ViewPagerAdapter.
 *
 * User Profile information is displayed on this tab such as:
 *      - name, age, languages spoken, interests;
 *      - community rating;
 *      - modules created information;
 * The user can also initiate the Edit Profle, Account Settings and Upload Profile Photo activities
 * from this tab.
 */
 public class ProfileTab extends Fragment {

    private ImageView avatar;
    private Button editProfileButt;
    private Button accountSettingsButt;
    private Button logoutButt;
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
    private EditText userEdit;
    private EditText authoredEdit;
    private EditText locationEdit;
    private EditText language1Edit;
    private EditText language2Edit;
    private EditText language3Edit;
    private EditText ageEdit;
    private FirebaseAuth FBAuth;
    private HashMap<String, Object> userMap;

    public ProfileTab() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FBAuth = FirebaseAuth.getInstance();

        if ((getActivity().getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            return;
        }

        userMap = (HashMap<String, Object>) getActivity()
                .getIntent()
                .getSerializableExtra("User");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile_tab, container, false);
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
        TextView username = (TextView) getActivity().findViewById(R.id.profile_tab_textview_username);
        TextView rating = (TextView) getActivity().findViewById(R.id.profile_tab_textview_rating);
        TextView authored = (TextView) getActivity().findViewById(R.id.profile_tab_textview_authored);
        TextView location = (TextView) getActivity().findViewById(R.id.profile_tab_textview_location);
        TextView language1 = (TextView) getActivity().findViewById(R.id.profile_tab_textview_language1);
        TextView language2 = (TextView) getActivity().findViewById(R.id.profile_tab_textview_language2);
        TextView language3 = (TextView) getActivity().findViewById(R.id.profile_tab_textview_language3);
        TextView age = (TextView) getActivity().findViewById(R.id.profile_tab_textview_age);
        TextView interests = (TextView) getActivity().findViewById(R.id.profile_tab_textview_interest);
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

    private void modifyElements() {
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

    private void setupProfileTab() {

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

                FBAuth.signOut();
                Intent logoutIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(logoutIntent);
                getActivity().finish();
            }
        });

        int counter = 0;
        HashMap<String, Object> authored = (HashMap<String, Object>) userMap.get("authored");
        if (authored.size() != 0 && !authored.containsKey("none")) {
            counter = authored.size();
        }

        authoredEdit.setText("" + counter);

        userEdit.setText(userMap.get("username").toString());
        locationEdit.setText(userMap.get("location").toString());

        language1Edit.setText(userMap.get("language1").toString());
        language2Edit.setText(userMap.get("language2").toString());
        language3Edit.setText(userMap.get("language3").toString());

        if (Integer.parseInt(userMap.get("age").toString()) == 0) {
            ageEdit.setText("?");
        } else {
            ageEdit.setText(userMap.get("age").toString());
        }

        // interest checkboxes are populated if there is any such information already stored
        try {
            ArrayList<String> interests = (ArrayList<String>) userMap.get("interests");
            if (interests.size() != 0)
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
        } catch (NullPointerException e) {

        }
    }
}
