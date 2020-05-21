package com.leshan.ylyj.model;

import android.content.Context;

import com.leshan.ylyj.basemodel.BaseModel;
import com.yilian.mylibrary.Ip;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.entity.AuthInfoEntity;
import rxfamily.entity.BankInfoEntity;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.BindPersonBankCardSuccessEntity;
import rxfamily.entity.BranchBankEntity;
import rxfamily.entity.CardPrivateDetailsInfo;
import rxfamily.entity.CardPublicDetailsInfo;
import rxfamily.entity.CheckBankCard4ElementEntity;
import rxfamily.entity.IntegralRegionEntity;
import rxfamily.entity.MyPurseEntity;
import rxfamily.entity.PublicCardDetailsEntity;
import rxfamily.entity.PublicCardDetailsEntityV2;
import rxfamily.entity.TransactionRecordEntity;
import rxfamily.entity.TwoTypeEntity;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class BankModel extends BaseModel {

    public BankModel(Context mContex) {
        super(mContex);
    }


    /**
     * 交易记录
     *
     * @param page  页
     * @param count 每页个数
     * @param date  时间筛选
     */
    public Observable<TransactionRecordEntity> getTransactionRecordPresenter(int page, int count, String date) {
        Observable<TransactionRecordEntity> observable = service.getTransactionRecordPresenter("jfb/trade_record", String.valueOf(page), String.valueOf(count), date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 验证银行卡号
     *
     * @param card
     * @return
     */
    public Observable<BankInfoEntity> addBankCards(String card) {
        Observable<BankInfoEntity> observable = service.addBankInfo("userAuth/bank_card_info", card)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 绑定私有银行卡
     *
     * @param bankCardNumber
     * @param phone
     * @param provinceId
     * @param cityId
     * @param service_phone  客服电话 根据卡号查询银行卡信息接口返回的字段
     * @param branchBankName
     * @return
     */
    public Observable<BindPersonBankCardSuccessEntity> bindPrivaceCard(String bankCardNumber, String phone, String provinceId, String cityId,
                                                                       String service_phone, String branchBankName, String smsCode) {
        Observable<BindPersonBankCardSuccessEntity> observable = service.addPersonalBankCardsInfo("userBank/bind_private_card", bankCardNumber,
                phone, provinceId, cityId, service_phone, branchBankName, smsCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<BindPersonBankCardSuccessEntity> bindPrivaceCard(Map<String, String> map) {
        Observable<BindPersonBankCardSuccessEntity> observable = service.addPersonalBankCardsInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;

    }

    /**
     * 银行卡4元素验证
     *
     * @param cardNumber
     * @param phone
     * @return
     */
    public Observable<CheckBankCard4ElementEntity> checkFourState(String cardNumber, String phone) {
        Observable<CheckBankCard4ElementEntity> observable = service.addBankCardsInfo("userBank/verify_card_by_four", cardNumber, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 验证短信验证码
     *
     * @param phone
     * @param SMS_code
     * @return
     */
    public Observable<BaseEntity> checkCode(String phone, String SMS_code) {
        Observable<BaseEntity> observable = service.checkCode("userBank/verify_SMS_code", phone, SMS_code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param verify_type
     * @param type
     * @param voice
     * @param verify_code
     * @return
     */
    public Observable<BaseEntity> getSMS(String phone, String verify_type, String type, String voice, String verify_code) {
        Observable<BaseEntity> observable = service.sendSMS("userBank/get_SMS_code_v2", phone, verify_type, type, voice, verify_code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 获取对公银行卡详情
     *
     * @param card_index
     * @return
     */
    public Observable<PublicCardDetailsEntityV2> getPublicDetails(String card_index) {
        Observable<PublicCardDetailsEntityV2> observable = service.getPublicDetailsInfo("userBank/public_card_info", card_index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 两种类型银行卡
     *
     * @param type
     * @return
     */
    public Observable<TwoTypeEntity> getCardList(String type) {
        Observable<TwoTypeEntity> observable = service.getSupportCards("userBank/support_bank_list", type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 钱包首页
     *
     * @return
     */
    public Observable<MyPurseEntity> getPurseData() {
        Observable<MyPurseEntity> observable = service.getMyPurseInfo("jfb/my_wallet")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 解绑银行卡
     *
     * @param card_index
     * @return
     */
    public Observable<BaseEntity> unBindingCards(String card_index) {
        Observable<BaseEntity> observable = service.unBundlingCard("userBank/unbind_user_card", card_index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 綁定對公卡
     *
     * @param card
     * @param bank_name
     * @param obligate_phone
     * @param branch_bank
     * @param card_holder
     * @param tax_code
     * @param province
     * @param city
     * @param company_address
     * @param cert_image
     * @param sms_code
     * @return
     */
    public Observable<BaseEntity> bindPublicCards(String card, String bank_name, String obligate_phone, String branch_bank, String card_holder, String tax_code, String province, String city, String company_address, String cert_image, String sms_code, String cardIndex) {
        Observable<BaseEntity> observable = service.addPublicBankCardsInfo("userBank/bind_public_card", card, bank_name, obligate_phone, branch_bank, card_holder, tax_code, province, city, company_address, cert_image, sms_code, cardIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 获取银行卡详情
     *
     * @param card_index
     * @return
     */
    public Observable<PublicCardDetailsEntity> getPersonalDetails(String card_index) {
        Observable<PublicCardDetailsEntity> observable = service.getMyPersonalDetailsInfo("userBank/private_card_info", card_index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 获取私有银行卡详情信息
     *
     * @param context
     * @param cardIndex
     * @return
     */
    public Observable<CardPrivateDetailsInfo> getPrivateDetailsInfo(Context context, String cardIndex) {
        String url = Ip.getURL(context) + "mall.php?c=userBank/private_card_info";
        Observable observable = service.getPrivateCardDetailsInfo(url, cardIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 解除银行卡
     *
     * @param context
     * @param cardIndex
     * @return
     */
    public Observable<BaseEntity> bankCardRelieved(Context context, String cardIndex) {
        String url = Ip.getURL(context) + "mall.php?c=userBank/unbind_user_card";
        Observable observable = service.bankCardRelieved(url, cardIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 获取公卡信息
     *
     * @param context
     * @param cardIndex
     * @return
     */
    public Observable<CardPublicDetailsInfo> getCardPublicDetailsInfo(Context context, String cardIndex) {
        String url = Ip.getURL(context) + "mall.php?c=userBank/public_card_info";
        Observable observable = service.getPublicCardDetailsInfo(url, cardIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 获取省市县
     *
     * @return
     */
    public Observable<IntegralRegionEntity> getRegion() {
        return service.getRegion("getRegion")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取支行信息
     *
     * @return
     */
    public Observable<BranchBankEntity> getBranchBankName(String provinceId, String cityId, String bankId, String keyword) {
        return service.getBranchBankName("get_branch_bank_list", provinceId, cityId, keyword, bankId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CheckBankCard4ElementEntity> checkBankCard4Element(String bankCardNumber, String userName, String idCardNumber, String phone) {
        return service.checkBankCard4Element("userAuth/verify_bank_card4", bankCardNumber, userName, idCardNumber, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取实名认证信息
     *
     * @return
     */
    public Observable<AuthInfoEntity> getAuthInfo() {
        return service.getAuthInfo("get_auth_info")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

}