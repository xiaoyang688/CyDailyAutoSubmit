package wiki.zimo.wiseduunifiedloginapi.task;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import wiki.zimo.wiseduunifiedloginapi.dao.UserMapper;
import wiki.zimo.wiseduunifiedloginapi.dto.User;
import wiki.zimo.wiseduunifiedloginapi.service.AutoSubmitService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

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

}
