package com.huilianjk.bonus.wechat.util;

import com.huilianjk.bonus.util.common.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by space on 15/12/5.
 */
@Component
public class Sign {
    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String signWechatPayRequest(String apiKey, Document xml) {
        String signString = getSignString(xml) + "&key=" + apiKey;
        return MD5Util.getMD5Code(signString);
    }

    private static String getSignString(Document document) {
        List<String> argList = new ArrayList<>();
        Element root = document.getRootElement();
        for (Object element : root.elements()) {
            argList.add(((Element) element).getName());
        }
        Collections.sort(argList);
        StringBuilder builder = new StringBuilder();
        for (String str : argList) {
            if (StringUtils.isNotBlank(root.elementText(str))) {
                builder.append("&" + str + "=" + root.elementText(str));
            }
        }
        return builder.toString().substring(1);
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
