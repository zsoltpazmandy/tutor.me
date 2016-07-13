package zsoltpazmandy.tutorme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewLibPopUpModDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lib_pop_up_mod_display);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final User u = new User(getApplicationContext());

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        getWindow().setLayout((int) (screenWidth * 0.8), (int) (screenHeight * 0.6));

        JSONObject user = null;

        try {
            user = new JSONObject(getIntent().getStringExtra("User String"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<String> infoToShow = getIntent().getStringArrayListExtra("Module Info");

        TextView nameView = (TextView) findViewById(R.id.popUpTextViewName);
        nameView.setText(infoToShow.get(3));

        TextView authView = (TextView) findViewById(R.id.popUpTextViewAuth);
        String author = "by " + infoToShow.get(1);
        authView.setText(author);

        TextView proView = (TextView) findViewById(R.id.popUpTextViewPro);
        int pro = Integer.parseInt(infoToShow.get(2));

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

        final JSONObject finalUser = user;
        enrollButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject userUpdated = finalUser;

                try {
                    assert finalUser != null;
                    userUpdated = u.getUser(getApplicationContext(), finalUser.getInt("ID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                boolean enrolledAlready = u.isLearning(getApplicationContext(), userUpdated, Integer.parseInt(infoToShow.get(0)));

                if (!enrolledAlready) {

                    u.addToLearning(getApplicationContext(), userUpdated, Integer.parseInt(infoToShow.get(0)));
                    Toast.makeText(ViewLibPopUpModDisplay.this, "Enrolled! You can view your progress on the Learning Tab.", Toast.LENGTH_SHORT).show();
                    Intent returnResult = new Intent(ViewLibPopUpModDisplay.this, ViewLibrary.class);
                    assert userUpdated != null;
                    returnResult.putExtra("User String", userUpdated.toString());

                    Module f = new Module();
                    JSONObject module = f.getModuleByID(getApplicationContext(), Integer.parseInt(infoToShow.get(0)));

                    returnResult.putExtra("Module", module.toString());

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

    public void updateUser(int id, JSONObject user) {

    }

}
