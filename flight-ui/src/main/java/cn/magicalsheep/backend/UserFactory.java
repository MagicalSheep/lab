package cn.magicalsheep.backend;

import cn.magicalsheep.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class UserFactory {

    private static final Logger logger = LogManager.getLogger();
    private static final Map<String, User> sources = new HashMap<>();

    /**
     * 用户登录校验
     *
     * @param account 账号
     * @param pwd     密码
     * @return 用户实体
     * @throws Exception Exception
     */
    public static User login(String account, String pwd) throws Exception {
        logger.info("User <{}> is trying login with password <{}>", account, pwd);
        if (!sources.containsKey(account)) {
            logger.info("Login failed: invalid user account");
            throw new Exception("用户不存在！");
        }
        if (!sources.get(account).getPwd().equals(pwd)) {
            logger.info("Login failed: wrong password");
            throw new Exception("密码错误！");
        }
        logger.info("Login successfully");
        return sources.get(account);
    }

    /**
     * 注册一个新用户
     *
     * @param name  用户名
     * @param pwd   密码
     * @param group 用户组
     * @return 账号
     */
    public static String register(String name, String pwd, int group) {
        logger.info("Register user with name <{}> and password <{}>", name, pwd);
        User user = new User();
        user.setName(name);
        user.setPwd(pwd);
        user.setGroup(group);
        user.setMoney(0);
        user.setId(String.valueOf(sources.size() + 1));
        sources.put(user.getId(), user);
        logger.info("Register successfully, user account is: <{}>", user.getId());
        return user.getId();
    }

    /**
     * 判断用户账号是否存在
     *
     * @param account 用户账号
     * @return 是否存在
     */
    public static boolean isUserExisted(String account) {
        logger.info("Check whether user account <{}> existed", account);
        return sources.containsKey(account);
    }

    /**
     * 更新数据源中的用户信息
     *
     * @param user 用户实体
     */
    public static void syncUser(User user) {
        logger.info("Update user <{}> information to user factory", user.getId());
        sources.put(user.getId(), user);
    }

    /**
     * 查找用户
     *
     * @param account 用户账号
     * @return 用户实体
     */
    public static User search(String account) {
        logger.info("Search for user <{}>", account);
        return sources.get(account);
    }

}
