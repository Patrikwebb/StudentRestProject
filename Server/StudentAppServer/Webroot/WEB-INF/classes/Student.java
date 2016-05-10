
import java.util.List;

public class Student {
	
	private String studentName;
	private int studentId;
	private String stringId;
	private List<String> courses;
	
	public List<String> getCourses() { return courses; }
	
	public void addCourses(List<String> course) { this.courses = course; }

	public Student(int id, String name) {
		this.studentId = id;
		this.studentName = name;
	}
	
	public String getName() { return studentName; }
	
	public int getId() { return studentId; }
	
	public String getId(String s) { return stringId; }

	@Override
	public String toString() {
		return new StringBuilder()
				.append(studentName)
				.append(" ")
				.append(studentId)
				.append(" ")
				.append(getCourses())
				.toString();
	}
}
