package cn.edu.swu.silkworm.faq;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class Tester {

    @Test
    public void searchTest() throws IOException {
        String queryString = "蚕卵害怕光照吗";
        queryString = "蚕丝的两大主要成分？";
        queryString = "蚕丝的成分？";
        queryString = "蚕丝的组成？";

        queryString = "雌雄蚕的鉴别?";
        queryString = "蚕怎么分公母?";
        queryString = "蚕怎么分公母?";


        queryString = "化性是什么？";
        queryString = "蚕宝宝的化性是什么？";
        queryString = "蚕宝宝的化性";

        queryString = "3龄蚕有多大？";
        queryString = "3龄蚕好长？";
        queryString = "3龄蚕的长度";
        queryString = "蚕的长度";

        SearchEngine searchEngine = new SearchEngine();
        searchEngine.startup();
        List<Question> questions = searchEngine.search(queryString);
        System.out.println("Search : " + queryString);
        for (Question question : questions) {
            System.out.println("Result : " + question.toString());
        }
    }

    private void logger(String msg) {
        System.out.println(msg);
    }

}
