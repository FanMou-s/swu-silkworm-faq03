package cn.edu.swu.wechat;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

public class WeChatServiceAgent {
    private static final String ACCESS_TOKEN_URL   = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String ADD_MATERIAL       = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";
    private static final String BATCH_GET_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";
    private static final String UPLOAD = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    //private static final String UPLOAD = "http://localhost:9999/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    private String appId = null;
    private String appSecret = null;
    private String token = null;
    private AccessToken accessToken = null;

    public WeChatServiceAgent(String appID, String appSecret, String token) {
        this.appId = appID;
        this.appSecret = appSecret;
        this.token = token;

        this.accessToken = new AccessToken(
        "34_B_8E2hZrcDiT5kksVDoMRZMzw3ZriPPCch9ELrznfPZpHv3CQeL14fCDM6YwFtwBQp_jwHm6aO_YVJDCF0hLh5LPReKzuRpmmBaxuKSLF2F4cq9FRuyHW46IdYHTq-1DAM_c8NzBlJwohNBjCSRcAIAVLK",
    7200
        );
    }

    public boolean checkSignature(String timestamp, String nonce, String signature) {
        String[] parts = new String[] {this.token, timestamp, nonce};
        Arrays.sort(parts);
        String str = parts[0] + parts[1] + parts[2];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(str.getBytes("UTF-8"));
            return signature.equalsIgnoreCase(Hex.encodeHexString(digest));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private AccessToken getAccessToken() {
        if (accessToken == null || accessToken.isExpire()) {
            synchronized (this) {
                if (accessToken != null && !accessToken.isExpire()) {
                    return accessToken;
                }
                AccessToken token = new AccessToken();
                String url = ACCESS_TOKEN_URL.replace("APPID", this.appId).replace("APPSECRET", this.appSecret);
                try {
                    JSONObject jsonObject = doGet(url);
                    if (jsonObject != null) {
                        token.setToken(jsonObject.getString("access_token"));
                        token.setExpiresIn(jsonObject.getInt("expires_in"));
                        token.setObtainTime(LocalDateTime.now());
                    }
                    System.out.println("Get Access Token Success!");
                    accessToken = token;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (accessToken == null || accessToken.isExpire()) {
            throw new IllegalArgumentException("获取到无效的 AccessToken");
        } else {
            return accessToken;
        }
    }

    public JSONObject batchGetMaterial() throws IOException {
        String url = BATCH_GET_MATERIAL.replace("ACCESS_TOKEN", this.getAccessToken().getToken());
        System.out.println(url);
        return doGet(url);
    }

    public JSONObject uploadTempImage(String filePath) throws IOException {
        String url = UPLOAD.replace("ACCESS_TOKEN", this.getAccessToken().getToken()).replace("TYPE", "image");
        return doUpload(url, filePath);
    }

    public JSONObject uploadTempImage(InputStream inputStream) throws IOException {
        String url = UPLOAD.replace("ACCESS_TOKEN", this.getAccessToken().getToken()).replace("TYPE", "image");
        return doUpload(url, inputStream, UUID.randomUUID().toString());
    }

    private static JSONObject doGet(String url) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            return responseToJSON(httpResponse);
        }
    }

    private static JSONObject doPost(String url, String outStr) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            return responseToJSON(httpResponse);
        }
    }

    private static JSONObject doUpload(String url, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return doUpload(url, fileInputStream, file.getName());
        }
    }

    private static JSONObject doUpload(String url, InputStream inputStream, String fileName) throws IOException {
        String BOUNDARY = "--------------" + System.currentTimeMillis();
        URL urlObj = new URL(url);

        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        conn.setRequestProperty("User-Agent", "curl/7.58.0");
        conn.setRequestProperty("Accept", "*/*");

        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append("--");
        headerBuilder.append(BOUNDARY);
        headerBuilder.append("\r\n");
        headerBuilder.append("Content-Disposition: form-data; name=\"media\"; filename=\"" + fileName + "\"\r\n");
        headerBuilder.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = headerBuilder.toString().getBytes("utf-8");

        // write out headers
        OutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(head);
        System.out.println("write out header");

        // write out body
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = inputStream.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        System.out.println("write out body");

        // write out foot
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
        out.write(foot);
        out.flush();
        out.close();
        System.out.println("write out foot");

        // receive return message
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println(buffer.toString());
            try {
                return JSONObject.fromObject(buffer.toString());
            }catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static JSONObject responseToJSON(HttpResponse httpResponse) {
        JSONObject jsonObject = null;
        HttpEntity entity = httpResponse.getEntity();
        System.out.println(entity.getContentLength());
        if (entity.getContentLength() > 0) {
            try {
                String result = EntityUtils.toString(entity, "UTF-8");
                System.out.println(result);
                jsonObject = JSONObject.fromObject(result);
                EntityUtils.consume(entity);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

}
