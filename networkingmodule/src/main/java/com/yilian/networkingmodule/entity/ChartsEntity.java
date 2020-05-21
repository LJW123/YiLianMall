package com.yilian.networkingmodule.entity;/**
 * Created by  on 2017/3/21 0021.
 */

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/3/21 0021.
 * 排行榜内容
 */
public class ChartsEntity extends HttpResultBean {


    /**
     * data : {"dealCharts":[{"allAmount":"260155200","consumer":"6360844947441619","name":"152****4340","value":"260155200"},{"allAmount":"86200000","consumer":"6361768401776607","name":"****","value":"86200000"},{"allAmount":"46516700","consumer":"6335491528377706","name":"187****0808","value":"46516700"},{"allAmount":"40000000","consumer":"6327375139726303","name":"187****1909","value":"40000000"},{"allAmount":"35000000","consumer":"6396148101257402","name":"****","value":"35000000"}],"discountCharts":[{"merchantIds":"6,7,8,9,10,12,13,16,19,21,22,23,25,26,30,34,35,42,44,48,51,54,60,64,67,68,75,79,80,84,89,92,94,112,118,120,121,125,127,130,133,141,142,145,146,149,157,160,161,169,177,200,201,205,567,570,571,573,1388,1396,1397,1403,1404,1407,1410","merchant_city":"149","merchant_province":"11","name":"河南郑州","value":"450506572"},{"merchantIds":"18,20,198,204,207,210,213,564,568,1385,1391,1411","merchant_city":"52","merchant_province":"2","name":"北京北京","value":"139664792"},{"merchantIds":"31,32,43,50,58,62,72,73,81,85,88,93,98,99,110,113,115,132,139,175,180,183,184,187","merchant_city":"157","merchant_province":"11","name":"河南平顶山","value":"75045742"},{"merchantIds":"5,27,46,82,97,109,138,193,206","merchant_city":"156","merchant_province":"11","name":"河南南阳","value":"51033550"},{"merchantIds":"191","merchant_city":"139","merchant_province":"10","name":"河北保定","value":"33130000"}],"merDiscountCharts":[{"allbonus":"160010200","name":"****","value":"160010200","phone":"18538288781","user_id":"6291495490082306"},{"allbonus":"110177000","name":"185****8781","phone":"18538288781","user_id":"6291495490082306","value":"110177000"},{"allbonus":"100902000","name":"189****0039","phone":"18911320039","user_id":"6335346174162001","value":"100902000"},{"allbonus":"86200000","name":"187****1909","phone":"18703811909","user_id":"6327375139726303","value":"86200000"},{"allbonus":"68464400","name":"189****7187","phone":"18937187187","user_id":"6327212147686811","value":"68464400"}],"totalAmount":{"integralNumber":"24.0400","integralNumberArr":[{"created_at":"1503632046","integralNumber":"24.0400\u2031"},{"created_at":"1503631886","integralNumber":"24.2800\u2031"},{"created_at":"1503631739","integralNumber":"24.1600\u2031"},{"created_at":"1503626402","integralNumber":"24.2700\u2031"},{"created_at":"1503540002","integralNumber":"24.2300\u2031"},{"created_at":"1503453601","integralNumber":"24.0400\u2031"},{"created_at":"1503367201","integralNumber":"24.3000\u2031"}],"merchantNum":2738,"merchantStr":"平台商家:2738","userNum":51948,"userStr":"平台会员:51948"},"userIntegralCharts":[{"integral":"4029999078","name":"182****1819","phone":"18237111819","value":"4029999078"},{"integral":"4015462194","name":"182****6617","phone":"18237196617","value":"4015462194"},{"integral":"1270371968","name":"152****4340","phone":"15238664340","value":"1270371968"},{"integral":"512179405","name":"185****8781","phone":"18538288781","value":"512179405"},{"integral":"200617352","name":"187****0808","phone":"18752080808","value":"200617352"}]}
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        /**
         * dealCharts : [{"allAmount":"260155200","consumer":"6360844947441619","name":"152****4340","value":"260155200"},{"allAmount":"86200000","consumer":"6361768401776607","name":"****","value":"86200000"},{"allAmount":"46516700","consumer":"6335491528377706","name":"187****0808","value":"46516700"},{"allAmount":"40000000","consumer":"6327375139726303","name":"187****1909","value":"40000000"},{"allAmount":"35000000","consumer":"6396148101257402","name":"****","value":"35000000"}]
         * discountCharts : [{"merchantIds":"6,7,8,9,10,12,13,16,19,21,22,23,25,26,30,34,35,42,44,48,51,54,60,64,67,68,75,79,80,84,89,92,94,112,118,120,121,125,127,130,133,141,142,145,146,149,157,160,161,169,177,200,201,205,567,570,571,573,1388,1396,1397,1403,1404,1407,1410","merchant_city":"149","merchant_province":"11","name":"河南郑州","value":"450506572"},{"merchantIds":"18,20,198,204,207,210,213,564,568,1385,1391,1411","merchant_city":"52","merchant_province":"2","name":"北京北京","value":"139664792"},{"merchantIds":"31,32,43,50,58,62,72,73,81,85,88,93,98,99,110,113,115,132,139,175,180,183,184,187","merchant_city":"157","merchant_province":"11","name":"河南平顶山","value":"75045742"},{"merchantIds":"5,27,46,82,97,109,138,193,206","merchant_city":"156","merchant_province":"11","name":"河南南阳","value":"51033550"},{"merchantIds":"191","merchant_city":"139","merchant_province":"10","name":"河北保定","value":"33130000"}]
         * merDiscountCharts : [{"allbonus":"160010200","name":"****","value":"160010200"},{"allbonus":"110177000","name":"185****8781","phone":"18538288781","user_id":"6291495490082306","value":"110177000"},{"allbonus":"100902000","name":"189****0039","phone":"18911320039","user_id":"6335346174162001","value":"100902000"},{"allbonus":"86200000","name":"187****1909","phone":"18703811909","user_id":"6327375139726303","value":"86200000"},{"allbonus":"68464400","name":"189****7187","phone":"18937187187","user_id":"6327212147686811","value":"68464400"}]
         * totalAmount : {"integralNumber":"24.0400","integralNumberArr":[{"created_at":"1503632046","integralNumber":"24.0400\u2031"},{"created_at":"1503631886","integralNumber":"24.2800\u2031"},{"created_at":"1503631739","integralNumber":"24.1600\u2031"},{"created_at":"1503626402","integralNumber":"24.2700\u2031"},{"created_at":"1503540002","integralNumber":"24.2300\u2031"},{"created_at":"1503453601","integralNumber":"24.0400\u2031"},{"created_at":"1503367201","integralNumber":"24.3000\u2031"}],"merchantNum":2738,"merchantStr":"平台商家:2738","userNum":51948,"userStr":"平台会员:51948"}
         * userIntegralCharts : [{"integral":"4029999078","name":"182****1819","phone":"18237111819","value":"4029999078"},{"integral":"4015462194","name":"182****6617","phone":"18237196617","value":"4015462194"},{"integral":"1270371968","name":"152****4340","phone":"15238664340","value":"1270371968"},{"integral":"512179405","name":"185****8781","phone":"18538288781","value":"512179405"},{"integral":"200617352","name":"187****0808","phone":"18752080808","value":"200617352"}]
         */

