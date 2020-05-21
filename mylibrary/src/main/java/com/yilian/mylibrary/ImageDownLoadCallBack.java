package com.yilian.mylibrary;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by  on 2017/6/15 0015.
 */

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);
    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
