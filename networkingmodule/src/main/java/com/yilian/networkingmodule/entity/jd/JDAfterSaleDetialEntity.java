package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 京东售后记录详情
 *
 * @author Created by Zg on 2018/5/29.
 */

public class JDAfterSaleDetialEntity extends HttpResultBean {


    @SerializedName("data")
    public DataBean data;

    public static class DataBean {

        /**
         * afsServiceId: 428264969 // 服务单号
         * <p>
         * backTypeName: 退款至奖励, // 退款方式说明
         * customerExpect: 10, // 服务类型码 退货(10)、换货(20)、维修(30)
         * <p>
         * afsApplyTime: "2018-05-22 16:46:17", // 申请时间
         * <p>
         * pickwareName:  上门取件 // 返货方式
         * <p>
         * orderId: 74040715959, // 京东订单号
         * <p>
         * afsServicePrice: ""// 退款金额(原订单商品金额总数,不包含运费)
         * <p>
         * isHasInvoice: 0, // 是不是有发票 0没有1有
         * <p>
         * isNeedDetectionReport: 0,// 是不是有检测报告 0没有1有
         * <p>
         * isHasPackage: 0, // 是否有包装 0没有1有
         * <p>
         * questionPic: "", // 上传图片访问地址, 不同图片逗号分割，可能为空
         * <p>
         * afsServiceStep: 20, // 服务单环节: 申请阶段(10),审核不通过 (20),客服审核(21),商家审核 (22),京东收货(31),商家收货 (32), 京东处理(33),商家处理 (34), 用户确认(40),完成 (50),取消(60);
         * <p>
         * afsServiceStepName: "审核关闭", // 服务单环节名称: 申请阶段,客服审核,商家审 核,京东收货,商家收货, 京东 处理,商家处理, 用户确认,完成, 取消;
         * <p>
         * approveNotes: "亲爱的客户，很抱歉您的商品出现这样的问题。您的申请因（此单关闭 不退 ），此单申请暂时为您取消。如您有其他需求备注订单号以邮件的形式发至sqwangting5@jd.com，我们为您核实办理。感谢您对京东的支持。	", // 审核意见
         * <p>
         * questionDesc: "不要了",// 问题描述
         * <p>
         * approvedResult: null, // 审核结果: 直赔积分(11),直赔余额 (12),直赔优惠卷 (13),直赔京 豆 (14),直赔商品 (21), 上门换新 (22),自营取件 (31),客户送货(32),客户发货 (33),闪电退款 (34),虚拟退款 (35),大家电检测 (80),大家电安装(81),大家电移机 (82),大家电维修(83),大家电其它 (84);
         * <p>
         * approvedResultName: null, // 审核结果名称: 直赔积分,直赔余额,直赔优惠卷,直赔京豆, 直赔商品,上门换新,自营取件,客户送货,客户发货,闪电退 款,虚拟退款,大家电检测,大家电安装,大家电移机,大家电维修 , 大家电其它;
         * <p>
         * processResult: null, // 处理结果: 返修换新(23), 退货(40), 换良(50),原返 60),病单 (71),出检(72), 维修 (73), 强制关单 (80),线下换新(90)
         * <p>
         * processResultName: null, // 处理结果名称: 返修换新,退货 , 换良,原返, 病单,出检,维修,强制关单,线下换新
         * <p>
         * serviceCustomerInfoDTO: { // 客户信息
         * customerPin: "hnlsdz2018", // 客户京东账号
         * customerName: "李楠", // 用户昵称
         * customerContactName: "刘", // 服务单联系人
         * customerTel: "18237111819", // 联系电话
         * customerMobilePhone: null, // 手机号
         * customerEmail: null, // 电子邮件地址
         * customerPostcode: null // 邮编
         * },
         * <p>
         * serviceAftersalesAddressInfoDTO: { // 售后地址信息
         * address:"",// 售后地址
         * tel:"",// 售后电话
         * linkMan:"",// 售后联系人
         * postCode:"",// 售后邮编
         * },
         * <p>
         * serviceExpressInfoDTO: { // 客户发货信息
         * afsServiceId:"",//服务单号
         * freightMoney:"",//运费
         * expressCompany:"",//快递公司名称
         * deliverDate:"",//客户发货日期
         * expressCode:"",//快递单号
         * },
         * <p>
         * serviceFinanceDetailInfoDTOs: [{ // 退款明细
         * refundWay:"",// 退款方式
         * refundWayName:"",// 退款方式名称
         * status:"",// 状态
         * statusName:"",// 状态名称
         * refundPrice:"",// 退款金额
         * wareName:"",// 商品名称
         * wareId:"",// 商品编号
         * },
         * {}],
         * <p>
         * serviceTrackInfoDTOs: [ // 服务单追踪信息
         * {
         * afsServiceId: 428264969, // 服务单号
         * title: "预处理环节", // 追踪标题
         * context: "您的服务单已申请成功，待售后审核中", // 追踪内容
         * createDate: "2018-05-22 16:46:17", // 提交时间
         * createName: "系统", // 操作人昵称
         * createPin: "afs system" // 操作人账号
         * },
         * {}
         * ],
         * <p>
         * serviceDetailInfoDTOs: [ // 服务单商品 明细
         * {
         * wareId: 1184648, // 商品编号
         * wareName: "南街村 北京方便面 麻辣味 65g*40袋", // 商品名称
         * wareBrand: "南街村", // 商品品牌
         * afsDetailType: 10, // 明细类型 主商品(10), 赠品(20), 附 件(30)，拍拍取主商品就可以
         * wareDescribe: null // 附件描述
         * },
         * {},
         * ],
         * <p>
         * allowOperations: [ // 获取服务单允许的操作列表
         * {
         * 列表为空代表不允许操作
         * 列表包含 1 代表取消
         * 列表包含 2 代表允许填写或者修改客户发货信息
         * },
         * ],
         */

