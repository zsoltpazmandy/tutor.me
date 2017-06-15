package zsoltpazmandy.tutorme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 * <p>
 * The activity is launched from the EditModules activity when the user has selected which module
 * they wish to edit. All information of the module can be edited (its name, description, module
 * content).
 * <p>
 * The author has the option to add new slides to the existing module, delete ones already there,
 * or edit their content. The slide to be edited is chosen by long-pressing on its name displayed
 * in the lower half of the screen, which action triggers the enabling of the Edit, Move and Delete
 * buttons. Adding slides is only possible if no slide is selected. The user may de-select a slide
 * by a single short-press anywere else on the list.
 */
public class EditSelectedModule extends AppCompatActivity {

    private final int CREATE_MODULE_ADD_SLIDE = 1;
    private final int EDIT_MODULE_ADD_SLIDE = 2;
    private final int CREATE_MODULE_ADD_TEXT_SLIDE = 3;
    private final int CREATE_MODULE_ADD_TABLE_SLIDE = 4;
    private final int EDIT_MODULE_ADD_TEXT_SLIDE = 5;
    private final int EDIT_MODULE_ADD_TABLE_SLIDE = 6;
    private final int EDIT_MODULE_EDIT_TEXT_SLIDE = 7;
    private final int EDIT_MODULE_EDIT_TABLE_SLIDE = 8;

    private final Module f = new Module();
    private final Cloud c = new Cloud();

    private HashMap<String, Object> userMap;
    private HashMap<String, Object> moduleMap;
    private HashMap<String, Object> allModNames;
    private ArrayList<String> allModNamesList;

    private TextView moduleNameTextView;
    private Button editNameButt;
    private TextView descTextView;
    private Button editDescButt;
    private Button addSlideButt;
    private Button editSlideButt;
    private Button moveSlideButt;
    private Button deleteSlideButt;
    private ListView slidesOfModuleListView;

