package zsoltpazmandy.tutorme;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

        FirebaseMessaging.getInstance().subscribeToTopic("test");

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        token = FirebaseInstanceId.getInstance().getToken();

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

//            ListenToMessages startListening = new ListenToMessages();
//            startListening.execute();

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
        protected String doInBackground(String... uid) {
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

        @Override
        protected void onProgressUpdate(String... user) {
            super.onProgressUpdate(user);
        }

    }


    class ListenToMessages extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uid) {
            DatabaseReference chatsessions = FirebaseDatabase.getInstance().getReference().child("/chat_sessions/");

            HashMap<String, String> learningMap = (HashMap<String, String>) userMap.get("learning");
            Set<String> myLearningIDs = learningMap.keySet();
            Set<String> myTutorIDs = new HashSet<>();
            for (String s : myLearningIDs) {
                myTutorIDs.add(learningMap.get(s));
            }

            HashMap<String, String> tuteeMap = (HashMap<String, String>) userMap.get("training");
            Set<String> myTrainingIDs = tuteeMap.keySet();
            Set<String> myTuteeIDs = new HashSet<>();
            for (String s : myTrainingIDs) {
                myTuteeIDs.add(tuteeMap.get(s));
            }


            for (String tutorID : myLearningIDs) {
                chatsessions.child(tutorID + "_" + userMap.get("id").toString()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (flag1) {

                            Intent notifIntent = new Intent(getApplicationContext(), MainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this)
                                    .setAutoCancel(true)
                                    .setContentTitle("tutor.me")
                                    .setContentText("One of your tutors has messaged you.")
                                    .setSmallIcon(R.drawable.school24)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setContentIntent(pendingIntent);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            manager.notify(0, builder.build());
                        }
                        flag1 = true;
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (flag2) {

                            Intent notifIntent = new Intent(getApplicationContext(), MainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this)
                                    .setAutoCancel(true)
                                    .setContentTitle("tutor.me")
                                    .setContentText("One of your tutors has messaged you.")
                                    .setSmallIcon(R.drawable.school24)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setContentIntent(pendingIntent);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            manager.notify(0, builder.build());
                        }
                        flag2 = true;
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            for (String tuteeID : myTuteeIDs) {
                chatsessions.child(userMap.get("id").toString() + "_" + tuteeID).addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (flag3) {
                            Intent notifIntent = new Intent(getApplicationContext(), MainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this)
                                    .setAutoCancel(true)
                                    .setContentTitle("tutor.me")
                                    .setContentText("Someone needs you!")
                                    .setSmallIcon(R.drawable.school24)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setContentIntent(pendingIntent);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            manager.notify(0, builder.build());
                        }
                        flag3 = true;
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (flag4) {
                            Intent notifIntent = new Intent(getApplicationContext(), MainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this)
                                    .setAutoCancel(true)
                                    .setContentTitle("tutor.me")
                                    .setContentText("Someone needs you!")
                                    .setSmallIcon(R.drawable.school24)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setContentIntent(pendingIntent);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            manager.notify(0, builder.build());
                        }
                        flag4 = true;
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            return null;
        }
    }
}
