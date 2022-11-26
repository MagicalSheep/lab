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
import java.util.*;

public class FlightHandler {

    private static final Logger logger = LogManager.getLogger();

    /**
     * 从数据源中查找某日从某地出发，到达某地的所有航班
     *
     * @param from     始发地
     * @param to       终点地
     * @param date     出发时间
     * @param callback 回调
     */
    public static void search(String from, String to, Date date, CallBackInterface callback) {
        logger.info("UI request for search for flight from <{}> to <{}> at <{}>", from, to, date);
        Map<String, Object> ret = new HashMap<>();
        List<Plane> res = FlightFactory.search(from, to, date);
        ret.put("result", res);
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 从数据源中获取某航班的信息
     *
     * @param planeName 航班名
     * @param callback  回调
     */
    public static void getFlightInfo(String planeName, CallBackInterface callback) {
        logger.info("UI request for flight <{}> information", planeName);
        Map<String, Object> ret = new HashMap<>();
        ret.put("plane", FlightFactory.search(planeName));
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 从数据源中获取所有航班的信息
     *
     * @param callback 回调
     */
    public static void getFlightInfo(CallBackInterface callback) {
        logger.info("UI request for all flight information");
        Map<String, Object> ret = new HashMap<>();
        ret.put("res", FlightFactory.searchAll());
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 修改数据源中某航班的信息
     *
     * @param plane    航班实体
     * @param callback 回调
     */
    public static void modifyFlight(Plane plane, CallBackInterface callback) {
        logger.info("UI request for modifying plane <{}> information", plane.getCompany() + " " + plane.getName());
        Map<String, Object> ret = new HashMap<>();
        if (!FlightFactory.isPlaneExisted(plane.getCompany() + " " + plane.getName())) {
            ret.put("msg", "航班不存在！");
            logger.info("Modify flight information failed: invalid plane");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        FlightFactory.syncFlight(plane);
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 向数据源中添加新航班
     *
     * @param plane    航班实体
     * @param callback 回调
     */
    public static void addFlight(Plane plane, CallBackInterface callback) {
        logger.info("UI request for add a new flight <{}>", plane.getCompany() + " " + plane.getName());
        Map<String, Object> ret = new HashMap<>();
        if (FlightFactory.isPlaneExisted(plane.getCompany() + " " + plane.getName())) {
            ret.put("msg", "航班已存在，无法添加新航班！");
            logger.info("Add flight failed: plane is already existed");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        FlightFactory.syncFlight(plane);
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 从数据源中删除某航班
     *
     * @param planeName 航班名
     * @param callback  回调
     */
    public static void removeFlight(String planeName, CallBackInterface callback) {
        logger.info("UI request for removing the flight <{}>", planeName);
        Map<String, Object> ret = new HashMap<>();
        if (!FlightFactory.isPlaneExisted(planeName)) {
            ret.put("msg", "航班不存在！");
            logger.info("Remove flight failed: plane is not existed");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        FlightFactory.removeFlight(planeName);
        List<Ticket> tickets = TicketFactory.searchAll(planeName);
        for (Ticket ticket : tickets) {
            TicketFactory.removeTicket(ticket.getId());
            User user = UserFactory.search(ticket.getOwnerID());
            user.removeTicket(planeName);
            user.addMoney(ticket.getPrice());
            UserFactory.syncUser(user);
        }
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 校验航班实体的属性值是否有效
     *
     * @param plane    航班实体
     * @param callback 回调
     */
    public static void validate(Plane plane, CallBackInterface callback) {
        logger.info("UI request for validating the plane information");
        Map<String, Object> ret = new HashMap<>();
        if (plane.getLevel1Price() < 0 || plane.getLevel2Price() < 0 || plane.getLevel3Price() < 0) {
            ret.put("msg", "机票价格不能为负数！");
            logger.info("Validate failed: price can not be negative number");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        if (plane.getLevel1TicketNumber() < 0 || plane.getLevel2TicketNumber() < 0 || plane.getLevel3TicketNumber() < 0) {
            ret.put("msg", "乘员定额不能为负数！");
            logger.info("Validate failed: passenger number can not be negative number");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        if (plane.getLevel1TicketNumber() == 0 && plane.getLevel2TicketNumber() == 0 && plane.getLevel3TicketNumber() == 0) {
            ret.put("msg", "乘员定额总数不能为0！");
            logger.info("Validate failed: total passenger number can not be zero");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        if (plane.getRemainLevel1TicketNumber() > plane.getLevel1TicketNumber()) {
            ret.put("msg", "经济舱剩余票数不能大于总票数！");
            logger.info("Validate failed: remain level 1 ticket's number can not be greater than the total");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        if (plane.getRemainLevel2TicketNumber() > plane.getLevel2TicketNumber()) {
            ret.put("msg", "公务舱剩余票数不能大于总票数！");
            logger.info("Validate failed: remain level 2 ticket's number can not be greater than the total");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        if (plane.getRemainLevel3TicketNumber() > plane.getLevel3TicketNumber()) {
            ret.put("msg", "头等舱剩余票数不能大于总票数！");
            logger.info("Validate failed: remain level 3 ticket's number can not be greater than the total");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        if ((plane.getEndTime().getTime() - plane.getStartTime().getTime()) <= 0) {
            ret.put("msg", "到达时间不能小于起飞时间！");
            logger.info("Validate failed: arriving time can not be less than the leaving time");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        if (plane.getStartTime().getTime() < System.currentTimeMillis()) {
            ret.put("msg", "起飞时间不能小于当前时间！");
            logger.info("Validate failed: leaving time can not be less than the current time");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        logger.info("Validate pass");
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }
}
