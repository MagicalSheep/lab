package cn.magicalsheep.backend;

import cn.magicalsheep.model.Plane;
import cn.magicalsheep.utils.DateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FlightFactory {

    private static final Logger logger = LogManager.getLogger();
    private static final Map<String, Plane> sources = new HashMap<>();
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();

    static {
        logger.info("Flight factory is initializing...");
        Plane[] planes = gson.fromJson(new InputStreamReader(Objects.requireNonNull(FlightFactory.class.getResourceAsStream("/data.json")), StandardCharsets.UTF_8), Plane[].class);
        for (Plane plane : planes)
            sources.put(plane.getCompany() + " " + plane.getName(), plane);
        logger.info("Flight factory initialize completed");
    }

    /**
     * 判断航班是否存在
     *
     * @param planeName 航班名
     * @return 是否存在
     */
    public static boolean isPlaneExisted(String planeName) {
        logger.info("Check whether plane <{}> is existed in flight factory", planeName);
        return sources.containsKey(planeName);
    }

    /**
     * 查找某个航班
     *
     * @param planeName 航班名
     * @return 航班实体
     */
    public static Plane search(String planeName) {
        logger.info("Search for plane <{}> in flight factory", planeName);
        return sources.getOrDefault(planeName, null);
    }

    /**
     * 查找某日从某地出发，到某地的所有航班
     *
     * @param from 始发地
     * @param to   终点地
     * @param st   出发时间
     * @return 满足条件的航班列表
     */
    public static List<Plane> search(String from, String to, Date st) {
        logger.info("Search for all planes that from <{}> to <{}> at <{}> in flight factory", from, to, DateUtils.fromDate(st));
        List<Plane> ret = new ArrayList<>();
        for (Plane plane : sources.values()) {
            if (plane.getFrom().equals(from) && plane.getTo().equals(to)
                    && DateUtils.fromDate(plane.getStartTime(), "yyyy-MM-dd").equals(DateUtils.fromDate(st, "yyyy-MM-dd")))
                ret.add(plane);
        }
        logger.info("{} planes have been found", ret.size());
        return ret;
    }

    /**
     * 返回所有航班信息
     *
     * @return 航班列表
     */
    public static List<Plane> searchAll() {
        logger.info("Search for all planes in flight factory");
        return new ArrayList<>(sources.values());
    }

    /**
     * 更新数据源中的航班信息
     *
     * @param plane 航班实体
     */
    public static void syncFlight(Plane plane) {
        logger.info("Update plane <{}> information to flight factory", plane.getCompany() + " " + plane.getName());
        sources.put(plane.getCompany() + " " + plane.getName(), plane);
    }

    /**
     * 从数据源中移除某个航班
     *
     * @param planeName 航班名
     */
    public static void removeFlight(String planeName) {
        logger.info("Remove plane <{}> from flight factory", planeName);
        sources.remove(planeName);
    }
}
