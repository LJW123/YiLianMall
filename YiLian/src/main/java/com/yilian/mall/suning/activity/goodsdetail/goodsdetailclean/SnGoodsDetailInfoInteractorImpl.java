package com.yilian.mall.suning.activity.goodsdetail.goodsdetailclean;

import com.yilian.mall.clean.AbstractInteractor;
import com.yilian.mall.clean.Executor;
import com.yilian.mall.clean.MainThread;
import com.yilian.networkingmodule.entity.suning.SnGoodsDetailInfoEntity;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/5/23.
 */

public class SnGoodsDetailInfoInteractorImpl extends AbstractInteractor implements SnGoodsDetailInfoInteractor {
    private Callback callback;
    private SnGoodsDetailInfoRepository jdGoodsDetailRepository;

    public SnGoodsDetailInfoInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                           Callback callback, SnGoodsDetailInfoRepository jdGoodsDetailRepository) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.jdGoodsDetailRepository = jdGoodsDetailRepository;
    }

    @Override
    public void run() {
        jdGoodsDetailRepository.getSnGoodsDetailInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SnGoodsDetailInfoEntity>() {
                    @Override
                    public void call(SnGoodsDetailInfoEntity jdGoodsDetailInfoEntity) {
                        mMainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.successCallback(jdGoodsDetailInfoEntity);
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mMainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.failedCallback("获取商品异常");
                            }
                        });
                    }
                });

    }
}
