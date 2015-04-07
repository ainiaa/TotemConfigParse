package com.coding91.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import jxl.Cell;
import jxl.DateCell;

/**
 *
 * @author Administrator
 */
public class DateTimeUtils {

    /**
     * 格式化
     *
     * @param duration
     * @return
     */
    public static String formatTimeDuration(long duration) {
        return formatTimeDuration(duration, false);
    }

    public static String formatTimeDuration(long duration, boolean showDay) {
        String finalDateString;
        long ssec = duration % 1000;// 毫秒
        long sec = (duration / 1000) % 60;// 秒
        long min = (duration / 1000 / 60) % 60; // 分钟
        long hour = (duration / 1000 / 60 / 60) % 24;// 小时

        if (showDay) {
            long day = duration / 1000 / 60 / 60 / 24;// 天
            finalDateString = String.format("%02d day %02d:%02d:%02d", day, hour, min, sec);
        } else {
            finalDateString = String.format("%02d:%02d:%02d", hour, min, sec);
        }
        return finalDateString;
    }

    public static String formatTime(Cell formatCell) {
        DateCell dateCell = (DateCell) formatCell;
        java.util.Date date = dateCell.getDate();
        //long time = (date.getTime() / 1000) - 60 * 60 * 8;
        TimeZone gmtZone = TimeZone.getTimeZone("GMT");
        //date.setTime(time * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(gmtZone);
        return formatter.format(date);
    }

    /**
     * 将时间字符串转换为时间戳
     *
     * @param timeString
     * @return
     */
    public static long timeStringTotimeStamp(String timeString) {
        return timeStringTotimeStamp(timeString, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将时间字符串转换为时间戳
     *
     * @param timeString
     * @param format
     * @return
     */
    public static long timeStringTotimeStamp(String timeString, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date;
        long timeStamp = 0;
        try {
            date = simpleDateFormat.parse(timeString);
            timeStamp = date.getTime();
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }

        return timeStamp;
    }

    public static String getCurrentTimeString() {
        return getCurrentTimeString("yyyy/MM/dd HH:mm:ss");
    }

    public static String getCurrentTimeString(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);//可以方便地修改日期格式 
        Date now = new Date();
        String currentTimeString = dateFormat.format(now);
        return currentTimeString;
    }

    public static String getTimeString(long nowTimeStamp, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);//可以方便地修改日期格式 
        Date now = new Date(nowTimeStamp);
        String currentTimeString = dateFormat.format(now);
        return currentTimeString;
    }

}
