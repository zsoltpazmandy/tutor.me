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

import java.util.HashMap;

public class ModuleProgress extends AppCompatActivity {

    HashMap<String, Object> modMap = null;
    HashMap<String, Object> userMap = null;
    private int lastSlide = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Module f = new Module();

        modMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        this.setTitle("Module information");

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

        nameOfModule.setText(modMap.get("name").toString());
        nameOfAuth.setText("written by " + modMap.get("authorName"));
        moduleRating.setText("Module *****");
        authRating.setText("Author *****");

        final User u = new User(getApplicationContext());
        String IDofTutor = u.getWhoTrainsMeThis(getApplicationContext(), userMap, modMap.get("id").toString());
        lastSlide = u.getLastSlideViewed(getApplicationContext(), userMap, modMap.get("id").toString());

        tutorName.setText("Your tutor: " + IDofTutor);
        tutorRating.setText("Tutor *****");
        progressBar.setMax(Integer.parseInt(modMap.get("noOfSlides").toString()));
        progressBar.setProgress(lastSlide);
        progressText.setText("" + lastSlide + " of " + modMap.get("noOfSlides") + " slides.");
        moduleDesc.setText(modMap.get("description").toString());

        boolean review = false;

        if (lastSlide < Integer.parseInt(modMap.get("noOfSlides").toString())) {
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

                HashMap<String, String> typesMap = (HashMap<String, String>) modMap.get("typesOfSlides");

                if(lastSlide == 0){
                    lastSlide = 1;
                }

                int type = Integer.parseInt(typesMap.get("Slide_" + lastSlide));

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

                startModule.putExtra("User", userMap);
                startModule.putExtra("Module", modMap);

                if (finalReview) {
                    startModule.putExtra("Slide Number", "" + 0);
                } else {
                    startModule.putExtra("Slide Number", "" + lastSlide);
                }
                startActivity(startModule);
                finish();

            }
        });


    }

    boolean wantsToQuitLearning = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuitLearning) {

            Intent returnHome = new Intent(ModuleProgress.this, Home.class);
            returnHome.putExtra("User", userMap);
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
