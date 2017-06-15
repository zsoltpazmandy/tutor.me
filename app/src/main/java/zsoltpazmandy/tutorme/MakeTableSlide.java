package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * The activity is used for three different use cases:
 * 1. Adding a table slide when creating a new module;
 * 2. Adding a table slide to an existing module;
 * 3. Editing an existing table slide of an existing module;
 *
 */
public class MakeTableSlide extends AppCompatActivity {

    private HashMap<String, Object> moduleMap = null;
    private HashMap<String, Object> userMap = null;
    private TextView topHint;
    private Button finishButt;
    private Button nextSlide;
    private int newSlideIndex;
    private EditText row1col1;
    private EditText row1col2;
    private EditText row2col1;
    private EditText row2col2;
    private EditText row3col1;
    private EditText row3col2;
    private EditText row4col1;
    private EditText row4col2;
    private EditText row5col1;
    private EditText row5col2;
    private EditText row6col1;
    private EditText row6col2;
    private EditText row7col1;
    private EditText row7col2;
    private EditText row8col1;
    private EditText row8col2;
    private EditText row9col1;
    private EditText row9col2;
    private EditText row0col1;
    private EditText row0col2;
    private HashMap<String, String> typesMap;
    private int slideIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_table_slide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        initVars();
        if (!getIntent().hasExtra("Slide To Edit"))
            slideIndex = Integer.parseInt(getIntent().getStringExtra("Next Slide Index"));
        if ((getIntent().hasExtra("New Module"))) {
            addingToNewModule();
        } else if (getIntent().hasExtra("Adding Slide To Existing Module")) {
            addingToExistingModule();
        } else {
            if (getIntent().hasExtra("Slide To Edit")) {
                editingSlide();
            }
        }
    }

    private void editingSlide() {

        setTitle("Edit module: edit slide");
        topHint.setText("Edit current contents of the slide. Remember to keep target words in the left column, these are the ones you want your Learner to learn, we'll use this information to generate quizzes later.");
        finishButt.setText("Cancel");
        nextSlide.setText("Save");

        final int slideEdited = Integer.parseInt(getIntent().getStringExtra("Slide To Edit"));

        String tableRaw = "";
        ArrayList<String> tableSlide = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            tableSlide.add(i, "");
        }

        tableRaw = moduleMap.get("Slide_" + slideEdited).toString();
        tableRaw = tableRaw.replace("[", "").replace("]", "").replace("\"", "");

        String[] temp = new String[20];
        temp = tableRaw.split(",");

        for (int i = 0; i < temp.length; i++) {
            tableSlide.add(i, temp[i].replace("##comma##", ","));
        }

        row1col1 = (EditText) findViewById(R.id.tableSlideRow1Col1);
        row1col1.setMaxWidth(row1col1.getWidth());
        row1col1.setText(tableSlide.get(0));
        row1col2 = (EditText) findViewById(R.id.tableSlideRow1Col2);
        row1col2.setMaxWidth(row1col2.getWidth());
        row1col2.setText(tableSlide.get(1));
        row2col1 = (EditText) findViewById(R.id.tableSlideRow2Col1);
        row2col1.setMaxWidth(row2col1.getWidth());
        row2col1.setText(tableSlide.get(2));
        row2col2 = (EditText) findViewById(R.id.tableSlideRow2Col2);
        row2col2.setMaxWidth(row2col2.getWidth());
        row2col2.setText(tableSlide.get(3));
        row3col1 = (EditText) findViewById(R.id.tableSlideRow3Col1);
        row3col1.setMaxWidth(row3col1.getWidth());
        row3col1.setText(tableSlide.get(4));
        row3col2 = (EditText) findViewById(R.id.tableSlideRow3Col2);
        row3col2.setMaxWidth(row3col2.getWidth());
        row3col2.setText(tableSlide.get(5));
        row4col1 = (EditText) findViewById(R.id.tableSlideRow4Col1);
        row4col1.setMaxWidth(row4col1.getWidth());
        row4col1.setText(tableSlide.get(6));
        row4col2 = (EditText) findViewById(R.id.tableSlideRow4Col2);
        row4col2.setMaxWidth(row4col2.getWidth());
        row4col2.setText(tableSlide.get(7));
        row5col1 = (EditText) findViewById(R.id.tableSlideRow5Col1);
        row5col1.setMaxWidth(row5col1.getWidth());
        row5col1.setText(tableSlide.get(8));
        row5col2 = (EditText) findViewById(R.id.tableSlideRow5Col2);
        row5col2.setMaxWidth(row5col2.getWidth());
        row5col2.setText(tableSlide.get(9));
        row6col1 = (EditText) findViewById(R.id.tableSlideRow6Col1);
        row6col1.setMaxWidth(row6col1.getWidth());
        row6col1.setText(tableSlide.get(10));
        row6col2 = (EditText) findViewById(R.id.tableSlideRow6Col2);
        row6col2.setMaxWidth(row6col2.getWidth());
        row6col2.setText(tableSlide.get(11));
        row7col1 = (EditText) findViewById(R.id.tableSlideRow7Col1);
        row7col1.setMaxWidth(row7col1.getWidth());
        row7col1.setText(tableSlide.get(12));
        row7col2 = (EditText) findViewById(R.id.tableSlideRow7Col2);
        row7col2.setMaxWidth(row7col2.getWidth());
        row7col2.setText(tableSlide.get(13));
        row8col1 = (EditText) findViewById(R.id.tableSlideRow8Col1);
        row8col1.setMaxWidth(row8col1.getWidth());
        row8col1.setText(tableSlide.get(14));
        row8col2 = (EditText) findViewById(R.id.tableSlideRow8Col2);
        row8col2.setMaxWidth(row8col2.getWidth());
        row8col2.setText(tableSlide.get(15));
        row9col1 = (EditText) findViewById(R.id.tableSlideRow9Col1);
        row9col1.setMaxWidth(row9col1.getWidth());
        row9col1.setText(tableSlide.get(16));
        row9col2 = (EditText) findViewById(R.id.tableSlideRow9Col2);
        row9col2.setMaxWidth(row9col2.getWidth());
        row9col2.setText(tableSlide.get(17));
        row0col1 = (EditText) findViewById(R.id.tableSlideRow10Col1);
        row0col1.setMaxWidth(row0col1.getWidth());
        row0col1.setText(tableSlide.get(18));
        row0col2 = (EditText) findViewById(R.id.tableSlideRow10Col2);
        row0col2.setMaxWidth(row0col2.getWidth());
        row0col2.setText(tableSlide.get(19));

        finishButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editFinished = new Intent(MakeTableSlide.this, EditSelectedModule.class);
                editFinished.putExtra("Module", moduleMap);
                editFinished.putExtra("User", userMap);
                setResult(RESULT_CANCELED, editFinished);
                finish();
            }
        });

        nextSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int col1 = 0, col2 = 0;

                if (row1col1.getText().length() != 0) {
                    col1++;
                }
                if (row1col2.getText().length() != 0) {
                    col2++;
                }
                if (row2col1.getText().length() != 0) {
                    col1++;
                }
                if (row2col2.getText().length() != 0) {
                    col2++;
                }
                if (row3col1.getText().length() != 0) {
                    col1++;
                }
                if (row3col2.getText().length() != 0) {
                    col2++;
                }
                if (row4col1.getText().length() != 0) {
                    col1++;
                }
                if (row4col2.getText().length() != 0) {
                    col2++;
                }
                if (row5col1.getText().length() != 0) {
                    col1++;
                }
                if (row5col2.getText().length() != 0) {
                    col2++;
                }
                if (row6col1.getText().length() != 0) {
                    col1++;
                }
                if (row6col2.getText().length() != 0) {
                    col2++;
                }
                if (row7col1.getText().length() != 0) {
                    col1++;
                }
                if (row7col2.getText().length() != 0) {
                    col2++;
                }
                if (row8col1.getText().length() != 0) {
                    col1++;
                }
                if (row8col2.getText().length() != 0) {
                    col2++;
                }
                if (row9col1.getText().length() != 0) {
                    col1++;
                }
                if (row9col2.getText().length() != 0) {
                    col2++;
                }
                if (row0col1.getText().length() != 0) {
                    col1++;
                }
                if (row0col2.getText().length() != 0) {
                    col2++;
                }

                if (col1 == 0 && col2 == 0) {
                    Toast.makeText(MakeTableSlide.this, "You can't create an empty slide.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (col1 != col2) {
                    Toast.makeText(MakeTableSlide.this, "Please complete the table by filling in entire rows.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> temp = new ArrayList<>();
                temp.add(row1col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row1col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row2col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row2col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row3col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row3col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row4col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row4col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row5col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row5col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row6col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row6col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row7col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row7col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row8col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row8col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row9col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row9col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row0col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row0col2.getText().toString().trim().replace(",", "##comma##"));

                temp.removeAll(Arrays.asList("", null));

                moduleMap.remove("Slide_" + slideEdited);

                String assembled = "";
                for (String s : temp) {
                    assembled = assembled + s + ",";
                }
                moduleMap.put("Slide_" + slideEdited, assembled.substring(0, assembled.length() - 1));

                Intent editFinished = new Intent(MakeTableSlide.this, EditSelectedModule.class);
                editFinished.putExtra("Module", moduleMap);
                editFinished.putExtra("User", userMap);
                setResult(RESULT_OK, editFinished);
                finish();
            }
        });
    }

    private void addingToExistingModule() {
        nextSlide.setText("Save");
        setTitle("Edit Module: add table slide");
        row1col1 = (EditText) findViewById(R.id.tableSlideRow1Col1);
        row1col1.setMaxWidth(row1col1.getWidth());
        row1col2 = (EditText) findViewById(R.id.tableSlideRow1Col2);
        row1col2.setMaxWidth(row1col2.getWidth());
        row2col1 = (EditText) findViewById(R.id.tableSlideRow2Col1);
        row2col1.setMaxWidth(row2col1.getWidth());
        row2col2 = (EditText) findViewById(R.id.tableSlideRow2Col2);
        row2col2.setMaxWidth(row2col2.getWidth());
        row3col1 = (EditText) findViewById(R.id.tableSlideRow3Col1);
        row3col1.setMaxWidth(row3col1.getWidth());
        row3col2 = (EditText) findViewById(R.id.tableSlideRow3Col2);
        row3col2.setMaxWidth(row3col2.getWidth());
        row4col1 = (EditText) findViewById(R.id.tableSlideRow4Col1);
        row4col1.setMaxWidth(row4col1.getWidth());
        row4col2 = (EditText) findViewById(R.id.tableSlideRow4Col2);
        row4col2.setMaxWidth(row4col2.getWidth());
        row5col1 = (EditText) findViewById(R.id.tableSlideRow5Col1);
        row5col1.setMaxWidth(row5col1.getWidth());
        row5col2 = (EditText) findViewById(R.id.tableSlideRow5Col2);
        row5col2.setMaxWidth(row5col2.getWidth());
        row6col1 = (EditText) findViewById(R.id.tableSlideRow6Col1);
        row6col1.setMaxWidth(row6col1.getWidth());
        row6col2 = (EditText) findViewById(R.id.tableSlideRow6Col2);
        row6col2.setMaxWidth(row6col2.getWidth());
        row7col1 = (EditText) findViewById(R.id.tableSlideRow7Col1);
        row7col1.setMaxWidth(row7col1.getWidth());
        row7col2 = (EditText) findViewById(R.id.tableSlideRow7Col2);
        row7col2.setMaxWidth(row7col2.getWidth());
        row8col1 = (EditText) findViewById(R.id.tableSlideRow8Col1);
        row8col1.setMaxWidth(row8col1.getWidth());
        row8col2 = (EditText) findViewById(R.id.tableSlideRow8Col2);
        row8col2.setMaxWidth(row8col2.getWidth());
        row9col1 = (EditText) findViewById(R.id.tableSlideRow9Col1);
        row9col1.setMaxWidth(row9col1.getWidth());
        row9col2 = (EditText) findViewById(R.id.tableSlideRow9Col2);
        row9col2.setMaxWidth(row9col2.getWidth());
        row0col1 = (EditText) findViewById(R.id.tableSlideRow10Col1);
        row0col1.setMaxWidth(row0col1.getWidth());
        row0col2 = (EditText) findViewById(R.id.tableSlideRow10Col2);
        row0col2.setMaxWidth(row0col2.getWidth());
        nextSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int col1 = 0, col2 = 0;

                if (row1col1.getText().length() != 0) {
                    col1++;
                }
                if (row1col2.getText().length() != 0) {
                    col2++;
                }
                if (row2col1.getText().length() != 0) {
                    col1++;
                }
                if (row2col2.getText().length() != 0) {
                    col2++;
                }
                if (row3col1.getText().length() != 0) {
                    col1++;
                }
                if (row3col2.getText().length() != 0) {
                    col2++;
                }
                if (row4col1.getText().length() != 0) {
                    col1++;
                }
                if (row4col2.getText().length() != 0) {
                    col2++;
                }
                if (row5col1.getText().length() != 0) {
                    col1++;
                }
                if (row5col2.getText().length() != 0) {
                    col2++;
                }
                if (row6col1.getText().length() != 0) {
                    col1++;
                }
                if (row6col2.getText().length() != 0) {
                    col2++;
                }
                if (row7col1.getText().length() != 0) {
                    col1++;
                }
                if (row7col2.getText().length() != 0) {
                    col2++;
                }
                if (row8col1.getText().length() != 0) {
                    col1++;
                }
                if (row8col2.getText().length() != 0) {
                    col2++;
                }
                if (row9col1.getText().length() != 0) {
                    col1++;
                }
                if (row9col2.getText().length() != 0) {
                    col2++;
                }
                if (row0col1.getText().length() != 0) {
                    col1++;
                }
                if (row0col2.getText().length() != 0) {
                    col2++;
                }

                if (col1 == 0 && col2 == 0) {
                    Toast.makeText(MakeTableSlide.this, "You can't create an empty slide.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (col1 != col2) {
                    Toast.makeText(MakeTableSlide.this, "Please complete the table by filling in entire rows.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> temp = new ArrayList<>();
                temp.add(row1col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row1col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row2col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row2col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row3col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row3col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row4col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row4col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row5col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row5col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row6col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row6col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row7col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row7col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row8col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row8col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row9col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row9col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row0col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row0col2.getText().toString().trim().replace(",", "##comma##"));

                temp.removeAll(Arrays.asList("", null));
                String assembled = "";
                for (String s : temp) {
                    assembled = assembled + s + ",";
                }
                typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                moduleMap.put("Slide_" + slideIndex, assembled.substring(0, assembled.length() - 1));
                moduleMap.put("noOfSlides", String.valueOf(slideIndex));

                Intent addNextSlide = new Intent(MakeTableSlide.this, EditSelectedModule.class);
                addNextSlide.putExtra("Module", moduleMap);
                addNextSlide.putExtra("User", userMap);
                addNextSlide.putExtra("Finished", true);
                setResult(RESULT_OK, addNextSlide);
                finish();
            }
        });

        finishButt.setText("Cancel");
        finishButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editFinished = new Intent(MakeTableSlide.this, EditSelectedModule.class);
                editFinished.putExtra("Module", moduleMap);
                editFinished.putExtra("User", userMap);
                setResult(RESULT_CANCELED, editFinished);
                finish();
            }
        });
    }

    private void addingToNewModule() {
        setTitle("Create module: new table slide");
        finishButt.setText("Save & Exit");
        nextSlide.setText("Add More Slides");

        newSlideIndex = Integer.parseInt(getIntent().getStringExtra("Next Slide Index"));
        row1col1 = (EditText) findViewById(R.id.tableSlideRow1Col1);
        row1col1.setMaxWidth(row1col1.getWidth());
        row1col2 = (EditText) findViewById(R.id.tableSlideRow1Col2);
        row1col2.setMaxWidth(row1col2.getWidth());
        row2col1 = (EditText) findViewById(R.id.tableSlideRow2Col1);
        row2col1.setMaxWidth(row2col1.getWidth());
        row2col2 = (EditText) findViewById(R.id.tableSlideRow2Col2);
        row2col2.setMaxWidth(row2col2.getWidth());
        row3col1 = (EditText) findViewById(R.id.tableSlideRow3Col1);
        row3col1.setMaxWidth(row3col1.getWidth());
        row3col2 = (EditText) findViewById(R.id.tableSlideRow3Col2);
        row3col2.setMaxWidth(row3col2.getWidth());
        row4col1 = (EditText) findViewById(R.id.tableSlideRow4Col1);
        row4col1.setMaxWidth(row4col1.getWidth());
        row4col2 = (EditText) findViewById(R.id.tableSlideRow4Col2);
        row4col2.setMaxWidth(row4col2.getWidth());
        row5col1 = (EditText) findViewById(R.id.tableSlideRow5Col1);
        row5col1.setMaxWidth(row5col1.getWidth());
        row5col2 = (EditText) findViewById(R.id.tableSlideRow5Col2);
        row5col2.setMaxWidth(row5col2.getWidth());
        row6col1 = (EditText) findViewById(R.id.tableSlideRow6Col1);
        row6col1.setMaxWidth(row6col1.getWidth());
        row6col2 = (EditText) findViewById(R.id.tableSlideRow6Col2);
        row6col2.setMaxWidth(row6col2.getWidth());
        row7col1 = (EditText) findViewById(R.id.tableSlideRow7Col1);
        row7col1.setMaxWidth(row7col1.getWidth());
        row7col2 = (EditText) findViewById(R.id.tableSlideRow7Col2);
        row7col2.setMaxWidth(row7col2.getWidth());
        row8col1 = (EditText) findViewById(R.id.tableSlideRow8Col1);
        row8col1.setMaxWidth(row8col1.getWidth());
        row8col2 = (EditText) findViewById(R.id.tableSlideRow8Col2);
        row8col2.setMaxWidth(row8col2.getWidth());
        row9col1 = (EditText) findViewById(R.id.tableSlideRow9Col1);
        row9col1.setMaxWidth(row9col1.getWidth());
        row9col2 = (EditText) findViewById(R.id.tableSlideRow9Col2);
        row9col2.setMaxWidth(row9col2.getWidth());
        row0col1 = (EditText) findViewById(R.id.tableSlideRow10Col1);
        row0col1.setMaxWidth(row0col1.getWidth());
        row0col2 = (EditText) findViewById(R.id.tableSlideRow10Col2);
        row0col2.setMaxWidth(row0col2.getWidth());
        nextSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int col1 = 0, col2 = 0;

                if (row1col1.getText().length() != 0) {
                    col1++;
                }
                if (row1col2.getText().length() != 0) {
                    col2++;
                }
                if (row2col1.getText().length() != 0) {
                    col1++;
                }
                if (row2col2.getText().length() != 0) {
                    col2++;
                }
                if (row3col1.getText().length() != 0) {
                    col1++;
                }
                if (row3col2.getText().length() != 0) {
                    col2++;
                }
                if (row4col1.getText().length() != 0) {
                    col1++;
                }
                if (row4col2.getText().length() != 0) {
                    col2++;
                }
                if (row5col1.getText().length() != 0) {
                    col1++;
                }
                if (row5col2.getText().length() != 0) {
                    col2++;
                }
                if (row6col1.getText().length() != 0) {
                    col1++;
                }
                if (row6col2.getText().length() != 0) {
                    col2++;
                }
                if (row7col1.getText().length() != 0) {
                    col1++;
                }
                if (row7col2.getText().length() != 0) {
                    col2++;
                }
                if (row8col1.getText().length() != 0) {
                    col1++;
                }
                if (row8col2.getText().length() != 0) {
                    col2++;
                }
                if (row9col1.getText().length() != 0) {
                    col1++;
                }
                if (row9col2.getText().length() != 0) {
                    col2++;
                }
                if (row0col1.getText().length() != 0) {
                    col1++;
                }
                if (row0col2.getText().length() != 0) {
                    col2++;
                }

                if (col1 == 0 && col2 == 0) {
                    Toast.makeText(MakeTableSlide.this, "You can't create an empty slide.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (col1 != col2) {
                    Toast.makeText(MakeTableSlide.this, "Please complete the table by filling in entire rows.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> temp = new ArrayList<>();
                temp.add(row1col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row1col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row2col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row2col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row3col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row3col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row4col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row4col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row5col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row5col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row6col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row6col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row7col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row7col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row8col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row8col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row9col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row9col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row0col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row0col2.getText().toString().trim().replace(",", "##comma##"));

                temp.removeAll(Arrays.asList("", null));

                String assembled = "";
                for (String s : temp) {
                    assembled = assembled + s + ",";
                }
                moduleMap.put("Slide_" + newSlideIndex, assembled.substring(0, assembled.length() - 1));

                HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                moduleMap.put("noOfSlides", String.valueOf(slideIndex));
                Intent addedSlide = new Intent(MakeTableSlide.this, AddSlide.class);
                addedSlide.putExtra("Module", moduleMap);
                addedSlide.putExtra("User", userMap);
                setResult(RESULT_OK, addedSlide);
                finish();
            }
        });

        finishButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int col1 = 0, col2 = 0;

                if (row1col1.getText().length() != 0) {
                    col1++;
                }
                if (row1col2.getText().length() != 0) {
                    col2++;
                }
                if (row2col1.getText().length() != 0) {
                    col1++;
                }
                if (row2col2.getText().length() != 0) {
                    col2++;
                }
                if (row3col1.getText().length() != 0) {
                    col1++;
                }
                if (row3col2.getText().length() != 0) {
                    col2++;
                }
                if (row4col1.getText().length() != 0) {
                    col1++;
                }
                if (row4col2.getText().length() != 0) {
                    col2++;
                }
                if (row5col1.getText().length() != 0) {
                    col1++;
                }
                if (row5col2.getText().length() != 0) {
                    col2++;
                }
                if (row6col1.getText().length() != 0) {
                    col1++;
                }
                if (row6col2.getText().length() != 0) {
                    col2++;
                }
                if (row7col1.getText().length() != 0) {
                    col1++;
                }
                if (row7col2.getText().length() != 0) {
                    col2++;
                }
                if (row8col1.getText().length() != 0) {
                    col1++;
                }
                if (row8col2.getText().length() != 0) {
                    col2++;
                }
                if (row9col1.getText().length() != 0) {
                    col1++;
                }
                if (row9col2.getText().length() != 0) {
                    col2++;
                }
                if (row0col1.getText().length() != 0) {
                    col1++;
                }
                if (row0col2.getText().length() != 0) {
                    col2++;
                }

                if (col1 == 0 && col2 == 0) {
                    Toast.makeText(MakeTableSlide.this, "You can't create an empty slide.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (col1 != col2) {
                    Toast.makeText(MakeTableSlide.this, "Please complete the table by filling in entire rows.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> temp = new ArrayList<>();
                temp.add(row1col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row1col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row2col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row2col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row3col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row3col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row4col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row4col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row5col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row5col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row6col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row6col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row7col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row7col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row8col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row8col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row9col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row9col2.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row0col1.getText().toString().trim().replace(",", "##comma##"));
                temp.add(row0col2.getText().toString().trim().replace(",", "##comma##"));

                temp.removeAll(Arrays.asList("", null));

                String assembled = "";
                for (String s : temp) {
                    assembled = assembled + s + ",";
                }
                moduleMap.put("Slide_" + newSlideIndex, assembled.substring(0, assembled.length() - 1));

                typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                moduleMap.put("noOfSlides", String.valueOf(slideIndex));
                Intent addedSlide = new Intent(MakeTableSlide.this, AddSlide.class);
                addedSlide.putExtra("Module", moduleMap);
                addedSlide.putExtra("User", userMap);
                addedSlide.putExtra("Finished", true);
                setResult(RESULT_OK, addedSlide);
                finish();
            }
        });
    }

    private void initVars() {
        topHint = (TextView) findViewById(R.id.tableSlideTag);
        finishButt = (Button) findViewById(R.id.tableSlideFinishButt);
        nextSlide = (Button) findViewById(R.id.nextSlideButt);
        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
    }

    /**
     * In order to ensure the user doesn't accidentally leave the activity, they are prompted to
     * repeat the BackPress action within 1 second.
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Your slide is not saved yet, please press 'Finish' to save your Module.", Toast.LENGTH_SHORT).show();
    }
}
