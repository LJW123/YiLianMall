package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author xiaofo on 2018/9/27.
 */

public class CtripBookRoomOrderEntity extends HttpResultBean {

    /**
     * ResStatus : 1
     * info : {"ResponseStatus":{"Timestamp":"2018-09-27T17:39:22.9553346+08:00","Ack":"Success","Errors":[],"Extension":[]},"Warnings":[{"Type":"Unknown","ShortText":"该房型不支持送券","Code":"110117000105","Value":"110117000105-该房型不支持送券"},{"Type":"Unknown","ShortText":"该房型不支持送券","Code":"110117000105","Value":"110117000105-该房型不支持送券"}],"HotelReservations":{"HotelReservation":{"RoomStays":{"RoomStay":{"RatePlans":{"RatePlan":{"RatePlanCode":"96844496","RoomID":"96844496"}}}},"ResGlobalInfo":{"DepositPayments":[{"AmountPercent":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"Amount":2209,"CurrencyCode":"CNY"},"GuaranteeCode":"2"}],"CancelPenalties":[{"AmountPercent":{"DisplayCurrency":[],"Amount":2209,"CurrencyCode":"CNY"},"Start":"2016-01-06 12:00:00","End":"2018-10-02 00:00:00"}],"Total":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"},"HotelReservationIDs":[{"ResID_Type":"501","ResID_Value":"3064637436"}]},"TPA_Extensions":{"Commissions":{"EstimatedAmount":{"Amount":309.26,"CurrencyCode":"CNY"}},"Cost":{"Amount":1767.2,"CurrencyCode":"CNY"}},"CreateDateTime":"2018-09-27T17:38:58.1100315+08:00","ResStatus":"S","IsInstantConfirm":false}},"Errors":[],"TimeStamp":"2018-09-27T17:39:22.8842736+08:00","Version":1,"PrimaryLangID":"zh"}
     * ResID_Value : 3064637436
     */

    @SerializedName("ResStatus")
    public int resStatus;
    @SerializedName("info")
    public InfoBean info;
    @SerializedName("ResID_Value")
    public String resIDValue;
    @SerializedName("id")
    public String id;
    @SerializedName("ReturnBean")
    public String returnBean;
    @SerializedName("amount")
    public String amount;


    public static class InfoBean {
        /**
         * ResponseStatus : {"Timestamp":"2018-09-27T17:39:22.9553346+08:00","Ack":"Success","Errors":[],"Extension":[]}
         * Warnings : [{"Type":"Unknown","ShortText":"该房型不支持送券","Code":"110117000105","Value":"110117000105-该房型不支持送券"},{"Type":"Unknown","ShortText":"该房型不支持送券","Code":"110117000105","Value":"110117000105-该房型不支持送券"}]
         * HotelReservations : {"HotelReservation":{"RoomStays":{"RoomStay":{"RatePlans":{"RatePlan":{"RatePlanCode":"96844496","RoomID":"96844496"}}}},"ResGlobalInfo":{"DepositPayments":[{"AmountPercent":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"Amount":2209,"CurrencyCode":"CNY"},"GuaranteeCode":"2"}],"CancelPenalties":[{"AmountPercent":{"DisplayCurrency":[],"Amount":2209,"CurrencyCode":"CNY"},"Start":"2016-01-06 12:00:00","End":"2018-10-02 00:00:00"}],"Total":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"},"HotelReservationIDs":[{"ResID_Type":"501","ResID_Value":"3064637436"}]},"TPA_Extensions":{"Commissions":{"EstimatedAmount":{"Amount":309.26,"CurrencyCode":"CNY"}},"Cost":{"Amount":1767.2,"CurrencyCode":"CNY"}},"CreateDateTime":"2018-09-27T17:38:58.1100315+08:00","ResStatus":"S","IsInstantConfirm":false}}
         * Errors : []
         * TimeStamp : 2018-09-27T17:39:22.8842736+08:00
         * Version : 1
         * PrimaryLangID : zh
         */

        @SerializedName("ResponseStatus")
        public ResponseStatusBean responseStatus;
        @SerializedName("HotelReservations")
        public HotelReservationsBean hotelReservations;
        @SerializedName("TimeStamp")
        public String timeStamp;
        @SerializedName("Version")
        public int version;
        @SerializedName("PrimaryLangID")
        public String primaryLangID;
        @SerializedName("Warnings")
        public List<WarningsBean> warnings;
        @SerializedName("Errors")
        public List<?> errors;

        public static class ResponseStatusBean {
            /**
             * Timestamp : 2018-09-27T17:39:22.9553346+08:00
             * Ack : Success
             * Errors : []
             * Extension : []
             */

            @SerializedName("Timestamp")
            public String timestamp;
            @SerializedName("Ack")
            public String ack;
            @SerializedName("Errors")
            public List<?> errors;
            @SerializedName("Extension")
            public List<?> extension;
        }

