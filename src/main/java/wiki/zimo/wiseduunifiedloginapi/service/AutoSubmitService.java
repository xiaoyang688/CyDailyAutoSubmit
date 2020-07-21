package wiki.zimo.wiseduunifiedloginapi.service;

import com.alibaba.fastjson.JSONArray;

import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/2 2:26 下午
 */
public interface AutoSubmitService {

    /**
     * 自动提交所有用户
     */
    void autoSubmitAllUser();

    /**
     * 自动提交接口(邮箱通知)
     *
     * @param username
     * @param password
     * @param email
     * @return
     */
    public String autoSubmitByEmail(String username, String password, String email, String uid);

    /**
     * 自动提交接口(微信通知)
     *
     * @param username
     * @param password
     * @param email
     * @param uid
     * @return
     */
    public String autoSubmitByWxPush(String username, String password, String email, String uid);

    /**
     * 获取表单基本信息
     *
     * @param cookie
     * @return
     */
    public Map<String, String> getFormBaseInfo(String cookie);

    /**
     * 获取SchoolTaskWid
     *
     * @param collectorWid
     * @param cookie
     * @return
     */
    public String getSchoolTaskWid(String collectorWid, String cookie);


    /**
     * 获取表单详情
     *
     * @param formWid
     * @param collectorWid
     * @param cookie
     * @return
     */
    public JSONArray getFormField(String formWid, String collectorWid, String cookie);

    /**
     * 提交订单
     *
     * @param formWid
     * @param collectWid
     * @param address
     * @param schoolTaskWid
     * @param formField
     * @param cookie
     * @return
     */
    public Map<String, String> submitForm(String formWid, String collectWid, String address, String schoolTaskWid, JSONArray formField, String cookie);

    /**
     * 获取最新cookie
     * @param username
     * @param password
     * @return
     */
    public String getCookie(String username, String password);

    }
