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

/**
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * The activity is used to provide a general overview of a selected module the user is training.
 * It is launched when the user selects one of the modules they have enrolled on from the Learning
 * Tab. It displays general module information: name, description, author and tutor information, and
 * ratings. Moreover, it shows the progress the user has made in the module using a progressbar.
 * The learner may initiate a chat activity from this screen by pressing the 'Message Tutor' button.
 *
 */
public class ModuleProgress extends AppCompatActivity {

    private ArrayList<HashMap<String, Object>> myTutors;
    private Set<String> IDsOfMyTutors;
    private User u = null;
    private HashMap<String, Object> moduleMap;
    private HashMap<String, Object> userMap;
    private HashMap<String, Object> tutorMap;
    private int lastSlide = 0;
    private boolean isReview;
    private boolean hasTutor = true;
    private TextView nameOfModule;
    private TextView nameOfAuth;
    private TextView moduleRating;
    private TextView authRating;
    private TextView tutorName;
    private TextView tutorRating;
    private TextView progressHeader;
    private ProgressBar progressBar;
    private TextView progressText;
    private TextView moduleDesc;
    private Button startButt;
    private Button chatButt;
    private String IDofTutor;
    private String tutorNameString;
    private AsyncGetMyTutors getMyTutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initVars();
        getMyTutorIDs();
        getMyTutors = new AsyncGetMyTutors();
        getMyTutors.execute();
        populateFields();
        setUpProgress();

        if (lastSlide < Integer.parseInt(moduleMap.get("noOfSlides").toString())) {
            if (lastSlide == 0) {
                startButt.setText("Start\nmodule");
                startButt.setEnabled(true);
            } else {
                startButt.setText("Continue\nmodule");
                startButt.setEnabled(true);
            }
        } else {
            startButt.setText("Review\nmodule");
            isReview = true;
        }
        addStartButtListener();
        addChatButtListener();
    }

    private void addChatButtListener() {
        chatButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startChat = new Intent(ModuleProgress.this, Chat.class);
                startChat.putExtra("User", userMap);
                startChat.putExtra("TutorMap", tutorMap);
                startChat.putExtra("TutorID", IDofTutor);
                startActivity(startChat);
            }
        });}

    private void addStartButtListener() {
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
        });}

    private void setUpProgress() {
        progressBar.setMax(Integer.parseInt(moduleMap.get("noOfSlides").toString()));
        progressBar.setProgress(lastSlide);
        progressText.setText("" + lastSlide + " of " + moduleMap.get("noOfSlides") + " slides.");
    }

    private void populateFields() {
        nameOfModule = (TextView) findViewById(R.id.view_module_name_of_module_view);
        nameOfAuth = (TextView) findViewById(R.id.view_module_author_of_module_view);
        moduleRating = (TextView) findViewById(R.id.view_module_rating);
        authRating = (TextView) findViewById(R.id.view_author_rating);
        tutorName = (TextView) findViewById(R.id.view_module_tutors_name);
        tutorRating = (TextView) findViewById(R.id.view_module_tutors_rating);
        progressHeader = (TextView) findViewById(R.id.view_module_progress_header);
        progressBar = (ProgressBar) findViewById(R.id.view_module_progressbar);
        progressText = (TextView) findViewById(R.id.view_module_progress_text);
        moduleDesc = (TextView) findViewById(R.id.view_module_description);
        startButt = (Button) findViewById(R.id.view_module_start_butt);
        chatButt = (Button) findViewById(R.id.view_module_chat_butt);
        nameOfModule.setText(moduleMap.get("name").toString());
        nameOfAuth.setText("written by " + moduleMap.get("authorName"));
        moduleRating.setText("Module *****");
        authRating.setText("Author *****");
        lastSlide = u.getLastSlideViewed(userMap, moduleMap.get("id").toString());
        tutorName.setText("Loading tutor info...");
        tutorRating.setText("Tutor *****");
    }

    private void initVars() {
        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        tutorMap = new HashMap<>();
        myTutors = new ArrayList<>();
        IDsOfMyTutors = new HashSet<>();
        tutorMap = new HashMap<>();
        u = new User();
        IDofTutor = u.getWhoTrainsMeThis(userMap, moduleMap.get("id").toString());
        this.setTitle("Module information");
        moduleDesc.setText(moduleMap.get("description").toString());
    }

    public String getThisTutorName(String iDofTutor) {
        for (HashMap<String, Object> tutor : myTutors) {
            try {
                if (tutor.get("id").toString().equals(iDofTutor))
                    return tutor.get("username").toString();
            } catch (NullPointerException e) {
                hasTutor = false;
                return "is no longer registered :(";
            }
        }
        hasTutor = false;
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

    /**
     * In order to ensure the user doesn't accidentally leave the activity, they are prompted to
     * repeat the BackPress action within 1 second.
     */
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
