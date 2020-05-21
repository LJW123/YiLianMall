package com.yilian.mall.clean;

import android.content.Context;

/**
 * @author xiaofo on 2018/7/18.
 */

public abstract class BaseRepository {

    public final Context mContext;

    public BaseRepository(Context context) {
        mContext = context;
    }
}
