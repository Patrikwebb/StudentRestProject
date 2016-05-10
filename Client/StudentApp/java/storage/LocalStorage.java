package se.yrgo.java15.patrik.studentapp.storage;

import android.database.Cursor;

import java.util.List;

import se.yrgo.java15.patrik.studentapp.model.Student;

public interface LocalStorage {

    String BASE_URL = "http://localhost:8081/StudentAppServer/StudentServlet";

    public List<Student> getStudents();

    public String getStudentById(Integer studentId);

    public void addStudent(Student student); // Throws Storage Exception

    public String getIdFromStudent(Integer studentId);

    public void clearAllStudents();

}
