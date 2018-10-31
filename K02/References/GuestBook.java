import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/GuestBook")
public class GuestBook extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
		response.sendRedirect("GuestBook.html");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		File tmp = new File(getServletContext().getRealPath("/WEB-INF/tmp.html"));
		PrintWriter tmpOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"));
		File guests   = new File(getServletContext().getRealPath("/WEB-INF/Guests.html"));
		BufferedReader guestsIn = null;
		String line;
		try {
			guestsIn = new BufferedReader(new InputStreamReader(new FileInputStream(guests), "UTF-8"));
			while (true) {
				line = guestsIn.readLine();
				if (line == null) {
					line = String.format("</body>%n</html>");
					break;
				}
				if (line.contains("</body>")) break;
				tmpOut.println(line);  
			}
		} catch (FileNotFoundException e) { // /WEB-INF/Guests.html が存在しない
		tmpOut.println("<html><head><meta charset=\"UTF-8\"><title>ゲストブック</title></head><body>");
		tmpOut.println("<h1 align=\"center\">ゲストブック</h1>御記帳有難うございました。<br /><a href=\"GuestBook.html\">戻る</a><hr />");
		line = String.format("</body>%n</html>");
		tmpOut.println("<table border='1'>");
		tmpOut.printf("<tr><td>名前</td><td>%s</td></tr>%n",
				request.getParameter("名前").replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
		tmpOut.printf("<tr><td>メールアドレス</td><td>%s</td></tr>%n", 
				request.getParameter("メールアドレス").replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
		tmpOut.printf("<tr><td>ホームページ</td><td>%s</td></tr>%n", 
				request.getParameter("ホームページ").replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
		tmpOut.printf("<tr><td>ひとこと</td><td>%s</td></tr>%n", 
				request.getParameter("ひとこと").replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));      
		tmpOut.println("</table>");
		tmpOut.println("<hr />");
		tmpOut.println(line);
		if (guestsIn != null) {
			while (true) {
				line = guestsIn.readLine();
				if (line == null) break;
				tmpOut.println(line);  
			}
			guestsIn.close();
		}
		tmpOut.close();
		guests.delete();       // rm WEB-INF/Guests.html 
		tmp.renameTo(guests);  // mv WEB-INF/tmp.html WEB-INF/Guests.html
		response.sendRedirect("GuestBookCat");
	}
}