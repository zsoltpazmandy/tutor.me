package zsoltpazmandy.tutorme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat extends AppCompatActivity {

    JSONObject user;
    JSONObject tutor;

    EditText enterMessage;
    ImageButton sendButton;
    TextView messageBox;

    DatabaseReference root = null;
    DatabaseReference chatRoot = null;

    DatabaseReference thisChat = null;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    FirebaseUser FBUser;

    String roomName = "";

    User u = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i("AuthStateChanged", "User is signed in with uid: " + user.getUid());
                } else {
                    Log.i("AuthStateChanged", "No user is signed in.");
                }
            }
        });


        u = new User(getApplicationContext());

        enterMessage = (EditText) findViewById(R.id.chat_enter_message);
        sendButton = (ImageButton) findViewById(R.id.chat_send_button);
        messageBox = (TextView) findViewById(R.id.chat_messagebox_text);
        messageBox.setMovementMethod(new ScrollingMovementMethod());


        try {
            user = new JSONObject(getIntent().getStringExtra("User"));
            tutor = new JSONObject(getIntent().getStringExtra("Tutor"));
            setTitle("Chatting with " + u.getUsername(getApplicationContext(), tutor));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            roomName = "" + tutor.getInt("ID") + "_" + user.getInt("ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        root = FirebaseDatabase.getInstance().getReference().child("/chat_sessions/" + roomName);

        setUpSendButtListener();
        setUpMessageBoxUpdater();

        System.out.println(mAuth.getCurrentUser().getUid().toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setUpSendButtListener() {


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sender = "";
                String recipient = "";
                try {
                    sender = user.getString("Username");
                    recipient = tutor.getString("Username");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String timeStamp = getTimeStamp();
                String message = enterMessage.getText().toString();

                ChatMessage chatMessage = new ChatMessage(sender, recipient, timeStamp, message);

                String messagePushTemp = root.push().getKey();
                Map<String, Object> map = new HashMap<String, Object>();

                root.child(messagePushTemp).setValue(map);

                chatRoot = root.child(messagePushTemp);
                chatRoot.setValue(chatMessage);

                enterMessage.setText("");
            }
        });
    }

    private void setUpMessageBoxUpdater() {

        DatabaseReference thisChat = root;

        thisChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator iter = dataSnapshot.getChildren().iterator();

                messageBox.setText("");


                while (iter.hasNext()) {
                    DataSnapshot temp = ((DataSnapshot) iter.next());

                    messageBox.append(temp.child("sender").getValue().toString());
                    messageBox.append("[" + temp.child("timeStamp").getValue().toString() + "]: ");
                    messageBox.append(temp.child("message").getValue().toString() + "\n");

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    public static String getTimeStamp() {
        try {

            SimpleDateFormat date = new SimpleDateFormat("HH:mm");
            String timeStamp = date.format(new Date());

            return timeStamp;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        u.saveUserLocally(getApplicationContext(), user);
    }
}
