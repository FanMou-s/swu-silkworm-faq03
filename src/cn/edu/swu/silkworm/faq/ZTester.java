package cn.edu.swu.silkworm.faq;

import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKTokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class ZTester {

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

    @Test
    public void tokenizerTest() {
        String queryString = "蚕宝宝病了";
        StringBuffer sb = new StringBuffer();
        IKTokenizer tokenizer = new IKTokenizer(new StringReader(queryString), false);
        try {
            while (tokenizer.incrementToken()) {
                TermAttribute termAtt = tokenizer.getAttribute(TermAttribute.class);
                sb.append(termAtt.term()).append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger(sb.toString());
    }

    private void logger(String msg) {
        System.out.println(msg);
    }

}
