package wiki.zimo.wiseduunifiedloginapi.service;

import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/6 12:53 下午
 */
public interface WxPushService {

    /**
     * 微信推送
     * @param content
     * @param uid
     */
    public void wxPush(String content, String uid);

    /**
     * 获取头像链接
     * @param uid
     * @return
     */
    public Map<String, String> getHeadImg(String uid);

}
