package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class HelpOtherDetailBean extends BaseEntity {

    @SerializedName("data")
    public DataBean datas;

    public class DataBean {
        @SerializedName("total")
        public String total;
        @SerializedName("IMG_URL")
        public String imgUrl;
        @SerializedName("PREVIEW_IMG_URL")
        public String previewImgUrl;
        @SerializedName("LIMIT")
        public String limit;
        @SerializedName("PLAN_PERIOD")
        public String PLAN_PERIOD;
        @SerializedName("EFFECT_DATE")
        public String effectDate;
        @SerializedName("H5_URL")
        public String shareUrl;
        @SerializedName("NAME")
        public String title;
        @SerializedName("DESCTRIPTION")
        public String desc;
        @SerializedName("ABSTRACT")
        public String absTract;

        @SerializedName("RULE_TITLE")
        public String ruleTitle;
        @SerializedName("RULE_CONTENT")
        public String ruleContent;

        @SerializedName("RANGE_TITLE")
        public String rangeTitle;
        @SerializedName("RANGE")
        public String rangeContent;

        @SerializedName("AMOUNT_MAX")
        public String amountMax;
        @SerializedName("AMOUNT_TITLE")
        public String amountTitle;
        @SerializedName("AMOUNT_CONTENT")
        public String amountContent;

        @SerializedName("WAIT_TITLE")
        public String waitTitle;
        @SerializedName("WAIT_CONTENT")
        public String waitContent;
        @SerializedName("WAIT_PERIOD")
        public String waitPeriod;
    }
}
