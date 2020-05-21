package com.yilian.mylibrary;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * @author Created by  on 2018/1/23.
 * {@link ShowAreaUtil#showArea(ArrayList, ArrayList, ArrayList, Context, IShowAreaUtil)}
 */

public interface IShowAreaUtil<T> {
    void selectArea(T t, @Nullable T t1, @Nullable T t2);
}
