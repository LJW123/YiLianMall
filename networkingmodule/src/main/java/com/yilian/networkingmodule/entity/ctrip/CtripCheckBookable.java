package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author xiaofo on 2018/9/27.
 */

public class CtripCheckBookable extends HttpResultBean {
public static final int BOOKABLE=1;
public static final int BOOK_UNABLE=0;
    /**
     * AvailabilityStatus : 1
     * InclusiveAmount : 791
     * info : {"ResponseStatus":{"Timestamp":"2018-09-27T15:08:14.2321648+08:00","Ack":"Success","Errors":[],"Extension":[]},"Warnings":[],"Profiles":[],"RoomStays":{"RoomStay":{"RatePlans":{"RatePlan":{"RatePlanDescription":[""],"RatePlanCode":"24883589","RatePlanName":"超豪华客房(限时特惠)[无早]","RoomID":"24883589","RoomName":"超豪华客房(限时特惠)[无早]","PrepaidIndicator":true,"AvailableQuantity":"10","IsInstantConfirm":false,"ReceiveTextRemark":false,"SupplierID":"2274","InvoiceTargetType":4,"MinQtyPerOrder":"1"}},"RoomRates":{"RoomRate":{"Rates":[{"Base":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"},"TPA_Extensions":{"Cost":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":648.37,"CurrencyCode":"CNY"}],"Amount":648.37,"CurrencyCode":"CNY"}},"MaxGuestApplicable":"5","EffectiveDate":"2018-10-01T00:00:00.0000000","ExpireDate":"2018-10-01T00:00:00.0000000"}],"RatePlanCode":"24883589","RoomID":"24883589","RoomTypeCode":"525202"}},"Total":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"},"BasicPropertyInfo":{"VendorMessages":[{"SubSection":[{"Paragraph":["目前北京全城禁烟，酒店均为无烟房。123"],"SubTitle":"城市重要通知"},{"Paragraph":["李红测试"],"SubTitle":"城市重要通知"}],"Title":"酒店提示","Language":"zh","InfoType":"4"}]},"DepositPayments":[{"AmountPercent":{"Amount":791,"CurrencyCode":"CNY"},"Description":["该订单确认后不可被取消修改，若未入住将收取您全额房费{Money}。携程会根据您的付款方式进行预授权或扣除房费，如订单不确认将解除预授权或全额退款至您的付款账户。"],"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"GuaranteeCode":"2","Start":"2018-10-01 00:00:00","End":"2018-10-01 00:00:00"}],"CancelPenalties":{"CancelPenalty":{"AmountPercent":{"DisplayCurrency":[],"Amount":791,"CurrencyCode":"CNY"},"Start":"2018-09-26T14:00:00","End":"2018-10-02T00:00:00"}},"TPA_Extensions":{"SpecialRequestOptions":[{"Name":"","ParagraphNumber":"0","RequestCode":"0","CodeContext":"此为向酒店或代理商申请的特殊价格，价格可能变动且不保证预订成功；提交订单后会先操作扣款。特殊需求皆不能保证满足。","IsNeedUserValue":false,"IsDefaultOption":true,"IsUnique":"T"}],"LastArrivalDateTime":{"DateTime":"2018-10-01T14:00:00.0000000","HourSpan":16,"TimeZoneRPH":0}},"AvailabilityStatus":"AvailableForSale"}},"Errors":[],"TimeStamp":"2018-09-27T15:08:14.2321648+08:00","Version":1,"PrimaryLangID":"zh"}
     */

    @SerializedName("AvailabilityStatus")
    public int availabilityStatus;
    @SerializedName("InclusiveAmount")
    public String inclusiveAmount;
    @SerializedName("info")
    public InfoBean info;

