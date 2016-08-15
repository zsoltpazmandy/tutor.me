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

    HashMap<String, Object> moduleMap = null;
    HashMap<String, Object> userMap = null;
    HashMap<String, Object> tutorMap = null;
    private int lastSlide = 0;
    private boolean isReview;

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

        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        tutorMap = new HashMap<>();
        myTutors = new ArrayList<>();
        IDsOfMyTutors = new HashSet<>();

        tutorMap = new HashMap<>();

        u = new User();

        IDofTutor = u.getWhoTrainsMeThis(userMap, moduleMap.get("id").toString());

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

        nameOfModule.setText(moduleMap.get("name").toString());
        nameOfAuth.setText("written by " + moduleMap.get("authorName"));
        moduleRating.setText("Module *****");
        authRating.setText("Author *****");

        lastSlide = u.getLastSlideViewed(userMap, moduleMap.get("id").toString());

        tutorName.setText("Loading tutor info...");
        tutorRating.setText("Tutor *****");
        progressBar.setMax(Integer.parseInt(moduleMap.get("noOfSlides").toString()));
        progressBar.setProgress(lastSlide);
        progressText.setText("" + lastSlide + " of " + moduleMap.get("noOfSlides") + " slides.");
        moduleDesc.setText(moduleMap.get("description").toString());

        if (lastSlide < Integer.parseInt(moduleMap.get("noOfSlides").toString())) {
            if (lastSlide == 0) {
                startButt.setText("Start");
                startButt.setEnabled(true);
            } else {
                startButt.setText("Continue");
                startButt.setEnabled(true);
            }
        } else {
            startButt.setText("Review");
            isReview = true;
        }

        assert startButt != null;
        startButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ModuleProgress.this, "Opening Module", Toast.LENGTH_SHORT).show();

                HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");

                if (lastSlide == 0) {
                    lastSlide = 1;
                }

                int type;

                if (isReview) {
                    type = Integer.parseInt(typesMap.get("Slide_" + 1));
                } else {
                    type = Integer.parseInt(typesMap.get("Slide_" + lastSlide));
                }

                Intent startModule = null;
                switch (type) {
                    case 1:
                        startModule = new Intent(ModuleProgress.this, ViewTextSlide.class);
                        break;
                    case 2:
                        startModule = new Intent(ModuleProgress.this, ViewTableSlide.class);
                        break;
                }
                try {
                    if (!getThisTutorMap(IDofTutor).equals(null)) {
                        startModule.putExtra("TutorMap", tutorMap);
                    }
                } catch (NullPointerException e){

                }
                startModule.putExtra("TutorID", IDofTutor);
                startModule.putExtra("User", userMap);
                startModule.putExtra("Module", moduleMap);

                if (isReview) {
                    startModule.putExtra("Review", true);
                    startModule.putExtra("Slide Number", "" + 0);
                } else {
                    startModule.putExtra("Slide Number", "" + lastSlide);
                }
                startActivity(startModule);
                finish();

            }
        });


    }

    public String getThisTutorName(String iDofTutor) {
        for (HashMap<String, Object> tutor : myTutors) {
            try {
                if (tutor.get("id").toString().equals(iDofTutor))
                    return tutor.get("username").toString();
            } catch (NullPointerException e) {
                return "is no longer registered :(";
            }
        }
        return "is no longer registered :(";
    }

    public HashMap<String, Object> getThisTutorMap(String iDofTutor) {
        for (HashMap<String, Object> tutor : myTutors) {
            try {
                if (tutor.get("id").toString().equals(iDofTutor))
                    return tutor;
            } catch (NullPointerException e) {
                return null;
            }
        }
        return null;
    }


    private void getMyTutorIDs() {
        HashMap<String, String> learningMap = (HashMap<String, String>) userMap.get("learning");
        Set<String> myLearningIDs = learningMap.keySet();
        for(String s : myLearningIDs){
            IDsOfMyTutors.add(learningMap.get(s));
        }
    }

    class AsyncGetMyTutors extends AsyncTask<String, ArrayList<HashMap<String, Object>>, String> {

        @Override
        protected String doInBackground(String... uid) {

            final DatabaseReference usersRoot = FirebaseDatabase.getInstance().getReference().child("/users");
            usersRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, Object> currentTutor = new HashMap<String, Object>();
                    for (String s : IDsOfMyTutors) {
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
        tutorMap = getThisTutorMap(IDofTutor);
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
