package se.yrgo.java15.patrik.studentapp.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import se.yrgo.java15.patrik.studentapp.activities.MainActivity;
import se.yrgo.java15.patrik.studentapp.model.Student;

public class StudentDatabase extends SQLiteOpenHelper {

    private static final String LOG_TAG =
            MainActivity.class.getName();

    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_NAME = "students";

    static final String STUDENT_ID_COLUMN = "student_id";
    static final String STUDENT_NAME_COLUMN = "student_name";

    private static final String DATABASE_CREATE =
            "CREATE table " + TABLE_NAME + "(" +
                STUDENT_ID_COLUMN + " integer primary key asc NOT NULL, " +
                STUDENT_NAME_COLUMN + " varchar(30) NOT NULL" +
            ");";

    private static final String DATABASE_GET_STUDENT_BY_ID =
            "SELECT " + STUDENT_NAME_COLUMN +
            " FROM " + TABLE_NAME +
            " WHERE " + STUDENT_ID_COLUMN + " = ? " +
            " AND " + STUDENT_NAME_COLUMN + " = " + STUDENT_NAME_COLUMN + ";";

    private static final String DATABASE_GET_ID_FROM_STUDENT =
            "SELECT " + STUDENT_ID_COLUMN +
            " FROM " + TABLE_NAME +
            " WHERE " + STUDENT_ID_COLUMN + " = ? " +
            " AND " + STUDENT_NAME_COLUMN + " = " + STUDENT_NAME_COLUMN + ";";

    private static final String DATABASE_CLEAR =
            "DROP TABLE students;";

    // Constructor
    public StudentDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void clearDatabase(SQLiteDatabase db){
        db.execSQL(DATABASE_CLEAR);

        Log.d(LOG_TAG, "StudentDataBase.clearDatabase -> db.execSQL(DATABASE_CLEAR)");
    }

    public String getStudentById(SQLiteDatabase db, Integer studentid){

        String student = null;
        Cursor cursor = null;

        String [] args = new String[]{studentid.toString()};
        cursor = db.rawQuery(DATABASE_GET_STUDENT_BY_ID, args);
        cursor.moveToFirst();
        student = cursor.getString(0);

        return student;
    }

    public String getIdFromStudent(SQLiteDatabase db, Integer studentid){

        String id = null;
        Cursor cursor = null;

        String [] args = new String[]{studentid.toString()};
        cursor = db.rawQuery(DATABASE_GET_ID_FROM_STUDENT, args);
        cursor.moveToFirst();
        id = cursor.getString(0);

        return id;
    }
}