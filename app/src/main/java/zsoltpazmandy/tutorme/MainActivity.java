package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

        final EditText usernameField = (EditText) findViewById(R.id.username_textfield);
        usernameField.setMaxWidth(usernameField.getWidth());
        final EditText passwordField = (EditText) findViewById(R.id.password_textfield);
        passwordField.setMaxWidth(passwordField.getWidth());

        Button loginButt = (Button) findViewById(R.id.login_butt);
        assert loginButt != null;
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "login button pressed", Toast.LENGTH_SHORT).show();

            }
        });

        Button signUpButt = (Button) findViewById(R.id.signUpButt);
        assert signUpButt != null;
        signUpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User(getApplicationContext());

                int returnVal = 0;

                try {

                    if (!usernameField.getText().toString().matches("^[\\w_-]+")) {
                        Toast.makeText(getApplicationContext(), "Username format incorrect.\nUse: A-Z, a-z, 0-9, _, -", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!(usernameField.getText().toString().length() > 2 && usernameField.getText().toString().length() < 11)) {
                        Toast.makeText(getApplicationContext(), "Username must be 3-10 characters long.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!passwordField.getText().toString().matches("^[\\w_-]+")) {
                        Toast.makeText(getApplicationContext(), "Password format incorrect.\nUse: A-Z, a-z, 0-9, _, -", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!(passwordField.getText().toString().length() > 5 && passwordField.getText().toString().length() < 11)) {
                        Toast.makeText(getApplicationContext(), "Password must be 6-10 characters long.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    returnVal = user.register(getApplicationContext(),
                            usernameField.getText().toString().trim().toLowerCase(),
                            passwordField.getText().toString().trim().toLowerCase());

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

                    // begin "complete profile" activity

                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed. Username taken.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Create Module button launches the create module activity
        // and finishes the main activity

        Button createButt = (Button) findViewById(R.id.createModButt);
        assert createButt != null;
        createButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createModule = new Intent(MainActivity.this, CreateModActivity.class);
                startActivity(createModule);
            }
        });


        final Button viewLibButt = (Button) findViewById(R.id.viewLibButt);
        assert viewLibButt != null;
        viewLibButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLibrary = new Intent(MainActivity.this, ViewLibrary.class);
                startActivity(openLibrary);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
