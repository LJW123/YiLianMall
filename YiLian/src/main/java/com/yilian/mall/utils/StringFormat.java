package com.yilian.mall.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 刘玉坤 on 2016/2/27.
 */
public class StringFormat {

    private static String time;

    public static String formatDate(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String time = sdf.format(new Date(NumberFormat.convertToLong(str + "000", 0L)));

        return time;
    }

    public static String formatDate(String str, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        String time = sdf.format(new Date(NumberFormat.convertToLong(str + "000", 0L)));

        return time;
    }

    /**
     * 带毫秒日期格式化
     *
     * @param str
     * @return
     */
    public static String formatDateS(String str) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        String s = String.format("%.3f", Double.parseDouble(str));

        String a = s.replace(".", "");
        String time = sdf.format(new Date(NumberFormat.convertToLong(a, 0L)));
        return time;
    }

    /**
     * 带毫秒日期格式化
     *
     * @param str
     * @return
     */
    public static String formatDateS(String str, String format) {


        SimpleDateFormat sdf = new SimpleDateFormat(format);

        String s = String.format("%.3f", Double.parseDouble(str));

        String a = s.replace(".", "");
        String time = sdf.format(new Date(NumberFormat.convertToLong(a, 0L)));
        return time;
    }

    public static String formatDateL(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String time0 = sdf.parse(str).getTime() + "";
            time = time0.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String formatNum(int time) {
        return time < 10 ? "0" + time : String.valueOf(time);
    }

    public static String formatMillisecond(int millisecond) {
        String retMillisecondStr;

        if (millisecond > 99) {
            retMillisecondStr = String.valueOf(millisecond / 10);
        } else if (millisecond <= 9) {
            retMillisecondStr = "0" + millisecond;
        } else {
            retMillisecondStr = String.valueOf(millisecond);
        }

        return retMillisecondStr;
    }

    /**
     * 系统时间日期格式化
     * @param str
     * @param format
     * @return
     */
    public static String formatDateE(long str, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        String time = sdf.format(new Date(str));

        return time;
    }

    //从字符串中取出数字
    public static String getNumber(String text){
        String number = "";
        Pattern pattern=Pattern.compile("(\\d+)");
        Matcher matcher=pattern.matcher(text);
        while(matcher.find()){
            String find=matcher.group(1).toString();
            number+=find;
        }
        return number;
    }

}
