package wiki.zimo.wiseduunifiedloginapi.task;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wiki.zimo.wiseduunifiedloginapi.service.AutoSubmitService;
import wiki.zimo.wiseduunifiedloginapi.service.SendEmailService;

import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/3 12:48 下午
 */
@Component
@EnableScheduling
public class AutoSubmitTask {

    @Autowired
    private AutoSubmitService autoSubmitService;

    @Autowired
    private SendEmailService sendEmailService;

    @Value("${EMAIL}")
    private String EMAIL;

    @Scheduled(cron = "0 0 1 * * ?")
    public void autoSubmitTask() {
        try {
            Map<String, String> formBaseInfo = autoSubmitService.getFormBaseInfo();
            String formWid = formBaseInfo.get("formWid");
            String collectorWid = formBaseInfo.get("wid");
            String schoolTaskWid = autoSubmitService.getSchoolTaskWid(collectorWid);
            JSONArray formField = autoSubmitService.getFormField(formWid, collectorWid);
            Map<String, String> map = autoSubmitService.submitForm(formWid, collectorWid, "定位信息", schoolTaskWid, formField);
            sendEmailService.send("cydaily@qq.com", EMAIL, "【今日校园打卡情况通知】", map.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
            sendEmailService.send("cydaily@qq.com", EMAIL, "【今日校园打卡情况通知】", "打卡失败！请您到今日校园app手动打卡~");
        }
    }


}
