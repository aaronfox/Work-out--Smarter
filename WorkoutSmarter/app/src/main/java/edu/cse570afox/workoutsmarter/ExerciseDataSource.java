package edu.cse570afox.workoutsmarter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
// TODO: Continue where left off on page 14 of Lecture 5 part 2
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
}
