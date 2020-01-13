package cn.edu.swu.silkworm.faq;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SearchServlet extends HttpServlet {

    private static SearchEngine searchEngine = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String queryString = req.getParameter("question");
        List<Question> questions =  this.getSearchEngine().search(queryString);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"result\": [");
        for (int i=0; i<questions.size(); i++) {
            Question question = questions.get(i);
            if (i > 0 ) sb.append(",");
            sb.append("{")
                    .append("\"question\":").append("\"" + question.getQuestion() + "\",")
                    .append("\"answer\":").append("\"" + question.getAnswer() + "\"}");
        }
        sb.append("]}");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(sb.toString());
        resp.flushBuffer();
    }

    private SearchEngine getSearchEngine() {
        if (searchEngine == null) {
            synchronized (this) {
                if (searchEngine != null) {
                    return searchEngine;
                }

                searchEngine = new SearchEngine();
                try {
                    searchEngine.startup();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return searchEngine;
    }
}
