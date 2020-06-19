package cn.edu.swu.silkworm.faq;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WeixinServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echoStr = request.getParameter("echostr");
        System.err.println(signature);
        System.err.println(timestamp);
        System.err.println(nonce);
        System.err.println(echoStr);
        if(WeixinService.checkSignature(timestamp, nonce, signature)) {
            PrintWriter out = response.getWriter();
            out.print(echoStr);
            out.flush();
            out.close();
            System.out.println("接入成功!");
        }else {
            System.out.println("接入失败!");
        }
    }

}
