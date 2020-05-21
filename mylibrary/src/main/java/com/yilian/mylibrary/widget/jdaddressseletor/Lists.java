package com.yilian.mylibrary.widget.jdaddressseletor;
import java.util.List;

/**
 * @author Created by  on 2018/5/25.
 */


public class Lists {
    public Lists() {
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }
}