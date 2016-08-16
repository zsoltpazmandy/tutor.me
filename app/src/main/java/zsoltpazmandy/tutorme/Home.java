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

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Home extends AppCompatActivity {

    private Toolbar toolbar = null;
    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;
    private ViewPagerAdapter viewPagerAdapter = null;

    private HashMap<String, Object> userMap = null;

    private FirebaseAuth mAuth;
    private String id;
    private String token;

    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;
    private boolean flag4 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("tutorme");

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        token = FirebaseInstanceId.getInstance().getToken();

        DatabaseReference userRoot = FirebaseDatabase.getInstance().getReference().child("/users/");
        userRoot.child(id).child("token").setValue(token);

//        AsyncRegId reg = new AsyncRegId();
//        reg.execute();

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        if (!isProfileComplete()) {
            warnOfIncompleteProfile();
        } else {
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
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

    private boolean isProfileComplete() {
        return (userMap.containsKey("language1") && userMap.containsKey("location")) ? true : false;
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

    class AsyncLoadUser extends AsyncTask<String, HashMap<String, Object>, String> {
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

    class AsyncRegId extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... asd) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("Token", token)
                    .add("id", id)
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.1.19/tutorme/add_id.php")
                    .post(body)
                    .build();
            try {
                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


}
