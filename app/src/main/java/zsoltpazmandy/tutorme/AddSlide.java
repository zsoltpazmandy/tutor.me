package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * Part of the Create module sequence; used to allow the user to pick the type of the slide that
 * is to be added to the module. It is accessed in two ways, depending on whether the module is
 * an already existing one, or one that the user had created at an earlier time and is now adding
 * additional slides to that module. The difference between the two use cases is flagged by the
 * extra on the intent: "Adding Slide To Existing Module" vs "New Module".
 *
 * The user may select the desired slide type by pressing the selected icon. At the present time
 * there are only two types of slides the user may choose from (Plaintext or Table) which two both
 * inherit from the TextSlide superclass. Additional slide types (currently features planned for
 * future extension) could include: Animation slide, Audio/Video stream slide, Drawing board slide,
 * etc.
 */
public class AddSlide extends AppCompatActivity {

    private HashMap<String, Object> moduleMap = null;
    private HashMap<String, Object> userMap = null;

    private final int CREATE_MODULE_ADD_TEXT_SLIDE = 3;
    private final int CREATE_MODULE_ADD_TABLE_SLIDE = 4;
    private final int EDIT_MODULE_ADD_TEXT_SLIDE = 5;
    private final int EDIT_MODULE_ADD_TABLE_SLIDE = 6;
    private final int EDIT_MODULE_EDIT_TEXT_SLIDE = 7;
    private final int EDIT_MODULE_EDIT_TABLE_SLIDE = 8;

    private int noOfSlides;

