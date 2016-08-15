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
import android.widget.ProgressBar;
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

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText emailField = null;
    private EditText passwordField = null;
    private Button loginButt = null;
    private Button signUpButt = null;

    private String email = null;
    private String password = null;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressBar loading;

    AsyncCloudGetUser getUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loading = (ProgressBar) findViewById(R.id.loading_circular);
        loading.setVisibility(View.GONE);

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
                loading.setVisibility(View.VISIBLE);


                email = emailField.getText().toString().trim();
                password = passwordField.getText().toString().trim();

                if (!validateInput(email, password)) {
                    loading.setVisibility(View.GONE);
                    return;
                }
                mAuth.signInWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    getUser = new AsyncCloudGetUser();
                                    getUser.execute(mAuth.getCurrentUser().getUid());
                                } else {
                                    loading.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    class AsyncCloudGetUser extends AsyncTask<String, HashMap<String, Object>, String> {
        @Override
        protected String doInBackground(String... uid) {
            final DatabaseReference userRoot = FirebaseDatabase.getInstance().getReference().child("/users/" + uid[0]);
            userRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> userMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    publishProgress(userMap);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(HashMap<String, Object>... values) {
            super.onProgressUpdate(values);

            Intent launchHome = new Intent(MainActivity.this, Home.class);
            launchHome.putExtra("User", values[0]);
            getUser.cancel(true);
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
