package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

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


        Button createButt = (Button) findViewById(R.id.createModButt);
        assert createButt != null;
        createButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createModule = new Intent(Home.this, CreateModActivity.class);
                startActivity(createModule);
                System.out.println("here");
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
    }

}
