package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;

public class TrainingTab extends Fragment {


    private HashMap<String, Object> userMap = null;

    public TrainingTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getActivity().getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            getActivity().finish();
            return;
        }
        userMap = (HashMap<String, Object>) getActivity().getIntent().getSerializableExtra("User");
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
            if(authoredMap.size()==1&& authoredMap.containsKey("none")){
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

}
