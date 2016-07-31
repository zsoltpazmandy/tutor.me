package zsoltpazmandy.tutorme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    private Toolbar toolbar = null;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;
    private ViewPagerAdapter viewPagerAdapter = null;

    private JSONObject user = new JSONObject();
    private User u;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new ProfileTab(), "Home");
        viewPagerAdapter.addFragments(new LearningTab(), "Learn");
        viewPagerAdapter.addFragments(new TrainingTab(), "Train");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();

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

        checkIfProfileComplete();
    }

    private void checkIfProfileComplete() {
        if (u.getLanguages(getApplicationContext(), user)[0] == 0) {
            AlertDialog.Builder incompleteAlert = new AlertDialog.Builder(Home.this);
            incompleteAlert.setTitle("Profile setup incomplete.");
            incompleteAlert.setMessage("Please complete setup.");
            incompleteAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent finishSetup = new Intent(Home.this, ProfileSetup.class);
                    finishSetup.putExtra("User String", user.toString());
                    startActivity(finishSetup);
                    finish();
                }
            });
            incompleteAlert.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mAuth.signOut();
                    Intent logoutIntent = new Intent(Home.this, MainActivity.class);
                    startActivity(logoutIntent);
                    finish();
                }
            });
            incompleteAlert.show();
        }
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
