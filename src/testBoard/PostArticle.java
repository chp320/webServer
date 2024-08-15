package testBoard;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class PostArticle extends HttpServlet implements Serializable {

    private static final long serialVersionUID = -8500502476594135406L;

    /**
     * POST method 처리 메서드
     * @param request
     * @param response
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        request.setCharacterEncoding("UTF-8");
        Article newArticle = new Article(request.getParameter("title"), request.getParameter("handle"), request.getParameter("messge"));
        Article.articleList.add(0, newArticle);

        // request 의 key로 정의된 값을 articleList 에 저장 후 response 를 위해 ReadArticle 로 redirect
        // $TOMCAT_HOME/webapps/testBoard 디렉토리 생성하였고, 이 디렉토리가 web application name 이 된다. ($TOMCAT_HOME/webapps 가 톰캣의 root doc 영역이 됨)
        response.sendRedirect("/testBoard/ReadArticle");
    }
}
