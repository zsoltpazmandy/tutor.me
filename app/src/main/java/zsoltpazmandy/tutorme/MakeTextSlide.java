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

public class MakeTextSlide extends AppCompatActivity {

    ArrayList<String> module = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_text_slide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textSlideTopTag = (TextView) findViewById(R.id.textSlideTopTag);
        final EditText slideStringEdit = (EditText) findViewById(R.id.textSlideString);

        Button addSlideButt = (Button) findViewById(R.id.nextSlide);
        assert addSlideButt != null;

        module = getIntent().getStringArrayListExtra("Module frame");

        addSlideButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (slideStringEdit.getText().length() == 0) {
                    Toast.makeText(MakeTextSlide.this, "You can't make an empty slide", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userInput = slideStringEdit.getText().toString();
                module.add(userInput);

                Intent slideAdded = new Intent(MakeTextSlide.this, AddSlide.class);
                slideAdded.putStringArrayListExtra("Slide added to module", module);
                setResult(1, slideAdded);
                finish();
            }
        });

        Button finishButt = (Button) findViewById(R.id.textSlideFinishButt);
        assert finishButt != null;

        finishButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideStringEdit.getText().length() == 0) {
                    Toast.makeText(MakeTextSlide.this, "You can't make an empty slide", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userInput = slideStringEdit.getText().toString();
                module.add(userInput);

                Intent addingSlidesOver = new Intent(MakeTextSlide.this, AddSlide.class);
                addingSlidesOver.putStringArrayListExtra("Last slide added to module", module);
                setResult(2, addingSlidesOver);
                finish();

            }
        });
    }
}
