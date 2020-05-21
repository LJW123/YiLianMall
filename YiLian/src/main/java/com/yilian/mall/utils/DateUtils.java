package com.yilian.mall.utils;

import com.orhanobut.logger.Logger;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * 根据时间戳获取星期几
     *
     * @param stamp 时间戳 必须是毫秒级
     * @return
     */
    public static int getWeekByStamp(long stamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(stamp);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return week;
    }

    /**
     * 日期时间字符串转化为Date类型
     *
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    /**
     * 返回unix时间戳 (1970年至今的秒数)
     *
     * @return
     */
    public static long getUnixStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 得到昨天的日期
     *
     * @return
     */
    public static String getYestoryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * 得到今天的日期
     *
     * @return
     */
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    /*
    * 将时间戳转换为时间 yyyy-MM-dd HH:mm:ss
    */
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 时间戳转化为时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
     * 时间戳转化为时间格式MM-dd
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStrMD(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }
    /**
     * 时间戳转化为时间格式HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr2(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 时间戳转化为时间格式HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr5(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }


    /**
     * 时间戳转化为时间格式MM-dd HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr3(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

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

    public static String getSecond(long millis) {
        long totalSeconds = millis / 1000;
        String second = (int) (totalSeconds % 60) + "";// 秒
        long totalMinutes = totalSeconds / 60;
        String minute = (int) (totalMinutes % 60) + "";// 分
        long totalHours = totalMinutes / 60;
        String hour = (int) (totalHours % 24) + "";// 时
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(totalHours + totalMinutes + totalSeconds * 1000);
    }

    public static String getHmsStr(long time) {
        long value = time / 1000;
        long hour = value / 3600;//小时
        long minute = value % 3600 / 60;//分钟
        long ss = value % 3600 % 60;//秒

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format((hour + minute + ss) * 1000);
    }

    /**
     * 时间戳转为HH:mm:ss格式
     *
     * @param time
     * @return
     */
    public static String mmToDate(long time) {
        String date = null;
        long Day = time / (24 * 60 * 60 * 1000);
        long hour = (time - Day * 24 * 60 * 60 * 1000) / (60 * 60 * 1000);
        long mintue = (time - Day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000) / (60 * 1000);
        long ss = (time - Day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - mintue * 60 * 1000) / 1000;

        date = Day + "天" + hour + "时" + mintue + "分" + ss + "秒";
        return date;
    }

    /**
     * 带毫秒日期格式化
     *
     * @param str
     * @return
     */
    public static String formatDateS(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss.SSSS");
        Logger.i(str.replace(".", ""));

        String time = sdf.format(new Date(NumberFormat.convertToLong(str.replace(".", ""), 0L)));
        return time;
    }

    /**
     * 得到日期   yyyy-MM-dd
     *
     * @param timeStamp 时间戳
     * @return
     */
    public static String formatDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }


    /**
     * 得到时间  HH:mm:ss
     *
     * @param timeStamp 时间戳
     * @return
     */
    public static String getTime(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }


    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;
        return time / 60 + "";
    }

    public static String getHms(long timer) {
        int subS = (int) (timer / 1000);  //总共的 秒数差

        int s = subS % 60;  // 秒
        int m = subS / 60 % 60;            //  分
        int h = subS / 60 / 60;            // 时
        String strH = "";
        String strM = "";
        String strS = "";
        return getStr(h, strH) + ":" + getStr(m, strM) + ":" + getStr(s, strS);
    }

    private static String getStr(int s, String str) {
        if (s < 10) {
            str = "0" + s;
        } else {
            str = String.valueOf(s);
        }
        return str;
    }


    /**
     * 毫秒值转换为时分秒 HH:MM:ss
     *
     * @param ms
     * @return
     */
    public static String millisecondToTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = (ms - day * dd - hour * hh - minute * mi - second * ss) / 10;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
//        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
        if (NumberFormat.convertToInt(strHour, 0) > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
        } else {
            return strMinute + ":" + strSecond + "." + strMilliSecond;
        }
    }

    /**
     * 获取当前月份
     */
    public static int getMonth() {
        Calendar c = Calendar.getInstance();
        int month = (c.get(Calendar.MONTH)) + 1;
        return month;
    }

    /**
     * 获取当前年份
     */
    public static int getYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year;
    }

}
