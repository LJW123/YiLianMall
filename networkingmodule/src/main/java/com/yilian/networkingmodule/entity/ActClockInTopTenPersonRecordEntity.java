package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/23 0023.
 */

public class ActClockInTopTenPersonRecordEntity extends HttpResultBean {

    /**
     code: 1,
     msg: "获取信息成功",
     name: "星星",//昵称
     photo: "app/images/head/20171109/7435908194499807_9657332_.jpg",//头像
     recordList: [//打卡记录
     */

    @SerializedName("name")
    public String name;
    @SerializedName("photo")
    public String photo;
    @SerializedName("recordList")
    public List<ActClockInMyRecordEntity.RecordListBean> recordList;

}
