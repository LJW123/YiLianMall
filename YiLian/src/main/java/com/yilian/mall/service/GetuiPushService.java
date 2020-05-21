package com.yilian.mall.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.igexin.sdk.GTServiceManager;
import com.orhanobut.logger.Logger;

/**
 * 核心服务, 继承 android.app.Service, 必须实现以下几个接口, 并在 AndroidManifest 声明该服务并配置成
 * android:process=":pushservice", 具体参考
 * PushManager.getInstance().initialize(this.getApplicationContext(), userPushService), 其中
 * userPushService 为 用户自定义服务 即 GetuiPushService.
 */
public class GetuiPushService extends Service {

    public static final String TAG = GetuiPushService.class.getName();

    @Override
    public void onCreate() {
        // 该行日志在 release 版本去掉
        Logger.d(TAG+TAG + " call -> onCreate -------");
Logger.i("个推初始化走了这里");
        super.onCreate();
        GTServiceManager.getInstance().onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 该行日志在 release 版本去掉
        Logger.d(TAG+TAG + " call -> onStartCommand -------");

        super.onStartCommand(intent, flags, startId);
        Logger.d(TAG+"onStartCommand "+intent+" flags "+flags+" startId "+startId);
        return GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
//        // 该行日志在 release 版本去掉
        Logger.d(TAG+"onBind -------");
        return GTServiceManager.getInstance().onBind(intent);
    }

    @Override
    public void onDestroy() {
        // 该行日志在 release 版本去掉
        Logger.d(TAG+"onDestroy -------");

        super.onDestroy();
        GTServiceManager.getInstance().onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GTServiceManager.getInstance().onLowMemory();
    }
}
