package com.yilian.networkingmodule.entity;


import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 返回服务商图片
 * Created by Administrator on 2018/2/3 0003.
 */

public class MerchantServiceProviderBean extends HttpResultBean {

    /**
     * data : [{"description":"成就个人百万梦","id":1,"img_src":"http://aaa","name":"合作服务商","type":1},{"description":"与你身边的商户共同创造价值财富","id":2,"img_src":"http://bbb","name":"城市/县区服务商","type":2},{"description":"平台帮您达到整个城市的商业服务中心","id":3,"img_src":"http://ccc","name":"省级/城市服务中心","type":3}]
     * datas : null
     * desc : null
     * outputPage : null
     */

    @SerializedName("datas")
    public Object datas;
    @SerializedName("desc")
    public Object desc;
    @SerializedName("outputPage")
    public Object outputPage;
    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * description : 成就个人百万梦
         * id : 1
         * img_src : http://aaa
         * name : 合作服务商
         * type : 1
         */

        @SerializedName("description")
        public String description;
        @SerializedName("id")
        public int id;
        @SerializedName("img_src")
        public String imgSrc;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public int type;
    }
}
