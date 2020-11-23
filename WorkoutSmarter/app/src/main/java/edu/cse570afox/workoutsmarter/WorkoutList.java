package edu.cse570afox.workoutsmarter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.Spinner;

public class WorkoutList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        // Spinner element
        // Reference: https://developer.android.com/guide/topics/ui/controls/spinner#java
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // CursorAdapter
        // TODO: implement cursoradapter
//        CursorAdapter adapter = CursorAdapter.
        // Spinner click listener
//        spinner.setOnItemSelectedListener(this);
    }
}