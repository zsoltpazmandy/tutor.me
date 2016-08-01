package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ModuleProgress extends AppCompatActivity {

    JSONObject user = null;
    JSONObject tutor = null;
    JSONObject module = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Module f = new Module();

        try {
            this.user = new JSONObject(getIntent().getStringExtra("User"));
            this.module = new JSONObject(getIntent().getStringExtra("Module"));
            this.setTitle("Module information");
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
        final TextView moduleDesc = (TextView) findViewById(R.id.view_module_description);
        Button startButt = (Button) findViewById(R.id.view_module_start_butt);

        try {
            nameOfModule.setText(module.getString("Name"));
            nameOfAuth.setText("written by " + module.getString("Author"));
            moduleRating.setText("Module *****");
            authRating.setText("Author *****");

            final User u = new User(getApplicationContext());
            int IDofTutor = 0;
            IDofTutor = u.getWhoTrainsMeThis(getApplicationContext(), user, Integer.parseInt(module.getString("ID")));
            tutor = u.getUser(getApplicationContext(), IDofTutor);
            String nameOfTutor = u.getUsername(getApplicationContext(), tutor);
            final int lastSlide = u.getLastSlideViewed(getApplicationContext(), user, module.getInt("ID"));

            tutorName.setText("Your tutor: " + nameOfTutor);
            tutorRating.setText("Tutor *****");
            progressBar.setMax(module.getInt("No. of Slides"));
            progressBar.setProgress(lastSlide);
            progressText.setText("" + lastSlide + " of " + module.getInt("No. of Slides") + " slides.");
            moduleDesc.setText(module.getString("Description"));

            boolean review = false;

            if (lastSlide < module.getInt("No. of Slides")) {
                if (lastSlide == 0) {
                    startButt.setText("Start");
                    startButt.setEnabled(true);
                } else {
                    startButt.setText("Continue");
                    startButt.setEnabled(true);
                }
            } else {
                startButt.setText("Review");
                review = true;
            }

            assert startButt != null;
            final boolean finalReview = review;
            startButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ModuleProgress.this, "Opening Module", Toast.LENGTH_SHORT).show();

                    int type = 0;

                    try {
                        type = f.getSlideType(getApplicationContext(),
                                module,
                                u.getLastSlideViewed(getApplicationContext(), user, module.getInt("ID")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent startModule = null;
                    switch (type) {
                        case 0:
                            return;
                        case 1:
                            startModule = new Intent(ModuleProgress.this, ViewTextSlide.class);
                            break;
                        case 2:
                            startModule = new Intent(ModuleProgress.this, ViewTableSlide.class);
                            break;
                    }

                    startModule.putExtra("User", user.toString());
                    startModule.putExtra("Module", module.toString());

                    if (finalReview) {
                        startModule.putExtra("Slide Number", "" + 0);
                    } else {
                        startModule.putExtra("Slide Number", "" + lastSlide);
                    }
                    startActivity(startModule);
                    finish();

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    boolean wantsToQuitLearning = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuitLearning) {

            User u = new User(getApplicationContext());

            JSONObject userUpdate = null;

            try {
                userUpdate = u.getUser(getApplicationContext(), Integer.parseInt(this.user.getString("ID")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent returnHome = new Intent(ModuleProgress.this, Home.class);
            returnHome.putExtra("User", userUpdate.toString());
            startActivity(returnHome);
            finish();

        }

        this.wantsToQuitLearning = true;
        Toast.makeText(this, "Press 'Back' once more to quit this module.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wantsToQuitLearning = false;
            }
        }, 1000);
    }



}
