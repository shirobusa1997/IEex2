import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SessionCounter")
public class SessionCounter extends HttpServlet {
	private static final String COUNTER = "counter";

	@Override 	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		HttpSession session = request.getSession(true);
		int i = 0;
		try {
			i = (int) session.getAttribute(COUNTER);
		} catch (NullPointerException | NumberFormatException e) { /* i = 0 のまま */ }
		PrintWriter out = response.getWriter();
		out.println("<html><head></head><body>");
		out.printf("あなたの %d 回目の御訪問です。", i++);
		out.println("</body></html>");
		session.setAttribute(COUNTER, i);
		out.close();    // closeを忘れない
	}
}