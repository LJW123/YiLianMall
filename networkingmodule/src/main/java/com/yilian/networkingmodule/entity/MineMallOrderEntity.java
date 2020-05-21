package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * 个人中心icons实体类
 *
 * @author Ray_L_Pain
 * @date 2017/12/4 0004
 */

public class MineMallOrderEntity extends HttpResultBean {

    @SerializedName("mallOrder")
    public ArrayList<MallBeanEntity> mallOrder;

}
