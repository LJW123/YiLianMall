package com.yilian.mylibrary;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * @author Created by  on 2017/12/15.
 */

public class FontUtils {
    /**
     * 获取字体样式
     *
     * @param context
     * @param fontFileName
     * @return
     */
    public static Typeface getFontTypeface(Context context, String fontFileName) {
        try {
            AssetManager assets = context.getAssets();
            Typeface fromAsset = Typeface.createFromAsset(assets, fontFileName);
            return fromAsset;
        } catch (Exception exception) {
            return Typeface.DEFAULT;
        }
    }
}
