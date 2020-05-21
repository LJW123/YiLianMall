package com.leshan.ylyj.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.leshan.ylyj.view.activity.Integraltreasuremodel.ExchangeIndexActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.ExchangeIndexRecordActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.IntegralRecordActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.IntegralTreasureActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.IntegrationRecordActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.MakeMoneyActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.SetIntegraNumActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.SetRetainedIntegraActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.ShiftToActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.ShiftToStateActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.TurnOutIntegralActivity;
import com.leshan.ylyj.view.activity.Integraltreasuremodel.TurnOutIntegralStateActivity;
import com.leshan.ylyj.view.activity.bankmodel.BindPublicBindCardActivity;
import com.leshan.ylyj.view.activity.bankmodel.CardPrivateDetaiInfolActivity;
import com.leshan.ylyj.view.activity.bankmodel.MyBankCardsActivity;
import com.leshan.ylyj.view.activity.bankmodel.MyPurseActivity;
import com.leshan.ylyj.view.activity.bankmodel.SetUpCashCardsActivity;
import com.leshan.ylyj.view.activity.bankmodel.TwoTypeCardActivity;
import com.leshan.ylyj.view.activity.creditmodel.CheckCarLicenseActivity;
import com.leshan.ylyj.view.activity.creditmodel.CheckCorrectCarLicenseActivity;
import com.leshan.ylyj.view.activity.creditmodel.CheckCorrectDrivingLicenseActivity;
import com.leshan.ylyj.view.activity.creditmodel.CheckDrivingLicenseActivity;
import com.leshan.ylyj.view.activity.creditmodel.CheckHaveDrivingLicenceActivity;
import com.leshan.ylyj.view.activity.creditmodel.CheckHaveDrivingLicenseActivity;
import com.leshan.ylyj.view.activity.creditmodel.CheckTheIdentityCardActivity;
import com.leshan.ylyj.view.activity.creditmodel.ConfirmIdentityInformationActivity;
import com.leshan.ylyj.view.activity.creditmodel.CreditCheckEachOtherActivity;
import com.leshan.ylyj.view.activity.creditmodel.CreditFootmarkActivity;
import com.leshan.ylyj.view.activity.creditmodel.CreditManagementActivity;
import com.leshan.ylyj.view.activity.creditmodel.DrivingLicenseActivity;
import com.leshan.ylyj.view.activity.creditmodel.EditCarLicenseActivity;
import com.leshan.ylyj.view.activity.creditmodel.EducationRegionActivity;
import com.leshan.ylyj.view.activity.creditmodel.EdutDrivingLicenseActivity;
import com.leshan.ylyj.view.activity.creditmodel.ExistingIDCardActivity;
import com.leshan.ylyj.view.activity.creditmodel.HaveDrivingLicenceActivity;
import com.leshan.ylyj.view.activity.creditmodel.HaveVehicleLicenceDiscActivity;
import com.leshan.ylyj.view.activity.creditmodel.IDInformationVerificationActivity;
import com.leshan.ylyj.view.activity.creditmodel.IdEditActivity;
import com.leshan.ylyj.view.activity.creditmodel.KnowMoreCreditActivity;
import com.leshan.ylyj.view.activity.creditmodel.MyBlankNoteActivity;
import com.leshan.ylyj.view.activity.creditmodel.MyCarLicenseActivity;
import com.leshan.ylyj.view.activity.creditmodel.MyCreditActivity;
import com.leshan.ylyj.view.activity.creditmodel.MyCreditGuardActivity;
import com.leshan.ylyj.view.activity.creditmodel.MyEmailActivity;
import com.leshan.ylyj.view.activity.creditmodel.MyEmailSuccessActivity;
import com.leshan.ylyj.view.activity.creditmodel.MyPersonalInforActivity;
import com.leshan.ylyj.view.activity.creditmodel.RecordActivity;
import com.leshan.ylyj.view.activity.creditmodel.SchoolNameActivity;
import com.leshan.ylyj.view.activity.creditmodel.ShareCreditActivity;
import com.leshan.ylyj.view.activity.creditmodel.SubmitDrivingLicenceActivity;
import com.leshan.ylyj.view.activity.creditmodel.SubmitEducationActivity;
import com.leshan.ylyj.view.activity.creditmodel.UploadingDrivingLicenceActivity;
import com.leshan.ylyj.view.activity.healthmodel.HealthFriendRobActivity;
import com.leshan.ylyj.view.activity.healthmodel.HealthFruitActivity;
import com.leshan.ylyj.view.activity.healthmodel.HealthFruitListActivity;
import com.leshan.ylyj.view.activity.healthmodel.HealthHomeActivity;
import com.leshan.ylyj.view.activity.healthmodel.HelpOtherActivity;
import com.leshan.ylyj.view.activity.healthmodel.MyFamilyHealthActivity;
import com.leshan.ylyj.view.activity.mypursemodel.TransactionRecordActivity;
import com.leshan.ylyj.view.activity.welfaremodel.WelfareDonationListActivity;
import com.leshan.ylyj.view.activity.welfaremodel.WelfareGetOperationActivity;
import com.leshan.ylyj.view.activity.welfaremodel.WelfareLoveMsgActivity;
import com.leshan.ylyj.view.activity.welfaremodel.WelfareSubMsgActivity;
import com.yilian.mylibrary.Constants;

