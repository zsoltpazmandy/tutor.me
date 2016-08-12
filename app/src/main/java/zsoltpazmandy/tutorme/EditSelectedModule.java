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

public class EditSelectedModule extends AppCompatActivity {

    Module f = new Module();
    Cloud c = new Cloud();

    private HashMap<String, Object> userMap = null;
    private HashMap<String, Object> moduleMap = null;
    private HashMap<String, Object> allModNames = null;
    private ArrayList<String> allModNamesList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_selected_module);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        try {
//            user = new JSONObject(getIntent().getStringExtra("User String"));
//            module = new JSONObject(getIntent().getStringExtra("Module"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        userMap = (HashMap<String, Object>) getIntent().getSerializableExtra("User");
        moduleMap = (HashMap<String, Object>) getIntent().getSerializableExtra("Module");
        if(getIntent().hasExtra("All Module Names")) {
            allModNamesList = (ArrayList<String>) getIntent().getSerializableExtra("All Module Names");
        }


        final TextView moduleNameLabel = (TextView) findViewById(R.id.edit_selected_name_label);
        assert moduleNameLabel != null;

        final TextView moduleNameTextView = (TextView) findViewById(R.id.edit_selected_module_top_hint);
        assert moduleNameTextView != null;

        Button editNameButt = (Button) findViewById(R.id.edit_selected_change_module_name_butt);
        assert editNameButt != null;

        TextView descLabel = (TextView) findViewById(R.id.edit_selected_desc_label);
        assert descLabel != null;

        final TextView descTextView = (TextView) findViewById(R.id.edit_selected_module_desc);
        assert descTextView != null;

        Button editDescButt = (Button) findViewById(R.id.edit_selected_change_module_desc_butt);
        assert editDescButt != null;

        TextView slidesLabel = (TextView) findViewById(R.id.edit_selected_slide_label);
        assert slidesLabel != null;

        final Button addSlideButt = (Button) findViewById(R.id.edit_selected_module_add_slide_butt);
        assert addSlideButt != null;

        final Button editSlideButt = (Button) findViewById(R.id.edit_selected_module_edit_slides_butt);
        assert editSlideButt != null;

        final Button moveSlideButt = (Button) findViewById(R.id.edit_selected_module_move_slides_butt);
        assert moveSlideButt != null;

        final Button deleteSlideButt = (Button) findViewById(R.id.edit_selected_module_del_slide_butt);
        assert deleteSlideButt != null;

        ListView slidesOfModuleListView = (ListView) findViewById(R.id.edit_selected_module_slideslist_view);
        assert slidesOfModuleListView != null;

        Button saveButt = (Button) findViewById(R.id.edit_selected_save_butt);
        assert saveButt != null;

        editNameButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSelectedModule.this);
                alert.setTitle("Change the name of the module");
                String current = "";
//                try {
//                    current = module.getString("Name");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                current = moduleMap.get("name").toString();

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
                            c.overWriteModuleHashMapInCloud(moduleMap);

//                                f.updateModule(getApplicationContext(), module.put("Name", newName.getText().toString().trim()));

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

        editDescButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSelectedModule.this);
                alert.setTitle("Edit description of the module");
                String current = "";
//                try {
//                    current = module.getString("Description");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                current = moduleMap.get("description").toString();
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
//                                module.remove("Description");
                            moduleMap.remove("description");
