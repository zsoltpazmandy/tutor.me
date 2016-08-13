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

import java.util.HashMap;

public class ViewTextSlide extends AppCompatActivity {

    int slideNumber = 0;
    int totalslides = 0;
    Module f = new Module();

    private HashMap<String, Object> userMap = null;
    private HashMap<String, Object> moduleMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_text_slide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        User u = new User();
        Cloud c = new Cloud();

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");

        TextView slideCountText = (TextView) findViewById(R.id.view_text_slide_top_slidecounttext);
        Button saveQuit = (Button) findViewById(R.id.view_text_slide_savenquit_butt);
        Button askTutor = (Button) findViewById(R.id.view_text_slide_asktutor_butt);
        final Button prevButt = (Button) findViewById(R.id.view_text_bottom_prev_butt);
        Button nextButt = (Button) findViewById(R.id.view_text_bottom_next_butt);

        this.slideNumber = Integer.parseInt(getIntent().getStringExtra("Slide Number"));

        if (this.slideNumber == 0) this.slideNumber = 1;

        u.updateProgress(userMap, moduleMap, slideNumber);

        this.totalslides = Integer.parseInt(moduleMap.get("noOfSlides").toString());
        this.setTitle(moduleMap.get("name").toString());
        assert slideCountText != null;
        slideCountText.setText("Slide " + slideNumber + "/" + totalslides);

        assert saveQuit != null;
        assert askTutor != null;
        assert prevButt != null;
        assert nextButt != null;


        saveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cloud c = new Cloud();
                c.saveUserHashMapInCloud(userMap);
                Intent returnHome = new Intent(ViewTextSlide.this, Home.class);
                returnHome.putExtra("User", userMap);
                startActivity(returnHome);
                finish();
            }
        });

        prevButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slideNumber--;
                int type = 0;// f.getSlideType(getApplicationContext(), module, slideNumber);

                Intent prevSlide = null;
                switch (type) {
                    case 0:
                        return;
                    case 1:
                        prevSlide = new Intent(ViewTextSlide.this, ViewTextSlide.class);
                        break;
                    case 2:
                        prevSlide = new Intent(ViewTextSlide.this, ViewTableSlide.class);
                        break;
                }

                prevSlide.putExtra("User", userMap);
                prevSlide.putExtra("Module", moduleMap);
                prevSlide.putExtra("Slide Number", "" + slideNumber);
                startActivity(prevSlide);
                finish();
            }
        });

        askTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
//
//                Intent startChat = new Intent(ViewTextSlide.this, Chat.class);
//                startChat.putExtra("User", user.toString());
//                startChat.putExtra("Tutor", tutor.toString());
//                startActivity(startChat);
            }
        });

        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slideNumber++;
                int type = 0;//f.getSlideType(getApplicationContext(), module, slideNumber);

                Intent nextSlide = null;
                switch (type) {
                    case 0:
                        return;
                    case 1:
                        nextSlide = new Intent(ViewTextSlide.this, ViewTextSlide.class);
                        break;
                    case 2:
                        nextSlide = new Intent(ViewTextSlide.this, ViewTableSlide.class);
                        break;
                }

                nextSlide.putExtra("User", userMap);
                nextSlide.putExtra("Module", moduleMap);
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

        String slideText = moduleMap.get("Slide_" + slideNumber).toString();
        TextView slideTextView = (TextView) findViewById(R.id.view_text_view);
        assert slideTextView != null;
        slideTextView.setText(slideText);

    }

    boolean wantsToQuitLearning = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuitLearning) {

            Intent returnHome = new Intent(ViewTextSlide.this, Home.class);
            returnHome.putExtra("User", userMap);
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

















































































































































































































