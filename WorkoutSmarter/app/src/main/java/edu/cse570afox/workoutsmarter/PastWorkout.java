package edu.cse570afox.workoutsmarter;

public class PastWorkout {

    private int pastWorkoutID;
    private String dateOfWorkout;
    private String workoutName;
    private int caloriesBurned;

    public PastWorkout() {
        pastWorkoutID = -1;
    }

    public int getPastWorkoutID() {
        return pastWorkoutID;
    }

    public void setPastWorkoutID(int pastWorkoutID) {
        this.pastWorkoutID = pastWorkoutID;
    }

    public String getDateOfWorkout() {
        return dateOfWorkout;
    }

    public void setDateOfWorkout(String dateOfWorkout) {
        this.dateOfWorkout = dateOfWorkout;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

}
