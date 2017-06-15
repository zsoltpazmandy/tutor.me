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

import java.util.HashMap;

/**
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 * <p>
 * The activity is used for three different use cases:
 * 1. Adding a text slide when creating a new module;
 * 2. Adding a text slide to an existing module;
 * 3. Editing an existing text slide of an existing module;
 */
public class MakeTextSlide extends AppCompatActivity {

    private HashMap<String, Object> moduleMap = null;
    private HashMap<String, Object> userMap = null;

    private TextView textSlideTopTag;
    private EditText slideStringEdit;
    private Button addSlideButt;
    private Button finishButt;

    private int slideIndex;

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

        textSlideTopTag = (TextView) findViewById(R.id.textSlideTopTag);
        slideStringEdit = (EditText) findViewById(R.id.textSlideString);

        addSlideButt = (Button) findViewById(R.id.nextSlide);
        addSlideButt.setText("Save & Add slide");
        finishButt = (Button) findViewById(R.id.textSlideFinishButt);
        finishButt.setText("Save & Exit");

        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        if (!getIntent().hasExtra("Slide To Edit"))
            slideIndex = Integer.parseInt(getIntent().getStringExtra("Next Slide Index"));

        if (getIntent().hasExtra("New Module")) {
            addingToNewModule();
        } else if (getIntent().hasExtra("Adding Slide To Existing Module")) {
            addingToExistingModule();
        } else {
            editingExistingModulesSlide();
        }
    }

    private void editingExistingModulesSlide() {
        setTitle("Edit module: edit slide");
        textSlideTopTag.setText("Edit current contents of the slide.");
        finishButt.setText("Cancel");
        addSlideButt.setText("Save slide");

        int slideEdited = Integer.parseInt(getIntent().getStringExtra("Slide To Edit"));
        slideStringEdit.setText(moduleMap.get("Slide_" + slideEdited).toString());

        finishButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editFinished = new Intent(MakeTextSlide.this, EditSelectedModule.class);
                editFinished.putExtra("Module", moduleMap);
                editFinished.putExtra("User", userMap);
                setResult(RESULT_CANCELED, editFinished);
                finish();
            }
        });

        addSlideButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideStringEdit.getText().length() == 0) {
                    Toast.makeText(MakeTextSlide.this, "You can't make an empty slide", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userInput = slideStringEdit.getText().toString().trim();
                moduleMap.put("Slide_" + getIntent().getStringExtra("Slide To Edit"), userInput);
                Intent editFinished = new Intent(MakeTextSlide.this, EditSelectedModule.class);
                editFinished.putExtra("Module", moduleMap);
                editFinished.putExtra("User", userMap);
                setResult(RESULT_OK, editFinished);
                finish();
            }
        });
    }

    private void addingToExistingModule() {
        setTitle("Edit module: add text slide");
        finishButt.setText("Cancel");
        addSlideButt.setText("Save slide");
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
                }
                String userInput = slideStringEdit.getText().toString().trim();
                moduleMap.put("Slide_" + slideIndex, userInput);
                moduleMap.put("noOfSlides", String.valueOf(slideIndex));
                Intent addedSlide = new Intent(MakeTextSlide.this, AddSlide.class);
                addedSlide.putExtra("Module", moduleMap);
                addedSlide.putExtra("User", userMap);
                addedSlide.putExtra("Finished", true);
                setResult(RESULT_OK, addedSlide);
                finish();
            }
        });
    }

    private void addingToNewModule() {
        setTitle("Create module: new text slide");
        addSlideButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideStringEdit.getText().length() == 0) {
                    Toast.makeText(MakeTextSlide.this, "You can't make an empty slide", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userInput = slideStringEdit.getText().toString().trim();
                moduleMap.put("Slide_" + slideIndex, userInput);
                moduleMap.put("noOfSlides", String.valueOf(slideIndex));
                Intent backToAddSlide = new Intent(MakeTextSlide.this, AddSlide.class);
                backToAddSlide.putExtra("Module", moduleMap);
                backToAddSlide.putExtra("User", userMap);
                backToAddSlide.putExtra("New Module", true);
                setResult(RESULT_OK, backToAddSlide);
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
                String userInput = slideStringEdit.getText().toString().trim();
                moduleMap.put("Slide_" + slideIndex, userInput);
                moduleMap.put("noOfSlides", String.valueOf(slideIndex));
                Intent addingSlidesOver = new Intent(MakeTextSlide.this, AddSlide.class);
                addingSlidesOver.putExtra("Module", moduleMap);
                addingSlidesOver.putExtra("User", userMap);
                addingSlidesOver.putExtra("Finished", "");
                setResult(RESULT_OK, addingSlidesOver);
                finish();
            }
        });
    }

    /**
     * In order to ensure the user doesn't accidentally leave the activity, they are prompted to
     * repeat the BackPress action within 1 second.
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Your slide is not saved yet, please press 'Save & Exit' if you're done with this Module.", Toast.LENGTH_SHORT).show();
    }
}