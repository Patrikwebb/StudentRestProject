package se.yrgo.java15.patrik.studentapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Student {

    //@SerializedName("studentID") @Expose private Integer studentID;
    //@SerializedName("studentName") @Expose private String studentName;

    private Integer studentID;

    private String studentName;

    // Constructor
    public Student(Integer studentID, String studentName){
        this.studentID = studentID;
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    @Override
    public String toString() {
        return studentID + ". " + studentName;
    }

//    @Override
//    public String toString() {
//        return "Student{" +
//                "studentID=" + studentID +
//                ", studentName='" + studentName + '\'' +
//                '}';
//    }

}
