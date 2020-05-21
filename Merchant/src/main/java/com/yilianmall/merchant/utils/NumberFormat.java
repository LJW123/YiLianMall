package com.yilianmall.merchant.utils;

import android.widget.EditText;

import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;

/**
 * Created by lauyk on 2015/11/17.
 */
public class NumberFormat {
    /**
     * @param value
     * @param defaultValue
     * @return integer
     * @throws
     * @Description: 对象转化为int类型
     */
    public final static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    /**
     * @param value
     * @param defaultValue
     * @return float
     * @throws
     * @Description: 对象转化为float类型
     */
    public final static float convertToFloat(Object value, float defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Float.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    /**
     * 保留两位小数
     * @param value
     * @param defaultValue
     * @return
     */
    public static double convertToDouble2(Object value, double defaultValue) {
        if (value == null || "".equals(defaultValue)) {
            return defaultValue;
        }
        double aDouble = Double.parseDouble(value.toString());
        DecimalFormat df = new DecimalFormat("#.00");
        String format = df.format(aDouble);
        Logger.i(format);
        return Double.parseDouble(format);
    }

    /**
     * @param value
     * @param defaultValue
     * @return float
     * @throws
     * @Description: 对象转化为float类型
     */
    public final static double convertToDouble(Object value, Double defaultValue) {
//        Logger.i("要处理的值："+value);
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        Double aDouble = Double.valueOf(value.toString());
        try {
//            Logger.i("处理后的结果："+aDouble);
            return aDouble;
        } catch (Exception e) {
            try {
//                Logger.i("处理后的结果："+aDouble);
                return aDouble.intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }


    /**
     * @param value
     * @param defaultValue
     * @return long
     * @throws
     * @Description: 对象转化为long类型
     */
    public final static long convertToLong(Object value, Long defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            Logger.i(String.valueOf(defaultValue));
            return defaultValue;
        }
        try {
            return Long.valueOf(value.toString());
        } catch (Exception e) {
            Logger.i(e.toString());
            return defaultValue;
        }
    }

    /**
     * 输入的数字保留两位小数
     *
     * @param s
     * @param editText
     */
    public static void keepTwoDecimal(String s, EditText editText) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.subSequence(0, s.indexOf(".") + 3).toString();
                editText.setText(s);
                editText.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }

    }

    //计算总人数
    public static String convertToTenThousand(String groupCondition, String totalNumber) {
        String totalNumberStr;
        int totalNumberPeople = Math.round(Integer.parseInt(groupCondition) * Integer.parseInt(totalNumber));
        totalNumberStr = String.valueOf(totalNumberPeople);
        Logger.i("计算总人数 "+totalNumberPeople +"\ngroupCondition "+groupCondition+"\ntotalNumber "+totalNumber);
        if (totalNumberStr.length() > 4) {
            for (int i = 0; i < totalNumberStr.length(); i++) {
                if (i < 1) {
                    totalNumberStr += i + ".";
                }else if (i==1){
                    totalNumberStr+=i+"万";
                }
            }
        }
        Logger.i("totalNumberStr  "+totalNumberStr);
        return totalNumberStr;
    }

    /**
     * double 转成int 并四舍五入
     * @param value
     * @return
     */
    public static String doubleConvertInt(double value){
        DecimalFormat df=new DecimalFormat("######0");//四舍五入转成整数
        return df.format(value);
    }
}
