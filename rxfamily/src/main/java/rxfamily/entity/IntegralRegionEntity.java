package rxfamily.entity;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by  on 2017/5/15 0015.
 * 省市县 三级列表
 */

public class IntegralRegionEntity extends BaseEntity {

    @SerializedName("list")
    public ArrayList<ProvincesBean> list;

    public static class ProvincesBean implements IPickerViewData {
        /**
         * region_id : 2
         * parent_id : 1
         * region_name : 北京
         * region_type : 1
         * agency_id : B
         * citys : [{"region_id":"52","parent_id":"2","region_name":"北京","region_type":"2","agency_id":"","countys":[{"region_id":"500","parent_id":"52","region_name":"东城区","region_type":"3","agency_id":""},{}]},{}]
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
             * countys : [{"region_id":"500","parent_id":"52","region_name":"东城区","region_type":"3","agency_id":""},{}]
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
