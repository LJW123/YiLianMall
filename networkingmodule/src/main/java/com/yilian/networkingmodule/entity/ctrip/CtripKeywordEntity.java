package com.yilian.networkingmodule.entity.ctrip;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.entity.ctrip.ctripMultiItem.CtripKeywordLayoutType;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 携程 关键字
 *
 * @author Zg
 */
public class CtripKeywordEntity extends HttpResultBean {

    @SerializedName("location")
    public List<Location> location;
    @SerializedName("zone")
    public List<Zone> zone;
    @SerializedName("brand")
    public List<Brand> brand;

    public static class Record implements MultiItemEntity {
        public String name;

        public Record(String recordName) {
            this.name = recordName;
        }

        @Override
        public int getItemType() {
            return CtripKeywordLayoutType.record;
        }
    }

    // 行政区域
    public static class Location implements MultiItemEntity {
        @SerializedName("location")
        public String location;
        @SerializedName("locationname")
        public String locationname;
        @SerializedName("city")
        public String city;

        @Override
        public int getItemType() {
            return CtripKeywordLayoutType.location;
        }
    }

    // 商圈
    public static class Zone implements MultiItemEntity {
        @SerializedName("zone")
        public String zone;
        @SerializedName("zonename")
        public String zonename;
        @SerializedName("city")
        public String city;

        @Override
        public int getItemType() {
            return CtripKeywordLayoutType.zone;
        }
    }

    // 品牌
    public static class Brand implements MultiItemEntity {
        @SerializedName("BrandCode")
        public String BrandCode;
        @SerializedName("BrandName")
        public String BrandName;

        @Override
        public int getItemType() {
            return CtripKeywordLayoutType.brand;
        }
    }
}
