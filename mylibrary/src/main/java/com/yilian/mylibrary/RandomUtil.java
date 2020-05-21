package com.yilian.mylibrary;

import java.util.Random;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/11/13 0013
 */

public class RandomUtil {

    /**
     * 生成一个startNum 到 endNum之间的随机数(不包含endNum的随机数)
     * @param startNum
     * @param endNum
     * @return
     */
    public static int getNum(int startNum, int endNum){
        if(endNum > startNum){
            Random random = new Random();
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }
}
