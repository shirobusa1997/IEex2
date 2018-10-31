// ===========================================================
// 情報環境実験II Information Environment Experiment II
// 2018.10.16 第1回 Servlet
// v1.0 CounterExt.java メインソース
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

public class CounterExt extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException{

    // 変数宣言
    int count;

    // [HTML]コンテンツタイプ・エンコード設定
    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();

    // [HTML]必須タグ出力
    out.println("<html><head></head><body>");

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

    out.printf("<font ");
    if(count % 2 == 0)
    {
        out.printf("color = 'red' ");
        if(count % 10 == 0)
        {
            out.printf("size = 10");
        }
    }
    out.printf(">");

    out.printf("%d", count);

    out.printf("</font>番目の来訪者です。%n");
    out.println("</body></html>");
    out.close();
  }
}