//                                f.updateModule(getApplicationContext(), module.put("Description", newDesc.getText().toString()));
                            moduleMap.put("description", newDesc.getText().toString());
                            c.overWriteModuleHashMapInCloud(moduleMap);
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

        editSlideButt.setEnabled(false);
        moveSlideButt.setEnabled(false);
        deleteSlideButt.setEnabled(false);

        int noOfSlides = Integer.parseInt(moduleMap.get("noOfSlides").toString());

        final ArrayList<String> slidesNames = new ArrayList<>();

        for (int i = 1; i <= noOfSlides; i++) {
            //
//                String tempString;
//                String[] tempArray = new String[1];
//                try {
//                    tempString = module.getString("Types of Slides").replace("[", "").replace("]", "");
//                    if (tempString.contains(",")) {
//                        tempArray = tempString.split(",");
//                    } else {
//                        tempArray[0] = tempString;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            System.out.println(moduleMap.toString());
            HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");

            if (Integer.parseInt(typesMap.get(""+i)) == 2) {

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


        slidesOfModuleListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                        if (!view.isSelected()) {
                            view.setSelected(true);

//                            String tempString;
//                            String[] tempArray = new String[1];
//                            try {
//                                tempString = module.getString("Types of Slides").replace("[", "").replace("]", "");
//                                if (tempString.contains(",")) {
//                                    tempArray = tempString.split(",");
//                                } else {
//                                    tempArray[0] = tempString;
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }

                            final HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");


                            final int slideNumber = position + 1;

//                            final String[] finalTempArray = tempArray;

                            addSlideButt.setEnabled(false);
                            editSlideButt.setEnabled(true);
                            editSlideButt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // start editing selected slide

                                    int posOffset = position + 1;


                                    switch (Integer.parseInt(typesMap.get("" + posOffset))) {
                                        case 1:

                                            Intent editTextSlide = new Intent(EditSelectedModule.this, MakeTextSlide.class);
                                            editTextSlide.putExtra("Module frame ready", moduleMap);
                                            editTextSlide.putExtra("Slide to edit", "" + slideNumber);
                                            startActivityForResult(editTextSlide, 1);

                                            break;
                                        case 2:

                                            Intent editTableSlide = new Intent(EditSelectedModule.this, MakeTableSlide.class);
                                            editTableSlide.putExtra("Module frame ready", moduleMap);
                                            editTableSlide.putExtra("Slide to edit", "" + slideNumber);
                                            startActivityForResult(editTableSlide, 1);
                                            break;
                                    }

                                }
                            });
                            moveSlideButt.setEnabled(true);
                            moveSlideButt.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final int number = position + 1;
                                            AlertDialog.Builder b = new AlertDialog.Builder(EditSelectedModule.this);
                                            b.setTitle("Move Slide " + number + " in the place of...");
                                            String[] slides = new String[slidesNames.size()];

                                            for (int i = 0; i < slidesNames.size(); i++) {
                                                slides[i] = slidesNames.get(i);
                                            }

                                            b.setItems(slides,
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

//                                                            String tempString;
//                                                            String[] tempArray = new String[1];
//                                                            try {
//                                                                tempString = module.getString("Types of Slides").replace("[", "").replace("]", "");
//                                                                if (tempString.contains(",")) {
//                                                                    tempArray = tempString.split(",");
//                                                                } else {
//                                                                    tempArray[0] = tempString;
//                                                                }
//                                                            } catch (JSONException e) {
//                                                                e.printStackTrace();
//                                                            }

                                                            // Copying slide types in the ArrayList
                                                            for (int i = 1; i <= count; i++) {
                                                                newTypesArray.add(Integer.parseInt(typesMap.get("" + i)));
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

                                                            HashMap<String, String> newTypesMap = new HashMap<String, String>();

                                                            // Insert new Slide data & Slide Type information in Module
                                                            for (int i = 0; i < tempList.size(); i++) {
                                                                int no = i + 1;
                                                                moduleMap.remove("Slide_" + no);
                                                                moduleMap.put("Slide_" + no, tempList.get(i));
//                                                                    module.put("Slide " + no, tempList.get(i));
                                                                if (tempTypeList.get(i) != 0)
                                                                    newTypesMap.put("" + no, tempTypeList.get(i).toString());
//                                                                        module.accumulate("Types of Slides", tempTypeList.get(i));
                                                            }

                                                            moduleMap.remove("typesOfSlides");
                                                            moduleMap.put("typesOfSlides", newTypesMap);


                                                            // Update module in the database
//                                                            f.updateModule(getApplicationContext(), module);
                                                            c.overWriteModuleHashMapInCloud(moduleMap);

                                                            Intent restartAct = new Intent(EditSelectedModule.this, EditSelectedModule.class);
                                                            restartAct.putExtra("User String", userMap);
                                                            restartAct.putExtra("Module", moduleMap);
                                                            startActivity(restartAct);
                                                            dialog.dismiss();
                                                            finish();
                                                        }

                                                    }

                                            );
                                            b.show();
                                        }
                                    }

                            );
                            deleteSlideButt.setEnabled(true);
                            deleteSlideButt.setOnClickListener(
                                    new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
                                            int indexToDelete = position;
                                            final int filenameNumber = indexToDelete + 1;

                                            AlertDialog.Builder alert = new AlertDialog.Builder(EditSelectedModule.this);
                                            alert.setTitle("Are you sure you want to delete Slide " + filenameNumber);
                                            alert.setPositiveButton("Yes, delete", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {

                                                            // f.removeSlide(getApplicationContext(), module, position);
                                                            moduleMap = f.removeSlide(getApplicationContext(), moduleMap, position);
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
                        addNewSlide.putExtra("Module frame ready", moduleMap);

                        int nextIndex = Integer.parseInt(moduleMap.get("noOfSlides").toString());

                        nextIndex++;

                        addNewSlide.putExtra("Index of new slide", "" + nextIndex);
                        startActivityForResult(addNewSlide, 1);

                    }
                }

        );


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

        switch (resultCode) {
            case RESULT_CANCELED:
                Toast.makeText(EditSelectedModule.this, "Editing slide cancelled", Toast.LENGTH_SHORT).show();
                break;
            case RESULT_OK:
                Toast.makeText(EditSelectedModule.this, "Slide updated.", Toast.LENGTH_SHORT).show();
//                try {
//                    module = new JSONObject(data.getStringExtra("Module edited"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                moduleMap = (HashMap<String, Object>) data.getSerializableExtra("Module edited");
                c.overWriteModuleHashMapInCloud(moduleMap);

//                f.updateModule(getApplicationContext(), module);


                Intent restartAct = new Intent(EditSelectedModule.this, EditSelectedModule.class);
                restartAct.putExtra("User String", userMap);
                restartAct.putExtra("Module", moduleMap);
                startActivity(restartAct);
                finish();
                break;
        }
    }

    boolean wantsToQuit = false;

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
