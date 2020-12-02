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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class WorkoutHistory extends AppCompatActivity {

    private static final String TAG = "WorkoutHistory";
    private PastWorkout currentPastWorkout = new PastWorkout();
    private DatePicker datePicker;
    private PastWorkoutAdapter pastWorkoutAdapter;
    ArrayList<PastWorkout> pastWorkouts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        // Init buttons
        initMainButton();
        initMapButton();
        changeWorkoutSpinner();
        initDeleteSwitch();
        datePicker = (DatePicker) findViewById(R.id.datePicker1);
        initRecordWorkoutButton();

        updateRecyclerView();

    }

    private void updateRecyclerView() {
        PastWorkoutDataSource ds = new PastWorkoutDataSource(this);
        try {
            ds.open();
            pastWorkouts = ds.getPastworkouts("a", "b");
            ds.close();
            RecyclerView pastWorkoutRV = findViewById(R.id.workoutHistoryRV);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            pastWorkoutRV.setLayoutManager(layoutManager);
            pastWorkoutAdapter = new PastWorkoutAdapter(pastWorkouts, this);
            pastWorkoutRV.setAdapter(pastWorkoutAdapter);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving past workouts.", Toast.LENGTH_LONG).show();
        }
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.workoutHistorySwitch);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean status = compoundButton.isChecked();
                pastWorkoutAdapter.setDelete(status);
                pastWorkoutAdapter.notifyDataSetChanged();
            }
        });
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
                String currWorkoutName = c.getString(1);
                currentPastWorkout.setWorkoutName(currWorkoutName);
//                c.close();
                currentPastWorkout.setDateOfWorkout(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear());
                // TODO: Count up actual calories burned from workout
                // First, get exercises from workout
                WorkoutDataSource workoutDS = new WorkoutDataSource(WorkoutHistory.this);
                // Find id of workout name
                int calorieCount = 0;
                try {
                    String query = "SELECT  * FROM workout WHERE workoutname ='" + currWorkoutName +"'";
                    SQLiteDatabase db = new WorkoutDBHelper(WorkoutHistory.this).getReadableDatabase();
                    Cursor cursor = db.rawQuery(query, null);

                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String exercises = cursor.getString(2);
                        String[] exercisesInfoStringArray = exercises.split(";");
                        if (exercisesInfoStringArray.length > 5) {
                            for (int i = 0; i < exercisesInfoStringArray.length; i += 5) {
                                calorieCount += Integer.parseInt(exercisesInfoStringArray[i + 2]);
                            }
                        }
//                        calorieCount += cursor.getInt()

                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                catch (Exception e) {
                        Log.v(TAG, "!!! Could not add calories, e: " + e.toString());
                }
                // end find of id of workout name
                currentPastWorkout.setCaloriesBurned(calorieCount);
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

                updateRecyclerView();
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