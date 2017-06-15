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

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * Used to view Table Slides within a module. 2 columns by 10 rows. The bottom of the screen hosts
 * navigation buttons that move the user forward or return to the previous slide. The top of the
 * screen displays the slide counter showing the ordinal number of the slide shown, and 2 buttons:
 * Save & Exit, and Ask Tutor.
 *
 */
public class ViewTableSlide extends AppCompatActivity {

    private Button saveQuit;
    private Button askTutor;
    private Button prevButt;
    private Button nextButt;
    private Cloud cloud;
    private HashMap<String, Object> userMap = null;
    private HashMap<String, Object> moduleMap = null;
    private HashMap<String, String> typesMap = null;
    private HashMap<String, Object> tutorMap = null;
    private int slideNumber;
    private int slideType;
    private int totalSlides;
    private String IDofTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_table_slide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        instantiateVars();
        saveButtListener();
        askTutorButtListener();
        prevButtListener();
        nextButtListener();
        enableDisableButtons();
        fillTable();
    }

    private void instantiateVars() {
        User user = new User();
        cloud = new Cloud();
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        IDofTutor = getIntent().getStringExtra("TutorID");
        TextView slideCountText = (TextView) findViewById(R.id.view_table_slide_top_slidecounttext);
        saveQuit = (Button) findViewById(R.id.view_table_slide_savenquit_butt);
        askTutor = (Button) findViewById(R.id.view_table_slide_asktutor_butt);
        try {
            if (getIntent().hasExtra("TutorMap")) {
                tutorMap = (HashMap<String, Object>) getIntent().getSerializableExtra("TutorMap");
            } else {
                askTutor.setEnabled(false);
            }
        } catch (NullPointerException e) {
            askTutor.setEnabled(false);
        }
        prevButt = (Button) findViewById(R.id.view_table_bottom_prev_butt);
        nextButt = (Button) findViewById(R.id.view_table_bottom_next_butt);
        this.slideNumber = Integer.parseInt(getIntent().getStringExtra("Slide Number"));

        if (this.slideNumber == 0) {
            this.slideNumber = 1;
        }
        if (!getIntent().hasExtra("Review"))
            userMap = cloud.updateProgress(userMap, moduleMap.get("id").toString(), slideNumber);

        this.totalSlides = Integer.parseInt(moduleMap.get("noOfSlides").toString());
        this.setTitle(moduleMap.get("name").toString());
        slideCountText.setText("Slide " + slideNumber + "/" + totalSlides);
    }

    private void nextButtListener() {
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
                        nextSlide = new Intent(ViewTableSlide.this, ViewTextSlide.class);
                        break;
                    case 2:
                        nextSlide = new Intent(ViewTableSlide.this, ViewTableSlide.class);
                        break;
                }
                nextSlide.putExtra("TutorID", IDofTutor);

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
                nextSlide.putExtra("User", userMap);
                nextSlide.putExtra("Module", moduleMap);
                nextSlide.putExtra("Slide Number", "" + slideNumber);
                startActivity(nextSlide);
                finish();
            }
        });
    }

    private void prevButtListener() {
        prevButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                slideNumber--;
                slideType = Integer.parseInt(typesMap.get("Slide_" + slideNumber));

                Intent prevSlide = null;
                switch (slideType) {
                    case 1:
                        prevSlide = new Intent(ViewTableSlide.this, ViewTextSlide.class);
                        break;
                    case 2:
                        prevSlide = new Intent(ViewTableSlide.this, ViewTableSlide.class);
                        break;
                }

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
                prevSlide.putExtra("TutorID", IDofTutor);
                prevSlide.putExtra("User", userMap);
                prevSlide.putExtra("Module", moduleMap);
                prevSlide.putExtra("Slide Number", "" + slideNumber);
                startActivity(prevSlide);
                finish();
            }
        });
    }

    private void askTutorButtListener() {
        askTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startChat = new Intent(ViewTableSlide.this, Chat.class);
                startChat.putExtra("User", userMap);
                startChat.putExtra("TutorID", IDofTutor);
                startChat.putExtra("TutorMap", tutorMap);
                startActivity(startChat);
            }
        });
    }

    private void saveButtListener() {
        saveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnHome = new Intent(ViewTableSlide.this, Home.class);
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
    }

    private void enableDisableButtons() {
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

    private void fillTable() {
        String tableRaw = "";
        ArrayList<String> tableSlide = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            tableSlide.add(i, "");
        }

        tableRaw = moduleMap.get("Slide_" + slideNumber).toString();
        tableRaw = tableRaw.replace("[", "").replace("]", "").replace("\"", "");

        String[] temp = new String[20];
        temp = tableRaw.split(",");

        for (int i = 0; i < temp.length; i++) {
            tableSlide.add(i, temp[i].replace("##comma##", ","));
        }

        TextView row1col1 = (TextView) findViewById(R.id.view_module_tableSlideRow1Col1);
        assert row1col1 != null;
        row1col1.setMaxWidth(row1col1.getWidth());
        row1col1.setText(tableSlide.get(0));
        TextView row1col2 = (TextView) findViewById(R.id.view_module_tableSlideRow1Col2);
        assert row1col2 != null;
        row1col2.setMaxWidth(row1col2.getWidth());
        row1col2.setText(tableSlide.get(1));
        TextView row2col1 = (TextView) findViewById(R.id.view_module_tableSlideRow2Col1);
        assert row2col1 != null;
        row2col1.setMaxWidth(row2col1.getWidth());
        row2col1.setText(tableSlide.get(2));
        TextView row2col2 = (TextView) findViewById(R.id.view_module_tableSlideRow2Col2);
        assert row2col2 != null;
        row2col2.setMaxWidth(row2col2.getWidth());
        row2col2.setText(tableSlide.get(3));
        TextView row3col1 = (TextView) findViewById(R.id.view_module_tableSlideRow3Col1);
        assert row3col1 != null;
        row3col1.setMaxWidth(row3col1.getWidth());
        row3col1.setText(tableSlide.get(4));
        TextView row3col2 = (TextView) findViewById(R.id.view_module_tableSlideRow3Col2);
        assert row3col2 != null;
        row3col2.setMaxWidth(row3col2.getWidth());
        row3col2.setText(tableSlide.get(5));
        TextView row4col1 = (TextView) findViewById(R.id.view_module_tableSlideRow4Col1);
        assert row4col1 != null;
        row4col1.setMaxWidth(row4col1.getWidth());
        row4col1.setText(tableSlide.get(6));
        TextView row4col2 = (TextView) findViewById(R.id.view_module_tableSlideRow4Col2);
        assert row4col2 != null;
        row4col2.setMaxWidth(row4col2.getWidth());
        row4col2.setText(tableSlide.get(7));
        TextView row5col1 = (TextView) findViewById(R.id.view_module_tableSlideRow5Col1);
        assert row5col1 != null;
        row5col1.setMaxWidth(row5col1.getWidth());
        row5col1.setText(tableSlide.get(8));
        TextView row5col2 = (TextView) findViewById(R.id.view_module_tableSlideRow5Col2);
        assert row5col2 != null;
        row5col2.setMaxWidth(row5col2.getWidth());
        row5col2.setText(tableSlide.get(9));
        TextView row6col1 = (TextView) findViewById(R.id.view_module_tableSlideRow6Col1);
        assert row6col1 != null;
        row6col1.setMaxWidth(row6col1.getWidth());
        row6col1.setText(tableSlide.get(10));
        TextView row6col2 = (TextView) findViewById(R.id.view_module_tableSlideRow6Col2);
        assert row6col2 != null;
        row6col2.setMaxWidth(row6col2.getWidth());
        row6col2.setText(tableSlide.get(11));
        TextView row7col1 = (TextView) findViewById(R.id.view_module_tableSlideRow7Col1);
        assert row7col1 != null;
        row7col1.setMaxWidth(row7col1.getWidth());
        row7col1.setText(tableSlide.get(12));
        TextView row7col2 = (TextView) findViewById(R.id.view_module_tableSlideRow7Col2);
        assert row7col2 != null;
        row7col2.setMaxWidth(row7col2.getWidth());
        row7col2.setText(tableSlide.get(13));
        TextView row8col1 = (TextView) findViewById(R.id.view_module_tableSlideRow8Col1);
        assert row8col1 != null;
        row8col1.setMaxWidth(row8col1.getWidth());
        row8col1.setText(tableSlide.get(14));
        TextView row8col2 = (TextView) findViewById(R.id.view_module_tableSlideRow8Col2);
        assert row8col2 != null;
        row8col2.setMaxWidth(row8col2.getWidth());
        row8col2.setText(tableSlide.get(15));
        TextView row9col1 = (TextView) findViewById(R.id.view_module_tableSlideRow9Col1);
        assert row9col1 != null;
        row9col1.setMaxWidth(row9col1.getWidth());
        row9col1.setText(tableSlide.get(16));
        TextView row9col2 = (TextView) findViewById(R.id.view_module_tableSlideRow9Col2);
        assert row9col2 != null;
        row9col2.setMaxWidth(row9col2.getWidth());
        row9col2.setText(tableSlide.get(17));
        TextView row0col1 = (TextView) findViewById(R.id.view_module_tableSlideRow10Col1);
        assert row0col1 != null;
        row0col1.setMaxWidth(row0col1.getWidth());
        row0col1.setText(tableSlide.get(18));
        TextView row0col2 = (TextView) findViewById(R.id.view_module_tableSlideRow10Col2);
        assert row0col2 != null;
        row0col2.setMaxWidth(row0col2.getWidth());
        row0col2.setText(tableSlide.get(19));
    }

    private boolean wantsToQuitLearning = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuitLearning) {

            Intent returnHome = new Intent(ViewTableSlide.this, Home.class);
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


