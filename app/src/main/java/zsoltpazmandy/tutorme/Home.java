package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabHost tabHost = (TabHost) findViewById(R.id.home_tabhost);
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

        JSONObject user = new JSONObject();

        try {
            user = new JSONObject(getIntent().getStringExtra("User"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject forwardUser = user;

        Button createButt = (Button) findViewById(R.id.createModButt);
        assert createButt != null;
        createButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createModule = new Intent(Home.this, CreateModActivity.class);
                createModule.putExtra("User", forwardUser.toString());
                startActivity(createModule);
                finish();
            }
        });

        final Button viewLibButt = (Button) findViewById(R.id.viewLibButt);
        assert viewLibButt != null;
        viewLibButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLibrary = new Intent(Home.this, ViewLibrary.class);
                startActivity(openLibrary);
            }
        });


        TextView username = (TextView) findViewById(R.id.profile_tab_textview_username);
        TextView rating = (TextView) findViewById(R.id.profile_tab_textview_rating);
        TextView location = (TextView) findViewById(R.id.profile_tab_textview_location);
        TextView language1 = (TextView) findViewById(R.id.profile_tab_textview_language1);
        TextView language2 = (TextView) findViewById(R.id.profile_tab_textview_language2);
        TextView language3 = (TextView) findViewById(R.id.profile_tab_textview_language3);
        TextView interests = (TextView) findViewById(R.id.profile_tab_textview_interest);

        EditText userEdit = (EditText) findViewById(R.id.profile_tab_edittext_username);
        userEdit.setEnabled(false);
        EditText ratingEdit = (EditText) findViewById(R.id.profile_tab_edittext_rating);
        ratingEdit.setText("5.0");
        ratingEdit.setEnabled(false);
        EditText locationEdit = (EditText) findViewById(R.id.profile_tab_edittext_location);
        locationEdit.setEnabled(false);
        EditText language1Edit = (EditText) findViewById(R.id.profile_tab_edittext_language1);
        language1Edit.setEnabled(false);
        EditText language2Edit = (EditText) findViewById(R.id.profile_tab_edittext_language2);
        language2Edit.setEnabled(false);
        EditText language3Edit = (EditText) findViewById(R.id.profile_tab_edittext_language3);
        language3Edit.setEnabled(false);

        try {

            User u = new User(getApplicationContext());

            user = new JSONObject(super.getIntent().getStringExtra("User"));

            userEdit.setText(user.getString("Username"));
            locationEdit.setText(u.decodeCountry(Integer.parseInt(user.getString("Location"))));
            String[] tmp = user.getString("Languages").replace("[", "").replace("]", "").split(",");
            language1Edit.setText(u.decodeLanguage(Integer.parseInt(tmp[1])));

            System.out.println(tmp.length);

            if (tmp.length == 3) {
                language2Edit.setText(u.decodeLanguage(Integer.parseInt(tmp[2])));
            }

            if (tmp.length == 4) {
                language2Edit.setText(u.decodeLanguage(Integer.parseInt(tmp[2])));
                language3Edit.setText(u.decodeLanguage(Integer.parseInt(tmp[3])));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
