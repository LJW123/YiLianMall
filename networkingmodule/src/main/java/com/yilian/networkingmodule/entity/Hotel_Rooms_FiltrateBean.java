package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.entity.ctrip.CtripRoomTypeInfo;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/10/18 16:42
 */

public class Hotel_Rooms_FiltrateBean extends HttpResultBean {
    @SerializedName("data")
    public List<CtripRoomTypeInfo> roomTypeInfos;
}
