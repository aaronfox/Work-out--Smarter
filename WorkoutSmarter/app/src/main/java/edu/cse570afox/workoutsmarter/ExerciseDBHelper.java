package edu.cse570afox.workoutsmarter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ExerciseDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exercises.db";
    private static final int DATABASE_VERSION = 3;

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
                + "VALUES('Squats', 150, 5, 'Legs'),"
                + "('Goblet Squats', 50, 8, 'Legs'),"
                + "('Leg Presses', 130, 6, 'Legs'),"
                + "('Hack Squats', 150, 7, 'Legs'),"
                + "('Calf Raises', 30, 8, 'Legs'),"
                + "('Flat Bench Presses', 100, 6, 'Chest'),"
                + "('Incline Bench Presses', 110, 6, 'Chest'),"
                + "('Dumbell Flys', 75, 8, 'Chest'),"
                + "('Dips', 45, 8, 'Chest'),"
                + "('Cable Flys', 150, 10, 'Chest'),"
                + "('Cable Crunches', 50, 10, 'Abs'),"
                + "('Flutter Kicks', 75, 8, 'Abs'),"
                + "('Bicycle Kicks', 85, 10, 'Abs'),"
                + "('Ab Wheel', 100, 10, 'Abs'),"
                + "('Dragon Flags', 45, 3, 'Abs'),"
                + "('Pull Ups', 150, 10, 'Back'),"
                + "('Barbell Rows', 65, 6, 'Back'),"
                + "('Cable Rows', 50, 8, 'Back'),"
                + "('Lat Pull Downs', 60, 10, 'Back'),"
                + "('T-Bar Rows', 35, 8, 'Back'),"
                + "('Shrugs', 60, 10, 'Shoulders'),"
                + "('Overhead Press', 50, 6, 'Shoulders'),"
                + "('Lateral Raises', 25, 12, 'Shoulders'),"
                + "('Upright Rows', 35, 8, 'Shoulders'),"
                + "('Rear Delt Flys', 45, 8, 'Shoulders'),"
                + "('Bicep Curls', 85, 6, 'Arms'),"
                + "('Skullcrushers', 70, 8, 'Arms'),"
                + "('Tricep Pulldowns', 50, 12, 'Arms'),"
                + "('Close Grip Bench Presses', 50, 6, 'Arms'),"
                + "('Poundstone Curls', 50, 6, 'Arms'),"
                + "('Deadlifts', 260, 4, 'Legs');";
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
