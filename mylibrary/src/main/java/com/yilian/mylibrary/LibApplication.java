package com.yilian.mylibrary;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ray_L_Pain on 2017/8/3 0003.
 */

public class LibApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //获取Context
        context = getApplicationContext();
    }

    //返回
    public static Context getContextObject(){
        return context;
    }
}
