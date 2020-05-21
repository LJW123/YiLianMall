package com.leshan.ylyj.view.activity.bankmodel;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.google.gson.annotations.SerializedName;
import com.yilian.mylibrary.BaseEntity;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/7/13 0013.
 */

public class JRegionModel extends BaseEntity {


    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean implements IPickerViewData {
        /**
         * region_id : 2
         * parent_id : 1
         * region_name : 北京
         * region_type : 1
         * agency_id : B
         * citys : [{"region_id":"52","parent_id":"2","region_name":"北京","region_type":"2","agency_id":"","countys":[{"region_id":"500","parent_id":"52","region_name":"东城区","region_type":"3","agency_id":""},{"region_id":"501","parent_id":"52","region_name":"西城区","region_type":"3","agency_id":""},{"region_id":"502","parent_id":"52","region_name":"海淀区","region_type":"3","agency_id":""},{"region_id":"503","parent_id":"52","region_name":"朝阳区","region_type":"3","agency_id":""},{"region_id":"504","parent_id":"52","region_name":"崇文区","region_type":"3","agency_id":""},{"region_id":"505","parent_id":"52","region_name":"宣武区","region_type":"3","agency_id":""},{"region_id":"506","parent_id":"52","region_name":"丰台区","region_type":"3","agency_id":""},{"region_id":"507","parent_id":"52","region_name":"石景山区","region_type":"3","agency_id":""},{"region_id":"508","parent_id":"52","region_name":"房山区","region_type":"3","agency_id":""},{"region_id":"509","parent_id":"52","region_name":"门头沟区","region_type":"3","agency_id":""},{"region_id":"510","parent_id":"52","region_name":"通州区","region_type":"3","agency_id":""},{"region_id":"511","parent_id":"52","region_name":"顺义区","region_type":"3","agency_id":""},{"region_id":"512","parent_id":"52","region_name":"昌平区","region_type":"3","agency_id":""},{"region_id":"513","parent_id":"52","region_name":"怀柔区","region_type":"3","agency_id":""},{"region_id":"514","parent_id":"52","region_name":"平谷区","region_type":"3","agency_id":""},{"region_id":"515","parent_id":"52","region_name":"大兴区","region_type":"3","agency_id":""},{"region_id":"516","parent_id":"52","region_name":"密云县","region_type":"3","agency_id":""},{"region_id":"517","parent_id":"52","region_name":"延庆县","region_type":"3","agency_id":""}]}]
         */

        @SerializedName("region_id")
        public String regionId;
        @SerializedName("parent_id")
        public String parentId;
        @SerializedName("region_name")
        public String regionName;
        @SerializedName("region_type")
        public String regionType;
        @SerializedName("agency_id")
        public String agencyId;
        @SerializedName("citys")
        public ArrayList<CitysBean> citys;

        @Override
        public String getPickerViewText() {
            return regionName;
        }

        public static class CitysBean implements IPickerViewData {
            /**
             * region_id : 52
             * parent_id : 2
             * region_name : 北京
             * region_type : 2
             * agency_id :
             * countys : [{"region_id":"500","parent_id":"52","region_name":"东城区","region_type":"3","agency_id":""},{"region_id":"501","parent_id":"52","region_name":"西城区","region_type":"3","agency_id":""},{"region_id":"502","parent_id":"52","region_name":"海淀区","region_type":"3","agency_id":""},{"region_id":"503","parent_id":"52","region_name":"朝阳区","region_type":"3","agency_id":""},{"region_id":"504","parent_id":"52","region_name":"崇文区","region_type":"3","agency_id":""},{"region_id":"505","parent_id":"52","region_name":"宣武区","region_type":"3","agency_id":""},{"region_id":"506","parent_id":"52","region_name":"丰台区","region_type":"3","agency_id":""},{"region_id":"507","parent_id":"52","region_name":"石景山区","region_type":"3","agency_id":""},{"region_id":"508","parent_id":"52","region_name":"房山区","region_type":"3","agency_id":""},{"region_id":"509","parent_id":"52","region_name":"门头沟区","region_type":"3","agency_id":""},{"region_id":"510","parent_id":"52","region_name":"通州区","region_type":"3","agency_id":""},{"region_id":"511","parent_id":"52","region_name":"顺义区","region_type":"3","agency_id":""},{"region_id":"512","parent_id":"52","region_name":"昌平区","region_type":"3","agency_id":""},{"region_id":"513","parent_id":"52","region_name":"怀柔区","region_type":"3","agency_id":""},{"region_id":"514","parent_id":"52","region_name":"平谷区","region_type":"3","agency_id":""},{"region_id":"515","parent_id":"52","region_name":"大兴区","region_type":"3","agency_id":""},{"region_id":"516","parent_id":"52","region_name":"密云县","region_type":"3","agency_id":""},{"region_id":"517","parent_id":"52","region_name":"延庆县","region_type":"3","agency_id":""}]
             */

            @SerializedName("region_id")
            public String regionId;
            @SerializedName("parent_id")
            public String parentId;
            @SerializedName("region_name")
            public String regionName;
            @SerializedName("region_type")
            public String regionType;
            @SerializedName("agency_id")
            public String agencyId;
            @SerializedName("countys")
            public ArrayList<CountysBean> countys;

            @Override
            public String getPickerViewText() {
                return regionName;
            }

            public static class CountysBean implements IPickerViewData {
                /**
                 * region_id : 500
                 * parent_id : 52
                 * region_name : 东城区
                 * region_type : 3
                 * agency_id :
                 */

                @SerializedName("region_id")
                public String regionId;
                @SerializedName("parent_id")
                public String parentId;
                @SerializedName("region_name")
                public String regionName;
                @SerializedName("region_type")
                public String regionType;
                @SerializedName("agency_id")
                public String agencyId;

                @Override
                public String getPickerViewText() {
                    return regionName;
                }
            }
        }
    }
}
