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
import java.util.HashMap;
import java.util.List;

/**
 * Created by zsolt on 29/06/16.
 * All Module management functions are found here
 */
public class Module {

    private String name;
    private String description;
    private int pro;
    private String author;
    private ArrayList<Integer> reviews;
    private ArrayList<Integer> trainers;
    private ArrayList<Integer> typesOfSlides;
    private int noOfSlides;
    private String ID;

    private int moduleCount;

    public Module() {
    }

    public Module(String name,
                  String description,
                  int pro,
                  String author,
                  ArrayList<Integer> reviews,
                  ArrayList<Integer> trainers,
                  ArrayList<Integer> typesOfSlides,
                  int noOfSlides,
                  String ID) {

        this.name = name;
        this.description = description;
        this.pro = pro;
        this.author = author;
        this.reviews = reviews;
        this.trainers = trainers;
        this.typesOfSlides = typesOfSlides;
        this.noOfSlides = noOfSlides;
        this.ID = ID;

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfSlides() {
        return noOfSlides;
    }

    public void setNoOfSlides(int noOfSlides) {
        this.noOfSlides = noOfSlides;
    }

    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }

    public ArrayList<Integer> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Integer> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<Integer> getTrainers() {
        return trainers;
    }

    public void setTrainers(ArrayList<Integer> trainers) {
        this.trainers = trainers;
    }

    public ArrayList<Integer> getTypesOfSlides() {
        return typesOfSlides;
    }

    public void setTypesOfSlides(ArrayList<Integer> typesOfSlides) {
        this.typesOfSlides = typesOfSlides;
    }

    public JSONObject getModuleRecordsJSON(Context context) throws JSONException {
        FileInputStream fileInput = null;

        JSONObject moduleRecordsJSON = new JSONObject();
        try {

            fileInput = context.openFileInput("module_records");
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

            moduleRecordsJSON = new JSONObject(oneBigString);

        } catch (FileNotFoundException FNFE) {

            // if the file does not exist, it's created from scratch

            JSONObject newModuleRecords = new JSONObject();
            newModuleRecords.put("IDs", "000000");
            setModuleRecordsJSON(context, newModuleRecords.toString());
            return getModuleRecordsJSON(context);

        } catch (JSONException | IOException e) {

            // if the file exists, but there is no data found inside, it is initialised

            JSONObject newModuleRecords = new JSONObject();
            newModuleRecords.put("IDs", "000000");
            setModuleRecordsJSON(context, newModuleRecords.toString());
            return getModuleRecordsJSON(context);
        }
        return moduleRecordsJSON;
    }

