package cn.magicalsheep.frontend;

import cn.magicalsheep.CallBackInterface;
import cn.magicalsheep.backend.FlightFactory;
import cn.magicalsheep.backend.TicketFactory;
import cn.magicalsheep.backend.UserFactory;
import cn.magicalsheep.model.Plane;
import cn.magicalsheep.model.Ticket;
import cn.magicalsheep.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class UserHandler {

    private static final Logger logger = LogManager.getLogger();
    private static User user = null;

    /**
     * 用户登录
     *
     * @param account  账号
     * @param pwd      密码
     * @param callback 回调
     */
    public static void login(String account, String pwd, CallBackInterface callback) {
        Map<String, Object> ret = new HashMap<>();
        logger.info("UI request for login with user account <{}> and password <{}>", account, pwd);
        try {
            user = UserFactory.login(account, pwd);
            ret.put("user", user);
            logger.info("User <{}> login successfully", user.getName());
            SwingUtilities.invokeLater(() -> callback.success(ret));
        } catch (Exception e) {
            logger.info("Login failed: <{}>", e.getMessage());
            SwingUtilities.invokeLater(() -> {
                ret.put("msg", e.getMessage());
                callback.failed(ret);
            });
        }
        logger.info("Process completed");
    }

    /**
     * 用户退出登录
     *
     * @param callback 回调
     */
    public static void logout(CallBackInterface callback) {
        logger.info("UI request for logout");
        Map<String, Object> ret = new HashMap<>();
        user = null;
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 向数据源中注册一个新用户
     *
     * @param name     用户名
     * @param pwd      密码
     * @param group    用户组
     * @param callback 回调
     */
    public static void register(String name, String pwd, int group, CallBackInterface callback) {
        logger.info("UI request for registering a new user with name <{}> and password <{}>", name, pwd);
        String account = UserFactory.register(name, pwd, group);
        logger.info("Register successfully, user id is <{}>", account);
        Map<String, Object> ret = new HashMap<>();
        ret.put("id", account);
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 为用户充值余额
     *
     * @param money    充值金额
     * @param callback 回调
     */
    public static void addMoney(int money, CallBackInterface callback) {
        logger.info("UI request for add money for a user");
        Map<String, Object> ret = new HashMap<>();
        if (user == null) {
            logger.info("Process failed: user does not login");
            SwingUtilities.invokeLater(() -> {
                ret.put("msg", "请先登录！");
                callback.failed(ret);
            });
            return;
        }
        logger.info("User <{}> is trying to add money(value: {})", user.getName(), money);
        user.addMoney(money);
        UserFactory.syncUser(user);
        logger.info("User <{}> add money successfully, current money is: {}", user.getName(), user.getMoney());
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> {
            ret.put("money", user.getMoney());
            callback.success(ret);
        });
    }

    /**
     * 为用户购买某航班的机票
     *
     * @param p         用户实体
     * @param planeName 航班名
     * @param level     舱位等级
     * @param callback  回调
     */
    public static void buyTicket(User p, String planeName, int level, CallBackInterface callback) {
        logger.info("UI request for buy plane <{}> ticket for user, and ticket level is {}", planeName, level);
        Map<String, Object> ret = new HashMap<>();
        if (p == null) {
            SwingUtilities.invokeLater(() -> {
                logger.info("Process failed: user does not login");
                ret.put("msg", "用户未登录！");
                callback.failed(ret);
            });
            return;
        }
        logger.info("User <{}> is trying to buy a ticket for airplane <{}>", p.getName(), planeName);
        if (p.hasTicket(planeName)) {
            SwingUtilities.invokeLater(() -> {
                ret.put("msg", "不能重复购买同一航班！");
                callback.failed(ret);
            });
            logger.info("Process failed: user <{}> already has ticket for plane <{}>", p.getName(), planeName);
            return;
        }
        Plane plane = FlightFactory.search(planeName);
        if (plane == null) {
            SwingUtilities.invokeLater(() -> {
                ret.put("msg", "航班不存在！");
                callback.failed(ret);
            });
            logger.info("Process failed: plane <{}> is not existed", planeName);
            return;
        }
        int needMoney, remainTickets;
        if (level == 1) {
            needMoney = plane.getLevel1Price();
            remainTickets = plane.getRemainLevel1TicketNumber();
        } else if (level == 2) {
            needMoney = plane.getLevel2Price();
            remainTickets = plane.getRemainLevel2TicketNumber();
        } else {
            needMoney = plane.getLevel3Price();
            remainTickets = plane.getRemainLevel3TicketNumber();
        }
        if (needMoney > p.getMoney()) {
            SwingUtilities.invokeLater(() -> {
                ret.put("msg", "余额不足！");
                callback.failed(ret);
            });
            logger.info("Process failed: user <{}> doesn't have enough money", p.getName());
            return;
        }
        if (remainTickets <= 0) {
            SwingUtilities.invokeLater(() -> {
                ret.put("msg", "票已售空！");
                callback.failed(ret);
            });
            logger.info("Process failed: ticket has been sold out");
            return;
        }
        TicketFactory.addTicket(plane, p, level);
        logger.info("User <{}> buy ticket for airplane <{}> successfully, ticket level is <{}>", p.getName(), planeName, level);
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> {
            ret.put("user", p);
            ret.put("plane", plane);
            callback.success(ret);
        });
    }

    /**
     * 为用户购买某航班的机票
     *
     * @param account   用户账号
     * @param planeName 航班名
     * @param level     舱位等级
     * @param callback  回调
     */
    public static void buyTicket(String account, String planeName, int level, CallBackInterface callback) {
        Map<String, Object> ret = new HashMap<>();
        if (!UserFactory.isUserExisted(account)) {
            ret.put("msg", "用户不存在！");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        User user = UserFactory.search(account);
        buyTicket(user, planeName, level, callback);
    }

    /**
     * 为用户购买某航班的机票
     *
     * @param planeName 航班名
     * @param level     舱位等级
     * @param callback  回调
     */
    public static void buyTicket(String planeName, int level, CallBackInterface callback) {
        buyTicket(user, planeName, level, callback);
    }

    /**
     * 用户退票
     *
     * @param planeName 航班名
     * @param callback  回调
     */
    public static void removeTicket(String planeName, CallBackInterface callback) {
        logger.info("UI request for remove plane <{}> ticket for user <{}>", planeName, user.getId());
        Map<String, Object> ret = new HashMap<>();
        if (!user.hasTicket(planeName)) {
            SwingUtilities.invokeLater(() -> {
                ret.put("msg", "机票不存在！");
                logger.info("Remove failed: invalid ticket");
                callback.failed(ret);
            });
            return;
        }
        Ticket ticket = user.getTicket(planeName);
        TicketFactory.removeTicket(ticket.getId());
        Plane plane = FlightFactory.search(planeName);
        if (ticket.getLevelID() == 1)
            plane.setRemainLevel1TicketNumber(plane.getRemainLevel1TicketNumber() + 1);
        else if (ticket.getLevelID() == 2)
            plane.setRemainLevel2TicketNumber(plane.getRemainLevel2TicketNumber() + 1);
        else
            plane.setRemainLevel3TicketNumber(plane.getRemainLevel3TicketNumber() + 1);
        FlightFactory.syncFlight(plane);
        user.removeTicket(planeName);
        user.addMoney(ticket.getPrice());
        UserFactory.syncUser(user);
        ret.put("user", user);
        ret.put("plane", plane);
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 获取已登录的用户信息
     *
     * @param callback 回调
     */
    public static void getUserInfo(CallBackInterface callback) {
        logger.info("UI request for get user information");
        Map<String, Object> ret = new HashMap<>();
        ret.put("user", user);
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }
}
