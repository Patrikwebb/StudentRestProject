package se.yrgo.java15.patrik.studentapp.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RegistrationDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_NAME = "registations";

    static final String STUDENT_ID_COLUMN = "student_id";
    static final String COURSES_ID_COLUMN = "courses_id";

    private static final String DATABASE_CREATE =
            "CREATE table " + TABLE_NAME + "(" +
                    STUDENT_ID_COLUMN + " int(8), " +
                    COURSES_ID_COLUMN + " int(8)" +
                    ");";

    // Constructor
    public RegistrationDatabase(Context context) {
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