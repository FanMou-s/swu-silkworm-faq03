package cn.edu.swu.wechat;

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

public class WeChatServlet extends HttpServlet {

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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce     = request.getParameter("nonce");
        String openid    = request.getParameter("openid");

//        InputStream inputStream = request.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        StringBuilder sb = new StringBuilder();
//        String line = null;
//        while((line = reader.readLine()) != null) {
//            sb.append(line);
//            sb.append("\n");
//        }
//        System.out.println(sb.toString());


        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            WeChatRequestHandler weixinRequestHandler = new WeChatRequestHandler();
            saxParser.parse(request.getInputStream(), weixinRequestHandler);
            WeChatRequest weixinRequest = weixinRequestHandler.getWeixinRequest();
            System.out.println(weixinRequest.toString());
        } catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }


        for (Enumeration<String> names = request.getParameterNames(); names.hasMoreElements(); ) {
            String name = names.nextElement();
            System.out.println(name + ": " + request.getParameter(name));
        }
    }



}
