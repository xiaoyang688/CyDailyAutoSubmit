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

    @Value("${USERNAME}")
    private String USERNAME;

    @Value("${PASSWORD}")
    private String PASSWORD;

    @Value("${EMAIL}")
    private String EMAIL;

    @Scheduled(cron = "0 0 6 * * ?")
    public void autoSubmitTask() {
        autoSubmitService.autoSubmit(USERNAME, PASSWORD, EMAIL);
    }


}
