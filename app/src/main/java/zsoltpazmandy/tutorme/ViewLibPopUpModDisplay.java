package zsoltpazmandy.tutorme;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewLibPopUpModDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lib_pop_up_mod_display);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        getWindow().setLayout((int) (screenWidth * 0.8), (int) (screenHeight * 0.6));

        ArrayList<String> infoToShow = getIntent().getStringArrayListExtra("Module Info");

        TextView nameView = (TextView) findViewById(R.id.popUpTextViewName);
        nameView.setText(infoToShow.get(3));

        TextView authView = (TextView) findViewById(R.id.popUpTextViewAuth);
        String author = "by " + infoToShow.get(1);
        authView.setText(author);

        TextView proView = (TextView) findViewById(R.id.popUpTextViewPro);
        int pro = Integer.parseInt(infoToShow.get(2));

        if (pro == 1) {
            proView.setText("PRO Module");
        } else {
            proView.setText("Free Module");
        }

        TextView ratingView = (TextView) findViewById(R.id.popUpTextViewRating);
        String ratingText = "Review IDs: " + infoToShow.get(5);
        ratingView.setText(ratingText);

        TextView descView = (TextView) findViewById(R.id.popUpTextViewDesc);
        descView.setText(infoToShow.get(4));

        TextView slideNumView = (TextView) findViewById(R.id.popUpTextNoOfSlides);

        // singular or plural "Slide/s"
        int amountOfSlides = Integer.parseInt(infoToShow.get(7));
        if (amountOfSlides > 1) {
            String noOfSlides = "Contains " + infoToShow.get(7) + " slides.";
            slideNumView.setText(noOfSlides);
        } else {
            slideNumView.setText("Contains 1 slide.");
        }

    }

}
