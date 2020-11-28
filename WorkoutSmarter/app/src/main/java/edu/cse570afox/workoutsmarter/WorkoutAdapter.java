package edu.cse570afox.workoutsmarter;

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
        // TODO: get number of exercises and place it here
        wvh.getTvWorkoutName().setText(workoutData.get(position).getWorkoutName());
        wvh.getTvNumExercises().setText("6");//workoutData.get(position).get());

//        evh.getTvMuscleGroup().setText(exerciseData.get(position).getMuscleGroupWorked());
    }

    @Override
    public int getItemCount() {
        return workoutData.size();
    }
}
