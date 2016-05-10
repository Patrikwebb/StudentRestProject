
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLFormat {
	
	//TODO Check for XML Converter Factory
	public String getStudents(List<Student> studentList) {
		
		String studentsXML = null;
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement("STUDENTS");
			doc.appendChild(rootElement);

			for (Student student : studentList) {
				Element studentElemnt = doc.createElement("STUDENT");
				studentElemnt.setAttribute("id", Integer.toString(student.getId()));
				
				Element name = doc.createElement("NAME");
				name.appendChild(doc.createTextNode(student.getName()));
				studentElemnt.appendChild(name);
				rootElement.appendChild(studentElemnt);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);

			StringWriter stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);
			transformer.transform(source, result);
			
			studentsXML = result.getWriter().toString();
			System.out.println("XMLFormat.getStudents: " + result.getWriter().toString());

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		return studentsXML;
	}
	
	public String getCourses(Student student) {
		
		String coursesXML = null;
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("COURSES");
			rootElement.setAttribute("student_id", Integer.toString(student.getId()));
			rootElement.setAttribute("student_name", student.getName());
			doc.appendChild(rootElement);

			for (String students : student.getCourses()) {
				Element course = doc.createElement("COURSE");
				course.appendChild(doc.createTextNode(students));
				rootElement.appendChild(course);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);

			StringWriter stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);
			transformer.transform(source, result);
			coursesXML = result.getWriter().toString();
			System.out.println("XMLFormat.getCourses: " + result.getWriter().toString());

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		return coursesXML;
	}
}
