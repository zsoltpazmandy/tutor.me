package zsoltpazmandy.tutorme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewModule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_module);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JSONObject user = null;
        JSONObject module = null;

        try {
            user = new JSONObject(getIntent().getStringExtra("User"));
            module = new JSONObject(getIntent().getStringExtra("Module"));
            this.setTitle(module.getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView nameOfModule = (TextView) findViewById(R.id.view_module_name_of_module_view);
        TextView nameOfAuth = (TextView) findViewById(R.id.view_module_author_of_module_view);
        TextView moduleRating = (TextView) findViewById(R.id.view_module_rating);
        TextView authRating = (TextView) findViewById(R.id.view_author_rating);
        TextView tutorName = (TextView) findViewById(R.id.view_module_tutors_name);
        TextView tutorRating = (TextView) findViewById(R.id.view_module_tutors_rating);
        TextView progressHeader = (TextView) findViewById(R.id.view_module_progress_header);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.view_module_progressbar);
        TextView progressText = (TextView) findViewById(R.id.view_module_progress_text);
        TextView moduleDesc = (TextView) findViewById(R.id.view_module_description);
        Button reviewButt = (Button) findViewById(R.id.view_module_review_butt);
        Button startButt = (Button) findViewById(R.id.view_module_start_butt);

        try {
            nameOfModule.setText(module.getString("Name"));
            nameOfAuth.setText("written by " + module.getString("Author"));
            moduleRating.setText("Module *****");
            authRating.setText("Author *****");
            tutorName.setText("Your tutor: " + module.getString("Author"));
            tutorRating.setText("Tutor *****");
            progressBar.setMax(module.getInt("No. of Slides"));
            progressBar.setProgress(user.getInt("Progress" + module.getString("ID")));
            progressText.setText("" + user.getInt("Progress" + module.getString("ID")) + " of " + module.getInt("No. of Slides") + " slides.");
            moduleDesc.setText(module.getString("Description"));

            reviewButt.setText("Review");

            if (user.getInt("Progress" + module.getString("ID")) != module.getInt("No. of Slides")) {
                if (user.getInt("Progress" + module.getString("ID")) == 0) {
                    reviewButt.setEnabled(false);
                    startButt.setText("Start");
                    startButt.setEnabled(true);
                } else {
                    reviewButt.setEnabled(true);
                    startButt.setText("Continue");
                    startButt.setEnabled(true);
                }
            } else {
                //module completed
            }

            assert startButt != null;
            startButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ViewModule.this, "Opening Module", Toast.LENGTH_SHORT).show();
                }
            });

            reviewButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ViewModule.this, "Reviewing Module", Toast.LENGTH_SHORT).show();

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
