package zsoltpazmandy.tutorme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;

        final TextView topText = (TextView) findViewById(R.id.edit_selected_module_top_hint);
        topText.setWidth((int) (screenWidth * 0.8));

        Button changeName = (Button) findViewById(R.id.edit_selected_change_module_name_butt);
        assert changeName != null;
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSelectedModule.this);
                alert.setTitle("Change the name of the module");
                String current = "";
                try {
                    current = module.getString("Name").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final EditText newName = new EditText(getApplicationContext());
                newName.setMaxLines(4);
                newName.setTextColor(Color.BLACK);
                newName.setText(current);
                alert.setView(newName);

                final String finalCurrent = current;
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {

                            if (newName.getText().toString().length() >= 100) {
                                Toast.makeText(EditSelectedModule.this, "Name must be max 100 characters!", Toast.LENGTH_SHORT).show();
                            }

                            if (newName.getText().toString().equals(finalCurrent)) {
                                Toast.makeText(EditSelectedModule.this, "Original name kept.", Toast.LENGTH_SHORT).show();
                            }

                            if (newName.getText().toString().length() == 0) {
                                Toast.makeText(EditSelectedModule.this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                            }

                            if (!f.isNameTaken(getApplicationContext(), newName.getText().toString())) {
                                module.remove("Name");
                                f.updateModule(getApplicationContext(), module.put("Name", newName.getText().toString()));
                                topText.setText(newName.getText().toString());

                            } else {
                                Toast.makeText(EditSelectedModule.this, "A module with that name already exists. Please choose a different name!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
        final TextView descText = (TextView) findViewById(R.id.edit_selected_module_desc);
        assert descText != null;
        descText.setWidth((int) (screenWidth * 0.8));

        Button editDescButt = (Button) findViewById(R.id.edit_selected_change_module_desc_butt);
        assert editDescButt != null;
        editDescButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSelectedModule.this);
                alert.setTitle("Edit description of the module");
                String current = "";
                try {
                    current = module.getString("Description").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final EditText newDesc = new EditText(getApplicationContext());
                newDesc.setMaxLines(4);
                newDesc.setTextColor(Color.BLACK);
                newDesc.setText(current);
                alert.setView(newDesc);

                final boolean[] failed = {false};

                final String finalCurrent = current;
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {

                            if (newDesc.getText().toString().length() >= 1000) {
                                failed[0] = true;
                                Toast.makeText(EditSelectedModule.this, "Description must be max 1000 characters!", Toast.LENGTH_SHORT).show();
                            }

                            if (newDesc.getText().toString().equals(finalCurrent)) {
                                failed[0] = true;
                                Toast.makeText(EditSelectedModule.this, "Original description kept.", Toast.LENGTH_SHORT).show();
                            }

                            if (newDesc.getText().toString().length() == 0) {
                                failed[0] = true;
                                Toast.makeText(EditSelectedModule.this, "Description cannot be empty!", Toast.LENGTH_SHORT).show();
                            }

                            if (!failed[0]) {
                                module.remove("Description");
                                f.updateModule(getApplicationContext(), module.put("Description", newDesc.getText().toString()));
                                descText.setText(newDesc.getText().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });


        TextView slideHint = (TextView) findViewById(R.id.edit_selected_module_slides_hint);
        final Button addSlideButt = (Button) findViewById(R.id.edit_selected_module_add_slide_butt);
        final Button editSlideButt = (Button) findViewById(R.id.edit_selected_module_edit_slides_butt);
        final Button moveSlideButt = (Button) findViewById(R.id.edit_selected_module_move_slides_butt);
        final Button deleteSlideButt = (Button) findViewById(R.id.edit_selected_module_del_slide_butt);
        final TextView selectedSlide = (TextView) findViewById(R.id.edit_selected_selected_slide_text);
        selectedSlide.setText("Select slide to edit");
        final ListView slidesOfModuleListView = (ListView) findViewById(R.id.edit_selected_module_slideslist_view);
        Button doneButt = (Button) findViewById(R.id.edit_selected_save_butt);
        assert doneButt != null;
        doneButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_OK);
                finish();
            }
        });

        Button discardButt = (Button) findViewById(R.id.edit_selected_discard_butt);


        try {
            assert topText != null;
            if (module.getString("Name").length() >= 57) {
                topText.setText(module.getString("Name") + "...");
            } else {
                topText.setText(module.getString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            assert descText != null;
            if (module.getString("Description").length() >= 77) {
                descText.setText(module.getString("Description").substring(0, 77) + "...");
            } else {
                descText.setText(module.getString("Description"));
            }
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

        final ArrayAdapter slidesNamesAdapter = new SlideArrayAdapter(this, slidesNames);

        assert slidesOfModuleListView != null;
        slidesOfModuleListView.setAdapter(slidesNamesAdapter);

        slidesOfModuleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

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

                final int slideNumber = position + 1;

                String[] type = {"Text Slide", "Table Slide"};

                selectedSlide.setText("Selected slide " + slideNumber + ": " + type[Integer.parseInt(tempArray[position])-1]);

                final String[] finalTempArray = tempArray;

                addSlideButt.setEnabled(false);
                editSlideButt.setEnabled(true);
                editSlideButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // start editing selected slide


                        switch (Integer.parseInt(finalTempArray[position])) {
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


                        Toast.makeText(EditSelectedModule.this, "Editing slide type:" + finalTempArray[position], Toast.LENGTH_SHORT).show();
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

    public void openDialog(final TextView nameView, final String currentName) {

    }
}