    public static class InfoBean {
        /**
         * ResponseStatus : {"Timestamp":"2018-09-27T15:08:14.2321648+08:00","Ack":"Success","Errors":[],"Extension":[]}
         * Warnings : []
         * Profiles : []
         * RoomStays : {"RoomStay":{"RatePlans":{"RatePlan":{"RatePlanDescription":[""],"RatePlanCode":"24883589","RatePlanName":"超豪华客房(限时特惠)[无早]","RoomID":"24883589","RoomName":"超豪华客房(限时特惠)[无早]","PrepaidIndicator":true,"AvailableQuantity":"10","IsInstantConfirm":false,"ReceiveTextRemark":false,"SupplierID":"2274","InvoiceTargetType":4,"MinQtyPerOrder":"1"}},"RoomRates":{"RoomRate":{"Rates":[{"Base":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"},"TPA_Extensions":{"Cost":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":648.37,"CurrencyCode":"CNY"}],"Amount":648.37,"CurrencyCode":"CNY"}},"MaxGuestApplicable":"5","EffectiveDate":"2018-10-01T00:00:00.0000000","ExpireDate":"2018-10-01T00:00:00.0000000"}],"RatePlanCode":"24883589","RoomID":"24883589","RoomTypeCode":"525202"}},"Total":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"},"BasicPropertyInfo":{"VendorMessages":[{"SubSection":[{"Paragraph":["目前北京全城禁烟，酒店均为无烟房。123"],"SubTitle":"城市重要通知"},{"Paragraph":["李红测试"],"SubTitle":"城市重要通知"}],"Title":"酒店提示","Language":"zh","InfoType":"4"}]},"DepositPayments":[{"AmountPercent":{"Amount":791,"CurrencyCode":"CNY"},"Description":["该订单确认后不可被取消修改，若未入住将收取您全额房费{Money}。携程会根据您的付款方式进行预授权或扣除房费，如订单不确认将解除预授权或全额退款至您的付款账户。"],"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"GuaranteeCode":"2","Start":"2018-10-01 00:00:00","End":"2018-10-01 00:00:00"}],"CancelPenalties":{"CancelPenalty":{"AmountPercent":{"DisplayCurrency":[],"Amount":791,"CurrencyCode":"CNY"},"Start":"2018-09-26T14:00:00","End":"2018-10-02T00:00:00"}},"TPA_Extensions":{"SpecialRequestOptions":[{"Name":"","ParagraphNumber":"0","RequestCode":"0","CodeContext":"此为向酒店或代理商申请的特殊价格，价格可能变动且不保证预订成功；提交订单后会先操作扣款。特殊需求皆不能保证满足。","IsNeedUserValue":false,"IsDefaultOption":true,"IsUnique":"T"}],"LastArrivalDateTime":{"DateTime":"2018-10-01T14:00:00.0000000","HourSpan":16,"TimeZoneRPH":0}},"AvailabilityStatus":"AvailableForSale"}}
         * Errors : []
         * TimeStamp : 2018-09-27T15:08:14.2321648+08:00
         * Version : 1
         * PrimaryLangID : zh
         */

        @SerializedName("ResponseStatus")
        public ResponseStatusBean responseStatus;
        @SerializedName("RoomStays")
        public RoomStaysBean roomStays;
        @SerializedName("TimeStamp")
        public String timeStamp;
        @SerializedName("Version")
        public int version;
        @SerializedName("PrimaryLangID")
        public String primaryLangID;
        @SerializedName("Warnings")
        public List<?> warnings;
        @SerializedName("Profiles")
        public List<?> profiles;
        @SerializedName("Errors")
        public List<?> errors;

        public static class ResponseStatusBean {
            /**
             * Timestamp : 2018-09-27T15:08:14.2321648+08:00
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

        public static class RoomStaysBean {
            /**
             * RoomStay : {"RatePlans":{"RatePlan":{"RatePlanDescription":[""],"RatePlanCode":"24883589","RatePlanName":"超豪华客房(限时特惠)[无早]","RoomID":"24883589","RoomName":"超豪华客房(限时特惠)[无早]","PrepaidIndicator":true,"AvailableQuantity":"10","IsInstantConfirm":false,"ReceiveTextRemark":false,"SupplierID":"2274","InvoiceTargetType":4,"MinQtyPerOrder":"1"}},"RoomRates":{"RoomRate":{"Rates":[{"Base":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"},"TPA_Extensions":{"Cost":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":648.37,"CurrencyCode":"CNY"}],"Amount":648.37,"CurrencyCode":"CNY"}},"MaxGuestApplicable":"5","EffectiveDate":"2018-10-01T00:00:00.0000000","ExpireDate":"2018-10-01T00:00:00.0000000"}],"RatePlanCode":"24883589","RoomID":"24883589","RoomTypeCode":"525202"}},"Total":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"},"BasicPropertyInfo":{"VendorMessages":[{"SubSection":[{"Paragraph":["目前北京全城禁烟，酒店均为无烟房。123"],"SubTitle":"城市重要通知"},{"Paragraph":["李红测试"],"SubTitle":"城市重要通知"}],"Title":"酒店提示","Language":"zh","InfoType":"4"}]},"DepositPayments":[{"AmountPercent":{"Amount":791,"CurrencyCode":"CNY"},"Description":["该订单确认后不可被取消修改，若未入住将收取您全额房费{Money}。携程会根据您的付款方式进行预授权或扣除房费，如订单不确认将解除预授权或全额退款至您的付款账户。"],"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"GuaranteeCode":"2","Start":"2018-10-01 00:00:00","End":"2018-10-01 00:00:00"}],"CancelPenalties":{"CancelPenalty":{"AmountPercent":{"DisplayCurrency":[],"Amount":791,"CurrencyCode":"CNY"},"Start":"2018-09-26T14:00:00","End":"2018-10-02T00:00:00"}},"TPA_Extensions":{"SpecialRequestOptions":[{"Name":"","ParagraphNumber":"0","RequestCode":"0","CodeContext":"此为向酒店或代理商申请的特殊价格，价格可能变动且不保证预订成功；提交订单后会先操作扣款。特殊需求皆不能保证满足。","IsNeedUserValue":false,"IsDefaultOption":true,"IsUnique":"T"}],"LastArrivalDateTime":{"DateTime":"2018-10-01T14:00:00.0000000","HourSpan":16,"TimeZoneRPH":0}},"AvailabilityStatus":"AvailableForSale"}
             */

