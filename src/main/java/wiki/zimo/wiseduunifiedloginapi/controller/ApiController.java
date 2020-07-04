package wiki.zimo.wiseduunifiedloginapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.zimo.wiseduunifiedloginapi.dto.User;
import wiki.zimo.wiseduunifiedloginapi.service.AutoSubmitService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AutoSubmitService autoSubmitService;

    @PostMapping("/autoSubmit")
    public String autoSubmit(@RequestBody User user) {
        String status = autoSubmitService.autoSubmit(user.getUsername(), user.getPassword(), user.getEmail());
        return status;
    }

}
