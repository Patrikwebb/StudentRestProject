import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/StudentAPI")
public class StudentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String format = "";
	private String id = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if(format != null || format != ""){

			Formater formaterFactory = new Formater();
			PrintWriter out = response.getWriter();

			format = request.getParameter("format");
			id = request.getParameter("id");
			
			out.print(formaterFactory.getDataAsFormat(format, id));
			response.setContentType(formaterFactory.getContent());

			// Print out the client ip and request for registration data
			System.out.println("\nIncomming request: \n");
			System.out.println("getRemoteAddr(): " + request.getRemoteAddr());
			//TODO Check getHeader(info);
			System.out.println("Formats request: " + formaterFactory.getContent());

			out.close();

		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