            @SerializedName("RoomStay")
            public RoomStayBean roomStay;

            public static class RoomStayBean {
                /**
                 * RatePlans : {"RatePlan":{"RatePlanDescription":[""],"RatePlanCode":"24883589","RatePlanName":"超豪华客房(限时特惠)[无早]","RoomID":"24883589","RoomName":"超豪华客房(限时特惠)[无早]","PrepaidIndicator":true,"AvailableQuantity":"10","IsInstantConfirm":false,"ReceiveTextRemark":false,"SupplierID":"2274","InvoiceTargetType":4,"MinQtyPerOrder":"1"}}
                 * RoomRates : {"RoomRate":{"Rates":[{"Base":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"},"TPA_Extensions":{"Cost":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":648.37,"CurrencyCode":"CNY"}],"Amount":648.37,"CurrencyCode":"CNY"}},"MaxGuestApplicable":"5","EffectiveDate":"2018-10-01T00:00:00.0000000","ExpireDate":"2018-10-01T00:00:00.0000000"}],"RatePlanCode":"24883589","RoomID":"24883589","RoomTypeCode":"525202"}}
                 * Total : {"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}
                 * BasicPropertyInfo : {"VendorMessages":[{"SubSection":[{"Paragraph":["目前北京全城禁烟，酒店均为无烟房。123"],"SubTitle":"城市重要通知"},{"Paragraph":["李红测试"],"SubTitle":"城市重要通知"}],"Title":"酒店提示","Language":"zh","InfoType":"4"}]}
                 * DepositPayments : [{"AmountPercent":{"Amount":791,"CurrencyCode":"CNY"},"Description":["该订单确认后不可被取消修改，若未入住将收取您全额房费{Money}。携程会根据您的付款方式进行预授权或扣除房费，如订单不确认将解除预授权或全额退款至您的付款账户。"],"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"GuaranteeCode":"2","Start":"2018-10-01 00:00:00","End":"2018-10-01 00:00:00"}]
                 * CancelPenalties : {"CancelPenalty":{"AmountPercent":{"DisplayCurrency":[],"Amount":791,"CurrencyCode":"CNY"},"Start":"2018-09-26T14:00:00","End":"2018-10-02T00:00:00"}}
                 * TPA_Extensions : {"SpecialRequestOptions":[{"Name":"","ParagraphNumber":"0","RequestCode":"0","CodeContext":"此为向酒店或代理商申请的特殊价格，价格可能变动且不保证预订成功；提交订单后会先操作扣款。特殊需求皆不能保证满足。","IsNeedUserValue":false,"IsDefaultOption":true,"IsUnique":"T"}],"LastArrivalDateTime":{"DateTime":"2018-10-01T14:00:00.0000000","HourSpan":16,"TimeZoneRPH":0}}
                 * AvailabilityStatus : AvailableForSale
                 */