        @SerializedName("afsServiceId")
        private String afsServiceId;
         @SerializedName("backTypeName")
        private String backTypeName;
        @SerializedName("customerExpect")
        private int customerExpect;
        @SerializedName("customerExpectStr")
        private String customerExpectStr;
        @SerializedName("afsApplyTime")
        private String afsApplyTime;
        @SerializedName("pickwareName")
        private String pickwareName;
        @SerializedName("orderId")
        private String orderId;
        @SerializedName("skuId")
        private String skuId;
        @SerializedName("afsServicePrice")
        private String afsServicePrice;
        @SerializedName("isHasInvoice")
        private int isHasInvoice;
        @SerializedName("isNeedDetectionReport")
        private int isNeedDetectionReport;
        @SerializedName("isHasPackage")
        private int isHasPackage;
        @SerializedName("questionPic")
        private String questionPic;
        @SerializedName("afsServiceStep")
        private int afsServiceStep;
        @SerializedName("afsServiceStepName")
        private String afsServiceStepName;
        @SerializedName("approveNotes")
        private String approveNotes;
        @SerializedName("questionDesc")
        private String questionDesc;
        @SerializedName("approvedResult")
        private String approvedResult;
        @SerializedName("approvedResultName")
        private String approvedResultName;
        @SerializedName("processResult")
        private String processResult;
        @SerializedName("processResultName")
        private String processResultName;
        @SerializedName("serviceCustomerInfoDTO")
        private serviceCustomerInfoDTOBean serviceCustomerInfoDTO;
        @SerializedName("serviceTrackInfoDTOs")
        private List<serviceTrackInfoDTOs> serviceTrackInfoDTOs;
        /**
         * 退回代购券
         */
        @SerializedName("returnQuan")
        private String returnQuan;

        public String getReturnQuan() {
            return returnQuan == null ? "0" : returnQuan;
        }

        public void setReturnQuan(String returnQuan) {
            this.returnQuan = returnQuan;
        }

