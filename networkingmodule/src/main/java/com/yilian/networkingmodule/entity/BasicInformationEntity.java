package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/1/15.
 */

public class BasicInformationEntity extends HttpResultBean {

    /**
     * {
     * code: 1,
     * msg: "success",
     * data: {
     * personal_signature:'太个性，没法签 '  ,//个性签名
     * sex:'0',//0未设置  1男  2女
     * age:"100",//年龄
     * birthday:"1515580673",//生日时间戳
     * blood_type:'AB',//血型
     * school:"华中科技大学",//学校
     * company:"php科技有限公司",//公司名称
     * profession:"1"//职业对应的id
     * profession_name:"工人",//职业名称
     * home_provice_cn:"河南"  ,//家乡 省
     * home_city_cn:"郑州"  ,//家乡 市
     * home_county_cn:"金水区"  ,//家乡 区
     * home_provice:"43"  ,//家乡 省  id
     * home_city:"20"  ,//家乡 市id
     * home_county:"2"  ,//家乡 区id
     * current_province_cn:"河南"  ,//所在地 省
     * current_city_cn:"郑州"  ,//所在地 市
     * current_county_cn:"金水区"  ,//所在地 区
     * current_province:"43"  ,//所在地 省  id
     * current_city:"23"  ,//所在地 市 id
     * current_county:"2"  ,//所在地 区id
     * }
     * }
     * data : {"personal_signature":"Zhenxinbucuo","sex":"","age":"","birthday":"","blood_type":"","school":"","company":"","profession":"","profession_name":"","home_provice_cn":"","home_city_cn":"","home_county_cn":"","home_provice":"","home_city":"","home_county":"","current_province_cn":"","current_city_cn":"","current_county_cn":"","current_province":"","current_city":"","current_county":""}
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        /**
         * personal_signature : Zhenxinbucuo
         * sex :
         * age :
         * birthday :
         * blood_type :
         * school :
         * company :
         * profession :
         * profession_name :
         * home_provice_cn :
         * home_city_cn :
         * home_county_cn :
         * home_provice :
         * home_city :
         * home_county :
         * current_province_cn :
         * current_city_cn :
         * current_county_cn :
         * current_province :
         * current_city :
         * current_county :
         */

        @SerializedName("personal_signature")
        public String personalSignature;
        @SerializedName("sex")
        public String sex;
        @SerializedName("age")
        public String age;
        @SerializedName("birthday")
        public String birthday;
        @SerializedName("blood_type")
        public String bloodType;
        @SerializedName("school")
        public String school;
        @SerializedName("company")
        public String company;
        @SerializedName("profession")
        public String profession;
        @SerializedName("profession_name")
        public String professionName;
        @SerializedName("home_provice_cn")
        public String homeProviceCn;
        @SerializedName("home_city_cn")
        public String homeCityCn;
        @SerializedName("home_county_cn")
        public String homeCountyCn;
        @SerializedName("home_provice")
        public String homeProvice;
        @SerializedName("home_city")
        public String homeCity;
        @SerializedName("home_county")
        public String homeCounty;
        @SerializedName("current_province_cn")
        public String currentProvinceCn;
        @SerializedName("current_city_cn")
        public String currentCityCn;
        @SerializedName("current_county_cn")
        public String currentCountyCn;
        @SerializedName("current_province")
        public String currentProvince;
        @SerializedName("current_city")
        public String currentCity;
        @SerializedName("current_county")
        public String currentCounty;
    }
}
