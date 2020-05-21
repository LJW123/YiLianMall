package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * @author xiaofo on 2018/7/26.
 */

public class SnCommitOrderEntity extends HttpResultBean implements Serializable{


    /**
     * order_id : 2018072916004579512
     * sn_order_id : 100002066833
     * amount : 242 商品价格
     * freight : 0 运费
     * time : 86400 倒计时长
     * id : 37
     * coupon : 0 代购券数量
     */

    @SerializedName("order_id")
    public String orderId;
    @SerializedName("sn_order_id")
    public String snOrderId;
    @SerializedName("amount")
    public String amount;
    @SerializedName("freight")
    public String freight;
    @SerializedName("time")
    public String time;
    @SerializedName("id")
    public String id;
    @SerializedName("coupon")
    public String daiGouQuanMoney;
}
