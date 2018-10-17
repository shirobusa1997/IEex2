import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Counter")
public class Counter extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<html><head></head><body>");
		int i; 

		File f = new File(getServletContext().getRealPath("/WEB-INF/counter.txt"));
		BufferedReader fin = null;
		try {
			fin = new BufferedReader(new FileReader(f));
			i =  Integer.parseInt(fin.readLine());
		} catch (FileNotFoundException        // ファイルがなければ 
			 | NullPointerException       // ファイルが空なら 
			 | NumberFormatException e) { // 数でないならば
		    i = 0;  // 0に
		} finally {
			if (fin != null) {
				fin.close();  // closeを忘れない
			}
		}

		PrintWriter fout = new PrintWriter(new FileWriter(f));
		fout.println(++i);
		fout.close(); // closeを忘れない

		out.printf("あなたは %d番目の来訪者です。%n", i);
		out.println("</body></html>");
		out.close();    // closeを忘れない
	}
}