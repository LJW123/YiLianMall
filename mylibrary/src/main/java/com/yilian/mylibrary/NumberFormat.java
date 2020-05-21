package com.yilian.mylibrary;

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
     * @param value
     * @param defaultValue
     * @return float
     * @throws
     */
    public final static double convertToDouble(Object value, Double defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        Double aDouble = Double.valueOf(value.toString());
        try {
            return aDouble;
        } catch (Exception e) {
            try {
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
    public static String DoubleConvertInt(double value){
        DecimalFormat df=new DecimalFormat("######0");//四舍五入转成整数
        return df.format(value);
    }

    /**
     * 获取两个数的百分比
     * @param numerator 分子
     * @param denominator 分母
     * @return
     */
    public static String getPercent(int numerator, int denominator) {
        String baifenbi = "";// 接受百分比的值  
        double baiy = numerator * 1.0;
        double baiz = denominator * 1.0;
        double fen = baiy / baiz;
        // NumberFormat nf = NumberFormat.getPercentInstance(); 注释掉的也是一种方法  
        // nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位  
        DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
        // 百分比格式，后面不足2位的用0补齐  
        // baifenbi=nf.format(fen);  
        baifenbi = df1.format(fen);
        System.out.println(baifenbi);
        return baifenbi;
    }
}
