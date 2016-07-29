package zsoltpazmandy.tutorme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class Chat extends AppCompatActivity {

    JSONObject user;
    JSONObject tutor;

    EditText enterMessage;
    ImageButton sendButton;
    TextView messageBox;

    Firebase chatRoot = null;
    Firebase thisChat = null;
    String roomName = "";

    User u = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);

        chatRoot = new Firebase("https://tutorme-1dcd6.firebaseio.com/chat_sessions");
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

        prepareChatroom();
        setUpSendButtListener();
        setUpMessageBoxUpdater();

    }

    private void prepareChatroom() {

        // creating unique chatroom name for the 2 users by using the combination of their IDs
        // format: tutorID_userId
        try {
            roomName = "" + tutor.getInt("ID") + "_" + user.getInt("ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sessionPushTemp = chatRoot.push().getKey();
        thisChat = chatRoot.child(sessionPushTemp);
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

                String messagePushTemp = thisChat.push().getKey();
                thisChat.child(messagePushTemp).setValue(chatMessage);
                enterMessage.setText("");
            }
        });
    }

    private void setUpMessageBoxUpdater() {

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
            public void onCancelled(FirebaseError firebaseError) {

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
        u.saveUser(getApplicationContext(), user);
    }
}
