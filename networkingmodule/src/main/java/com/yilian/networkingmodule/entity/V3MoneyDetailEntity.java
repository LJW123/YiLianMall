package com.yilian.networkingmodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/12/13.
 *         奖励、奖券记录单笔详情
 */

public class V3MoneyDetailEntity extends HttpResultBean implements Parcelable {
    public static final Creator<V3MoneyDetailEntity> CREATOR = new Creator<V3MoneyDetailEntity>() {
        @Override
        public V3MoneyDetailEntity createFromParcel(Parcel in) {
            return new V3MoneyDetailEntity(in);
        }

        @Override
        public V3MoneyDetailEntity[] newArray(int size) {
            return new V3MoneyDetailEntity[size];
        }
    };
    /**
     * amount : 800
     * data : [{"content":"通过微信扫码商家收入","title":"交易类型"},{"content":400,"title":"获得奖券"},{"content":"收入","title":"账单分类"},{"content":"2017-12-14 14:16","title":"创建时间"},{"content":"2017121414155922840","title":"关联订单号"}]
     * id : 2421151
     * show_given : 1
     * top_image : app/images/head/20171109/6896571039863102_805366_1510214295778environment.jpg
     * top_name : 西伯利亚的狗(176****3353)
     * trade_status : 交易成功
     * type : 2
     * user_id : 6896571039863102
     * user_name : 西伯利亚的狗
     * user_phone : 176****3353
     * status:提取都益豆钱包成功或者失败 状态
     * address:益豆钱包的地址
     */

    @SerializedName("amount")
    public String amount;
    @SerializedName("id")
    public String id;
    @SerializedName("show_given")
    public int showGiven;
    @SerializedName("top_image")
    public String topImage;
    @SerializedName("top_name")
    public String topName;
    @SerializedName("trade_status")
    public String tradeStatus;
    @SerializedName("type")
    public int type;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("user_phone")
    public String userPhone;
    @SerializedName("data")
    public List<DataBean> data;
    @SerializedName("status")
    public int status;
    @SerializedName("address")
    public String address;
    @SerializedName("angel_value")
    public String angelValue;

    public V3MoneyDetailEntity(Parcel in) {
        amount = in.readString();
        id = in.readString();
        showGiven = in.readInt();
        topImage = in.readString();
        topName = in.readString();
        tradeStatus = in.readString();
        type = in.readInt();
        userId = in.readString();
        userName = in.readString();
        userPhone = in.readString();
        status = in.readInt();
        address = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount);
        dest.writeString(id);
        dest.writeInt(showGiven);
        dest.writeString(topImage);
        dest.writeString(topName);
        dest.writeString(tradeStatus);
        dest.writeInt(type);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userPhone);
        dest.writeInt(status);
        dest.writeString(address);
    }


    public static class DataBean {
        /**
         * content : 通过微信扫码商家收入
         * title : 交易类型
         * line_type:是否显示分割线
         */

        @SerializedName("content")
        public String content;
        @SerializedName("title")
        public String title;
        @SerializedName("line_type")
        public int lineType;
    }
}
