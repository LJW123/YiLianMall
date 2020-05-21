package com.yilian.mall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.wxlib.util.SysUtil;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.vondear.rxtools.RxTool;
import com.yilian.luckypurchase.activity.LuckyWinPopWindowActivity;
import com.yilian.mall.ui.RedPacketDialog;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.LibApplication;
import com.yilian.mylibrary.ProviderImpl;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.RedPacketWhetherEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyApplication extends LibApplication {
    public static MyApplication instance = null;
    public static boolean info = false;
    public static AppHandler handler;
    public static SharedPreferences sp;

    private double Latitude = 0.0;
    private double Longitude = 0.0;
    private String city;
    private YWIMKit ywimKit;
    private int mActivityCount;
    private boolean isFirst = false;

    public static MyApplication getInstance() {
        return instance;
    }

    public static void sendMessage(android.os.Message msg) {
        handler.sendMessage(msg);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
        ProviderImpl.getInstance().init(this);
        com.yilian.mylibrary.PreferenceUtils.writeBoolConfig(Constants.RED_PACKET_FIRST, isFirst, getApplicationContext());
        //初始化科大讯飞
        SpeechUtility.createUtility(MyApplication.this, "appid=" + Constants.SPEAKE_APP_ID);
        Logger.i("2017年11月23日 17:32:56---走了onCreate");


//test merge to money
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivityCount++;
                //如果mActivityCount ==1，说明是从后台到前台
                if (mActivityCount == 1) {
//                    从后台回到了前台
                    showLuckyWindow();
//                    isShowRedWindow();
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mActivityCount--;
                //如果mActivityCount ==0，说明是前台到后台
                if (mActivityCount == 0) {
                    //说明从前台回到了后台
                    PreferenceUtils.writeBoolConfig(Constants.IS_CHECKED_UPDATE, true, getApplicationContext());
                    PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, false, getApplicationContext());

                    Logger.i("从前台到了后台 mActivityCount:" + mActivityCount);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
//                if (activity instanceof LeFenPhoneLoginActivity || activity instanceof V2_LoginActivity || activity instanceof V2_RegisterActivity) {
//                    checkShowRedWindow();
//                }
            }
        });

        long time1 = System.currentTimeMillis();
        instance = this;
        //初始化全局异常处理
        CrashHandler.getInstance().init(instance);
        sp = getSharedPreferences(Constants.SP_FILE, 0);
        initImageLoader(getApplicationContext());
//......X5内核...............................................................................
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onCoreInitFinished() {
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Logger.i("onViewInitFinished is " + b);
            }
        };
        QbSdk.initX5Environment(getApplicationContext(), cb);
//...........................................................................................
        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        }
//第一个参数是Application Context
//这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
        if (SysUtil.isMainProcess()) {
            YWAPI.init(this, Constants.ALI_APP_KEY);
        }
//.......................................................................................................

        //基于友盟的页面统计,禁止默认页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(true);
