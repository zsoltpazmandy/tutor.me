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
 * Created by zsolt on 30/06/16.
 */
public class User {

    private int id;

    private String username;
    private String password;
    private double rating;
    private int[] learning;
    private int[] training;
    private int[] languages;
    private int age;
    private int location;
    private int[] interests;

    public User(Context context) {

    }

    public User(Context context, int id) {

        this.id = id;

        JSONObject user = new JSONObject();

        try {

            user = getUser(context, id);

        } catch (JSONException e) {
            System.out.println("Cannot find username in database");
            Toast.makeText(context, "Cannot find username in database", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {

            this.username = user.getString("Username");
            this.password = user.getString("Password");
            this.rating = user.getDouble("Rating");
            String[] tempLearning = user.getString("Learning").replace("[", "").replace("]", "").split(",");
            this.learning = new int[tempLearning.length];
            for (int i = 0; i < tempLearning.length; i++) {
                this.learning[i] = Integer.parseInt(tempLearning[i]);
            }
            String[] tempTraining = user.getString("Training").replace("[", "").replace("]", "").split(",");
            this.training = new int[tempTraining.length];
            for (int i = 0; i < tempTraining.length; i++) {
                this.training[i] = Integer.parseInt(tempTraining[i]);
            }
            String[] tempLanguages = user.getString("Languages").replace("[", "").replace("]", "").split(",");
            this.languages = new int[tempLanguages.length];
            for (int i = 0; i < tempLanguages.length; i++) {
                this.languages[i] = Integer.parseInt(tempLanguages[i]);
            }
            this.age = user.getInt("Age");
            this.location = user.getInt("Location");
            String[] tempInterests = user.getString("Interests").replace("[", "").replace("]", "").split(",");
            for (int i = 0; i < tempInterests.length; i++) {
                this.interests[i] = Integer.parseInt(tempInterests[i]);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

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

    public int register(Context context, String username, String password) throws JSONException, IOException {

        int returnIDtoReg = 0;

        boolean taken = true;
        boolean flag = true;

        taken = isUsernameTaken(context, username);

        if (!taken) {
            JSONObject newUser = new JSONObject();

            int newId = assignID(context);
            returnIDtoReg = newId;

            newUser.put("ID", newId).put("Username", username).put("Password", password);

            if(!saveUser(context, newUser)){
                flag = false;
            }

            if(!setUserRecordsJSON(context, getUserRecords(context).accumulate("IDs", newId).toString())){
                flag = false;
            }

        } else {
            flag = false;
        }

        return returnIDtoReg;
    }

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

    public JSONObject loadUser(Context context, String id) throws JSONException {

        JSONObject returnUser = new JSONObject();
        FileInputStream fileInput = null;

        try {

            fileInput = context.openFileInput("user" + id);
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String oneBigString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                oneBigString += read_data;
                data = new char[100];
            }

            returnUser = new JSONObject(oneBigString);

        } catch (IOException e) {

            JSONObject newUser = new JSONObject();
            newUser.put("ID", id);
            newUser.put("Username", "unknown");
            newUser.put("Password", "unknown");

            FileOutputStream fou = null;

            try {

                fou = context.openFileOutput("user" + id, Context.MODE_PRIVATE);
                OutputStreamWriter osw = new OutputStreamWriter(fou);
                osw.write(newUser.toString());
                osw.flush();
                osw.close();

            } catch (FileNotFoundException e2) {
                e.printStackTrace();
            } catch (IOException e3) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnUser;
    }

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

    public void resetCounter(Context context) throws IOException, JSONException {

        JSONObject newUserRecords = new JSONObject();
        newUserRecords.put("IDs", 0);

        if(setUserRecordsJSON(context, newUserRecords.toString())){
            Toast.makeText(context, "User counter reset", Toast.LENGTH_SHORT).show();
        }

    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getRating() {
        return rating;
    }

    public int[] getLearning() {
        return learning;
    }

    public int[] getTraining() {
        return training;
    }

    public int[] getLanguages() {
        return languages;
    }

    public int getAge() {
        return age;
    }

    public int getLocation() {
        return location;
    }

    public int[] getInterests() {
        return interests;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setLearning(int[] learning) {
        this.learning = learning;
    }

    public void setTraining(int[] training) {
        this.training = training;
    }

    public void setLanguages(int[] languages) {
        this.languages = languages;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setInterests(int[] interests) {
        this.interests = interests;
    }

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

    public String decodeCountry(int countryID) {

        String returnValue = "";

        switch (countryID) {
            case 0:
                returnValue = "Afghanistan";
                break;
            case 1:
                returnValue = "Albania";
                break;
            case 2:
                returnValue = "Algeria";
                break;
            case 3:
                returnValue = "Andorra";
                break;
            case 4:
                returnValue = "Angola";
                break;
            case 5:
                returnValue = "Antigua and Barbuda";
                break;
            case 6:
                returnValue = "Argentina";
                break;
            case 7:
                returnValue = "Armenia";
                break;
            case 8:
                returnValue = "Australia";
                break;
            case 9:
                returnValue = "Austria";
                break;
            case 10:
                returnValue = "Azerbaijan";
                break;
            case 11:
                returnValue = "Bahamas";
                break;
            case 12:
                returnValue = "Bahrain";
                break;
            case 13:
                returnValue = "Bangladesh";
                break;
            case 14:
                returnValue = "Barbados";
                break;
            case 15:
                returnValue = "Belarus";
                break;
            case 16:
                returnValue = "Belgium";
                break;
            case 17:
                returnValue = "Belize";
                break;
            case 18:
                returnValue = "Benin";
                break;
            case 19:
                returnValue = "Bhutan";
                break;
            case 20:
                returnValue = "Bolivia";
                break;
            case 21:
                returnValue = "Bosnia and Herzegovina";
                break;
            case 22:
                returnValue = "Botswana";
                break;
            case 23:
                returnValue = "Brazil";
                break;
            case 24:
                returnValue = "Brunei Darussalam";
                break;
            case 25:
                returnValue = "Bulgaria";
                break;
            case 26:
                returnValue = "Burkina Faso";
                break;
            case 27:
                returnValue = "Burundi";
                break;
            case 28:
                returnValue = "Cabo Verde";
                break;
            case 29:
                returnValue = "Cambodia";
                break;
            case 30:
                returnValue = "Cameroon";
                break;
            case 31:
                returnValue = "Canada";
                break;
            case 32:
                returnValue = "Central African Republic";
                break;
            case 33:
                returnValue = "Chad";
                break;
            case 34:
                returnValue = "Chile";
                break;
            case 35:
                returnValue = "China";
                break;
            case 36:
                returnValue = "Colombia";
                break;
            case 37:
                returnValue = "Comoros";
                break;
            case 38:
                returnValue = "Congo";
                break;
            case 39:
                returnValue = "Costa Rica";
                break;
            case 40:
                returnValue = "Côte d'Ivoire";
                break;
            case 41:
                returnValue = "Croatia";
                break;
            case 42:
                returnValue = "Cuba";
                break;
            case 43:
                returnValue = "Cyprus";
                break;
            case 44:
                returnValue = "Czech Republic";
                break;
            case 45:
                returnValue = "Democratic People's Republic of Korea (North Korea)";
                break;
            case 46:
                returnValue = "Democratic Republic of the Cong";
                break;
            case 47:
                returnValue = "Denmark";
                break;
            case 48:
                returnValue = "Djibouti";
                break;
            case 49:
                returnValue = "Dominica";
                break;
            case 50:
                returnValue = "Dominican Republic";
                break;
            case 51:
                returnValue = "Ecuador";
                break;
            case 52:
                returnValue = "Egypt";
                break;
            case 53:
                returnValue = "El Salvador";
                break;
            case 54:
                returnValue = "Equatorial Guinea";
                break;
            case 55:
                returnValue = "Eritrea";
                break;
            case 56:
                returnValue = "Estonia";
                break;
            case 57:
                returnValue = "Ethiopia";
                break;
            case 58:
                returnValue = "Fiji";
                break;
            case 59:
                returnValue = "Finland";
                break;
            case 60:
                returnValue = "France";
                break;
            case 61:
                returnValue = "Gabon";
                break;
            case 62:
                returnValue = "Gambia";
                break;
            case 63:
                returnValue = "Georgia";
                break;
            case 64:
                returnValue = "Germany";
                break;
            case 65:
                returnValue = "Ghana";
                break;
            case 66:
                returnValue = "Greece";
                break;
            case 67:
                returnValue = "Grenada";
                break;
            case 68:
                returnValue = "Guatemala";
                break;
            case 69:
                returnValue = "Guinea";
                break;
            case 70:
                returnValue = "Guinea-Bissau";
                break;
            case 71:
                returnValue = "Guyana";
                break;
            case 72:
                returnValue = "Haiti";
                break;
            case 73:
                returnValue = "Honduras";
                break;
            case 74:
                returnValue = "Hungary";
                break;
            case 75:
                returnValue = "Iceland";
                break;
            case 76:
                returnValue = "India";
                break;
            case 77:
                returnValue = "Indonesia";
                break;
            case 78:
                returnValue = "Iran";
                break;
            case 79:
                returnValue = "Iraq";
                break;
            case 80:
                returnValue = "Ireland";
                break;
            case 81:
                returnValue = "Israel";
                break;
            case 82:
                returnValue = "Italy";
                break;
            case 83:
                returnValue = "Jamaica";
                break;
            case 84:
                returnValue = "Japan";
                break;
            case 85:
                returnValue = "Jordan";
                break;
            case 86:
                returnValue = "Kazakhstan";
                break;
            case 87:
                returnValue = "Kenya";
                break;
            case 88:
                returnValue = "Kiribati";
                break;
            case 89:
                returnValue = "Kuwait";
                break;
            case 90:
                returnValue = "Kyrgyzstan";
                break;
            case 91:
                returnValue = "Lao People's Democratic Republic (Laos)";
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
                returnValue = "Micronesia (Federated States of)";
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
                returnValue = "Republic of Korea (South Korea)";
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
                returnValue = "Saint Kitts and Nevis";
                break;
            case 145:
                returnValue = "Saint Lucia";
                break;
            case 146:
                returnValue = "Saint Vincent and the Grenadines";
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
                returnValue = "United Arab Emirates";
                break;
            case 183:
                returnValue = "United Kingdom of Great Britain and Northern Ireland";
                break;
            case 184:
                returnValue = "United Republic of Tanzania";
                break;
            case 185:
                returnValue = "United States of America";
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

    public String decodeLanguage(int languageID) {

        String returnValue = "";

        switch (languageID) {
            case 0:
                returnValue = "Afrikaans";
                break;
            case 1:
                returnValue = "Akan";
                break;
            case 2:
                returnValue = "Albanian";
                break;
            case 3:
                returnValue = "Amharic";
                break;
            case 4:
                returnValue = "Arabic";
                break;
            case 5:
                returnValue = "Armenian";
                break;
            case 6:
                returnValue = "Azerbaijani";
                break;
            case 7:
                returnValue = "Belarusian";
                break;
            case 8:
                returnValue = "Bengali";
                break;
            case 9:
                returnValue = "Bosnian";
                break;
            case 10:
                returnValue = "Bulgarian";
                break;
            case 11:
                returnValue = "Burmese";
                break;
            case 12:
                returnValue = "Cantonese";
                break;
            case 13:
                returnValue = "Catalan";
                break;
            case 14:
                returnValue = "Chinese, Simplified";
                break;
            case 15:
                returnValue = "Chinese, Traditional";
                break;
            case 16:
                returnValue = "Chuukese";
                break;
            case 17:
                returnValue = "Croatian";
                break;
            case 18:
                returnValue = "Czech";
                break;
            case 19:
                returnValue = "Danish";
                break;
            case 20:
                returnValue = "Dutch";
                break;
            case 21:
                returnValue = "Dzongkha";
                break;
            case 22:
                returnValue = "English";
                break;
            case 23:
                returnValue = "Estonian";
                break;
            case 24:
                returnValue = "Farsi";
                break;
            case 25:
                returnValue = "Finnish";
                break;
            case 26:
                returnValue = "Flemish";
                break;
            case 27:
                returnValue = "French";
                break;
            case 28:
                returnValue = "Fukienese";
                break;
            case 29:
                returnValue = "Georgian";
                break;
            case 30:
                returnValue = "German";
                break;
            case 31:
                returnValue = "Greek";
                break;
            case 32:
                returnValue = "Gujarati";
                break;
            case 33:
                returnValue = "Haitian Creole";
                break;
            case 34:
                returnValue = "Hebrew";
                break;
            case 35:
                returnValue = "Hindi";
                break;
            case 36:
                returnValue = "Hmong";
                break;
            case 37:
                returnValue = "Hungarian";
                break;
            case 38:
                returnValue = "Icelandic";
                break;
            case 39:
                returnValue = "Indonesian";
                break;
            case 40:
                returnValue = "Italian";
                break;
            case 41:
                returnValue = "Japanese";
                break;
            case 42:
                returnValue = "Javanese";
                break;
            case 43:
                returnValue = "Kannada";
                break;
            case 44:
                returnValue = "Kazakh";
                break;
            case 45:
                returnValue = "Khalkha Mongolian";
                break;
            case 46:
                returnValue = "Khmer";
                break;
            case 47:
                returnValue = "Korean";
                break;
            case 48:
                returnValue = "Kurdish";
                break;
            case 49:
                returnValue = "Lao";
                break;
            case 50:
                returnValue = "Latin";
                break;
            case 51:
                returnValue = "Latvian";
                break;
            case 52:
                returnValue = "Lingala";
                break;
            case 53:
                returnValue = "Lithuanian";
                break;
            case 54:
                returnValue = "Macedonian";
                break;
            case 55:
                returnValue = "Malagasy";
                break;
            case 56:
                returnValue = "Malay";
                break;
            case 57:
                returnValue = "Maltese";
                break;
            case 58:
                returnValue = "Mandarin";
                break;
            case 59:
                returnValue = "Marathi";
                break;
            case 60:
                returnValue = "Marshallese";
                break;
            case 61:
                returnValue = "Nepali";
                break;
            case 62:
                returnValue = "Norwegian";
                break;
            case 63:
                returnValue = "Nuer";
                break;
            case 64:
                returnValue = "Nyanja";
                break;
            case 65:
                returnValue = "Pashto";
                break;
            case 66:
                returnValue = "Polish";
                break;
            case 67:
                returnValue = "Portuguese";
                break;
            case 68:
                returnValue = "Punjabi";
                break;
            case 69:
                returnValue = "Quechua";
                break;
            case 70:
                returnValue = "Romani";
                break;
            case 71:
                returnValue = "Romanian";
                break;
            case 72:
                returnValue = "Romansch";
                break;
            case 73:
                returnValue = "Rundi";
                break;
            case 74:
                returnValue = "Russian";
                break;
            case 75:
                returnValue = "Samoan";
                break;
            case 76:
                returnValue = "Serbian";
                break;
            case 77:
                returnValue = "Shanghainese";
                break;
            case 78:
                returnValue = "Sinhala";
                break;
            case 79:
                returnValue = "Slovak";
                break;
            case 80:
                returnValue = "Slovene";
                break;
            case 81:
                returnValue = "Somali";
                break;
            case 82:
                returnValue = "Sotho";
                break;
            case 83:
                returnValue = "Spanish";
                break;
            case 84:
                returnValue = "Swahili";
                break;
            case 85:
                returnValue = "Swedish";
                break;
            case 86:
                returnValue = "Tagalog";
                break;
            case 87:
                returnValue = "Tajik";
                break;
            case 88:
                returnValue = "Tamil";
                break;
            case 89:
                returnValue = "Telugu";
                break;
            case 90:
                returnValue = "Thai";
                break;
            case 91:
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

}
