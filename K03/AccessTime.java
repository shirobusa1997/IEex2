// ===========================================================
// 情報環境実験II Information Environment Experiment II
// 2018.10.30 第3回 Servlet
// v1.0 AccessTime.java メインソース
// s16t287 檜垣大地
// ===========================================================

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class AccessTime extends HttpServlet {
	private static final String COUNTER = "counter";

	@Override 	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		Calendar now = Calendar.getInstance();

		HttpSession session = request.getSession(true);
		String date = "null";
		try {
			date = session.getAttribute(COUNTER).toString();
		} catch (NullPointerException | NumberFormatException e) { /* i = 0 のまま */ }
		PrintWriter out = response.getWriter();
		out.println("<html><head></head><body>");

		if(date == "null"){
			out.printf("初めてのアクセスです。");
		}else{
			out.printf("前回は、%s にアクセスしました。", date);
		}
		out.println("</body></html>");

		SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH時mm分ss秒");
		date = format.format(now.getTime());
		session.setAttribute(COUNTER, date);
		out.close();    // closeを忘れない
	}
}