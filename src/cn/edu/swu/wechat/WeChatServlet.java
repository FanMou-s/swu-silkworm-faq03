package cn.edu.swu.wechat;

import cn.edu.swu.silkworm.faq.Question;
import cn.edu.swu.silkworm.faq.SearchEngine;
import cn.edu.swu.silkworm.faq.SearchEngineFactory;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.Enumeration;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class WeChatServlet extends HttpServlet {

    private Logger logger = LogManager.getLogger(WeChatServlet.class);

    @Override
    public void init(ServletConfig config) {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce     = request.getParameter("nonce");
        String echoStr   = request.getParameter("echostr");

        if (WeChatService.checkSignature(timestamp, nonce, signature)) {
            PrintWriter out = response.getWriter();
            out.print(echoStr);
            out.flush();
            out.close();
            System.out.println("微信接入成功!");
        }else {
            System.out.println("微信接入失败!");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce     = request.getParameter("nonce");
        String openid    = request.getParameter("openid");

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            WeChatRequestHandler weChatRequestHandler = new WeChatRequestHandler();
            saxParser.parse(request.getInputStream(), weChatRequestHandler);
            WeChatRequest weChatRequest = weChatRequestHandler.getWeixinRequest();

            System.out.println(".................................");
            System.out.println(weChatRequest.toString());

            List<Question> questions = null;
            switch (weChatRequest.getMsgType()) {
                case TEXT:
                    SearchEngine searchEngine = SearchEngineFactory.getInstance();
                    questions = searchEngine.search(weChatRequest.getContent());
                    break;
                default:
                    questions = null;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("<xml>");
            sb.append("<ToUserName><![CDATA[" + weChatRequest.getFromUserName() + "]]></ToUserName>");
            sb.append("<FromUserName><![CDATA[" + weChatRequest.getToUserName() + "]]></FromUserName>");
            sb.append("<CreateTime>" + (System.currentTimeMillis() / 1000) + "</CreateTime>");
            sb.append("<MsgType><![CDATA[text]]></MsgType>");
            if (questions != null && questions.size() > 0) {
                Question question = questions.get(0);
                logger.info("[" + weChatRequest.getContent() + "], [" + question.getQuestion() + "], [" + question.getAnswer() + "]");
                sb.append("<Content><![CDATA[" + questions.get(0).getAnswer() + "]]></Content>");
            } else {
                logger.info("[" + weChatRequest.getContent() + "], []");
                sb.append("<Content><![CDATA[哈哈，问题把我难住了！]]></Content>");
            }
            sb.append("</xml>");

            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(sb.toString());
            response.flushBuffer();

        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }

        for (Enumeration<String> names = request.getParameterNames(); names.hasMoreElements(); ) {
            String name = names.nextElement();
            System.out.println(name + ": " + request.getParameter(name));
        }
    }
}
