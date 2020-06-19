package cn.edu.swu.wechat;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class WeChatService {

    private static final String TOKEN = "JinLuTechC313";

    public static boolean checkSignature(String timestamp, String nonce, String signature) {
        String[] parts = new String[] {TOKEN, timestamp, nonce};
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
}
