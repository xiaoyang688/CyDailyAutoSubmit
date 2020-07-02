package wiki.zimo.wiseduunifiedloginapi.service;

/**
 * @author xiaoyang
 * @create 2020/6/30 9:03 下午
 */
public interface SendEmailService {

    /**
     * 发送邮箱
     * @param sender
     * @param receiver
     * @param title
     * @param text
     * @return
     */
    String send(String sender, String receiver, String title, String text);

}