                @SerializedName("RatePlans")
                public RatePlansBean ratePlans;
                @SerializedName("RoomRates")
                public RoomRatesBean roomRates;
                @SerializedName("Total")
                public TotalBean total;
                @SerializedName("BasicPropertyInfo")
                public BasicPropertyInfoBean basicPropertyInfo;
                @SerializedName("CancelPenalties")
                public CancelPenaltiesBean cancelPenalties;
                @SerializedName("TPA_Extensions")
                public TPAExtensionsBeanXXX tpaExtensions;
                @SerializedName("AvailabilityStatus")
                public String availabilityStatus;
                @SerializedName("DepositPayments")
                public List<DepositPaymentsBean> depositPayments;

                public static class RatePlansBean {
                    /**
                     * RatePlan : {"RatePlanDescription":[""],"RatePlanCode":"24883589","RatePlanName":"超豪华客房(限时特惠)[无早]","RoomID":"24883589","RoomName":"超豪华客房(限时特惠)[无早]","PrepaidIndicator":true,"AvailableQuantity":"10","IsInstantConfirm":false,"ReceiveTextRemark":false,"SupplierID":"2274","InvoiceTargetType":4,"MinQtyPerOrder":"1"}
                     */

                    @SerializedName("RatePlan")
                    public RatePlanBean ratePlan;

                    public static class RatePlanBean {
                        /**
                         * RatePlanDescription : [""]
                         * RatePlanCode : 24883589
                         * RatePlanName : 超豪华客房(限时特惠)[无早]
                         * RoomID : 24883589
                         * RoomName : 超豪华客房(限时特惠)[无早]
                         * PrepaidIndicator : true
                         * AvailableQuantity : 10
                         * IsInstantConfirm : false
                         * ReceiveTextRemark : false
                         * SupplierID : 2274
                         * InvoiceTargetType : 4
                         * MinQtyPerOrder : 1
                         */

                        @SerializedName("RatePlanCode")
                        public String ratePlanCode;
                        @SerializedName("RatePlanName")
                        public String ratePlanName;
                        @SerializedName("RoomID")
                        public String roomID;
                        @SerializedName("RoomName")
                        public String roomName;
                        @SerializedName("PrepaidIndicator")
                        public boolean prepaidIndicator;
                        @SerializedName("AvailableQuantity")
                        public String availableQuantity;
                        @SerializedName("IsInstantConfirm")
                        public boolean isInstantConfirm;
                        @SerializedName("ReceiveTextRemark")
                        public boolean receiveTextRemark;
                        @SerializedName("SupplierID")
                        public String supplierID;
                        @SerializedName("InvoiceTargetType")
                        public int invoiceTargetType;
                        @SerializedName("MinQtyPerOrder")
                        public String minQtyPerOrder;
                        @SerializedName("RatePlanDescription")
                        public List<String> ratePlanDescription;
                    }
                }

                public static class RoomRatesBean {
                    /**
                     * RoomRate : {"Rates":[{"Base":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"},"TPA_Extensions":{"Cost":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":648.37,"CurrencyCode":"CNY"}],"Amount":648.37,"CurrencyCode":"CNY"}},"MaxGuestApplicable":"5","EffectiveDate":"2018-10-01T00:00:00.0000000","ExpireDate":"2018-10-01T00:00:00.0000000"}],"RatePlanCode":"24883589","RoomID":"24883589","RoomTypeCode":"525202"}
                     */

                    @SerializedName("RoomRate")
                    public RoomRateBean roomRate;

                    public static class RoomRateBean {
                        /**
                         * Rates : [{"Base":{"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"},"TPA_Extensions":{"Cost":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":648.37,"CurrencyCode":"CNY"}],"Amount":648.37,"CurrencyCode":"CNY"}},"MaxGuestApplicable":"5","EffectiveDate":"2018-10-01T00:00:00.0000000","ExpireDate":"2018-10-01T00:00:00.0000000"}]
                         * RatePlanCode : 24883589
                         * RoomID : 24883589
                         * RoomTypeCode : 525202
                         */

                        @SerializedName("RatePlanCode")
                        public String ratePlanCode;
                        @SerializedName("RoomID")
                        public String roomID;
                        @SerializedName("RoomTypeCode")
                        public String roomTypeCode;
                        @SerializedName("Rates")
                        public List<RatesBean> rates;

                        public static class RatesBean {
                            /**
                             * Base : {"TPA_Extensions":[{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}],"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}
                             * TPA_Extensions : {"Cost":{"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":648.37,"CurrencyCode":"CNY"}],"Amount":648.37,"CurrencyCode":"CNY"}}
                             * MaxGuestApplicable : 5
                             * EffectiveDate : 2018-10-01T00:00:00.0000000
                             * ExpireDate : 2018-10-01T00:00:00.0000000
                             */

