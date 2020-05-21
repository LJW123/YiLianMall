package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Created by  on 2018/5/22.
 * 收货地址信息
 */

public class JDAddressInfoEntity implements Serializable {
    @Override
    public String toString() {
        return "JDAddressInfoEntity{" +
                "nation='" + nation + '\'' +
                ", nationId='" + nationId + '\'' +
                ", province='" + province + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", city='" + city + '\'' +
                ", cityId='" + cityId + '\'' +
                ", county='" + county + '\'' +
                ", countyId='" + countyId + '\'' +
                ", townId='" + townId + '\'' +
                ", town='" + town + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                '}';
    }
    /**
     * provinceId : 7
     * county : 管城区
     * cityId : 412
     * province : 河南
     * townId : 51752
     * town : 城区
     * countyId : 3546
     * nation : 中国
     * city : 郑州市
     * nationId : 4744
     */
    /**
     * 国家
     */
    @SerializedName("nation")
    public String nation;
    @SerializedName("nationId")
    public String nationId;
    /**
     * 省
     */
    @SerializedName("province")
    public String province;
    @SerializedName("provinceId")
    public String provinceId;
    /**
     * 市
     */
    @SerializedName("city")
    public String city;
    @SerializedName("cityId")
    public String cityId;
    /**
     * 区/县
     */
    @SerializedName("county")
    public String county;
    @SerializedName("countyId")
    public String countyId;
    /**
     * 镇
     */
    @SerializedName("townId")
    public String townId;
    @SerializedName("town")
    public String town;
    /**
     * 详细地址 有可能不存在
     */
    @SerializedName("address")
    public String detailAddress;

}
