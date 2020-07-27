package wiki.zimo.wiseduunifiedloginapi.service;

import wiki.zimo.wiseduunifiedloginapi.dto.User;

/**
 * @author xiaoyang
 * @create 2020/7/6 1:02 下午
 */
public interface UserService {

    /**
     * 通过username查找
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 获取学生真实名字
     * @param user
     * @return
     */
    String getRealName(User user);

    /**
     * 获取用户地址
     * @param latitude
     * @param longitude
     * @return
     */
    String getAddress(String longitude, String latitude);

    /**
     * 通过uid查找用户
     * @param uid
     * @return
     */
    User findByUid(String uid);

    /**
     * 通过uid删除
     *
     * @param uid
     * @return
     */
    int deleteByUID(String uid);

    /**
     * 更新结果
     * @param username
     * @param result
     */
    void updateResult(String username, String result);
}
