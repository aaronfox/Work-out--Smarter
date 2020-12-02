package edu.cse570afox.workoutsmarter;

import android.content.Context;
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

public class PastWorkoutAdapter extends RecyclerView.Adapter {

    private ArrayList<PastWorkout> pastWorkoutData;
    public Context parentContext;
    private boolean isDeleting;

    public class PastWorkoutViewHolder extends RecyclerView.ViewHolder {


        public TextView workoutHistoryItemNameTextView;
        public TextView workoutHistoryItemCaloriesBurnedTextView;
        public TextView workoutHistoryDateTextView;
        public Button deleteButton;

        public PastWorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutHistoryItemNameTextView = itemView.findViewById(R.id.workoutHistoryItemNameTextView);
            workoutHistoryItemCaloriesBurnedTextView = itemView.findViewById(R.id.workoutHistoryItemCaloriesBurnedTextView);
            workoutHistoryDateTextView = itemView.findViewById(R.id.workoutHistoryDateTextView);
            deleteButton = itemView.findViewById(R.id.workoutHistoryItemDeleteButton);
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

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public PastWorkoutAdapter(ArrayList<PastWorkout> arrayList, Context context) {
        pastWorkoutData = arrayList;
        parentContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_history_item_view, parent, false);
        return new PastWorkoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        PastWorkoutViewHolder pwvh = (PastWorkoutViewHolder) holder;
        pwvh.getWorkoutHistoryItemNameTextView().setText(pastWorkoutData.get(position).getWorkoutName());
        pwvh.getWorkoutHistoryDateTextView().setText(pastWorkoutData.get(position).getDateOfWorkout());
        pwvh.getWorkoutHistoryItemCaloriesBurnedTextView().setText("Calories Burned: " + pastWorkoutData.get(position).getCaloriesBurned());
        if (isDeleting) {
            pwvh.getDeleteButton().setVisibility(View.VISIBLE);
            pwvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else {
            pwvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return pastWorkoutData.size();
    }

    private void deleteItem(int position) {
        PastWorkout pastWorkout = pastWorkoutData.get(position);
        PastWorkoutDataSource ds = new PastWorkoutDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deletePastWorkout(pastWorkout.getPastWorkoutID());
            ds.close();
            if (didDelete) {
                pastWorkoutData.remove(position);
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