        public static class HotelReservationsBean {
            /**
             * HotelReservation : {"RoomStays":{"RoomStay":{"RatePlans":{"RatePlan":{"RatePlanCode":"96844496","RoomID":"96844496"}}}},"ResGlobalInfo":{"DepositPayments":[{"AmountPercent":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"Amount":2209,"CurrencyCode":"CNY"},"GuaranteeCode":"2"}],"CancelPenalties":[{"AmountPercent":{"DisplayCurrency":[],"Amount":2209,"CurrencyCode":"CNY"},"Start":"2016-01-06 12:00:00","End":"2018-10-02 00:00:00"}],"Total":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"},"HotelReservationIDs":[{"ResID_Type":"501","ResID_Value":"3064637436"}]},"TPA_Extensions":{"Commissions":{"EstimatedAmount":{"Amount":309.26,"CurrencyCode":"CNY"}},"Cost":{"Amount":1767.2,"CurrencyCode":"CNY"}},"CreateDateTime":"2018-09-27T17:38:58.1100315+08:00","ResStatus":"S","IsInstantConfirm":false}
             */

            @SerializedName("HotelReservation")
            public HotelReservationBean hotelReservation;

            public static class HotelReservationBean {
                /**
                 * RoomStays : {"RoomStay":{"RatePlans":{"RatePlan":{"RatePlanCode":"96844496","RoomID":"96844496"}}}}
                 * ResGlobalInfo : {"DepositPayments":[{"AmountPercent":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"Amount":2209,"CurrencyCode":"CNY"},"GuaranteeCode":"2"}],"CancelPenalties":[{"AmountPercent":{"DisplayCurrency":[],"Amount":2209,"CurrencyCode":"CNY"},"Start":"2016-01-06 12:00:00","End":"2018-10-02 00:00:00"}],"Total":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"},"HotelReservationIDs":[{"ResID_Type":"501","ResID_Value":"3064637436"}]}
                 * TPA_Extensions : {"Commissions":{"EstimatedAmount":{"Amount":309.26,"CurrencyCode":"CNY"}},"Cost":{"Amount":1767.2,"CurrencyCode":"CNY"}}
                 * CreateDateTime : 2018-09-27T17:38:58.1100315+08:00
                 * ResStatus : S
                 * IsInstantConfirm : false
                 */

                @SerializedName("RoomStays")
                public RoomStaysBean roomStays;
                @SerializedName("ResGlobalInfo")
                public ResGlobalInfoBean resGlobalInfo;
                @SerializedName("TPA_Extensions")
                public TPAExtensionsBeanX tpaExtensions;
                @SerializedName("CreateDateTime")
                public String createDateTime;
                @SerializedName("ResStatus")
                public String resStatus;
                @SerializedName("IsInstantConfirm")
                public boolean isInstantConfirm;

                public static class RoomStaysBean {
                    /**
                     * RoomStay : {"RatePlans":{"RatePlan":{"RatePlanCode":"96844496","RoomID":"96844496"}}}
                     */

                    @SerializedName("RoomStay")
                    public RoomStayBean roomStay;

                    public static class RoomStayBean {
                        /**
                         * RatePlans : {"RatePlan":{"RatePlanCode":"96844496","RoomID":"96844496"}}
                         */

                        @SerializedName("RatePlans")
                        public RatePlansBean ratePlans;

                        public static class RatePlansBean {
                            /**
                             * RatePlan : {"RatePlanCode":"96844496","RoomID":"96844496"}
                             */

                            @SerializedName("RatePlan")
                            public RatePlanBean ratePlan;

                            public static class RatePlanBean {
                                /**
                                 * RatePlanCode : 96844496
                                 * RoomID : 96844496
                                 */

                                @SerializedName("RatePlanCode")
                                public String ratePlanCode;
                                @SerializedName("RoomID")
                                public String roomID;
                            }
                        }
                    }
                }

                public static class ResGlobalInfoBean {
                    /**
                     * DepositPayments : [{"AmountPercent":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"Amount":2209,"CurrencyCode":"CNY"},"GuaranteeCode":"2"}]
                     * CancelPenalties : [{"AmountPercent":{"DisplayCurrency":[],"Amount":2209,"CurrencyCode":"CNY"},"Start":"2016-01-06 12:00:00","End":"2018-10-02 00:00:00"}]
                     * Total : {"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}
                     * HotelReservationIDs : [{"ResID_Type":"501","ResID_Value":"3064637436"}]
                     */

                    @SerializedName("Total")
                    public TotalBean total;
                    @SerializedName("DepositPayments")
                    public List<DepositPaymentsBean> depositPayments;
                    @SerializedName("CancelPenalties")
                    public List<CancelPenaltiesBean> cancelPenalties;
                    @SerializedName("HotelReservationIDs")
                    public List<HotelReservationIDsBean> hotelReservationIDs;

                    public static class TotalBean {
                        /**
                         * TPA_Extensions : [{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}]
                         * ExclusiveAmount : 0
                         * InclusiveAmount : 2209
                         * CurrencyCode : CNY
                         */

