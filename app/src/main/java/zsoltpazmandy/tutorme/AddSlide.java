package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AddSlide extends AppCompatActivity {

    JSONObject module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView selSlideHint = (TextView) findViewById(R.id.selectSlideHint);

        TextView textSlideTag = (TextView) findViewById(R.id.plaintextSlideTag);

        final ImageView textSlideImg = (ImageView) findViewById(R.id.plaintextImage);

        assert textSlideImg != null;

        try {
            module = new JSONObject(getIntent().getStringExtra("Module frame ready"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        textSlideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    module.accumulate("Types of Slides", "#");
                    module.accumulate("Types of Slides", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent addPlaintextSlide = new Intent(AddSlide.this, MakeTextSlide.class);
                addPlaintextSlide.putExtra("Module frame ready", module.toString());
                Toast.makeText(AddSlide.this, "Adding Plaintext slide to module", Toast.LENGTH_SHORT).show();
                startActivityForResult(addPlaintextSlide, 10);

            }
        });

        TextView tableSlideTag = (TextView) findViewById(R.id.tableSlideTag);

        final ImageView tableSlideImg = (ImageView) findViewById(R.id.tableImage);
        assert tableSlideImg != null;

        tableSlideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert module != null;

                try {
                    module.accumulate("Types of Slides", "#");
                    module.accumulate("Types of Slides", "2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent addPlaintextSlide = new Intent(AddSlide.this, MakeTableSlide.class);
                addPlaintextSlide.putExtra("Module frame ready", module.toString());
                Toast.makeText(AddSlide.this, "Adding Table slide to module", Toast.LENGTH_SHORT).show();
                startActivityForResult(addPlaintextSlide, 10);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                try {
                    module = new JSONObject(data.getStringExtra("Slide added to module"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Slide added to module", Toast.LENGTH_SHORT).show();
                break;
            case 2:

                try {
                    module = new JSONObject(data.getStringExtra("Last slide added to module"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent moduleComplete = new Intent(AddSlide.this, CreateModActivity.class);
                moduleComplete.putExtra("Module complete", module.toString());
                setResult(1, moduleComplete);
                finish();

                break;

            case 3:
                try {
                    module = new JSONObject(data.getStringExtra("Slide added to module"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, "Slide added to module", Toast.LENGTH_SHORT).show();

                break;
//            case 4:
//                module.clear();
//                module.addAll(data.getStringArrayListExtra("Slide added to module"));
//                Toast.makeText(this, "Slide added to module", Toast.LENGTH_SHORT).show();
//                Intent moduleComplete

        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}





















