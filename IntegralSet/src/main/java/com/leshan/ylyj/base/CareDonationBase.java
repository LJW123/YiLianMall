package com.leshan.ylyj.base;

import android.content.Context;

import com.leshan.ylyj.bean.TreasureBean;

import rx.Observable;


/**
 * Created by @author zhaiyaohua on 2018/1/11 0011.
 */

public class CareDonationBase {
    public interface  IView{
        void showSuccessMsg(String s);

        void showErrorMsg(String s);

        void setData(TreasureBean treasureBean);
    }
    public interface IPresenter {

        void deliverList(Context context, String token);
    }
    public interface IModel {

        Observable getData(Context context, String token);
    }

}
