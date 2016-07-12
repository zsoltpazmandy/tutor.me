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

public class MakeTextSlide extends AppCompatActivity {

    JSONObject module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_text_slide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        TextView textSlideTopTag = (TextView) findViewById(R.id.textSlideTopTag);
        final EditText slideStringEdit = (EditText) findViewById(R.id.textSlideString);

        Button addSlideButt = (Button) findViewById(R.id.nextSlide);
        assert addSlideButt != null;
        final Button finishButt = (Button) findViewById(R.id.textSlideFinishButt);
        assert finishButt != null;

        try {
            module = new JSONObject(getIntent().getStringExtra("Module frame ready"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (getIntent().hasExtra("Slide to edit")) {
            setTitle("Editing slide");
            textSlideTopTag.setText("Edit current contents of the slide.");
            finishButt.setText("Cancel");
            addSlideButt.setText("Save slide");

            int slideEdited = Integer.parseInt(getIntent().getStringExtra("Slide to edit"));
            try {
                slideStringEdit.setText(module.getString("Slide " + slideEdited));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            finishButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });

            addSlideButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (slideStringEdit.getText().length() == 0) {
                        Toast.makeText(MakeTextSlide.this, "You can't make an empty slide", Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        String userInput = slideStringEdit.getText().toString();

                        try {
                            module.put("Slide " + getIntent().getStringExtra("Slide to edit"), userInput);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent editFinished = new Intent();
                        editFinished.putExtra("Module edited", module.toString());
                        setResult(RESULT_OK, editFinished);
                        finish();
                    }


                    // grab new text, add into module overriding existing slide,
                    // send it back to editselectedmodule in an intent
                    // have that class update the module and the whole thing in activity result
                }
            });

        } else {

            addSlideButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (slideStringEdit.getText().length() == 0) {
                        Toast.makeText(MakeTextSlide.this, "You can't make an empty slide", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userInput = slideStringEdit.getText().toString();

                    int amountOfSlides = 0;

                    try {
                        amountOfSlides = module.getJSONArray("Types of Slides").length();
                    } catch (JSONException e) {
                        amountOfSlides = 1;
                    }

                    try {
                        module.put("Slide " + amountOfSlides, userInput);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent slideAdded = new Intent(MakeTextSlide.this, AddSlide.class);
                    slideAdded.putExtra("Slide added to module", module.toString());
                    setResult(1, slideAdded);
                    finish();
                }
            });


            finishButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (slideStringEdit.getText().length() == 0) {
                        Toast.makeText(MakeTextSlide.this, "You can't make an empty slide", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userInput = slideStringEdit.getText().toString();

                    int amountOfSlides = 0;

                    try {
                        amountOfSlides = module.getJSONArray("Types of Slides").length();
                    } catch (JSONException e) {
                        amountOfSlides = 1;
                    }

                    try {
                        module.put("Slide " + amountOfSlides, userInput);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent addingSlidesOver = new Intent(MakeTextSlide.this, AddSlide.class);
                    addingSlidesOver.putExtra("Last slide added to module", module.toString());
                    setResult(2, addingSlidesOver);
                    finish();

                }
            });
        }
    }

//    @Override
//    public void onBackPressed() {
//        Toast.makeText(this, "Your slide is not saved yet, please press 'Finish' to save your Module.", Toast.LENGTH_SHORT).show();
//        return;
//    }
}
