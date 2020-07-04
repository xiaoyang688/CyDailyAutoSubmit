package wiki.zimo.wiseduunifiedloginapi.service;

import com.alibaba.fastjson.JSONArray;

import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/2 2:26 下午
 */
public interface AutoSubmitService {

    /**
     * 自动提交接口
     *
     * @param username
     * @param password
     * @param email
     * @return
     */
    public String autoSubmit(String username, String password, String email);

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


}
