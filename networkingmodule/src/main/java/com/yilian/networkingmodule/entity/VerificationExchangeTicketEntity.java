package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * 带核销券实体类
 * Created by Zg on 2018/9/8
 */
public class VerificationExchangeTicketEntity extends HttpResultBean  {


    /**
     * trade :
     * agent_id : 102
     * agent_type : 1
     * agent_region : 河南郑州惠济区
     * agent_merchant_num : 0
     * total_region_subsidy : 0
     * total_merchant_bonus : 0
     * agency_quan : 10000000000
     * income_quan : 0
     * expend_quan : 0
     */

    @SerializedName("trade")
    public Trade trade;
    @SerializedName("agent_id")
    public String agentId;
    @SerializedName("agent_type")
    public String agentType;
    @SerializedName("agent_region")
    public String agentRegion;
    @SerializedName("agent_merchant_num")
    public int agentMerchantNum;
    @SerializedName("total_region_subsidy")
    public int totalRegionSubsidy;
    @SerializedName("total_merchant_bonus")
    public int totalMerchantBonus;
    @SerializedName("agency_quan")
    public String agencyQuan;
    @SerializedName("income_quan")
    public int incomeQuan;
    @SerializedName("expend_quan")
    public int expendQuan;



    public  class Trade{
        /**
         * dayDeal : 0
         * dayDiscount : 0
         * totalDeal : 0
         * totalDiscount : 0
         */

        @SerializedName("dayDeal")
        public int dayDeal;
        @SerializedName("dayDiscount")
        public int dayDiscount;
        @SerializedName("totalDeal")
        public int totalDeal;
        @SerializedName("totalDiscount")
        public int totalDiscount;
    }


}