                            @SerializedName("Base")
                            public BaseBean base;
                            @SerializedName("TPA_Extensions")
                            public TPAExtensionsBeanX tpaExtensions;
                            @SerializedName("MaxGuestApplicable")
                            public String maxGuestApplicable;
                            @SerializedName("EffectiveDate")
                            public String effectiveDate;
                            @SerializedName("ExpireDate")
                            public String expireDate;

                            public static class BaseBean {
                                /**
                                 * TPA_Extensions : [{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}]
                                 * ExclusiveAmount : 0
                                 * InclusiveAmount : 791
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
                                     * InclusiveAmount : 791
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

                            public static class TPAExtensionsBeanX {
                                /**
                                 * Cost : {"DisplayCurrency":[{"ExclusiveAmount":0,"InclusiveAmount":648.37,"CurrencyCode":"CNY"}],"Amount":648.37,"CurrencyCode":"CNY"}
                                 */

                                @SerializedName("Cost")
                                public CostBean cost;

                                public static class CostBean {
                                    /**
                                     * DisplayCurrency : [{"ExclusiveAmount":0,"InclusiveAmount":648.37,"CurrencyCode":"CNY"}]
                                     * Amount : 648.37
                                     * CurrencyCode : CNY
                                     */

                                    @SerializedName("Amount")
                                    public double amount;
                                    @SerializedName("CurrencyCode")
                                    public String currencyCode;
                                    @SerializedName("DisplayCurrency")
                                    public List<DisplayCurrencyBean> displayCurrency;

                                    public static class DisplayCurrencyBean {
                                        /**
                                         * ExclusiveAmount : 0
                                         * InclusiveAmount : 648.37
                                         * CurrencyCode : CNY
                                         */

                                        @SerializedName("ExclusiveAmount")
                                        public int exclusiveAmount;
                                        @SerializedName("InclusiveAmount")
                                        public double inclusiveAmount;
                                        @SerializedName("CurrencyCode")
                                        public String currencyCode;
                                    }
                                }
                            }
                        }
                    }
                }

                public static class TotalBean {
                    /**
                     * TPA_Extensions : [{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}]
                     * ExclusiveAmount : 0
                     * InclusiveAmount : 791
                     * CurrencyCode : CNY
                     */

                    @SerializedName("ExclusiveAmount")
                    public int exclusiveAmount;
                    @SerializedName("InclusiveAmount")
                    public int inclusiveAmount;
                    @SerializedName("CurrencyCode")
                    public String currencyCode;
                    @SerializedName("TPA_Extensions")
                    public List<TPAExtensionsBeanXX> tpaExtensions;

                    public static class TPAExtensionsBeanXX {
                        /**
                         * ExclusiveAmount : 0
                         * InclusiveAmount : 791
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

                public static class BasicPropertyInfoBean {
                    @SerializedName("VendorMessages")
                    public List<VendorMessagesBean> vendorMessages;

                    public static class VendorMessagesBean {
                        /**
                         * SubSection : [{"Paragraph":["目前北京全城禁烟，酒店均为无烟房。123"],"SubTitle":"城市重要通知"},{"Paragraph":["李红测试"],"SubTitle":"城市重要通知"}]
                         * Title : 酒店提示
                         * Language : zh
                         * InfoType : 4
                         */

                        @SerializedName("Title")
                        public String title;
                        @SerializedName("Language")
                        public String language;
                        @SerializedName("InfoType")
                        public String infoType;
                        @SerializedName("SubSection")
                        public List<SubSectionBean> subSection;

                        public static class SubSectionBean {
                            /**
                             * Paragraph : ["目前北京全城禁烟，酒店均为无烟房。123"]
                             * SubTitle : 城市重要通知
                             */

                            @SerializedName("SubTitle")
                            public String subTitle;
                            @SerializedName("Paragraph")
                            public List<String> paragraph;
                        }
                    }
                }

                public static class CancelPenaltiesBean {
                    /**
                     * CancelPenalty : {"AmountPercent":{"DisplayCurrency":[],"Amount":791,"CurrencyCode":"CNY"},"Start":"2018-09-26T14:00:00","End":"2018-10-02T00:00:00"}
                     */

                    @SerializedName("CancelPenalty")
                    public CancelPenaltyBean cancelPenalty;

