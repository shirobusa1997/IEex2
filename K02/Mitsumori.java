// ===========================================================
// 情報環境実験II Information Environment Experiment II
// 2018.10.13 第2回 Servlet
// v1.0 Mitsumori.java メインソース
// s16t287 檜垣大地
// ===========================================================

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class Mitsumori extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.sendRedirect("Mitsumori.html");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();

		out.println("<html><head></head><body><center>");

		request.setCharacterEncoding("UTF-8");

		int ud_bd = Integer.parseInt(request.getParameter("bd"));
		int ud_ink = Integer.parseInt(request.getParameter("ink"));
		int ud_paper = Integer.parseInt(request.getParameter("paper"));
		int ud_bd_m = ud_bd * 500;
		int ud_ink_m = ud_ink * 2000;
		int ud_paper_m = ud_paper * 400;

		out.println("<table border='1'>");
		out.println("<tr><th>品名</th><th>単価</th><th>個数</th><th>小計</th></tr>");
		out.printf("<tr><td>BD-Rディスク</td><td>500円</td><td>%d</td><td>%d</td></tr>%n", ud_bd, ud_bd_m);
		out.printf("<tr><td>インクカードリッジ</td><td>4000円</td><td>%d</td><td>%d</td></tr>%n", ud_ink, ud_ink_m);
		out.printf("<tr><td>A4用紙 500枚</td><td>400円</td><td>%d</td><td>%d</td></tr>%n", ud_paper, ud_paper_m);
		out.printf("<tr><td colspan='3'>合計</td><td>%d</td>%n", ud_bd_m + ud_ink_m + ud_paper_m);
		out.println("</table>");

		out.println("</center></body></html>");
		out.close();
	}


}