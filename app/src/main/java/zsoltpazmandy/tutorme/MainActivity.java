package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private User u = null;

    private JSONObject user = null;

    private EditText emailField = null;
    private EditText passwordField = null;
    private Button loginButt = null;
    private Button signUpButt = null;
    private Button resetUserBaseButt = null;

    private String email = null;
    private String password = null;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        u = new User(getApplicationContext());

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseMessaging.getInstance().subscribeToTopic("tutor.me");
        FirebaseInstanceId.getInstance().getToken();


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // ...
                } else {
                    // ...
                }
            }
        };

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setupFields();
        setupLoginButton();
        setupSignupButton();
        setupResetButton();
    }

    private void setupResetButton() {
        resetUserBaseButt = (Button) findViewById(R.id.resetUserBase_butt);
        resetUserBaseButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    u.purgeUserRecords(getApplicationContext());
                    u.resetCounter(getApplicationContext());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupSignupButton() {
        signUpButt = (Button) findViewById(R.id.signUpButt);
        signUpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupActivity = new Intent(MainActivity.this, SignUp.class);
                signupActivity.putExtra("email", emailField.getText().toString().trim());
                signupActivity.putExtra("password", passwordField.getText().toString().trim());
                startActivity(signupActivity);
                finish();
            }
        });
    }

    private void setupLoginButton() {
        loginButt = (Button) findViewById(R.id.login_butt);
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailField.getText().toString().trim();
                password = passwordField.getText().toString().trim();

                if (!validateInput(email, password))
                    return;


                mAuth.signInWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

//                                    int localID = u.loginWithEmail(getApplicationContext(), email, password);

                                    AsyncCloudGetUser getUser = new AsyncCloudGetUser();
                                    getUser.execute(mAuth.getCurrentUser().getUid());

                                    //                                    } else {

//                                        Cloud c = new Cloud();
//                                        try {
//                                            user = c.getUserJSON();
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        Intent launchHome = new Intent(MainActivity.this, Home.class);
//                                        launchHome.putExtra("User", user.toString());
//                                        startActivity(launchHome);
//                                        finish();
//                                    }


                                } else {
                                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    class AsyncCloudGetUser extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... uid) {


            final DatabaseReference userRoot = FirebaseDatabase.getInstance().getReference().child("/users/" + uid[0]);
            userRoot.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();


                    final JSONObject userFromCloud = new JSONObject();
                    try {
                        userFromCloud.put("Age", userMap.get("age"));
                        userFromCloud.put("Email", userMap.get("email"));
                        userFromCloud.put("ID", userMap.get("id"));
                        userFromCloud.put("Language 1", userMap.get("language1"));
                        userFromCloud.put("Language 2", userMap.get("language2"));
                        userFromCloud.put("Language 3", userMap.get("language3"));
                        userFromCloud.put("Learning", userMap.get("learning"));
                        userFromCloud.put("Location", userMap.get("location"));
                        userFromCloud.put("Trained by", userMap.get("trainedBy"));
                        userFromCloud.put("uID", userMap.get("uID"));
                        userFromCloud.put("Username", userMap.get("username"));

                        final DatabaseReference userProgressRoot = userRoot.child("progress");
                        userProgressRoot.addValueEventListener(new ValueEventListener() {

                            JSONObject userFromCloudWithProgress = null;

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                userFromCloudWithProgress = userFromCloud;

                                Iterator iter = dataSnapshot.getChildren().iterator();

                                while (iter.hasNext()) {
//                                    DataSnapshot line = (DataSnapshot) iter.next();
//                                    Map<String, Object> userProg = (Map<String, Object>) line.getValue();

                                    // read in all user progress module by module
                                    String temp = iter.next().toString();
                                    try {

                                        // the entire String unprocessed without the datasnapshot prefix
                                        String raw = temp.substring(21);

                                        // the moduleID delimited by the first underscore _
                                        String moduleID = raw.substring(0,6);

                                        String progData = "";
                                        // if the name of the module does not contain an additional user entered underscore
                                        if (raw.split("_").length == 3) {

                                            progData = raw.substring(6);

                                        } else { // if the name of the module contains underscores
                                            for (int i = 1; i < raw.split("_").length; i++) {
                                                progData = progData + "_" + raw.split("_")[i];
                                            }
                                        }

                                        // moduleID = ID of the module
                                        // progData = the entire 'value' (should be name, totalslides, lastslide)

                                        // used to pad moduleID strings with zeros, so it's always of length 6
                                        // so substrings are not required
                                        switch (moduleID.length()){
                                            case 1:
                                                moduleID = "00000" + moduleID;
                                                break;
                                            case 2:
                                                moduleID = "0000" + moduleID;
                                                break;
                                            case 3:
                                                moduleID = "000" + moduleID;
                                                break;
                                            case 4:
                                                moduleID = "00" + moduleID;
                                                break;
                                            case 5:
                                                moduleID = "0" + moduleID;
                                                break;
                                        }

                                        moduleID = moduleID.replace("_","");
                                        progData = progData.substring(10,progData.length()-2);

                                        userFromCloudWithProgress.accumulate("Progress", moduleID + progData);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                String stringifiedJSON = userFromCloudWithProgress.toString();
                                publishProgress(stringifiedJSON);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Intent launchHome = new Intent(MainActivity.this, Home.class);
            launchHome.putExtra("User", values[0].toString());
            startActivity(launchHome);
            finish();
        }


    }

    private void setupFields() {
        emailField = (EditText) findViewById(R.id.username_textfield);
        emailField.setMaxWidth(emailField.getWidth());
        passwordField = (EditText) findViewById(R.id.password_textfield);
        passwordField.setMaxWidth(passwordField.getWidth());
    }

    public boolean validateInput(String email, String password) {
        boolean result = true;

        if (!email.trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (!password.trim().matches("^[\\w_-]+")) {
            Toast.makeText(getApplicationContext(), "Password format incorrect.\nUse: A-Z, a-z, 0-9, _, -", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (!(password.trim().length() > 5 && password.trim().length() < 11)) {
            Toast.makeText(getApplicationContext(), "Password must be 6-10 characters long.", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
