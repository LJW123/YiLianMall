package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请售后提交返回结果
 *
 * @author Created by Zg on 2018/5/28.
 */

public class JDAfterSaleApplyForSubmitEntity extends HttpResultBean {

    @SerializedName("id")
    public String id;
}
