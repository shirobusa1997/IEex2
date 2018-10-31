// ===========================================================
// 情報環境実験II Information Environment Experiment II
// 2018.10.13 第3回 Servlet
// v1.0 AccessTime.java メインソース
// s16t287 檜垣大地
// ===========================================================

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class AccessTime extends HttpServlet{
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