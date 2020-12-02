package edu.cse570afox.workoutsmarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.Date;

public class WorkoutHistory extends AppCompatActivity {

    private static final String TAG = "WorkoutHistory";
    private PastWorkout currentPastWorkout = new PastWorkout();
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        // Init buttons
        initMainButton();
        initMapButton();
        changeWorkoutSpinner();
        datePicker = (DatePicker) findViewById(R.id.datePicker1);
        initRecordWorkoutButton();
    }

    private void initRecordWorkoutButton() {
        Button recordWorkoutButton = (Button) findViewById(R.id.recordWorkoutButton);
        recordWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save pastworkout data to db
                // Workout name
                Spinner workoutSpinner = (Spinner) findViewById(R.id.workoutHistorySelectSpinner);
                Cursor c = (Cursor) workoutSpinner.getSelectedItem();
                currentPastWorkout.setWorkoutName(c.getString(1));
//                c.close();
                currentPastWorkout.setDateOfWorkout(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear());
                // TODO: Count up actual calories burned from workout
                currentPastWorkout.setCaloriesBurned(90);
//                hideKeyboard();
                boolean wasSuccessful = false;
                // Need to add unique workout every time since someone could have multiple workouts in the same day
                PastWorkoutDataSource ds = new PastWorkoutDataSource(WorkoutHistory.this);
                try {
                    ds.open();

                    if (currentPastWorkout.getPastWorkoutID() == -1) {
                        wasSuccessful = ds.insertPastWorkout(currentPastWorkout);
                        int newID = ds.getLastPastWorkoutID();
                        currentPastWorkout.setPastWorkoutID(newID);
                    }
                    else {
                        // Just use insertPastWorkout instead of updatePastWorkout here
                        wasSuccessful = ds.insertPastWorkout(currentPastWorkout);
                    }
                    ds.close();
                }
                catch (Exception e) {
                    Log.v(TAG, "!!! wasn't successful bc e: " + e.toString());
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    Log.v(TAG, "!!! SUCC INSERT");
                } else {
                    Log.v(TAG, "!!! Not SUCC FOR INSERT");
                }
            }
        });
    }

    private void initMapButton() {
        ImageButton ibMap = findViewById(R.id.mapButton);
        ibMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutHistory.this, GymMap.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void changeWorkoutSpinner() {
        Spinner changeWorkoutSpinner = (Spinner) findViewById(R.id.workoutHistorySelectSpinner);
        String[] queryCols = new String[]{"_id", "workoutname"};
        SQLiteDatabase db = new WorkoutDBHelper(this).getReadableDatabase();
        String query = "SELECT workoutname FROM workout";
        // Query explanation: https://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
        Cursor cursor = db.query(
                "workout", // the table to query
                queryCols,                              // the columns to return
                null,       // the columns for the WHERE clause
                null,                          // the values for the WHERE clause
                null,                          // don't group the rows
                null,                           // don't filter by row groups
                "workoutname"                 // sort by exercise name
        );

        // Spinner example reference: https://en.proft.me/2016/10/20/spinner-example-android/

        String[] adapterCols = new String[]{"workoutname"};
        int[] adapterRowViews = new int[]{android.R.id.text1};
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, cursor, adapterCols, adapterRowViews, 0);
        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changeWorkoutSpinner.setAdapter(cursorAdapter);
    }

    private void initMainButton() {
        ImageButton ibMain = findViewById(R.id.mainButton);
        ibMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutHistory.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}