        public String getAfsServiceId() {
            return afsServiceId == null ? "" : afsServiceId;
        }

        public void setAfsServiceId(String afsServiceId) {
            this.afsServiceId = afsServiceId;
        }

        public String getBackTypeName() {
            return backTypeName == null ? "" : backTypeName;
        }

        public void setBackTypeName(String backTypeName) {
            this.backTypeName = backTypeName;
        }

        public int getCustomerExpect() {
            return customerExpect;
        }

        public void setCustomerExpect(int customerExpect) {
            this.customerExpect = customerExpect;
        }

        public String getCustomerExpectStr() {
            return customerExpectStr == null ? "" : customerExpectStr;
        }

        public void setCustomerExpectStr(String customerExpectStr) {
            this.customerExpectStr = customerExpectStr;
        }

        public List<JDAfterSaleDetialEntity.serviceTrackInfoDTOs> getServiceTrackInfoDTOs() {
            if (serviceTrackInfoDTOs == null) {
                return new ArrayList<>();
            }
            return serviceTrackInfoDTOs;
        }

        public void setServiceTrackInfoDTOs(List<JDAfterSaleDetialEntity.serviceTrackInfoDTOs> serviceTrackInfoDTOs) {
            this.serviceTrackInfoDTOs = serviceTrackInfoDTOs;
        }

        public String getAfsApplyTime() {
            return afsApplyTime == null ? "" : afsApplyTime;
        }

        public void setAfsApplyTime(String afsApplyTime) {
            this.afsApplyTime = afsApplyTime;
        }

        public String getPickwareName() {
            return pickwareName == null ? "" : pickwareName;
        }

        public void setPickwareName(String pickwareName) {
            this.pickwareName = pickwareName;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getAfsServicePrice() {
            return afsServicePrice == null ? "" : afsServicePrice;
        }

        public void setAfsServicePrice(String afsServicePrice) {
            this.afsServicePrice = afsServicePrice;
        }

        public int getIsHasInvoice() {
            return isHasInvoice;
        }

        public void setIsHasInvoice(int isHasInvoice) {
            this.isHasInvoice = isHasInvoice;
        }

        public int getIsNeedDetectionReport() {
            return isNeedDetectionReport;
        }

        public void setIsNeedDetectionReport(int isNeedDetectionReport) {
            this.isNeedDetectionReport = isNeedDetectionReport;
        }

        public int getIsHasPackage() {
            return isHasPackage;
        }

        public void setIsHasPackage(int isHasPackage) {
            this.isHasPackage = isHasPackage;
        }

        public String getQuestionPic() {
            return questionPic == null ? "" : questionPic;
        }

        public void setQuestionPic(String questionPic) {
            this.questionPic = questionPic;
        }

        public int getAfsServiceStep() {
            return afsServiceStep;
        }

        public void setAfsServiceStep(int afsServiceStep) {
            this.afsServiceStep = afsServiceStep;
        }

        public String getAfsServiceStepName() {
            return afsServiceStepName == null ? "" : afsServiceStepName;
        }

        public void setAfsServiceStepName(String afsServiceStepName) {
            this.afsServiceStepName = afsServiceStepName;
        }

        public String getApproveNotes() {
            return approveNotes == null ? "" : approveNotes;
        }

        public void setApproveNotes(String approveNotes) {
            this.approveNotes = approveNotes;
        }

        public String getQuestionDesc() {
            return questionDesc == null ? "" : questionDesc;
        }

        public void setQuestionDesc(String questionDesc) {
            this.questionDesc = questionDesc;
        }

        public String getApprovedResult() {
            return approvedResult == null ? "" : approvedResult;
        }

        public void setApprovedResult(String approvedResult) {
            this.approvedResult = approvedResult;
        }

        public String getApprovedResultName() {
            return approvedResultName == null ? "" : approvedResultName;
        }

        public void setApprovedResultName(String approvedResultName) {
            this.approvedResultName = approvedResultName;
        }

        public String getProcessResult() {
            return processResult == null ? "" : processResult;
        }

        public void setProcessResult(String processResult) {
            this.processResult = processResult;
        }

        public String getProcessResultName() {
            return processResultName == null ? "" : processResultName;
        }

        public void setProcessResultName(String processResultName) {
            this.processResultName = processResultName;
        }

        public serviceCustomerInfoDTOBean getServiceCustomerInfoDTO() {
            return serviceCustomerInfoDTO;
        }

        public void setServiceCustomerInfoDTO(serviceCustomerInfoDTOBean serviceCustomerInfoDTO) {
            this.serviceCustomerInfoDTO = serviceCustomerInfoDTO;
        }

        public String getSkuId() {
            return skuId == null ? "" : skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }
    }