                        @SerializedName("ExclusiveAmount")
                        public int exclusiveAmount;
                        @SerializedName("InclusiveAmount")
                        public int inclusiveAmount;
                        @SerializedName("CurrencyCode")
                        public String currencyCode;
                        @SerializedName("TPA_Extensions")
                        public List<TPAExtensionsBean> tpaExtensions;

                        public static class TPAExtensionsBean {
                            /**
                             * ExclusiveAmount : 0
                             * InclusiveAmount : 2209
                             * CurrencyCode : CNY
                             */

                            @SerializedName("ExclusiveAmount")
                            public int exclusiveAmount;
                            @SerializedName("InclusiveAmount")
                            public int inclusiveAmount;
                            @SerializedName("CurrencyCode")
                            public String currencyCode;
                        }
                    }

                    public static class DepositPaymentsBean {
                        /**
                         * AmountPercent : {"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}],"Amount":2209,"CurrencyCode":"CNY"}
                         * GuaranteeCode : 2
                         */

                        @SerializedName("AmountPercent")
                        public AmountPercentBean amountPercent;
                        @SerializedName("GuaranteeCode")
                        public String guaranteeCode;

                        public static class AmountPercentBean {
                            /**
                             * DisplayCurrency : [{"ExclusiveAmount":0,"InclusiveAmount":2209,"CurrencyCode":"CNY"}]
                             * Amount : 2209
                             * CurrencyCode : CNY
                             */

                            @SerializedName("Amount")
                            public int amount;
                            @SerializedName("CurrencyCode")
                            public String currencyCode;
                            @SerializedName("DisplayCurrency")
                            public List<DisplayCurrencyBean> displayCurrency;

                            public static class DisplayCurrencyBean {
                                /**
                                 * ExclusiveAmount : 0
                                 * InclusiveAmount : 2209
                                 * CurrencyCode : CNY
                                 */

                                @SerializedName("ExclusiveAmount")
                                public int exclusiveAmount;
                                @SerializedName("InclusiveAmount")
                                public int inclusiveAmount;
                                @SerializedName("CurrencyCode")
                                public String currencyCode;
                            }
                        }
                    }

                    public static class CancelPenaltiesBean {
                        /**
                         * AmountPercent : {"DisplayCurrency":[],"Amount":2209,"CurrencyCode":"CNY"}
                         * Start : 2016-01-06 12:00:00
                         * End : 2018-10-02 00:00:00
                         */

                        @SerializedName("AmountPercent")
                        public AmountPercentBeanX amountPercent;
                        @SerializedName("Start")
                        public String start;
                        @SerializedName("End")
                        public String end;

                        public static class AmountPercentBeanX {
                            /**
                             * DisplayCurrency : []
                             * Amount : 2209
                             * CurrencyCode : CNY
                             */

                            @SerializedName("Amount")
                            public int amount;
                            @SerializedName("CurrencyCode")
                            public String currencyCode;
                            @SerializedName("DisplayCurrency")
                            public List<?> displayCurrency;
                        }
                    }

                    public static class HotelReservationIDsBean {
                        /**
                         * ResID_Type : 501
                         * ResID_Value : 3064637436
                         */

                        @SerializedName("ResID_Type")
                        public String resIDType;
                        @SerializedName("ResID_Value")
                        public String resIDValue;
                    }
                }

                public static class TPAExtensionsBeanX {
                    /**
                     * Commissions : {"EstimatedAmount":{"Amount":309.26,"CurrencyCode":"CNY"}}
                     * Cost : {"Amount":1767.2,"CurrencyCode":"CNY"}
                     */

                    @SerializedName("Commissions")
                    public CommissionsBean commissions;
                    @SerializedName("Cost")
                    public CostBean cost;

                    public static class CommissionsBean {
                        /**
                         * EstimatedAmount : {"Amount":309.26,"CurrencyCode":"CNY"}
                         */

                        @SerializedName("EstimatedAmount")
                        public EstimatedAmountBean estimatedAmount;

                        public static class EstimatedAmountBean {
                            /**
                             * Amount : 309.26
                             * CurrencyCode : CNY
                             */

                            @SerializedName("Amount")
                            public double amount;
                            @SerializedName("CurrencyCode")
                            public String currencyCode;
                        }
                    }

                    public static class CostBean {
                        /**
                         * Amount : 1767.2
                         * CurrencyCode : CNY
                         */

                        @SerializedName("Amount")
                        public double amount;
                        @SerializedName("CurrencyCode")
                        public String currencyCode;
                    }
                }
            }
        }

        public static class WarningsBean {
            /**
             * Type : Unknown
             * ShortText : 该房型不支持送券
             * Code : 110117000105
             * Value : 110117000105-该房型不支持送券
             */

            @SerializedName("Type")
            public String type;
            @SerializedName("ShortText")
            public String shortText;
            @SerializedName("Code")
            public String code;
            @SerializedName("Value")
            public String value;
        }
    }
}
