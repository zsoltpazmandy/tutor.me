package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * The Learning tab contains a button linked to the Module Library, and a list of all the names of
 * modules the has enrolled on (and the ones they have already completed as well).
 * This list is suffixed by the rounded up % of the user's progress done in each module.
 *
 * Progress is defined by the ratio of slides contained within the module the user has already viewed
 * vs the the total number of slides contained in the module.
 *
 * An asynchronously executed task loads all the modules the user has enrolled on, and stores them
 * locally. This is so that the user can continue to view the module content even if network connection
 * is lost.
 */
public class LearningTab extends Fragment {

    private ArrayList<String> modsLearningNames;
    private ArrayList<String> modsLearningTotSlides;
    private ArrayList<String> modsLearningLastSlides;
    private Set<String> modIDsLearning;
    private ArrayList<HashMap<String, Object>> modules;
    private TextView learningView;
    private ListView learningList;
    private HashMap<String, Object> userMap;

    public LearningTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getActivity().getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            getActivity().finish();
            return;
        }

        userMap = (HashMap<String, Object>) getActivity().getIntent().getSerializableExtra("User");
        Cloud cloud = new Cloud();

        modsLearningNames = new ArrayList<>();
        modsLearningTotSlides = new ArrayList<>();
        modsLearningLastSlides = new ArrayList<>();
        modules = new ArrayList<>();
        modIDsLearning = new HashSet<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_learning_tab, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupElements();
        setUpProgress();
        AsyncGetMyModules getMyModules = new AsyncGetMyModules();
        getMyModules.execute();
    }

    private void setupElements() {
        learningView = (TextView) getActivity().findViewById(R.id.learning_tab_currently_learning_top);
        Button browseAllButt = (Button) getActivity().findViewById(R.id.viewLibButt);
        browseAllButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLibrary = new Intent(getActivity(), ViewLibrary.class);
                openLibrary.putExtra("User", userMap);
                startActivity(openLibrary);
                getActivity().finish();
            }
        });

        learningList = (ListView) getActivity().findViewById(R.id.learning_tab_currently_learning_list);
    }


    private void setUpProgress() {
        HashMap<String, String> userProgress = (HashMap<String, String>) userMap.get("progress");
        if (userProgress.size() == 1 && userProgress.containsKey("none")) {
            modsLearningNames.add("");
        } else {
            modIDsLearning = userProgress.keySet();
            for (String s : modIDsLearning) {
                // needs progress format as: ../progress/hashmaps of key: 000000, value: Name_X_Y
                //                                              X = total slides in module
                //                                              Y = latest slide the user has viewed
                modsLearningNames.add(userProgress.get(s).split("_")[0]);
                modsLearningTotSlides.add(userProgress.get(s).split("_")[1]);
                modsLearningLastSlides.add(userProgress.get(s).split("_")[2]);
            }
        }
    }


    private void setupLearningTab() {
        if (modsLearningNames.get(0).equals("")) {
            learningView.setText(R.string.no_module_taken_yet);
            return;
        } else {
            learningView.setText(R.string.i_m_currently_learning);
        }
        if (modsLearningNames.size() > 0) {
            ArrayList<String> learningDisplayArray = new ArrayList<>();
            for (int i = 0; i < modsLearningNames.size(); i++) {
                double progress = Double.valueOf(modsLearningLastSlides.get(i).replaceAll("\"", ""));
                double totalSlides = Double.valueOf(modsLearningTotSlides.get(i).replaceAll("\"", ""));
                double tempDouble = (progress / totalSlides) * 100;
                long percentCompleted = Math.round(tempDouble);
                learningDisplayArray.add(modsLearningNames.get(i) + "\n(" + percentCompleted + "%)");
            }
//            learningDisplayArray = showMostRecentFirst(learningDisplayArray);
            final ListAdapter currentModulesAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, learningDisplayArray);
            learningList.setAdapter(currentModulesAdapter);
            learningList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, Object> selectedModule = (HashMap<String, Object>) modules.get(position);
                    Intent openModule = new Intent(getActivity(), ModuleProgress.class);
                    openModule.putExtra("User", userMap);
                    openModule.putExtra("Module", selectedModule);
                    startActivity(openModule);
                    getActivity().finish();
                }
            });
        }
    }

    /**
     * showMostRecentFirst reorders the items on the list in a way that the module in which the
     * user has made the most progress is displayed first.
     * it currently does not work.
     */
    public ArrayList<String> showMostRecentFirst(ArrayList<String> moduleNames) {

        // order module names descending
        ArrayList<String> orderedDesc = new ArrayList<>();
        TreeSet<Integer> tempTree = new TreeSet<>();
        for (String s : moduleNames) {

            String temp = s.substring(s.length() - 5, s.length() - 2).replace("(", "").replace(")", "").replace("%", "").replace("\n", "");
            int i = Integer.parseInt(temp);
            tempTree.add(i);
        }
        NavigableSet<Integer> result = tempTree.descendingSet();
        for (int i : result) {
            for (int j = 0; j < moduleNames.size(); j++) {
                String temp2 = moduleNames.get(j).substring(moduleNames.get(j).length() - 5, moduleNames.get(j).length() - 2).replace("(", "").replace(")", "").replace("%", "").replace("\n", "");
                if (Integer.parseInt(temp2) == i) {
                    orderedDesc.add(moduleNames.get(j));
                }
            }
        }
        return orderedDesc;
    }

    private class AsyncGetMyModules extends AsyncTask<String, ArrayList<HashMap<String, Object>>, String> {

        @Override
        protected String doInBackground(String... uid) {

            final DatabaseReference modulesRoot = FirebaseDatabase.getInstance().getReference().child("/modules");
            modulesRoot.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (String s : modIDsLearning) {

                                HashMap<String, Object> modMap = new HashMap<>();
                                modMap.put("name", dataSnapshot.child(s).child("name").getValue().toString());
                                modMap.put("description", dataSnapshot.child(s).child("description").getValue().toString());
                                modMap.put("id", dataSnapshot.child(s).child("id").getValue().toString());
                                modMap.put("author", dataSnapshot.child(s).child("author").getValue().toString());
                                modMap.put("pro", dataSnapshot.child(s).child("pro").getValue().toString());
                                modMap.put("author", dataSnapshot.child(s).child("author").getValue().toString());
                                modMap.put("noOfSlides", dataSnapshot.child(s).child("noOfSlides").getValue().toString());
                                modMap.put("authorName", dataSnapshot.child(s).child("authorName").getValue().toString());
                                int amountOfSlides = Integer.parseInt(dataSnapshot.child(s).child("noOfSlides").getValue().toString());

                                for (int j = 1; j <= amountOfSlides; j++) {
                                    modMap.put("Slide_" + j, dataSnapshot.child(s).child("Slide_" + j).getValue().toString());
                                }

                                HashMap<String, String> reviewMap = (HashMap) dataSnapshot.child(s).child("reviews").getValue();
                                modMap.put("reviews", reviewMap);

                                HashMap<String, String> trainersMap = (HashMap) dataSnapshot.child(s).child("trainers").getValue();
                                modMap.put("trainers", trainersMap);

                                HashMap<String, String> typesMap = (HashMap) dataSnapshot.child(s).child("typesOfSlides").getValue();
                                modMap.put("typesOfSlides", typesMap);

                                modules.add(modMap);
                            }
                            publishProgress(modules);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    }
            );
            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<HashMap<String, Object>>... modulesArray) {
            super.onProgressUpdate(modulesArray);
            modules = modulesArray[0];
            setupLearningTab();
        }
    }
}
