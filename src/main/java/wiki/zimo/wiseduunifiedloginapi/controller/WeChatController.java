package wiki.zimo.wiseduunifiedloginapi.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wiki.zimo.wiseduunifiedloginapi.helper.LocalCache;
import wiki.zimo.wiseduunifiedloginapi.helper.WeChatUtil;

import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/21 9:20 下午
 */
@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @Value("${CALLBACK_URL}")
    private String CALLBACK_URL;

    @GetMapping("/config")
    public Map<String, String> config(@RequestParam("uid") String uid) {
        String url = CALLBACK_URL + "/api/login?uid=" + uid;
        String jsApiTicket = LocalCache.get("jsApiToken");
        if (jsApiTicket == null) {
            JSONObject accessToken = WeChatUtil.getAccessToken();
            String access_token = accessToken.getString("access_token");
            jsApiTicket = WeChatUtil.getJsApiTicket(access_token);
            LocalCache.put("jsApiToken", jsApiTicket, 7000);
        }
        Map<String, String> map = WeChatUtil.generateWxTicket(jsApiTicket, url);
        return map;
    }
}
