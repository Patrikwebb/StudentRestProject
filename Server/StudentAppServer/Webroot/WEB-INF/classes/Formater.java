/**
 * Returns: format, Student and Courses <Br />
 *          Depends on request.
 * @author Patrik
 *
 */
public class Formater {
   
    String contentFormat = "text/plain";
 
    public String getDataAsFormat(String format, String id) {
       
        StudentStorage storage = StorageManager.getStorage();
       
        JsonFormat Json;
        XMLFormat XML;
       
        String responsString = null;
        int i = 0;
       
        // If we ask for json format
        if (format.equalsIgnoreCase("json")) {
            format = "application/json";
	    contentFormat = "application/json";
            Json = new JsonFormat();
            // Check if we asked for id
            if (id!=null && !id.isEmpty()) {
                i = Integer.parseInt(id);
                responsString = Json.getCoursesJson(storage.getCourses(i));
            } else {
                // Only return students as Json Format
                responsString = Json.getStudentsJson(storage.getStudents());
            }
        // If we ask for xml format
        } else if (format.equalsIgnoreCase("xml")) {
            format = "application/xml";
	    contentFormat = "application/xml";
            XML = new XMLFormat();
            // Check if we asked for id
            if (id!=null && !id.isEmpty()) {
                i = Integer.parseInt(id);
                responsString = XML.getCourses(storage.getCourses(i));
            } else {
                // Only return students as XML format
                responsString = XML.getStudents(storage.getStudents());
            }
        } else {
            responsString = "Unknown format";
        }
        return responsString;
    }
   
    public String getContent(){
        return contentFormat;
    }
}
