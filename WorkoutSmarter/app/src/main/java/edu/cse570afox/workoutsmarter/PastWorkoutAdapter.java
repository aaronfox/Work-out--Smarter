package edu.cse570afox.workoutsmarter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PastWorkoutAdapter extends RecyclerView.Adapter {

    private ArrayList<PastWorkout> pastWorkoutData;

    public class PastWorkoutViewHolder extends RecyclerView.ViewHolder {


        public TextView workoutHistoryItemNameTextView;
        public TextView workoutHistoryItemCaloriesBurnedTextView;
        public TextView workoutHistoryDateTextView;

        public PastWorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutHistoryItemNameTextView = itemView.findViewById(R.id.workoutHistoryItemNameTextView);
            workoutHistoryItemCaloriesBurnedTextView = itemView.findViewById(R.id.workoutHistoryItemCaloriesBurnedTextView);
            workoutHistoryDateTextView = itemView.findViewById(R.id.workoutHistoryDateTextView);
        }

        public TextView getWorkoutHistoryItemNameTextView() {
            return workoutHistoryItemNameTextView;
        }

        public TextView getWorkoutHistoryItemCaloriesBurnedTextView() {
            return workoutHistoryItemCaloriesBurnedTextView;
        }

        public TextView getWorkoutHistoryDateTextView() {
            return workoutHistoryDateTextView;
        }
    }

    public PastWorkoutAdapter(ArrayList<PastWorkout> arrayList) {
        pastWorkoutData = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_history_item_view, parent, false);
        return new PastWorkoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PastWorkoutViewHolder pwvh = (PastWorkoutViewHolder) holder;
        pwvh.getWorkoutHistoryItemNameTextView().setText(pastWorkoutData.get(position).getWorkoutName());
        pwvh.getWorkoutHistoryDateTextView().setText(pastWorkoutData.get(position).getDateOfWorkout());
        pwvh.getWorkoutHistoryItemCaloriesBurnedTextView().setText("Calories Burned: " + pastWorkoutData.get(position).getCaloriesBurned());
    }

    @Override
    public int getItemCount() {
        return pastWorkoutData.size();
    }
}
