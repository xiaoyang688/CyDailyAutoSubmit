package wiki.zimo.wiseduunifiedloginapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wiki.zimo.wiseduunifiedloginapi.helper.LocalCache;
import wiki.zimo.wiseduunifiedloginapi.service.AutoSubmitService;
import wiki.zimo.wiseduunifiedloginapi.service.LoginService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyang
 * @create 2020/7/2 2:30 下午
 */
@Service
public class AutoSubmitServiceImpl implements AutoSubmitService {


    private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    @Value("${COLLECTING_PROCESSING}")
    private String COLLECTING_PROCESSING;

    @Value("${DETAIL_COLLECTOR}")
    private String DETAIL_COLLECTOR;

    @Value("${FROM_FIELD}")
    private String FROM_FIELD;

    @Value("${SUBMIT_FROM}")
    private String SUBMIT_FROM;

    @Value("${USERNAME}")
    private String USERNAME;

    @Value("${PASSWORD}")
    private String PASSWORD;

    public static OkHttpClient client = new OkHttpClient();

    @Autowired
    private LoginService loginService;

    @Override
    public Map<String, String> getFormBaseInfo() {

        HashMap<String, Integer> reqBody = new HashMap<>();
        reqBody.put("pageSize", 6);
        reqBody.put("pageNumber", 1);

        String json = JSONObject.toJSONString(reqBody);

        Response response = getResponse(COLLECTING_PROCESSING, json);

        try {
            String resp = response.body().string();
            System.out.println(resp);
            JSONObject jsonObject = JSON.parseObject(resp);
            if ("SUCCESS".equals(jsonObject.getString("message"))) {
                JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("rows");
                if (jsonArray.size() == 0) {
                    return null;
                }
                JSONObject row = jsonArray.getJSONObject(0);
                String wid = row.getString("wid");
                String formWid = row.getString("formWid");

                Map<String, String> result = new HashMap<>();
                result.put("wid", wid);
                result.put("formWid", formWid);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getSchoolTaskWid(String collectorWid) {

        HashMap<String, String> map = new HashMap<>();
        map.put("collectorWid", collectorWid);

        String json = JSONObject.toJSONString(map);
        Response response = getResponse(DETAIL_COLLECTOR, json);

        try {
            String resp = response.body().string();
            System.out.println(resp);
            JSONObject jsonObject = JSON.parseObject(resp);
            if ("SUCCESS".equals(jsonObject.getString("message"))) {
                String schoolTaskWid = jsonObject.getJSONObject("datas").getJSONObject("collector").getString("schoolTaskWid");
                return schoolTaskWid;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONArray getFormField(String formWid, String collectorWid) {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("pageSize", 30);
        map.put("pageNumber", 1);
        map.put("formWid", Integer.valueOf(formWid));
        map.put("collectorWid", Integer.valueOf(collectorWid));

        String json = JSONObject.toJSONString(map);
        Response response = getResponse(FROM_FIELD, json);

        try {
            String resp = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(resp);
            if ("SUCCESS".equals(jsonObject.getString("message"))) {
                JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("rows");
                return parseFormField(jsonArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> submitForm(String formWid, String collectWid, String address, String schoolTaskWid, JSONArray formField) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formWid", formWid);
        jsonObject.put("collectWid", collectWid);
        jsonObject.put("address", address);
        jsonObject.put("schoolTaskWid", schoolTaskWid);
        jsonObject.put("form", formField);

        String json = JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue);
        Response response = getResponse(SUBMIT_FROM, json);

        HashMap<String, String> resultMap = new HashMap<>(16);
        try {
            String resp = response.body().string();
            System.out.println(resp);
            JSONObject respJson = JSON.parseObject(resp);
            String message = respJson.getString("message");
            if ("SUCCESS".equals(message)) {
                resultMap.put("message", "自动打卡成功");
            } else {
                resultMap.put("message", message);
            }
            return resultMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String autoSubmit(String username, String password, String email) {
        //TODO
        return null;
    }

    /**
     * 处理提交表单数据
     *
     * @param jsonArray
     * @return
     */
    private JSONArray parseFormField(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject row = jsonArray.getJSONObject(i);

            row.put("minValue", 0);
            row.put("maxValue", 0);

            row.put("maxLength", null);
            row.put("imageCount", null);

            JSONArray fieldItems = row.getJSONArray("fieldItems");
            //处理特殊字段 008 012 019 021 015
            String colName = row.getString("colName");
            if ("field008".equals(colName)) {
                //将fieldItems致空
                JSONArray fi = row.getJSONArray("fieldItems");
                fi.clear();
                fi.add(null);
            } else if ("field019".equals(colName)) {
                JSONArray fi = row.getJSONArray("fieldItems");
                fi.clear();
                fi.add(null);
            } else if ("field012".equals(colName)) {
                //获取地址
                String address = row.getString("value");
                String[] areas = address.split("/");
                for (int k = 0; k < areas.length; k++) {
                    row.put("area" + (k + 1), areas[k]);
                }
            } else if ("field021".equals(colName)) {
                for (int j = 1; j < 4; j++) {
                    row.put("area" + j, "");
                }
            } else if ("field015".equals(colName)) {
                row.put("date", "");
                row.put("time", "");
            }

            if (fieldItems.size() >= 1 && fieldItems.get(0) != null) {
                for (int j = 0; j < fieldItems.size(); j++) {
                    JSONObject fieldItem = fieldItems.getJSONObject(j);
                    //过滤isSelected为空的字段
                    if (fieldItem.getInteger("isSelected") == (Integer.valueOf(1))) {
                        fieldItems.clear();
                        fieldItems.add(fieldItem);
                        String content = fieldItem.getString("content");
                        row.put("value", content);
                    }
                }
            }
        }
        return jsonArray;
    }

    /**
     * 获取最新cookie
     *
     * @param username
     * @param password
     * @return
     */
    private String getCookie(String username, String password) {
        Map<String, String> cookieMap = null;
        String cookie = null;
        try {
            String cookieCache = LocalCache.get("cookie");
            if (cookieCache != null) {
                cookie = cookieCache;
            } else {
                cookieMap = loginService.login(username, password);
                LocalCache.put("cookie", cookieMap.get("cookies"), 60);
                cookie = LocalCache.get("cookie");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookie;
    }

    /**
     * 模拟请求
     *
     * @param url
     * @param json
     * @return
     */
    private Response getResponse(String url, String json) {

        String cookie = getCookie(USERNAME, PASSWORD);
        System.out.println(cookie);

        RequestBody body = RequestBody.create(json, MEDIA_TYPE);
        Request request = null;

        if (!SUBMIT_FROM.equals(url)) {
            request = new Request.Builder()
                    .addHeader("Host", "thxy.campusphere.net")
                    .addHeader("accept", "application/json, text/plain, */*")
                    .addHeader("Origin", "https://thxy.campusphere.net")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 4.4.4; OPPO R11 Plus Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Safari/537.36")
                    .addHeader("Cookie", cookie)
                    .url(url)
                    .post(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .addHeader("Host", "thxy.campusphere.net")
                    .addHeader("accept", "application/json, text/plain, */*")
                    .addHeader("Origin", "https://thxy.campusphere.net")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 4.4.4; OPPO R11 Plus Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Safari/537.36")
                    .addHeader("Cpdaily-Extension", "CEzs4zRiNZBrVyVJIR48j6/3TPSFahdaLc4npCP9yU7LJMmriddO2xR8oE3/ D/4/dhjMDmYcxH5WqdvlJ3Ky/k51zECE4+q3+RebmedTxgOXow8bt3Wl8eOE U94zOMB9phz+iHc5LsdudT+U9g+c+DKL3HDRZeRspyTMTuk2PY96+ZDo5FJK dXlMIfj/zoRbihdsiz8auSAc7YKlvCZWqRqUOO9zqi0sGGCXg5TJ5bwstSrM L46aSlffJT663PRxrzmD8Lr0JD4=")
                    .addHeader("Cookie", cookie)
                    .url(url)
                    .post(body)
                    .build();
        }
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