/**
 * 最简单的初始化
 * 获取APP ID并将以下代码复制到项目Application类onCreate()中，Bugly会为自动检测环境并完成配置：
 * 为了保证运营数据的准确性，建议不要在异步线程初始化Bugly。
 *
 第三个参数为SDK调试模式开关，调试模式的行为特性如下：

 输出详细的Bugly SDK的Log；
 每一条Crash都会被立即上报；
 自定义日志将会在Logcat中输出。
 建议在测试阶段建议设置成true，发布时设置为false。
 */
        //打包的时候用NONE
        if (BuildConfig.DEBUG) {
            Logger.init("YiLian").setLogLevel(LogLevel.FULL);
            CrashReport.initCrashReport(getApplicationContext(), "ca5b40bb7f", true);
        } else {
            Logger.init("YiLian").setLogLevel(LogLevel.NONE);
            CrashReport.initCrashReport(getApplicationContext(), "ca5b40bb7f", false);
        }
        long time2 = System.currentTimeMillis();
        Logger.i("启动时间：" + (time2 - time1));
    }

    private void showLuckyWindow() {
        Logger.i("从后台回到了前台：mActivityCount：" + mActivityCount);
        String luckyActivityId = PreferenceUtils.readStrConfig(Constants.APP_LUCKY_PRIZE_ACTIVITY_ID, getApplicationContext(), "");
        if (!TextUtils.isEmpty(luckyActivityId)) {
            Intent intent = new Intent(getApplicationContext(), LuckyWinPopWindowActivity.class);
            intent.putExtra("activityId", luckyActivityId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            AppManager.getInstance().getTopActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                //.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 当app来到后台检测是否弹出奖励弹窗
     */
    public void isShowRedWindow() {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                RetrofitUtils2 retrofitUtils2 = RetrofitUtils2.getInstance(getApplicationContext());
                if (retrofitUtils2 == null) {
                    return;
                }
                retrofitUtils2.whetherShowRedDialog(new Callback<RedPacketWhetherEntity>() {
                    @Override
                    public void onResponse(Call<RedPacketWhetherEntity> call, Response<RedPacketWhetherEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(getApplicationContext(), body)) {
                            if (CommonUtils.serivceReturnCode(getApplicationContext(), body.code, body.msg)) {
                                RedPacketWhetherEntity entity = response.body();
                                switch (entity.code) {
                                    case 1:
                                        boolean isShow;
                                        String redPacketId = "";
                                        switch (entity.isAlert) {
                                            case "0":
                                                isShow = false;
                                                break;
                                            case "1":
                                                isShow = true;
                                                redPacketId = entity.cashBonusId;
                                                startRedPacketActivity();
                                                break;
                                            default:
                                                isShow = false;
                                                break;
                                        }
                                        PreferenceUtils.writeBoolConfig(Constants.RED_PACKET_SHOW, isShow, getApplicationContext());
                                        PreferenceUtils.writeStrConfig(Constants.RED_PACKET_ID, redPacketId, getApplicationContext());
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RedPacketWhetherEntity> call, Throwable t) {
                    }
                });
            }
        });
    }

    private void startRedPacketActivity() {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
//                    沉睡5S 防止首次启动时奖励页面先于首页出现
                    Thread.sleep(5000);
                    Intent intent = new Intent(getApplicationContext(), RedPacketDialog.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 当app来到前台检测是否弹出每日奖励
     */
    public void checkShowRedWindow() {
        if (PreferenceUtils.readBoolConfig(Constants.RED_PACKET_SHOW, getApplicationContext(), false)) {
            startRedPacketActivity();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//解决方法超过65535时 分包打包5.0以下系统不支持的问题
    }

    public YWIMKit getYWIMKit() throws Exception {
        if (ywimKit == null) {
            String aliUserId = sp.getString(Constants.ALI_USER_ID, "");
            if (TextUtils.isEmpty(aliUserId)) {
                throw new Exception("ali id is null");
            }
            Logger.i("ali id :" + aliUserId);
            ywimKit = YWAPI.getIMKitInstance(aliUserId, Constants.ALI_APP_KEY);
            ywimKit.setShortcutBadger(0);

        }
        return ywimKit;
    }

    public void setYWIMKit(YWIMKit ywimKit) {
        this.ywimKit = ywimKit;

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        final double latitude = NumberFormat.convertToDouble(PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, this), 0d);
        return latitude;
    }

    public void setLatitude(double lat) {
        this.Latitude = lat;
    }

    public double getLongitude() {
        final double longitude = NumberFormat.convertToDouble(PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, this), 0d);
        return longitude;
    }

    public void setLongitude(double lng) {
        this.Longitude = lng;
    }

    public static class AppHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Logger.i("IntentService", "2017-1-11&&1111111");
                    break;
                case 1:
                    Logger.i("IntentService", "2017-1-11&&2222222");
                    break;
            }
        }
    }
}