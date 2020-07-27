package wiki.zimo.wiseduunifiedloginapi.task;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wiki.zimo.wiseduunifiedloginapi.dao.UserMapper;
import wiki.zimo.wiseduunifiedloginapi.dto.User;
import wiki.zimo.wiseduunifiedloginapi.helper.AESUtil;
import wiki.zimo.wiseduunifiedloginapi.service.AutoSubmitService;
import wiki.zimo.wiseduunifiedloginapi.service.WxPushService;

import java.io.IOException;
import java.util.List;

/**
 * @author xiaoyang
 * @create 2020/7/3 12:48 下午
 */

@EnableScheduling
@Component
public class AutoSubmitTask {

    @Autowired
    private AutoSubmitService autoSubmitService;

    @Value("${USERNAME}")
    private String USERNAME;

    @Value("${PASSWORD}")
    private String PASSWORD;

    @Value("${EMAIL}")
    private String EMAIL;

    @Value("${CALLBACK_URL}")
    private String CALLBACK_URL;

    @Autowired
    private WxPushService wxPushService;

    @Autowired
    private UserMapper userMapper;

    @Scheduled(cron = "0 0 9 * * *")
    public void autoSubmitTaskByWxPush() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(CALLBACK_URL + "/api/autoSubmitAllUser")
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 1 9 * * *")
    public void report(){
        List<User> users = userMapper.selectAll();
        StringBuffer sb = new StringBuffer();
        for (User user : users) {
            sb.append(AESUtil.decrypt(user.getRealName()) + " " + user.getResult() + "\n");
        }
        wxPushService.wxPush(sb.toString(), "UID_iAB4BFMt7quFBtA4eFOeQl117fbZ");
    }

}
