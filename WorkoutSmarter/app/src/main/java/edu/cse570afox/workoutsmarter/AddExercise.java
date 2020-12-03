package edu.cse570afox.workoutsmarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddExercise extends AppCompatActivity {

    private static final String TAG = "AddExercise";
    private Exercise currentExercise = new Exercise();
    private  ArrayAdapter<CharSequence> adapter;
    private int sentExerciseID = -1;
    private ArrayList<Exercise> sentExercises = new ArrayList<>();
    private String sentWorkoutName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Muscle Group Spinner
        Spinner spMuscleGroup = (Spinner) findViewById(R.id.muscleGroupSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.muscle_group_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spMuscleGroup.setAdapter(adapter);

        initSaveNewExerciseButton();
        initDeleteExerciseButton();


        Bundle extras = getIntent().getExtras();

        if (getIntent().hasExtra("exercise_array_list")) {
            sentExercises = getIntent().getParcelableArrayListExtra("exercise_array_list");
        }

        if (getIntent().hasExtra("workoutName")) {
            sentWorkoutName = getIntent().getStringExtra("workoutName");
        }
        if (getIntent().hasExtra("exerciseID")) {
            initExercise(extras.getInt("exerciseID"));
        }
        else {
            currentExercise = new Exercise();
        }
    }

    private void initDeleteExerciseButton() {
        Button bDeleteExercise = findViewById(R.id.deleteExerciseButton);
        bDeleteExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Get exercise ID and then delete item from there
                // first need to have extras from WorkoutList fill in info.
                // Then, when info is filled out, only then can an exercise be deleted. Otherwise, there is no exercise to delete.
                if (sentExerciseID > -1) {
                    ExerciseDataSource ds = new ExerciseDataSource(AddExercise.this);
                    try {
                        ds.open();
                        ds.deleteExercise(sentExerciseID);
                        ds.close();
                        // Go back to WorkoutList Activity
                        Intent intent = new Intent(AddExercise.this, WorkoutList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // send data back if needed

                        if (sentExercises.size() > 0) {
                            // Remove exercise from sent exercise first
                            for (int i = 0; i < sentExercises.size(); i++) {
                                if (sentExercises.get(i).getExerciseID() == sentExerciseID) {
                                    sentExercises.remove(i);
                                }
                            }
                            intent.putParcelableArrayListExtra("exercise_array_list", sentExercises);
                        }
                        if (sentWorkoutName.isEmpty() == false) {
                            intent.putExtra("workoutName", sentWorkoutName);
                        }
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(AddExercise.this, "Load Exercise Failed.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(AddExercise.this, "No Exercise to Delete.", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    private void initExercise(int id) {
        ExerciseDataSource ds = new ExerciseDataSource(AddExercise.this);
        sentExerciseID = id;
        try {
            ds.open();
            currentExercise = ds.getSpecificExercise(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Exercise Failed.", Toast.LENGTH_LONG).show();
        }

        // Fill in info for exercise
        EditText editName = findViewById(R.id.exerciseNameEditText);
        EditText caloriesBurnedEditText = findViewById(R.id.caloriesBurnedEditText);

        Spinner muscleGroupSpinner = findViewById(R.id.muscleGroupSpinner);

        EditText repsEditText = findViewById(R.id.repsEditText);

        editName.setText(currentExercise.getExerciseName());
        caloriesBurnedEditText.setText("" +currentExercise.getCalories());
        repsEditText.setText("" + currentExercise.getReps());
        // Set spinner position
        if (currentExercise.getMuscleGroupWorked() != null) {
            int spinnerPosition = adapter.getPosition(currentExercise.getMuscleGroupWorked());
            muscleGroupSpinner.setSelection(spinnerPosition);
        }

    }

    private void initSaveNewExerciseButton() {
        Button bSaveExercise = findViewById(R.id.saveExerciseButton);
        bSaveExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Save Exercise data to exercise DB before going back to Workout List
                // Exercise Name
                final EditText etExerciseName = (EditText) findViewById(R.id.exerciseNameEditText);
                // Make sure exercise name is filled out
                if (etExerciseName.getText().toString().isEmpty()) {
                    Toast.makeText(AddExercise.this,"Please enter entire exercise name", Toast.LENGTH_LONG).show();
                    return;
                }
                currentExercise.setExerciseName(etExerciseName.getText().toString());

                // Calories Burned
                final EditText etCaloriesBurned = (EditText) findViewById(R.id.caloriesBurnedEditText);
                // Make sure calories is valid number
                if (etCaloriesBurned.getText().toString().isEmpty() == false) {
                    if (Integer.parseInt(etCaloriesBurned.getText().toString()) <= 0) {
                        Toast.makeText(AddExercise.this, "Please enter entire a valid calorie count", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else {
                    Toast.makeText(AddExercise.this, "Please enter entire a valid calorie count", Toast.LENGTH_LONG).show();
                    return;
                }
                currentExercise.setCalories(Integer.parseInt(etCaloriesBurned.getText().toString()));

                // Get spinner info for Muscle Group too
                // Muscle Group
                final Spinner spMuscleGroup = (Spinner) findViewById(R.id.muscleGroupSpinner);
                currentExercise.setMuscleGroupWorked(spMuscleGroup.getSelectedItem().toString());

                // Number of Reps
                final EditText etReps = (EditText) findViewById(R.id.repsEditText);
                // Make sure reps is valid number
                if (etReps.getText().toString().isEmpty() == false) {
                    if (Integer.parseInt(etReps.getText().toString()) <= 0) {
                        Toast.makeText(AddExercise.this, "Please enter entire a valid rep count", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else {
                    Toast.makeText(AddExercise.this, "Please enter entire a valid rep count", Toast.LENGTH_LONG).show();
                    return;
                }
                currentExercise.setReps(Integer.parseInt(etReps.getText().toString()));

                // see if need to implement hideKeyboard method
//                hideKeyboard();

                boolean wasSuccessful = false;
                ExerciseDataSource ds = new ExerciseDataSource(AddExercise.this);
                try {
                    ds.open();

                    if (currentExercise.getExerciseID() == -1) {
                        wasSuccessful = ds.insertExercise(currentExercise);
                        int newID = ds.getLastExerciseID();
                        currentExercise.setExerciseID(newID);
                    }
                    else {
                        wasSuccessful = ds.updateExercise(currentExercise);
                    }
                    ds.close();
                }
                catch (Exception e) {
                    Log.d(TAG, "Caught exception: " + e.toString());
                    wasSuccessful = false;
                }


                Intent intent = new Intent(AddExercise.this, WorkoutList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (sentExercises.size() > 0) {
                    // If user changed content of this exercise, change it in sentExercises so it's fixed in the RV of WorkoutList
                    for (int i = 0; i < sentExercises.size(); i++) {
                        if (sentExercises.get(i).getExerciseID() == sentExerciseID) {
                            // Change all data to reflect changes user made
                            sentExercises.get(i).setExerciseName(etExerciseName.getText().toString());
                            sentExercises.get(i).setCalories(Integer.parseInt(etCaloriesBurned.getText().toString()));
                            sentExercises.get(i).setMuscleGroupWorked(spMuscleGroup.getSelectedItem().toString());
                            sentExercises.get(i).setReps(Integer.parseInt(etReps.getText().toString()));
                        }
                    }
                    intent.putParcelableArrayListExtra("exercise_array_list", sentExercises);
                }
                if (sentWorkoutName.isEmpty() == false) {
                    intent.putExtra("workoutName", sentWorkoutName);
                }
                startActivity(intent);
            }
        });
    }

}