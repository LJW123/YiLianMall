package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/1/20.
 */

public class CerResultEntity extends HttpResultBean {
    /**
     * 0非商家 1商家
     */
    @SerializedName("is_merchant")
    public int isMerchant;
    /**
     * info : {"card_name":"","user_name":"","card_id":"","card_status":""}
     * data : {"type":"","content":"","image":"","url":""}
     */

    @SerializedName("info")
    public InfoBean info;
    @SerializedName("data")
    public DataBean data;

    public static class InfoBean {
        /**
         * card_name : 填写的身份证名称
         * user_name :处理过的用户姓名
         * card_id :
         * card_status ://0待上传证件 1表示待审核，2表示审核通过，3表示审核拒绝
         */
        @SerializedName("card_name")
        public String cardName;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("card_id")
        public String cardId;
        @SerializedName("card_status")
        public int cardStatus;
    }

    public static class DataBean {
        /**
         * type :
         * content :
         * image :
         * url :
         */

        @SerializedName("type")
        public String type;
        @SerializedName("content")
        public String content;
        @SerializedName("image")
        public String image;
        @SerializedName("url")
        public String url;
    }
}
