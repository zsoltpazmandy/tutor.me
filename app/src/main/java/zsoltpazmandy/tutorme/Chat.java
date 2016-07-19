package zsoltpazmandy.tutorme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat extends AppCompatActivity {

    JSONObject user;
    String tutor;

    Socket clientSocket = null;
    String host = "192.168.1.72";
    int port = 60001;

    EditText enterMessage;
    ImageButton sendButton;
    TextView messageBox;

    static ArrayList<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enterMessage = (EditText) findViewById(R.id.chat_enter_message);
        sendButton = (ImageButton) findViewById(R.id.chat_send_button);
        messageBox = (TextView) findViewById(R.id.chat_messagebox_text);
        messageBox.setMovementMethod(new ScrollingMovementMethod());

        messages = new ArrayList<>();

        try {
            user = new JSONObject(getIntent().getStringExtra("User"));
            tutor = getIntent().getStringExtra("Tutor");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTitle("Chatting with " + tutor);


        new Thread() {
            @Override
            public void run() {
                BufferedWriter output = null;

                try {
                    clientSocket = new Socket(host, port);
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    output.write(user.getString("Username") + " is now online.");
                    output.newLine();
                    output.flush();



                    String message;

                    while ((message = input.readLine()) != null) {

                        updateMessageBox(messageBox, message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });


        messageBox.addTextChangedListener(new TextWatcher() {
            ScrollView messageBoxSV = (ScrollView) findViewById(R.id.chat_messagebox_sv);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                messageBoxSV.fullScroll(ScrollView.FOCUS_DOWN);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                messageBoxSV.fullScroll(ScrollView.FOCUS_DOWN);
            }

            @Override
            public void afterTextChanged(Editable s) {
                messageBoxSV.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });


    }

    public void updateMessageBox(final TextView messageBox, final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                messageBox.setText(messageBox.getText() + "\n" + message);

            }
        });
    }


    public void sendMessage(View v) {

        // disallow empties
        if(enterMessage.getText().toString().trim().equals("")) {
            enterMessage.setError("You can't send an empty message");
            return;
        }

        BufferedWriter output = null;

        String name = null;
        try {
            name = user.getString("Username");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            output.write(name + " [" + getTimeStamp() + "] : " + enterMessage.getText().toString());
            output.newLine();
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        enterMessage.setText("");
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
}
