package com.yilian.mylibrary;

import android.content.Context;

/**
 * @author xiaofo on 2018/10/26.
 */

public class ProviderImpl implements IProvider {
    private static volatile ProviderImpl mInstance = null;
    private Context context;

    private ProviderImpl() {
    }

    public static ProviderImpl getInstance() {
        if (mInstance == null) {
            synchronized (ProviderImpl.class) {
                if (mInstance == null) {
                    mInstance = new ProviderImpl();
                }
            }
        }

        return mInstance;
    }

    public void init(Context context) {

        this.context = context;
    }

    @Override
    public String getApplicationId() {
        if (null == context) {
            throw new IllegalArgumentException("ProviderImpl not init");
        }
        return context.getPackageName();
    }
}
