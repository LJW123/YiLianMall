package com.yilian.mylibrary.widget;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Ray_L_Pain on 2017/10/18 0018.
 */

public class MyBigDecimal {

    public static String add(double d1, double d2) { // 进行加法运算
        String st;
        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        st = df.format(b1.add(b2).doubleValue());
        return st;
    }

    public static double sub(double d1, double d2) { // 进行减法运算
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();
    }


    public static double mul(double d1, double d2) { // 进行乘法运算
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double d1, double d2, int len) {// 进行除法运算
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double div1(double d1, double d2, int len) {// 进行除法运算 向上取整
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.divide(b2, len, BigDecimal.ROUND_UP).doubleValue();
    }


    public static double round(double d, int len) {     // 进行四舍五入操作
        BigDecimal b1 = new BigDecimal(String.valueOf(d));
        BigDecimal b2 = new BigDecimal("1");
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量，表示进行四舍五入的操作
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 字符串进行乘法计算并返回非指数形式的结果
     *
     * @param s1
     * @param s2
     * @return
     */
    public static String mul(String s1, String s2) {
        BigDecimal bigDecimal1 = new BigDecimal(s1);
        BigDecimal bigDecimal2 = new BigDecimal(s2);
        return bigDecimal1.multiply(bigDecimal2).toPlainString();
    }

    public static String sub(String s1, String s2) { // 进行减法运算
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return b1.subtract(b2).toPlainString();
    }

    public static String add(String s1, String s2) {
        BigDecimal bigDecimal1 = new BigDecimal(s1);
        BigDecimal bigDecimal2 = new BigDecimal(s2);
        return bigDecimal1.add(bigDecimal2).toPlainString();
    }


    /**
     * 除法运算
     *
     * @param d1
     * @param d2
     * @param scale     保留小数点位数
     * @param roundMode BigDecimal.ROUND_HALF_UP（四舍五入）
     *                  BigDecimal.ROUND_DOWN(截取)
     *                  BigDecimal.ROUND_UP(向上加1)
     *                  BigDecimal.ROUND_HALF_EVEN(5舍六入)
     * @return
     */
    public static String div3(double d1, double d2, Integer roundMode, Integer scale) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        BigDecimal resultDecimal = b1.divide(b2);
        if (roundMode != null && scale != null) {
            resultDecimal.setScale(scale, roundMode);
        }
        return resultDecimal.toPlainString();
    }

    public static String div3(String d1, String d2, Integer roundMode, Integer scale) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 =new BigDecimal(d2);
        BigDecimal resultDecimal = b1.divide(b2);
        if (roundMode != null && scale != null) {
            resultDecimal.setScale(scale, roundMode);
        }
        return resultDecimal.toPlainString();
    }

}
