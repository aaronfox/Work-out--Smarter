package edu.cse570afox.workoutsmarter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ExerciseDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exercises.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String CREATE_TABLE_EXERCISE =
            "create table exercise (_id integer primary key autoincrement, "
                    + "exercisename text not null,"
                    + "calories integer,"
                    + "reps integer,"
                    + "musclegroupworked text);";

    public ExerciseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_EXERCISE);
        // Also create initial data
        final String ADD_INITIAL_EXERCISE_DATA =
                "INSERT INTO exercise (exercisename, calories, reps, musclegroupworked)"
                + "VALUES('Squat', 150, 5, 'Legs');";
        database.execSQL(ADD_INITIAL_EXERCISE_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ExerciseDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS exercise");
        onCreate(db);
    }
}
