package zsoltpazmandy.tutorme;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


    public void saveUserInCloud(String id,
                                String email,
                                String username,
                                String location,
                                String language1,
                                String language2,
                                String language3,
                                String age,
                                String authored,
                                String progress,
                                String learning,
                                String trainedBy) {

        DatabaseReference currentUser = this.userRoot.child(id);

        User user = new User(id, email, username, location, language1, language2, language3, age, authored, learning, trainedBy);

        currentUser.setValue(user);

        // String progress therefore must be: 000000_Name of the module_100_50
        // where 000000 is the module's local ID, 100 is the total number of slides, and 50 is the last slide the user has seen
        String progressKey = progress.substring(0, 6);
        String progressValue = progress.substring(6);

        currentUser.child("progress").child(progressKey).setValue(progressValue);

//        currentUser.child("progress").child("000001_").setValue("Name of the module_100_58");

    }

    public void saveUserInCloud(Context context, JSONObject user) {
        String id = null;
        String email = null;
        String username = null;
        String location = null;
        String language1 = null;
        String language2 = null;
        String language3 = null;
        String age = null;
        String authored = null;
        String progress = null;
        String learning = null;
        String trainedBy = null;

        try {
            id = user.getString("id");
            email = user.getString("Email");
            username = user.getString("Username");
            location = user.getString("Location");
            language1 = user.getString("Language 1");
            language2 = user.getString("Language 2");
            language3 = user.getString("Language 3");
            age = user.getString("Age");
            authored = user.getString("Authored");
            progress = user.getString("Progress");
            learning = user.getString("Learning");
            trainedBy = user.getString("Trained by");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DatabaseReference currentUser = this.userRoot.child(id);
        User userInCloud = new User(id, email, username, location, language1, language2, language3, age, authored, learning, trainedBy);
        currentUser.setValue(userInCloud);

        progress = progress.replace("[","").replace("]","").replaceAll("\"","");

        String progressKey = progress.substring(0, 6);
        String progressValue = progress.substring(6);
        currentUser.child("progress").child(progressKey).setValue(progressValue);


    }

    public void saveModuleInCloud(String name,
                                  String description,
                                  int pro,
                                  String author,
                                  ArrayList<Integer> reviews,
                                  ArrayList<Integer> trainers,
                                  ArrayList<Integer> typesOfSlides,
                                  int noOfSlides,
                                  String ID) {

        Module thisModule = new Module(name, description, pro, author, reviews, trainers, typesOfSlides, noOfSlides, ID);
        moduleRoot.child(String.valueOf(ID)).setValue(thisModule);
    }

    public void saveModuleInCloud(Context context, JSONObject module) {

        User u = new User(context);

        try {
            String name = module.getString("Name");

            String description = module.getString("Description");
            int pro = module.getInt("PRO");
            String author = module.getString("Author");

            ArrayList<String> temp = u.getStringArrayListFromJSON(context, module, "Reviews");
            ArrayList<Integer> reviews = new ArrayList<>();
            for (String s : temp) {
                reviews.add(Integer.parseInt(s));
            }

            temp = u.getStringArrayListFromJSON(context, module, "Trainers");
            ArrayList<Integer> trainers = new ArrayList<>();
            for (String s : temp) {
                trainers.add(Integer.parseInt(s));
            }

            temp = u.getStringArrayListFromJSON(context, module, "Types of Slides");
            ArrayList<Integer> typesOfSlides = new ArrayList<>();
            for (String s : temp) {
                typesOfSlides.add(Integer.parseInt(s));
            }

            int noOfSlides = module.getInt("No. of Slides");
            String ID = module.getString("ID");

            Module thisModule = new Module(name, description, pro, author, reviews, trainers, typesOfSlides, noOfSlides, ID);
            moduleRoot.child(String.valueOf(ID)).setValue(thisModule);

            for(int i = 1; i <= noOfSlides; i++){
                moduleRoot.child(String.valueOf(ID)).child("Slide_" + i).setValue(module.getString("Slide " + i));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void updateModuleInCloud(Context context, JSONObject module){

        User u = new User(context);

        try {
            String name = module.getString("Name");

            String description = module.getString("Description");
            int pro = module.getInt("PRO");
            String author = module.getString("Author");

            ArrayList<String> temp = u.getStringArrayListFromJSON(context, module, "Reviews");
            ArrayList<Integer> reviews = new ArrayList<>();
            for (String s : temp) {
                reviews.add(Integer.parseInt(s));
            }

            temp = u.getStringArrayListFromJSON(context, module, "Trainers");
            ArrayList<Integer> trainers = new ArrayList<>();
            for (String s : temp) {
                trainers.add(Integer.parseInt(s));
            }

            temp = u.getStringArrayListFromJSON(context, module, "Types of Slides");
            ArrayList<Integer> typesOfSlides = new ArrayList<>();
            for (String s : temp) {
                typesOfSlides.add(Integer.parseInt(s));
            }

            int noOfSlides = module.getInt("No. of Slides");
            String ID = module.getString("ID");

            HashMap<String, Object> thisModule = new HashMap<String, Object>();
            thisModule.put("name",name);
            thisModule.put("description",description);
            thisModule.put("pro",pro);
            thisModule.put("author",author);
            thisModule.put("reviews",reviews);
            thisModule.put("trainers",trainers);
            thisModule.put("typesOfSlides",typesOfSlides);
            thisModule.put("noOfSlides",noOfSlides);
            thisModule.put("ID",ID);

            moduleRoot.child(String.valueOf(ID)).updateChildren(thisModule);

            for(int i = 1; i <= noOfSlides; i++){
                moduleRoot.child(String.valueOf(ID)).child("Slide_" + i).setValue(module.getString("Slide " + i));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> ModuleJSONtoHashMap(JSONObject module){
        HashMap<String, Object> moduleMap = new HashMap<>();

        try {
            moduleMap.put("name",module.getString("Name"));
            moduleMap.put("description",module.getString("Description"));
            moduleMap.put("id",module.getString("ID"));
            moduleMap.put("author",module.getString("Author"));
            moduleMap.put("pro",module.getString("PRO"));
            moduleMap.put("noOfSlides",module.getString("No. of Slides"));
            moduleMap.put("typesOfSlides",module.getString("Types of Slides"));

            for(int i = 1; i <= Integer.parseInt(module.getString("No. of Slides"));i++){
                moduleMap.put("Slide_" + i,module.getString("Slide " + i));
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        return moduleMap;
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
