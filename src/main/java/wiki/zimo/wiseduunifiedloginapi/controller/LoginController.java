package wiki.zimo.wiseduunifiedloginapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wiki.zimo.wiseduunifiedloginapi.dto.User;
import wiki.zimo.wiseduunifiedloginapi.helper.AESUtil;
import wiki.zimo.wiseduunifiedloginapi.service.UserService;
import wiki.zimo.wiseduunifiedloginapi.service.WxPushService;

import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/5 11:00 下午
 */
@Controller
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private WxPushService wxPushService;


    @RequestMapping("/login")
    public String login(@RequestParam(name = "uid", required = false) String uid, Model model) {
        Map<String, String> userInfo = wxPushService.getHeadImg(uid);
        model.addAttribute("headImg", userInfo.get("headImg"));
        model.addAttribute("nickName", userInfo.get("nickName"));
        model.addAttribute("uid", uid);

        User user = userService.findByUid(uid);
        if (user != null) {
            model.addAttribute("realName", AESUtil.decrypt(user.getRealName()));
            return "user";
        }
        return "index";
    }
}
