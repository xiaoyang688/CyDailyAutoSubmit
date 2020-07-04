package wiki.zimo.wiseduunifiedloginapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import wiki.zimo.wiseduunifiedloginapi.helper.LocalCache;
import wiki.zimo.wiseduunifiedloginapi.service.SendEmailService;

/**
 * @author xiaoyang
 * @create 2020/6/30 9:05 下午
 */
@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private Integer EXPIRE_TIME = 60;

    private String IS_SEND = "1";

    @Override
    public void send(String sender, String receiver, String title, String text) {

        //限制邮箱发送频率
        String value = LocalCache.get(receiver);
        if (IS_SEND.equals(value)) {
            return;
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(receiver);
        mailMessage.setSubject(title);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
        LocalCache.put(receiver, "1", EXPIRE_TIME);
    }
}
