package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author xiaofo on 2018/7/5.
 */

public class JdDefaultAddressListEntity extends HttpResultBean {
    @SerializedName("data")
    public List<JDShippingAddressInfoEntity.DataBean> data;
}
