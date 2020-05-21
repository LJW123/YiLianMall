package com.yilian.mylibrary;

import android.widget.EditText;

import java.math.BigDecimal;

/**
 * Created by  on 2017/6/13 0013.
 */

public class DecimalUtil {
    /**
     * 限制输入框内为小数点后两位
     *
     * @param s
     */
    public static void keep2Decimal(CharSequence s, EditText etMoney) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                etMoney.setText(s);
                etMoney.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            etMoney.setText(s);
            etMoney.setSelection(2);
        }

        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                etMoney.setText(s.subSequence(0, 1));
                etMoney.setSelection(1);
                return;
            }
        }
    }

    /**
     *  限制输入框内为小数点后两位
     * @param s
     * @param etMoney
     * @return 是否符合规范
     */
    public static boolean keep2DecimalByReturn(CharSequence s, EditText etMoney) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                etMoney.setText(s);
                etMoney.setSelection(s.length());
                return false;
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            etMoney.setText(s);
            etMoney.setSelection(2);
            return false;
        }

        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                etMoney.setText(s.subSequence(0, 1));
                etMoney.setSelection(1);
                return false;
            }
        }
        return true;
    }

    /**
     * 限制输入框内为小数点后length位
     *
     * @param s
     * @param leng 小数点后的长度
     */
    public static void keepDecimal(CharSequence s, EditText etMoney, int leng) {
        if (s.toString().contains(".")) {//若输入数字包括小数点，则保留小数点后两位
            if (s.length() - 1 - s.toString().indexOf(".") > leng) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + leng + 1);
                etMoney.setText(s);
                etMoney.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {//若输入以小数点开头则前面补0
            s = "0" + s;
            etMoney.setText(s);
            etMoney.setSelection(2);
        }

        if (s.toString().startsWith("0")//若输入以0开头，第二位不是小数点，则删除第二位，只保留输入0
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                etMoney.setText(s.subSequence(0, 1));
                etMoney.setSelection(1);
                return;
            }
        }
    }
    /**
     * 防止数据相乘会出现计算机计数法的数据
     * @param value
     * @param cardinalNumber
     * @return
     */
    public static String convertDoubleToString(double value, double cardinalNumber) {
        BigDecimal baseVaule = new BigDecimal(Double.toString(value));
        BigDecimal cardinalNumbers = new BigDecimal(Double.toString(cardinalNumber));
        BigDecimal result = baseVaule.multiply(cardinalNumbers);
        return String.valueOf(result);
    }
}
