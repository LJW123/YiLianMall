package com.leshan.ylyj.base;

import rx.Observable;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class HealthBase {

    public interface IModel {
        Observable getMyHealthTopMsg(String userId);

    }

    public interface IPresenter {
        public void getMyHeadTopMsg(String userId);
    }
}
