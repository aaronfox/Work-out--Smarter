package edu.cse570afox.workoutsmarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class WorkoutList extends AppCompatActivity {

    private static final String TAG = "WorkoutList";
    private ArrayList<Exercise> exercises;
    private ArrayList<Exercise> currentExercisesInWorkout = new ArrayList<Exercise>();
    private Workout currentWorkout = new Workout();

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int positon = viewHolder.getAdapterPosition();
            Intent intent = new Intent(WorkoutList.this, MainActivity.class);
            startActivity(intent);
        }
    };
    // TODO: left off on completing workoutlist on lecture 6 pt 2 page 10
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        // Init buttons
        initAddNewExerciseButton();
        initSaveWorkoutButton();
        initAddExerciseToWorkoutListButton();

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
                Log.v(TAG, "editTextWorkoutName.getText().toString() == " + editTextWorkoutName.getText().toString());
                currentWorkout.setWorkoutName(editTextWorkoutName.getText().toString());

                // Exercises: use currentExercisesInWorkout array to get current exercises
                StringBuffer sb = new StringBuffer();
                Exercise currExerciseForLoop;
                for (int i = 0; i < currentExercisesInWorkout.size(); i++) {
                    // Serialize data using semicolons
                    currExerciseForLoop = currentExercisesInWorkout.get(i);
                    String exerciseStringToAdd = currExerciseForLoop.getExerciseName() + ";"
                            + currExerciseForLoop.getCalories() + ";"
                            + currExerciseForLoop.getReps() + ";"
                            + currExerciseForLoop.getMuscleGroupWorked() + ";";

                    sb.append(exerciseStringToAdd);
                }
                // TODO: combine exercise data array into one string for adding into Workout DB
                String finalStringToAdd = sb.toString();
                currentWorkout.setExercises(finalStringToAdd);

                // TODO: save workout to SQLite first
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


                // END TODO:
                Intent intent = new Intent(WorkoutList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }



    private void initAddExerciseToWorkoutListButton() {
        Button bAddExercise = findViewById(R.id.addExerciseButton);
        bAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add current exercise in exercise spinner
                Log.d(TAG, "Clicked add button");
                Spinner addExerciseSpinner = (Spinner) findViewById(R.id.addExerciseSpinner);
                Cursor stringCursor = (Cursor) addExerciseSpinner.getSelectedItem();
                String exerciseNameToAdd = stringCursor.getString(stringCursor.getColumnIndex("exercisename"));
//                stringCursor.close();
                // TODO: Get actual exercise by exercise name
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
                        updateRecyclerView();
                        // TODO: Add the above data to Recycler View instead of just printing it
                        Log.d(TAG," WOO " + exerciseNameString + " " + caloriesInt + " " + repsInt + " " + muscleGroupString);
                    } while(cursor.moveToNext());
                }
//                cursor.close();
//                Log.d(TAG,"hellllo: " + cursor.getString(0));//cursor.getString(2) + " " + cursor.getString(4));
            }
        });
    }

    private void updateRecyclerView() {
        RecyclerView exerciseList = findViewById(R.id.rvCurrentWorkoutPlan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        exerciseList.setLayoutManager(layoutManager);
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(currentExercisesInWorkout);
        exerciseAdapter.setOnItemClickListener(onItemClickListener);
        exerciseList.setAdapter(exerciseAdapter);
    }
}