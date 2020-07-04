package wiki.zimo.wiseduunifiedloginapi.dto;

import java.io.Serializable;

/**
 * @author xiaoyang
 * @create 2020/7/3 11:56 下午
 */
public class User implements Serializable {

    private String username;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