    private ImageView tableSlideImg;
    private ImageView textSlideImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slide);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initVars();
        textSlideImgListener();
        tableSlideImgListener();
    }

    private void initVars() {
        TextView selSlideHint = (TextView) findViewById(R.id.selectSlideHint);
        TextView textSlideTag = (TextView) findViewById(R.id.plaintextSlideTag);

        textSlideImg = (ImageView) findViewById(R.id.plaintextImage);

        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        noOfSlides = Integer.parseInt(moduleMap.get("noOfSlides").toString());

        TextView tableSlideTag = (TextView) findViewById(R.id.tableSlideTag);
        tableSlideImg = (ImageView) findViewById(R.id.tableImage);
    }

    private void textSlideImgListener() {
        textSlideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddSlide.this, "Adding text slide to module", Toast.LENGTH_SHORT).show();

                if (getIntent().hasExtra("New Module")) { // CREATE NEW MODULE
                    if (noOfSlides == 0) { // FIRST SLIDE
                        HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                        typesMap.remove("none");
                        typesMap.put("Slide_1", "1");
                        moduleMap.put("typesOfSlides", typesMap);
                        Intent addSlideIntent = new Intent(AddSlide.this, MakeTextSlide.class);
                        addSlideIntent.putExtra("New Module", true);
                        addSlideIntent.putExtra("Module", moduleMap);
                        addSlideIntent.putExtra("User", userMap);
                        int nextSlideIndex = noOfSlides + 1;
                        addSlideIntent.putExtra("Next Slide Index", String.valueOf(nextSlideIndex));
                        startActivityForResult(addSlideIntent, CREATE_MODULE_ADD_TEXT_SLIDE);
                    } else { // NOT THE FIRST SLIDE
                        HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                        int nextSlideIndex = noOfSlides + 1;
                        typesMap.put("Slide_" + nextSlideIndex, "1");
                        moduleMap.put("typesOfSlides", typesMap);
                        Intent addSlideIntent = new Intent(AddSlide.this, MakeTextSlide.class);
                        addSlideIntent.putExtra("Module", moduleMap);
                        addSlideIntent.putExtra("User", userMap);
                        addSlideIntent.putExtra("New Module", true);
                        addSlideIntent.putExtra("Next Slide Index", String.valueOf(nextSlideIndex));
                        startActivityForResult(addSlideIntent, CREATE_MODULE_ADD_TEXT_SLIDE);
                    }
                } else { // ADDING SLIDES TO EXISTING MODULE
                    HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                    int nextSlideIndex = noOfSlides + 1;
                    typesMap.put("Slide_" + nextSlideIndex, "1");
                    moduleMap.put("typesOfSlides", typesMap);
                    Intent addSlideIntent = new Intent(AddSlide.this, MakeTextSlide.class);
                    addSlideIntent.putExtra("Module", moduleMap);
                    addSlideIntent.putExtra("User", userMap);
                    addSlideIntent.putExtra("Adding Slide To Existing Module", true);
                    addSlideIntent.putExtra("Next Slide Index", String.valueOf(nextSlideIndex));
                    startActivityForResult(addSlideIntent, EDIT_MODULE_ADD_TEXT_SLIDE);
                }
            }
        });
    }

    private void tableSlideImgListener() {
        tableSlideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("New Module")) { // CREATE NEW MODULE
                    if (noOfSlides == 0) { // FIRST SLIDE
                        HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                        typesMap.remove("none");
                        typesMap.put("Slide_1", "2");
                        moduleMap.put("typesOfSlides", typesMap);
                        Intent addSlideIntent = new Intent(AddSlide.this, MakeTableSlide.class);
                        addSlideIntent.putExtra("New Module", true);
                        addSlideIntent.putExtra("Module", moduleMap);
                        addSlideIntent.putExtra("User", userMap);
                        int nextSlideIndex = noOfSlides + 1;
                        addSlideIntent.putExtra("Next Slide Index", String.valueOf(nextSlideIndex));
                        startActivityForResult(addSlideIntent, CREATE_MODULE_ADD_TABLE_SLIDE);
                    } else { // NOT THE FIRST SLIDE
                        HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                        int nextSlideIndex = noOfSlides + 1;
                        typesMap.put("Slide_" + nextSlideIndex, "2");
                        moduleMap.put("typesOfSlides", typesMap);
                        Intent addSlideIntent = new Intent(AddSlide.this, MakeTableSlide.class);
                        addSlideIntent.putExtra("Module", moduleMap);
                        addSlideIntent.putExtra("User", userMap);
                        addSlideIntent.putExtra("New Module", true);
                        addSlideIntent.putExtra("Next Slide Index", String.valueOf(nextSlideIndex));
                        startActivityForResult(addSlideIntent, CREATE_MODULE_ADD_TABLE_SLIDE);
                    }
                } else { // ADDING SLIDES TO EXISTING MODULE
                    HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                    int nextSlideIndex = noOfSlides + 1;
                    typesMap.put("Slide_" + nextSlideIndex, "2");
                    moduleMap.put("typesOfSlides", typesMap);
                    Intent addSlideIntent = new Intent(AddSlide.this, MakeTableSlide.class);
                    addSlideIntent.putExtra("Module", moduleMap);
                    addSlideIntent.putExtra("User", userMap);
                    addSlideIntent.putExtra("Adding Slide To Existing Module", true);
                    addSlideIntent.putExtra("Next Slide Index", String.valueOf(nextSlideIndex));
                    startActivityForResult(addSlideIntent, EDIT_MODULE_ADD_TABLE_SLIDE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        moduleMap = (HashMap<String, Object>) data.getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) data.getSerializableExtra("User");

        int EDIT_MODULE_ADD_SLIDE = 2;
        int CREATE_MODULE_ADD_SLIDE = 1;
        if (requestCode == EDIT_MODULE_ADD_SLIDE) {
            if (requestCode == RESULT_OK) {
                Intent slideAdded = new Intent(AddSlide.this, CreateModActivity.class);
                slideAdded.putExtra("User", userMap);
                slideAdded.putExtra("Module", moduleMap);
                setResult(RESULT_OK, slideAdded);
                finish();
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        } else if (requestCode == CREATE_MODULE_ADD_SLIDE) {
            if (resultCode == RESULT_OK) {
                Intent textSlideAdded = new Intent(AddSlide.this, CreateModActivity.class);
                textSlideAdded.putExtra("User", userMap);
                textSlideAdded.putExtra("Module", moduleMap);
                setResult(RESULT_OK, textSlideAdded);
                finish();
            }
        } else if (requestCode == CREATE_MODULE_ADD_TEXT_SLIDE) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("Finished")) {
                    Intent textSlideAdded = new Intent(AddSlide.this, CreateModActivity.class);
                    textSlideAdded.putExtra("User", userMap);
                    textSlideAdded.putExtra("Module", moduleMap);
                    setResult(RESULT_OK, textSlideAdded);
                    finish();
                } else {
                    Intent textSlideAdded = new Intent(AddSlide.this, AddSlide.class);
                    textSlideAdded.putExtra("User", userMap);
                    textSlideAdded.putExtra("Module", moduleMap);
                    textSlideAdded.putExtra("New Module", true);
                    startActivityForResult(textSlideAdded, CREATE_MODULE_ADD_SLIDE);
                    Toast.makeText(AddSlide.this, "Text slide added", Toast.LENGTH_SHORT).show();
                }
            }

            if (resultCode == RESULT_CANCELED) {
                setResult(RESULT_CANCELED);
                finish();
            }
        } else if (requestCode == CREATE_MODULE_ADD_TABLE_SLIDE) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("Finished")) {
                    Intent tableSlideAdded = new Intent(AddSlide.this, CreateModActivity.class);
                    tableSlideAdded.putExtra("User", userMap);
                    tableSlideAdded.putExtra("Module", moduleMap);
                    setResult(RESULT_OK, tableSlideAdded);
                    finish();
                } else {
                    Intent tableSlideAdded = new Intent(AddSlide.this, AddSlide.class);
                    tableSlideAdded.putExtra("User", userMap);
                    tableSlideAdded.putExtra("Module", moduleMap);
                    tableSlideAdded.putExtra("New Module", true);
                    startActivityForResult(tableSlideAdded, CREATE_MODULE_ADD_SLIDE);
                    Toast.makeText(AddSlide.this, "Table slide added", Toast.LENGTH_SHORT).show();
                }
            }

            if (resultCode == RESULT_CANCELED) {
                setResult(RESULT_CANCELED);
                finish();
            }
        } else if (requestCode == EDIT_MODULE_ADD_TEXT_SLIDE) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("Finished")) {
                    Intent textSlideAdded = new Intent(AddSlide.this, EditSelectedModule.class);
                    textSlideAdded.putExtra("User", userMap);
                    textSlideAdded.putExtra("Module", moduleMap);
                    setResult(RESULT_OK, textSlideAdded);
                    finish();
                }
            }

            if (resultCode == RESULT_CANCELED) {
                setResult(RESULT_CANCELED);
                finish();
            }
        } else if (requestCode == EDIT_MODULE_ADD_TABLE_SLIDE) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("Finished")) {
                    Intent tableSlideAdded = new Intent(AddSlide.this, EditSelectedModule.class);
                    tableSlideAdded.putExtra("User", userMap);
                    tableSlideAdded.putExtra("Module", moduleMap);
                    setResult(RESULT_OK, tableSlideAdded);
                    finish();
                }
            }

            if (resultCode == RESULT_CANCELED) {
                setResult(RESULT_CANCELED);
                finish();
            }
        } else {
        }
    }

    @Override
    public void onBackPressed() {
    }
}
