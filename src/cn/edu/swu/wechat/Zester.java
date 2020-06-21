package cn.edu.swu.wechat;

import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/*
curl -F media=@jinlutech.png "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=34_yNQXILg19o1s28-zQWvBtZiW0LpzRcWeFIZ2sM_mJVgnEvXuwA-6gEZzzsLnXifVItP0xxjmZnBLunoFC4KsR1B1sSrO407B8Rr5PpxwI9ZI0b1ypJG9xmG3PZmwmw8mj7YixSeRAl9Jmmf-VNMeAIADVU&type=image" |jq
{
  "type": "image",
  "media_id": "2U9fjUW-YHQLiI8rOecwvvhYIgTqiK_2MpwJ_ULrv6YCzHitHl4-GEL731Nn9dQ8",
  "created_at": 1592676085,
  "item": []
}


curl -F media=@jinlutech.png "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=34_bS6jcgCJsKU3uSRUrU4UeOqMlwfLdcLLHBTegqr75W3qXMVuLC9okMCzpZjE-rzpXy7dG6HQ5koFEh7HglieotSc04MuAXq7k3u0CzVKBSC3K_-bjZRBEKkDtpsuppmUP5MYIHgNqHBgFvKqACYdAAADFB" | jq
{
  "errcode": 48001,
  "errmsg": "api unauthorized hints: [bAkCBeijRa-MEZNRa!]"
}

*/

public class Zester {

    @Test
    public void batchGetMaterialTest() throws IOException {
        WeChatServiceAgent weChatServiceAgent = new WeChatServiceAgent(WeChatServlet.APP_ID, WeChatServlet.APP_SECRET, WeChatServlet.TOKEN);
        JSONObject result = weChatServiceAgent.batchGetMaterial();
        System.out.println(result.toString());
    }

    @Test
    public void uploadImageTest() throws IOException {
        WeChatServiceAgent weChatServiceAgent = new WeChatServiceAgent(WeChatServlet.APP_ID, WeChatServlet.APP_SECRET, WeChatServlet.TOKEN);
        //JSONObject result = weChatServiceAgent.uploadTempImage("/home/ws/mini.png");
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("pics/mini.png");
        //System.out.println(inputStream);
        JSONObject result = weChatServiceAgent.uploadTempImage(inputStream, "mini.png");
        if (result != null) {
            System.out.println(result.toString());
            String mediaId = result.getString("media_id");
            System.out.println(mediaId);
        } else {
            System.out.println("return null");
        }

    }

}
