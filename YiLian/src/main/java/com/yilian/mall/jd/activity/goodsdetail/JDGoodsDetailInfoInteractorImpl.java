package com.yilian.mall.jd.activity.goodsdetail;

import com.yilian.mall.clean.AbstractInteractor;
import com.yilian.mall.clean.Executor;
import com.yilian.mall.clean.MainThread;
import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/5/23.
 */

public class JDGoodsDetailInfoInteractorImpl extends AbstractInteractor implements JDGoodsDetailInfoInteractor {
    private Callback callback;
    private JDGoodsDetailInfoRepository jdGoodsDetailRepository;

    public JDGoodsDetailInfoInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                           Callback callback, JDGoodsDetailInfoRepository jdGoodsDetailRepository) {
        super(threadExecutor, mainThread);
        this.callback = callback;
        this.jdGoodsDetailRepository = jdGoodsDetailRepository;
    }

    @Override
    public void run() {
        jdGoodsDetailRepository.getJDGoodsDetailInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JDGoodsDetailInfoEntity>() {
                    @Override
                    public void call(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity) {
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
