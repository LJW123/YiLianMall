package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/5/26.
 */

public class JDShippingAddressList extends HttpResultBean {
    @SerializedName("data")
   public List<JDShippingAddressInfoEntity.DataBean> list;
}
