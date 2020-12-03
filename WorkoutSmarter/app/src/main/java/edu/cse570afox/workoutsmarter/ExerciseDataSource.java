package edu.cse570afox.workoutsmarter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ExerciseDataSource {

    private SQLiteDatabase database;
    private ExerciseDBHelper dbHelper;

    public ExerciseDataSource(Context context) {
        dbHelper = new ExerciseDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertExercise(Exercise e) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("exercisename", e.getExerciseName());
            initialValues.put("calories", e.getCalories());
            initialValues.put("reps", e.getReps());
            initialValues.put("musclegroupworked", e.getMuscleGroupWorked());

            didSucceed = database.insert("exercise", null, initialValues) > 0;
        }
        catch (Exception ex) {
            // Will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateExercise(Exercise e) {
                boolean didSucceed = false;
                try {
                    Long rowId = (long) e.getExerciseID();
                    ContentValues updateValues = new ContentValues();

                    updateValues.put("exercisename", e.getExerciseName());
            updateValues.put("calories", e.getCalories());
            updateValues.put("reps", e.getReps());
            updateValues.put("musclegroupworked", e.getMuscleGroupWorked());

            didSucceed = database.update("exercise", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception ex) {
            // Will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastExerciseID() {
        int lastID = -1;
        try {
            String query = "Select MAX(_id) from exercise";
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

    // implement means of displaying needed data to recyclerview when user adds an exercise to a workout
    public ArrayList<Exercise> getExercises(String sortField, String sortOrder) {
        ArrayList<Exercise> exercises = new ArrayList<Exercise>();
//        ArrayList<String> exerciseNames = new ArrayList<String>();

        try {
            String query = "SELECT  * FROM exercise";
            Cursor cursor = database.rawQuery(query, null);

            Exercise newExercise;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newExercise = new Exercise();
                newExercise.setExerciseID(cursor.getInt(0));
                newExercise.setExerciseName(cursor.getString(1));
                newExercise.setCalories(cursor.getInt(2));
                newExercise.setReps(cursor.getInt(3));
                newExercise.setMuscleGroupWorked(cursor.getString(4));

                exercises.add(newExercise);
//                exerciseNames.add(cursor.getString(1));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            exercises = new ArrayList<Exercise>();
//            exerciseNames = new ArrayList<String>();
        }
        return exercises;
//        return exerciseNames;
    }

    public Exercise getSpecificExercise(int exerciseID) {
        Exercise exercise = new Exercise();
        String query = "SELECT * FROM exercise WHERE _id =" + exerciseID;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            exercise.setExerciseID(cursor.getInt(0));
            exercise.setExerciseName(cursor.getString(1));
            exercise.setCalories(cursor.getInt(2));
            exercise.setReps(cursor.getInt(3));
            exercise.setMuscleGroupWorked(cursor.getString(4));

            cursor.close();
        }

        return exercise;
    }

    public boolean deleteExercise(int exerciseID) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("exercise", "_id=" + exerciseID, null) > 0;
        }
        catch (Exception e) {

        }
        return didDelete;
    }
}
