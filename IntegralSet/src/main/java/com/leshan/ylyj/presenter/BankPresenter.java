package com.leshan.ylyj.presenter;


import android.content.Context;

import com.leshan.ylyj.base.BasePresenter;
import com.leshan.ylyj.base.BaseView;
import com.leshan.ylyj.model.BankModel;

import java.util.Map;

import rx.Subscription;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class BankPresenter extends BasePresenter {

    private BankModel mModel;


    public BankPresenter(Context context, BaseView view) {
        attachView(view);
        mModel = new BankModel(context);
    }

    public BankPresenter(Context context, BaseView view, int flag) {
        this.mFlag = flag;
        attachView(view);
        mModel = new BankModel(context);
    }

    public void getPurse() {
        Subscription subscription = mModel.getPurseData().subscribe(observer);
        addSubscription(subscription);
    }

    public void getTransactionRecordPresenter(int page, int count, String date) {
        Subscription subscription = mModel.getTransactionRecordPresenter(page, count, date).subscribe(observer);
        addSubscription(subscription);
    }

    public void checkCard(String cardnum) {
        Subscription subscription = mModel.addBankCards(cardnum).subscribe(observer);
        addSubscription(subscription);
    }

    public void checkFourState(String cardNumber, String phone) {
        Subscription subscription = mModel.checkFourState(cardNumber, phone).subscribe(observer);
        addSubscription(subscription);
    }

    public void getSMS(String phone, String verify_type, String type, String voice, String verify_code) {
        Subscription subscription = mModel.getSMS(phone, verify_type, type, voice, verify_code).subscribe(observer);
        addSubscription(subscription);
    }

    public void checkCode(String phone, String sms) {
        Subscription subscription = mModel.checkCode(phone, sms).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 绑定私有卡
     *
     * @param cardNumber
     * @param phone
     * @param provinceId
     * @param cityId
     * @param servicePhone   客服电话 根据卡号查询银行卡信息接口返回的字段
     * @param branchBankName
     */
    public void bindPrivaceCard(String cardNumber, String phone, String provinceId, String cityId, String servicePhone,
                                String branchBankName, String smsCode) {
        Subscription subscription = mModel.bindPrivaceCard(cardNumber, phone, provinceId,
                cityId, servicePhone, branchBankName, smsCode).subscribe(observer);
        addSubscription(subscription);
    }

    public void bindPrivaceCard(Map<String, String> map) {
        Subscription subscription = mModel.bindPrivaceCard(map).subscribe(observer);
        addSubscription(subscription);
    }

    public void getTwoTypeList(String type) {
        Subscription subscription = mModel.getCardList(type).subscribe(observer);
        addSubscription(subscription);
    }

    public void getPublicCardsDetail(String card_index) {
        Subscription subscription = mModel.getPublicDetails(card_index).subscribe(observer);
        addSubscription(subscription);
    }

    public void unBindingCards(String card_index) {
        Subscription subscription = mModel.unBindingCards(card_index).subscribe(observer);
        addSubscription(subscription);
    }

    public void bindPublicCards(String card, String bank_name, String obligate_phone, String branch_bank, String card_holder, String tax_code, String province, String city, String company_address, String cert_image, String sms_code,String cardIndex) {
        Subscription subscription = mModel.bindPublicCards(card, bank_name, obligate_phone, branch_bank, card_holder, tax_code, province, city, company_address, cert_image, sms_code,cardIndex).subscribe(observer);
        addSubscription(subscription);
    }


    public void getPersonalDetails(String card_index) {
        Subscription subscription = mModel.getPersonalDetails(card_index).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 获取私人银行卡信息
     * @param context
     * @param cardIndex
     */
    public void getPrivateCardDetailsInfo(Context context,String cardIndex){
        Subscription subscriber=mModel.getPrivateDetailsInfo(context,cardIndex).subscribe(observer);
        addSubscription(subscriber);
    }

    /**
     * 解除银行卡
     * @param context
     * @param cardIndex
     */
    public void bankCardRelieved(Context context,String cardIndex){
        Subscription subscription=mModel.bankCardRelieved(context,cardIndex).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 获取公卡信息
     * @param context
     * @param cardIndex
     */
    public void getCardPublicDetailsInfo(Context context, String cardIndex){
        Subscription subscription=mModel.getCardPublicDetailsInfo(context,cardIndex).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 获取省市县
     */
    public void getRegion() {
        Subscription subscription = mModel.getRegion().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 获取支行信息
     */
    public void getBranchBankNameList(String provinceId, String cityId, String bankId, String keyword) {
        Subscription subscription = mModel.getBranchBankName(provinceId, cityId, bankId, keyword).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 检查银行卡四元素
     *
     * @param bankCardNumber
     * @param userName
     * @param idCardNumber
     * @param phone
     */
    public void checkBankCard4Element(String bankCardNumber, String userName, String idCardNumber, String phone) {
        Subscription subscription = mModel.checkBankCard4Element(bankCardNumber, userName, idCardNumber, phone).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 获取实名认证信息
     *
     * @return
     */
    public void getAuthInfo() {
        Subscription subscription = mModel.getAuthInfo().subscribe(observer);
        addSubscription(subscription);
    }
}
