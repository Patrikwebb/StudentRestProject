
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBStudent implements StudentStorage{
	
    private static DBStudent storage;
    private StudentDatabase studentDB;
    
    // Return storage / service
    public static DBStudent getInstance() {
		if(storage == null){
			storage = new DBStudent();
		}
		return storage;
	}
    
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	@Override
	public List<Student> getStudents() {
		List<Student> studentList = new ArrayList<>();
		try {
			conn = DBConnection.open();
			stmt = conn.prepareStatement(StudentDatabase.GET_ALL_STUDENTS);

			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("student_id");
				String name = rs.getString("student_name");
				Student student = new Student(id, name);
				studentList.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return studentList;
	}


	@Override
	public Student getCourses(int id) {
		List<String> coursesList = new ArrayList<>();
		Student student = null; 
		String name = null;
		try {
			conn = DBConnection.open();
			stmt = conn.prepareStatement(StudentDatabase.GET_STUDENTS_COURSES);
			stmt.setString(1, Integer.toString(id));
			rs = stmt.executeQuery();
			while (rs.next()) {
				name  = rs.getString("student_name");
				String course = rs.getString("courses_code");
				coursesList.add(course);
			}
			student = new Student(id, name);
			student.addCourses(coursesList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student;
	}

	//TODO (Check client for missing metods)
	@Override
	public void clearAllStudents() {
		// TODO Auto-generated method stub
		
	}
	
	
}
