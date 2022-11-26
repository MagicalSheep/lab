package cn.magicalsheep.backend;

import cn.magicalsheep.model.Plane;
import cn.magicalsheep.model.Ticket;
import cn.magicalsheep.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketFactory {

    private static int cnt = 1;
    private static final Logger logger = LogManager.getLogger();
    private static final Map<Integer, Ticket> sources = new HashMap<>();

    /**
     * 向数据源中添加机票
     *
     * @param plane 航班实体
     * @param user  用户实体
     * @param level 舱位等级
     */
    public static void addTicket(Plane plane, User user, int level) {
        logger.info("Add plane <{}> ticket for user <{}>, level is {}", plane.getCompany() + " " + plane.getName(), user.getId(), level);
        Ticket ticket = new Ticket(plane, user, level);
        ticket.setId(cnt++);
        sources.put(ticket.getId(), ticket);
        if (level == 1)
            plane.setRemainLevel1TicketNumber(plane.getRemainLevel1TicketNumber() - 1);
        else if (level == 2)
            plane.setRemainLevel2TicketNumber(plane.getRemainLevel2TicketNumber() - 1);
        else
            plane.setRemainLevel3TicketNumber(plane.getRemainLevel3TicketNumber() - 1);
        FlightFactory.syncFlight(plane);
        user.addTicket(ticket);
        user.addMoney(-ticket.getPrice());
        UserFactory.syncUser(user);
    }

    /**
     * 判断机票是否存在
     *
     * @param ticketID 机票ID
     * @return 是否存在
     */
    public static boolean isTicketExisted(int ticketID) {
        logger.info("Check whether ticket <{}> is existed", ticketID);
        return sources.containsKey(ticketID);
    }

    /**
     * 从数据源中移除机票
     *
     * @param id 机票ID
     */
    public static void removeTicket(int id) {
        logger.info("Remove ticket <{}>", id);
        sources.remove(id);
    }

    /**
     * 返回某个航班的所有已订机票
     *
     * @param planeName 航班名
     * @return 满足条件的机票列表
     */
    public static List<Ticket> searchAll(String planeName) {
        logger.info("Search for all tickets for plane <{}>", planeName);
        List<Ticket> res = new ArrayList<>();
        for (Ticket ticket : sources.values())
            if ((ticket.getCompany() + " " + ticket.getName()).equals(planeName))
                res.add(ticket);
        logger.info("{} tickets have been found", res.size());
        return res;
    }

    /**
     * 返回所有机票
     *
     * @return 机票列表
     */
    public static List<Ticket> searchAll() {
        logger.info("Search for all tickets from ticket factory");
        return new ArrayList<>(sources.values());
    }

    /**
     * 查找机票
     *
     * @param ticketID 机票ID
     * @return 机票实体
     */
    public static Ticket search(int ticketID) {
        logger.info("Search for ticket <{}>", ticketID);
        return sources.get(ticketID);
    }
}
