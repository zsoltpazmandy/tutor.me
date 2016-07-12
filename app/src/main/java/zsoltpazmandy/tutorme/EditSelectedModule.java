package zsoltpazmandy.tutorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditSelectedModule extends AppCompatActivity {

    JSONObject user = null;
    JSONObject module = null;
    Module f = new Module();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_selected_module);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User u = new User(getApplicationContext());

        try {
            user = new JSONObject(getIntent().getStringExtra("User String"));
            module = new JSONObject(getIntent().getStringExtra("Module"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView topText = (TextView) findViewById(R.id.edit_selected_module_top_hint);
        TextView slideHint = (TextView) findViewById(R.id.edit_selected_module_slides_hint);
        final Button addSlideButt = (Button) findViewById(R.id.edit_selected_module_add_slide_butt);
        final Button editSlideButt = (Button) findViewById(R.id.edit_selected_module_edit_slides_butt);
        final Button moveSlideButt = (Button) findViewById(R.id.edit_selected_module_move_slides_butt);
        final Button deleteSlideButt = (Button) findViewById(R.id.edit_selected_module_del_slide_butt);
        ListView slidesOfModuleListView = (ListView) findViewById(R.id.edit_selected_module_slideslist_view);

        try {
            assert topText != null;
            topText.setText(module.getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert editSlideButt != null;
        editSlideButt.setEnabled(false);
        assert moveSlideButt != null;
        moveSlideButt.setEnabled(false);
        assert deleteSlideButt != null;
        deleteSlideButt.setEnabled(false);

        int noOfSlides = 0;
        try {
            noOfSlides = module.getInt("No. of Slides");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<String> slidesNames = new ArrayList<>();

        for (int i = 1; i <= noOfSlides; i++) {
            try {

                String tempString = "";
                String[] tempArray = new String[1];
                try {
                    tempString = module.getString("Types of Slides").replace("[", "").replace("]", "");
                    if (tempString.contains(",")) {
                        tempArray = tempString.split(",");
                    } else {
                        tempArray[0] = tempString;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (Integer.parseInt(tempArray[i - 1]) == 2) {

                    String tableRaw = "";
                    try {

                        tableRaw = module.getString("Slide " + i);
                        tableRaw = tableRaw.replace("[", "").replace("]", "").replace("\"", "");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String[] temp = new String[20];
                    temp = tableRaw.split(",");

                    String currentRow = "| ";

                    for (int j = 0; j < temp.length; j++) {
                        currentRow = currentRow + temp[j].replace("##comma##", ",") + " | " + temp[j + 1].replace("##comma##", ",") + " | ";
                        j++;
                    }


                    if (currentRow.length() >= 40) {
                        slidesNames.add(currentRow.substring(0, 40) + "...");
                    } else {
                        slidesNames.add(currentRow);
                    }


                } else {


                    if (module.getString("Slide " + i).length() >= 40) {
                        slidesNames.add(module.getString("Slide " + i).substring(0, 40) + "...");
                    } else {
                        slidesNames.add(module.getString("Slide " + i));
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final ArrayAdapter slidesNamesAdapter = new ArrayAdapter<String>(this,
                R.layout.slide_select_custom_listview_layout, R.id.list_item, slidesNames);

        assert slidesOfModuleListView != null;
        slidesOfModuleListView.setAdapter(slidesNamesAdapter);

        slidesOfModuleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                view.setSelected(true);

                addSlideButt.setEnabled(false);
                editSlideButt.setEnabled(true);
                editSlideButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // start editing selected slide
                        String tempString = "";
                        String[] tempArray = new String[1];
                        try {
                            tempString = module.getString("Types of Slides").replace("[", "").replace("]", "");
                            if (tempString.contains(",")) {
                                tempArray = tempString.split(",");
                            } else {
                                tempArray[0] = tempString;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int slideNumber = position + 1;

                        switch (Integer.parseInt(tempArray[position])) {
                            case 1:

                                Intent editTextSlide = new Intent(EditSelectedModule.this, MakeTextSlide.class);
                                editTextSlide.putExtra("Module frame ready", module.toString());
                                editTextSlide.putExtra("Slide to edit", "" + slideNumber);
                                startActivityForResult(editTextSlide, 1);

                                break;
                            case 2:

                                Intent editTableSlide = new Intent(EditSelectedModule.this, MakeTableSlide.class);
                                editTableSlide.putExtra("Module frame ready", module.toString());
                                editTableSlide.putExtra("Slide to edit", "" + slideNumber);
                                startActivityForResult(editTableSlide, 1);
                                break;
                        }


                        Toast.makeText(EditSelectedModule.this, "Editing slide type:" + tempArray[position], Toast.LENGTH_SHORT).show();
                    }
                });
                moveSlideButt.setEnabled(true);
                moveSlideButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EditSelectedModule.this, "Moving slide number" + position, Toast.LENGTH_SHORT).show();
                    }
                });
                deleteSlideButt.setEnabled(true);
                deleteSlideButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EditSelectedModule.this, "Deleting slide number" + position, Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });

        assert addSlideButt != null;
        addSlideButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start adding slide activity
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_CANCELED:
                Toast.makeText(EditSelectedModule.this, "Editing slide cancelled", Toast.LENGTH_SHORT).show();
                break;
            case RESULT_OK:
                Toast.makeText(EditSelectedModule.this, "Slide updated.", Toast.LENGTH_SHORT).show();

                try {
                    module = new JSONObject(data.getStringExtra("Module edited"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                f.updateModule(getApplicationContext(), module);
                Intent restartAct = new Intent(EditSelectedModule.this, EditSelectedModule.class);
                restartAct.putExtra("User String", user.toString());
                restartAct.putExtra("Module", module.toString());
                startActivityForResult(restartAct, 1);
                finish();
                break;
        }
    }
}
