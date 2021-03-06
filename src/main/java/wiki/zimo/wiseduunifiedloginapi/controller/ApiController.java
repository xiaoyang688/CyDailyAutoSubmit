package wiki.zimo.wiseduunifiedloginapi.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import wiki.zimo.wiseduunifiedloginapi.dao.UserMapper;
import wiki.zimo.wiseduunifiedloginapi.dto.User;
import wiki.zimo.wiseduunifiedloginapi.helper.AESUtil;
import wiki.zimo.wiseduunifiedloginapi.service.AutoSubmitService;
import wiki.zimo.wiseduunifiedloginapi.service.LoginService;
import wiki.zimo.wiseduunifiedloginapi.service.UserService;
import wiki.zimo.wiseduunifiedloginapi.service.WxPushService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AutoSubmitService autoSubmitService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private WxPushService wxPushService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Value("${CALLBACK_URL}")
    private String CALLBACK_URL;

    @PostMapping("/autoSubmit")
    public String autoSubmit(@RequestBody User user) {
        String status = autoSubmitService.autoSubmitByEmail(user.getUsername(), user.getPassword(), user.getAddress(), user.getUid());
        return status;
    }

    @GetMapping("/autoSubmitAllUser")
    public String autoSubmitAllUser() {

        List<User> users = userMapper.selectAll();
        CyclicBarrier barrier = new CyclicBarrier(users.size());
        for (User user : users) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = autoSubmitService.autoSubmitByWxPush(user.getUsername(), user.getPassword(), user.getAddress(), user.getUid());
                    System.out.println(result);
                }
            }).start();
        }
        try {
            barrier.await(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    @PostMapping("/callback")
    public String callback(@RequestBody JSONObject jsonObject) {
        String uid = jsonObject.getJSONObject("data").getString("uid");
        String uidCode = uid.split("_")[1];
        String url = CALLBACK_URL + "/api/login?uid=" + uidCode;
        String content = "\uD83D\uDC49<a href=\"" + url + "\">点击模拟登录今日校园</a>\uD83D\uDC48";
        wxPushService.wxPush(content, uid);
        return "SUCCESS";
    }

    @PostMapping("/login")
    public HashMap<String, String> login(User user) {
        HashMap<String, String> map = new HashMap<>();
        System.out.println(user.toString());
        synchronized (this) {
            try {
                loginService.login(user.getUsername(), user.getPassword());
                String realName = userService.getRealName(user);
                User searchUser = userService.findByUsername(user.getUsername());
                user.setUsername(AESUtil.encrypt(user.getUsername()));
                user.setPassword(AESUtil.encrypt(user.getPassword()));
                user.setRealName(AESUtil.encrypt(realName));
                user.setAddress(AESUtil.encrypt(user.getAddress()));
                if (searchUser == null) {
                    userMapper.insert(user);
                }
                map.put("message", "success");
                String currentTime = getCurrentTime(new Date());
                String url = CALLBACK_URL + "/api/logout/" + user.getUid();
                wxPushService.wxPush("尊敬的" + realName + "同学，您在" + currentTime + "已成功模拟登录今日校园，每天早上9点将进行自动打卡，打卡结果将在本公众号通知" + "\n" + "<a href=\"" + url + "\">【如有特殊情况请取消自动打卡】</a>", "UID_" + user.getUid());
                wxPushService.wxPush(realName + "同学在" + currentTime + "成功模拟登录", "UID_iAB4BFMt7quFBtA4eFOeQl117fbZ");
            } catch (Exception e) {
                e.printStackTrace();
                map.put("message", "fail");
            }
        }
        return map;
    }

    @GetMapping("/getAddress")
    public String getAddress(String longitude, String latitude) {
        String address = userService.getAddress(longitude, latitude);
        return address;
    }

    private String getCurrentTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日HH时mm分");
        String format = formatter.format(date);
        return format;
    }

}
