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

public class ViewLibPopUpModDisplay extends Activity {

    private ArrayList<String> infoToShow = null;
    private HashMap<String, String> moduleMap = null;
    private HashMap<String, Object> userMap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lib_pop_up_mod_display);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final User u = new User();

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        getWindow().setLayout((int) (screenWidth * 0.8), (int) (screenHeight * 0.6));

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");

        infoToShow = getIntent().getStringArrayListExtra("Module Info");

        moduleMap = (HashMap<String, String>) getIntent().getSerializableExtra("Module");

        TextView nameView = (TextView) findViewById(R.id.popUpTextViewName);
        nameView.setText(infoToShow.get(3));

        TextView authView = (TextView) findViewById(R.id.popUpTextViewAuth);
        String author = "by " + moduleMap.get("authorName").toString();
        authView.setText(author);

        TextView proView = (TextView) findViewById(R.id.popUpTextViewPro);
        int pro = 0;
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

        TextView ratingView = (TextView) findViewById(R.id.popUpTextViewRating);
        String ratingText = "Review IDs: " + infoToShow.get(5);
        ratingView.setText(ratingText);

        final Button enrollButt = (Button) findViewById(R.id.enrollButt);
        if (pro == 1) {
            enrollButt.setText(R.string.popup_view_enroll_pro);
        } else {
            enrollButt.setText(R.string.popup_enroll_free);
        }

        enrollButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean enrolledAlready = u.isLearning(getApplicationContext(), userMap, infoToShow.get(0));
                if (ownModule()) {
                    Toast.makeText(ViewLibPopUpModDisplay.this, "You cannot train yourself!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!enrolledAlready) {
                    String modID = moduleMap.get("id");
                    String moduleName = moduleMap.get("name");
                    String totalSlides = moduleMap.get("noOfSlides");

                    userMap = u.addToLearning(getApplicationContext(), userMap, modID, moduleName, totalSlides);

                    // for now, this method assigns the first available tutor (== Author) of a module
                    userMap = u.assignTutor(userMap, moduleMap);

                    Toast.makeText(ViewLibPopUpModDisplay.this, "Enrolled! You can view your progress on the Learning Tab.", Toast.LENGTH_SHORT).show();
                    Intent returnResult = new Intent(ViewLibPopUpModDisplay.this, ViewLibrary.class);
                    System.out.println(userMap.toString());
                    returnResult.putExtra("User", userMap);
                    returnResult.putExtra("Module", moduleMap);

                    setResult(1, returnResult);
                    finish();

                } else {
                    Toast.makeText(ViewLibPopUpModDisplay.this, "You've already started this module.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView slideNumView = (TextView) findViewById(R.id.popUpTextNoOfSlides);

        // singular or plural "Slide/s"
        int amountOfSlides = Integer.parseInt(infoToShow.get(7));
        if (amountOfSlides > 1) {
            String noOfSlides = getString(R.string.noOfSlides_contains) + infoToShow.get(7) + getString(R.string.noOfSlides_slides);
            slideNumView.setText(noOfSlides);
        } else {
            slideNumView.setText(R.string.noOfSlides_contains_one);
        }

        TextView descView = (TextView) findViewById(R.id.popUpTextViewDesc);
        descView.setText(infoToShow.get(4));

    }

    private boolean ownModule() {
        if (userMap.get("id").toString().equals(moduleMap.get("author")))
            return true;

        return false;
    }

}
