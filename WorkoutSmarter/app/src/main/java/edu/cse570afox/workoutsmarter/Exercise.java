package edu.cse570afox.workoutsmarter;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {

    private int exerciseID;
    private String exerciseName;
    private int calories;
    private int reps;
    private String muscleGroupWorked;

    public Exercise() {
        exerciseID = -1;
    }

    protected Exercise(Parcel in) {
        exerciseID = in.readInt();
        exerciseName = in.readString();
        calories = in.readInt();
        reps = in.readInt();
        muscleGroupWorked = in.readString();

    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getMuscleGroupWorked() {
        return muscleGroupWorked;
    }

    public void setMuscleGroupWorked(String muscleGroupWorked) {
        this.muscleGroupWorked = muscleGroupWorked;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(exerciseID);
        parcel.writeString(exerciseName);
        parcel.writeInt(calories);
        parcel.writeInt(reps);
        parcel.writeString(muscleGroupWorked);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}
