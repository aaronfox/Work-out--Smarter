package edu.cse570afox.workoutsmarter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
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
}
