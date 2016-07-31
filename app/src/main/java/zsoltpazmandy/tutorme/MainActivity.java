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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

                                    int localID = 0;
                                    localID = u.loginWithEmail(getApplicationContext(), email, password);
                                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    try {
                                        user = u.getUser(getApplicationContext(), localID);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Intent launchHome = new Intent(MainActivity.this, Home.class);
                                    launchHome.putExtra("User", user.toString());
                                    startActivity(launchHome);
                                    finish();

                                } else {
                                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
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
