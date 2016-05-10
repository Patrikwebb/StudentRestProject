
import java.util.List;

public interface StudentStorage {
	
    public List<Student> getStudents();
    
    public Student getCourses(int id);
    
    public void clearAllStudents();
    
}
