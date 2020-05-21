package com.yilian.mall.suning.activity.goodsdetail.addressclean;

import com.yilian.mall.clean.AbstractInteractor;
import com.yilian.mall.clean.Executor;
import com.yilian.mall.clean.MainThread;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author xiaofo on 2018/7/18.
 */

public class SnDefaultShippingAddressInfoInteractorImpl extends AbstractInteractor implements ISnDefaultShippingAddressInfoInteractor {

    private final Callback mCallback;
    private final ISnDefaultShippingAddressInfoRepository mSnDefaultShippingAddressInfoRepository;

    public SnDefaultShippingAddressInfoInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                                      Callback callback,
                                                      ISnDefaultShippingAddressInfoRepository iSnDefaultShippingAddressInfoRepository) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mSnDefaultShippingAddressInfoRepository = iSnDefaultShippingAddressInfoRepository;
    }

    @Override
    public void run() {

        mSnDefaultShippingAddressInfoRepository.getSnDefaultShippingAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SnShippingAddressInfoEntity>() {
                    @Override
                    public void call(SnShippingAddressInfoEntity snShippingAddressInfoEntity) {
                        mMainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.getShippingAddressSuccess(snShippingAddressInfoEntity);
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mMainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.getShippingAddressError(throwable.getMessage());
                            }
                        });
                    }
                });
    }
}
