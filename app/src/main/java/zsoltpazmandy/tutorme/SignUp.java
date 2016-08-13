package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private EditText usernameField;
    private EditText emailField;
    private EditText pwFirst;
    private EditText pwSecond;
    private Button signUpButt;

    private String username;
    private String email;
    private String id;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpViews();

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    System.out.println("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    System.out.println("onAuthStateChanged:signed_out");
                }
            }
        });
    }

    private void setUpViews() {
        usernameField = (EditText) findViewById(R.id.username_field);
        usernameField.setMaxLines(1);
        emailField = (EditText) findViewById(R.id.email_field);
        if (getIntent().hasExtra("email")) {
            emailField.setText(getIntent().getExtras().getString("email").toString());
        }
        pwFirst = (EditText) findViewById(R.id.password1field);
        if (getIntent().hasExtra("password")) {
            pwFirst.setText(getIntent().getExtras().getString("password").toString());
        }
        pwSecond = (EditText) findViewById(R.id.password2field);
        signUpButt = (Button) findViewById(R.id.signUpButt);

        addSignUpListener();
    }

    private void addSignUpListener() {
        signUpButt
                .setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username = usernameField.getText().toString().trim();
                        email = emailField.getText().toString().trim();

                        if (!fieldsValid())
                            return;

                        mAuth.createUserWithEmailAndPassword(
                                emailField.getText()
                                        .toString()
                                        .trim(),

                                pwFirst.getText()
                                        .toString()
                                        .trim())

                                .addOnCompleteListener(
                                        SignUp.this,
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) { // registered on cloud

                                                    id = mAuth.getCurrentUser()
                                                            .getUid()
                                                            .toString();

                                                    HashMap<String, Object> userPrepMap = new HashMap<String, Object>();
                                                    userPrepMap.put("id", id);
                                                    userPrepMap.put("username", username);
                                                    userPrepMap.put("email", email);


                                                    Cloud c = new Cloud();
                                                    c.prepUser(id, email, username);

                                                    // continue to profile setup
                                                    Intent setupProfile = new Intent(SignUp.this, ProfileSetup.class);
                                                    setupProfile.putExtra("User", userPrepMap);
                                                    startActivity(setupProfile);
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignUp.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                    }
                });
    }


    private boolean fieldsValid() {
        boolean allValid = true;

        if (!username.trim().matches("^[\\w_-]+")) {
            usernameField.setError("Username format incorrect.\n" +
                    "Use: A-Z, a-z, 0-9, _, -");
            allValid = false;
        }

        if (!(username.trim().length() > 2 && username.trim().length() < 11)) {
            usernameField.setError("Username must be 3-10 characters long.");
            allValid = false;
        }

        if (!emailField.getText().toString().trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            emailField.setError("Invalid email address");
            allValid = false;
        }

        if (!pwFirst.getText().toString().trim().matches("^[\\w_-]+")) {
            pwFirst.setError("Password format incorrect.\n" +
                    "Use: A-Z, a-z, 0-9, _, -");
            allValid = false;
        }

        if (!(pwFirst.getText().toString().length() > 5 && pwFirst.getText().toString().trim().length() < 11)) {
            pwFirst.setError("Password must be 6-10 characters long.");
            allValid = false;
        }
        if (!pwFirst.getText().toString().equals(pwSecond.getText().toString())) {
            pwSecond.setError("The two passwords do not match.");
            allValid = false;
        }

        return allValid;
    }

    boolean wantsToQuit = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuit) {
            Intent restartApp = new Intent(SignUp.this, MainActivity.class);
            startActivity(restartApp);
            finish();
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
