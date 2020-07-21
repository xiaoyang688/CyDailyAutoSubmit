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
}
