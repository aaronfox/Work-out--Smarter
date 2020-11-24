package edu.cse570afox.workoutsmarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class WorkoutList extends AppCompatActivity {

    private static final String TAG = "WorkoutList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        // Init buttons
        initAddNewExerciseButton();
        initSaveWorkoutButton();

        ExerciseDataSource ds = new ExerciseDataSource(this);
        ArrayList<String> exerciseNames;
        try {
            ds.open();
            exerciseNames = ds.getExercises("a", "b");
            ds.close();
            RecyclerView exerciseList = findViewById(R.id.rvCurrentWorkoutPlan);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            exerciseList.setLayoutManager(layoutManager);
            ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exerciseNames);
            exerciseList.setAdapter(exerciseAdapter);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving exercises", Toast.LENGTH_LONG).show();
        }

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
                Intent intent = new Intent(WorkoutList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}