// ===========================================================
// 情報環境実験II Information Environment Experiment II
// 2018.10.30 第3回 Servlet
// v1.0 QuizEx.java メインソース
// s16t287 檜垣大地
// ===========================================================

import java.io.*;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

class Quiz_ud{
	int 	choiceNum;		// 選択肢番号
	boolean correction;		// 正誤判定
}

public class QuizEx extends HttpServlet{
	private static final String ANSWER    = "answer";
	private static final String SCORE     = "score";
	private static final String NUMBER    = "number";
	private static final String QUESTIONS = "questions";
	private static final String USERDATA  = "userDataIndex";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 最初の問は GET
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<html><head></head><body>");

		int i, number = 0, score = 0;
		ArrayList<Quiz_ud> userDataIndex;
		ArrayList<String[]> questions;

		HttpSession session = request.getSession(true);

		if(session.isNew() || session.getAttribute(QUESTIONS) == null){
			// 最初の出題
			questions = new ArrayList<String[]>();
			userDataIndex = new ArrayList<Quiz_ud>();

			File f = new File(getServletContext().getRealPath("WEB-INF/quiz.txt"));
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			String line="";
			while((line = in.readLine()) != null) {
				line = line.trim();
				if(line.trim().equals(""))
					continue;
				questions.add(line.split("\\s+")); // 空白の１つ以上の繰返し
			}
			in.close();
			session.setAttribute(QUESTIONS, questions);
			session.setAttribute(USERDATA, userDataIndex);
			out.println("<p>ようこそ KU_ENIEクイズへ!<br />では最初の問題です。</p>");
		}else{
			// 2問目以降の出題
			try {
				number = (int)session.getAttribute(NUMBER);
				score  = (int)session.getAttribute(SCORE);
				questions = (ArrayList<String[]>)session.getAttribute(QUESTIONS);
				userDataIndex = (ArrayList<Quiz_ud>)session.getAttribute(USERDATA);

				String[] tokens = questions.get(number - 1);
				int a = Integer.parseInt(tokens[tokens.length - 1]);
				int answer = Integer.parseInt(request.getParameter(ANSWER));
				if (a == answer) { // aは最後の文字
					out.println("正解です。<br />");
					userDataIndex.add(new Quiz_ud());
					userDataIndex.get(number-1).choiceNum = answer;
					userDataIndex.get(number-1).correction = true;
					score++;
				} else {
					out.println("残念でした。<br />");
					userDataIndex.add(new Quiz_ud());
					userDataIndex.get(number-1).choiceNum = answer;
					userDataIndex.get(number-1).correction = false;
				}
			} catch (Exception e) {
				session.removeAttribute(QUESTIONS);
				out.println("想定外のアクセスでエラーが起こりました。タブを閉じるかリロードしてください。");
				e.printStackTrace(out);
				out.println("</body></html>");
				out.close();
				return;
			}
		}

		if (number >= questions.size()) { // 終
			out.println("<br />これで QUIZは終わりです。<br />");
			out.printf("正解数は、%d問でした。<br><br>", score);
			
			String[] tokens;
			Quiz_ud  temp_Quiz_ud;
			out.println("<table border ='1'><tr><th>問題文</th><th>選択</th><th>回答</th><th>正誤</th></tr>");
			for(int count = 0; count < questions.size(); count++){
				tokens = questions.get(count);
				temp_Quiz_ud = userDataIndex.get(count);
				out.printf("<td>" + tokens[0] + "</td><td>");
				out.printf("%d:%s", temp_Quiz_ud.choiceNum, tokens[temp_Quiz_ud.choiceNum]);
				out.printf("</td><td>%d:%s", Integer.parseInt(tokens[tokens.length - 1]), tokens[Integer.parseInt(tokens[tokens.length - 1])]);
				out.printf("</td><td>%s</td></tr>", String.valueOf(temp_Quiz_ud.correction));
			}

			out.println("</table>");

			session.removeAttribute(QUESTIONS);
		} else { // 次の問を表示
			String[] tokens = questions.get(number);
			out.println("次の問: " + tokens[0] + "<br />");
			out.println("<form method='post'>");
			for (i = 0; i < tokens.length - 2; i++) {
				out.print("<input type='radio' name='answer'");
				out.printf(" value='%d'　/> %s", i + 1, tokens[i  + 1]);
			}
			out.println("<br />");
			out.println("<input type='submit' value='送信' />");
			out.println("<input type='reset' value='やめ' />");
			out.println("</form>");

			number++;
			session.setAttribute(USERDATA, userDataIndex);
			session.setAttribute(NUMBER, number);
			session.setAttribute(SCORE, score);
		}
		out.println("</body></html>");
		out.close();
	}

}