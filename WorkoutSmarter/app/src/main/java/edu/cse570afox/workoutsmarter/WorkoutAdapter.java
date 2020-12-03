package edu.cse570afox.workoutsmarter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter {

    private final String TAG = "WorkoutAdapter";
    private ArrayList<Workout> workoutData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {

        public TextView tvWorkoutName;
        public TextView tvNumExercises;
        public Button deleteButton;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkoutName = itemView.findViewById(R.id.workoutNameTextView);
            tvNumExercises = itemView.findViewById(R.id.numExercisesTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getTvWorkoutName() {
            return tvWorkoutName;
        }

        public TextView getTvNumExercises() {
            return tvNumExercises;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

    }

    public WorkoutAdapter(ArrayList<Workout> arrayList, Context context) {
        workoutData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item, parent, false);
        return new WorkoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        WorkoutAdapter.WorkoutViewHolder wvh = (WorkoutAdapter.WorkoutViewHolder) holder;
        wvh.getTvWorkoutName().setText(workoutData.get(position).getWorkoutName());
        // Get number of exercises by querying string and seeing how many semicolons are there
        String[] exercisesInfoStringArray = workoutData.get(position).getExercises().split(";");
        ArrayList<Exercise> exercisesInfo = new ArrayList<Exercise>();// workoutData.get(position).getExercises().split(";");
        if (exercisesInfoStringArray.length > 5) {
            for (int i = 0; i < exercisesInfoStringArray.length; i += 5) {
                Exercise newExercise = new Exercise();
                // add exercise ID to list
//            newExercise.setExerciseID(Integer.parseInt(exercisesInfoStringArray[i]));
                newExercise.setExerciseID(Integer.parseInt(exercisesInfoStringArray[i]));
                newExercise.setExerciseName(exercisesInfoStringArray[i + 1]);
                newExercise.setCalories(Integer.parseInt(exercisesInfoStringArray[i + 2]));
                newExercise.setReps(Integer.parseInt(exercisesInfoStringArray[i + 3]));
                newExercise.setMuscleGroupWorked(exercisesInfoStringArray[i + 4]);

                exercisesInfo.add(newExercise);
            }
        }
//       ArrayList<String> exerciseNames = new String[];
//        int sizeOfExercises = (int)(exercisesInfoStringArray.length / 4)
        wvh.getTvNumExercises().setText("Number of Exercises: " + Integer.toString((exercisesInfoStringArray.length / 4)));//workoutData.get(position).get());
        if (isDeleting) {
            wvh.getDeleteButton().setVisibility(View.VISIBLE);
            wvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else {
            wvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return workoutData.size();
    }

    private void deleteItem(int position) {
        Workout workout = workoutData.get(position);
        WorkoutDataSource ds = new WorkoutDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteWorkout(workout.getWorkoutID());
            ds.close();
            if (didDelete) {
                workoutData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentContext, "Delete failed.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
        }
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }
}
