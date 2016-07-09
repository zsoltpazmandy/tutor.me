package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewModule extends AppCompatActivity {

    JSONObject user = null;
    JSONObject module = null;
    int slideNumber = 0;
    int totalslides = 0;
    Functions f = new Functions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_module);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        User u = new User(getApplicationContext());

        TextView slideCountText = (TextView) findViewById(R.id.view_module_slide_count_text);
        Button saveQuit = (Button) findViewById(R.id.view_module_quit_butt);
        Button askTutor = (Button) findViewById(R.id.view_module_message_tutor_butt);
        Button prevButt = (Button) findViewById(R.id.view_module_prev_butt);
        Button nextButt = (Button) findViewById(R.id.view_module_next_butt);

        try {
            this.user = new JSONObject(getIntent().getStringExtra("User"));
            this.module = new JSONObject(getIntent().getStringExtra("Module"));
            this.slideNumber = Integer.parseInt(getIntent().getStringExtra("Slide Number"));

            if (this.slideNumber == 0) {
                this.slideNumber = 1;
            }

            u.updateProgress(getApplicationContext(), user, module, slideNumber);

            this.totalslides = module.getInt("No. of Slides");
            this.setTitle(module.getString("Name"));
            assert slideCountText != null;
            slideCountText.setText("Slide " + slideNumber + "/" + totalslides);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert saveQuit != null;
        assert askTutor != null;
        assert prevButt != null;
        assert nextButt != null;


        saveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = new User(getApplicationContext());

                JSONObject userUpdate = null;
                try {
                    userUpdate = u.getUser(getApplicationContext(), Integer.parseInt(user.getString("ID")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent returnHome = new Intent(ViewModule.this, Home.class);
                returnHome.putExtra("User", userUpdate.toString());
                startActivity(returnHome);
                finish();
            }
        });

        prevButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideNumber--;
                Intent prevSlide = new Intent(ViewModule.this, ViewModule.class);
                prevSlide.putExtra("User", user.toString());
                prevSlide.putExtra("Module", module.toString());
                prevSlide.putExtra("Slide Number", "" + slideNumber);
                startActivity(prevSlide);
                finish();
            }
        });

        askTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewModule.this, "Messaging tutor operation not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slideNumber++;
                Intent nextSlide = new Intent(ViewModule.this, ViewModule.class);
                nextSlide.putExtra("User", user.toString());
                nextSlide.putExtra("Module", module.toString());
                nextSlide.putExtra("Slide Number", "" + slideNumber);
                startActivity(nextSlide);
                finish();
            }
        });


        if (slideNumber == 1) {
            prevButt.setEnabled(false);
        } else {
            prevButt.setEnabled(true);
        }

        if (slideNumber == totalslides) {
            nextButt.setEnabled(false);
        } else {
            nextButt.setEnabled(true);
        }


        String typesTemp = "";
        String[] typesTempArray = new String[1];

        try {
            typesTemp = module.getString("Types of Slides");
            typesTemp = typesTemp.replace("[", "").replace("]", "");
            if (typesTemp.contains(",")) {
                typesTempArray = typesTemp.split(",");
            } else {
                typesTempArray[0] = typesTemp;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        int typeOfSlide = Integer.parseInt(typesTempArray[slideNumber - 1]);

        switch (typeOfSlide) {
            case 1:

                String slideText = "";

                try {

                    slideText = module.getString("Slide " + slideNumber);
                    TextView slideTextView = (TextView) findViewById(R.id.view_module_slide_text_view);
                    assert slideTextView != null;
                    slideTextView.setText(slideText);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case 2:
                break;
        }


    }

    boolean wantsToQuitLearning = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuitLearning) {

            User u = new User(getApplicationContext());

            JSONObject userUpdate = null;

            try {
                userUpdate = u.getUser(getApplicationContext(), Integer.parseInt(this.user.getString("ID")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent returnHome = new Intent(ViewModule.this, Home.class);
            returnHome.putExtra("User", userUpdate.toString());
            startActivity(returnHome);
            finish();

        }

        this.wantsToQuitLearning = true;
        Toast.makeText(this, "Press 'Back' once more to save your progress & quit learning.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wantsToQuitLearning = false;
            }
        }, 1000);
    }
}
