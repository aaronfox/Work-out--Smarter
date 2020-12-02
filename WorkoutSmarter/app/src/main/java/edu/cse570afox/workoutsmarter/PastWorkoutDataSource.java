package edu.cse570afox.workoutsmarter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

public class PastWorkoutDataSource {
    private SQLiteDatabase database;
    private PastWorkoutDBHelper dbHelper;
    private static final String TAG = "PastWorkoutDataSource";

    public PastWorkoutDataSource(Context context) {
        dbHelper = new PastWorkoutDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertPastWorkout(PastWorkout pw) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("pastworkoutname", pw.getWorkoutName());
            initialValues.put("dateOfWorkout", pw.getDateOfWorkout());
            initialValues.put("caloriesburned", pw.getCaloriesBurned());

            didSucceed = database.insert("pastworkout", null, initialValues) > 0;
        }
        catch (Exception e) {
            Log.w(TAG, "!!!Unsuccessful insert into pastworkout: " + e.toString());
        }
        return didSucceed;
    }

    public boolean updatePastWorkout(PastWorkout pw) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) pw.getPastWorkoutID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("pastworkoutname", pw.getWorkoutName());
            updateValues.put("dateOfWorkout", pw.getDateOfWorkout());
            updateValues.put("caloriesburned", pw.getCaloriesBurned());

            didSucceed = database.update("pastworkout", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            Log.w(TAG, "!!!Unsuccessful update of pastworkout: " + e.toString());

        }
        return didSucceed;
    }

    public int getLastPastWorkoutID() {
        int lastID = -1;
        try {
            String query = "Select MAX(_id) from pastworkout";
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
