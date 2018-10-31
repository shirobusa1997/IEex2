// ===========================================================
// 情報環境実験II Information Environment Experiment II
// 2018.10.13 第2回 Servlet
// v1.0 NewGuestBookCat.java メインソース
// s16t287 檜垣大地
// ===========================================================

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class NewGuestBookCat extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

		File f = null;

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<html><head><meta charset=\"UTF-8\"><title>ゲストブック</title></head><body>");
		out.println("<h1 align=\"center\">ゲストブック</h1>御記帳有難うございました。<br /><a href=\"GuestBook.html\">戻る</a><hr />");

		String line;

		for(int i = 1; i <= count; i++){

			out.printf("%d.", i);

			f = new File(getServletContext().getRealPath("/WEB-INF/GB/" + String.valueOf(i) +".html"));
			fin = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

			while((line = fin.readLine()) != null) {
				out.println(line);
			}

			out.println("<br><br>");

		}

		out.println("</body><br></html>");

		fin.close();
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}