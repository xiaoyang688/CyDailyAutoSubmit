package wiki.zimo.wiseduunifiedloginapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author xiaoyang
 * @create 2020/7/26 5:28 下午
 */
@Controller
public class ShareController {

    @GetMapping("/share")
    public String share() {
        return "share";
    }

}
