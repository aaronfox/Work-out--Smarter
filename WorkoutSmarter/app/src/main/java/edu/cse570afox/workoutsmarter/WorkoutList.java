package edu.cse570afox.workoutsmarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class WorkoutList extends AppCompatActivity {

    private static final String TAG = "WorkoutList";
    private ArrayList<Exercise> exercises;
    private ArrayList<Exercise> currentExercisesInWorkout = new ArrayList<Exercise>();
    private Workout currentWorkout = new Workout();
    private ExerciseAdapter exerciseAdapter;
    private boolean isDeletingStatus;

//    public WorkoutList(Context context)
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // On Clicking exercise, send extras to AddExercise activity to edit it
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int exerciseID = currentExercisesInWorkout.get(position).getExerciseID();
            Intent intent = new Intent(WorkoutList.this, AddExercise.class);
            intent.putExtra("exerciseID", exerciseID);
            startActivity(intent);
        }
    };
    // TODO: left off on completing workoutlist on lecture 6 pt 2 page 10
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.v(TAG, "!!!EXTRAS");
            initWorkout(extras.getInt("workoutID"));
        } else {
            Log.v(TAG, "!!!EXTRAS NULL");
            currentWorkout = new Workout();
        }
        // Init buttons
        initAddNewExerciseButton();
        initSaveWorkoutButton();
        initAddExerciseToWorkoutListButton();
        initDeleteSwitch();

        // Add items to RecyclerView
