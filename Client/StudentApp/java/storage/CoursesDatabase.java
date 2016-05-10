package se.yrgo.java15.patrik.studentapp.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CoursesDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_NAME = "courses";

    static final String COURSES_ID_COLUMN = "courses_id";
    static final String COURSES_CODE_COLUMN = "courses_code";

    private static final String DATABASE_CREATE =
            "CREATE table " + TABLE_NAME + "(" +
                    COURSES_ID_COLUMN + " integer primary key asc, " +
                    COURSES_CODE_COLUMN + " varchar(12)" +
                    ");";

    // Constructor
    public CoursesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}