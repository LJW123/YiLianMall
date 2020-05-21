package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 京东首页通知
 * @author Created by zhaiyaohua on 2018/6/20.
 */

public class JdHomePageNoticeEntity extends HttpResultBean {

    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        /**
         * id : 2
         * title : 通知:从天津快来发哈开放后打开凤凰 发货快复活卡收到货费付货款粉红色大富豪闪电发货sad护肤开始放假卡萨丁护肤开始发哈萨克地方哈迪斯客户oooooo
         * time : 2
         */

        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("time")
        public String time;
    }
}
