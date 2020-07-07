package wiki.zimo.wiseduunifiedloginapi.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wiki.zimo.wiseduunifiedloginapi.dao.UserMapper;
import wiki.zimo.wiseduunifiedloginapi.dto.User;
import wiki.zimo.wiseduunifiedloginapi.service.AutoSubmitService;

import java.util.List;

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
    private UserMapper userMapper;

    @Value("${USERNAME}")
    private String USERNAME;

    @Value("${PASSWORD}")
    private String PASSWORD;

    @Value("${EMAIL}")
    private String EMAIL;

    @Scheduled(cron = "0 0 6 * * ?")
    public void autoSubmitTaskByEmail() {
        autoSubmitService.autoSubmitByEmail(USERNAME, PASSWORD, EMAIL, null);
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void autoSubmitTaskByWxPush() {

        List<User> users = userMapper.selectAll();

        for (User user : users) {
            autoSubmitService.autoSubmitByWxPush(user.getUsername(), user.getPassword(), user.getEmail(), user.getUid());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
