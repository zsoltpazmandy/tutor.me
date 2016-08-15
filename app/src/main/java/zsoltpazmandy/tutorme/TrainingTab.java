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

public class TrainingTab extends Fragment {


    private HashMap<String, Object> userMap = null;
    private HashMap<String, Object> tuteeMap = null;
    private ListView trainingList = null;
    private TextView trainingTabTop = null;
    private ArrayList<HashMap<String, Object>> myTutees;
    private ArrayList<String> tuteesIDs;
    private ArrayList<String> trainingListContent = null;


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


    private void setUpTuteeList() {

        if (!tuteesIDs.get(0).equals("none")) {
            trainingTabTop.setText(R.string.training_tab_currently_tutoring_hint);
            trainingList = (ListView) getActivity().findViewById(R.id.training_tab_tutee_list);
            ListAdapter trainingListAdapter = new ArrayAdapter<String>(getActivity(),
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
                    getActivity().finish();
                }
            });

        }

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
        HashMap<String, String> trainingMap = (HashMap<String, String>) userMap.get("training");
        for (String id : trainingMap.keySet()) {
            tuteesIDs.add(trainingMap.get(id));
        }
        getMyTutees.execute();

    }

    public void setupTrainingTab() {
        {


            trainingTabTop = (TextView) getActivity().findViewById(R.id.training_tab_top);
            trainingTabTop.setText(R.string.training_tab_no_tutees_hint);
            setUpTuteeList();

            Button createButt = (Button) getActivity().findViewById(R.id.createModButt);
            assert createButt != null;
            Button editModButt = (Button) getActivity().findViewById(R.id.training_tab_edit_module_butt);
            assert editModButt != null;

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
        }

    }

    class AsyncGetMyTutees extends AsyncTask<String, ArrayList<HashMap<String, Object>>, String> {

        @Override
        protected String doInBackground(String... uid) {

            final DatabaseReference usersRoot = FirebaseDatabase.getInstance().getReference().child("/users");
            usersRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, Object> currentTutee = new HashMap<String, Object>();

                    for (String s : tuteesIDs) {
                        currentTutee = (HashMap) dataSnapshot.child(s).getValue();
                        trainingListContent.add(currentTutee.get("username").toString());
                        myTutees.add(currentTutee);
                    }
                    publishProgress(myTutees);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    publishProgress(myTutees);
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<HashMap<String, Object>>... tutees) {
            super.onProgressUpdate(tutees);
            myTutees = tutees[0];
            setupTrainingTab();
        }

    }
}
