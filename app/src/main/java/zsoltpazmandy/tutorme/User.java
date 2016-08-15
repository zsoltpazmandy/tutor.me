package zsoltpazmandy.tutorme;

import java.util.HashMap;
import java.util.Set;

/**
 * Functions related to User Records, updater functions, user records management
 * <p/>
 * Created by zsolt on 30/06/16.
 */
public class User {

    private Cloud c;

    private String id;
    private String email;
    private String username;

    public User() {
        c = new Cloud();
    }

    public User(String id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashMap<String, Object> buildUserHashMap(String id, String username, String email, String age, String language1, String language2, String language3, String location) {

        HashMap<String, String> authored = new HashMap<>();
        authored.put("none", "none");
        HashMap<String, String> training = new HashMap<>();
        training.put("none", "none");
        HashMap<String, String> learning = new HashMap<>();
        learning.put("none", "none");
        HashMap<String, String> progress = new HashMap<>();
        progress.put("none", "none");

        HashMap<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", username);
        user.put("email", email);
        user.put("age", age);
        user.put("language1", language1);
        user.put("language2", language2);
        user.put("language3", language3);
        user.put("location", location);
        user.put("authored", authored);
        user.put("training", training);
        user.put("learning", learning);
        user.put("progress", progress);

        return user;
    }


    public String getWhoTrainsMeThis(HashMap<String, Object> userMap, String moduleID) {
        HashMap<String, String> learningMap = (HashMap<String, String>) userMap.get("learning");
        return learningMap.get(moduleID);
    }


    public boolean isLearning(HashMap<String, Object> user, String moduleID) {
        HashMap<String, String> learning = (HashMap<String, String>) user.get("learning");
        Set<String> learningIDset = learning.keySet();

        for (String s : learningIDset) {
            if (s.equals(moduleID)) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, Object> addToLearning(HashMap<String, Object> userMap, String IDofTutor, String modID, String modName, String noOfSlides) {

        HashMap<String, String> learningMap = (HashMap<String, String>) userMap.get("learning");
        if (learningMap.containsKey("none")) learningMap.remove("none");
        learningMap.put(modID, IDofTutor);

        HashMap<String, String> progressMap = (HashMap<String, String>) userMap.get("progress");
        if (progressMap.containsKey("none")) progressMap.remove("none");
        progressMap.put(modID, modName + "_" + noOfSlides + "_0");

        userMap.remove("learning");
        userMap.remove("progress");
        userMap.put("learning", learningMap);
        userMap.put("progress", progressMap);

        Cloud c = new Cloud();
        c.saveUserHashMapInCloud(userMap);

        return userMap;
    }

    public String assignTutor(HashMap<String, Object> userMap, HashMap<String, String> moduleMap) {

        String tutorID = moduleMap.get("author");

        c.saveUserHashMapInCloud(userMap);
        c.addToTrainersTrainees(tutorID, userMap.get("id").toString(), moduleMap.get("id").toString());

        return tutorID;
    }

    public int getLastSlideViewed(HashMap<String, Object> userMap, String moduleID) {
        int lastSlide = 0;

        HashMap<String, String> progressMap = (HashMap<String, String>) userMap.get("progress");
        Set<String> modIDS = progressMap.keySet();

        for (String s : modIDS) {
            if (s.equals(moduleID)) {
                lastSlide = Integer.parseInt(progressMap.get(s).split("_")[2]);
            }
        }
        return lastSlide;
    }

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
}