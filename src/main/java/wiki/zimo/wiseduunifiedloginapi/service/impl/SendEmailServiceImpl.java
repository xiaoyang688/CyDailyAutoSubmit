package wiki.zimo.wiseduunifiedloginapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import wiki.zimo.wiseduunifiedloginapi.service.SendEmailService;

/**
 * @author xiaoyang
 * @create 2020/6/30 9:05 下午
 */
@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String send(String sender, String receiver, String title, String text) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(receiver);
        mailMessage.setSubject(title);
        mailMessage.setText(text);

        javaMailSender.send(mailMessage);

        return "success";
    }
}
