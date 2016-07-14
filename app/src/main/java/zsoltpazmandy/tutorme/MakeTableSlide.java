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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MakeTableSlide extends AppCompatActivity {

    JSONObject module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_table_slide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView topHint = (TextView) findViewById(R.id.tableSlideTag);
        Button finishButt = (Button) findViewById(R.id.tableSlideFinishButt);
        assert finishButt != null;
        Button nextSlide = (Button) findViewById(R.id.nextSlideButt);
        assert nextSlide != null;

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        try {
            module = new JSONObject(getIntent().getStringExtra("Module frame ready"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (getIntent().hasExtra("Slide to edit")) {

            setTitle("Editing slide");
            topHint.setText("Edit current contents of the slide. Remember to keep target words in the left column, these are the ones you want your Learner to learn, we'll use this information to generate quizzes later.");
            finishButt.setText("Cancel");
            nextSlide.setText("Save slide");

            final int slideEdited = Integer.parseInt(getIntent().getStringExtra("Slide to edit"));


            String tableRaw = "";
            ArrayList<String> tableSlide = new ArrayList<>();

            for (int i = 0; i < 20; i++) {
                tableSlide.add(i, "");
            }

            try {

                tableRaw = module.getString("Slide " + slideEdited);
                tableRaw = tableRaw.replace("[", "").replace("]", "").replace("\"", "");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] temp = new String[20];
            temp = tableRaw.split(",");

            for (int i = 0; i < temp.length; i++) {
                tableSlide.add(i, temp[i].replace("##comma##", ","));
            }


            final EditText row1col1 = (EditText) findViewById(R.id.tableSlideRow1Col1);
            assert row1col1 != null;
            row1col1.setMaxWidth(row1col1.getWidth());
            row1col1.setText(tableSlide.get(0));
            final EditText row1col2 = (EditText) findViewById(R.id.tableSlideRow1Col2);
            assert row1col2 != null;
            row1col2.setMaxWidth(row1col2.getWidth());
            row1col2.setText(tableSlide.get(1));
            final EditText row2col1 = (EditText) findViewById(R.id.tableSlideRow2Col1);
            assert row2col1 != null;
            row2col1.setMaxWidth(row2col1.getWidth());
            row2col1.setText(tableSlide.get(2));
            final EditText row2col2 = (EditText) findViewById(R.id.tableSlideRow2Col2);
            assert row2col2 != null;
            row2col2.setMaxWidth(row2col2.getWidth());
            row2col2.setText(tableSlide.get(3));
            final EditText row3col1 = (EditText) findViewById(R.id.tableSlideRow3Col1);
            assert row3col1 != null;
            row3col1.setMaxWidth(row3col1.getWidth());
            row3col1.setText(tableSlide.get(4));
            final EditText row3col2 = (EditText) findViewById(R.id.tableSlideRow3Col2);
            assert row3col2 != null;
            row3col2.setMaxWidth(row3col2.getWidth());
            row3col2.setText(tableSlide.get(5));
            final EditText row4col1 = (EditText) findViewById(R.id.tableSlideRow4Col1);
            assert row4col1 != null;
            row4col1.setMaxWidth(row4col1.getWidth());
            row4col1.setText(tableSlide.get(6));
            final EditText row4col2 = (EditText) findViewById(R.id.tableSlideRow4Col2);
            assert row4col2 != null;
            row4col2.setMaxWidth(row4col2.getWidth());
            row4col2.setText(tableSlide.get(7));
            final EditText row5col1 = (EditText) findViewById(R.id.tableSlideRow5Col1);
            assert row5col1 != null;
            row5col1.setMaxWidth(row5col1.getWidth());
            row5col1.setText(tableSlide.get(8));
            final EditText row5col2 = (EditText) findViewById(R.id.tableSlideRow5Col2);
            assert row5col2 != null;
            row5col2.setMaxWidth(row5col2.getWidth());
            row5col2.setText(tableSlide.get(9));
            final EditText row6col1 = (EditText) findViewById(R.id.tableSlideRow6Col1);
            assert row6col1 != null;
            row6col1.setMaxWidth(row6col1.getWidth());
            row6col1.setText(tableSlide.get(10));
            final EditText row6col2 = (EditText) findViewById(R.id.tableSlideRow6Col2);
            assert row6col2 != null;
            row6col2.setMaxWidth(row6col2.getWidth());
            row6col2.setText(tableSlide.get(11));
            final EditText row7col1 = (EditText) findViewById(R.id.tableSlideRow7Col1);
            assert row7col1 != null;
            row7col1.setMaxWidth(row7col1.getWidth());
            row7col1.setText(tableSlide.get(12));
            final EditText row7col2 = (EditText) findViewById(R.id.tableSlideRow7Col2);
            assert row7col2 != null;
            row7col2.setMaxWidth(row7col2.getWidth());
            row7col2.setText(tableSlide.get(13));
            final EditText row8col1 = (EditText) findViewById(R.id.tableSlideRow8Col1);
            assert row8col1 != null;
            row8col1.setMaxWidth(row8col1.getWidth());
            row8col1.setText(tableSlide.get(14));
            final EditText row8col2 = (EditText) findViewById(R.id.tableSlideRow8Col2);
            assert row8col2 != null;
            row8col2.setMaxWidth(row8col2.getWidth());
            row8col2.setText(tableSlide.get(15));
            final EditText row9col1 = (EditText) findViewById(R.id.tableSlideRow9Col1);
            assert row9col1 != null;
            row9col1.setMaxWidth(row9col1.getWidth());
            row9col1.setText(tableSlide.get(16));
            final EditText row9col2 = (EditText) findViewById(R.id.tableSlideRow9Col2);
            assert row9col2 != null;
            row9col2.setMaxWidth(row9col2.getWidth());
            row9col2.setText(tableSlide.get(17));
            final EditText row0col1 = (EditText) findViewById(R.id.tableSlideRow10Col1);
            assert row0col1 != null;
            row0col1.setMaxWidth(row0col1.getWidth());
            row0col1.setText(tableSlide.get(18));
            final EditText row0col2 = (EditText) findViewById(R.id.tableSlideRow10Col2);
            assert row0col2 != null;
            row0col2.setMaxWidth(row0col2.getWidth());
            row0col2.setText(tableSlide.get(19));


            finishButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_CANCELED);
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

                    module.remove("Slide " + slideEdited);

                    for (String s : temp) {
                        try {
                            module.accumulate("Slide " + slideEdited, s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent editFinished = new Intent();
                    editFinished.putExtra("Module edited", module.toString());
                    setResult(RESULT_OK, editFinished);
                    finish();
                }


            });

        } else if ((getIntent().hasExtra("Index of new slide"))) {


            setTitle("Adding Table Slide");
            finishButt.setText("Cancel");
            nextSlide.setText("Save slide");

            final int newSlideIndex = Integer.parseInt(getIntent().getStringExtra("Index of new slide"));


            final EditText row1col1 = (EditText) findViewById(R.id.tableSlideRow1Col1);
            assert row1col1 != null;
            row1col1.setMaxWidth(row1col1.getWidth());

            final EditText row1col2 = (EditText) findViewById(R.id.tableSlideRow1Col2);
            assert row1col2 != null;
            row1col2.setMaxWidth(row1col2.getWidth());

            final EditText row2col1 = (EditText) findViewById(R.id.tableSlideRow2Col1);
            assert row2col1 != null;
            row2col1.setMaxWidth(row2col1.getWidth());

            final EditText row2col2 = (EditText) findViewById(R.id.tableSlideRow2Col2);
            assert row2col2 != null;
            row2col2.setMaxWidth(row2col2.getWidth());

            final EditText row3col1 = (EditText) findViewById(R.id.tableSlideRow3Col1);
            assert row3col1 != null;
            row3col1.setMaxWidth(row3col1.getWidth());

            final EditText row3col2 = (EditText) findViewById(R.id.tableSlideRow3Col2);
            assert row3col2 != null;
            row3col2.setMaxWidth(row3col2.getWidth());

            final EditText row4col1 = (EditText) findViewById(R.id.tableSlideRow4Col1);
            assert row4col1 != null;
            row4col1.setMaxWidth(row4col1.getWidth());

            final EditText row4col2 = (EditText) findViewById(R.id.tableSlideRow4Col2);
            assert row4col2 != null;
            row4col2.setMaxWidth(row4col2.getWidth());

            final EditText row5col1 = (EditText) findViewById(R.id.tableSlideRow5Col1);
            assert row5col1 != null;
            row5col1.setMaxWidth(row5col1.getWidth());

            final EditText row5col2 = (EditText) findViewById(R.id.tableSlideRow5Col2);
            assert row5col2 != null;
            row5col2.setMaxWidth(row5col2.getWidth());

            final EditText row6col1 = (EditText) findViewById(R.id.tableSlideRow6Col1);
            assert row6col1 != null;
            row6col1.setMaxWidth(row6col1.getWidth());

            final EditText row6col2 = (EditText) findViewById(R.id.tableSlideRow6Col2);
            assert row6col2 != null;
            row6col2.setMaxWidth(row6col2.getWidth());

            final EditText row7col1 = (EditText) findViewById(R.id.tableSlideRow7Col1);
            assert row7col1 != null;
            row7col1.setMaxWidth(row7col1.getWidth());

            final EditText row7col2 = (EditText) findViewById(R.id.tableSlideRow7Col2);
            assert row7col2 != null;
            row7col2.setMaxWidth(row7col2.getWidth());

            final EditText row8col1 = (EditText) findViewById(R.id.tableSlideRow8Col1);
            assert row8col1 != null;
            row8col1.setMaxWidth(row8col1.getWidth());

            final EditText row8col2 = (EditText) findViewById(R.id.tableSlideRow8Col2);
            assert row8col2 != null;
            row8col2.setMaxWidth(row8col2.getWidth());

            final EditText row9col1 = (EditText) findViewById(R.id.tableSlideRow9Col1);
            assert row9col1 != null;
            row9col1.setMaxWidth(row9col1.getWidth());

            final EditText row9col2 = (EditText) findViewById(R.id.tableSlideRow9Col2);
            assert row9col2 != null;
            row9col2.setMaxWidth(row9col2.getWidth());

            final EditText row0col1 = (EditText) findViewById(R.id.tableSlideRow10Col1);
            assert row0col1 != null;
            row0col1.setMaxWidth(row0col1.getWidth());

            final EditText row0col2 = (EditText) findViewById(R.id.tableSlideRow10Col2);
            assert row0col2 != null;
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

                    for (String s : temp) {
                        try {
                            module.accumulate("Slide " + newSlideIndex, s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    int amountOfSlides = 0;

                    String temp2;

                    try {
                        temp2 = module.getString("Types of Slides");
                        temp2 = temp2.replace("[", "").replace("]", "");

                        amountOfSlides = temp2.split(",").length;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        module.put("No. of Slides", amountOfSlides);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent addedSlide = new Intent();
                    addedSlide.putExtra("Module edited", module.toString());
                    setResult(3, addedSlide);
                    finish();


                }
            });

        } else {

            final EditText row1col1 = (EditText) findViewById(R.id.tableSlideRow1Col1);
            assert row1col1 != null;
            row1col1.setMaxWidth(row1col1.getWidth());

            final EditText row1col2 = (EditText) findViewById(R.id.tableSlideRow1Col2);
            assert row1col2 != null;
            row1col2.setMaxWidth(row1col2.getWidth());

            final EditText row2col1 = (EditText) findViewById(R.id.tableSlideRow2Col1);
            assert row2col1 != null;
            row2col1.setMaxWidth(row2col1.getWidth());

            final EditText row2col2 = (EditText) findViewById(R.id.tableSlideRow2Col2);
            assert row2col2 != null;
            row2col2.setMaxWidth(row2col2.getWidth());

            final EditText row3col1 = (EditText) findViewById(R.id.tableSlideRow3Col1);
            assert row3col1 != null;
            row3col1.setMaxWidth(row3col1.getWidth());

            final EditText row3col2 = (EditText) findViewById(R.id.tableSlideRow3Col2);
            assert row3col2 != null;
            row3col2.setMaxWidth(row3col2.getWidth());

            final EditText row4col1 = (EditText) findViewById(R.id.tableSlideRow4Col1);
            assert row4col1 != null;
            row4col1.setMaxWidth(row4col1.getWidth());

            final EditText row4col2 = (EditText) findViewById(R.id.tableSlideRow4Col2);
            assert row4col2 != null;
            row4col2.setMaxWidth(row4col2.getWidth());

            final EditText row5col1 = (EditText) findViewById(R.id.tableSlideRow5Col1);
            assert row5col1 != null;
            row5col1.setMaxWidth(row5col1.getWidth());

            final EditText row5col2 = (EditText) findViewById(R.id.tableSlideRow5Col2);
            assert row5col2 != null;
            row5col2.setMaxWidth(row5col2.getWidth());

            final EditText row6col1 = (EditText) findViewById(R.id.tableSlideRow6Col1);
            assert row6col1 != null;
            row6col1.setMaxWidth(row6col1.getWidth());

            final EditText row6col2 = (EditText) findViewById(R.id.tableSlideRow6Col2);
            assert row6col2 != null;
            row6col2.setMaxWidth(row6col2.getWidth());

            final EditText row7col1 = (EditText) findViewById(R.id.tableSlideRow7Col1);
            assert row7col1 != null;
            row7col1.setMaxWidth(row7col1.getWidth());

            final EditText row7col2 = (EditText) findViewById(R.id.tableSlideRow7Col2);
            assert row7col2 != null;
            row7col2.setMaxWidth(row7col2.getWidth());

            final EditText row8col1 = (EditText) findViewById(R.id.tableSlideRow8Col1);
            assert row8col1 != null;
            row8col1.setMaxWidth(row8col1.getWidth());

            final EditText row8col2 = (EditText) findViewById(R.id.tableSlideRow8Col2);
            assert row8col2 != null;
            row8col2.setMaxWidth(row8col2.getWidth());

            final EditText row9col1 = (EditText) findViewById(R.id.tableSlideRow9Col1);
            assert row9col1 != null;
            row9col1.setMaxWidth(row9col1.getWidth());

            final EditText row9col2 = (EditText) findViewById(R.id.tableSlideRow9Col2);
            assert row9col2 != null;
            row9col2.setMaxWidth(row9col2.getWidth());

            final EditText row0col1 = (EditText) findViewById(R.id.tableSlideRow10Col1);
            assert row0col1 != null;
            row0col1.setMaxWidth(row0col1.getWidth());

            final EditText row0col2 = (EditText) findViewById(R.id.tableSlideRow10Col2);
            assert row0col2 != null;
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

                    int amountOfSlides = 0;

                    try {
                        amountOfSlides = module.getJSONArray("Types of Slides").length();

                    } catch (JSONException e) {
                        amountOfSlides = 1;
                    }

                    temp.removeAll(Arrays.asList("", null));

                    for (String s : temp) {
                        try {
                            module.accumulate("Slide " + amountOfSlides, s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent addNextSlide = new Intent(MakeTableSlide.this, AddSlide.class);
                    addNextSlide.putExtra("Slide added to module", module.toString());
                    setResult(1, addNextSlide);
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

                    int amountOfSlides = 0;

                    try {
                        amountOfSlides = module.getJSONArray("Types of Slides").length();
                    } catch (JSONException e) {
                        amountOfSlides = 1;
                    }

                    temp.removeAll(Arrays.asList("", null));

                    for (String s : temp) {
                        try {
                            module.accumulate("Slide " + amountOfSlides, s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent addingSlidesOver = new Intent(MakeTableSlide.this, AddSlide.class);
                    addingSlidesOver.putExtra("Last slide added to module", module.toString());
                    setResult(2, addingSlidesOver);
                    finish();
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Your slide is not saved yet, please press 'Finish' to save your Module.", Toast.LENGTH_SHORT).show();
        return;
    }
}
