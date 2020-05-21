package com.yilian.mall.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/9/24.
 */
public class ScoreUtil {
    public static String getScroe(String score) {
        Logger.i("score:"+score);
        if("0".equals(score)){
            return "5.0";
        }
        if (score.length() < 2) {
            return "0."+score;
        } else if(score.length()==2){
            return score.substring(0,1)+"."+score.substring(1);
        }else{
            score = score.substring(0, 2);
            Logger.i("score1:"+score);
            return score.substring(0,1)+"."+score.substring(1);
        }

    }
}
