package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ModuleProgress extends AppCompatActivity {

    private ArrayList<HashMap<String, Object>> myTutors = null;
    private Set<String> IDsOfMyTutors = null;

    private User u = null;

    HashMap<String, Object> modMap = null;
    HashMap<String, Object> userMap = null;
    private int lastSlide = 0;

    private TextView nameOfModule = null;
    private TextView nameOfAuth = null;
    private TextView moduleRating = null;
    private TextView authRating = null;
    private TextView tutorName = null;
    private TextView tutorRating = null;
    private TextView progressHeader = null;
    private ProgressBar progressBar = null;
    private TextView progressText = null;
    private TextView moduleDesc = null;
    private Button startButt = null;

    private String IDofTutor;
    private String tutorNameString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        modMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        myTutors = new ArrayList<>();
        IDsOfMyTutors = new HashSet<>();

        u = new User();

        IDofTutor = u.getWhoTrainsMeThis(getApplicationContext(), userMap, modMap.get("id").toString());

        getMyTutorIDs();

        AsyncGetMyTutors getMyTutors = new AsyncGetMyTutors();
        getMyTutors.execute();

        this.setTitle("Module information");

        nameOfModule = (TextView) findViewById(R.id.view_module_name_of_module_view);
        nameOfAuth = (TextView) findViewById(R.id.view_module_author_of_module_view);
        moduleRating = (TextView) findViewById(R.id.view_module_rating);
        authRating = (TextView) findViewById(R.id.view_author_rating);
        tutorName = (TextView) findViewById(R.id.view_module_tutors_name);
        tutorRating = (TextView) findViewById(R.id.view_module_tutors_rating);
        progressHeader = (TextView) findViewById(R.id.view_module_progress_header);
        progressBar = (ProgressBar) findViewById(R.id.view_module_progressbar);
        progressText = (TextView) findViewById(R.id.view_module_progress_text);
        TextView moduleDesc = (TextView) findViewById(R.id.view_module_description);
        startButt = (Button) findViewById(R.id.view_module_start_butt);

        nameOfModule.setText(modMap.get("name").toString());
        nameOfAuth.setText("written by " + modMap.get("authorName"));
        moduleRating.setText("Module *****");
        authRating.setText("Author *****");

        lastSlide = u.getLastSlideViewed(userMap, modMap.get("id").toString());

        tutorName.setText("Loading tutor info...");
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

    private String getThisTutorName(String iDofTutor) {
        for(HashMap<String, Object> tutor : myTutors){
            try {
                if (tutor.get("id").toString().equals(iDofTutor))
                    return tutor.get("username").toString();
            } catch (NullPointerException e){
                return "is no longer registered :(";
            }
        }
        return "is no longer registered :(";
    }

    private void getMyTutorIDs() {
        HashMap<String, String> trainedByMap = (HashMap<String, String>) userMap.get("trainedBy");
        IDsOfMyTutors = trainedByMap.keySet();
    }

    class AsyncGetMyTutors extends AsyncTask<String, ArrayList<HashMap<String, Object>>, String> {

        @Override
        protected String doInBackground(String... uid) {

            final DatabaseReference usersRoot = FirebaseDatabase.getInstance().getReference().child("/users");
            usersRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, Object> currentTutor = new HashMap<String, Object>();

                    for(String s: IDsOfMyTutors){
                        currentTutor = (HashMap) dataSnapshot.child(s).getValue();
                        myTutors.add(currentTutor);
                    }
                    publishProgress(myTutors);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    publishProgress(myTutors);
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<HashMap<String, Object>>... tutors) {
            super.onProgressUpdate(tutors);
            myTutors = tutors[0];
            setUpTutorInfo();
        }

    }

    private void setUpTutorInfo() {
        tutorNameString = getThisTutorName(IDofTutor);
        tutorName.setText("Your tutor: " + tutorNameString);
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
