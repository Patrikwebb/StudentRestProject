package se.yrgo.java15.patrik.studentapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import se.yrgo.java15.patrik.studentapp.model.Student;

@Generated("org.jsonschema2pojo")
public class DBLocal implements LocalStorage {

    private static final String LOG_TAG =
            DBLocal.class.getName();

    private static DBLocal storage;
    private StudentDatabase studentDB;

    private SQLiteDatabase database;

    // Return storage / service
    public static DBLocal getInstance(Context context){
        if(storage==null){
            storage = new DBLocal(context);
        }
        return storage;
    }

    // Constructor returns DB
    private DBLocal(Context context){
        studentDB = new StudentDatabase(context);
        database = studentDB.getWritableDatabase();
    }

    private String[] columns = {
            StudentDatabase.STUDENT_ID_COLUMN,
            StudentDatabase.STUDENT_NAME_COLUMN
    };

    @Override
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<Student>();

        Cursor cursor = database.query(
                StudentDatabase.TABLE_NAME,
                columns, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){

            Student student = new Student(cursor.getInt(0),cursor.getString(1));
            // Add to my List<Student> students
            students.add(student);
            cursor.moveToNext();
        }
        cursor.close();

        // Return our List of students
        return students;
    }

    @Override
    public void addStudent(Student student) {

        ContentValues values = new ContentValues();

        values.put(StudentDatabase.STUDENT_ID_COLUMN, student.getStudentID());
        values.put(StudentDatabase.STUDENT_NAME_COLUMN, student.getStudentName());

        database.insert(StudentDatabase.TABLE_NAME, null, values);

    } // Throws new Storage Exception (SQL Exception)

    @Override
    public void clearAllStudents() {
        studentDB.clearDatabase(database);

        // Recreate table structure
        studentDB.onCreate(database);
    }

    @Override
    public String getStudentById(Integer studentId) {

       return studentDB.getStudentById(database, studentId);
    }

    @Override
    public String getIdFromStudent(Integer studentId) {

        return studentDB.getIdFromStudent(database, studentId);
    }
}
