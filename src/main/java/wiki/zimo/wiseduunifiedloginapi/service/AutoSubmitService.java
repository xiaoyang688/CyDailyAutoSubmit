package wiki.zimo.wiseduunifiedloginapi.service;

import com.alibaba.fastjson.JSONArray;

import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/2 2:26 下午
 */
public interface AutoSubmitService {

    /**
     * 获取表单基本信息
     * @return
     */
    public Map<String, String> getFormBaseInfo();

    /**
     * 获取SchoolTaskWid
     * @param collectorWid
     * @return
     */
    public String getSchoolTaskWid(String collectorWid);


    /**
     * 获取表单详情
     * @param formWid
     * @param collectorWid
     * @return
     */
    public JSONArray getFormField(String formWid, String collectorWid);

    /**
     * 提交订单
     * @param formWid
     * @param collectWid
     * @param address
     * @param schoolTaskWid
     * @param formField
     * @return
     */
    public Map<String, String> submitForm(String formWid, String collectWid, String address, String schoolTaskWid, JSONArray formField);
}
