package zsoltpazmandy.tutorme;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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

    public void prepUser(String id, String email, String username) {
        DatabaseReference currentUser = this.userRoot.child(id);
        User user = new User(id, email, username);
        currentUser.setValue(user);
    }

    public void saveUserHashMapInCloud(HashMap<String,Object> usermap){
        userRoot.child(usermap.get("id").toString()).updateChildren(usermap);
    }

    public void saveModuleHashMapInCloud(HashMap<String, Object> moduleMap){
        moduleRoot.child(moduleMap.get("id").toString()).updateChildren(moduleMap);
    }

    public void overWriteModuleHashMapInCloud(HashMap<String, Object> moduleMap){
        moduleRoot.child(moduleMap.get("id").toString()).setValue(moduleMap);
    }

    public void addToTrainersTrainees(String tutorID, String tutee, String moduleID){
        DatabaseReference tutorsTrainingRoot = userRoot.child(tutorID).child("training");

        HashMap<String, Object> newRelation = new HashMap<>();
        newRelation.put(moduleID, tutee);

        tutorsTrainingRoot.updateChildren(newRelation);
    }

    public void addToAuthored(HashMap<String, Object> userMap, HashMap<String, Object> moduleMap) {
        DatabaseReference thisUser = userRoot.child(mAuth.getCurrentUser().getUid());
        HashMap<String, String> authored = (HashMap<String, String>) userMap.get("authored");

        if(authored.containsKey("none")){
            authored.remove("none");
        }
        authored.put(moduleMap.get("id").toString(), moduleMap.get("name").toString());

        userMap.remove("authored");
        userMap.put("authored", authored);

        thisUser.updateChildren(userMap);
    }

    public void addToTraining(HashMap<String, Object> userMap, String moduleID) {

        DatabaseReference thisUser = userRoot.child(userMap.get("id").toString());

        HashMap<String, String> training = (HashMap<String, String>) userMap.get("training");

        if(training.containsKey("none")){
            training.remove("none");
        }
        training.put(moduleID, "true");

        userMap.remove("training");
        userMap.put("training", training);

        thisUser.updateChildren(userMap);
    }

    public void updateProgress(String moduleID, String moduleName, String totalSlides, String lastSlide) {


        DatabaseReference thisUser = userRoot.child(mAuth.getCurrentUser().getUid());

        // NameOfModule" "/" "totalSlides" "/" "lastSlide"
        String progressString = moduleName + "/" + totalSlides + "/" + lastSlide;

        thisUser.child("progress").child(moduleID).setValue(progressString);
    }




}
