package com.leshan.ylyj.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.leshan.ylyj.base.BasePresenter;
import com.leshan.ylyj.base.BaseView;
import com.leshan.ylyj.model.HealthModel;

import rx.Subscription;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class HealthPresenter extends BasePresenter {

    private HealthModel model;
    private Subscription subscription;

    public HealthPresenter(Context context, BaseView view, @Nullable int flag) {
        attachView(view);
        model = new HealthModel(context);
        mFlag = flag;
    }

    /**
     * 我的健康
     */
    public void getMyHealthTopMsg() {
        subscription = model.getMyHealthTopMsg(observer);
        addSubscription(subscription);
    }

    public void getMyHealList(String page, String count) {
        subscription = model.getMyHealList(page, count, observer);
        addSubscription(subscription);
    }

    public void getWeather(String location) {
        subscription = model.getWeahter(location, observer);
        addSubscription(subscription);
    }

    /**
     *健康果
     */
    public void getHealFruitCount() {
        subscription = model.getHealthFruitCount(observer);
        addSubscription(subscription);
    }

    public void getHealFruitList(String page, String count) {
        subscription = model.getHealthFruitList(page, count, observer);
        addSubscription(subscription);
    }

    public void exchangeFruit(String apples) {
        subscription = model.exchangeFruit(apples, observer);
        addSubscription(subscription);
    }

    /**
     *抢果
     */
    public void getFriendRobAllCount() {
        subscription = model.getFriednRobAllCount(observer);
        addSubscription(subscription);
    }

    public void getFriendRobList(String page, String count) {
        subscription = model.getFriednRobList(page, count, observer);
        addSubscription(subscription);
    }

    public void healRobFriend(String friendId, String count) {
        subscription = model.healthRobFriend(friendId, count, observer);
        addSubscription(subscription);
    }

    /**
     * 互助计划
     */
    public void getHelpOtherDetail(String projectId) {
        subscription = model.helpOtherDetail(projectId, observer);
        addSubscription(subscription);
    }

    public void getHelpOtherFind(String name) {
        subscription = model.helpOtherFindBAI(name, observer);
        addSubscription(subscription);
    }

    public void getHelpOtherJoin(String projectId, String applesNum, String tagId, String name, String idNum, String birthday, String joinId) {
        subscription = model.helpOtherJoin(projectId, applesNum, tagId, name, idNum, birthday, joinId, observer);
        addSubscription(subscription);
    }

    /**
     * 我的家庭
     */
    public void getMyFamilyList() {
        subscription = model.myFamilyList(observer);
        addSubscription(subscription);
    }

}