                    public static class CancelPenaltyBean {
                        /**
                         * AmountPercent : {"DisplayCurrency":[],"Amount":791,"CurrencyCode":"CNY"}
                         * Start : 2018-09-26T14:00:00
                         * End : 2018-10-02T00:00:00
                         */

                        @SerializedName("AmountPercent")
                        public AmountPercentBean amountPercent;
                        @SerializedName("Start")
                        public String start;
                        @SerializedName("End")
                        public String end;

                        public static class AmountPercentBean {
                            /**
                             * DisplayCurrency : []
                             * Amount : 791
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
                }

                public static class TPAExtensionsBeanXXX {
                    /**
                     * SpecialRequestOptions : [{"Name":"","ParagraphNumber":"0","RequestCode":"0","CodeContext":"此为向酒店或代理商申请的特殊价格，价格可能变动且不保证预订成功；提交订单后会先操作扣款。特殊需求皆不能保证满足。","IsNeedUserValue":false,"IsDefaultOption":true,"IsUnique":"T"}]
                     * LastArrivalDateTime : {"DateTime":"2018-10-01T14:00:00.0000000","HourSpan":16,"TimeZoneRPH":0}
                     */

                    @SerializedName("LastArrivalDateTime")
                    public LastArrivalDateTimeBean lastArrivalDateTime;
                    @SerializedName("SpecialRequestOptions")
                    public List<SpecialRequestOptionsBean> specialRequestOptions;

                    public static class LastArrivalDateTimeBean {
                        /**
                         * DateTime : 2018-10-01T14:00:00.0000000
                         * HourSpan : 16
                         * TimeZoneRPH : 0
                         */

                        @SerializedName("DateTime")
                        public String dateTime;
                        @SerializedName("HourSpan")
                        public int hourSpan;
                        @SerializedName("TimeZoneRPH")
                        public int timeZoneRPH;
                    }

                    public static class SpecialRequestOptionsBean {
                        /**
                         * Name :
                         * ParagraphNumber : 0
                         * RequestCode : 0
                         * CodeContext : 此为向酒店或代理商申请的特殊价格，价格可能变动且不保证预订成功；提交订单后会先操作扣款。特殊需求皆不能保证满足。
                         * IsNeedUserValue : false
                         * IsDefaultOption : true
                         * IsUnique : T
                         */

                        @SerializedName("Name")
                        public String name;
                        @SerializedName("ParagraphNumber")
                        public String paragraphNumber;
                        @SerializedName("RequestCode")
                        public String requestCode;
                        @SerializedName("CodeContext")
                        public String codeContext;
                        @SerializedName("IsNeedUserValue")
                        public boolean isNeedUserValue;
                        @SerializedName("IsDefaultOption")
                        public boolean isDefaultOption;
                        @SerializedName("IsUnique")
                        public String isUnique;
                    }
                }

                public static class DepositPaymentsBean {
                    /**
                     * AmountPercent : {"Amount":791,"CurrencyCode":"CNY"}
                     * Description : ["该订单确认后不可被取消修改，若未入住将收取您全额房费{Money}。携程会根据您的付款方式进行预授权或扣除房费，如订单不确认将解除预授权或全额退款至您的付款账户。"]
                     * TPA_Extensions : [{"ExclusiveAmount":0,"InclusiveAmount":791,"CurrencyCode":"CNY"}]
                     * GuaranteeCode : 2
                     * Start : 2018-10-01 00:00:00
                     * End : 2018-10-01 00:00:00
                     */

                    @SerializedName("AmountPercent")
                    public AmountPercentBeanX amountPercent;
                    @SerializedName("GuaranteeCode")
                    public String guaranteeCode;
                    @SerializedName("Start")
                    public String start;
                    @SerializedName("End")
                    public String end;
                    @SerializedName("Description")
                    public List<String> description;
                    @SerializedName("TPA_Extensions")
                    public List<TPAExtensionsBeanXXXX> tpaExtensions;

                    public static class AmountPercentBeanX {
                        /**
                         * Amount : 791
                         * CurrencyCode : CNY
                         */

                        @SerializedName("Amount")
                        public int amount;
                        @SerializedName("CurrencyCode")
                        public String currencyCode;
                    }

                    public static class TPAExtensionsBeanXXXX {
                        /**
                         * ExclusiveAmount : 0
                         * InclusiveAmount : 791
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
        }
    }
}
