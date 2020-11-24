package edu.cse570afox.workoutsmarter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter {

    private ArrayList<String> exerciseData;

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public TextView tvExerciseName;
        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
        }
        public TextView getTvExerciseName() { return tvExerciseName; }
    }

    public ExerciseAdapter(ArrayList<String> arrayList ) {
        exerciseData = arrayList;
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
        evh.getTvExerciseName().setText(exerciseData.get(position));
    }

    @Override
    public int getItemCount() {
        return exerciseData.size();
    }
}
