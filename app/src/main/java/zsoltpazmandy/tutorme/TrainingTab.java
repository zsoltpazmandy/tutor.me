package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.HashMap;

public class TrainingTab extends Fragment {

    private User u;
    private Module f;
    private FirebaseAuth mAuth;
    private JSONObject user = new JSONObject();

    private HashMap<String, Object> userMap = null;

    public TrainingTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        if ((getActivity().getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            getActivity().finish();
            return;
        }

//        try {
//            this.user = new JSONObject(getActivity().getIntent().getStringExtra("User"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        userMap = (HashMap<String, Object>) getActivity().getIntent().getSerializableExtra("User");

        u = new User(getActivity().getApplicationContext());
        f = new Module();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_training_tab, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupTrainingTab();
    }

    public void setupTrainingTab() {
        {

            Button createButt = (Button) getActivity().findViewById(R.id.createModButt);
            assert createButt != null;
            Button editModButt = (Button) getActivity().findViewById(R.id.training_tab_edit_module_butt);
            assert editModButt != null;
            Button populateFake = (Button) getActivity().findViewById(R.id.training_tab_populate_library_butt);
            assert populateFake != null;

//            JSONObject user = new JSONObject();
//
//            try {
//                user = new JSONObject(getActivity().getIntent().getStringExtra("User"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

//            final JSONObject forwardUser = user;

            createButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createModule = new Intent(getActivity(), CreateModActivity.class);
                    createModule.putExtra("User", userMap);
                    startActivity(createModule);
                    getActivity().finish();
                }
            });

            // disable button if the user has not authored any modules
//            try {
//                if (user.getString("Authored").equals("")) {
//                    editModButt.setEnabled(false);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                editModButt.setEnabled(false);
//            }
            HashMap<String, Object> authoredMap = (HashMap<String, Object>) userMap.get("authored");
            if(authoredMap.size()==1&& authoredMap.containsKey("none")){
                editModButt.setEnabled(false);
            }



//            final JSONObject finalUser = user;
            editModButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent editModIntent = new Intent(getActivity(), EditModules.class);
                    editModIntent.putExtra("User", userMap);
                    startActivity(editModIntent);
                    getActivity().finish();
                }
            });

//            populateFake.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        f.populateLibrary(getActivity().getApplicationContext(), finalUser);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    JSONObject updatedUser = null;
//                    try {
//                        updatedUser = u.getUser(getActivity().getApplicationContext(), finalUser.getInt("ID"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Intent restart = new Intent(getActivity(), Home.class);
//                    restart.putExtra("User", updatedUser.toString());
//                    startActivity(restart);
//                    getActivity().finish();
//                }
//            });
        }

    }

}
