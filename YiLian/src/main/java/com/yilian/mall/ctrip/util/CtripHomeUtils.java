package com.yilian.mall.ctrip.util;

import com.yilian.mylibrary.TimeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 携程 首页使用的一些类
 * Created by Zg on 2018/8/27
 */
public class CtripHomeUtils {

    private static String formatStr = "yyyy-MM-dd";

    /**
     * 根据当前日期获得是周几
     * time=yyyy-MM-dd
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";
        try {
            boolean flag = IsToday(time);
            if (flag == true) {
                //是今天
                Week = "今天";
                return Week;
            } else {
                //不是今天

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int wek = c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += "周日";
        }
        if (wek == 2) {
            Week += "周一";
        }
        if (wek == 3) {
            Week += "周二";
        }
        if (wek == 4) {
            Week += "周三";
        }
        if (wek == 5) {
            Week += "周四";
        }
        if (wek == 6) {
            Week += "周五";
        }
        if (wek == 7) {
            Week += "周六";
        }
        return Week;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间   "2016-06-28"
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = format.parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 时间戳转化为Sting  yyyy-MM-dd
     */
    public static String getDateStr(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(timestamp * 1000);
    }
    /**
     * Date对象 转化为Sting  yyyy-MM-dd
     */
    public static String getDateStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        return formatter.format(date);
    }

    /**
     * 时间戳转化为Sting  MM月dd日
     *
     * @param day 传入的 时间   yyyy-MM-dd
     */
    public static String getDateByMMdd(String day) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date;
        try {
            date = format.parse(day);
            SimpleDateFormat format1 = new SimpleDateFormat("MM月dd日");
            return format1.format(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return day;
        }
    }




    /**
     * 两个日期相差几天
     *
     * @param startTime 入住时间   yyyy-MM-dd
     * @param endTime   离店时间  yyyy-MM-dd
     */
    public static long getTimeDistance(String startTime, String endTime) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(formatStr);
        // 一天的毫秒数
        long nd = 1000 * 24 * 60 * 60;
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            // 计算差多少天
            day = diff / nd;
            // 输出结果
            if (day >= 1) {
                return day;
            } else {
                if (day == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
