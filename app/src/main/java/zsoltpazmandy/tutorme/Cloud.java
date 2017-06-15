package zsoltpazmandy.tutorme;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Set;


/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * All operations involving updating any data in the Firebase database are performed by a method
 * of this class.
 */
class Cloud {

    private FirebaseAuth mAuth = null;
    private DatabaseReference userRoot = null;
    private DatabaseReference moduleRoot = null;

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

    /**
     * Updates the users directory in the database by adding the user object that stores data in a
     * HashMap.
     *
     * @param usermap HashMap that stores user information
     */
    public void saveUserHashMapInCloud(HashMap<String, Object> usermap) {
        userRoot.child(usermap.get("id").toString()).updateChildren(usermap);
    }

    /**
     * Updates the modules directory in the database by adding the module object that stores data in a
     * HashMap.
     *
     * @param moduleMap HashMap that stores module information and content
     */
    public void saveModuleHashMapInCloud(HashMap<String, Object> moduleMap) {
        moduleRoot.child(moduleMap.get("id").toString()).updateChildren(moduleMap);
    }

    /**
     * Updates a previously stored module and its content in the database.
     *
     * @param moduleMap HashMap that stores module information and content
     */
    public void overWriteModuleHashMapInCloud(HashMap<String, Object> moduleMap) {
        moduleRoot.child(moduleMap.get("id").toString()).setValue(moduleMap);
    }

    /**
     * The method is called when a Tutee enrolls on a module. The Tutor's user information is updated
     * by adding the module's ID and the Tutee's name to the directory.
     *
     * @param tutorID the ID of the Tutor
     * @param tutee the ID of the Tutee
     * @param moduleID the ID of the module the Tutee is enrolling on
     */
    public void addToTutorsTuteesList(String tutorID, String tutee, String moduleID) {
        DatabaseReference tutorsTrainingRoot = userRoot.child(tutorID).child("training");
        HashMap<String, Object> newRelation = new HashMap<>();
        newRelation.put(moduleID, tutee);
        tutorsTrainingRoot.updateChildren(newRelation);
    }

    /**
     * The method is called when a Tutor creates a module. The module's name and ID are added to the
     * user's directory under the key "authored"
     *
     * @param userMap HashMap that stores user information
     * @param moduleMap HashMap that stores the module information and content
     */
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

    /**
     * The method is called when a Tutor creates a module. The module's ID is stored in the user's
     * directory under the key "training".
     *
     * @param userMap HashMap that stores user information
     * @param moduleID ID of the module
     */
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

    /**
     * The method is called every time the user opens a slide of a module for the first time.
     * The metric of progress: amount of slides seen / total slides in the module
     * Progress is stored under the key "progress" in the user's directory in the following pattern:
     * NameOfTheModule + "_" + LastSlideViewed + "_" + TotalSlidesInTheModule
     *
     * @param userMap HashMap that stores user information
     * @param moduleID ID of the module
     * @param newLastSlideInt ordinal number of the last slide that has been viewed by the user
     * @return the updated user HashMap
     */
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
                name = progressMap.get(currentModuleID).split("_")[0];
                totalSlidesString = progressMap.get(currentModuleID).split("_")[1];
                oldLastSlideString = progressMap.get(currentModuleID).split("_")[2];
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
