package com.leshan.ylyj.base;

import android.content.Context;

import rx.Observable;
import rxfamily.entity.MyPurseEntity;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class MyPurseBase {

    public interface IView {

        void showSuccessMsg(String s);

        void showErrorMsg(String s);

        void setData(MyPurseEntity myPurseEntity);
    }

    public interface IPresenter {

        void deliverList(String token, String device_id);
    }

    public interface IModel {

        Observable getPurseData();
    }
}
