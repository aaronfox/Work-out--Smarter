package edu.cse570afox.workoutsmarter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

public class WorkoutDataSource {


    private SQLiteDatabase database;
    private WorkoutDBHelper dbHelper;

    public WorkoutDataSource(Context context) {
        dbHelper = new WorkoutDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertWorkout(Workout w) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("workoutname", w.getWorkoutName());
            initialValues.put("exercises", w.getExercises());

            didSucceed = database.insert("workout", null, initialValues) > 0;
        }
        catch (Exception ex) {
            // Will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateWorkout(Workout w) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) w.getWorkoutID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("workoutname", w.getWorkoutName());
            updateValues.put("exercises", w.getExercises());

            didSucceed = database.update("workout", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception ex) {
            // Will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastWorkoutID() {
        int lastID = -1;
        try {
            String query = "Select MAX(_id) from workout";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastID = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastID = -1;
        }
        return lastID;
    }

    // TODO: implement means of displaying workouts in RecyclerView
    public ArrayList<Workout> getWorkouts(String sortField, String sortOrder) {
        ArrayList<Workout> workouts = new ArrayList<Workout>();
//        ArrayList<String> exerciseNames = new ArrayList<String>();

        try {
            String query = "SELECT  * FROM workout";
            Cursor cursor = database.rawQuery(query, null);

            Workout newWorkout;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newWorkout = new Workout();
                newWorkout.setWorkoutID(cursor.getInt(0));
                newWorkout.setWorkoutName(cursor.getString(1));
                newWorkout.setExercises(cursor.getString(2));

                workouts.add(newWorkout);
//                exerciseNames.add(cursor.getString(1));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            workouts = new ArrayList<Workout>();
//            exerciseNames = new ArrayList<String>();
        }
        return workouts;
//        return exerciseNames;
    }

    public Workout getSpecificWorkout(int workoutID) {
        Workout workout = new Workout();

        String query = "SELECT  * FROM workout WHERE _id =" + workoutID;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            workout.setWorkoutID(cursor.getInt(0));
            workout.setWorkoutName(cursor.getString(1));
            workout.setExercises(cursor.getString(2));

            cursor.close();
        }

        return workout;
    }

    public boolean deleteWorkout(int workoutID) {
        boolean isDelete
    }
}
