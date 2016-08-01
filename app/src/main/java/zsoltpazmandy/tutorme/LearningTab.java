package zsoltpazmandy.tutorme;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class LearningTab extends Fragment {

    private JSONObject user = new JSONObject();
    private User u;
    private Module f;
    private FirebaseAuth mAuth;

    public LearningTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        if ((getActivity().getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            getActivity().finish();
            return;
        }

        try {
            this.user = new JSONObject(getActivity().getIntent().getStringExtra("User"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        u = new User(getActivity().getApplicationContext());
        f = new Module();

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

        ArrayList<String> learningIDs = new ArrayList<>();

        for (String s : u.getLearning(getActivity().getApplicationContext(), user)) {
            learningIDs.add(s);
        }

        if (learningIDs.size() == 0) {
            assert learningView != null;
            learningView.setText(R.string.no_module_taken_yet);
        } else {
            assert learningView != null;
            learningView.setText(R.string.i_m_currently_learning);

            List<String> currentModules = u.getLearning(getActivity().getApplicationContext(), user);

            ListView learningList = (ListView) getActivity().findViewById(R.id.learning_tab_currently_learning_list);

            ArrayList<String> learningModules = new ArrayList<>();

            for (String s : currentModules) {
                try {
                    double progress = u.getLastSlideViewed(getActivity().getApplicationContext(), user, Integer.parseInt(s.replace("#","").replace("\"","")));
                    double totalSlides = f.getSlideCount(getActivity().getApplicationContext(), Integer.parseInt(s.replace("#","").replace("\"","")));
                    double tempDouble = (progress / totalSlides) * 100;
                    long percentCompleted = Math.round(tempDouble);
                    learningModules.add(f.getModuleByID(getActivity().getApplicationContext(), Integer.parseInt(s.replace("#","").replace("\"",""))).getString("Name") + "\n(" + percentCompleted + "%)");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            learningModules = showMostRecentFirst(learningModules);

            final ListAdapter currentModulesAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, learningModules);

            assert learningList != null;
            learningList.setAdapter(currentModulesAdapter);

            learningList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedModule = String.valueOf(currentModulesAdapter.getItem(position));
                    selectedModule = selectedModule.split("\n")[0]; // get rid of progress tag

                    JSONObject module = null;

                    try {
                        module = f.getModuleByName(getActivity().getApplicationContext(), selectedModule);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent openModule = new Intent(getActivity(), ModuleProgress.class);
                    openModule.putExtra("User", user.toString());
                    openModule.putExtra("Module", module.toString());
                    startActivity(openModule);
                    getActivity().finish();
                }
            });
        }

        Button browseAllButt = (Button) getActivity().findViewById(R.id.viewLibButt);
        assert browseAllButt != null;
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
