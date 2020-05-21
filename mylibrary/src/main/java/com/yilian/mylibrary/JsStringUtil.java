package com.yilian.mylibrary;

/**
 * @atuhor Created by  on 2017/10/30 0030.
 */

public class JsStringUtil {
    /**
     * 通过js方法获取的字符串返回值，不是标准的json ,去掉所有的反斜杠和首尾两个双引号，即为标准json字符串
     *
     * @param jsString
     * @return
     */
    public static String getJsonString(String jsString) {
        String substring = jsString.substring(1, jsString.length() - 1);
        return substring.replace("\\", "");
    }
}
