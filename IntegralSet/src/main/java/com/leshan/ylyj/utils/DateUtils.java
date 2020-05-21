package com.leshan.ylyj.utils;

import com.yilian.mylibrary.TimeConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    /**
     * 时间戳转化为时间格式 MM月dd日 HH:mm
     * @param timeStamp 时间戳
     * @return
     */
    public static String timeStampToStr4(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 时间戳转化为时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToMDHM(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 判断是否今天
     *
     * @param millis 毫秒时间戳
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isToday(final long millis) {
        long wee = getWeeOfToday();
        return millis >= wee && millis < wee + TimeConstants.DAY;
    }



    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
