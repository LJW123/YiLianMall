package com.yilian.mall.contract;

/**
 * @author xiaofo on 2018/7/6.
 */

public class CashDeskContract {
    interface Model{
        void getUserBalance();
    }
    interface CashDeskPresenter {
        void getUserBalance();

    }
    interface View{

    }
}
