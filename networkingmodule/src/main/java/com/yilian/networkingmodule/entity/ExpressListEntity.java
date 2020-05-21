package com.yilian.networkingmodule.entity;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by LYQ on 2017/10/9.
 */

public class ExpressListEntity extends HttpResultBean  {

    @SerializedName("list")
    public List<ListBean> list;



    public static class ListBean implements IPickerViewData{
        /**
         * id : 24
         * express_id : ems
         * express_name : EMS快递
         * add_time : 1504056381
         * is_del : 0
         * express_price : 0
         * express_pic :
         * is_used : 0
         */

        @SerializedName("id")
        public String id;
        @SerializedName("express_id")
        public String expressId;
        @SerializedName("express_name")
        public String expressName;
        @SerializedName("add_time")
        public String addTime;
        @SerializedName("is_del")
        public String isDel;
        @SerializedName("express_price")
        public String expressPrice;
        @SerializedName("express_pic")
        public String expressPic;
        @SerializedName("is_used")
        public String isUsed;

        @Override
        public String getPickerViewText() {
            return expressName;
        }
    }
}
