package edu.cse570afox.workoutsmarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainWorkoutActivity";
    ArrayList<Workout> workouts;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.v(TAG, "CLICKED");
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int workoutID = workouts.get(position).getWorkoutID();
            Intent intent = new Intent(MainActivity.this, WorkoutList.class);
            intent.putExtra("workoutID", workoutID);
            Log.v(TAG, "sending ID of " + workoutID);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set data for RecyclerView
        WorkoutDataSource ds = new WorkoutDataSource(this);

        try {
            ds.open();
            workouts = ds.getWorkouts("a", "b");
            ds.close();
            RecyclerView workoutList = findViewById(R.id.workoutListRecyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            workoutList.setLayoutManager(layoutManager);
            WorkoutAdapter workoutAdapter = new WorkoutAdapter(workouts);
            workoutAdapter.setOnItemClickListener(onItemClickListener);
            workoutList.setAdapter(workoutAdapter);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error Retrieving Workouts", Toast.LENGTH_LONG).show();
        }

        // Init buttons
        initHistoryButton();
        initMapButton();
        initAddNewWorkoutButton();
        initDeleteSwitch();
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.deleteSwitch);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean status = compoundButton.isChecked();
                WorkoutAdapter workoutAdapter = new WorkoutAdapter(workouts);
                workoutAdapter.setDelete(status);
                workoutAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initHistoryButton() {
        ImageButton ibHistory = findViewById(R.id.historyButton);
        ibHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WorkoutHistory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMapButton() {
        ImageButton ibMap = findViewById(R.id.mapButton);
        ibMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GymMap.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initAddNewWorkoutButton() {
        Button bAddNewWorkout = findViewById(R.id.addNewWorkoutButton);
        bAddNewWorkout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WorkoutList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}