package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class AddSlide extends AppCompatActivity {

    JSONObject module;
    HashMap<String, Object> moduleMap = null;

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

        TextView selSlideHint = (TextView) findViewById(R.id.selectSlideHint);

        TextView textSlideTag = (TextView) findViewById(R.id.plaintextSlideTag);

        final ImageView textSlideImg = (ImageView) findViewById(R.id.plaintextImage);

        assert textSlideImg != null;

//        try {
//            module = new JSONObject(getIntent().getStringExtra("Module frame ready"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module frame ready");

        textSlideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    module.accumulate("Types of Slides", 1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                if (moduleMap.containsKey("typesOfSlides")) {
                    HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                    int slideCount = typesMap.keySet().size() + 1;
                    typesMap.put("" + slideCount, "1");
                    moduleMap.put("typesOfSlides", typesMap);
                } else {
                    HashMap<String, String> typesMap = new HashMap<String, String>();
                    typesMap.put("1", "1");
                    moduleMap.put("typesOfSlides", typesMap);
                }

                Intent addPlaintextSlide = new Intent(AddSlide.this, MakeTextSlide.class);
                addPlaintextSlide.putExtra("Module frame ready", moduleMap);
                Toast.makeText(AddSlide.this, "Adding Plaintext slide to module", Toast.LENGTH_SHORT).show();

                if (getIntent().hasExtra("Index of new slide")) {
                    addPlaintextSlide.putExtra("Index of new slide", getIntent().getStringExtra("Index of new slide"));
                    startActivityForResult(addPlaintextSlide, 3);
                } else {

                    startActivityForResult(addPlaintextSlide, 10);
                }

            }
        });

        TextView tableSlideTag = (TextView) findViewById(R.id.tableSlideTag);

        final ImageView tableSlideImg = (ImageView) findViewById(R.id.tableImage);
        assert tableSlideImg != null;

        tableSlideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert module != null;

//                try {
//                    module.accumulate("Types of Slides", 2);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                if (moduleMap.containsKey("typesOfSlides")) {
                    HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                    int slideCount = typesMap.keySet().size();
                    typesMap.put("" + slideCount + 1, "2");
                    moduleMap.put("typesOfSlides", typesMap);
                } else {
                    HashMap<String, String> typesMap = new HashMap<String, String>();
                    typesMap.put("1", "2");
                    moduleMap.put("typesOfSlides", typesMap);
                }

                Intent addPlaintextSlide = new Intent(AddSlide.this, MakeTableSlide.class);
                addPlaintextSlide.putExtra("Module frame ready", moduleMap);
                Toast.makeText(AddSlide.this, "Adding Table slide to module", Toast.LENGTH_SHORT).show();

                if (getIntent().hasExtra("Index of new slide")) {
                    addPlaintextSlide.putExtra("Index of new slide", getIntent().getStringExtra("Index of new slide"));
                    startActivityForResult(addPlaintextSlide, 3);
                } else {
                    startActivityForResult(addPlaintextSlide, 10);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {

            case 1:

//                try {
//                    module = new JSONObject(data.getStringExtra("Slide added to module"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                moduleMap = (HashMap<String, Object>) data.getSerializableExtra("Slide added to module");

                Toast.makeText(this, "Slide added to module", Toast.LENGTH_SHORT).show();

                break;

            case 2:

//                try {
//                    module = new JSONObject(data.getStringExtra("Last slide added to module"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                moduleMap = (HashMap<String, Object>) data.getSerializableExtra("Last slide added to module");


                Intent moduleComplete = new Intent(AddSlide.this, CreateModActivity.class);
                moduleComplete.putExtra("Module complete", moduleMap);
                setResult(1, moduleComplete);
                finish();
                break;
            case 3:
                moduleMap = (HashMap<String, Object>) data.getSerializableExtra("Module edited");
                Intent slideAddedToMod = new Intent(AddSlide.this, EditSelectedModule.class);
                slideAddedToMod.putExtra("Module edited", moduleMap);
                setResult(RESULT_OK, slideAddedToMod);
                finish();


                break;
            case 4:

                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }


}
