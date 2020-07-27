package wiki.zimo.wiseduunifiedloginapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wiki.zimo.wiseduunifiedloginapi.dao.UserMapper;
import wiki.zimo.wiseduunifiedloginapi.dto.User;
import wiki.zimo.wiseduunifiedloginapi.helper.WeChatUtil;
import wiki.zimo.wiseduunifiedloginapi.service.AutoSubmitService;
import wiki.zimo.wiseduunifiedloginapi.service.UserService;

import java.io.IOException;

/**
 * @author xiaoyang
 * @create 2020/7/6 1:03 下午
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AutoSubmitService autoSubmitService;

    public static OkHttpClient client = new OkHttpClient();

    @Override
    public User findByUsername(String username) {
        User user = userMapper.selectByPrimaryKey(username);
        return user;
    }

    @Override
    public String getRealName(User user) {

        String cookie = autoSubmitService.getCookie(user.getUsername(), user.getPassword());

        Request request = new Request.Builder()
                .addHeader("Host", "thxy.campusphere.net")
                .addHeader("accept", "application/json, text/plain, */*")
                .addHeader("Origin", "https://thxy.campusphere.net")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 4.4.4; OPPO R11 Plus Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Safari/537.36")
                .addHeader("Cookie", cookie)
                .url("https://thxy.campusphere.net/portal/desktop/userDesktopInfo")
                .post(RequestBody.create("", null))
                .build();

        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            JSONObject jsonObject = JSON.parseObject(resp);
            String realName = jsonObject.getJSONObject("datas").getString("userName");
            return realName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAddress(String longitude, String latitude) {
        String accessTokenUrl = "https://restapi.amap.com/v3/geocode/regeo?output=json&location=LOCATION&key=a75d26219b13abeedfc7cd985453bc61&radius=500";
        String requestUrl = accessTokenUrl.replace("LOCATION", longitude + "," + latitude);
        JSONObject jsonObject = WeChatUtil.doGet(requestUrl);
        String address = null;
        try {
            address = jsonObject.getJSONObject("regeocode").getString("formatted_address");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "定位失败";
        }
        return address;

    }

    @Override
    public User findByUid(String uid) {

        User user = new User();
        user.setUid(uid);
        User searchUser = userMapper.selectOne(user);
        return searchUser;

    }

    @Override
    public int deleteByUID(String uid) {
        User user = new User();
        user.setUid(uid);
        int result = userMapper.delete(user);
        return result;
    }

    @Override
    public void updateResult(String username, String result) {
        User user = userMapper.selectByPrimaryKey(username);
        user.setResult(result);
        userMapper.updateByPrimaryKeySelective(user);
    }
}
