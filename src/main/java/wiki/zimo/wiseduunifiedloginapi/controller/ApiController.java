package wiki.zimo.wiseduunifiedloginapi.controller;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wiki.zimo.wiseduunifiedloginapi.service.AutoSubmitService;
import wiki.zimo.wiseduunifiedloginapi.service.LoginService;
import wiki.zimo.wiseduunifiedloginapi.service.SendEmailService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private AutoSubmitService autoSubmitService;

    @RequestMapping("/login")
    public Map<String, String> login(@RequestParam(value = "login_url", required = false) String login_url,
                                     @RequestParam(value = "needcaptcha_url", required = false) String needcaptcha_url,
                                     @RequestParam(value = "captcha_url", required = false) String captcha_url,
                                     @RequestParam("username") String username,
                                     @RequestParam("password") String password) {
        try {
            Map<String, String> cookies = loginService.login(login_url, needcaptcha_url, captcha_url, username, password);
            return cookies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/sendEmail")
    public String sendMail() {

        String status = sendEmailService.send("cydaily@qq.com", "18024088480@163.com", "Hello World", "66666");
        return status;

    }

    @GetMapping("/test")
    public Map<String, String>  test() {
        Map<String, String> formBaseInfo = autoSubmitService.getFormBaseInfo();
        String formWid = formBaseInfo.get("formWid");
        String collectorWid = formBaseInfo.get("wid");
        String schoolTaskWid = autoSubmitService.getSchoolTaskWid(collectorWid);
        JSONArray formField = autoSubmitService.getFormField(formWid, collectorWid);
        Map<String, String> map = autoSubmitService.submitForm(formWid, collectorWid, "定位信息", schoolTaskWid, formField);
        String status = sendEmailService.send("cydaily@qq.com", "1501214688@qq.com", "【今日校园打卡情况通知】", map.get("message"));
        return map;
    }

}
