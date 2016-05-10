package se.yrgo.java15.patrik.studentapp.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Courses {

    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("student_id")
    @Expose
    private Integer studentId;
    @SerializedName("courses")
    @Expose
    private List<Course> courses = new ArrayList<Course>();

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

}