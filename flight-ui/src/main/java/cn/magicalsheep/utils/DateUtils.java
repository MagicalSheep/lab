package cn.magicalsheep.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    /**
     * 将日期字符串转换为Date类，注意：格式错误时返回当前时间
     *
     * @param date   要转换的字符串
     * @param format 格式
     * @return Date
     */
    public static Date getDate(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 将日期字符串转换为Date类，格式为：yyyy-MM-dd HH:mm
     * 注意：格式错误时返回当前时间
     *
     * @param date 要转换的字符串
     * @return Date
     */
    public static Date getDate(String date) {
        return getDate(date, "yyyy-MM-dd HH:mm");
    }

    public static String fromDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(date);
    }

    public static String fromDate(Date date) {
        return fromDate(date, "yyyy-MM-dd HH:mm");
    }
}
