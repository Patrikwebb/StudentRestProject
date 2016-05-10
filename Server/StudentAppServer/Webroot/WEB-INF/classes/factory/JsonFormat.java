
import java.util.List;

public class JsonFormat {
	
	//TODO Check for Json Converter Factory
	public String getCoursesJson(Student student) {
		
		String jsonOutput = null;
		StringBuilder json = new StringBuilder();
		
		json.append("{ ");
		for (String courses : student.getCourses()) {			
			json.append("\"student_name\"")
				.append(": \"")
				.append(student.getName() + "\",")
				.append("\"student_id\"")
				.append(": " + student.getId() + ", ")
				.append("\"courses\": [ ")
					.append("{")
						.append("\"course_code\"")
						.append(": \"")
						.append(student.getCourses())
						.append("\"")
						.append(" }");
			if (!courses.equals(student.getCourses().get(student.getCourses().size() - 1))) {
				json.append("}")
					.append(",");
			}
		} // END OF forEach
		
		// Error message (No data)
		if(student.getCourses().isEmpty()){
			json.append("\"courses\": [ ")
					.append("{ ")
					.append("\"course_code\"")
						.append(": \"")
						.append("No courses added")
						.append("\"")
					.append(" }");
		}
		json.append(" ] }");
		
		jsonOutput = json.toString();
		return jsonOutput;
	}

	public String getStudentsJson(List<Student> studentList) {
		
		String jsonOutput = null;
		
		StringBuilder json = new StringBuilder();
		
		json.append(
				"{")
			.append("\"students\": [")
			.append(" { ");
		for (Student student : studentList) {
			json.append("\"student_id\"")
				.append(": ")
				.append(student.getId())
				.append(",")
				.append("\"student_name\"")
				.append(": \"")
				.append(student.getName())
				.append("\"");
				if(!student.equals(studentList.get(studentList.size() -1))){
					json.append("}, {");
				}
		}
		json.append("} ] } ");
		
		jsonOutput = json.toString();
	return jsonOutput;
	}
}
