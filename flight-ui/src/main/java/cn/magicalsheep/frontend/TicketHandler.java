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

public class TicketHandler {

    private static final Logger logger = LogManager.getLogger();

    /**
     * 向数据源请求所有机票信息
     *
     * @param callback 回调
     */
    public static void getTicketInfo(CallBackInterface callback) {
        logger.info("UI request for all tickets information");
        Map<String, Object> ret = new HashMap<>();
        ret.put("res", TicketFactory.searchAll());
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }

    /**
     * 从数据源中移除一张机票
     *
     * @param ticketID 机票ID
     * @param callback 回调
     */
    public static void removeTicket(int ticketID, CallBackInterface callback) {
        logger.info("UI request for remove ticket <{}>", ticketID);
        Map<String, Object> ret = new HashMap<>();
        if (!TicketFactory.isTicketExisted(ticketID)) {
            ret.put("msg", "机票不存在！");
            logger.info("Remove ticket failed: invalid ticket");
            SwingUtilities.invokeLater(() -> callback.failed(ret));
            return;
        }
        Ticket ticket = TicketFactory.search(ticketID);
        String planeName = ticket.getCompany() + " " + ticket.getName();
        Plane plane = FlightFactory.search(planeName);
        if (ticket.getLevelID() == 1)
            plane.setRemainLevel1TicketNumber(plane.getRemainLevel1TicketNumber() + 1);
        else if (ticket.getLevelID() == 2)
            plane.setRemainLevel2TicketNumber(plane.getRemainLevel2TicketNumber() + 1);
        else
            plane.setRemainLevel3TicketNumber(plane.getRemainLevel3TicketNumber() + 1);
        FlightFactory.syncFlight(plane);
        User user = UserFactory.search(ticket.getOwnerID());
        user.removeTicket(planeName);
        user.addMoney(ticket.getPrice());
        UserFactory.syncUser(user);
        TicketFactory.removeTicket(ticketID);
        logger.info("Process completed");
        SwingUtilities.invokeLater(() -> callback.success(ret));
    }
}
