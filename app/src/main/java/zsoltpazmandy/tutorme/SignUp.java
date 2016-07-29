package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;

import java.io.IOException;

public class SignUp extends AppCompatActivity {

    TextView signupLabel;
    EditText usernameField;
    EditText emailField;
    EditText password1Field;
    EditText password2Field;
    Button signUpButt;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpViews();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
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
                // ...
            }
        };
    }

    private void setUpViews() {
        signupLabel = (TextView) findViewById(R.id.signup_label);
        usernameField = (EditText) findViewById(R.id.username_field);
        usernameField.setMaxLines(1);
        emailField = (EditText) findViewById(R.id.email_field);
        if (getIntent().hasExtra("email")) {
            emailField.setText(getIntent().getExtras().getString("email").toString());
        }
        password1Field = (EditText) findViewById(R.id.password1field);
        if (getIntent().hasExtra("password")) {
            password1Field.setText(getIntent().getExtras().getString("password").toString());
        }
        password2Field = (EditText) findViewById(R.id.password2field);
        signUpButt = (Button) findViewById(R.id.signUpButt);

        addSignUpListener();
    }

    private void addSignUpListener() {
        signUpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// offline reg
                String username = usernameField.getText().toString().trim();
                User user = new User(getApplicationContext());
                int returnVal = 0;

                try {

                    if (!username.trim().matches("^[\\w_-]+")) {
                        usernameField.setError("Username format incorrect.\n" +
                                "Use: A-Z, a-z, 0-9, _, -");
                        return;
                    }

                    if (!(username.trim().length() > 2 && username.trim().length() < 11)) {
                        usernameField.setError("Username must be 3-10 characters long.");
                        return;
                    }

                    if (!emailField.getText().toString().trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                        emailField.setError("Invalid email address");
                        return;
                    }

                    if (!password1Field.getText().toString().trim().matches("^[\\w_-]+")) {
                        password1Field.setError("Password format incorrect.\n" +
                                "Use: A-Z, a-z, 0-9, _, -");
                        return;
                    }

                    if (!(password1Field.getText().toString().length() > 5 && password1Field.getText().toString().trim().length() < 11)) {
                        password1Field.setError("Password must be 6-10 characters long.");
                        return;
                    }
                    if (!password1Field.getText().toString().equals(password2Field.getText().toString())) {
                        password2Field.setError("The two passwords do not match.");
                        return;
                    }


                    returnVal = user.register(getApplicationContext(), username, password1Field.getText().toString(), emailField.getText().toString().trim());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (returnVal != 0) {

                    Toast.makeText(getApplicationContext(), "User registered.", Toast.LENGTH_SHORT).show();
                    // REGISTRATION ON FIREBASE:
                    mAuth.createUserWithEmailAndPassword(emailField.getText().toString().trim(), password1Field.getText().toString().trim())
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                            //...
                                    if (!task.isSuccessful()) {
                                        // ....
                                    }
                                }
                            });
                    Intent setupProfile = new Intent(SignUp.this, ProfileSetup.class);
                    try {
                        setupProfile.putExtra("User String", user.getUser(getApplicationContext(), returnVal).toString());
                        startActivity(setupProfile);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed. Username taken.", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
