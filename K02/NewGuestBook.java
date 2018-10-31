// ===========================================================
// 情報環境実験II Information Environment Experiment II
// 2018.10.13 第2回 Servlet
// v1.0 NewGuestBook.java メインソース
// s16t287 檜垣大地
// ===========================================================

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class NewGuestBook extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
		response.sendRedirect("GuestBook.html");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int count;

		// ファイルオブジェクトの新規作成と参照ファイルのロード
		File file = new File(getServletContext().getRealPath("WEB-INF/counter.txt"));
		// BufferedReaderオブジェクトの生成
		BufferedReader fin = null;

		try{
	        fin = new BufferedReader(new FileReader(file));
	        count = Integer.parseInt(fin.readLine());
	    }catch(FileNotFoundException | NullPointerException | NumberFormatException e){
	        count = 0;        
	    }finally{
	        if(fin != null)
	        {
	            fin.close();
	        }
	    }

	    PrintWriter fout = new PrintWriter(new FileWriter(file));
	    fout.println(++count);
	    fout.close();

		request.setCharacterEncoding("UTF-8");

		File tmp = new File(getServletContext().getRealPath("/WEB-INF/tmp.html"));
		PrintWriter tmpOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"));
		File guests   = new File(getServletContext().getRealPath("/WEB-INF/GB/" + String.valueOf(count) +".html"));
		BufferedReader guestsIn = null;
		String line;

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
		if (guestsIn != null) {
			while (true) {
				line = guestsIn.readLine();
				if (line == null) break;
				tmpOut.println(line);  
			}
			guestsIn.close();
		}
		tmpOut.close();
		tmp.renameTo(guests);  // mv WEB-INF/tmp.html WEB-INF/Guests.html
		response.sendRedirect("NewGuestBookCat");
	}
}