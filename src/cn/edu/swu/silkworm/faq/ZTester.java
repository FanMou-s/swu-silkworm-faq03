package cn.edu.swu.silkworm.faq;

import cn.edu.swu.wechat.MessageType;
import cn.edu.swu.wechat.WeChatRequest;
import cn.edu.swu.wechat.WeChatRequestHandler;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKTokenizer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
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
        queryString = "蚕蛾图片";

        SearchEngine searchEngine = SearchEngineFactory.getInstance();
        List<Question> questions = searchEngine.search(queryString);
        System.out.println("Search : " + queryString);
        for (Question question : questions) {
            System.out.println(question.getMediaType());
            //System.out.println("Result : " + question.toString());
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


    @Test
    public void xmlParse() throws ParserConfigurationException, SAXException, IOException {
        String strXML = "<xml><ToUserName><![CDATA[gh_99f283298ae1]]></ToUserName><FromUserName><![CDATA[oLLBW0XYtUuoz9UpmSixw0LEBfVI]]></FromUserName><CreateTime>1592577377</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[蚕茧]]></Content><MsgId>22800227607571304</MsgId></xml>";

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        WeChatRequestHandler weixinRequestHandler = new WeChatRequestHandler();
        saxParser.parse(new ByteArrayInputStream(strXML.getBytes()), weixinRequestHandler);

        WeChatRequest weixinRequest = weixinRequestHandler.getWeixinRequest();

        System.out.println(weixinRequest.toString());
    }

    @Test
    public void messageType() {
        System.out.println(MessageType.valueOf("video".toUpperCase()));
    }

    private void logger(String msg) {
        System.out.println(msg);
    }

}
