package com.leshan.ylyj.model;

import android.content.Context;

import com.leshan.ylyj.basemodel.BaseModel;
import com.leshan.ylyj.baseurl.BaseUrl;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class HealthModel extends BaseModel {

    public HealthModel(Context mContex) {
        super(mContex);
    }

    /**
     * 健康主页
     *
     * @return
     */
    public Subscription getMyHealthTopMsg(Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/userHealthyApple/massage.do";
        return service.getMyHealthTopMsg(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Subscription getMyHealList(String page, String count, Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/userHealthyApple/projectotal.do";
        return service.getMyHealthList(url, page, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Subscription getWeahter(String location, Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/weather.do";
        return service.getWeather(url, location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 健康果
     *
     * @return
     */
    public Subscription getHealthFruitCount( Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/happyapple/HealthyAppleNum.do";
        return service.getHealthFruitCount(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Subscription getHealthFruitList(String page, String count, Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/happyapple/changelist.do";
        return service.getHealthFruitList(url, page, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Subscription exchangeFruit(String apples, Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/happyapple/tradeHappyApple.do";
        return service.exchangeFruit(url, apples)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 好友抢果
     */
    public Subscription getFriednRobAllCount(Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/friendhappyapple/findlootApple.do";
        return service.getFriendRobAllCount(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Subscription getFriednRobList(String page, String count, Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/friendhappyapple/dynamicList.do";
        return service.getFriendRobList(url, page, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Subscription healthRobFriend(String friendId, String count, Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/friendhappyapple/lootApple.do";
        return service.healthRobFriend(url, friendId, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 互助计划
     */
    public Subscription helpOtherDetail(String projectId, Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/helpplan/projectdetail.do";
        return service.helpOtherDetail(url, projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Subscription helpOtherFindBAI(String name, Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/helpplan/findUserBirthdayIdCard.do";
        return service.helpOtherFindBAI(url, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Subscription helpOtherJoin(String projectId, String applesNum, String tagId, String name, String idNum, String birthday, String joinId, Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/helpplan/joinProject.do";
        return service.helpOtherJoin(url, projectId, applesNum, tagId, name, idNum, birthday, joinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 我的家庭
     */
    public Subscription myFamilyList(Observer observer) {
        String url = BaseUrl.BASE_URL_1 + "v1/ha/myhomeplan/list.do";
        return service.getMyFamilyList(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
