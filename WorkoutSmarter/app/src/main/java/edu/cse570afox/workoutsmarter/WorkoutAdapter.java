package edu.cse570afox.workoutsmarter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter {

    private final String TAG = "WorkoutAdapter";
    private ArrayList<Workout> workoutData;
    private View.OnClickListener mOnItemClickListener;

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {

        public TextView tvWorkoutName;
        public TextView tvNumExercises;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkoutName = itemView.findViewById(R.id.workoutNameTextView);
            tvNumExercises = itemView.findViewById(R.id.numExercisesTextView);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
        public TextView getTvWorkoutName() { return tvWorkoutName; }
        public TextView getTvNumExercises() { return tvNumExercises; }
    }

    public WorkoutAdapter(ArrayList<Workout> arrayList) {
        workoutData = arrayList;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WorkoutAdapter.WorkoutViewHolder wvh = (WorkoutAdapter.WorkoutViewHolder) holder;
        wvh.getTvWorkoutName().setText(workoutData.get(position).getWorkoutName());
        // TODO: get number of exercises and place it here
        // Get number of exercises by querying string and seeing how many semicolons are there
        String[] exercisesInfoStringArray = workoutData.get(position).getExercises().split(";");
        ArrayList<Exercise>  exercisesInfo = new ArrayList<Exercise>();// workoutData.get(position).getExercises().split(";");
        if (exercisesInfoStringArray.length > 5) {
            for (int i = 0; i < exercisesInfoStringArray.length; i += 5) {
                Exercise newExercise = new Exercise();
                // TODO add exercise ID to list
//            newExercise.setExerciseID(Integer.parseInt(exercisesInfoStringArray[i]));
                newExercise.setExerciseID(Integer.parseInt(exercisesInfoStringArray[i]));
                newExercise.setExerciseName(exercisesInfoStringArray[i+1]);
                newExercise.setCalories(Integer.parseInt(exercisesInfoStringArray[i + 2]));
                newExercise.setReps(Integer.parseInt(exercisesInfoStringArray[i + 3]));
                newExercise.setMuscleGroupWorked(exercisesInfoStringArray[i + 4]);

                exercisesInfo.add(newExercise);
            }
        }
//       ArrayList<String> exerciseNames = new String[];
//        int sizeOfExercises = (int)(exercisesInfoStringArray.length / 4)
        wvh.getTvNumExercises().setText("Number of Exercises: " + Integer.toString((exercisesInfoStringArray.length / 4)));//workoutData.get(position).get());

//        evh.getTvMuscleGroup().setText(exerciseData.get(position).getMuscleGroupWorked());
    }

    @Override
    public int getItemCount() {
        return workoutData.size();
    }

//    private void deleteWorkout(int position) {
//        Workout workout = workoutData.get(position);
//        WorkoutDataSource ds = new WorkoutDataSource(parentContext);
//    }
}