//        ExerciseDataSource ds = new ExerciseDataSource(this);
//        try {
//            ds.open();
//            exercises = ds.getExercises("a", "b");
//            ds.close();
//            RecyclerView exerciseList = findViewById(R.id.rvCurrentWorkoutPlan);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//            exerciseList.setLayoutManager(layoutManager);
//            ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exercises);
//            exerciseAdapter.setOnItemClickListener(onItemClickListener);
//            exerciseList.setAdapter(exerciseAdapter);
//        }
//        catch (Exception e) {
//            Toast.makeText(this, "Error retrieving exercises", Toast.LENGTH_LONG).show();
//        }

        updateRecyclerView();
        // Spinner element
        // Reference: https://developer.android.com/guide/topics/ui/controls/spinner#java
        Spinner workoutGroupToAddSpinner = (Spinner) findViewById(R.id.workoutGroupToAddSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.muscle_group_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        workoutGroupToAddSpinner.setAdapter(adapter);

        // Initialize data in add exercise spinner
        changeAddExerciseSpinnerData();

        // workoutGroupToAddSpinner click listener
        workoutGroupToAddSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeAddExerciseSpinnerData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void changeAddExerciseSpinnerData() {
        Spinner workoutGroupToAddSpinner = (Spinner) findViewById(R.id.workoutGroupToAddSpinner);
        //Spinner for adding exercise
        Spinner addExerciseSpinner = (Spinner) findViewById(R.id.addExerciseSpinner);
        // Get Muscle group to use in query
        String currMuscleGroup = workoutGroupToAddSpinner.getSelectedItem().toString();
//        ExerciseDataSource ds =
//        String query = "Select from exercise";
//        Cursor cursor = database.rawQuery(query, null);
        String[] queryCols = new String[]{"_id", "exercisename"};
        SQLiteDatabase db = new ExerciseDBHelper(this).getReadableDatabase();
        String[] group_to_work = new String[] {currMuscleGroup};
        // Query explanation: https://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
        Cursor cursor = db.query(
                "exercise", // the table to query
                queryCols,                              // the columns to return
                "musclegroupworked = ?",       // the columns for the WHERE clause
                group_to_work,                          // the values for the WHERE clause
                null,                          // don't group the rows
                null,                           // don't filter by row groups
                "exercisename"                 // sort by exercise name
        );

        // Spinner example reference: https://en.proft.me/2016/10/20/spinner-example-android/

        String[] adapterCols = new String[]{"exercisename"};
        int[] adapterRowViews = new int[]{android.R.id.text1};
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, cursor, adapterCols, adapterRowViews, 0);
        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addExerciseSpinner.setAdapter(cursorAdapter);
    }

    private void initAddNewExerciseButton() {
        Button bAddCustomExercise = findViewById(R.id.addCustomExerciseIntentButton);
        bAddCustomExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutList.this, AddExercise.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSaveWorkoutButton() {
        Button bSaveWorkout = findViewById(R.id.saveWorkoutButton);
        bSaveWorkout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Save Workout Data from to currentWorkout
                // Workout Name
                final EditText editTextWorkoutName = (EditText) findViewById(R.id.editTextWorkoutName);
                // Make sure User has workout name filled out
                if (editTextWorkoutName.getText().toString().isEmpty()) {
                    Toast.makeText(WorkoutList.this,"Please enter entire workout name", Toast.LENGTH_LONG).show();
                    return;
                }
                currentWorkout.setWorkoutName(editTextWorkoutName.getText().toString());

                // Exercises: use currentExercisesInWorkout array to get current exercises
                StringBuffer sb = new StringBuffer();
                Exercise currExerciseForLoop;
                // Make sure there are workouts in the list
                if (currentExercisesInWorkout.size() > 0) {
                    for (int i = 0; i < currentExercisesInWorkout.size(); i++) {
                        // Serialize data using semicolons
                        currExerciseForLoop = currentExercisesInWorkout.get(i);
                        String exerciseStringToAdd = currExerciseForLoop.getExerciseID() + ";"
                                + currExerciseForLoop.getExerciseName() + ";"
                                + currExerciseForLoop.getCalories() + ";"
                                + currExerciseForLoop.getReps() + ";"
                                + currExerciseForLoop.getMuscleGroupWorked() + ";";

                        sb.append(exerciseStringToAdd);
                    }
                }
                else {
                    Toast.makeText(WorkoutList.this,"Please add at least one exercise.", Toast.LENGTH_LONG).show();
                    return;
                }
                // Combine exercise data array into one string for adding into Workout DB
                String finalStringToAdd = sb.toString();
                Log.d(TAG, "finalStringToAdd == " + finalStringToAdd);
                currentWorkout.setExercises(finalStringToAdd);

                // Save workout to SQLite first
                boolean wasSuccessful = false;
                WorkoutDataSource ds = new WorkoutDataSource(WorkoutList.this);
                try {
                    ds.open();

                    if (currentWorkout.getWorkoutID() == -1) {
                        wasSuccessful = ds.insertWorkout(currentWorkout);
                        int newID = ds.getLastWorkoutID();
                        currentWorkout.setWorkoutID(newID);
                    }
                    else {
                        wasSuccessful = ds.updateWorkout(currentWorkout);
                    }
                    ds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                }

                if(wasSuccessful) {
                    Log.v(TAG, "SUCCESFULLLLL");
                }


                Intent intent = new Intent(WorkoutList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    // Need to have context of WorkoutList when calling this. Otherwise there will be different arraylists of currentExercisesInWorkout
    public void removeExerciseFromCurrentExercisesInWorkout(int id) {
        boolean didDelete = false;
        for (int i = 0; i < currentExercisesInWorkout.size(); i++) {
            if (didDelete == false) {
                if (currentExercisesInWorkout.get(i).getExerciseID() == id) {
                    currentExercisesInWorkout.remove(i);
                    didDelete = true;
                    updateRecyclerView();
                    // TODO: make sure DELETE button still there after a delete
                    exerciseAdapter.setDelete(true);
                    isDeletingStatus = true;
                }
            }
        }
    }


    private void initAddExerciseToWorkoutListButton() {
        Button bAddExercise = findViewById(R.id.addExerciseButton);
        bAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add current exercise in exercise spinner
                Spinner addExerciseSpinner = (Spinner) findViewById(R.id.addExerciseSpinner);
                Cursor stringCursor = (Cursor) addExerciseSpinner.getSelectedItem();
                String exerciseNameToAdd = stringCursor.getString(stringCursor.getColumnIndex("exercisename"));
//                stringCursor.close();
                // Get actual exercise by exercise name
                String[] queryCols = new String[]{"_id", "exercisename", "calories", "reps", "musclegroupworked"};
                SQLiteDatabase db = new ExerciseDBHelper(WorkoutList.this).getReadableDatabase();
                String[] exercise_name_to_add = new String[] {exerciseNameToAdd};
                Log.d(TAG, "exerciseNameToAdd == " + exerciseNameToAdd);
                // Query explanation: https://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
                Cursor cursor = db.query(
                        "exercise", // the table to query
                        queryCols,                              // the columns to return
                        "exercisename = ?",       // the columns for the WHERE clause
                        exercise_name_to_add,                          // the values for the WHERE clause
                        null,                          // don't group the rows
                        null,                           // don't filter by row groups
                        null                 // don't sort
                );
//                exercises.add(exerciseToAdd);
                if (cursor.moveToFirst()){
                    do {
                        String exerciseNameString = cursor.getString(cursor.getColumnIndex("exercisename"));
                        int caloriesInt = cursor.getInt(cursor.getColumnIndex("calories"));
                        int repsInt = cursor.getInt(cursor.getColumnIndex("reps"));
                        String muscleGroupString =  cursor.getString(cursor.getColumnIndex("musclegroupworked"));
                        Exercise exerciseToAdd = new Exercise();
                        exerciseToAdd.setExerciseID(cursor.getInt(cursor.getColumnIndex("_id")));
                        exerciseToAdd.setExerciseName(exerciseNameString);
                        exerciseToAdd.setCalories(caloriesInt);
                        exerciseToAdd.setReps(repsInt);
                        exerciseToAdd.setMuscleGroupWorked(muscleGroupString);
                        currentExercisesInWorkout.add(exerciseToAdd);

                        // Add the above data to Recycler View
                        updateRecyclerView();
                    } while(cursor.moveToNext());
                }
                if (isDeletingStatus) {
                    exerciseAdapter.setDelete(isDeletingStatus);
                }
//                cursor.close();
            }
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.deleteExerciseSwitch);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean status = compoundButton.isChecked();
                isDeletingStatus = status;
                exerciseAdapter.setDelete(status);
                exerciseAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateRecyclerView() {
        RecyclerView exerciseList = findViewById(R.id.rvCurrentWorkoutPlan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        exerciseList.setLayoutManager(layoutManager);
        exerciseAdapter = new ExerciseAdapter(currentExercisesInWorkout, this);
        exerciseAdapter.setOnItemClickListener(onItemClickListener);
        exerciseList.setAdapter(exerciseAdapter);
    }

    private void initWorkout(int id) {
        WorkoutDataSource ds = new WorkoutDataSource(WorkoutList.this);
        try {
            ds.open();
            currentWorkout = ds.getSpecificWorkout(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Loading of workout failed.", Toast.LENGTH_LONG).show();
        }

        // Update workout to reflect current workout
        Log.v(TAG, "updating recycler view!");
        // TODO: Get every exercise that is in workout then add it to RecyclerView
        // ALSO TODO: correctly edit exercise info when a user clicks on the workout in the MainActivity.
        // Need to get exercise id from exercises list first
        SQLiteDatabase db = new WorkoutDBHelper(WorkoutList.this).getReadableDatabase();
        String query = "SELECT * FROM workout WHERE _id =" + id;
        Cursor cursor = db.rawQuery(query, null);

        String exercisesString;
        String workoutName;
        if (cursor.moveToFirst()){
            do {
                workoutName = cursor.getString(cursor.getColumnIndex("workoutname"));
                exercisesString = cursor.getString(cursor.getColumnIndex("exercises"));
//                int caloriesInt = cursor.getInt(cursor.getColumnIndex("calories"));
//                int repsInt = cursor.getInt(cursor.getColumnIndex("reps"));
//                String muscleGroupString =  cursor.getString(cursor.getColumnIndex("musclegroupworked"));
//                Exercise exerciseToAdd = new Exercise();
//                exerciseToAdd.setExerciseID(cursor.getInt(cursor.getColumnIndex("_id")));
//                exerciseToAdd.setExerciseName(exerciseNameString);
//                exerciseToAdd.setCalories(caloriesInt);
//                exerciseToAdd.setReps(repsInt);
//                exerciseToAdd.setMuscleGroupWorked(muscleGroupString);
//                currentExercisesInWorkout.add(exerciseToAdd);
                // Add the above data to Recycler View
//                updateRecyclerView();
            } while(cursor.moveToNext());
        }
        else {
            Toast.makeText(this, "Could not fetch exercises.", Toast.LENGTH_LONG).show();
            return;
        }
        cursor.close();
        // Go through exercises in exercise string
        String[] exercisesInfoStringArray = exercisesString.split(";");
//        ArrayList<Exercise>  exercisesInfo = new ArrayList<Exercise>();// workoutData.get(position).getExercises().split(";");
        if (exercisesInfoStringArray.length > 5) {
            for (int i = 0; i < exercisesInfoStringArray.length; i += 5) {
                Exercise newExercise = new Exercise();
                // TODO add exercise ID to list
//            newExercise.setExerciseID(Integer.parseInt(exercisesInfoStringArray[i]));
                newExercise.setExerciseID(Integer.parseInt(exercisesInfoStringArray[i]));
                newExercise.setExerciseName(exercisesInfoStringArray[i+1]);
                newExercise.setCalories(Integer.parseInt(exercisesInfoStringArray[i + 2]));
                newExercise.setReps(Integer.parseInt(exercisesInfoStringArray[i + 3]));
                newExercise.setMuscleGroupWorked(exercisesInfoStringArray[i + 4]);

                currentExercisesInWorkout.add(newExercise);
            }
        }
        updateRecyclerView();
        // Add name of workout to textfield
        final EditText editTextWorkoutName = (EditText) findViewById(R.id.editTextWorkoutName);
        editTextWorkoutName.setText(workoutName);


    }
}