package com.yilian.networkingmodule.entity;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by  on 2017/5/15 0015.
 * 行业分类
 */

public class IndustryEntity extends BaseEntity {

    /**
     * code :
     * list : [{"industry_id":"3","industry_name":"美食","industry_desp":"中餐、西餐、日韩料理、甜点饮品、咖啡酒吧等","industry_parent":"0","son_industry":[{"industry_id":"13","industry_name":"火锅","industry_desp":"各种火锅","industry_parent":"3"},{}]},{}]
     */

    @SerializedName("list")
    public ArrayList<IndustryBean> list;

    public static class IndustryBean implements IPickerViewData {
        /**
         * industry_id : 3
         * industry_name : 美食
         * industry_desp : 中餐、西餐、日韩料理、甜点饮品、咖啡酒吧等
         * industry_parent : 0
         * son_industry : [{"industry_id":"13","industry_name":"火锅","industry_desp":"各种火锅","industry_parent":"3"},{}]
         */

        @SerializedName("industry_id")
        public String industryId;
        @SerializedName("industry_name")
        public String industryName;
        @SerializedName("industry_desp")
        public String industryDesp;
        @SerializedName("industry_parent")
        public String industryParent;
        @SerializedName("son_industry")
        public ArrayList<SonIndustryBean> sonIndustry;

        @Override
        public String getPickerViewText() {
            return industryName;
        }

        public static class SonIndustryBean implements IPickerViewData {
            /**
             * industry_id : 13
             * industry_name : 火锅
             * industry_desp : 各种火锅
             * industry_parent : 3
             */

            @SerializedName("industry_id")
            public String industryId;
            @SerializedName("industry_name")
            public String industryName;
            @SerializedName("industry_desp")
            public String industryDesp;
            @SerializedName("industry_parent")
            public String industryParent;

            @Override
            public String getPickerViewText() {
                return industryName;
            }
        }
    }
}
