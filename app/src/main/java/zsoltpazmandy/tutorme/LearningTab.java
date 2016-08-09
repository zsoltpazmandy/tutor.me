package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.NavigableSet;
import java.util.TreeSet;

public class LearningTab extends Fragment {

    private JSONObject user = new JSONObject();
    private User u;
    private Module f;
    private FirebaseAuth mAuth;
    private String uID;

    private ArrayList<String> getModsLearningIDs;
    private ArrayList<String> modsLearningNames;
    private ArrayList<String> modsLearningTotSlides;
    private ArrayList<String> modsLearningLastSlides;

    public LearningTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();

        u = new User(getActivity().getApplicationContext());
        f = new Module();

        if ((getActivity().getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            getActivity().finish();
            return;
        }

        modsLearningNames = new ArrayList<>();
        getModsLearningIDs = new ArrayList<>();
        modsLearningTotSlides = new ArrayList<>();
        modsLearningLastSlides = new ArrayList<>();

        String[] progressArray = new String[0];

        try {
            this.user = new JSONObject(getActivity().getIntent().getStringExtra("User"));
            progressArray = u.getStringFromJSON(getActivity().getApplicationContext(), user, "Progress");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // if there has been any registered progress
        if (!progressArray[0].equals("")) {

            // for every module's progress string
            for (String s : progressArray) {

                // 6-digit ID put in arraylist
                getModsLearningIDs.add(s.substring(0, 6));
                String rawProg = s.substring(6);

                if (rawProg.split("_").length == 3) { // name contains no underscores
                    modsLearningNames.add(rawProg.split("_")[0]);
                    modsLearningTotSlides.add(rawProg.split("_")[1]);
                    modsLearningLastSlides.add(rawProg.split("_")[2]);
                } else { // if name contains underscores

                    // the last index in the arraylist, where the name is rebuilt
                    int index = modsLearningNames.size();

                    // if this isn't the first entry in the arraylist, the index of insertion is the last position: size-1
                    if (modsLearningNames.size() != 0)
                        index--;

                    // index where the name ends, total & last slides begin
                    int indexOfTotal = rawProg.split("_").length - 1;
                    int indexOfLast = rawProg.split("_").length;

                    // rebuild name
                    for (int i = 0; i < rawProg.split("_").length - 2; i++) {
                        modsLearningNames.add(index, rawProg.split("_")[i]);
                    }

                    modsLearningTotSlides.add(rawProg.split("_")[indexOfTotal]);
                    modsLearningLastSlides.add(rawProg.split("_")[indexOfTotal + 1]);
                }
            }
        } else {
            modsLearningNames.add("");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_learning_tab, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupLearningTab();
    }

    private void setupLearningTab() {

        TextView learningView = (TextView) getActivity().findViewById(R.id.learning_tab_currently_learning_top);
//  v1
//        ArrayList<String> learningIDs = new ArrayList<>();
//
//        for (String s : u.getLearning(getActivity().getApplicationContext(), user)) {
//            learningIDs.add(s);
//        }
//
//        if (learningIDs.size() == 0) {
//            assert learningView != null;
//            learningView.setText(R.string.no_module_taken_yet);
//        } else {
//            assert learningView != null;
//            learningView.setText(R.string.i_m_currently_learning);
//
//            List<String> currentModules = u.getLearning(getActivity().getApplicationContext(), user);
//
//            ListView learningList = (ListView) getActivity().findViewById(R.id.learning_tab_currently_learning_list);
//
//            ArrayList<String> learningModules = new ArrayList<>();
//
//            for (String s : currentModules) {
//                try {

//                    double progress =
//                                  u.getLastSlideViewed(
//                                  getActivity().getApplicationContext(),
//                                  user,
//                                  Integer.parseInt(s.replace("#","").replace("\"","")));

//                    double totalSlides =
//                                  f.getSlideCount(getActivity().getApplicationContext(),
//                                  Integer.parseInt(s.replace("#","").replace("\"","")));

//                    double tempDouble =
//                                  (progress / totalSlides) * 100;

//                    long percentCompleted =
//                                  Math.round(tempDouble);

//                    learningModules.add(
//                                  f.getModuleByID(getActivity().getApplicationContext(),
//                       Integer.parseInt(s.replace("#","").replace("\"",""))).getString("Name") + "\n(" + percentCompleted + "%)");

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            learningModules = showMostRecentFirst(learningModules);
//
//            final ListAdapter currentModulesAdapter = new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_list_item_1, learningModules);
//
//            assert learningList != null;
//            learningList.setAdapter(currentModulesAdapter);
//
//            learningList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String selectedModule = String.valueOf(currentModulesAdapter.getItem(position));
//                    selectedModule = selectedModule.split("\n")[0]; // get rid of progress tag
//
//                    JSONObject module = null;
//
//                    try {
//                        module = f.getModuleByName(getActivity().getApplicationContext(), selectedModule);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    Intent openModule = new Intent(getActivity(), ModuleProgress.class);
//                    openModule.putExtra("User", user.toString());
//                    openModule.putExtra("Module", module.toString());
//                    startActivity(openModule);
//                    getActivity().finish();
//                }
//            });
//        }

        // setup display
        if (modsLearningNames.get(0).equals("null")) {
            learningView.setText(R.string.no_module_taken_yet);
            return;
        }
        learningView.setText(R.string.i_m_currently_learning);


        if (modsLearningNames.size() > 1) {

            // Strings to be displayed in the list (Names, progresses)
            ArrayList<String> learningDisplayArray = new ArrayList<>();

            for (int i = 0; i < modsLearningNames.size(); i++) {
                double progress = Double.valueOf(modsLearningLastSlides.get(i));
                double totalSlides = Double.valueOf(modsLearningTotSlides.get(i));
                double tempDouble = (progress / totalSlides) * 100;
                long percentCompleted = Math.round(tempDouble);
                learningDisplayArray.add(modsLearningNames.get(i) + "\n(" + percentCompleted + "%)");
            }

            // Order them Desc
            learningDisplayArray = showMostRecentFirst(learningDisplayArray);

            // Assemble the listview & the adapter
            ListView learningList = (ListView) getActivity().findViewById(R.id.learning_tab_currently_learning_list);
            final ListAdapter currentModulesAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, learningDisplayArray);
            learningList.setAdapter(currentModulesAdapter);

        }

        Button browseAllButt = (Button) getActivity().findViewById(R.id.viewLibButt);
        browseAllButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLibrary = new Intent(getActivity(), ViewLibrary.class);
                openLibrary.putExtra("User String", user.toString());
                startActivity(openLibrary);
                getActivity().finish();
            }
        });


    }

    public ArrayList<String> showMostRecentFirst(ArrayList<String> modules) {

        ArrayList<String> orderedDesc = new ArrayList<>();

        TreeSet<Integer> tempTree = new TreeSet<>();

        for (String s : modules) {

            String temp = s.substring(s.length() - 5, s.length() - 2).replace("(", "").replace(")", "").replace("%", "").replace("\n", "");
            int i = Integer.parseInt(temp);
            tempTree.add(i);
        }

        NavigableSet<Integer> result = tempTree.descendingSet();

        for (int i : result) {
            for (int j = 0; j < modules.size(); j++) {
                String temp2 = modules.get(j).substring(modules.get(j).length() - 5, modules.get(j).length() - 2).replace("(", "").replace(")", "").replace("%", "").replace("\n", "");
                if (Integer.parseInt(temp2) == i) {
                    orderedDesc.add(modules.get(j));
                }
            }
        }

        return orderedDesc;
    }
}
