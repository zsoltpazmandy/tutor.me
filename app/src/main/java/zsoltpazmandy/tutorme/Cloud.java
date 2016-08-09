package zsoltpazmandy.tutorme;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by zsolt on 31/07/16.
 */
public class Cloud {

    FirebaseAuth mAuth = null;
    DatabaseReference userRoot = null;
    DatabaseReference moduleRoot = null;

    public Cloud() {
        userRoot = FirebaseDatabase.getInstance().getReference().child("/users/");
        moduleRoot = FirebaseDatabase.getInstance().getReference().child("/modules/");
        mAuth = FirebaseAuth.getInstance();
    }

    public void prepUser(String uID, String id, String email, String username) {
        DatabaseReference currentUser = this.userRoot.child(uID);
        User user = new User(uID, id, email, username);
        currentUser.setValue(user);
    }


    public void saveUserInCloud(String uID,
                                String id,
                                String email,
                                String username,
                                String location,
                                String language1,
                                String language2,
                                String language3,
                                String age,
                                String progress,
                                String learning,
                                String trainedBy) {

        DatabaseReference currentUser = this.userRoot.child(uID);

        User user = new User(uID, id, email, username, location, language1, language2, language3, age, learning, trainedBy);

        currentUser.setValue(user);

        // String progress therefore must be: 000000_Name of the module_100_50
        // where 000000 is the module's local ID, 100 is the total number of slides, and 50 is the last slide the user has seen
        String progressKey = progress.substring(0,6);
        String progressValue = progress.substring(6);

        currentUser.child("progress").child(progressKey).setValue(progressValue);

//        currentUser.child("progress").child("000001_").setValue("Name of the module_100_58");

    }

    public void saveModuleInCloud(String name,
                                  String description,
                                  int pro,
                                  String author,
                                  ArrayList<Integer> reviews,
                                  ArrayList<Integer> trainers,
                                  ArrayList<Integer> typesOfSlides,
                                  int noOfSlides,
                                  int ID) {

        Module thisModule = new Module(name, description, pro, author, reviews, trainers, typesOfSlides, noOfSlides, ID);
        moduleRoot.child(String.valueOf(ID)).setValue(thisModule);
    }

    public void addToTraining(String moduleID) {

        DatabaseReference thisUser = userRoot.child(mAuth.getCurrentUser().getUid());
        thisUser.child("Training").child(moduleID).setValue("true");

    }

    public void updateProgress(String moduleID, String moduleName, String totalSlides, String lastSlide) {


        DatabaseReference thisUser = userRoot.child(mAuth.getCurrentUser().getUid());

        // NameOfModule" "/" "totalSlides" "/" "lastSlide"
        String progressString = moduleName + "/" + totalSlides + "/" + lastSlide;

        thisUser.child("progress").child(moduleID).setValue(progressString);
    }


}
