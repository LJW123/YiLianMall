package com.yilian.mall.jd.presenter.impl;

import android.content.Context;

import com.yilian.mall.jd.presenter.JDCheckAreaSupportSellPresenter;
import com.yilian.networkingmodule.entity.jd.JDIsAreaRestrict;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/5/29.
 */

public class JDCheckAreaSupportSellPresenterImpl implements JDCheckAreaSupportSellPresenter {
    private View view;

    public JDCheckAreaSupportSellPresenterImpl(View view) {
        this.view = view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription checkAreaSupportSell(Context context, String skuIds, String provinceId, String cityId, String countyId, String townId) {
        return RetrofitUtils3.getRetrofitService(context)
                .checkJDAreaSupportSell("jd_goods/jd_checkarealimit", skuIds, provinceId, cityId, countyId, townId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDIsAreaRestrict>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDIsAreaRestrict o) {
//                        目前只针对1个sku进行查询 所以根据查询结果第一条进行判断当前区域是否限制销售
                        List<JDIsAreaRestrict.DataBean> data = o.data;
                        if (data != null) {
                            if (data.size() > 0) {
                                JDIsAreaRestrict.DataBean dataBean = data.get(0);
                                if (dataBean.skuId.equals(skuIds)) {
                                    if (dataBean.isAreaRestrict) {
//                                        限制
                                        view.unSupportAreaSell();
                                    } else {
//                                        不限制
                                        view.supportAreaSell();
                                    }
                                }else{
//                                    状态查询异常
                                    view.unSupportAreaSell();
                                }
                            }else{
//                                    状态查询异常
                                view.unSupportAreaSell();
                            }
                        }else{
//                                    状态查询异常
                            view.unSupportAreaSell();
                        }
                    }
                });
    }
}