        @SerializedName("totalAmount")
        public TotalAmountBean totalAmount;
        @SerializedName("dealCharts")
        public ArrayList<ChartsBean> dealCharts;
        @SerializedName("discountCharts")
        public ArrayList<ChartsBean> discountCharts;
        @SerializedName("merDiscountCharts")
        public ArrayList<ChartsBean> merDiscountCharts;
        @SerializedName("userIntegralCharts")
        public ArrayList<ChartsBean> userIntegralCharts;

        public class TotalAmountBean {
            /**
             * integralNumber : 24.0400
             * integralNumberArr : [{"created_at":"1503632046","integralNumber":"24.0400\u2031"},{"created_at":"1503631886","integralNumber":"24.2800\u2031"},{"created_at":"1503631739","integralNumber":"24.1600\u2031"},{"created_at":"1503626402","integralNumber":"24.2700\u2031"},{"created_at":"1503540002","integralNumber":"24.2300\u2031"},{"created_at":"1503453601","integralNumber":"24.0400\u2031"},{"created_at":"1503367201","integralNumber":"24.3000\u2031"}]
             * merchantNum : 2738
             * merchantStr : 平台商家:2738
             * userNum : 51948
             * userStr : 平台会员:51948
             */

            @SerializedName("integralNumber")
            public String integralNumber;
            @SerializedName("merchantNum")
            public int merchantNum;
            @SerializedName("merchantStr")
            public String merchantStr;
            @SerializedName("userNum")
            public int userNum;
            @SerializedName("userStr")
            public String userStr;
            @SerializedName("integralNumberArr")
            public ArrayList<ScoreExponent> integralNumberArr;

        }

        public class ChartsBean {
            @SerializedName("allbonus")
            public String allbonus;
            @SerializedName("integral")
            public String integral;
            @SerializedName("name")
            public String name;
            @SerializedName("phone")
            public String phone;
            @SerializedName("value")
            public String value;
            @SerializedName("merchantIds")
            public String merchantIds;
            @SerializedName("merchant_city")
            public String merchantCity;
            @SerializedName("merchant_province")
            public String merchantProvince;
            @SerializedName("allAmount")
            public String allAmount;
            @SerializedName("consumer")
            public String consumer;
            @SerializedName("user_id")
            public String userId;
        }
    }
}
