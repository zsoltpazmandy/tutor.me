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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * The Training tab contains buttons to start the following activities:
 *      1. Create Module
 *      2. Edit Modules (enabled if the user has already authored any modules)
 *
 * The Training tab also includes a list of users who are currently enrolled on a module the user
 * is tutoring.
 */
public class TrainingTab extends Fragment {

    private HashMap<String, Object> userMap;
    private HashMap<String, Object> tuteeMap;
    private TextView trainingTabTop;
    private ArrayList<HashMap<String, Object>> myTutees;
    private ArrayList<String> tuteesIDs;
    private ArrayList<String> trainingListContent;
    private boolean noTutees = true;

    public TrainingTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getActivity().getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            getActivity().finish();
            return;
        }
        myTutees = new ArrayList<>();
        userMap = (HashMap<String, Object>) getActivity().getIntent().getSerializableExtra("User");
        tuteeMap = new HashMap<>();
        tuteesIDs = new ArrayList<>();
        trainingListContent = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_training_tab, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AsyncGetMyTutees getMyTutees = new AsyncGetMyTutees();
        getMyTutees.execute();
    }

    private void setupTrainingTab() {
        trainingTabTop = (TextView) getActivity().findViewById(R.id.training_tab_top);
        trainingTabTop.setText(R.string.training_tab_no_tutees_hint);

        Button createButt = (Button) getActivity().findViewById(R.id.createModButt);
        Button editModButt = (Button) getActivity().findViewById(R.id.training_tab_edit_module_butt);

        createButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createModule = new Intent(getActivity(), CreateModActivity.class);
                createModule.putExtra("User", userMap);
                startActivity(createModule);
                getActivity().finish();
            }
        });
        HashMap<String, Object> authoredMap = (HashMap<String, Object>) userMap.get("authored");
        if (authoredMap.size() == 1 && authoredMap.containsKey("none")) {
            editModButt.setEnabled(false);
        }

        editModButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editModIntent = new Intent(getActivity(), EditModules.class);
                editModIntent.putExtra("User", userMap);
                startActivity(editModIntent);
                getActivity().finish();
            }
        });
        setUpTuteeList();
    }

    private void setUpTuteeList() {
        if (!noTutees) {
            for (int i = 0; i < tuteesIDs.size(); i++) {
                trainingListContent.clear();
                trainingListContent.add(myTutees.get(i).get("username").toString());
            }

            trainingTabTop.setText(R.string.training_tab_currently_tutoring_hint);
            ListView trainingList = (ListView) getActivity().findViewById(R.id.training_tab_tutee_list);
            ListAdapter trainingListAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, trainingListContent);
            trainingList.setAdapter(trainingListAdapter);

            trainingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    tuteeMap = myTutees.get(position);
                    Intent startChat = new Intent(getActivity(), Chat.class);
                    startChat.putExtra("User", userMap);
                    startChat.putExtra("TuteeMap", tuteeMap);
                    startActivity(startChat);
                }
            });
        }
    }

    private class AsyncGetMyTutees extends AsyncTask<String, ArrayList<HashMap<String, Object>>, String> {
        @Override
        protected String doInBackground(String... uid) {
            final DatabaseReference usersRoot = FirebaseDatabase.getInstance().getReference().child("/users");
            usersRoot.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            HashMap<String, String> trainingMap = (HashMap<String, String>) userMap.get("training");
                            for (String id : trainingMap.keySet()) {
                                if (!trainingMap.get(id).equals("true") && !trainingMap.get(id).equals("none")) {
                                    tuteesIDs.add(trainingMap.get(id));
                                }
                            }

                            HashMap<String, Object> currentTutee = new HashMap<>();
                            if (tuteesIDs.size() != 0)
                                for (String s : tuteesIDs) {
                                    currentTutee = (HashMap<String, Object>) dataSnapshot.child(s).getValue();
                                    myTutees.add(currentTutee);
                                    noTutees = false;
                                }
                            publishProgress();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity().getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<HashMap<String, Object>>... tutees) {
            super.onProgressUpdate(tutees);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getActivity().getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setupTrainingTab();
        }
    }
}
