package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddSlide extends AppCompatActivity {

    ArrayList<String> module = new ArrayList<>();

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

        module = getIntent().getStringArrayListExtra("Module frame");

        textSlideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPlaintextSlide = new Intent(AddSlide.this, MakeTextSlide.class);
                addPlaintextSlide.putStringArrayListExtra("Module frame", module);
                Toast.makeText(AddSlide.this, "Adding Plaintext slide to module", Toast.LENGTH_SHORT).show();
                startActivityForResult(addPlaintextSlide, 1);

            }
        });

        TextView tableSlideTag = (TextView) findViewById(R.id.tableSlideTag);

        final ImageView tableSlideImg = (ImageView) findViewById(R.id.tableImage);
        assert tableSlideImg != null;

        tableSlideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTableSlide = new Intent(AddSlide.this, MakeTableSlide.class);
                addTableSlide.putStringArrayListExtra("Module frame", module);
                Toast.makeText(AddSlide.this, "Adding text-table slide to module", Toast.LENGTH_SHORT).show();
                startActivityForResult(addTableSlide, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                module.clear();
                module.addAll(data.getStringArrayListExtra("Slide added to module"));
                Toast.makeText(this, "Slide added to module", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                module.clear();
                module.addAll(data.getStringArrayListExtra("Last slide added to module"));
                Intent moduleComplete = new Intent(AddSlide.this, CreateModActivity.class);
                moduleComplete.putStringArrayListExtra("Module complete", module);
                setResult(1, moduleComplete);
                finish();

        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
