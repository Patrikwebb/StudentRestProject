public class StudentDatabase{
	
	public final static String GET_ALL_STUDENTS = "SELECT * FROM students;";
	
	public final static String GET_STUDENT_BY_ID = "SELECT * FROM students WHERE id = ?";
	
	public final static String GET_STUDENTS_COURSES = 
			"SELECT s.student_name, c.courses_code "
			+ "FROM students s "
			+ "INNER JOIN registations r ON s.student_id = r.student_id "
			+ "INNER JOIN courses c ON c.courses_id = r.courses_id WHERE s.student_id = ? ;";
	
	public final static String GET_ALL = 
			"SELECT s.id, s.name, c.course_code "
			+ "FROM students s INNER JOIN registrations r ON s.id = r.student_id "
			+ "INNER JOIN courses c ON c.id = r.course_id";

	
}
