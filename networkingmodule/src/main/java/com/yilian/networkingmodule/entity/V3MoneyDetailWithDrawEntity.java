package com.yilian.networkingmodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by  on 2017/12/13.
 *         奖励、奖券单笔订单详情的领奖励详情类
 */

public class V3MoneyDetailWithDrawEntity extends HttpResultBean implements Parcelable {


    public static final Creator<V3MoneyDetailWithDrawEntity> CREATOR = new Creator<V3MoneyDetailWithDrawEntity>() {
        @Override
        public V3MoneyDetailWithDrawEntity createFromParcel(Parcel in) {
            return new V3MoneyDetailWithDrawEntity(in);
        }

        @Override
        public V3MoneyDetailWithDrawEntity[] newArray(int size) {
            return new V3MoneyDetailWithDrawEntity[size];
        }
    };
    /**
     * id : 2420840
     * extract_reason:失败原因
     * trade_status : 待打款
     * show_given : 0
     * type : 108
     * amount : 100000
     * trade_type : 奖励领取
     * top_name : 河南乐善网络科技有限公司
     * top_image : app/images/tubiao/pingtai.png
     * status : 0
     * <p>
     * extract_status : [{"image":"duihao.png","status":"待打款","time":"2017-12-13 16:53"}]
     * extract_to : 中国邮政储蓄银行(6790)李剑威
     * bill_type : 领奖励
     * time : 2017-12-13 16:53
     * extract_order : 2017121316535567040
     * data : [{"title":"创建时间","content":"2017-12-13 16:53"}]
     */

    @SerializedName("id")
    public String id;
    @SerializedName("trade_status")
    public String tradeStatus;
    @SerializedName("show_given")
    public int showGiven;
    @SerializedName("type")
    public int type;
    @SerializedName("amount")
    public String amount;
    @SerializedName("trade_type")
    public String tradeType;
    @SerializedName("top_name")
    public String topName;
    @SerializedName("top_image")
    public String topImage;
    @SerializedName("status")
    public int status;
    @SerializedName("extract_to")
    public String extractTo;
    @SerializedName("bill_type")
    public String billType;
    @SerializedName("time")
    public String time;
    @SerializedName("extract_order")
    public String extractOrder;
    @SerializedName("extract_status")
    public List<ExtractStatusBean> extractStatus;
    @SerializedName("data")
    public List<DataBean> data;
    @SerializedName("extract_reason")
    public String extractReason;
    @SerializedName("city")
    public String city;
    @SerializedName("province_cn")
    public String province_cn;
    @SerializedName("bank_code")
    public String bankCode;
    @SerializedName("card_holder")
    public String cardHolder;
    @SerializedName("branch_bank")
    public String branchBank;
    @SerializedName("card_bank")
    public String cardBank;
    /**
     * 本地添加字段，同步类型
     * 0只更新提款订单 1同步到银行卡信息
     */
    public int syncType;
    @SerializedName("card_number")
    public String cardNum;
    @SerializedName("city_cn")
    public String cityCn;
    @SerializedName("province")
    public String province;

    protected V3MoneyDetailWithDrawEntity(Parcel in) {
        id = in.readString();
        tradeStatus = in.readString();
        showGiven = in.readInt();
        type = in.readInt();
        amount = in.readString();
        tradeType = in.readString();
        topName = in.readString();
        topImage = in.readString();
        status = in.readInt();
        extractTo = in.readString();
        billType = in.readString();
        time = in.readString();
        extractOrder = in.readString();
        extractStatus = in.createTypedArrayList(ExtractStatusBean.CREATOR);
        data = in.createTypedArrayList(DataBean.CREATOR);
        extractReason = in.readString();
        city = in.readString();
        province_cn = in.readString();
        bankCode = in.readString();
        cardHolder = in.readString();
        branchBank = in.readString();
        cardBank = in.readString();
        syncType = in.readInt();
        cardNum = in.readString();
        cityCn = in.readString();
        province = in.readString();
    }

    public List<ExtractStatusBean> getExtractStatus() {
        if (extractStatus == null) {
            return new ArrayList<>();
        }
        return extractStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static class ExtractStatusBean implements Parcelable {
        public static final Creator<ExtractStatusBean> CREATOR = new Creator<ExtractStatusBean>() {
            @Override
            public ExtractStatusBean createFromParcel(Parcel in) {
                return new ExtractStatusBean(in);
            }

            @Override
            public ExtractStatusBean[] newArray(int size) {
                return new ExtractStatusBean[size];
            }
        };
        /**
         * image : duihao.png
         * status : 待打款
         * time : 2017-12-13 16:53
         */
        /**
         * 本地添加字段
         * 1打款成功
         */
        public int resultStatus;
        @SerializedName("image")
        public String image;
        @SerializedName("status")
        public String status;
        @SerializedName("time")
        public String time;

        protected ExtractStatusBean(Parcel in) {
            resultStatus = in.readInt();
            image = in.readString();
            status = in.readString();
            time = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(resultStatus);
            dest.writeString(image);
            dest.writeString(status);
            dest.writeString(time);
        }
    }

    public static class DataBean implements Parcelable {
        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
        /**
         * title : 创建时间
         * content : 2017-12-13 16:53
         */

        @SerializedName("title")
        public String title;
        @SerializedName("content")
        public String content;

        protected DataBean(Parcel in) {
            title = in.readString();
            content = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(content);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tradeStatus);
        dest.writeInt(showGiven);
        dest.writeInt(type);
        dest.writeString(amount);
        dest.writeString(tradeType);
        dest.writeString(topName);
        dest.writeString(topImage);
        dest.writeInt(status);
        dest.writeString(extractTo);
        dest.writeString(billType);
        dest.writeString(time);
        dest.writeString(extractOrder);
        dest.writeTypedList(extractStatus);
        dest.writeTypedList(data);
        dest.writeString(extractReason);
        dest.writeString(city);
        dest.writeString(province_cn);
        dest.writeString(bankCode);
        dest.writeString(cardHolder);
        dest.writeString(branchBank);
        dest.writeString(cardBank);
        dest.writeInt(syncType);
        dest.writeString(cardNum);
        dest.writeString(cityCn);
        dest.writeString(province);
    }


}
