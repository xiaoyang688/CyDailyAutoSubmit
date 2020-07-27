package wiki.zimo.wiseduunifiedloginapi.dto;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author xiaoyang
 * @create 2020/7/3 11:56 下午
 */
@Table(name = "tb_user")
public class User implements Serializable {

    @Id
    private String username;
    private String password;
    private String uid;
    private String realName;
    private String address;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                ", realName='" + realName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
