package edu.cse570afox.workoutsmarter;

import android.app.Activity;
import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter {

    private final String TAG = "ExerciseAdapter";
    private ArrayList<Exercise> exerciseData;
    private View.OnClickListener mOnItemClickListener;
    private Context parentContext;
    private boolean isDeleting;


    public class ExerciseViewHolder extends RecyclerView.ViewHolder {


        public TextView exerciseNameTextView;
        public TextView muscleGroupTextView;
        public TextView caloriesTextView;
        public TextView repsTextView;
        public Button deleteExerciseButton;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            muscleGroupTextView = itemView.findViewById(R.id.muscleGroupTextView);
            caloriesTextView = itemView.findViewById(R.id.caloriesTextView);
            repsTextView = itemView.findViewById(R.id.repsTextView);
            deleteExerciseButton = itemView.findViewById(R.id.exerciseDeleteButton);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
//        public TextView getTvExerciseName() { return tvExerciseName; }
//        public TextView getTvMuscleGroup() { return tvMuscleGroup; }
        public TextView getExerciseNameTextView() { return exerciseNameTextView; }
        public TextView getMuscleGroupTextView() { return muscleGroupTextView; }
        public TextView getCaloriesTextView() { return caloriesTextView; }
        public TextView getRepsTextView() { return repsTextView; }
        public Button getDeleteExerciseButton() {return  deleteExerciseButton;}
    }

    public ExerciseAdapter(ArrayList<Exercise> arrayList, Context context) {
        exerciseData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.updated_exercise_list_item, parent, false);
        return new ExerciseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ExerciseViewHolder evh = (ExerciseViewHolder) holder;
//        evh.getTvExerciseName().setText(exerciseData.get(position).getExerciseName());
//        evh.getTvMuscleGroup().setText(exerciseData.get(position).getMuscleGroupWorked());

        evh.getExerciseNameTextView().setText(exerciseData.get(position).getExerciseName());
        evh.getMuscleGroupTextView().setText("(" + exerciseData.get(position).getMuscleGroupWorked() +")");
        evh.getCaloriesTextView().setText("Calories: " + exerciseData.get(position).getCalories());
        evh.getRepsTextView().setText("Reps: " + exerciseData.get(position).getReps());

        if(isDeleting) {
            evh.getDeleteExerciseButton().setVisibility(View.VISIBLE);
            evh.getDeleteExerciseButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Don't delete item. Just return item id to remove from RV of workoutlist
                    // need context of workoutlist that called this not just a new workoutlist
                    WorkoutList workoutList = (WorkoutList) parentContext;
//                    WorkoutList workoutList = new WorkoutList(parentContext);
                    Exercise exercise = exerciseData.get(position);
                    int idToRemove = exercise.getExerciseID();
                    workoutList.removeExerciseFromCurrentExercisesInWorkout(idToRemove);
//                    exerciseToRemove(position);
//                    deleteItem(position);
                }
            });
        }
        else {
            evh.getDeleteExerciseButton().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return exerciseData.size();
    }

    private Exercise exerciseToRemove(int position) {
        Exercise exercise = exerciseData.get(position);
        return exercise;
    }

    private void deleteItem(int position) {
        Exercise exercise = exerciseData.get(position);
        ExerciseDataSource ds = new ExerciseDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteExercise(exercise.getExerciseID());
            ds.close();
            if (didDelete) {
                exerciseData.remove(position);
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
