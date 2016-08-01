package zsoltpazmandy.tutorme;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void prepUser(String uID, String id, String email, String username){
        DatabaseReference currentUser = this.userRoot.child(uID);
        User user = new User(uID, id, email, username);
        currentUser.setValue(user);
    }


    public void saveUser(String uID,
                         String id,
                         String email,
                         String username,
                         String location,
                         String language1,
                         String language2,
                         String language3,
                         String age,
                         String learning,
                         String trainedBy,
                         String training,
                         String progress) {

        DatabaseReference currentUser = this.userRoot.child(uID);

        User user = new User(uID, id, email, username, location, language1, language2, language3, age, learning, trainedBy, training, progress);

        currentUser.setValue(user);

        currentUser.child("Progress").setValue("1");
        currentUser.child("Progress").child("1").child("Last Slide").setValue("5");

    }

    public JSONObject getUserJSON() throws JSONException {

        String uID = mAuth.getCurrentUser().getUid();
        DatabaseReference userRoot = this.userRoot.child(uID);

        JSONObject localUser = new JSONObject();

        localUser.put("ID", userRoot.child("ID"));
        localUser.put("Email", userRoot.child("Email"));
        localUser.put("Username", userRoot.child("Username"));
        localUser.put("Location", userRoot.child("Location"));
        localUser.put("Language 1", userRoot.child("Language 1"));
        localUser.put("Language 2", userRoot.child("Language 2"));
        localUser.put("Language 3", userRoot.child("Language 3"));
        localUser.put("Interests", userRoot.child("Interests"));
        localUser.put("Age", userRoot.child("Age"));
        localUser.put("Learning", userRoot.child("Learning"));
        localUser.put("Trained by", userRoot.child("Trained by"));
        localUser.put("Training", userRoot.child("Training"));
        localUser.put("Progress", userRoot.child("Progress"));

        return localUser;
    }

    public void saveModule() {


    }

}
