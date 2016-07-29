package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;

import java.io.IOException;
public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Logged off", Toast.LENGTH_SHORT).show();
                }
            }
        };


        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

// SETUP TEXT FIELDS
        final EditText usernameField = (EditText) findViewById(R.id.username_textfield);
        usernameField.setMaxWidth(usernameField.getWidth());
        final EditText passwordField = (EditText) findViewById(R.id.password_textfield);
        passwordField.setMaxWidth(passwordField.getWidth());

// LOGIN BUTTON
        Button loginButt = (Button) findViewById(R.id.login_butt);
        assert loginButt != null;
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                User user = new User(getApplicationContext());

                if (validateInput(username, password)) {
                    int returnVal = user.login(getApplicationContext(), username, password);
                    if (returnVal != 0) {
                        Intent launchHome = new Intent(MainActivity.this, Home.class);
                        try {
                            launchHome.putExtra("User", user.getUser(getApplicationContext(), returnVal).toString());
                            startActivity(launchHome);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        // LOGINTO FIREBASE
//                        mAuth.signInWithEmailAndPassword(usernameField.getText().toString(), passwordField.getText().toString())
//                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                        if (!task.isSuccessful()) {
//                                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                                            Toast.makeText(MainActivity.this, R.string.auth_failed,
//                                                    Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });


                    }
                }

            }
        });



final Button signUp = (Button) findViewById(R.id.signUpButt);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupActivity = new Intent(MainActivity.this, SignUp.class);
                signupActivity.putExtra("email", usernameField.getText().toString().trim());
                signupActivity.putExtra("password", passwordField.getText().toString().trim());
                startActivity(signupActivity);
                finish();
            }
        });



        final Button resetUserbase = (Button) findViewById(R.id.resetUserBase_butt);
        assert resetUserbase != null;
        resetUserbase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = new User(getApplicationContext());
                try {
                    u.purgeUserRecords(getApplicationContext());
                    u.resetCounter(getApplicationContext());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean validateInput(String username, String password) {
        boolean result = true;

        if (!username.trim().matches("^[\\w_-]+")) {
            Toast.makeText(getApplicationContext(), "Username format incorrect.\nUse: A-Z, a-z, 0-9, _, -", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (!(username.trim().length() > 2 && username.trim().length() < 11)) {
            Toast.makeText(getApplicationContext(), "Username must be 3-10 characters long.", Toast.LENGTH_SHORT).show();
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
