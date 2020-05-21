package com.leshan.ylyj.base;

import android.content.Context;

import rx.Observable;
import rxfamily.entity.MyCardsEntity;
import rxfamily.entity.MyPurseEntity;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class MyBankCardsBase  {

    public interface IView {

        void showSuccessMsg(String s);

        void showErrorMsg(String s);

        void setCardsData(MyCardsEntity myCardsEntity);
    }

    public interface IPresenter {

        void deliverCardsList(String token, String device_id);
    }

    public interface IModel {

        Observable getCardsData();
    }

}
