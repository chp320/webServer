package testBoard;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

public class ReadArticle extends HttpServlet implements Serializable {

    private static final long serialVersionUID = 828809197589395452L;

    // HTML 에서 키워드로 사용되는 문자를 이스케이프 처리하는 유틸리티 메서드
    private String escapeHtml(String src) {
        return src.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }

    /**
     * GET method 처리하는 메서드
     * - response 에 Article 객체에 담긴 데이터 개수만큼 내용을 반복 출력
     * @param req
     * @param res
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>테스트용 게시판</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>테스트용 게시판</h1>");
        out.println("<form action='/testbbs/PostBBS' method='post'>");
        out.println("제목: <input type='text' name='title' size='60'>" + "<br>");
        out.println("작성자: <input type='text' name='handle'><br>");
        out.println("<textarea name='message' rows='4' cols='60'>" + "</textarea>" + "<br>");
        out.println("<input type='submit'");
        out.println("</form>");
        out.println("<hr>");

        for(Article article : Article.articleList) {
            out.println("<p>[" + escapeHtml(article.title) + "]&nbsp;&nbsp;"
                        + escapeHtml(article.handle) + " 님&nbsp;&nbsp;"
                        + escapeHtml(article.date.toString()) + "</p>");
            out.println("<p>");
            out.println(escapeHtml(article.message).replace("\r\n", "<br>"));
            out.println("</p>");
        }
        out.println("</body>");
        out.println("</html>");
    }
}
