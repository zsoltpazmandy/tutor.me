package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    }
                }

            }
        });


// SIGN UP BUTTON
        Button signUpButt = (Button) findViewById(R.id.signUpButt);
        assert signUpButt != null;
        signUpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

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

                    if (!password.trim().matches("^[\\w_-]+")) {
                        passwordField.setError("Password format incorrect.\n" +
                                "Use: A-Z, a-z, 0-9, _, -");
                        return;
                    }

                    if (!(password.trim().length() > 5 && password.trim().length() < 11)) {
                        passwordField.setError("Password must be 6-10 characters long.");
                        return;
                    }


//                    if (validateInput(username, password)) {
                        returnVal = user.register(getApplicationContext(), username, password);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (returnVal != 0) {
                    Toast.makeText(getApplicationContext(), "Username registered.", Toast.LENGTH_SHORT).show();

                    Intent setupProfile = new Intent(MainActivity.this, ProfileSetup.class);
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
}