import rxfamily.entity.ALiCarSuccessDataInfo;
import rxfamily.entity.DrivingLicenseOutputValueEntity;
import rxfamily.entity.IntegralStatusEntity;


/**
 * 用于activity跳转等
 * Created by Zg on 2017/6/13
 */
public class SkipUtils {

    /**
     * 分享信用
     *
     * @param mContext
     */
    public static void toShareCredit(Context mContext, String creditTotal) {
        Intent intent = new Intent(mContext, ShareCreditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("creditTotal", creditTotal);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 了解信用
     *
     * @param mContext
     */
    public static void toKnowMoreCredit(Context mContext, int myNum) {
        Intent intent = new Intent(mContext, KnowMoreCreditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("myNum", myNum);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 信用管理
     *
     * @param mContext
     */
    public static void toCreditManagement(Context mContext) {
        Intent intent = new Intent(mContext, CreditManagementActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * identification
     * 1、信用提升,、2个人信息
     *
     * @param mContext
     */
    public static void toMyPersonalInfor(Context mContext, String identification) {
        Intent intent = new Intent(mContext, MyPersonalInforActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("identification", identification);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 我的白条
     *
     * @param mContext
     */
    public static void toMyWhite(Context mContext) {
        Intent intent = new Intent(mContext, MyBlankNoteActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 学历
     *
     * @param mContext
     */
    public static void toRecord(Context mContext) {
        Intent intent = new Intent(mContext, RecordActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 信用守护
     *
     * @param mContext
     */
    public static void toMyCreditGuard(Context mContext) {
        Intent intent = new Intent(mContext, MyCreditGuardActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 收货订单
     *
     * @param mContext
     */
    public static void toMyMallOrderListComment(Context mContext, int order_Type) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MyMallOrderListActivity"));
        Bundle bundle = new Bundle();
        bundle.putInt("order_Type", order_Type);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    /**
     * 基本信息完善
     *
     * @param mContext
     */
    public static void toEssentialInformation(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.BasicInfomationViewImplActivity"));
        mContext.startActivity(intent);
    }

    /**
     * 实名认证
     *
     * @param mContext
     */
    public static void toCertificationViewImpl(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.CertificationViewImplActivity"));
        mContext.startActivity(intent);
    }

    /**
     * 爱心捐赠
     *
     * @param mContext
     */
    public static void toWelfareDonationList(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.welfaremodel.WelfareDonationListActivity"));
        mContext.startActivity(intent);
    }


    /**
     * 信用足迹
     *
     * @param mContext
     */
    public static void toMyCreditFootMark(Context mContext) {
        Intent intent = new Intent(mContext, CreditFootmarkActivity.class);
        mContext.startActivity(intent);
    }


    /**
     * 信用互查
     *
     * @param mContext
     */
    public static void toCreditCheckEachOther(Context mContext) {
        Intent intent = new Intent(mContext, CreditCheckEachOtherActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 学历地区
     *
     * @param mContext
     */
    public static void toEducationRegion(Context mContext) {
        Intent intent = new Intent();
        intent.setClass(mContext, EducationRegionActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    /**
     * 院校名称
     *
     * @param mContext
     */
    public static void toSchoolName(Context mContext, String provinceid) {
        Intent intent = new Intent();
        intent.setClass(mContext, SchoolNameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("provinceid", provinceid);
        intent.putExtras(bundle);
        ((Activity) mContext).startActivityForResult(intent, 1);
    }

    /**
     * 提交学历信息
     *
     * @param mContext
     */
    public static void toSubmitEducation(Context mContext) {
        Intent intent = new Intent(mContext, SubmitEducationActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 邮箱
     *
     * @param mContext
     */
    public static void toMyEmail(Context mContext) {
        Intent intent = new Intent(mContext, MyEmailActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 邮箱提交成功
     *
     * @param mContext
     */
    public static void toMyEmailSuccess(Context mContext) {
        Intent intent = new Intent(mContext, MyEmailSuccessActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 邮箱提交成功
     *
     * @param mContext
     */
    public static void toMyEmailSuccess(Context mContext, String commitDate, String email) {
        Intent intent = new Intent(mContext, MyEmailSuccessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("commitDate", commitDate);
        bundle.putString("email", email);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 驾驶证
     *
     * @param mContext
     */
    public static void toMyCarLicense(Context mContext) {
        Intent intent = new Intent(mContext, MyCarLicenseActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 上传驾驶证
     *
     * @param mContext
     */
    public static void toUploadingDrivingLicence(Context mContext, String str) {
        Intent intent = new Intent(mContext, UploadingDrivingLicenceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("str", str);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 行驶证
     *
     * @param mContext
     */
    public static void toDrivingLicense(Context mContext) {
        Intent intent = new Intent(mContext, DrivingLicenseActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 核对驾驶证信息
     *
     * @param mContext
     */
    public static void toCheckCarLicense(Context mContext) {
        Intent intent = new Intent(mContext, CheckCarLicenseActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 核对驾驶证信息
     *
     * @param mContext
     */
    public static void toCheckCarLicense(Context mContext, ALiCarSuccessDataInfo bean, String positive, String back) {
        Intent intent = new Intent(mContext, CheckCarLicenseActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("bean", bean);
        intent.putExtra("positive", positive);
        intent.putExtra("back", back);
        intent.putExtras(mBundle);
        mContext.startActivity(intent);
    }

    /**
     * 驾驶证核对证无误
     *
     * @param mContext
     */
    public static void toCheckCorrectCarLicense(Context mContext) {
        Intent intent = new Intent(mContext, CheckCorrectCarLicenseActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 提交驾驶证（提交成功）
     * 传入驾驶证标识
     *
     * @param mContext
     */
    public static void toSubmitDrivingLicence(Context mContext, String str) {
        Intent intent = new Intent(mContext, SubmitDrivingLicenceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("str", str);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 编辑驾驶证
     *
     * @param mContext
     */
    public static void toEditCarLicense(Context mContext) {
        Intent intent = new Intent(mContext, EditCarLicenseActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 已有驾驶证
     *
     * @param mContext
     */
    public static void toHaveDrivingLicence(Context mContext) {
        Intent intent = new Intent(mContext, HaveDrivingLicenceActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 查看已有驾驶证
     *
     * @param mContext
     */
    public static void toCheckHaveDrivingLicence(Context mContext, String name, String carId, String fileNo) {
        Intent intent = new Intent(mContext, CheckHaveDrivingLicenceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("carId", carId);
        bundle.putString("fileNo", fileNo);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 查看已有驾驶证
     *
     * @param mContext
     */
    public static void toCheckHaveDrivingLicence(Context mContext) {
        Intent intent = new Intent(mContext, CheckHaveDrivingLicenceActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 核对行驶证信息
     *
     * @param mContext
     */
    public static void toCheckDrivingLicense(Context mContext, DrivingLicenseOutputValueEntity faceValue, DrivingLicenseOutputValueEntity backValue, String positive, String back) {
        Intent intent = new Intent(mContext, CheckDrivingLicenseActivity.class);
        intent.putExtra("value_face", faceValue);
        intent.putExtra("value_back", backValue);
        intent.putExtra("positive", positive);
        intent.putExtra("back", back);
        mContext.startActivity(intent);
    }

    /**
     * 行车证核对证无误
     *
     * @param mContext
     */
    public static void toCheckCorrectDrivingLicense(Context mContext) {
        Intent intent = new Intent(mContext, CheckCorrectDrivingLicenseActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 编辑行车证
     *
     * @param mContext
     */
    public static void toEdutDrivingLicense(Context mContext) {
        Intent intent = new Intent(mContext, EdutDrivingLicenseActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 已有行车证
     *
     * @param mContext
     */
    public static void toHaveVehicleLicenceDisc(Context mContext) {
        Intent intent = new Intent(mContext, HaveVehicleLicenceDiscActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 查看已有行驶证
     *
     * @param mContext
     */
    public static void toCheckHaveDrivingLicense(Context mContext, String name, String carNo, String vin) {
        Intent intent = new Intent(mContext, CheckHaveDrivingLicenseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("carNo", carNo);
        bundle.putString("vin", vin);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 身份证信息验证
     *
     * @param mContext
     */
    public static void toIDInformationVerification(Context mContext) {
        Intent intent = new Intent(mContext, IDInformationVerificationActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 确认并提交身份证信息
     *
     * @param mContext
     */
    public static void toConfirmIdentityInformation(Context mContext) {
        Intent intent = new Intent(mContext, ConfirmIdentityInformationActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 编辑身份证信息
     *
     * @param mContext
     */
    public static void toIdEdit(Context mContext) {
        Intent intent = new Intent(mContext, IdEditActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 已有身份证
     *
     * @param mContext
     */
    public static void toExistingIDCard(Context mContext) {
        Intent intent = new Intent(mContext, ExistingIDCardActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 查看已有身份证
     *
     * @param mContext
     */
    public static void toCheckTheIdentityCard(Context mContext) {
        Intent intent = new Intent(mContext, CheckTheIdentityCardActivity.class);
        mContext.startActivity(intent);
    }


    /**
     * 我的钱包
     *
     * @param mContext
     */
    public static void toMyPurse(Context mContext) {
        Intent intent = new Intent(mContext, MyPurseActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 交易明细
     *
     * @param mContext
     */
    public static void toTransactionRecord(Context mContext) {
        Intent intent = new Intent(mContext, TransactionRecordActivity.class);
        mContext.startActivity(intent);
    }


    /**
     * 集分宝
     *
     * @param mContext
     */
    public static void toIntegralTreasure(Context mContext) {
        Intent intent = new Intent(mContext, IntegralTreasureActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 设置保留奖券
     *
     * @param mContext
     * @param type             0 设置开启自动转入 1 编辑保留奖券
     * @param jfbAutoTime      后台设置 集分宝自动转入时间
     * @param limitExplainShow //限额说明  0不展示 1展示
     * @param limitExplainUrl  //限额说明H5链接地址
     * @param isTransferLimit  1不限制转入 2限制转入
     * @param jfbTransferLimit 集分宝转入限额
     */
    public static void toSetRetainedIntegra(Context mContext, int type, String jfbAutoTime, String limitExplainShow, String limitExplainUrl, String isTransferLimit, String jfbTransferLimit) {
        Intent intent = new Intent(mContext, SetRetainedIntegraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("jfbAutoTime", jfbAutoTime);
        bundle.putString("limitExplainShow", limitExplainShow);
        bundle.putString("limitExplainUrl", limitExplainUrl);
        bundle.putString("isTransferLimit", isTransferLimit);
        bundle.putString("jfbTransferLimit", jfbTransferLimit);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 自动转入详情
     *
     * @param mContext
     * @param jfbAutoTime      后台设置 集分宝自动转入时间
     * @param jfbHoldIntegral  自动转入 保留奖券数  单位：分
     * @param limitExplainShow //限额说明  0不展示 1展示
     * @param limitExplainUrl  //限额说明H5链接地址
     * @param isTransferLimit  1不限制转入 2限制转入
     * @param jfbTransferLimit 集分宝转入限额
     */
    public static void toSetIntegraNum(Context mContext, String jfbAutoTime, String jfbHoldIntegral, String limitExplainShow, String limitExplainUrl, String isTransferLimit, String jfbTransferLimit) {
        Intent intent = new Intent(mContext, SetIntegraNumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("jfbAutoTime", jfbAutoTime);
        bundle.putString("jfbHoldIntegral", jfbHoldIntegral);
        bundle.putString("limitExplainShow", limitExplainShow);
        bundle.putString("limitExplainUrl", limitExplainUrl);
        bundle.putString("isTransferLimit", isTransferLimit);
        bundle.putString("jfbTransferLimit", jfbTransferLimit);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 兑换明细
     *
     * @param mContext
     * @param TYPE     0 集分宝明细  1 兑换明细
     */
    public static void toIntegralRecord(Context mContext, int TYPE) {
        Intent intent;
        if (TYPE == 0) {
            intent = new Intent(mContext, IntegrationRecordActivity.class);
            mContext.startActivity(intent);
        } else if (TYPE == 1) {
            intent = new Intent(mContext, IntegralRecordActivity.class);
            mContext.startActivity(intent);
        }

    }

    /**
     * 转出
     *
     * @param jfbIntegral 集分宝奖券余额  单位分
     * @param mContext
     */
    public static void toTurnOut(Context mContext, String jfbIntegral) {
        Intent intent = new Intent(mContext, TurnOutIntegralActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("jfbIntegral", jfbIntegral);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 转出状态
     *
     * @param mData    转出状态 数据
     * @param mContext
     */
    public static void toTurnOutIntegralState(Context mContext, IntegralStatusEntity.Data mData) {
        Intent intent = new Intent(mContext, TurnOutIntegralStateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("IntegralStatusEntity", mData);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 转入
     *
     * @param integral          用户奖券余额  单位分
     * @param limitExplainShow  限额说明  0不展示 1展示
     * @param limitExplainUrl   限额说明H5链接地址
     * @param planGiveRed       预计发奖励时间（09-26）
     * @param isTransferLimit   1不限制转入 2限制转入
     * @param jfbTransferLimit  集分宝转入限额
     * @param remainJfbIntegral 剩余可转入奖券
     * @param mContext
     */
    public static void toShiftTo(Context mContext, String integral, String limitExplainShow, String limitExplainUrl, String planGiveRed, String isTransferLimit, String jfbTransferLimit, String remainJfbIntegral) {
        Intent intent = new Intent(mContext, ShiftToActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("integral", integral);
        bundle.putString("limitExplainShow", limitExplainShow);
        bundle.putString("limitExplainUrl", limitExplainUrl);
        bundle.putString("planGiveRed", planGiveRed);
        bundle.putString("isTransferLimit", isTransferLimit);
        bundle.putString("jfbTransferLimit", jfbTransferLimit);
        bundle.putString("remainJfbIntegral", remainJfbIntegral);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 转入状态
     *
     * @param mData    转入状态 数据
     * @param mContext
     */
    public static void toShiftToState(Context mContext, IntegralStatusEntity.Data mData) {
        Intent intent = new Intent(mContext, ShiftToStateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("IntegralStatusEntity", mData);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    /**
     * 兑换指数
     *
     * @param exchangeIndex 兑换指数  参照现在处理方式（万分之）
     * @param mContext
     */
    public static void toExchangeIndex(Context mContext, String exchangeIndex) {
        Intent intent = new Intent(mContext, ExchangeIndexActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("exchangeIndex", exchangeIndex);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 兑换指数记录
     *
     * @param mContext
     */
    public static void toExchangeIndexRecord(Context mContext) {
        Intent intent = new Intent(mContext, ExchangeIndexRecordActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 我的奖券
     *
     * @param mContext
     */
    public static void toMyIntegration(Context mContext) {
//        Intent intent = new Intent(mContext, MyIntegrationActivity.class);
//        mContext.startActivity(intent);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyActivity"));
        intent.putExtra("type", "1");
        mContext.startActivity(intent);
    }

    public static void toMyMoney(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyActivity"));
        intent.putExtra("type", "0");
        mContext.startActivity(intent);
    }

    /**
     * 赚取佣金
     *
     * @param mContext
     */
    public static void toMakeMoney(Context mContext) {
        Intent intent = new Intent(mContext, MakeMoneyActivity.class);
        mContext.startActivity(intent);
    }


    /**
     * 我的信用
     *
     * @param mContext
     */
    public static void toMyCredit(Context mContext) {
        Intent intent = new Intent(mContext, MyCreditActivity.class);
        mContext.startActivity(intent);
    }


    /**
     * 我的银行卡
     *
     * @param mContext
     */
    public static void toMyBankCards(Context mContext) {
        Intent intent = new Intent(mContext, MyBankCardsActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转选择提现银行卡页面
     */
    public static void toSetUpCashCard(Context context) {
        Intent intent = new Intent(context, SetUpCashCardsActivity.class);
        context.startActivity(intent);
    }

    /**
     * 个人银行卡详情
     */
    public static void toBankCardsDetail(Context mContext, String cardId) {
        Intent intent = new Intent(mContext, CardPrivateDetaiInfolActivity.class);
        intent.putExtra("card_id", cardId);
        mContext.startActivity(intent);
    }

    /**
     * 添加银行卡 -- 对公
     *
     * @param mContext
     */
    public static void toAddBankCardPublic(Context mContext) {
//        Intent intent = new Intent(mContext, AddPublicCardActivity.class);
        Intent intent = new Intent(mContext, BindPublicBindCardActivity.class);
        mContext.startActivity(intent);
    }


    /**
     * 好友抢果
     *
     * @param mContext
     */
    public static void toRob(Context mContext) {
        Intent intent = new Intent(mContext, HealthFriendRobActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 我的健康
     *
     * @param mContext
     */
    public static void toMyHealth(Context mContext) {
        Intent intent = new Intent(mContext, HealthHomeActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 我的健康果
     *
     * @param mContext
     */
    public static void toHealthFruit(Context mContext) {
        Intent intent = new Intent(mContext, HealthFruitActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 健康果-列表
     */
    public static void toHealthFruitList(Context mContext) {
        Intent intent = new Intent(mContext, HealthFruitListActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 我的家庭
     *
     * @param mContext
     */
    public static void toMyFamily(Context mContext) {
        Intent intent = new Intent(mContext, MyFamilyHealthActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 申请互助
     *
     * @param mContext
     */
    public static void toHelpEach(Context mContext, String id, String type, String tag, String idNum, String name, String birthday, String joinId, String title) {
        Intent intent = new Intent(mContext, HelpOtherActivity.class);
        intent.putExtra(Constants.HEALTH_HELP_OTHER_ID, id);
        intent.putExtra(Constants.HEALTH_HELP_OTHER_TYPE, type);
        intent.putExtra(Constants.HEALTH_HELP_OTHER_TAG, tag);
        intent.putExtra(Constants.HEALTH_HELP_OTHER_ID_NUM, idNum);
        intent.putExtra(Constants.HEALTH_HELP_OTHER_NAME, name);
        intent.putExtra(Constants.HEALTH_HELP_OTHER_BIRTHDAY, birthday);
        intent.putExtra(Constants.HEALTH_HELP_OTHER_JOIN_ID, joinId);
        intent.putExtra(Constants.HEALTH_HELP_OTHER_TITLE, title);
        mContext.startActivity(intent);
    }

    /**
     * 公益爱心捐赠列表
     *
     * @param mContext
     */
    public static void toWelfareList(Context mContext) {
        Intent intent = new Intent(mContext, WelfareDonationListActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 公益个人信息界面
     *
     * @param mContext
     */
    public static void toCareDonation(Context mContext, Intent intent) {
        mContext.startActivity(intent);
    }

    /**
     * 公益捐款成功
     *
     * @param mContext
     */
    public static void toWelfareLoveMsg(Context mContext) {
        Intent intent = new Intent(mContext, WelfareLoveMsgActivity.class);
        mContext.startActivity(intent);
    }


    /**
     * 公益如何获取经验值
     *
     * @param mContext
     */
    public static void toWelfareGetOperation(Context mContext) {
        Intent intent = new Intent(mContext, WelfareGetOperationActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到公益详情web页面
     *
     * @param context
     * @param intent
     */
    public static void toWelfareDetailsWebView(Context context, Intent intent) {
        context.startActivity(intent);
    }

    /**
     * 跳转公益爱心提交留言界面
     *
     * @param context
     */
    public static void toSubmitWelfareLoveMsg(Context context, String proId) {
        Intent intent = new Intent(context, WelfareSubMsgActivity.class);
        intent.putExtra("proId", proId);
        context.startActivity(intent);
    }


    /**
     * 两种卡
     *
     * @param mContext
     */
    public static void toSupportTwoBankCardType(Context mContext) {
        Intent intent = new Intent(mContext, TwoTypeCardActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转短信验证码界面
     *
     * @param mContext
     * @param phonenum
     */
    public static void toSMSCheck(Context mContext, String phonenum, int smsType) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.SmsCodeViewImplActivity"));
        intent.putExtra("phone", phonenum);
        intent.putExtra("smsType", smsType);
        mContext.startActivity(intent);
    }

    /**
     * 跳转实名认证界面
     *
     * @param mContext
     */
    public static void toCert(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.CertificationViewImplActivity"));
        mContext.startActivity(intent);
    }


}
