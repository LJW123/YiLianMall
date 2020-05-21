package com.yilian.mylibrary;

/**
 * @author xiaofo on 2018/9/13.
 */

public class NumberUtil {
    /**
     * 判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}
