import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GuestBookCat")
public class GuestBookCat extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File f = new File(getServletContext().getRealPath("/WEB-INF/Guests.html"));
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		String line;
		while((line = fin.readLine()) != null) {
			out.println(line);
		}
		fin.close();
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}