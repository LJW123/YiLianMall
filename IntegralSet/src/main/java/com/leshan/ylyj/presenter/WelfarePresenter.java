package com.leshan.ylyj.presenter;

import android.content.Context;

import com.leshan.ylyj.base.BaseView;
import com.leshan.ylyj.model.WelfareModel;

import com.leshan.ylyj.base.BasePresenter;

import java.util.Map;

import rx.Subscription;

/**
 *@net 公益模块present
 * Created by @author zhaiyaohua on 2018/1/11 0011.
 */

public class WelfarePresenter extends BasePresenter {
    private WelfareModel modle;

    /**
     * presenter关联具体的modle
     *
     * @param context
     */
    public WelfarePresenter(Context context, BaseView view) {
        attachView(view);
        modle = new WelfareModel(context);
    }

    /**
     *
     * @param context
     * @param view
     */

    public WelfarePresenter(Context context, BaseView view,int flag) {
        attachView(view);
        this.mFlag=flag;
        modle = new WelfareModel(context);
    }

    /**
     * @net
     * 获取公益的爱心数量
     * 执行网络请求并订阅观察者observer
     *
     */
    public void getWelfareLoveNum() {
        Subscription subscription=modle.getWefareLoveNum().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 获取公益列表
     *
     */
    public void getWelfareList(int count,int pager) {
        Subscription subscription = modle.getWefareList(count,pager).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 获取公益成长体系
     */
    public void getWelfareGrowthSystem() {
        addSubscription(modle.getGrowthSystem().subscribe(observer));
    }

    /**
     * 获取公益个人详情
     */
    public void getWelfarePersonalInfo() {
        addSubscription(modle.getWelfarePersonalInfo().subscribe(observer));
    }

    /**
     * 获取公益经验值
     */
    public void getWelfareExperice(){
        addSubscription(modle.getWelfareExperience().subscribe(observer));
    }

    /**
     * 如何获取公益爱心经验值
     */
    public void getWelfareOperation(){
        addSubscription(modle.getWelfareOpero().subscribe(observer));
    }

    /**
     * 提交公益爱心留言
     * @param proId
     * @param content
     */
    public void submitWefareLoveMsg(String proId,String recordId,String content){
        addSubscription(modle.submitWelfareLoveMsg(proId,recordId,content).subscribe(observer));
    }

    /**
     * 获取公益分享信息
     * @param proId
     */
    public void getWelfareShareData(String proId){
        addSubscription(modle.getWelfareShareData(proId).subscribe(observer));
    }
    /**
     * 公益分享成功调用
     */
    public void postWelfareShareResult(Map<String,String> map){
        addSubscription(modle.postWelfareShareResult(map).subscribe(observer));
    }
}