    private ArrayList<String> slidesNames;
    private HashMap<String, String> typesMap;
    private int slideNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_selected_module);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        if (getIntent().hasExtra("All Module Names")) {
            allModNamesList = (ArrayList<String>) getIntent().getSerializableExtra("All Module Names");
        }

        TextView moduleNameLabel = (TextView) findViewById(R.id.edit_selected_name_label);
        moduleNameTextView = (TextView) findViewById(R.id.edit_selected_module_top_hint);
        editNameButt = (Button) findViewById(R.id.edit_selected_change_module_name_butt);
        TextView descLabel = (TextView) findViewById(R.id.edit_selected_desc_label);
        descTextView = (TextView) findViewById(R.id.edit_selected_module_desc);
        editDescButt = (Button) findViewById(R.id.edit_selected_change_module_desc_butt);
        TextView slidesLabel = (TextView) findViewById(R.id.edit_selected_slide_label);
        addSlideButt = (Button) findViewById(R.id.edit_selected_module_add_slide_butt);
        editSlideButt = (Button) findViewById(R.id.edit_selected_module_edit_slides_butt);
        moveSlideButt = (Button) findViewById(R.id.edit_selected_module_move_slides_butt);
        deleteSlideButt = (Button) findViewById(R.id.edit_selected_module_del_slide_butt);
        slidesOfModuleListView = (ListView) findViewById(R.id.edit_selected_module_slideslist_view);
        Button saveButt = (Button) findViewById(R.id.edit_selected_save_butt);

        addEditNameButtListener();
        addEditDescButtListener();

        slidesOfModuleListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        saveButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(EditSelectedModule.this, Home.class);
                backHome.putExtra("User", userMap);
                startActivity(backHome);
                finish();
            }
        });

        trimNameAndDesc();
        prepareButtons();
        buildSlidesList();
        addListViewListener();

        slidesOfModuleListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                        if (!view.isSelected()) {
                            view.setSelected(true);

                            typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");
                            slideNumber = position + 1;
                            addSlideButt.setEnabled(false);
                            editSlideButt.setEnabled(true);
                            addEditSlideButtListener(position);

                            if (Integer.parseInt(moduleMap.get("noOfSlides").toString()) <= 1) {
                                moveSlideButt.setEnabled(false);
                            } else {
                                moveSlideButt.setEnabled(true);
                                addMoveSlideButtListener(position);
                            }
                            if (Integer.parseInt(moduleMap.get("noOfSlides").toString()) <= 1) {
                                deleteSlideButt.setEnabled(false);
                            } else {
                                deleteSlideButt.setEnabled(true);
                                addDeleteSlideButtListener(position);
                            }
                        }
                        return true;
                    }
                }
        );

        addSlideButt.setOnClickListener(
                new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        Intent addNewSlide = new Intent(EditSelectedModule.this, AddSlide.class);
                        addNewSlide.putExtra("Module", moduleMap);
                        addNewSlide.putExtra("User", userMap);
                        addNewSlide.putExtra("Adding Slide To Existing Module", true);
                        startActivityForResult(addNewSlide, EDIT_MODULE_ADD_SLIDE);
                    }
                }
        );
    }

    private void addDeleteSlideButtListener(final int position) {
        deleteSlideButt.setOnClickListener(
                new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        final int filenameNumber = position + 1;

                        AlertDialog.Builder alert = new AlertDialog.Builder(EditSelectedModule.this);
                        alert.setTitle("Are you sure you want to delete Slide " + filenameNumber);
                        alert.setPositiveButton("Yes, delete", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // f.removeSlide(getApplicationContext(), module, position);
                                        moduleMap = f.removeSlide(moduleMap, position);
                                        c.overWriteModuleHashMapInCloud(moduleMap);
                                        Intent restartAct = new Intent(EditSelectedModule.this, EditSelectedModule.class);
                                        restartAct.putExtra("User", userMap);
                                        restartAct.putExtra("Module", moduleMap);
                                        startActivity(restartAct);
                                        dialog.dismiss();
                                        finish();
                                    }
                                }
                        );

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener()

                                {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                        alert.show();
                    }
                }
        );
    }

    private void addMoveSlideButtListener(final int position) {
        moveSlideButt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int number = position + 1;
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditSelectedModule.this);
                        builder.setTitle("Move Slide " + number + " in the place of...");
                        String[] slides = new String[slidesNames.size()];

                        for (int i = 0; i < slidesNames.size(); i++) {
                            slides[i] = slidesNames.get(i);
                        }

                        builder.setItems(slides,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int index) {
                                /*
                                The following algorithm is used to reorder the slides in the module.
                                */
                                        //Add all slide strings & their respective slide types
                                        //to 2 ArrayLists
                                        ArrayList<String> allSlidesString = new ArrayList<>();
                                        ArrayList<Integer> newTypesArray = new ArrayList<>();

                                        // ID of the module
                                        String id = moduleMap.get("id").toString();

                                        // Amount of slides in the module
                                        int count = Integer.parseInt(moduleMap.get("noOfSlides").toString());

                                        // Slides copied to the ArrayList
                                        for (int i = 1; i <= count; i++) {
                                            allSlidesString.add(moduleMap.get("Slide_" + i).toString());
                                        }

                                        // Retrieving slide type information
                                        HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");

                                        // Copying slide types in the ArrayList
                                        for (int i = 1; i <= count; i++) {
                                            newTypesArray.add(Integer.parseInt(typesMap.get("Slide_" + i)));
                                        }


                                        // Store the String we want to move and its type separately
                                        String tempContent = allSlidesString.get(position);
                                        int tempType = newTypesArray.get(position);

                                        // Remove that element and its type from both ArrayLists
                                        allSlidesString.remove(position);
                                        newTypesArray.remove(position);

                                        if (index == allSlidesString.size()) {
                                            allSlidesString.add("");
                                            newTypesArray.add(0);
                                        }

                                        // Store the String which is in the position we're moving the
                                        // Slide to and its respective slide type
                                        String temp = allSlidesString.get(index);
                                        int typeOfSlideAtTarget = newTypesArray.get(index);

                                        // Create 2 new temporary ArrayLists where up to the target index
                                        // everything is as in the original list
                                        ArrayList<String> tempList = new ArrayList<>();
                                        ArrayList<Integer> tempTypeList = new ArrayList<>();
                                        for (int i = 0; i < index; i++) {
                                            tempList.add(allSlidesString.get(i));
                                            tempTypeList.add(newTypesArray.get(i));
                                        }

                                        // Add the slide String we're moving after the first part in the temp list
                                        // along with the slide's type
                                        tempList.add(tempContent);
                                        tempTypeList.add(tempType);

                                        // Add the slide String which was originally at the position we're
                                        // moving to along with its type
                                        tempList.add(temp);
                                        tempTypeList.add(typeOfSlideAtTarget);

                                        // Copy the rest of the elements from the original list
                                        // to the temporary list after the position we've just
                                        // inserted the new element and the original element at that position
                                        // along with all the slides' respective types
                                        for (int i = index + 1; i < allSlidesString.size(); i++) {
                                            tempList.add(allSlidesString.get(i));
                                            tempTypeList.add(newTypesArray.get(i));
                                        }

                                        // Remove old Slide Type information
                                        moduleMap.remove("typesOfSlides");

                                        HashMap<String, String> newTypesMap = new HashMap<>();

                                        // Insert new Slide data & Slide Type information in Module
                                        for (int i = 0; i < tempList.size(); i++) {
                                            int no = i + 1;
                                            moduleMap.remove("Slide_" + no);
                                            moduleMap.put("Slide_" + no, tempList.get(i));
                                            if (tempTypeList.get(i) != 0)
                                                newTypesMap.put("Slide_" + no, tempTypeList.get(i).toString());
                                        }

                                        moduleMap.remove("typesOfSlides");
                                        moduleMap.put("typesOfSlides", newTypesMap);

                                        // Update module in the database
                                        c.overWriteModuleHashMapInCloud(moduleMap);

                                        Intent restartAct = new Intent(EditSelectedModule.this, EditSelectedModule.class);
                                        restartAct.putExtra("User", userMap);
                                        restartAct.putExtra("Module", moduleMap);
                                        startActivity(restartAct);
                                        dialog.dismiss();
                                        finish();
                                    }
                                }
                        );
                        builder.show();
                    }
                }
        );
    }

    private void addEditSlideButtListener(final int position) {
        editSlideButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int posOffset = position + 1;

                switch (Integer.parseInt(typesMap.get("Slide_" + posOffset))) {
                    case 1:
                        Intent editTextSlide = new Intent(EditSelectedModule.this, MakeTextSlide.class);
                        editTextSlide.putExtra("Module", moduleMap);
                        editTextSlide.putExtra("User", userMap);
                        editTextSlide.putExtra("Slide To Edit", "" + posOffset);
                        startActivityForResult(editTextSlide, EDIT_MODULE_EDIT_TEXT_SLIDE);
                        break;
                    case 2:
                        Intent editTableSlide = new Intent(EditSelectedModule.this, MakeTableSlide.class);
                        editTableSlide.putExtra("Module", moduleMap);
                        editTableSlide.putExtra("User", userMap);
                        editTableSlide.putExtra("Slide To Edit", "" + posOffset);
                        startActivityForResult(editTableSlide, EDIT_MODULE_EDIT_TABLE_SLIDE);
                        break;
                }

            }
        });
    }

    private void addListViewListener() {
        slidesOfModuleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(false);
                addSlideButt.setEnabled(true);
                editSlideButt.setEnabled(false);
                moveSlideButt.setEnabled(false);
                deleteSlideButt.setEnabled(false);
            }
        });
    }

    private void buildSlidesList() {
        int noOfSlides = Integer.parseInt(moduleMap.get("noOfSlides").toString());

        slidesNames = new ArrayList<>();

        for (int i = 1; i <= noOfSlides; i++) {

            HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");

            if (Integer.parseInt(typesMap.get("Slide_" + i)) == 2) {
                String tableRaw = "";
                tableRaw = moduleMap.get("Slide_" + i).toString();
                tableRaw = tableRaw.replace("[", "").replace("]", "").replace("\"", "");
                String[] temp;
                temp = tableRaw.split(",");
                String currentRow = "| ";
                for (int j = 0; j < temp.length; j++) {
                    currentRow = currentRow + temp[j].replace("##comma##", ",") + " | " + temp[j + 1].replace("##comma##", ",") + " | ";
                    j++;
                }

                if (currentRow.length() >= 80) {
                    slidesNames.add("Slide " + i + ": " + currentRow.substring(0, 80) + "...");
                } else {
                    slidesNames.add("Slide " + i + ": " + currentRow);
                }

            } else {

                if (moduleMap.get("Slide_" + i).toString().length() >= 80) {
                    slidesNames.add("Slide " + i + ": " + moduleMap.get("Slide_" + i).toString().substring(0, 80) + "...");
                } else {
                    slidesNames.add("Slide " + i + ": " + moduleMap.get("Slide_" + i).toString());
                }
            }
        }

        final ArrayAdapter slidesNamesAdapter = new SlideArrayAdapter(this, slidesNames);
        slidesOfModuleListView.setAdapter(slidesNamesAdapter);
    }

    private void prepareButtons() {
        editSlideButt.setEnabled(false);
        moveSlideButt.setEnabled(false);
        deleteSlideButt.setEnabled(false);
    }

    private void trimNameAndDesc() {
        if (moduleMap.get("name").toString().length() >= 77) {
            moduleNameTextView.setText(String.format("%s...", moduleMap.get("name").toString()));
        } else {
            moduleNameTextView.setText(moduleMap.get("name").toString());
        }

        if (moduleMap.get("description").toString().length() >= 97) {
            descTextView.setText(String.format("%s...", moduleMap.get("description").toString().substring(0, 97)));
        } else {
            descTextView.setText(moduleMap.get("description").toString());
        }
    }

    private void addEditDescButtListener() {
        editDescButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSelectedModule.this);
                alert.setTitle("Edit description of the module");
                String current = moduleMap.get("description").toString();
                final EditText newDesc = new EditText(getApplicationContext());
                newDesc.setMaxLines(4);
                newDesc.setTextColor(Color.BLACK);
                newDesc.setText(current);
                alert.setView(newDesc);

                final boolean[] failed = {false};

                final String finalCurrent = current;
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

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
                            moduleMap.remove("description");
                            moduleMap.put("description", newDesc.getText().toString());
                            c.saveModuleHashMapInCloud(moduleMap);
                            descTextView.setText(newDesc.getText().toString());
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
    }

    private void addEditNameButtListener() {
        editNameButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSelectedModule.this);
                alert.setTitle("Change the name of the module");
                String current = moduleMap.get("name").toString();

                final EditText newName = new EditText(getApplicationContext());
                newName.setMaxLines(4);
                newName.setTextColor(Color.BLACK);
                newName.setText(current);
                alert.setView(newName);

                final String finalCurrent = current;
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (newName.getText().toString().length() >= 100) {
                            Toast.makeText(EditSelectedModule.this, "Name must be max 100 characters!", Toast.LENGTH_SHORT).show();
                        }

                        if (newName.getText().toString().equals(finalCurrent)) {
                            Toast.makeText(EditSelectedModule.this, "Original name kept.", Toast.LENGTH_SHORT).show();
                        }

                        if (newName.getText().toString().length() == 0) {
                            Toast.makeText(EditSelectedModule.this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                        }

                        if (!isNameTaken(newName.getText().toString().trim())) {
                            moduleMap.remove("name");
                            moduleMap.put("name", newName.getText().toString().trim());
                            c.saveModuleHashMapInCloud(moduleMap);
                            moduleNameTextView.setText(newName.getText().toString());
                        } else {
                            Toast.makeText(EditSelectedModule.this, "A module with that name already exists. Please choose a different name!", Toast.LENGTH_SHORT).show();
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
    }

    private boolean isNameTaken(String moduleName) {
        for (String s : allModNamesList) {
            if (s.toLowerCase().trim().equals(moduleName.toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(EditSelectedModule.this, "Cancelled", Toast.LENGTH_SHORT).show();
            return;
        }

        moduleMap = (HashMap<String, Object>) data.getSerializableExtra("Module");
        userMap = (HashMap<String, Object>) data.getSerializableExtra("User");

        if (requestCode == EDIT_MODULE_ADD_SLIDE) {
            if (resultCode == RESULT_OK) {
                Intent restartAct = new Intent(EditSelectedModule.this, EditSelectedModule.class);
                restartAct.putExtra("User", userMap);
                restartAct.putExtra("Module", moduleMap);
                c.overWriteModuleHashMapInCloud(moduleMap);
                startActivity(restartAct);
                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Adding slides to module cancelled.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == EDIT_MODULE_EDIT_TEXT_SLIDE) {
            if (resultCode == RESULT_OK) {
                Intent restartAct = new Intent(EditSelectedModule.this, EditSelectedModule.class);
                restartAct.putExtra("User", userMap);
                restartAct.putExtra("Module", moduleMap);
                c.overWriteModuleHashMapInCloud(moduleMap);
                startActivity(restartAct);
                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Editing slide cancelled.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == EDIT_MODULE_EDIT_TABLE_SLIDE) {
            if (resultCode == RESULT_OK) {
                Intent restartAct = new Intent(EditSelectedModule.this, EditSelectedModule.class);
                restartAct.putExtra("User", userMap);
                restartAct.putExtra("Module", moduleMap);
                c.overWriteModuleHashMapInCloud(moduleMap);
                startActivity(restartAct);
                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Editing slide cancelled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * In order to ensure the user doesn't accidentally leave the activity, they are prompted to
     * repeat the BackPress action within 1 second.
     */
    private boolean wantsToQuit = false;

    @Override
    public void onBackPressed() {
        if (wantsToQuit) {

            Intent backHome = new Intent(EditSelectedModule.this, Home.class);
            backHome.putExtra("User", userMap);
            startActivity(backHome);
            finish();
            return;
        }

        this.wantsToQuit = true;
        Toast.makeText(this, "Press 'Back' once more to quit editing this module.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wantsToQuit = false;
            }
        }, 1000);
    }

}
