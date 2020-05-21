package com.yilian.networkingmodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by liuyuqi on 2017/6/8 0008.
 */
public class MyBalanceBeanEntity extends HttpResultBean implements Parcelable {


    /**
     * bean : 0
     * total_bean : 4000
     * charge_rate : 2
     */

    @SerializedName("bean")
    public String bean;
    @SerializedName("total_bean")
    public String totalBean;
    @SerializedName("extract_bean")
    public String extractBean;
    @SerializedName("charge_rate")
    public int chargeRate;
    @SerializedName("charge_rate_angel")
    public int chargeRateAngel;
    @SerializedName("angel_value")
    public double angelValue;
    /**
     * 转赠的手续费
     */
    @SerializedName("bean_transfer_charge_rate")
    public String beanTransferChargeRate;
    /**
     * 最低转赠限额
     */
    @SerializedName("lower_bean_for_transfer")
    public String lowerBeanForTransfer;

    protected MyBalanceBeanEntity(Parcel in) {
        bean = in.readString();
        totalBean = in.readString();
        extractBean = in.readString();
        chargeRate = in.readInt();
        chargeRateAngel = in.readInt();
        angelValue = in.readDouble();
        beanTransferChargeRate = in.readString();
        lowerBeanForTransfer = in.readString();
    }

    public static final Creator<MyBalanceBeanEntity> CREATOR = new Creator<MyBalanceBeanEntity>() {
        @Override
        public MyBalanceBeanEntity createFromParcel(Parcel in) {
            return new MyBalanceBeanEntity(in);
        }

        @Override
        public MyBalanceBeanEntity[] newArray(int size) {
            return new MyBalanceBeanEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bean);
        dest.writeString(totalBean);
        dest.writeString(extractBean);
        dest.writeInt(chargeRate);
        dest.writeInt(chargeRateAngel);
        dest.writeDouble(angelValue);
        dest.writeString(beanTransferChargeRate);
        dest.writeString(lowerBeanForTransfer);
    }
}
