package edu.cse570afox.workoutsmarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddExercise extends AppCompatActivity {

    private static final String TAG = "AddExercise";
    private Exercise currentExercise = new Exercise();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Muscle Group Spinner
        Spinner spMuscleGroup = (Spinner) findViewById(R.id.muscleGroupSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.muscle_group_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spMuscleGroup.setAdapter(adapter);

        initSaveNewExerciseButton();
    }

    private void initSaveNewExerciseButton() {
        Button bSaveExercise = findViewById(R.id.saveExerciseButton);
        bSaveExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Save Exercise data to exercise DB before going back to Workout List
                // Exercise Name
                final EditText etExerciseName = (EditText) findViewById(R.id.exerciseNameEditText);
                currentExercise.setExerciseName(etExerciseName.getText().toString());

                // Calories Burned
                final EditText etCaloriesBurned = (EditText) findViewById(R.id.caloriesBurnedEditText);
                currentExercise.setCalories(Integer.parseInt(etCaloriesBurned.getText().toString()));

                // TODO: get spinner info for Muscle Group too
                // Muscle Group
                final Spinner spMuscleGroup = (Spinner) findViewById(R.id.muscleGroupSpinner);
                currentExercise.setMuscleGroupWorked(spMuscleGroup.getSelectedItem().toString());

                // Calories Burned
                final EditText etReps = (EditText) findViewById(R.id.repsEditText);
                currentExercise.setReps(Integer.parseInt(etReps.getText().toString()));

                // TODO: see if need to implement hideKeyboard method
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

                if (wasSuccessful) {
                    Log.d(TAG, "Successfully inserted/updated Exercise");
                }
                else {
                    Log.d(TAG, "Did not insert/update Exercise");
                }

                Intent intent = new Intent(AddExercise.this, WorkoutList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}