// ===========================================================
// 情報環境実験II Information Environment Expression II
// 2018.10.16 第1回 Servlet
// v1.0 ImageCounter.java メインソース
// s16t287 檜垣大地
// ===========================================================

// ===========================================================
// 事前処理
// ===========================================================

// -----------------------------------------------------------
// 参照パッケージ指定
// -----------------------------------------------------------

// java.io
import java.io.*;

// javax.servlet
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class ImageCounter extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		// ローカル変数宣言
		int count;

		// [HTML]コンテンツタイプ・エンコード設定
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		// [HTML]必須タグ出力
		out.println("<html><head></head><body><center>");

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

	    out.printf("あなたは ");

	    out.println("<img src='images/0.png' alt = '0'>");
	    out.println("<img src='images/1.png' alt = '1'>");
	    out.println("<img src='images/2.png' alt = '2'>");
	    out.println("<img src='images/3.png' alt = '3'>");
	    out.println("<img src='images/4.png' alt = '4'>");
	    out.println("<img src='images/5.png' alt = '5'>");
	    out.println("<img src='images/6.png' alt = '6'>");
	    out.println("<img src='images/7.png' alt = '7'>");
	    out.println("<img src='images/8.png' alt = '8'>");
	    out.println("<img src='images/9.png' alt = '9'>");

	    out.printf("%d番目の来訪者です。%n", count);
	    out.println("</body></html>");
	    out.close();
	}
}