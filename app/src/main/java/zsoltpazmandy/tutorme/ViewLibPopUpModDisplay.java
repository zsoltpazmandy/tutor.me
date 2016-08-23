package zsoltpazmandy.tutorme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * Pop-up window used in the ViewLibrary activity to display module overview, where the user may
 * decide whether or not they wish to enroll on the module. Other than basic information relating
 * to the module's author, title, rating, it also displays the module's entire description field.
 * It is displayed in the centre of the screen and is dismissed by pressing anywhere outside the
 * window.
 */
public class ViewLibPopUpModDisplay extends Activity {

    private ArrayList<String> infoToShow;
    private HashMap<String, String> moduleMap;
    private HashMap<String, Object> userMap;
    private User user;
    private int screenWidth;
    private int screenHeight;
    private TextView nameView;
    private TextView authView;
    private String author;
    private int pro;
    private TextView ratingView;
    private String ratingText;
    private String modID;
    private String moduleName;
    private String totalSlides;
    private String IDofTutor;
    private TextView slideNumView;
    private int amountOfSlides;
    private String noOfSlides;
    private TextView descView;
    private Button enrollButt;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lib_pop_up_mod_display);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        initVars();
        setUpEnrollButton();
    }

    private void setUpEnrollButton() {
        enrollButt = (Button) findViewById(R.id.enrollButt);
        if (pro == 1) {
            enrollButt.setText(R.string.popup_view_enroll_pro);
        } else {
            enrollButt.setText(R.string.popup_enroll_free);
        }

        enrollButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean enrolledAlready = user.isLearning(userMap, infoToShow.get(0));
                if (ownModule()) {
                    Toast.makeText(ViewLibPopUpModDisplay.this, "You cannot train yourself!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!enrolledAlready) {
                    modID = moduleMap.get("id");
                    moduleName = moduleMap.get("name");
                    totalSlides = moduleMap.get("noOfSlides");

                    // for now, this method assigns the first available tutor (== Author) of a module
                    IDofTutor = user.assignTutor(userMap, moduleMap);
                    userMap = user.addToLearning(userMap, IDofTutor, modID, moduleName, totalSlides);

                    Toast.makeText(ViewLibPopUpModDisplay.this, "Enrolled! You can view your progress on the Learning Tab.", Toast.LENGTH_SHORT).show();
                    Intent returnResult = new Intent(ViewLibPopUpModDisplay.this, ViewLibrary.class);
                    returnResult.putExtra("User", userMap);
                    returnResult.putExtra("Module", moduleMap);
                    setResult(1, returnResult);
                    finish();

                } else {
                    Toast.makeText(ViewLibPopUpModDisplay.this, "You've already started this module.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initVars() {
        user = new User();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        getWindow().setLayout((int) (screenWidth * 0.8), (int) (screenHeight * 0.6));
        slideNumView = (TextView) findViewById(R.id.popUpTextNoOfSlides);
        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        infoToShow = getIntent().getStringArrayListExtra("Module Info");
        moduleMap = (HashMap<String, String>) getIntent().getSerializableExtra("Module");
        nameView = (TextView) findViewById(R.id.popUpTextViewName);
        nameView.setText(infoToShow.get(3));
        authView = (TextView) findViewById(R.id.popUpTextViewAuth);
        author = "by " + moduleMap.get("authorName").toString();
        authView.setText(author);

        TextView proView = (TextView) findViewById(R.id.popUpTextViewPro);
        if(infoToShow.get(2).equals("true")){
            pro = 1;
        } else {
            pro = 0;
        }

        if (pro == 1) {
            proView.setText(R.string.popup_view_pro_tag);
        } else {
            proView.setText(R.string.popup_view_free_tag);
        }

        ratingView = (TextView) findViewById(R.id.popUpTextViewRating);
        ratingText = "Review IDs: " + infoToShow.get(5);
        ratingView.setText(ratingText);
        amountOfSlides = Integer.parseInt(infoToShow.get(7));
        if (amountOfSlides > 1) {
            noOfSlides = getString(R.string.noOfSlides_contains) + infoToShow.get(7) + getString(R.string.noOfSlides_slides);
            slideNumView.setText(noOfSlides);
        } else {
            slideNumView.setText(R.string.noOfSlides_contains_one);
        }

        descView = (TextView) findViewById(R.id.popUpTextViewDesc);
        descView.setText(infoToShow.get(4));
    }

    private boolean ownModule() {
        if (userMap.get("id").toString().equals(moduleMap.get("author")))
            return true;

        return false;
    }

}