    // 客户信息
    public static class serviceCustomerInfoDTOBean {


        /**
         { // 客户信息
         customerPin: "hnlsdz2018", // 客户京东账号
         customerName: "李楠", // 用户昵称
         customerContactName: "刘", // 服务单联系人
         customerTel: "18237111819", // 联系电话
         customerMobilePhone: null, // 手机号
         customerEmail: null, // 电子邮件地址
         customerPostcode: null // 邮编
         }
         */

        @SerializedName("customerPin")
        private String customerPin;
        @SerializedName("customerName")
        private String customerName;
        @SerializedName("customerContactName")
        private String customerContactName;
        @SerializedName("customerTel")
        private String customerTel;
        @SerializedName("customerMobilePhone")
        private Object customerMobilePhone;
        @SerializedName("customerEmail")
        private Object customerEmail;
        @SerializedName("customerPostcode")
        private Object customerPostcode;

        public String getCustomerPin() {
            return customerPin == null ? "" : customerPin;
        }

        public void setCustomerPin(String customerPin) {
            this.customerPin = customerPin;
        }

        public String getCustomerName() {
            return customerName == null ? "" : customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerContactName() {
            return customerContactName == null ? "" : customerContactName;
        }

        public void setCustomerContactName(String customerContactName) {
            this.customerContactName = customerContactName;
        }

        public String getCustomerTel() {
            return customerTel == null ? "" : customerTel;
        }

        public void setCustomerTel(String customerTel) {
            this.customerTel = customerTel;
        }

        public Object getCustomerMobilePhone() {
            return customerMobilePhone;
        }

        public void setCustomerMobilePhone(Object customerMobilePhone) {
            this.customerMobilePhone = customerMobilePhone;
        }

        public Object getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(Object customerEmail) {
            this.customerEmail = customerEmail;
        }

        public Object getCustomerPostcode() {
            return customerPostcode;
        }

        public void setCustomerPostcode(Object customerPostcode) {
            this.customerPostcode = customerPostcode;
        }
    }

    // 服务单追踪信息
    public static class serviceTrackInfoDTOs implements Serializable {

        /**
         * afsServiceId : 428264969
         * title : 预处理环节
         * context : 您的服务单已申请成功，待售后审核中
         * createDate : 2018-05-22 16:46:17
         * createName : 系统
         * createPin : afs system
         */

        @SerializedName("afsServiceId")
        private int afsServiceId;
        @SerializedName("title")
        private String title;
        @SerializedName("context")
        private String context;
        @SerializedName("createDate")
        private String createDate;
        @SerializedName("createName")
        private String createName;
        @SerializedName("createPin")
        private String createPin;

        public int getAfsServiceId() {
            return afsServiceId;
        }

        public void setAfsServiceId(int afsServiceId) {
            this.afsServiceId = afsServiceId;
        }

        public String getTitle() {
            return title == null ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContext() {
            return context == null ? "" : context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getCreateDate() {
            return createDate == null ? "" : createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCreateName() {
            return createName == null ? "" : createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public String getCreatePin() {
            return createPin == null ? "" : createPin;
        }

        public void setCreatePin(String createPin) {
            this.createPin = createPin;
        }
    }
}
