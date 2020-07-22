package wiki.zimo.wiseduunifiedloginapi.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiaoyang
 * @create 2020/7/21 9:05 下午
 */
public class WeChatUtil {

    private static String appId = "wxc0db1a7fb9ffca02";
    private static String appSecret = "bf8b1acb7073e96f81ef56812449ca6d";

    public static JSONObject getAccessToken() {
        String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appId=APPID&secret=APPSECRET";
        String requestUrl = accessTokenUrl.replace("APPID", appId).replace("APPSECRET", appSecret);
        return WeChatUtil.doGet(requestUrl);
    }

    public static JSONObject doGet(String requestUrl) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String responseContent = null;
        JSONObject result = null;

        try {
            HttpGet httpGet = new HttpGet(requestUrl);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            result = JSON.parseObject(responseContent);
        } catch (IOException e) {
            System.out.println("HTTP请求异常：" + e.getMessage());
        }

        return result;
    }

    public static String getJsApiTicket(String accessToken) {
        String apiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        String requestUrl = apiTicketUrl.replace("ACCESS_TOKEN", accessToken);
        System.out.println("getJsApiTicket.requestUrl ====> " + requestUrl);

        JSONObject result = WeChatUtil.doGet(requestUrl);
        System.out.println("getHsApiTicket.response ====> " + result);

        String jsApiTicket = null;
        if (null != result) {
            jsApiTicket = result.getString("ticket");
        }
        return jsApiTicket;
    }

    public static Map<String, String> generateWxTicket(String jsApiTicket, String url) {
        Map<String, String> ret = new HashMap<>();
        String nonceStr = createNonceStr();
        String timestamp = createTimestamp();
        String string1;
        String signature = "";

        string1 = "jsapi_ticket=" + jsApiTicket +
                "&noncestr=" + nonceStr +
                "&timestamp=" + timestamp +
                "&url=" + url;

        System.out.println("string1 ====> " + string1);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
            System.out.println("signature ====> " + signature);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsApiTicket);
        ret.put("nonceStr", nonceStr);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appid", appId);

        return ret;
    }



    /**
     * 字节数组转换为十六进制字符串
     *
     * @param hash 字节数组
     * @return 十六进制字符串
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 生成随机字符串
     *
     * @return 随机字符串
     */
    private static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成时间戳
     *
     * @return 时间戳
     */
    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static void main(String[] args) {
        JSONObject accessToken = getAccessToken();
        String access_token = accessToken.getString("access_token");
        String jsApiTicket = getJsApiTicket(access_token);
        System.out.println(jsApiTicket);
       /* Map<String, String> map = generateWxTicket(jsApiTicket, "https://test.xiaoyang666.top/api/");
        System.out.println(map);*/

    }

}
