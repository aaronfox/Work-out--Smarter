package edu.cse570afox.workoutsmarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Spinner;

public class WorkoutList extends AppCompatActivity {

    private static final String TAG = "WorkoutList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        // Init buttons
        initAddNewExerciseButton();
        initSaveWorkoutButton();

        // Spinner element
        // Reference: https://developer.android.com/guide/topics/ui/controls/spinner#java
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // CursorAdapter
        // TODO: implement cursoradapter
//        CursorAdapter adapter = CursorAdapter.
        // Spinner click listener
//        spinner.setOnItemSelectedListener(this);
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