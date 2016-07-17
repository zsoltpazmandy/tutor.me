package zsoltpazmandy.tutorme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    JSONObject user;
    String tutor;

    Socket clientSocket = null;
    String host = "192.168.1.72";
    int port = 60001;

    EditText enterMessage;
    ImageButton sendButton;
    TextView messageBox;


    ArrayList<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enterMessage = (EditText) findViewById(R.id.chat_enter_message);
        sendButton = (ImageButton) findViewById(R.id.chat_send_button);
        messageBox = (TextView) findViewById(R.id.chat_messagebox_text);

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

                try {
                    clientSocket = new Socket(host, port);
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String line;

                    while ((line = input.readLine()) != null) {
                        System.out.println(line);
                        final String finalLine = line;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageBox.setText(messageBox.getText().toString() + "\n" + finalLine);
                            }
                        });
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
    }

    public void sendMessage(View v) {

        BufferedWriter output = null;

        try {
            output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            output.write(enterMessage.getText().toString());
            output.newLine();
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        enterMessage.setText("");
    }
}
