package zsoltpazmandy.tutorme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;


/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * Home activity of the application: a tabhost that includes the three main activity tabs:
 *      1. User Profile tab
 *      2. Learn tab
 *      3. Train tab
 *
 * Handling the 3 tabs is done using a custom Pager Adapter (see ViewPagerAdapter class) that
 * displays the 3 separate segments within the same frame. The reason for this solution is to enable the
 * intuitive and commonly standardised gesture of switching tabs by swiping left and right.
 *
 * Since Google's Firebase Auth registers the Auth information before any other profile information
 * is saved in the database, it must be ensured that the user isn't logging in with no other information
 * of them stored in the database. If that is the case, they must be prompted to complete their
 * registration my filling in at least the required information on the ProfileSetup activity.
 * isProfileComplete() checks whether the just logged in user has completed the ProfileSetup,
 * which if they haven't they are prompted by a dialog window to either visit the ProfileSetup activity
 * or Quit the application.
 *
 * As soon as the user this class is created an asynchronous task is executed to run indefinitely
 * (until logout) which sets a value listener on the user's directory. This is then used to retrieve
 * any user information that is changed by someone other than the user. (e.g. if someone enrolls
 * on a module the user trains, that action is initiated by another person on another device, so
 * the current user's data is updated, and the current user has to be notified of this and their
 * application has to register any updates).
 */
public class Home extends AppCompatActivity {

    private HashMap<String, Object> userMap;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("tutorme");

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();

        DatabaseReference userRoot = FirebaseDatabase.getInstance().getReference().child("/users/");
        userRoot.child(id).child("token").setValue(token);

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        if (!isProfileComplete()) {
            warnOfIncompleteProfile();
        } else {
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragments(new ProfileTab(), "Home");
            viewPagerAdapter.addFragments(new LearningTab(), "Learn");
            viewPagerAdapter.addFragments(new TrainingTab(), "Train");
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);

            if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }

            AsyncLoadUser load = new AsyncLoadUser();
            load.execute();
        }
    }

    private boolean isProfileComplete() {
        return (userMap.containsKey("language1") && userMap.containsKey("location"));
    }

    private void warnOfIncompleteProfile() {
        AlertDialog.Builder incompleteAlert = new AlertDialog.Builder(Home.this);
        incompleteAlert.setTitle("Profile setup incomplete.");
        incompleteAlert.setMessage("Please complete setup.");
        incompleteAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent finishSetup = new Intent(Home.this, ProfileSetup.class);
                finishSetup.putExtra("User", userMap);
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

    private class AsyncLoadUser extends AsyncTask<String, HashMap<String, Object>, String> {
        @Override
        protected String doInBackground(String... uid) {

            final DatabaseReference thisUserRoot = FirebaseDatabase.getInstance().getReference().child("/users/" + mAuth.getCurrentUser().getUid());
            thisUserRoot.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    userMap.put("id", dataSnapshot.child("id").getValue().toString());
                    userMap.put("username", dataSnapshot.child("username").getValue().toString());
                    userMap.put("email", dataSnapshot.child("email").getValue().toString());
                    userMap.put("age", dataSnapshot.child("age").getValue().toString());
                    userMap.put("location", dataSnapshot.child("location").getValue().toString());
                    userMap.put("language1", dataSnapshot.child("language1").getValue().toString());
                    userMap.put("language2", dataSnapshot.child("language2").getValue().toString());
                    userMap.put("language3", dataSnapshot.child("language3").getValue().toString());

                    HashMap<String, String> authored = (HashMap) dataSnapshot.child("authored").getValue();
                    HashMap<String, String> training = (HashMap) dataSnapshot.child("training").getValue();
                    HashMap<String, String> learning = (HashMap) dataSnapshot.child("learning").getValue();
                    HashMap<String, String> progress = (HashMap) dataSnapshot.child("progress").getValue();

                    userMap.put("authored", authored);
                    userMap.put("training", training);
                    userMap.put("learning", learning);
                    userMap.put("progress", progress);

                    publishProgress(userMap);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            return null;

        }

        @Override
        protected void onProgressUpdate(HashMap<String, Object>... user) {
            super.onProgressUpdate(user);
            userMap = user[0];
        }
    }


    /**
     * In order to ensure the user doesn't accidentally leave the activity, they are prompted to
     * repeat the BackPress action within 1 second.
     */
    private boolean wantsToQuit = false;
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
