package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Zg
 *         携程 根据城市和区县名称查找对应的id接口
 */
public class CtripAddressTransformEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;


    public static class DataBean {

        /**
         * provinceid: "1",
         * provincename: "北京",
         * city : 1
         * cityname : 北京
         */

        @SerializedName("provinceid")
        public String provinceid;
        @SerializedName("provincename")
        public String provincename;
        @SerializedName("city")
        public String city;
        @SerializedName("cityname")
        public String cityname;

    }


}
