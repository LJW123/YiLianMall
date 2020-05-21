package com.leshan.ylyj.model;

import android.content.Context;

import com.leshan.ylyj.basemodel.BaseModel;
import com.leshan.ylyj.baseurl.BaseUrl;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.WelfareExpericeEntity;
import rxfamily.entity.WelfareGrowthSystem;
import rxfamily.entity.WelfareListEntity;
import rxfamily.entity.WelfareLoveEntity;
import rxfamily.entity.WelfareLoveOperationEntity;
import rxfamily.entity.WelfarePersonInfoEntity;
import rxfamily.entity.WelfareShareDataEntivity;

/**
 * @author zhaiyaohua on 2018/1/11 0011.
 * @net 根据业务创建网络请求model，必须继承BaseModel
 * BaseModel中封装了网络请求的工具
 */

public class WelfareModel extends BaseModel {

    public WelfareModel(Context mContex) {
        super(mContex);
    }

    public WelfareModel(Context mContext, boolean has_cache) {
        super(mContext, has_cache);
    }

    /**
     * 获取公益列表
     *
     * @return
     */
    public Observable getWefareList(int count, int pager) {
        String url = BaseUrl.BASE_URL_1 + "v1/publicWelfareProject/publicWelfareProjectList.do";
        Observable<WelfareListEntity> observable = service.getWelfareList(url, count, pager)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * @return
     * @net 获取公益爱心数量
     */
    public Observable getWefareLoveNum() {
        String url = BaseUrl.BASE_URL_1 + "v1/personalDetails/totalDonation.do";
        Observable<WelfareLoveEntity> observable = service.getWelfareLoveNum(url) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 获取公益个人信息
     *
     * @return
     */
    public Observable<WelfarePersonInfoEntity> getWelfarePersonalInfo() {
        String url = BaseUrl.BASE_URL_1 + "v1/personalDetails/getAchievement.do";
        Observable observable = service.getWelfarePersonalInfo(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 获取公益经验值
     *
     * @return
     */
    public Observable<WelfareExpericeEntity> getWelfareExperience() {
        String url = BaseUrl.BASE_URL_1 + "v1/personalDetails/findUserInfo.do";
        Observable observable = service.getWelfareExperience(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 获取成长体系
     */
    public Observable<WelfareGrowthSystem> getGrowthSystem() {
        String url = BaseUrl.BASE_URL_1 + "v1/personalDetails/findGrowthSystem.do";
        Observable observable = service.getWelfareGrowthSystem(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 如何获取公益经验值
     *
     * @return
     */
    public Observable<WelfareLoveOperationEntity> getWelfareOpero() {
        String url = BaseUrl.BASE_URL_1 + "v1/personalDetails/getLoveExperienceMethod.do";
        Observable observable = service.getWelfareOper(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 提交公益爱心留言
     *
     * @param proId
     * @param content
     * @return
     */
    public Observable<BaseEntity> submitWelfareLoveMsg(String proId, String recordId,String content) {
        String url = BaseUrl.BASE_URL_1 + "v1/publicWelfareProject/addPublicWelfareProjectPomment.do";
        Observable observable = service.submitWelfareLoveMsg(url, recordId,proId, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 获取公益分享数据
     *
     * @param proId
     * @return
     */
    public Observable<WelfareShareDataEntivity> getWelfareShareData(String proId) {
        String url = BaseUrl.BASE_URL_1 + "v1/publicWelfareProject/findProjectInfo.do";
        Observable observable = service.getWelfareShareData(url, proId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 公益分享成功调用
     *
     * @return
     */
    public Observable<BaseEntity> postWelfareShareResult(Map<String, String> map) {
        String url = BaseUrl.BASE_URL_1 + "v1/publicWelfareProject/publicWelfareProjectsShare.do";
        Observable observable = service.getWelfareShareResult(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }
}