    public void setModuleRecordsJSON(Context context, String moduleRecordsString) {

        try {


            FileOutputStream fou = context.openFileOutput("module_records", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(moduleRecordsString);
            osw.flush();
            osw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateModuleRecords(Context context, JSONObject module) throws JSONException {

        FileInputStream fileInput = null;

        try {

            fileInput = context.openFileInput("module_records");
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String oneBigString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                oneBigString += read_data;
                data = new char[100];
            }

            JSONObject moduleRecordsJSON = new JSONObject(oneBigString);
            moduleRecordsJSON.accumulate("IDs", module.getString("ID"));
            setModuleRecordsJSON(context, moduleRecordsJSON.toString());

        } catch (FileNotFoundException FNFE) {

            JSONObject newModuleRecords = new JSONObject();
            newModuleRecords.accumulate("IDs", "000000");
            setModuleRecordsJSON(context, newModuleRecords.toString());

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    public JSONObject getModuleByID(Context context, String id) {
        JSONObject theModule = null;
        if (id.length() == 0) {
            return theModule;
        }

        if (id.substring(1, id.length() - 1).equals("000000")) {
            return theModule;
        }

        FileInputStream fileInput = null;

        try {

            fileInput = context.openFileInput("module" + id.substring(1, id.length() - 1));

            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }
            theModule = new JSONObject(moduleString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return theModule;
    }

    public JSONObject getModuleByName(Context context, String name) throws IOException, JSONException {
        JSONObject returnObject = new JSONObject();

        FileInputStream fileInput = null;

        List<String> IDs = getIDs(context);
        for (int i = 0; i < moduleCount(context); i++) {
            fileInput = context.openFileInput("module" + IDs.get(i).substring(1, IDs.get(i).length() - 1));
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }

            JSONObject currentObj = new JSONObject(moduleString);

            if (currentObj.getString("Name").equals(name)) {
                returnObject = currentObj;
            }

        }

        return returnObject;
    }

    public ArrayList<String> getModuleNames(Context context) throws IOException, JSONException {
        ArrayList<String> namesToReturn = new ArrayList<>();

        FileInputStream fileInput = null;

        ArrayList<String> modIDs = getIDs(context);

        for (int i = 0; i < moduleCount(context); i++) {


            fileInput = context.openFileInput("module" + modIDs.get(i).substring(1, modIDs.get(i).length() - 1));
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }

            JSONObject currentObj = new JSONObject(moduleString);
            namesToReturn.add(currentObj.getString("Name"));
        }

        return namesToReturn;
    }

    public ArrayList<String> getIDs(Context context) throws JSONException, IOException {

        ArrayList<String> IDsToReturn = new ArrayList<>();

        String temp;
        for (int i = 1; i <= moduleCount(context); i++) {
            temp = getModuleRecordsJSON(context).getString("IDs");
            temp = temp.replace("[", "").replace("]", "");
            if (temp.contains(",")) {
                if (!temp.split(",")[i].equals("\"\""))
                    IDsToReturn.add(temp.split(",")[i]);
            } else {
                if (!temp.equals("\"\""))
                    IDsToReturn.add(temp);
            }
        }

        return IDsToReturn;
    }

    public int moduleCount(Context context) throws JSONException, IOException {
        JSONObject moduleRecordsJSON = getModuleRecordsJSON(context);
        int amountOfModules = 0;

        try {
            amountOfModules = moduleRecordsJSON.getJSONArray("IDs").length() - 1;
        } catch (JSONException e) {
            try {
                if (moduleRecordsJSON.getString("IDs").equals("[\"\",\"000001\"]")) {
                    amountOfModules = 1;
                }
            } catch (JSONException e2) {
                setModuleRecordsJSON(context, moduleRecordsJSON.put("IDs", "").toString());
                amountOfModules = 0;
            }
        }
        return amountOfModules;
    }

    public void saveModuleLocally(Context context, JSONObject module) {
        try {

//            String ID = String.valueOf(moduleCount(context) + 1);
//
//            String IDpadded = "";
//
//            switch (ID.length()) {
//                case 1:
//                    IDpadded = "00000" + ID;
//                    break;
//                case 2:
//                    IDpadded = "0000" + ID;
//                    break;
//                case 3:
//                    IDpadded = "000" + ID;
//                    break;
//                case 4:
//                    IDpadded = "00" + ID;
//                    break;
//                case 5:
//                    IDpadded = "0" + ID;
//                    break;
//            }
//            module.put("ID", IDpadded);
            updateModuleRecords(context, module);
            FileOutputStream fou = context.openFileOutput("module" + module.getString("ID"), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(module.toString());
            osw.flush();
            osw.close();

            Toast.makeText(context, "Module successfully added to Library", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(module.toString());
    }


    public HashMap<String, Object> removeSlide(Context context, HashMap<String, Object> moduleMap, int indexOfSlideToRemove) {

        ArrayList<String> allSlides = new ArrayList<>();
        ArrayList<Integer> allTypes = new ArrayList<>();

        int slideCount = Integer.parseInt(moduleMap.get("noOfSlides").toString());

//        String typesString = module.getString("Types of Slides");
//        String[] typesStringArray = new String[1];
//        typesString = typesString.replace("[", "").replace("]", "");
//        if (typesString.contains(",")) {
//            typesStringArray = typesString.split(",");
//        } else {
//            typesStringArray[0] = typesString;
//        }

        HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");

        for (int i = 1; i <= slideCount; i++) {
            allSlides.add(moduleMap.get("Slide_" + i).toString());
            allTypes.add(Integer.parseInt(typesMap.get("Slide_"+i)));
        }

        for (int i = 1; i <= slideCount; i++) {
            moduleMap.remove("Slide_" + i);
        }
        moduleMap.remove("typesOfSlides");

        HashMap<String, String> newTypesMap = new HashMap<>();

        allSlides.remove(indexOfSlideToRemove);
        allTypes.remove(indexOfSlideToRemove);

        for (int i = 1; i <= slideCount; i++) {
            moduleMap.remove("Slide_" + i);
        }

        for (int i = 1; i <= slideCount - 1; i++) {
            moduleMap.put("Slide_" + i, allSlides.get(i - 1));
            newTypesMap.put("Slide_"+i, allTypes.get(i-1).toString());
        }

        moduleMap.put("typesOfSlides",newTypesMap);

        slideCount--;
        moduleMap.remove("noOfSlides");
        moduleMap.put("noOfSlides", ""+slideCount);


        return  moduleMap;
//        updateModule(context, module);

    }

    public void updateModule(Context context, JSONObject module) {

        try {

            FileOutputStream fou = context.openFileOutput("module" + module.get("ID").toString().substring(1, module.getString("ID").length() - 1), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(module.toString());
            osw.flush();
            osw.close();
            Toast.makeText(context, "Module successfully updated.", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cloud c = new Cloud();
//        c.saveModuleInCloud(context, module);
    }

    public boolean isNameTaken(Context context, String moduleName) throws IOException, JSONException {

        FileInputStream fileInput = null;

        ArrayList<String> allModuleNames = new ArrayList<>();

        JSONObject currentModule = new JSONObject();

        List<String> IDs = getIDs(context);

        for (int i = 0; i < moduleCount(context); i++) {

            fileInput = context.openFileInput("module" + IDs.get(i).substring(1, IDs.get(i).length() - 1));
            InputStreamReader streamReader = new InputStreamReader(fileInput);
            char[] data = new char[100];
            String moduleString = "";
            int size;

            while ((size = streamReader.read(data)) > 0) {
                String read_data = String.copyValueOf(data, 0, size);
                moduleString += read_data;
                data = new char[100];
            }

            if (moduleString.length() > 0) {
                currentModule = new JSONObject(moduleString);
                allModuleNames.add(currentModule.getString("Name"));
            }

        }

        return !currentModule.equals(null) && allModuleNames.contains(moduleName);
    }

    public ArrayList<Integer> getTrainers(Context context, JSONObject module) {
        ArrayList<Integer> IDsOfTrainers = new ArrayList<>();

        String temp;
        String[] tempArray = new String[1];

        try {
            temp = module.getString("Trainers").replace("[", "").replace("]", "");

            if (temp.contains(",")) {
                tempArray = temp.split(",");
            } else {
                tempArray[0] = temp;
            }

            for (int i = 0; i < tempArray.length; i++) {
                IDsOfTrainers.add(Integer.parseInt(tempArray[i]));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return IDsOfTrainers;
    }

    public int getSlideCount(Context context, String moduleID) {
        int slideCount = 0;

        try {
            slideCount = getModuleByID(context, moduleID).getInt("No. of Slides");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return slideCount;
    }

    public int getSlideType(Context context, HashMap<String,Object> module, int slideNumber) {

        int slideType = 0;

        if (slideNumber > 0) {
            slideNumber--;
        }

        ArrayList<String> typesOfSlides = (ArrayList<String>) module.get("typesOfSlides");

        slideType = Integer.parseInt(typesOfSlides.get(slideNumber));

//        try {
//
//            String[] temp = new String[1];
//            temp[0] = module.getString("Types of Slides").replace("[", "").replace("]", "");
//            if (temp[0].contains(",")) {
//                temp = temp[0].split(",");
//                slideType = Integer.parseInt(temp[slideNumber]);
//            } else {
//                slideType = Integer.parseInt(temp[0]);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return slideType;

    }

    public void purgeLibrary(Context context) throws IOException, JSONException {

        List<String> IDs = getIDs(context);

        if (IDs.size() == 0)
            return;

        for (int i = 0; i < moduleCount(context); i++) {
            FileOutputStream fou = context.openFileOutput("module" + IDs.get(i).substring(1, IDs.get(i).length() - 1), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write("");
            osw.flush();
            osw.close();
        }
    }

    public void resetCounter(Context context) throws IOException, JSONException {

        if (moduleCount(context) > 0) { // it's already at zero, no need to reset
            JSONObject newModRec = new JSONObject();
            newModRec.accumulate("IDs", "000000");
            setModuleRecordsJSON(context, newModRec.toString());
        }
    }

    public void populateLibrary(Context context, JSONObject user) throws IOException, JSONException {

        purgeLibrary(context);
        resetCounter(context);
        Cloud c = new Cloud();


        int counter = 0;

        try {
            String moduleString = "{\n" +
                    "\t\"Name\":\"Name of a sample module 1\",\n" +
                    "\t\"Description\":\"The project aims to create a learning platform that links inquisitive individuals wishing to learn something with capable trainers ready to help them. It is an easy-to-use mobile application with a continuously growing, extensible, community-maintained library of subjects to laren. The purpose of the project is not to design specific curricula or to force a certain methodology on its users. Rather, it is a means of creating and delivering any learning material the community finds worthy of teaching. For the trainers of the community, it is a tutoring tool; for its learners it is a source of knowledge and access to people who other than being proficient in certain subjects, often have invaluable practical experience, too.\",\n" +
                    "\t\"PRO\":0,\n" +
                    "\t\"Author\":\"" + user.getString("Username") + "\",\n" +
                    "\t\"Reviews\":1,\n" +
                    "\t\"Trainers\":1,\n" +
                    "\t\"Types of Slides\":[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1],\n" +
                    "\t\"Slide 1\":\"User: Any user of the service. All users are either 'Trainers' and/or 'Learners'. Users register for free using their email addresses and/or Facebook accounts. During the registration users may mark existing modules (Amateur or Pro) they would be willing to train others in by claiming proficiency in the relevant field. This claim is never centrally verified in any way. Learners express their satisfaction with the performance of their Trainers by rating them. Low ratings will eventually result in the loss of trust of the community, as no Learner would likely choose to be trained by someone who has been rated inadequate by others.\",\n" +
                    "\t\"Slide 2\":\"User Profiles: Basic user information (such as name, age, nationality, geographic location, fields of study) are shown and are visible to everyone on users' profiles. Additionally average rating and a collection of the most recent feedback from other users is displayed on every user's profile. No personal credit card information of any kind is stored anywhere within the system. Payments are planned to be completed using PayPal direct debit payments, the implementation of which, however, is beyond the scope of the current project.\",\n" +
                    "\t\"Slide 3\":\"Trainer: Any user training other users: Trainers are paid by Learners for tutoring them in a Pro module. For Trainers, the platform is a tool that enables them to earn money and the recognition of the community. Any user creating a module will automatically become its Trainer, however, other users can also decide to become Trainers of modules they did not create. A Trainer primarily uses the platform to deliver their learning content and provide relevant tutoring to their Learners. Trainers of a module may also be Learners of other modules at the same time.\",\n" +
                    "\t\"Slide 4\":\"Learner: Any user learning any module (Amateur or Pro) on the platform. Learners may take up any number of Amateur modules free of charge. Learners must pay Trainers for their tutoring given in Pro modules. Although Learners hold fewer responsibilities in the community than Trainers (since they are paying customers), their community rating and feedback is recorded. This is so, in order to ensure their respect of community rules and disciplined participation in the learning process. Since all people have different capabilities, and no prerequisite study of any kind may be taken for granted before a Learner starts a module, no Learner may ever be subjected to being rated based on their learning performance.\",\n" +
                    "\t\"Slide 5\":\"Module: A single unit of a material to be learnt. The scope of a module is defined by the authorJSON and is dependent on the type of material (e.g. a language module may consist of the elicitation, explanation and practice of one or several verb tenses, or a module may consist of 100 pieces of vocabulary). All modules are designed by users who are proficient in the relevant field, this proficiency, however, is not verified in any way. The authorJSON of a module automatically becomes its first Trainer. Future Trainers may also offer tutoring in the same module, in which case Learners may choose their Trainer by comparing the information found on their user profiles (e.g. availability or rating). However, in order to incentivize Trainers' future authoring activities, Learners' choice of Trainer always defaults to the authorJSON of a module (its first Trainer). This is to ensure that a Trainer is rewarded by receiving the most payments if their authored module was acknowledged by the community, further fostering the Trainer's success.\",\n" +
                    "\t\"Slide 6\":\"Amateur Module: An Amateur module can be taken up by anyone, its authorJSON is automatically listed as its Trainer. Any module created initially belongs to this category. It is up to the community to decide whether an Amateur module is worth paying for. Once a [to be determined] amount of Learners have completed the course and have rated the module positively, the module becomes certified i.e. becomes a Pro module. As the Trainer receives no money for tutoring the Learner of an Amateur (i.e. brand new) module, its Trainer will aim to provide the best tutoring service and potentially modify the module to incorporate any constructive criticism from the Learner(s) in order to attract positive ratings from the community, thus ensuring that it would eventually become a Pro module, for which the Trainer would also be paid for.\",\n" +
                    "\t\"Slide 7\":\"Pro Module: When a Learner takes up a Pro module they choose a Trainer from all the available Trainers offering tutoring in that particular module. By choosing a Trainer, the Learner agrees to pay the Trainer. Authors of modules are automatically listed as Trainers of Pro modules, but any other user may also sign up to be a Trainer of a module. If there is more than one available Trainer to a specific module, the Learner may choose which Trainer they wish to be tutored by based on the Trainers' availabilities, experience in training the module or their overall rating. Trainers of Pro modules offer tutoring to Learners by answering their questions, explaining all or parts of the module using an instant messaging system and/or by using recorded voice messages.\",\n" +
                    "\t\"Slide 8\":\"Creating Modules: Any registered user may authorJSON learning modules and have them stored and readily available in the Module Library for other users to find and learn from. The application is intended to provide as much freedom to its trainers in compiling their learning modules as possible. Therefore, several of the most common teaching material design patterns are available for the trainers to choose from. However, for the scope of the current project the creation of modules is limited to text input only. The user may choose from several templates (eg. tables, fill-in-the-gaps, multiple-choice questions etc) to present the learning material. This is to be extended in the future with the possibility of including other media e.g. images, animations, audio and video.\",\n" +
                    "\t\"Slide 9\":\"Training: Trainers are expected to help Learners dependently of the module in question either if they run into any difficulties understanding the material or perhaps Learners need to be guided through the material step-by-step. The platform is not intended to restrict or regulate how the community interacts - it is the community itself that will determine design its learning modules, and thus how much work is required from Trainers. Module descriptions will provide a clear summary of the module content and the amount and type of work required for its completion. Trainers will be rated for their input, and so will Learners - if any user fails to provide adequate tutoring, or respect the rules of the community and/or accept the requirements of a certain learning module, this will reflect in the feedback and rating of the collaborating user. This is the means by which the community is able to regulate and improve its composition: by valuing users' interaction and patience, and praising hard-working contributors by advertising their skills and competence. As an addition to Trainers designing and compiling the learning materials, they also help their Learners in any way they require. This may mean answering to their questions they might have during the completion of the learning module and/or with any relevant exercise, but it is not limited to answering questions. Some modules will require the Trainer's active involvement in the learning process: e.g. in a language learning module the Trainer (likely native speaker of the Learner's target language) would need to provide additional support (i.e. pronunciation of words, audio conversation etc.).\",\n" +
                    "\t\"Slide 10\":\"Rating: All users (Learners and Trainers alike) rate each other based on their performance during or after they collaborated on a module. The rating system ensures that users' track record is visible to all future collaborators. As Learners choose their Trainers based on the Trainer's availability, proficiency, or ratings record, it is crucial for any Trainer to aim to maintain a generally positive rating overall. Likewise, Learners' input, willingness and respect of the general guidelines of the community will be rated by their respective Trainers, which may eventually negatively affect their perception within the community. Any collaboration on a module is concluded by the mutual rating of those involved: Learners rate & provide feedback on their Trainer's performance and vice versa. All user profiles display the latest feedback and ratings activity pertaining to the user in order to give a better understanding of what to expect from an unknown member of the community before accepting to work together. Ratings data and feedback stay on record as long as the user's account exists.\",\n" +
                    "\t\"Slide 11\":\"Development Plan & Expectations: The current paper intends to introduce the general idea of the project along with a list of prioritised critical objectives. The project will primarily focus on these components of the system and will conclude by presenting a list of proposed future extensions. The project is expected to complete the design and implementation of the core functions and logic of the system, and make space for the incorporation of a number of important extensions in the future. The following core elements below have been identified as those without which the fundamental functionality of the platform would not be achievable. The lack of any one of these would mean that the project fails to provide the services it seeks to.\",\n" +
                    "\t\"Slide 12\":\"Core elements: 1. User registration & profiles: Users must be able to register in order for the system to be able to store their personal information and learning data. The development of corresponding classes and functions, therefore, is of the highest priority. Upon launching the application, users are presented with the options to register using using their email addresses or Facebook accounts, or to login using existing account details.\",\n" +
                    "\t\"Slide 13\":\"Core elements: 2. Creating & publishing learning material: Users must be able to create their own modules in the public Module Library. The user community, and user involvement in designing the learning material are key defining factors that lead to the success of the system. The platform's modular quality is key to its versatility, and is therefore a high priority element on the list of core elements to implement.\",\n" +
                    "\t\"Slide 14\":\"Core elements: 3. Choosing & delivering learning material: Users must be able to take up and learn any module they can find in the Module Library. Module content must be made readily and easily accessible in order to ensure core functionality of the platform, i.e. to allow users to learn from other members of the community.\",\n" +
                    "\t\"Slide 15\":\"Core elements: 4. Instant messaging (IM): Users must be able to initiate communication with other members of the community, therefore, the instant messaging service must be implemented within the system from the beginning. Learners must be able to request help from their Trainers and receive tutoring real-time, using email to maintain support between users would be far less efficient in providing support to Learners. \",\n" +
                    "\t\"Slide 16\":\"Core elements: 5. Android Application: Users must be able to access their profiles, learning material, and communications channels which connect them with the rest of the community (whether the user is a Trainer or a Learner) in order to maintain an seamless and efficient service. The development of the application and the integration of the first 4 core logic elements are the final step of the development of the core functions of the platform.\",\n" +
                    "\t\"Slide 17\":[\"User Functions\",\"2 days\",\"Testing\",\"1 day\",\"Create Module\",\"7 days\",\"Testing\",\"1 day\",\"Learn Module\",\"7 days\",\"Testing\",\"1 day\",\"IM\",\"2 days\",\"Testing\",\"1 day\",\"Android application\",\"7 days\",\"UI\",\"1 day\"],\n" +
                    "\t\"Slide 18\":\"Future Extension: 1. Payments using PayPal direct debit, 2. User Rating & feedback, 3. Expand Functionalities within Module Creation (e.g. images, audio/video)\",\n" +
                    "\t\"No. of Slides\":18,\n" +
                    "\t\"ID\":000001\n" +
                    "}";
            JSONObject module = new JSONObject(moduleString);
//            c.saveModuleInCloud(context, module);

            FileOutputStream fou = context.openFileOutput("module000001", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fou);
            osw.write(module.toString());
            osw.flush();
            osw.close();

            updateModuleRecords(context, module);

            String moduleString2 = "{\n" +
                    "\t\"Name\":\"Name of a sample module 2\",\n" +
                    "\t\"Description\":\"The project aims to create a learning platform that links inquisitive individuals wishing to learn something with capable trainers ready to help them. It is an easy-to-use mobile application with a continuously growing, extensible, community-maintained library of subjects to laren. The purpose of the project is not to design specific curricula or to force a certain methodology on its users. Rather, it is a means of creating and delivering any learning material the community finds worthy of teaching. For the trainers of the community, it is a tutoring tool; for its learners it is a source of knowledge and access to people who other than being proficient in certain subjects, often have invaluable practical experience, too.\",\n" +
                    "\t\"PRO\":0,\n" +
                    "\t\"Author\":\"" + user.getString("Username") + "\",\n" +
                    "\t\"Reviews\":1,\n" +
                    "\t\"Trainers\":1,\n" +
                    "\t\"Types of Slides\":[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1],\n" +
                    "\t\"Slide 1\":\"User: Any user of the service. All users are either 'Trainers' and/or 'Learners'. Users register for free using their email addresses and/or Facebook accounts. During the registration users may mark existing modules (Amateur or Pro) they would be willing to train others in by claiming proficiency in the relevant field. This claim is never centrally verified in any way. Learners express their satisfaction with the performance of their Trainers by rating them. Low ratings will eventually result in the loss of trust of the community, as no Learner would likely choose to be trained by someone who has been rated inadequate by others.\",\n" +
                    "\t\"Slide 2\":\"User Profiles: Basic user information (such as name, age, nationality, geographic location, fields of study) are shown and are visible to everyone on users' profiles. Additionally average rating and a collection of the most recent feedback from other users is displayed on every user's profile. No personal credit card information of any kind is stored anywhere within the system. Payments are planned to be completed using PayPal direct debit payments, the implementation of which, however, is beyond the scope of the current project.\",\n" +
                    "\t\"Slide 3\":\"Trainer: Any user training other users: Trainers are paid by Learners for tutoring them in a Pro module. For Trainers, the platform is a tool that enables them to earn money and the recognition of the community. Any user creating a module will automatically become its Trainer, however, other users can also decide to become Trainers of modules they did not create. A Trainer primarily uses the platform to deliver their learning content and provide relevant tutoring to their Learners. Trainers of a module may also be Learners of other modules at the same time.\",\n" +
                    "\t\"Slide 4\":\"Learner: Any user learning any module (Amateur or Pro) on the platform. Learners may take up any number of Amateur modules free of charge. Learners must pay Trainers for their tutoring given in Pro modules. Although Learners hold fewer responsibilities in the community than Trainers (since they are paying customers), their community rating and feedback is recorded. This is so, in order to ensure their respect of community rules and disciplined participation in the learning process. Since all people have different capabilities, and no prerequisite study of any kind may be taken for granted before a Learner starts a module, no Learner may ever be subjected to being rated based on their learning performance.\",\n" +
                    "\t\"Slide 5\":\"Module: A single unit of a material to be learnt. The scope of a module is defined by the authorJSON and is dependent on the type of material (e.g. a language module may consist of the elicitation, explanation and practice of one or several verb tenses, or a module may consist of 100 pieces of vocabulary). All modules are designed by users who are proficient in the relevant field, this proficiency, however, is not verified in any way. The authorJSON of a module automatically becomes its first Trainer. Future Trainers may also offer tutoring in the same module, in which case Learners may choose their Trainer by comparing the information found on their user profiles (e.g. availability or rating). However, in order to incentivize Trainers' future authoring activities, Learners' choice of Trainer always defaults to the authorJSON of a module (its first Trainer). This is to ensure that a Trainer is rewarded by receiving the most payments if their authored module was acknowledged by the community, further fostering the Trainer's success.\",\n" +
                    "\t\"Slide 6\":\"Amateur Module: An Amateur module can be taken up by anyone, its authorJSON is automatically listed as its Trainer. Any module created initially belongs to this category. It is up to the community to decide whether an Amateur module is worth paying for. Once a [to be determined] amount of Learners have completed the course and have rated the module positively, the module becomes certified i.e. becomes a Pro module. As the Trainer receives no money for tutoring the Learner of an Amateur (i.e. brand new) module, its Trainer will aim to provide the best tutoring service and potentially modify the module to incorporate any constructive criticism from the Learner(s) in order to attract positive ratings from the community, thus ensuring that it would eventually become a Pro module, for which the Trainer would also be paid for.\",\n" +
                    "\t\"Slide 7\":\"Pro Module: When a Learner takes up a Pro module they choose a Trainer from all the available Trainers offering tutoring in that particular module. By choosing a Trainer, the Learner agrees to pay the Trainer. Authors of modules are automatically listed as Trainers of Pro modules, but any other user may also sign up to be a Trainer of a module. If there is more than one available Trainer to a specific module, the Learner may choose which Trainer they wish to be tutored by based on the Trainers' availabilities, experience in training the module or their overall rating. Trainers of Pro modules offer tutoring to Learners by answering their questions, explaining all or parts of the module using an instant messaging system and/or by using recorded voice messages.\",\n" +
                    "\t\"Slide 8\":\"Creating Modules: Any registered user may authorJSON learning modules and have them stored and readily available in the Module Library for other users to find and learn from. The application is intended to provide as much freedom to its trainers in compiling their learning modules as possible. Therefore, several of the most common teaching material design patterns are available for the trainers to choose from. However, for the scope of the current project the creation of modules is limited to text input only. The user may choose from several templates (eg. tables, fill-in-the-gaps, multiple-choice questions etc) to present the learning material. This is to be extended in the future with the possibility of including other media e.g. images, animations, audio and video.\",\n" +
                    "\t\"Slide 9\":\"Training: Trainers are expected to help Learners dependently of the module in question either if they run into any difficulties understanding the material or perhaps Learners need to be guided through the material step-by-step. The platform is not intended to restrict or regulate how the community interacts - it is the community itself that will determine design its learning modules, and thus how much work is required from Trainers. Module descriptions will provide a clear summary of the module content and the amount and type of work required for its completion. Trainers will be rated for their input, and so will Learners - if any user fails to provide adequate tutoring, or respect the rules of the community and/or accept the requirements of a certain learning module, this will reflect in the feedback and rating of the collaborating user. This is the means by which the community is able to regulate and improve its composition: by valuing users' interaction and patience, and praising hard-working contributors by advertising their skills and competence. As an addition to Trainers designing and compiling the learning materials, they also help their Learners in any way they require. This may mean answering to their questions they might have during the completion of the learning module and/or with any relevant exercise, but it is not limited to answering questions. Some modules will require the Trainer's active involvement in the learning process: e.g. in a language learning module the Trainer (likely native speaker of the Learner's target language) would need to provide additional support (i.e. pronunciation of words, audio conversation etc.).\",\n" +
                    "\t\"Slide 10\":\"Rating: All users (Learners and Trainers alike) rate each other based on their performance during or after they collaborated on a module. The rating system ensures that users' track record is visible to all future collaborators. As Learners choose their Trainers based on the Trainer's availability, proficiency, or ratings record, it is crucial for any Trainer to aim to maintain a generally positive rating overall. Likewise, Learners' input, willingness and respect of the general guidelines of the community will be rated by their respective Trainers, which may eventually negatively affect their perception within the community. Any collaboration on a module is concluded by the mutual rating of those involved: Learners rate & provide feedback on their Trainer's performance and vice versa. All user profiles display the latest feedback and ratings activity pertaining to the user in order to give a better understanding of what to expect from an unknown member of the community before accepting to work together. Ratings data and feedback stay on record as long as the user's account exists.\",\n" +
                    "\t\"Slide 11\":\"Development Plan & Expectations: The current paper intends to introduce the general idea of the project along with a list of prioritised critical objectives. The project will primarily focus on these components of the system and will conclude by presenting a list of proposed future extensions. The project is expected to complete the design and implementation of the core functions and logic of the system, and make space for the incorporation of a number of important extensions in the future. The following core elements below have been identified as those without which the fundamental functionality of the platform would not be achievable. The lack of any one of these would mean that the project fails to provide the services it seeks to.\",\n" +
                    "\t\"Slide 12\":\"Core elements: 1. User registration & profiles: Users must be able to register in order for the system to be able to store their personal information and learning data. The development of corresponding classes and functions, therefore, is of the highest priority. Upon launching the application, users are presented with the options to register using using their email addresses or Facebook accounts, or to login using existing account details.\",\n" +
                    "\t\"Slide 13\":\"Core elements: 2. Creating & publishing learning material: Users must be able to create their own modules in the public Module Library. The user community, and user involvement in designing the learning material are key defining factors that lead to the success of the system. The platform's modular quality is key to its versatility, and is therefore a high priority element on the list of core elements to implement.\",\n" +
                    "\t\"Slide 14\":\"Core elements: 3. Choosing & delivering learning material: Users must be able to take up and learn any module they can find in the Module Library. Module content must be made readily and easily accessible in order to ensure core functionality of the platform, i.e. to allow users to learn from other members of the community.\",\n" +
                    "\t\"Slide 15\":\"Core elements: 4. Instant messaging (IM): Users must be able to initiate communication with other members of the community, therefore, the instant messaging service must be implemented within the system from the beginning. Learners must be able to request help from their Trainers and receive tutoring real-time, using email to maintain support between users would be far less efficient in providing support to Learners. \",\n" +
                    "\t\"Slide 16\":\"Core elements: 5. Android Application: Users must be able to access their profiles, learning material, and communications channels which connect them with the rest of the community (whether the user is a Trainer or a Learner) in order to maintain an seamless and efficient service. The development of the application and the integration of the first 4 core logic elements are the final step of the development of the core functions of the platform.\",\n" +
                    "\t\"Slide 17\":[\"User Functions\",\"2 days\",\"Testing\",\"1 day\",\"Create Module\",\"7 days\",\"Testing\",\"1 day\",\"Learn Module\",\"7 days\",\"Testing\",\"1 day\",\"IM\",\"2 days\",\"Testing\",\"1 day\",\"Android application\",\"7 days\",\"UI\",\"1 day\"],\n" +
                    "\t\"Slide 18\":\"Future Extension: 1. Payments using PayPal direct debit, 2. User Rating & feedback, 3. Expand Functionalities within Module Creation (e.g. images, audio/video)\",\n" +
                    "\t\"No. of Slides\":18,\n" +
                    "\t\"ID\":000002\n" +
                    "}";
            JSONObject module2 = new JSONObject(moduleString2);
//            c.saveModuleInCloud(context, module2);

            FileOutputStream fou2 = context.openFileOutput("module000002", Context.MODE_PRIVATE);
            OutputStreamWriter osw2 = new OutputStreamWriter(fou2);
            osw2.write(module2.toString());
            osw2.flush();
            osw2.close();

            updateModuleRecords(context, module2);


            String moduleString3 = "{\n" +
                    "\t\"Name\":\"Name of a sample module 3\",\n" +
                    "\t\"Description\":\"The project aims to create a learning platform that links inquisitive individuals wishing to learn something with capable trainers ready to help them. It is an easy-to-use mobile application with a continuously growing, extensible, community-maintained library of subjects to laren. The purpose of the project is not to design specific curricula or to force a certain methodology on its users. Rather, it is a means of creating and delivering any learning material the community finds worthy of teaching. For the trainers of the community, it is a tutoring tool; for its learners it is a source of knowledge and access to people who other than being proficient in certain subjects, often have invaluable practical experience, too.\",\n" +
                    "\t\"PRO\":0,\n" +
                    "\t\"Author\":\"" + user.getString("Username") + "\",\n" +
                    "\t\"Reviews\":1,\n" +
                    "\t\"Trainers\":1,\n" +
                    "\t\"Types of Slides\":[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1],\n" +
                    "\t\"Slide 1\":\"User: Any user of the service. All users are either 'Trainers' and/or 'Learners'. Users register for free using their email addresses and/or Facebook accounts. During the registration users may mark existing modules (Amateur or Pro) they would be willing to train others in by claiming proficiency in the relevant field. This claim is never centrally verified in any way. Learners express their satisfaction with the performance of their Trainers by rating them. Low ratings will eventually result in the loss of trust of the community, as no Learner would likely choose to be trained by someone who has been rated inadequate by others.\",\n" +
                    "\t\"Slide 2\":\"User Profiles: Basic user information (such as name, age, nationality, geographic location, fields of study) are shown and are visible to everyone on users' profiles. Additionally average rating and a collection of the most recent feedback from other users is displayed on every user's profile. No personal credit card information of any kind is stored anywhere within the system. Payments are planned to be completed using PayPal direct debit payments, the implementation of which, however, is beyond the scope of the current project.\",\n" +
                    "\t\"Slide 3\":\"Trainer: Any user training other users: Trainers are paid by Learners for tutoring them in a Pro module. For Trainers, the platform is a tool that enables them to earn money and the recognition of the community. Any user creating a module will automatically become its Trainer, however, other users can also decide to become Trainers of modules they did not create. A Trainer primarily uses the platform to deliver their learning content and provide relevant tutoring to their Learners. Trainers of a module may also be Learners of other modules at the same time.\",\n" +
                    "\t\"Slide 4\":\"Learner: Any user learning any module (Amateur or Pro) on the platform. Learners may take up any number of Amateur modules free of charge. Learners must pay Trainers for their tutoring given in Pro modules. Although Learners hold fewer responsibilities in the community than Trainers (since they are paying customers), their community rating and feedback is recorded. This is so, in order to ensure their respect of community rules and disciplined participation in the learning process. Since all people have different capabilities, and no prerequisite study of any kind may be taken for granted before a Learner starts a module, no Learner may ever be subjected to being rated based on their learning performance.\",\n" +
                    "\t\"Slide 5\":\"Module: A single unit of a material to be learnt. The scope of a module is defined by the authorJSON and is dependent on the type of material (e.g. a language module may consist of the elicitation, explanation and practice of one or several verb tenses, or a module may consist of 100 pieces of vocabulary). All modules are designed by users who are proficient in the relevant field, this proficiency, however, is not verified in any way. The authorJSON of a module automatically becomes its first Trainer. Future Trainers may also offer tutoring in the same module, in which case Learners may choose their Trainer by comparing the information found on their user profiles (e.g. availability or rating). However, in order to incentivize Trainers' future authoring activities, Learners' choice of Trainer always defaults to the authorJSON of a module (its first Trainer). This is to ensure that a Trainer is rewarded by receiving the most payments if their authored module was acknowledged by the community, further fostering the Trainer's success.\",\n" +
                    "\t\"Slide 6\":\"Amateur Module: An Amateur module can be taken up by anyone, its authorJSON is automatically listed as its Trainer. Any module created initially belongs to this category. It is up to the community to decide whether an Amateur module is worth paying for. Once a [to be determined] amount of Learners have completed the course and have rated the module positively, the module becomes certified i.e. becomes a Pro module. As the Trainer receives no money for tutoring the Learner of an Amateur (i.e. brand new) module, its Trainer will aim to provide the best tutoring service and potentially modify the module to incorporate any constructive criticism from the Learner(s) in order to attract positive ratings from the community, thus ensuring that it would eventually become a Pro module, for which the Trainer would also be paid for.\",\n" +
                    "\t\"Slide 7\":\"Pro Module: When a Learner takes up a Pro module they choose a Trainer from all the available Trainers offering tutoring in that particular module. By choosing a Trainer, the Learner agrees to pay the Trainer. Authors of modules are automatically listed as Trainers of Pro modules, but any other user may also sign up to be a Trainer of a module. If there is more than one available Trainer to a specific module, the Learner may choose which Trainer they wish to be tutored by based on the Trainers' availabilities, experience in training the module or their overall rating. Trainers of Pro modules offer tutoring to Learners by answering their questions, explaining all or parts of the module using an instant messaging system and/or by using recorded voice messages.\",\n" +
                    "\t\"Slide 8\":\"Creating Modules: Any registered user may authorJSON learning modules and have them stored and readily available in the Module Library for other users to find and learn from. The application is intended to provide as much freedom to its trainers in compiling their learning modules as possible. Therefore, several of the most common teaching material design patterns are available for the trainers to choose from. However, for the scope of the current project the creation of modules is limited to text input only. The user may choose from several templates (eg. tables, fill-in-the-gaps, multiple-choice questions etc) to present the learning material. This is to be extended in the future with the possibility of including other media e.g. images, animations, audio and video.\",\n" +
                    "\t\"Slide 9\":\"Training: Trainers are expected to help Learners dependently of the module in question either if they run into any difficulties understanding the material or perhaps Learners need to be guided through the material step-by-step. The platform is not intended to restrict or regulate how the community interacts - it is the community itself that will determine design its learning modules, and thus how much work is required from Trainers. Module descriptions will provide a clear summary of the module content and the amount and type of work required for its completion. Trainers will be rated for their input, and so will Learners - if any user fails to provide adequate tutoring, or respect the rules of the community and/or accept the requirements of a certain learning module, this will reflect in the feedback and rating of the collaborating user. This is the means by which the community is able to regulate and improve its composition: by valuing users' interaction and patience, and praising hard-working contributors by advertising their skills and competence. As an addition to Trainers designing and compiling the learning materials, they also help their Learners in any way they require. This may mean answering to their questions they might have during the completion of the learning module and/or with any relevant exercise, but it is not limited to answering questions. Some modules will require the Trainer's active involvement in the learning process: e.g. in a language learning module the Trainer (likely native speaker of the Learner's target language) would need to provide additional support (i.e. pronunciation of words, audio conversation etc.).\",\n" +
                    "\t\"Slide 10\":\"Rating: All users (Learners and Trainers alike) rate each other based on their performance during or after they collaborated on a module. The rating system ensures that users' track record is visible to all future collaborators. As Learners choose their Trainers based on the Trainer's availability, proficiency, or ratings record, it is crucial for any Trainer to aim to maintain a generally positive rating overall. Likewise, Learners' input, willingness and respect of the general guidelines of the community will be rated by their respective Trainers, which may eventually negatively affect their perception within the community. Any collaboration on a module is concluded by the mutual rating of those involved: Learners rate & provide feedback on their Trainer's performance and vice versa. All user profiles display the latest feedback and ratings activity pertaining to the user in order to give a better understanding of what to expect from an unknown member of the community before accepting to work together. Ratings data and feedback stay on record as long as the user's account exists.\",\n" +
                    "\t\"Slide 11\":\"Development Plan & Expectations: The current paper intends to introduce the general idea of the project along with a list of prioritised critical objectives. The project will primarily focus on these components of the system and will conclude by presenting a list of proposed future extensions. The project is expected to complete the design and implementation of the core functions and logic of the system, and make space for the incorporation of a number of important extensions in the future. The following core elements below have been identified as those without which the fundamental functionality of the platform would not be achievable. The lack of any one of these would mean that the project fails to provide the services it seeks to.\",\n" +
                    "\t\"Slide 12\":\"Core elements: 1. User registration & profiles: Users must be able to register in order for the system to be able to store their personal information and learning data. The development of corresponding classes and functions, therefore, is of the highest priority. Upon launching the application, users are presented with the options to register using using their email addresses or Facebook accounts, or to login using existing account details.\",\n" +
                    "\t\"Slide 13\":\"Core elements: 2. Creating & publishing learning material: Users must be able to create their own modules in the public Module Library. The user community, and user involvement in designing the learning material are key defining factors that lead to the success of the system. The platform's modular quality is key to its versatility, and is therefore a high priority element on the list of core elements to implement.\",\n" +
                    "\t\"Slide 14\":\"Core elements: 3. Choosing & delivering learning material: Users must be able to take up and learn any module they can find in the Module Library. Module content must be made readily and easily accessible in order to ensure core functionality of the platform, i.e. to allow users to learn from other members of the community.\",\n" +
                    "\t\"Slide 15\":\"Core elements: 4. Instant messaging (IM): Users must be able to initiate communication with other members of the community, therefore, the instant messaging service must be implemented within the system from the beginning. Learners must be able to request help from their Trainers and receive tutoring real-time, using email to maintain support between users would be far less efficient in providing support to Learners. \",\n" +
                    "\t\"Slide 16\":\"Core elements: 5. Android Application: Users must be able to access their profiles, learning material, and communications channels which connect them with the rest of the community (whether the user is a Trainer or a Learner) in order to maintain an seamless and efficient service. The development of the application and the integration of the first 4 core logic elements are the final step of the development of the core functions of the platform.\",\n" +
                    "\t\"Slide 17\":[\"User Functions\",\"2 days\",\"Testing\",\"1 day\",\"Create Module\",\"7 days\",\"Testing\",\"1 day\",\"Learn Module\",\"7 days\",\"Testing\",\"1 day\",\"IM\",\"2 days\",\"Testing\",\"1 day\",\"Android application\",\"7 days\",\"UI\",\"1 day\"],\n" +
                    "\t\"Slide 18\":\"Future Extension: 1. Payments using PayPal direct debit, 2. User Rating & feedback, 3. Expand Functionalities within Module Creation (e.g. images, audio/video)\",\n" +
                    "\t\"No. of Slides\":18,\n" +
                    "\t\"ID\":000003\n" +
                    "}";
            JSONObject module3 = new JSONObject(moduleString3);
//            c.saveModuleInCloud(context, module3);

            FileOutputStream fou3 = context.openFileOutput("module000003", Context.MODE_PRIVATE);
            OutputStreamWriter osw3 = new OutputStreamWriter(fou3);
            osw3.write(module3.toString());
            osw3.flush();
            osw3.close();

            updateModuleRecords(context, module3);


            Toast.makeText(context, "3 Modules successfully added to Library", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
