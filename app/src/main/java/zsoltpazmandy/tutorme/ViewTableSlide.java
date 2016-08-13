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

public class ViewTableSlide extends AppCompatActivity {


    private HashMap<String, Object> userMap = null;
    private HashMap<String, Object> moduleMap = null;
    int slideNumber = 0;
    int totalslides = 0;
    Module f = new Module();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_table_slide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        User u = new User();

        TextView slideCountText = (TextView) findViewById(R.id.view_table_slide_top_slidecounttext);
        Button saveQuit = (Button) findViewById(R.id.view_table_slide_savenquit_butt);
        Button askTutor = (Button) findViewById(R.id.view_table_slide_asktutor_butt);
        Button prevButt = (Button) findViewById(R.id.view_table_bottom_prev_butt);
        Button nextButt = (Button) findViewById(R.id.view_table_bottom_next_butt);

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        this.slideNumber = Integer.parseInt(getIntent().getStringExtra("Slide Number"));

        if (this.slideNumber == 0) {
            this.slideNumber = 1;
        }

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

                Intent returnHome = new Intent(ViewTableSlide.this, Home.class);
                returnHome.putExtra("User", userMap.toString());
                startActivity(returnHome);
                finish();
            }
        });

        prevButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slideNumber--;
                int type = 0;//f.getSlideType(getApplicationContext(), module, slideNumber);

                Intent prevSlide = null;
                switch (type) {
                    case 0:
                        return;
                    case 1:
                        prevSlide = new Intent(ViewTableSlide.this, ViewTextSlide.class);
                        break;
                    case 2:
                        prevSlide = new Intent(ViewTableSlide.this, ViewTableSlide.class);
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
                Toast.makeText(ViewTableSlide.this, "Messaging tutor operation not yet implemented", Toast.LENGTH_SHORT).show();
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
                        nextSlide = new Intent(ViewTableSlide.this, ViewTextSlide.class);
                        break;
                    case 2:
                        nextSlide = new Intent(ViewTableSlide.this, ViewTableSlide.class);
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

    boolean wantsToQuitLearning = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuitLearning) {

            Intent returnHome = new Intent(ViewTableSlide.this, Home.class);
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


