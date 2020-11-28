package edu.cse570afox.workoutsmarter;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter {

    private final String TAG = "ExerciseAdapter";
    private ArrayList<Exercise> exerciseData;
    private View.OnClickListener mOnItemClickListener;

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        public TextView tvExerciseName;
        public TextView tvMuscleGroup;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvMuscleGroup = itemView.findViewById(R.id.tvMuscleGroup);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
        public TextView getTvExerciseName() { return tvExerciseName; }
        public TextView getTvMuscleGroup() { return tvMuscleGroup; }
    }

    public ExerciseAdapter(ArrayList<Exercise> arrayList) {
        exerciseData = arrayList;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_item, parent, false);
        return new ExerciseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExerciseViewHolder evh = (ExerciseViewHolder) holder;
        evh.getTvExerciseName().setText(exerciseData.get(position).getExerciseName());
        evh.getTvMuscleGroup().setText(exerciseData.get(position).getMuscleGroupWorked());
    }

    @Override
    public int getItemCount() {
        return exerciseData.size();
    }
}
