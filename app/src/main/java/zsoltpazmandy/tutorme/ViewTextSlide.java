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

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * Used to view Text Slides within a module. The bottom of the screen hosts navigation buttons that
 * move the user forward or return to the previous slide. The top of the screen displays the slide
 * counter showing the ordinal number of the slide shown, and 2 buttons: Save & Exit, and Ask Tutor.
 *
 */
public class ViewTextSlide extends AppCompatActivity {

    private Button saveQuit;
    private Button askTutor;
    private Button prevButt;
    private Button nextButt;

    private Cloud cloud;
    private HashMap<String, Object> userMap = null;
    private HashMap<String, Object> tutorMap = null;
    private HashMap<String, Object> moduleMap = null;
    private HashMap<String, String> typesMap = null;

    private int slideNumber;
    private int slideType;
    private int totalSlides;
    private String IDofTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_text_slide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        instantiateVars();

        if (!getIntent().hasExtra("Review"))
            userMap = cloud.updateProgress(userMap, moduleMap.get("id").toString(), slideNumber);
        updateElements();
        setUpListeners();
    }

    private void updateElements() {
        if (slideNumber == 1) {
            prevButt.setEnabled(false);
        } else {
            prevButt.setEnabled(true);
        }

        if (slideNumber == totalSlides) {
            nextButt.setEnabled(false);
        } else {
            nextButt.setEnabled(true);
        }
    }

    private void instantiateVars() {
        User user = new User();
        cloud = new Cloud();
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        IDofTutor = getIntent().getStringExtra("TutorID");
        TextView slideCountText = (TextView) findViewById(R.id.view_text_slide_top_slidecounttext);
        saveQuit = (Button) findViewById(R.id.view_text_slide_savenquit_butt);
        askTutor = (Button) findViewById(R.id.view_text_slide_asktutor_butt);
        try {
            if (getIntent().hasExtra("TutorMap")) {
                tutorMap = (HashMap<String, Object>) getIntent().getSerializableExtra("TutorMap");
            } else {
                askTutor.setEnabled(false);

            }
        } catch (NullPointerException e) {
            askTutor.setEnabled(false);
        }
        prevButt = (Button) findViewById(R.id.view_text_bottom_prev_butt);
        nextButt = (Button) findViewById(R.id.view_text_bottom_next_butt);
        this.slideNumber = Integer.parseInt(getIntent().getStringExtra("Slide Number"));
        if (this.slideNumber == 0) this.slideNumber = 1;
        this.totalSlides = Integer.parseInt(moduleMap.get("noOfSlides").toString());
        this.setTitle(moduleMap.get("name").toString());
        slideCountText.setText("Slide " + slideNumber + "/" + totalSlides);
        String slideText = moduleMap.get("Slide_" + slideNumber).toString();
        TextView slideTextView = (TextView) findViewById(R.id.view_text_view);
        slideTextView.setText(slideText);
    }

    private void setUpListeners() {
        saveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnHome = new Intent(ViewTextSlide.this, Home.class);
                returnHome.putExtra("TutorID", IDofTutor);
                if (!getIntent().hasExtra("Review")) {
                    userMap = cloud.updateProgress(userMap, moduleMap.get("id").toString(), slideNumber);
                } else {
                    returnHome.putExtra("Review", true);
                }
                returnHome.putExtra("User", userMap);
                startActivity(returnHome);
                finish();
            }
        });

        prevButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                slideNumber--;
                slideType = Integer.parseInt(typesMap.get("Slide_" + slideNumber));

                Intent prevSlide = null;
                switch (slideType) {
                    case 1:
                        prevSlide = new Intent(ViewTextSlide.this, ViewTextSlide.class);
                        break;
                    case 2:
                        prevSlide = new Intent(ViewTextSlide.this, ViewTableSlide.class);
                        break;
                }
                prevSlide.putExtra("TutorID", IDofTutor);
                if (!getIntent().hasExtra("Review")) {
                    userMap = cloud.updateProgress(userMap, moduleMap.get("id").toString(), slideNumber);
                } else {
                    prevSlide.putExtra("Review", true);
                }
                try {
                    if (getIntent().hasExtra("TutorMap")) {
                        prevSlide.putExtra("TutorMap", tutorMap);
                    }
                } catch (NullPointerException e) {

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

                Intent startChat = new Intent(ViewTextSlide.this, Chat.class);
                startChat.putExtra("User", userMap);
                startChat.putExtra("TutorMap", tutorMap);
                startChat.putExtra("TutorID", IDofTutor);
                startActivity(startChat);
            }
        });

        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                slideNumber++;
                slideType = Integer.parseInt(typesMap.get("Slide_" + slideNumber));

                Intent nextSlide = null;
                switch (slideType) {
                    case 0:
                        return;
                    case 1:
                        nextSlide = new Intent(ViewTextSlide.this, ViewTextSlide.class);
                        break;
                    case 2:
                        nextSlide = new Intent(ViewTextSlide.this, ViewTableSlide.class);
                        break;
                }
                if (!getIntent().hasExtra("Review")) {
                    userMap = cloud.updateProgress(userMap, moduleMap.get("id").toString(), slideNumber);
                } else {
                    nextSlide.putExtra("Review", true);
                }
                try {
                    if (getIntent().hasExtra("TutorMap")) {
                        nextSlide.putExtra("TutorMap", tutorMap);
                    }
                } catch (NullPointerException e) {

                }
                nextSlide.putExtra("TutorID", IDofTutor);
                nextSlide.putExtra("User", userMap);
                nextSlide.putExtra("Module", moduleMap);
                nextSlide.putExtra("Slide Number", "" + slideNumber);
                startActivity(nextSlide);
                finish();
            }
        });
    }

    private boolean wantsToQuitLearning = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuitLearning) {
            Intent returnHome = new Intent(ViewTextSlide.this, Home.class);
            if (!getIntent().hasExtra("Review")) {
                userMap = cloud.updateProgress(userMap, moduleMap.get("id").toString(), slideNumber);
            } else {
                returnHome.putExtra("Review", true);
            }
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
