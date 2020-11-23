package edu.cse570afox.workoutsmarter;

public class Exercise {

    private int exerciseID;
    private String exerciseName;
    private int calories;
    private int reps;
    private String muscleGroupWorked;

    public Exercise() {
        exerciseID = -1;
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


}
