package wiki.zimo.wiseduunifiedloginapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wiki.zimo.wiseduunifiedloginapi.dto.User;
import wiki.zimo.wiseduunifiedloginapi.helper.AESUtil;
import wiki.zimo.wiseduunifiedloginapi.service.UserService;
import wiki.zimo.wiseduunifiedloginapi.service.WxPushService;

import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/20 9:13 下午
 */
@Controller
@RequestMapping("/api")
public class LogoutController {

    @Autowired
    private WxPushService wxPushService;

    @Autowired
    private UserService userService;

    @Value("${CALLBACK_URL}")
    private String CALLBACK_URL;

    @GetMapping("/logout/{id}")
    public String logout(@PathVariable("id") String uid, Model model) {

        Map<String, String> userInfo = wxPushService.getHeadImg(uid);
        model.addAttribute("headImg", userInfo.get("headImg"));
        model.addAttribute("nickName", userInfo.get("nickName"));
        model.addAttribute("uid", uid);
        User user = userService.findByUid(uid);
        if (user != null) {
            model.addAttribute("realName", AESUtil.decrypt(user.getRealName()));
        }
        String url = CALLBACK_URL + "/api/login/" + uid;
        int result = userService.deleteByUID(uid);
        if (result < 1) {
            wxPushService.wxPush("您当前没有模拟登录今日校园" + "\n" + "\uD83D\uDC49<a href=\"" + url + "\">点击模拟登录今日校园</a>\uD83D\uDC48", "UID_" + uid);
            return "error";
        }
        wxPushService.wxPush("您已成功取消自动打卡！请您到今日校园app手动打卡，欢迎再次使用~ " + "\n" + "\uD83D\uDC49<a href=\"" + url + "\">点击再次使用自动打卡功能</a>\uD83D\uDC48", "UID_" + uid);
        return "logout";
    }

}
