package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.HashMap;

/**
 * @author Created by  on 2018/5/25.
 */

public class JDAreaStringData extends HttpResultBean {

    /**
     * data : {"登封市":416,"金水区":3545,"经济开发区":47300,"惠济区":3544,"荥阳市":46822,"新郑市":46820,"管城区":3546,"上街区":2782,"郑东新区":4337,"中牟县":46823,"二七区":3547,"中原区":3548,"巩义市":46821,"新密市":415,"高新技术开发区":47301}
     */

    @SerializedName("data")
    public HashMap<String,String> data;
}
