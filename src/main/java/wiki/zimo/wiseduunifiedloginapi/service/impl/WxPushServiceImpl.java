package wiki.zimo.wiseduunifiedloginapi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.Page;
import com.zjiecode.wxpusher.client.bean.Result;
import com.zjiecode.wxpusher.client.bean.WxUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wiki.zimo.wiseduunifiedloginapi.helper.LocalCache;
import wiki.zimo.wiseduunifiedloginapi.service.WxPushService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/6 12:55 下午
 */
@Service
public class WxPushServiceImpl implements WxPushService {

    public static Message message = new Message();

    @Value("${APP_TOKEN}")
    private String APP_TOKEN;

    private static String STATUS = "1";

    @Override
    public void wxPush(String content, String uid) {
        message.setAppToken(APP_TOKEN);
        message.setContentType(Message.CONTENT_TYPE_TEXT);
        message.setContent(content);
        message.setUid(uid);
        WxPusher.send(message);
    }

    @Override
    public Map<String, String> getHeadImg(String uid) {
        Result<Page<WxUser>> users = WxPusher.queryWxUser(APP_TOKEN, "UID_" + uid);
        String json = JSONObject.toJSONString(users);
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject record = jsonObject.getJSONObject("data").getJSONArray("records").getJSONObject(0);
        String headImg = record.getString("headImg");
        String nickName = record.getString("nickName");
        HashMap<String, String> result = new HashMap<>();
        result.put("headImg", headImg);
        result.put("nickName", nickName);
        return result;
    }
}
