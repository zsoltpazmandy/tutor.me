package zsoltpazmandy.tutorme;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by zsolt on 31/07/16.
 */
public class Cloud {

    private FirebaseAuth mAuth = null;
    private DatabaseReference userRoot = null;
    private DatabaseReference moduleRoot = null;
    private DatabaseReference progressRoot = null;

    public Cloud() {
        userRoot = FirebaseDatabase.getInstance().getReference().child("/users/");
        moduleRoot = FirebaseDatabase.getInstance().getReference().child("/modules/");
        progressRoot = FirebaseDatabase.getInstance().getReference().child("/active_progress/");
        mAuth = FirebaseAuth.getInstance();
    }

    public void prepUser(String id, String email, String username) {
        DatabaseReference currentUser = this.userRoot.child(id);
        User user = new User(id, email, username);
        currentUser.setValue(user);
    }

    public void saveUserHashMapInCloud(HashMap<String, Object> usermap) {
        userRoot.child(usermap.get("id").toString()).updateChildren(usermap);
    }

    public void saveModuleHashMapInCloud(HashMap<String, Object> moduleMap) {
        moduleRoot.child(moduleMap.get("id").toString()).updateChildren(moduleMap);
    }

    public void overWriteModuleHashMapInCloud(HashMap<String, Object> moduleMap) {
        moduleRoot.child(moduleMap.get("id").toString()).setValue(moduleMap);
    }

    public void addToTrainersTrainees(String tutorID, String tutee, String moduleID) {
        DatabaseReference tutorsTrainingRoot = userRoot.child(tutorID).child("training");

        HashMap<String, Object> newRelation = new HashMap<>();
        newRelation.put(moduleID, tutee);

        tutorsTrainingRoot.updateChildren(newRelation);
    }

    public void addToAuthored(HashMap<String, Object> userMap, HashMap<String, Object> moduleMap) {
        DatabaseReference thisUser = userRoot.child(mAuth.getCurrentUser().getUid());
        HashMap<String, String> authored = (HashMap<String, String>) userMap.get("authored");

        if (authored.containsKey("none")) {
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

        if (training.containsKey("none")) {
            training.remove("none");
        }
        training.put(moduleID, "true");

        userMap.remove("training");
        userMap.put("training", training);

        thisUser.updateChildren(userMap);
    }

    public HashMap<String, Object> updateProgress(HashMap<String, Object> userMap, String moduleID, int newLastSlideInt) {

        HashMap<String, String> progressMap = (HashMap<String, String>) userMap.get("progress");
        Set<String> modIDsLearning = progressMap.keySet();

        int oldLastSlideInt = 0;
        int totalSlidesInt = 0;

        String name = "";
        String totalSlidesString = "";
        String oldLastSlideString = "";

        String newLastSlideString = String.valueOf(newLastSlideInt);

        for (String currentModuleID : modIDsLearning) {
            if (moduleID.equals(currentModuleID)) {
                name = progressMap.get(currentModuleID).toString().split("_")[0];
                totalSlidesString = progressMap.get(currentModuleID).toString().split("_")[1];
                oldLastSlideString = progressMap.get(currentModuleID).toString().split("_")[2];
            }
        }

        totalSlidesInt = Integer.parseInt(totalSlidesString);
        oldLastSlideInt = Integer.parseInt(oldLastSlideString);

        if (newLastSlideInt > oldLastSlideInt) {   // new progress stored
            progressMap.remove(moduleID);
            progressMap.put(moduleID, name + "_" + totalSlidesString + "_" + newLastSlideString);
            userMap.remove("progress");
            userMap.put("progress", progressMap);
            userRoot.child(userMap.get("id").toString()).updateChildren(userMap);
        }

        if (newLastSlideInt == totalSlidesInt) {    // module completed
            progressMap.remove(moduleID);
            progressMap.put(moduleID, name + "_" + totalSlidesString + "_" + newLastSlideString);
            userMap.remove("progress");
            userMap.put("progress", progressMap);
            userRoot.child(userMap.get("id").toString()).updateChildren(userMap);
        }
        return userMap;
    }
}
