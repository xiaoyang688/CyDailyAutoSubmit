package wiki.zimo.wiseduunifiedloginapi.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * @author xiaoyang
 * @create 2020/7/13 9:10 上午
 */

@Component
@EnableScheduling
public class AutoSubmitByEmail {


    /*@Scheduled(cron = "0 0 6 * * ?")
    public void autoSubmitTaskByEmail() {
        autoSubmitService.autoSubmitByEmail(USERNAME, PASSWORD, EMAIL, null);
    }
*/

}
