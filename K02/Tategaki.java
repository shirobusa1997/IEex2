// ===========================================================
// 情報環境実験II Information Environment Experiment II
// 2018.10.13 第2回 Servlet
// v1.0 Tategaki.java メインソース
// s16t287 檜垣大地
// ===========================================================

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class Tategaki extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.sendRedirect("Tategaki.html");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		out.println("<html><head></head><body>");

		request.setCharacterEncoding("UTF-8");

		String ud_ref = request.getParameter("ud");

		out.println("<table border='1'>");

		for(String character : ud_ref.split("")){
			out.printf("<tr><td>%s</td></tr>%n", character);
		}

		out.println("</table>");

		out.println("</body></html>");
		out.close();

	}
}