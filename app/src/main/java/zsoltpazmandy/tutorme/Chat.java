package zsoltpazmandy.tutorme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * The Chat activity is launched either by a tutee asking for help with the learning material, in
 * which case the activity is arrived to from the ViewTextSlide or ViewTableSlide activities, OR
 * it may also be initiated by a tutor who wants to contact one of their tutees by selecting their
 * name on the Training Tab.
 *
 * Message objects consist of Sender & Recipient names, Timestamp and Message body. When a user
 * enters a message in the message entry field and presses the send button, the message is recorded
 * in the Firebase database under a directory called chat_sessions. Every chat session is filed
 * under an identifier which is the combination of the two users' unique IDs in the following pattern:
 * tutor's ID + "_" + tutee's ID. This is so if 2 users ever happen to collaborate on more than one
 * module they will have the opportunity to access their message logs from any past conversations
 * they might have had in the past. A ValueListener ensures that the message box (which displays sent
 * messages) is updated any time a new message is sent and recorded in the database.
 *
 * Other than sending the message, the send button also triggers the sending of a push notification
 * using Google's own user tokens and infrastructure, which consequently allows the user to receive
 * a notification even if the application is not running, or is hidden in the background. This feature
 * enables tutees to be provided tutoring support by prompting their tutor to assist them any time
 * they need any help.
 *
 */
 public class Chat extends AppCompatActivity {

    private HashMap<String, Object> userMap;
    private HashMap<String, Object> partner;
    private EditText enterMessage;
    private ImageButton sendButton;
    private TextView messageBox;
    private DatabaseReference root;
    private DatabaseReference chatRoot;
    private OkHttpClient client;
    private SendPushNotification sendPush;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initVars();
        setUpSendButtListener();
        setUpMessageBoxUpdater();
    }

    private void initVars() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseMessagingService FBM = new FirebaseMessagingService();
        client = new OkHttpClient();

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        String roomName = "";
        if (getIntent().hasExtra("TutorMap")) {
            partner = (HashMap<String, Object>) getIntent().getSerializableExtra("TutorMap");
            roomName = partner.get("id").toString() + "_" + userMap.get("id").toString();
        } else {
            partner = (HashMap<String, Object>) getIntent().getSerializableExtra("TuteeMap");
            roomName = userMap.get("id").toString() + "_" + partner.get("id").toString();
        }

        setTitle("Chatting with " + partner.get("username").toString());

        enterMessage = (EditText) findViewById(R.id.chat_enter_message);
        sendButton = (ImageButton) findViewById(R.id.chat_send_button);
        messageBox = (TextView) findViewById(R.id.chat_messagebox_text);
        messageBox.setMovementMethod(new ScrollingMovementMethod());

        root = FirebaseDatabase.getInstance().getReference().child("/chat_sessions/" + roomName);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setUpSendButtListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sender = userMap.get("username").toString();
                String recipient = partner.get("username").toString();
                String timeStamp = getTimeStamp();
                String message = enterMessage.getText().toString();
                ChatMessage chatMessage = new ChatMessage(sender, recipient, timeStamp, message);
                String messagePushTemp = root.push().getKey();
                Map<String, Object> map = new HashMap<>();
                root.child(messagePushTemp).setValue(map);
                chatRoot = root.child(messagePushTemp);
                chatRoot.setValue(chatMessage);
                sendPush = new SendPushNotification();
                String[] notifItems = new String[3];
                notifItems[0] = partner.get("token").toString();
                notifItems[1] = userMap.get("username").toString();
                notifItems[2] = message;
                sendPush.execute(notifItems);
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

    private static String getTimeStamp() {
        try {

            SimpleDateFormat date = new SimpleDateFormat("HH:mm");

            return date.format(new Date());

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private class SendPushNotification extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... notifItems) {
            JSONObject message = new JSONObject();
            JSONObject data = new JSONObject();

            try {
                message.put("to", notifItems[0]);
                data.put("title", notifItems[1]);
                data.put("body", notifItems[2]);
                data.put("tag", notifItems[1]);
                message.put("data", data);
                message.put("project_id", "tutorme-1dcd6");
                post(message.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            sendPush.cancel(true);
        }
    }

    private String post(String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Authorization", "key = AIzaSyDt8dxIKOUE8wDPFtgTYZPmN9X69iHLgYg")
                .addHeader("Content-Type", "application/json")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
        /*
        ADD to the notification sent:
            an Intent, clicking which pulls up the Chat activity on the recipient's end with
            the correct extras linking to the sender:
                1. getIntent().getSerializableExtra("User") => sending from here, it's "partnerMap"
                2. INVERT THIS:
                    if (getIntent().hasExtra("TutorMap")) {
                    partner = (HashMap<String, Object>) getIntent().getSerializableExtra("TutorMap");
                    roomName = partner.get("id").toString() + "_" + userMap.get("id").toString();
                   } else {
                    partner = (HashMap<String, Object>) getIntent().getSerializableExtra("TuteeMap");
                    roomName = userMap.get("id").toString() + "_" + partner.get("id").toString();
                    }
         */
    }

    /**
     * In order to ensure the user doesn't accidentally leave the activity, they are prompted to
     * repeat the BackPress action within 1 second.
     */
    private boolean wantsToQuitChat = false;
    @Override
    public void onBackPressed() {
        if (wantsToQuitChat) {
            super.onBackPressed();
            finish();
        }
        this.wantsToQuitChat = true;
        Toast.makeText(this, "Press 'Back' once more to quit chatting.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wantsToQuitChat = false;
            }
        }, 1000);
    }
}
