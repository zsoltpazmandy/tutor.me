package zsoltpazmandy.tutorme;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Functions related to User Records, updater functions, user records management
 * <p/>
 * Created by zsolt on 30/06/16.
 */
public class User {

    public User(Context context) {
    }

    /*
    USER LOGIN, REGISTRATION, USER RECORDS MANAGEMENT FUNCTIONS
     */

    /**
     * Reads user data from a file on the disc
     * User data is stored as JSON Objects in files. Filenames are created as follows:
     * "user" + i
     * where 'i' is the User's ID.
     *
     * @param context
     * @param id
     * @return
     * @throws JSONException
     */
    public JSONObject getUser(Context context, int id) throws JSONException {
        JSONObject returnObject = new JSONObject();

        FileInputStream fileInput = null;
        try {
            fileInput = context.openFileInput("user" + id);

            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }

            returnObject = new JSONObject(moduleString);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return returnObject;
    }

    /**
     * Builds a JSON Object using the input Username and Password:
     * It checks if username has been registered already (isUsernameTaken), returns 0 if it has
     * Saves the JSON by calling saveUser(Context, JSONObject)
     * Updates the UserRecords by calling setUserRecordsJSON(Context, String)
     *
     * @param context
     * @param username Username of the user
     * @param password Password of the user
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public int register(Context context, String username, String password, String email) throws JSONException, IOException {

        int returnIDtoReg = 0;

        boolean taken = true;

        taken = isUsernameTaken(context, username) && isEmailTaken(context, email);

        if (!taken) {
            JSONObject newUser = new JSONObject();

            int newId = assignID(context);
            returnIDtoReg = newId;

            newUser.put("ID", newId).put("Username", username).put("Password", password).put("Email", email);

            saveUser(context, newUser);
            setUserRecordsJSON(context, getUserRecords(context).accumulate("IDs", newId).toString());

        }

        return returnIDtoReg;
    }

    /**
     * Loops through all user IDs, checks arguments against each User object and finds if they
     * match an existing User object. If it finds a match for the username, it then checks whether
     * the password is correct.
     *
     * @param context
     * @param username
     * @param password
     * @return 0 if Username could not be found or if entered password was incorrect
     */
    public int login(Context context, String username, String password) {
        int userID = 0;
        boolean found = false;

        try {

            List<Integer> allIDs = getUserIDs(context);

            for (int i = 1; i < userCount(context); i++) {
                if (getUsername(context, getUser(context, allIDs.get(i))).equals(username)) {
                    if (getPassword(context, getUser(context, allIDs.get(i))).equals(password)) {
                        userID = allIDs.get(i);
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                        found = true;
                        return userID;
                    } else {
                        if (getUsername(context, getUser(context, allIDs.get(i))).equals(username) &&
                                !getPassword(context, getUser(context, allIDs.get(i))).equals(password)) {
                            Toast.makeText(context, "Password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            if (!found) {
                Toast.makeText(context, "Username cannot be found in database", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return userID;
    }

    /**
     * Loops through all user IDs, checks arguments against each User object and finds if they
     * match an existing User object. If it finds a match for the username, it then checks whether
     * the password is correct.
     *
     * @param context
     * @param email
     * @param password
     * @return 0 if Username could not be found or if entered password was incorrect
     */
    public int loginWithEmail(Context context, String email, String password) {
        int userID = 0;
        boolean found = false;

        try {

            List<Integer> allIDs = getUserIDs(context);

            for (int i = 1; i < userCount(context); i++) {
                if (getEmail(context, getUser(context, allIDs.get(i))).equals(email)) {
                    if (getPassword(context, getUser(context, allIDs.get(i))).equals(password)) {
                        userID = allIDs.get(i);
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                        found = true;
                        return userID;
                    } else {
                        if (getEmail(context, getUser(context, allIDs.get(i))).equals(email) &&
                                !getPassword(context, getUser(context, allIDs.get(i))).equals(password)) {
                            Toast.makeText(context, "Password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            if (!found) {
                Toast.makeText(context, "Email cannot be found in database", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return userID;
    }



    /**
     * Checks whether the argument Username has already been registered in the database.
     * Converts all usernames to lowercase
     *
     * @param context
     * @param username
     * @return true if taken, false if not
     */
    public boolean isUsernameTaken(Context context, String username) {

        boolean taken = false;

        try {

            for (int i = 1; i < userCount(context); i++) {
                if (getUser(context, i)
                        .getString("Username")
                        .toLowerCase()
                        .equals(username.toLowerCase())) {
                    taken = true;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return taken;
    }

    /**
     * Checks whether the argument Email has already been registered in the database.
     * Converts all email addresses to lowercase
     *
     * @param context
     * @param email
     * @return true if taken, false if not
     */
    public boolean isEmailTaken(Context context, String email) {

        boolean taken = false;

        try {

            for (int i = 1; i < userCount(context); i++) {
                if (getUser(context, i)
                        .getString("Email")
                        .toLowerCase()
                        .equals(email.toLowerCase())) {
                    taken = true;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return taken;
    }



    /**
     * Finds and returns the next assignable User ID by reading in the ID of the last added ID and
     * incrementing it by 1.
     *
     * @param context
     * @return
     */
    public int assignID(Context context) {
        int nextID = 0;
        try {
            List<Integer> registeredIDs = getUserIDs(context);
            nextID = registeredIDs.get(registeredIDs.size() - 1);
            nextID++;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nextID;
    }

    /**
     * Saves User data provided as the argument
     * Filenames are created as follows:
     * "user" + i
     * where 'i' is the User's ID.
     *
     * @param context
     * @param user
     * @return true if saved successfully
     */
    public boolean saveUser(Context context, JSONObject user) {

        boolean isSuccessful = false;

        FileOutputStream fou = null;

        try {

            fou = context.openFileOutput("user" + user.getString("ID"), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(user.toString());
            osw.flush();
            osw.close();

            isSuccessful = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

    /**
     * Updates UserRecords by overwriting it with the stringified JSON version provided as its arg.
     * Intended to be used, therefore, e.g. by calling setUserRecords(getUserRecords.accumulate("IDs", newID))
     * in order to retrieve the latest state of UserRecords JSON & adding the newly added ID.
     * <p/>
     * Filename: "user_records"
     *
     * @param context
     * @param userRecordsString
     * @return
     */
    public boolean setUserRecordsJSON(Context context, String userRecordsString) {

        boolean success = false;

        try {

            FileOutputStream fou = context.openFileOutput("user_records", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(userRecordsString);
            osw.flush();
            osw.close();

            success = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    /**
     * Retrieves UserRecords (a JSON Object containing all registered user IDs)
     * Creates it anew with just a zero recorded in it if the file could not be found or
     * if the JSON appears to be damaged or unreadable.
     * <p/>
     * UserRecords are used to provide an easy way of determining the amount of registered users &
     * their IDs.
     * <p/>
     * Filename: "user_records"
     *
     * @param context
     * @return
     * @throws JSONException
     */
    public JSONObject getUserRecords(Context context) throws JSONException {

        FileInputStream fileInput = null;

        JSONObject userRecords = new JSONObject();

        try {

            fileInput = context.openFileInput("user_records");
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String oneBigString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                oneBigString += read_data;
                data = new char[100];
            }

            // if records file is found all in order

            userRecords = new JSONObject(oneBigString);

        } catch (FileNotFoundException FNFE) {

            // if the file does not exist, it's created from scratch

            JSONObject newUserRecords = new JSONObject();
            newUserRecords.put("IDs", 0);
            setUserRecordsJSON(context, newUserRecords.toString());
            return getUserRecords(context);

        } catch (JSONException | IOException e) {

            // if the file exists, but there is no data found inside, it is initialised

            JSONObject newUserRecords = new JSONObject();
            newUserRecords.put("IDs", 0);
            setUserRecordsJSON(context, newUserRecords.toString());
            return getUserRecords(context);
        }
        return userRecords;
    }

    /**
     * Returns the amount of users who are currently registered in the UserRecords.
     *
     * @param context
     * @return
     * @throws JSONException
     */
    public int userCount(Context context) throws JSONException {

        JSONObject userRecordsJSON = getUserRecords(context);

        int amountOfUsers = 0;

        try {
            amountOfUsers = userRecordsJSON.getJSONArray("IDs").length();
        } catch (JSONException e) {
            try {
                if (userRecordsJSON.getInt("IDs") % 1 == 0) {
                    amountOfUsers = 1;
                }
            } catch (JSONException e2) {
                setUserRecordsJSON(context, userRecordsJSON.put("IDs", 0).toString());
                amountOfUsers = 0;
            }
        }

        return amountOfUsers;
    }

    /*
    --------------------------------------------------------------
     */

    /*
    TEST FUNCTIONS: RESET USER RECORDS
     */

    /**
     * EXCLUSIVELY USED FOR TESTING
     * <p/>
     * Overwrites all existing files in the database: it looks up all registered User IDs, which are
     * the same as the User data filenames. It then creates one initial entry with the ID zero.
     * (this is so User IDs, indexes, ordinal number of Users is always registered starting from 1.)
     *
     * @param context
     * @throws IOException
     * @throws JSONException
     */
    public void purgeUserRecords(Context context) throws IOException, JSONException {

        List<Integer> IDs = getUserIDs(context);

        for (int i = 0; i < userCount(context); i++) {
            FileOutputStream fou = context.openFileOutput("user" + IDs.get(i), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write("");
            osw.flush();
            osw.close();
        }

        JSONObject zeroUser = new JSONObject();
        zeroUser.put("ID", 0).put("Username", "----------").put("Password", "----------");
        saveUser(context, zeroUser);

    }

    /**
     * EXCLUSIVELY USED FOR TESTING
     * <p/>
     * Updates UserRecords by registering only one initial User, with ID: zero.
     * Any getUserCount call after this will return zero.
     *
     * @param context
     * @throws IOException
     * @throws JSONException
     */
    public void resetCounter(Context context) throws IOException, JSONException {

        JSONObject newUserRecords = new JSONObject();
        newUserRecords.put("IDs", 0);

        if (setUserRecordsJSON(context, newUserRecords.toString())) {
            Toast.makeText(context, "User counter reset", Toast.LENGTH_SHORT).show();
        }

    }

    /*
    --------------------------------------------------------------
     */

    /*
    USER DATA RETRIEVAL
     */

    /**
     * Returns an ArrayList of integers of the IDs of modules the User has authored.
     *
     * @param context
     * @param user
     * @return
     */
    public ArrayList<Integer> getModulesAuthoredBy(Context context, JSONObject user) {
        ArrayList<Integer> modulesAuthoredByUser = new ArrayList<>();
        JSONObject moduleRecords = null;
        Module f = new Module();

        // grab User's ID
        int currentUserID = 0;
        try {
            currentUserID = user.getInt("ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check all module IDs
        try {
            moduleRecords = f.getModuleRecordsJSON(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // v2 - OK

        int[] moduleIDs = getIntfromJSON(context, moduleRecords, "IDs");

        JSONObject currentModule = null;

        for (int i = 0; i < moduleIDs.length; i++) {
            currentModule = f.getModuleByID(context, moduleIDs[i]);
            try {
                if (currentModule.getString("Author").equals(getUsername(context, user))) {
                    modulesAuthoredByUser.add(moduleIDs[i]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException ne){
                return new ArrayList<Integer>();
            }
        }

        /*
        v1


        String temp = "";
        String[] tempArray = new String[1];
        try {
            temp = moduleRecords.getString("IDs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        temp = temp.replace("[", "").replace("]", "");
        if (temp.contains(",")) {
            tempArray = temp.split(",");
        } else {
            tempArray[0] = temp;
        }
        if (tempArray[0].equals("")) {
            return modulesAuthoredByUser;
        }

        ArrayList<Integer> allModuleIDs = new ArrayList<>();
        for (int i = 0; i < tempArray.length; i++) {
            allModuleIDs.add(Integer.parseInt(tempArray[i]));
        }


        // from all the module IDS, select the ones where Author's username == current user's Username
        JSONObject currentModule = null;
        for (int i = 0; i < allModuleIDs.size(); i++) {
            currentModule = f.getModuleByID(context, allModuleIDs.get(i));
            try {
                if (currentModule.getString("Author").equals(getUsername(context, user))) {
                    modulesAuthoredByUser.add(allModuleIDs.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        */

        return modulesAuthoredByUser;
    }

    /**
     * Returns a List of int of all registered User IDs.
     *
     * @param context
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public List<Integer> getUserIDs(Context context) throws JSONException, IOException {

        ArrayList<Integer> IDsToReturn = new ArrayList<>();

        String temp;

        for (int i = 0; i < userCount(context); i++) {
            temp = getUserRecords(context).getString("IDs");
            temp = temp.replace("[", "").replace("]", "");
            IDsToReturn.add(Integer.parseInt(temp.split(",")[i]));
        }

        return IDsToReturn;
    }

    /**
     * Retrieves "Username" String from User JSON.
     * <p/>
     * Returns empty String if not found/set yet.
     *
     * @param context
     * @param user
     * @return
     */
    public String getUsername(Context context, JSONObject user) {
        String username = "";

        try {
            username = user.getString("Username");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return username;
    }

    /**
     * Retrieves "Email" String from User JSON.
     * <p/>
     * Returns empty String if not found/set yet.
     *
     * @param context
     * @param user
     * @return
     */
    public String getEmail(Context context, JSONObject user) {
        String email = "";

        try {
            email = user.getString("Email");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return email;
    }

    /**
     * Retrieves "Password" String from User JSON.
     * <p/>
     * Returns empty String if not found/set yet.
     *
     * @param context
     * @param user
     * @return
     */
    public String getPassword(Context context, JSONObject user) {
        String password = "";
        try {
            password = user.getString("Password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return password;
    }

    /**
     * Retrieves "Location" int code from User JSON.
     * To be used in conjunction with decodeLocation to get String value of numeric location value.
     * <p/>
     * Returns ZERO if not found/set yet.
     *
     * @param context
     * @param user
     * @return
     */
    public int getLocation(Context context, JSONObject user) {
        int location = 0;

        try {
            location = Integer.parseInt(user.getString("Location").replace("[", "").replace("]", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Retrieve languages spoken by the user. If none have been registered, all three slots are
     * set to and returned as zero (at initial user profile setup).
     *
     * @param context
     * @param user
     * @return
     */
    public int[] getLanguages(Context context, JSONObject user) {

        // setting all three languages to zero
        int[] languages = {0, 0, 0};

        String[] tempLanguages = new String[1];

        // reading "Languages" string from user's JSON object record
        try {

            String tempString = user.getString("Languages").replace("[", "").replace("]", "");

            if (tempString.contains(",")) {
                tempLanguages = tempString.split(",");
            } else {
                tempLanguages[0] = tempString;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // if no languages were parsed from the JSON, returns the initial 0,0,0
        try {
            tempLanguages[0].equals(null);
        } catch (NullPointerException e) {
            return languages;
        }

        // otherwise write languages codes in array & return them
        for (int i = 0; i < tempLanguages.length; i++) {
            languages[i] = Integer.parseInt(tempLanguages[i]);
        }
        return languages;
    }

    /**
     * Retrieves all modules the user is currently enrolled on checks the index of moduleID among
     * those, then retrieves all of the user's currently assigned trainers & returns the ID of the
     * Trainer who is assigned to be Training the module whose ID is moduleID;
     *
     * @param context
     * @param user
     * @param moduleID
     * @return
     */
    public int getWhoTrainsMeThis(Context context, JSONObject user, int moduleID) {
        int trainerID = 0;

        String allLearning = "";
        String[] allLearningArray = new String[1];

        try {
            allLearning = user.getString("Learning").replace("[", "").replace("]", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (allLearning.contains(",")) {
            allLearningArray = allLearning.split(",");
        } else {
            allLearningArray[0] = allLearning;
        }

        int index = 0;

        for (int i = 0; i < allLearningArray.length; i++) {
            if (Integer.parseInt(allLearningArray[i]) == moduleID) {
                index = i;
            }
        }

        String allTrainers = "";
        String[] allTrainersArray = new String[1];

        try {
            allTrainers = user.getString("Trained by").replace("]", "").replace("[", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (allTrainers.contains(",")) {
            allTrainersArray = allTrainers.split(",");
        } else {
            allTrainersArray[0] = allTrainers;
        }

        trainerID = Integer.parseInt(allTrainersArray[index]);
        return trainerID;
    }

    /**
     * Retrieves "Age" as an int from User JSON.
     * <p/>
     * Returns ZERO if not found/set yet. As it is not a required field, age may stay zero, in which
     * case a '?' is displayed on the User Profile Tab.
     *
     * @param context
     * @param user
     * @return
     */
    public int getAge(Context context, JSONObject user) {
        int age = 0;

        try {
            age = Integer.parseInt(user.getString("Age"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return age;
    }

    /**
     * Retrieves "Interests" int[] codes from User JSON.
     * To be used in conjunction with decodeInterest to get String value of numeric interest value.
     * <p/>
     * Returns ZERO if not found/set yet. It is not a required field, so it may stay zero, in which
     * case all 'Interests' checkboxes remain unchecked on the User Profile Tab.
     *
     * @param context
     * @param user
     * @return
     */
    public int[] getInterests(Context context, JSONObject user) {

        int[] interests = new int[10];

        String[] tempInterests = new String[0];

        try {

            String tempString = user.getString("Interests").replace("[", "").replace("]", "").replace("x", "");
            tempString = tempString.substring(3, tempString.length());
            tempInterests = tempString.split(",");

        } catch (JSONException e) {

            e.printStackTrace();
        }

        for (int i = 1; i < tempInterests.length; i++) {
            interests[i - 1] = Integer.parseInt(tempInterests[i].replace("\"", "")) + 1;
        }

        return interests;
    }

    /**
     * Returns a List of ints which are the module IDs of the modules the user is currently enrolled
     * on.
     *
     * @param context
     * @param user
     * @return
     */
    public List<Integer> getLearning(Context context, JSONObject user) {

        ArrayList<Integer> learningTheseModules = new ArrayList<>();

        String[] tempArray = new String[1];

        try {
            String tempLearning = user.getString("Learning");
            tempLearning = tempLearning.replace("[", "").replace("]", "");

            if (tempLearning.contains(",")) {
                tempArray = tempLearning.split(",");
            } else {
                tempArray[0] = tempLearning;
            }

            for (int i = 0; i < tempArray.length; i++) {
                learningTheseModules.add(Integer.parseInt(tempArray[i]));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return learningTheseModules;
    }

    /**
     * Checks whether the user has already enrolled on a module by checking all the module IDs the
     * user is enrolled on agains the moduleID provided as an argument.
     *
     * @param context
     * @param user
     * @param moduleID
     * @return true if use is already enrolled
     */
    public boolean isLearning(Context context, JSONObject user, int moduleID) {
        boolean result = false;

        for (int i = 0; i < getLearning(context, user).size(); i++) {
            if (getLearning(context, user).get(i) == moduleID)
                result = true;
        }

        return result;
    }

    /*
    --------------------------------------------------------------
     */

    /*
    SETTING/UPDATING USER DATA
     */

    /**
     * Adds moduleID to the user JSON's "Learning" array which contains the module IDs the User
     * is currently enrolled on.
     * Initialises User's progress on the newly added module as ZERO by adding a new value to the
     * User JSON in the following format:
     * <p/>
     * "Progress + i" = j
     * <p/>
     * Where 'i' is the module ID, the value 'j' is the last slide of the module the User has opened.
     * <p/>
     * Progress is stored so Users may continue Learning the given module exactly from where they
     * left off; and to mark the module as 'Completed' (not implemented yet).
     *
     * @param context
     * @param user
     * @param newLearning
     */
    public void addToLearning(Context context, JSONObject user, int newLearning) {

        try {
            user.accumulate("Learning", newLearning);
            user.accumulate("Progress" + newLearning, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveUser(context, user);
    }

    /**
     * At the moment the class assigns the first Trainer found on the list of trainers of the module
     * <p/>
     * This class is supposed to perform the algorithmic matching of Learner & Trainer based on
     * - languages spoken
     * - geographic location
     * - interests
     *
     * @param context
     * @param user     the user enrolling on a Module
     * @param moduleID the module the user is starting to learn
     */
    public void assignTutor(Context context, JSONObject user, int moduleID) {

        int IDofAssignedTrainer = 0;

        JSONObject module = null;
        Module f = new Module();
        ArrayList<Integer> trainers = new ArrayList<>();

        module = f.getModuleByID(context, moduleID);
        trainers = f.getTrainers(context, module);

        // Algorithm that matches the Learner with a Trainer goes here

        // Now, it only picks the first available Trainer:
        IDofAssignedTrainer = trainers.get(0);

        try {
            user.accumulate("Trained by", IDofAssignedTrainer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveUser(context, user);
    }

    /**
     * Updates User JSON Progress entry stored for the given module by overwriting and saving the
     * stored value IF it is different from the stored value.
     *
     * @param context
     * @param user
     * @param module
     * @param lastSlide
     */
    public void updateProgress(Context context, JSONObject user, JSONObject module, int lastSlide) {

        boolean changed = false;

        try {
            // check if new value different from the stored value
            if (lastSlide != Integer.parseInt(user.getString("Progress" + module.getInt("ID")))) {
                user.put("Progress" + module.getInt("ID"), lastSlide);
                changed = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // save User data if Progress was updated
        if (changed) saveUser(context, user);
    }

    /**
     * Updates user's stored languages in the database.
     * At least language1 (mother-tongue) must be non-zero.
     *
     * @param context
     * @param user      the user object that is being updated
     * @param language1 the user's 1st language
     * @param language2 the user's 2nd language
     * @param language3 the user's 3rd language
     */
    public void setLanguages(Context context, JSONObject user, int language1, int language2, int language3) {

        if (language1 == 0) {
            return;
        } else {

            user.remove("Languages");

            try {
                user.accumulate("Languages", language1);
                user.accumulate("Languages", language2);
                user.accumulate("Languages", language3);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        saveUser(context, user);

    }

    /*
    --------------------------------------------------------------
     */

    /*
    DECODERS int -> String
     */

    /**
     * Returns human-readable String values for interest codes provided as integers.
     *
     * @param interestID int code for various interest Strings
     * @return
     */
    public String decodeInterest(int interestID) {

        String returnValue = "";

        switch (interestID) {
            case 0:
                returnValue = "Languages";
                break;
            case 1:
                returnValue = "Travelling";
                break;
            case 2:
                returnValue = "Sports";
                break;
            case 3:
                returnValue = "History";
                break;
            case 4:
                returnValue = "Music";
                break;
            case 5:
                returnValue = "Science";
                break;
            case 6:
                returnValue = "Arts";
                break;
            case 7:
                returnValue = "Food & Cuisine";
                break;
            case 8:
                returnValue = "Health";
                break;
            case 9:
                returnValue = "Computers";
                break;
        }

        return returnValue;
    }

    /**
     * Returns human-readable String values for Country codes provided as integers.
     *
     * @param countryID
     * @return
     */
    public String decodeCountry(int countryID) {

        String returnValue = "";

        switch (countryID) {
            case 1:
                returnValue = "Afghanistan";
                break;
            case 2:
                returnValue = "Albania";
                break;
            case 3:
                returnValue = "Algeria";
                break;
            case 4:
                returnValue = "Andorra";
                break;
            case 5:
                returnValue = "Angola";
                break;
            case 6:
                returnValue = "Antigua and Barbuda";
                break;
            case 7:
                returnValue = "Argentina";
                break;
            case 8:
                returnValue = "Armenia";
                break;
            case 9:
                returnValue = "Australia";
                break;
            case 10:
                returnValue = "Austria";
                break;
            case 11:
                returnValue = "Azerbaijan";
                break;
            case 12:
                returnValue = "Bahamas";
                break;
            case 13:
                returnValue = "Bahrain";
                break;
            case 14:
                returnValue = "Bangladesh";
                break;
            case 15:
                returnValue = "Barbados";
                break;
            case 16:
                returnValue = "Belarus";
                break;
            case 17:
                returnValue = "Belgium";
                break;
            case 18:
                returnValue = "Belize";
                break;
            case 19:
                returnValue = "Benin";
                break;
            case 20:
                returnValue = "Bhutan";
                break;
            case 21:
                returnValue = "Bolivia";
                break;
            case 22:
                returnValue = "Bosnia and Herzegovina";
                break;
            case 23:
                returnValue = "Botswana";
                break;
            case 24:
                returnValue = "Brazil";
                break;
            case 25:
                returnValue = "Brunei Darussalam";
                break;
            case 26:
                returnValue = "Bulgaria";
                break;
            case 27:
                returnValue = "Burkina Faso";
                break;
            case 28:
                returnValue = "Burundi";
                break;
            case 29:
                returnValue = "Cabo Verde";
                break;
            case 30:
                returnValue = "Cambodia";
                break;
            case 31:
                returnValue = "Cameroon";
                break;
            case 32:
                returnValue = "Canada";
                break;
            case 33:
                returnValue = "CAR";
                break;
            case 34:
                returnValue = "Chad";
                break;
            case 35:
                returnValue = "Chile";
                break;
            case 36:
                returnValue = "China";
                break;
            case 37:
                returnValue = "Colombia";
                break;
            case 38:
                returnValue = "Comoros";
                break;
            case 39:
                returnValue = "Congo";
                break;
            case 40:
                returnValue = "Costa Rica";
                break;
            case 41:
                returnValue = "Côte d'Ivoire";
                break;
            case 42:
                returnValue = "Croatia";
                break;
            case 43:
                returnValue = "Cuba";
                break;
            case 44:
                returnValue = "Cyprus";
                break;
            case 45:
                returnValue = "Czech Republic";
                break;
            case 46:
                returnValue = "DPRK";
                break;
            case 47:
                returnValue = "DRC";
                break;
            case 48:
                returnValue = "Denmark";
                break;
            case 49:
                returnValue = "Djibouti";
                break;
            case 50:
                returnValue = "Dominica";
                break;
            case 51:
                returnValue = "Dominican Republic";
                break;
            case 52:
                returnValue = "Ecuador";
                break;
            case 53:
                returnValue = "Egypt";
                break;
            case 54:
                returnValue = "El Salvador";
                break;
            case 55:
                returnValue = "Equatorial Guinea";
                break;
            case 56:
                returnValue = "Eritrea";
                break;
            case 57:
                returnValue = "Estonia";
                break;
            case 58:
                returnValue = "Ethiopia";
                break;
            case 59:
                returnValue = "Fiji";
                break;
            case 60:
                returnValue = "Finland";
                break;
            case 61:
                returnValue = "France";
                break;
            case 62:
                returnValue = "Gabon";
                break;
            case 63:
                returnValue = "Gambia";
                break;
            case 64:
                returnValue = "Georgia";
                break;
            case 65:
                returnValue = "Germany";
                break;
            case 66:
                returnValue = "Ghana";
                break;
            case 67:
                returnValue = "Greece";
                break;
            case 68:
                returnValue = "Grenada";
                break;
            case 69:
                returnValue = "Guatemala";
                break;
            case 70:
                returnValue = "Guinea";
                break;
            case 71:
                returnValue = "Guinea-Bissau";
                break;
            case 72:
                returnValue = "Guyana";
                break;
            case 73:
                returnValue = "Haiti";
                break;
            case 74:
                returnValue = "Honduras";
                break;
            case 75:
                returnValue = "Hungary";
                break;
            case 76:
                returnValue = "Iceland";
                break;
            case 77:
                returnValue = "India";
                break;
            case 78:
                returnValue = "Indonesia";
                break;
            case 79:
                returnValue = "Iran";
                break;
            case 80:
                returnValue = "Iraq";
                break;
            case 81:
                returnValue = "Ireland";
                break;
            case 82:
                returnValue = "Israel";
                break;
            case 83:
                returnValue = "Italy";
                break;
            case 84:
                returnValue = "Jamaica";
                break;
            case 85:
                returnValue = "Japan";
                break;
            case 86:
                returnValue = "Jordan";
                break;
            case 87:
                returnValue = "Kazakhstan";
                break;
            case 88:
                returnValue = "Kenya";
                break;
            case 89:
                returnValue = "Kiribati";
                break;
            case 90:
                returnValue = "Kuwait";
                break;
            case 91:
                returnValue = "Kyrgyzstan";
                break;
            case 92:
                returnValue = "Laos";
                break;
            case 93:
                returnValue = "Latvia";
                break;
            case 94:
                returnValue = "Lebanon";
                break;
            case 95:
                returnValue = "Lesotho";
                break;
            case 96:
                returnValue = "Liberia";
                break;
            case 97:
                returnValue = "Libya";
                break;
            case 98:
                returnValue = "Liechtenstein";
                break;
            case 99:
                returnValue = "Lithuania";
                break;
            case 100:
                returnValue = "Luxembourg";
                break;
            case 101:
                returnValue = "Macedonia";
                break;
            case 102:
                returnValue = "Madagascar";
                break;
            case 103:
                returnValue = "Malawi";
                break;
            case 104:
                returnValue = "Malaysia";
                break;
            case 105:
                returnValue = "Maldives";
                break;
            case 106:
                returnValue = "Mali";
                break;
            case 107:
                returnValue = "Malta";
                break;
            case 108:
                returnValue = "Marshall Islands";
                break;
            case 109:
                returnValue = "Mauritania";
                break;
            case 110:
                returnValue = "Mauritius";
                break;
            case 111:
                returnValue = "Mexico";
                break;
            case 112:
                returnValue = "Micronesia";
                break;
            case 113:
                returnValue = "Monaco";
                break;
            case 114:
                returnValue = "Mongolia";
                break;
            case 115:
                returnValue = "Montenegro";
                break;
            case 116:
                returnValue = "Morocco";
                break;
            case 117:
                returnValue = "Mozambique";
                break;
            case 118:
                returnValue = "Myanmar";
                break;
            case 119:
                returnValue = "Namibia";
                break;
            case 120:
                returnValue = "Nauru";
                break;
            case 121:
                returnValue = "Nepal";
                break;
            case 122:
                returnValue = "Netherlands";
                break;
            case 123:
                returnValue = "New Zealand";
                break;
            case 124:
                returnValue = "Nicaragua";
                break;
            case 125:
                returnValue = "Niger";
                break;
            case 126:
                returnValue = "Nigeria";
                break;
            case 127:
                returnValue = "Norway";
                break;
            case 128:
                returnValue = "Oman";
                break;
            case 129:
                returnValue = "Pakistan";
                break;
            case 130:
                returnValue = "Palau";
                break;
            case 131:
                returnValue = "Panama";
                break;
            case 132:
                returnValue = "Papua New Guinea";
                break;
            case 133:
                returnValue = "Paraguay";
                break;
            case 134:
                returnValue = "Peru";
                break;
            case 135:
                returnValue = "Philippines";
                break;
            case 136:
                returnValue = "Poland";
                break;
            case 137:
                returnValue = "Portugal";
                break;
            case 138:
                returnValue = "Qatar";
                break;
            case 139:
                returnValue = "South Korea";
                break;
            case 140:
                returnValue = "Republic of Moldova";
                break;
            case 141:
                returnValue = "Romania";
                break;
            case 142:
                returnValue = "Russian Federation";
                break;
            case 143:
                returnValue = "Rwanda";
                break;
            case 144:
                returnValue = "St Kitts and Nevis";
                break;
            case 145:
                returnValue = "Saint Lucia";
                break;
            case 146:
                returnValue = "St Vincent";
                break;
            case 147:
                returnValue = "Samoa";
                break;
            case 148:
                returnValue = "San Marino";
                break;
            case 149:
                returnValue = "Sao Tome and Principe";
                break;
            case 150:
                returnValue = "Saudi Arabia";
                break;
            case 151:
                returnValue = "Senegal";
                break;
            case 152:
                returnValue = "Serbia";
                break;
            case 153:
                returnValue = "Seychelles";
                break;
            case 154:
                returnValue = "Sierra Leone";
                break;
            case 155:
                returnValue = "Singapore";
                break;
            case 156:
                returnValue = "Slovakia";
                break;
            case 157:
                returnValue = "Slovenia";
                break;
            case 158:
                returnValue = "Solomon Islands";
                break;
            case 159:
                returnValue = "Somalia";
                break;
            case 160:
                returnValue = "South Africa";
                break;
            case 161:
                returnValue = "South Sudan";
                break;
            case 162:
                returnValue = "Spain";
                break;
            case 163:
                returnValue = "Sri Lanka";
                break;
            case 164:
                returnValue = "Sudan";
                break;
            case 165:
                returnValue = "Suriname";
                break;
            case 166:
                returnValue = "Swaziland";
                break;
            case 167:
                returnValue = "Sweden";
                break;
            case 168:
                returnValue = "Switzerland";
                break;
            case 169:
                returnValue = "Syrian Arab Republic";
                break;
            case 170:
                returnValue = "Tajikistan";
                break;
            case 171:
                returnValue = "Thailand";
                break;
            case 172:
                returnValue = "Timor-Leste";
                break;
            case 173:
                returnValue = "Togo";
                break;
            case 174:
                returnValue = "Tonga";
                break;
            case 175:
                returnValue = "Trinidad and Tobago";
                break;
            case 176:
                returnValue = "Tunisia";
                break;
            case 177:
                returnValue = "Turkey";
                break;
            case 178:
                returnValue = "Turkmenistan";
                break;
            case 179:
                returnValue = "Tuvalu";
                break;
            case 180:
                returnValue = "Uganda";
                break;
            case 181:
                returnValue = "Ukraine";
                break;
            case 182:
                returnValue = "UAE";
                break;
            case 183:
                returnValue = "UK";
                break;
            case 184:
                returnValue = "Tanzania";
                break;
            case 185:
                returnValue = "USA";
                break;
            case 186:
                returnValue = "Uruguay";
                break;
            case 187:
                returnValue = "Uzbekistan";
                break;
            case 188:
                returnValue = "Vanuatu";
                break;
            case 189:
                returnValue = "Venezuela";
                break;
            case 190:
                returnValue = "Vietnam";
                break;
            case 191:
                returnValue = "Yemen";
                break;
            case 192:
                returnValue = "Zambia";
                break;
            case 193:
                returnValue = "Zimbabwe";
                break;
        }

        return returnValue;
    }

    /**
     * Returns human-readable String values for Language codes provided as integers.
     *
     * @param languageID int code for various language Strings
     * @return
     */
    public String decodeLanguage(int languageID) {

        String returnValue = "";

        switch (languageID) {
            case 1:
                returnValue = "Afrikaans";
                break;

            case 2:
                returnValue = "Akan";
                break;

            case 3:
                returnValue = "Albanian";
                break;

            case 4:
                returnValue = "Amharic";
                break;

            case 5:
                returnValue = "Arabic";
                break;

            case 6:
                returnValue = "Armenian";
                break;

            case 7:
                returnValue = "Azerbaijani";
                break;

            case 8:
                returnValue = "Belarusian";
                break;

            case 9:
                returnValue = "Bengali";
                break;

            case 10:
                returnValue = "Bosnian";
                break;

            case 11:
                returnValue = "Bulgarian";
                break;

            case 12:
                returnValue = "Burmese";
                break;

            case 13:
                returnValue = "Cantonese";
                break;

            case 14:
                returnValue = "Catalan";
                break;

            case 15:
                returnValue = "Chinese, Simplified";
                break;

            case 16:
                returnValue = "Chinese, Traditional";
                break;

            case 17:
                returnValue = "Chuukese";
                break;

            case 18:
                returnValue = "Croatian";
                break;

            case 19:
                returnValue = "Czech";
                break;

            case 20:
                returnValue = "Danish";
                break;

            case 21:
                returnValue = "Dutch";
                break;

            case 22:
                returnValue = "Dzongkha";
                break;

            case 23:
                returnValue = "English";
                break;

            case 24:
                returnValue = "Estonian";
                break;

            case 25:
                returnValue = "Farsi";
                break;

            case 26:
                returnValue = "Finnish";
                break;

            case 27:
                returnValue = "Flemish";
                break;

            case 28:
                returnValue = "French";
                break;

            case 29:
                returnValue = "Fukienese";
                break;

            case 30:
                returnValue = "Georgian";
                break;

            case 31:
                returnValue = "German";
                break;

            case 32:
                returnValue = "Greek";
                break;

            case 33:
                returnValue = "Gujarati";
                break;

            case 34:
                returnValue = "Haitian Creole";
                break;

            case 35:
                returnValue = "Hebrew";
                break;

            case 36:
                returnValue = "Hindi";
                break;

            case 37:
                returnValue = "Hmong";
                break;

            case 38:
                returnValue = "Hungarian";
                break;

            case 39:
                returnValue = "Icelandic";
                break;

            case 40:
                returnValue = "Indonesian";
                break;

            case 41:
                returnValue = "Italian";
                break;

            case 42:
                returnValue = "Japanese";
                break;

            case 43:
                returnValue = "Javanese";
                break;

            case 44:
                returnValue = "Kannada";
                break;

            case 45:
                returnValue = "Kazakh";
                break;

            case 46:
                returnValue = "Khalkha Mongolian";
                break;

            case 47:
                returnValue = "Khmer";
                break;

            case 48:
                returnValue = "Korean";
                break;

            case 49:
                returnValue = "Kurdish";
                break;

            case 50:
                returnValue = "Lao";
                break;

            case 51:
                returnValue = "Latin";
                break;

            case 52:
                returnValue = "Latvian";
                break;

            case 53:
                returnValue = "Lingala";
                break;

            case 54:
                returnValue = "Lithuanian";
                break;

            case 55:
                returnValue = "Macedonian";
                break;

            case 56:
                returnValue = "Malagasy";
                break;

            case 57:
                returnValue = "Malay";
                break;

            case 58:
                returnValue = "Maltese";
                break;

            case 59:
                returnValue = "Mandarin";
                break;

            case 60:
                returnValue = "Marathi";
                break;

            case 61:
                returnValue = "Marshallese";
                break;

            case 62:
                returnValue = "Nepali";
                break;

            case 63:
                returnValue = "Norwegian";
                break;

            case 64:
                returnValue = "Nuer";
                break;

            case 65:
                returnValue = "Nyanja";
                break;

            case 66:
                returnValue = "Pashto";
                break;

            case 67:
                returnValue = "Polish";
                break;

            case 68:
                returnValue = "Portuguese";
                break;

            case 69:
                returnValue = "Punjabi";
                break;

            case 70:
                returnValue = "Quechua";
                break;

            case 71:
                returnValue = "Romani";
                break;

            case 72:
                returnValue = "Romanian";
                break;

            case 73:
                returnValue = "Romansch";
                break;

            case 74:
                returnValue = "Rundi";
                break;

            case 75:
                returnValue = "Russian";
                break;

            case 76:
                returnValue = "Samoan";
                break;

            case 77:
                returnValue = "Serbian";
                break;

            case 78:
                returnValue = "Shanghainese";
                break;

            case 79:
                returnValue = "Sinhala";
                break;

            case 80:
                returnValue = "Slovak";
                break;

            case 81:
                returnValue = "Slovene";
                break;

            case 82:
                returnValue = "Somali";
                break;

            case 83:
                returnValue = "Sotho";
                break;

            case 84:
                returnValue = "Spanish";
                break;

            case 85:
                returnValue = "Swahili";
                break;

            case 86:
                returnValue = "Swedish";
                break;

            case 87:
                returnValue = "Tagalog";
                break;

            case 88:
                returnValue = "Tajik";
                break;

            case 89:
                returnValue = "Tamil";
                break;

            case 90:
                returnValue = "Telugu";
                break;

            case 91:
                returnValue = "Thai";
                break;

            case 92:
                returnValue = "Tibetan";
                break;

            case 93:
                returnValue = "Tigrinya";
                break;

            case 94:
                returnValue = "Tswana";
                break;

            case 95:
                returnValue = "Turkish";
                break;

            case 96:
                returnValue = "Turkmen";
                break;

            case 97:
                returnValue = "Ukrainian";
                break;

            case 98:
                returnValue = "Urdu";
                break;

            case 99:
                returnValue = "Uzbek";
                break;

            case 100:
                returnValue = "Vietnamese";
                break;

            case 101:
                returnValue = "Welsh";
                break;

            case 102:
                returnValue = "Yao";
                break;

            case 103:
                returnValue = "Yiddish";
                break;

            case 104:
                returnValue = "Zulu";
                break;

        }

        return returnValue;
    }


    /**
     * Returns integer values as an array from the JSON object provided
     *
     * @param context
     * @param input   JSON Object to extract data from
     * @param toFind  String value to extract values from within the JSON
     * @return
     */
    public int[] getIntfromJSON(Context context, JSONObject input, String toFind) {

        int[] returnValue = new int[1];

        String temp = "";
        String[] tempArray = new String[1];

        try {
            // target String is read in from the JSON Object
            temp = input.getString(toFind);
            // brackets are removed from the beginning and the end in case it is an array
            temp = temp.replace("[", "").replace("]", "").trim();
        } catch (JSONException e) {
            returnValue[0] = 0;
            return returnValue;
        }

        // if the array contained more than one element, it is split at the separator commas ','
        // and placed in a temporary String[]
        if (temp.contains(",")) {
            tempArray = temp.split(",");
            returnValue = new int[tempArray.length];
        } else {
            // if it was only 1 element, and thus there are no commas, then there is only one element
            // in the temporary String[]
            tempArray[0] = temp;
        }

        if (tempArray.length == 0) {
            returnValue[0] = 0;
            return returnValue;
        }

        // the temporary String[] is then parsed and copied into an int[]
        for (int i = 0; i < tempArray.length; i++) {
            returnValue[i] = Integer.parseInt(tempArray[i]);
        }

        // int[] is then returned
        return returnValue;

    }

    public String[] getStringFromJSON(Context context, JSONObject input, String toFind) {
        String[] returnValue = new String[1];

        String temp = "";

        try {
            // target String is read in from the JSON Object
            temp = input.getString(toFind);
            // brackets are removed from the beginning and the end in case it is an array
            temp = temp.replace("[", "").replace("]", "").trim();
        } catch (JSONException e) {
            e.printStackTrace();
            returnValue[0] = "";
            return returnValue;
        }

        if (temp.contains(",")) {
            returnValue = temp.split(",");
        } else {
            returnValue[0] = temp;
        }

        if(returnValue.length == 0){
            returnValue[0] = "";
            return returnValue;
        }

        return returnValue;
    }

}