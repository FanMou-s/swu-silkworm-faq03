package cn.edu.swu.silkworm.faq;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;

public class WeixinServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce     = request.getParameter("nonce");
        String echoStr   = request.getParameter("echostr");

        if (WeixinService.checkSignature(timestamp, nonce, signature)) {
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
        String nonce = request.getParameter("nonce");

//        InputStream inputStream = request.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        reader.readLine();
        for (Enumeration<String> names = request.getParameterNames(); names.hasMoreElements(); ) {
            String name = names.nextElement();
            System.out.println("name:" + name);
            System.out.println(request.getParameter(name));
        }

    }



}
