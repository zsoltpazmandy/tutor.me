package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * This class handles the registration of a new user in the database. It collects the user-entered
 * email address, password which are sent to register on Firebase. If the registration is successful
 * (i.e. if the device manages to establish a network connection with Firebase, the user-provided
 * data is of the correct format and if the email address being registered is free) the user's basic
 * information is registered as a stub in the database. Ideally this information is completed on the
 * following activity, ProfileSetup, however it may happen that this second stage of the registration
 * fails for some reason (e.g. the user closes the application by accident), but in order to minimise
 * the necessity to repeatedly enter information by the user, the email & password combination is
 * immediately registered and a user directory is created in the database with the user's ID and
 * username. Failing to complete the registration prompts the user that they must complete it before
 * being allowed to use the application. This is done on the main screen after successful login.
 *
 * Registration of email & password are handled by Google's Firebase Auth
 *
 * Username and ID is recorded in the Firebase database separately from Auth information.
 * Such operations are handled from within the Cloud class.
 *
 * fieldsValid() checks whether the inserted characters are acceptable.
 *      email:      [a-z][A-Z]@[a-z][A-Z].[a-z][A-Z]
 *      password:   minumum 6 alphanumeric char in length
 *      username:   [a-z] 3-10 alphanumeric char in length
 *
 *      User-entered information is trimmed of initial or final whitespaces
 */
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
    private Cloud cloud;

    private ProgressBar loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpViews();

        loading = (ProgressBar) findViewById(R.id.loading_circular);
        loading.setVisibility(View.GONE);

        cloud = new Cloud();

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

    /**
     *If the user has already entered their email address and password on the previous screen it is
     *sent to this one and the fields are populated with this data in order to reduce the necessity
     *of repeatedly entering information.
     */
    private void setUpViews() {
        usernameField = (EditText) findViewById(R.id.username_field);
        usernameField.setMaxLines(1);
        emailField = (EditText) findViewById(R.id.email_field);
        if (getIntent().hasExtra("email")) {
            emailField.setText(getIntent().getExtras().getString("email"));
        }
        pwFirst = (EditText) findViewById(R.id.password1field);
        if (getIntent().hasExtra("password")) {
            pwFirst.setText(getIntent().getExtras().getString("password"));
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
                        loading.setVisibility(View.VISIBLE);
                        username = usernameField.getText().toString().trim();
                        email = emailField.getText().toString().trim();

                        if (!fieldsValid()) {
                            loading.setVisibility(View.GONE);
                            return;
                        }

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
                                                            .getUid();

                                                    HashMap<String, Object> userPrepMap = new HashMap<>();
                                                    userPrepMap.put("id", id);
                                                    userPrepMap.put("username", username);
                                                    userPrepMap.put("email", email);

                                                    cloud.prepUser(id, email, username);

                                                    // continue to profile setup
                                                    Intent setupProfile = new Intent(SignUp.this, ProfileSetup.class);
                                                    setupProfile.putExtra("User", userPrepMap);
                                                    startActivity(setupProfile);
                                                    finish();
                                                } else {
                                                    loading.setVisibility(View.GONE);
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

    /**
     * In order to ensure the user doesn't accidentally leave the activity, they are prompted to
     * repeat the BackPress action within 1 second.
     */
    private boolean wantsToQuit = false;
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
