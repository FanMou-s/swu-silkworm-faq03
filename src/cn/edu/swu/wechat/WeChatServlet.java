package cn.edu.swu.wechat;

import cn.edu.swu.silkworm.faq.Question;
import cn.edu.swu.silkworm.faq.SearchEngine;
import cn.edu.swu.silkworm.faq.SearchEngineFactory;
import net.sf.json.JSONObject;
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
import java.util.Random;

import org.apache.logging.log4j.Logger;

/*
curl "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxde34793fe89bf7cf&secret=7eec37ce4d2762e80ccc0e8e21cc48b1" | jq
{
  "access_token":"34_bS6jcgCJsKU3uSRUrU4UeOqMlwfLdcLLHBTegqr75W3qXMVuLC9okMCzpZjE-rzpXy7dG6HQ5koFEh7HglieotSc04MuAXq7k3u0CzVKBSC3K_-bjZRBEKkDtpsuppmUP5MYIHgNqHBgFvKqACYdAAADFB",
  "expires_in":7200
}

*/

public class WeChatServlet extends HttpServlet {
    private final static Logger logger = LogManager.getLogger(WeChatServlet.class);
    public final static String APP_ID = "wxde34793fe89bf7cf";
    public final static String APP_SECRET = "7eec37ce4d2762e80ccc0e8e21cc48b1";
    public final static String TOKEN = "JinLuTechC313";

    private WeChatServiceAgent weChatServiceAgent = null;

    @Override
    public void init(ServletConfig config) {
        this.weChatServiceAgent = new WeChatServiceAgent(APP_ID, APP_SECRET, TOKEN);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce     = request.getParameter("nonce");
        String echoStr   = request.getParameter("echostr");

        if (this.weChatServiceAgent.checkSignature(timestamp, nonce, signature)) {
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
                    System.out.println("Find Answer: " + questions.size());
                    break;
                default:
                    questions = null;
                    System.out.println("Find Answer: " + 0);
            }

            String result = null;
            if (questions != null && questions.size() > 0) {
                result = this.questionToXml(weChatRequest, questions.get(0));
            }

            if (result == null) {
                result = this.buildFailedMessage(weChatRequest);
            }

            System.out.println(result);

            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(result);
            response.flushBuffer();
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }

        /*
        for (Enumeration<String> names = request.getParameterNames(); names.hasMoreElements(); ) {
            String name = names.nextElement();
            System.out.println(name + ": " + request.getParameter(name));
        }
        */
    }

    private String questionToXml(WeChatRequest weChatRequest, Question question) {
        String xml = null;
        System.out.println("Answer Type: " + question.getMediaType());
        if (question.getMediaType().equalsIgnoreCase("text")) {
            xml = this.buildTextMessage(weChatRequest, question);
        } else if (question.getMediaType().equalsIgnoreCase("image")) {
            xml = this.buildImageMessage(weChatRequest, question);
        }
        return xml;
    }

    private String buildFailedMessage(WeChatRequest weChatRequest) {
        logger.info("[" + weChatRequest.getContent() + "], []");

        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[" + weChatRequest.getFromUserName() + "]]></ToUserName>");
        sb.append("<FromUserName><![CDATA[" + weChatRequest.getToUserName() + "]]></FromUserName>");
        sb.append("<CreateTime>" + (System.currentTimeMillis() / 1000) + "</CreateTime>");
        sb.append("<MsgType><![CDATA[text]]></MsgType>");
        sb.append("<Content><![CDATA[哈哈，问题把我难住了！]]></Content>");
        sb.append("</xml>");
        return sb.toString();
    }

    private String buildTextMessage(WeChatRequest weChatRequest, Question question) {
        logger.info("[" + weChatRequest.getContent() + "], [" + question.getQuestion() + "], [" + question.getAnswer() + "]");
        System.out.println("Builder Text Message:");
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[" + weChatRequest.getFromUserName() + "]]></ToUserName>");
        sb.append("<FromUserName><![CDATA[" + weChatRequest.getToUserName() + "]]></FromUserName>");
        sb.append("<CreateTime>" + (System.currentTimeMillis() / 1000) + "</CreateTime>");
        sb.append("<MsgType><![CDATA[text]]></MsgType>");
        sb.append("<Content><![CDATA[" + question.getAnswer() + "]]></Content>");
        sb.append("</xml>");
        return sb.toString();
    }

    private String buildImageMessage(WeChatRequest weChatRequest, Question question) {
        logger.info("[" + weChatRequest.getContent() + "], [" + question.getQuestion() + "], [" + question.getAnswer() + "]");


        String[] imageNames = question.getAnswer().split(",");
        String imageName = imageNames[(new Random()).nextInt(imageNames.length)];

        System.out.println("Builder Image Message: " + imageName);

        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("pics/" + imageName)){
            System.out.println("Image InputStream: " + inputStream.toString());
            JSONObject jsonObject = this.weChatServiceAgent.uploadTempImage(inputStream);
            String mediaId = jsonObject.getString("media_id");
            System.out.println("Media ID: " + jsonObject.toString());

            StringBuilder sb = new StringBuilder();
            sb.append("<xml>");
            sb.append("<ToUserName><![CDATA[" + weChatRequest.getFromUserName() + "]]></ToUserName>");
            sb.append("<FromUserName><![CDATA[" + weChatRequest.getToUserName() + "]]></FromUserName>");
            sb.append("<CreateTime>" + (System.currentTimeMillis() / 1000) + "</CreateTime>");
            sb.append("<MsgType><![CDATA[image]]></MsgType>");
            sb.append("<Image>");
            sb.append("<MediaId><![CDATA[" + mediaId + "]]></MediaId>");
            sb.append("</Image>");
            sb.append("</xml>");
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
