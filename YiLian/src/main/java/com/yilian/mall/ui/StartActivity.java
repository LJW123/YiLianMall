package com.yilian.mall.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.ADEntity;
import com.yilian.mall.entity.JPLevel1CategoryEntity;
import com.yilian.mall.entity.LanguagePackageEntity;
import com.yilian.mall.entity.PushMsg;
import com.yilian.mall.entity.RegisterDevice;
import com.yilian.mall.entity.Start_app;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.service.GetuiPushService;
import com.yilian.mall.service.PushIntentService;
import com.yilian.mall.utils.FileUtils;
import com.yilian.mall.utils.LanguagePackageUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.SharedPreferencesUtils;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ThreadUtil;
import com.yilianmall.merchant.activity.MerchantAfterSaleDetailsActivity;
import com.yilianmall.merchant.activity.MerchantCenterActivity;
import com.yilianmall.merchant.activity.MerchantComboManageActivity;
import com.yilianmall.merchant.activity.MerchantOrderDetailsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * StartActivity逻辑
 * 初始化配置：检查权限
 * 没有则请求权限
 * 有权限则 跳转页面和注册设备E
 * 注册设备时 调用startAPP接口获取盐值
 */
public class StartActivity extends BaseActivity {
    public static final int HAVEAD = 1;
    public static final int NOAD = -1;
    private static final int REQUEST_PERMISSION_CODE = 0;
    DisplayMetrics dm;

    AccountNetRequest netRequset;
    private JPNetRequest jpNetRequest;
    private MallNetRequest request;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HAVEAD:
                    sp.edit().putBoolean(Constants.SPKEY_HAVEAD, true).commit();
                    break;
                case NOAD:
                    sp.edit().putBoolean(Constants.SPKEY_HAVEAD, false).commit();
            }

        }
    };
    private ArrayList<JPLevel1CategoryEntity.ListBean> jpLevel1CategoryList;
    private JPLevel1CategoryEntity.ListBean[] jpLevel1CategoryArray;
    private boolean readPhonePermission;
    private boolean writeExternalPermission;

    /**
     * 友盟数据统计的集成测试
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, "permission.READ_PHONE_STATE")) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }

            Logger.i("友盟的集成测试  deviceId " + device_id.toString() + " mac  " + mac.toString());
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //益联商家中心通知 展示
        PreferenceUtils.writeBoolConfig(Constants.KEY_MERCHANT_NOTICE, true, getApplicationContext());
        //京东首页提示信息展示 展示
        PreferenceUtils.writeBoolConfig(Constants.KEY_JD_HOME_PAGE_NOTICE, true, getApplicationContext());
        //苏宁首页提示信息展示 展示
        PreferenceUtils.writeBoolConfig(Constants.KEY_SN_HOME_PAGE_NOTICE, true, getApplicationContext());
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().killActivity(this);
        jpNetRequest = null;

        com.yilian.mylibrary.PreferenceUtils.writeBoolConfig(Constants.RED_PACKET_FIRST, true, mContext);
    }

    private void init() {
        //全屏
        initWindow();

        //注册或StartApp
        initConfig();

        getDeviceInfo(mContext);
    }

    private void initLanguagePackage() {
        Logger.i("push_id : " + "initLanguagePackage");
        try {
            String url = "http://img.yilian.lefenmall.com/configs/yilian.json";
            //下载json文件
            LanguagePackageUtils.createFile(url);
            Gson gson = new Gson();
            //用sp把语言包存起来
            String info = gson.toJson(LanguagePackageUtils.parseJsonWithGson(LanguagePackageUtils.getFile(), LanguagePackageEntity.class));
            PreferenceUtils.saveLanguagePackae(mContext, info);
            //把ip字段存起来
            Logger.i("获取的URL：1" + gson.fromJson(PreferenceUtils.getLanguaePakcage(mContext), LanguagePackageEntity.class).toString());

            PreferenceUtils.writeStrConfig(Constants.LANGUAGE_PACKAGE_IP,
                    gson.fromJson(PreferenceUtils.getLanguaePakcage(mContext), LanguagePackageEntity.class).testIp, mContext);

            Logger.i("获取的URL：2" + PreferenceUtils.readStrConfig(Constants.LANGUAGE_PACKAGE_IP, mContext));
        } catch (Exception e) {
//            showToast("语言包解析错误！");
            e.printStackTrace();

        }
    }

    /**
     * 初始化完毕后跳转到内部页面
     */
    private void jumpIntoInnerPage() {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                //        2018-05-17 14:18:49线上商城取消,分类不再获取
//                getCategoryNameList();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                // 欢迎界面显示2秒,如果不是首次安装，就不显示引导页
                //显示引导页
                if (sp.getBoolean(Constants.SPKEY_IS_FIRST, true)) {
                    startActivity(new Intent(StartActivity.this, GuideActivity.class));
                } else {
                    final boolean haveAD = sp.getBoolean(Constants.SPKEY_HAVEAD, false);
                    //跳转到广告页面
                    if (haveAD) {
                        Logger.i("startActivity:跳转到了广告");
                        startActivity(new Intent(mContext, ADActivity.class));
                    } else {//跳转到首页
                        Logger.i("startActivity:跳转到了首页");
                        startActivity(new Intent(mContext, JPMainActivity.class));
                    }
                    getAD_Data();
                    notificationClick();
                }
            }
        });
    }

    /**
     * 获取广告数据
     */
    private void getAD_Data() {
        if (request == null) {
            request = new MallNetRequest(mContext);
        }
        request.getADData(new RequestCallBack<ADEntity>() {
            @Override
            public void onSuccess(ResponseInfo<ADEntity> responseInfo) {
                final ADEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        handler.sendEmptyMessage(HAVEAD);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(Constants.SPKEY_AD_TYPE, result.data.type).commit();//广告跳转类型
                        edit.putString(Constants.SPKEY_AD_CONTENT, result.data.content).commit();//广告跳转内容（例如：跳转WebView时的URL）
                        edit.putInt(Constants.SPKEY_AD_SHOWTIME, Integer.valueOf(result.data.showTime)).commit();//广告显示时间长度
                        edit.putString(Constants.SPKEY_AD_IMAGE_URL, result.data.imageUrl).commit();
                        Logger.i("请求到了广告数据");
                        Logger.i(result.data.toString());
                        saveAD_Data(result, edit);

                        break;
                    default:
                        handler.sendEmptyMessage(NOAD);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    /**
     * 存储广告数据
     *
     * @param result
     * @param edit
     */
    private void saveAD_Data(final ADEntity result, SharedPreferences.Editor edit) {
//        启动广告

        edit.putString(Constants.SPKEY_AD_IMAGE_URL, result.data.imageUrl).commit();
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(Constants.ImageUrl + result.data.imageUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + Constants.BASE_PATH);
                        if (bitmap != null) {
                            FileUtils.saveBtpToFile(getApplicationContext(), bitmap, Environment.getExternalStorageDirectory().getPath() + Constants.BASE_PATH + "ad");//把图片保存到sd卡
                        }
                        inputStream.close();

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        首页弹屏广告
        ADEntity.AdvDataBean newAdvDataBean = result.advDataBean;

        if (newAdvDataBean != null) {
            SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(mContext, Constants.SPKEY_JP_MAIN_ACTIVITY);
            sharedPreferencesUtils.setObject(Constants.JP_MAIN_ACTIVITY_ADV, newAdvDataBean);//广告存到本地
            com.yilian.mylibrary.PreferenceUtils.writeObjectConfig(Constants.JP_MAIN_ACTIVITY_ADV, newAdvDataBean, mContext);
        }


//        if (newAdvDataBean.isShow.equals("1")) {
//            if (!TextUtils.isEmpty(newAdvDataBean.advImageUrl)) {//如果推送图片图片URL不为空，也就是有推送活动图片
//                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(mContext, Constants.SPKEY_JP_MAIN_ACTIVITY);
//                sharedPreferencesUtils.setObject(Constants.JP_MAIN_ACTIVITY_ADV, newAdvDataBean);//广告存到本地
//                com.yilian.mylibrary.PreferenceUtils.writeObjectConfig(Constants.JP_MAIN_ACTIVITY_ADV, newAdvDataBean, mContext);
//            }
//        } else {//删除上次存储的信息
////            com.yilian.mylibrary.PreferenceUtils.writeObjectConfig(Constants.JP_MAIN_ACTIVITY_ADV, null, mContext);
//            Logger.i("弹屏走了这里2");
//
//            com.yilian.mylibrary.PreferenceUtils.remove(Constants.JP_MAIN_ACTIVITY_ADV, mContext);
//        }
//首页公告
        ADEntity.HomePageNoticeBean notice = result.notice;
        Logger.i("公告：" + notice.toString());
        Logger.i("弹屏走了这里3");

        if (notice != null) {
            com.yilian.mylibrary.PreferenceUtils.writeObjectConfig(Constants.HOME_PAGE_NOTICE, notice, mContext);
        }

//        if (notice != null) {
//            Logger.i("弹屏走了这里4");
//
//            com.yilian.mylibrary.PreferenceUtils.writeObjectConfig(Constants.HOME_PAGE_NOTICE, notice, mContext);
//        } else {//删除上次存储的信息
//            Logger.i("弹屏走了这里5");
//
////            com.yilian.mylibrary.PreferenceUtils.writeObjectConfig(Constants.HOME_PAGE_NOTICE, null, mContext);
//            com.yilian.mylibrary.PreferenceUtils.remove(Constants.HOME_PAGE_NOTICE,mContext);
//        }
    }

    private void GetuiPush() {
        Logger.i("push_id : " + "GetuiPush");
//        初始化个推服务
        PushManager.getInstance().initialize(this.getApplicationContext(), GetuiPushService.class);
        //注册个推服务
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), PushIntentService.class);
        Logger.i("个推的服务已启动  ");
    }

    private void requestPermission() {
        if (!readPhonePermission && !writeExternalPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        } else if (!readPhonePermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PERMISSION_CODE);
        } else if (!writeExternalPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }
    }

    /**
     * 通知栏点击消息跳转
     */
    private void notificationClick() {
        Intent intent = null;
        if (getIntent().getBooleanExtra("notice", false)) {//如果从通知栏消息跳转而来,根据消息类型启动消息需要跳转的页面
            PushMsg pushMsg = (PushMsg) getIntent().getSerializableExtra("push_msg");
            List<String> content = pushMsg.id;
            intent = new Intent(mContext, StartActivity.class);
            switch (pushMsg.type) {
                case "0": //0.新闻公告活动web页面 (id : ["0","url"])//不需要登录
                    intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("url", content.get(1));
                    intent.putExtra("title_name", pushMsg.title);
                    break;
                case "1": //1.外部浏览器打开web页面 (id : ["0","url"])
                    intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("url", content.get(1));
                    intent.putExtra("title_name", pushMsg.title);
                    break;
                case "2": //2.本地商城详情页面(id : ["0","本地商城id"])
                    intent = new Intent(mContext, JPLeFenHomeActivity.class);
                    intent.putExtra("type", content.get(0));
                    intent.putExtra("index_id", content.get(1));
                    break;
                case "3": { //3.本地活动页面
                    switch (content.get(0)) {
                        case "1":
                            intent = new Intent(mContext, DaZhuanPanDetailActivity.class);
                            break;
                        case "2":
                            intent = new Intent(mContext, GuessDetailActivity.class);
                            break;
                        case "3":
                            intent = new Intent(mContext, GuaGuaKaActivity.class);
                            break;
                        case "4":
                            intent = new Intent(mContext, YaoYiYaoActivity.class);
                            break;
                        case "5":
                            intent = new Intent(mContext, JPSignActivity.class);
                            break;
                        default:
                            return;
                    }
                    intent.putExtra("activity_index", content.get(1));
                }
                break;
                case "4": //4.中奖凭证页面 (id : ["0","中奖凭证id"])
                    intent = new Intent(mContext, PrizeVoucherDetailActivity.class);
                    intent.putExtra("voucher_index", content.get(1));
                    break;
                case "5": //5.商品详情页面 (id : ["0","本地商城id(filiale_id),商品详情id(goods_id)"])
                    String str = content.get(1);
                    String filialeId = str.split(",")[0];
                    String goodsId = str.split(",")[1];
                    intent = new Intent(mContext, JPNewCommDetailActivity.class);
                    intent.putExtra("filiale_id", filialeId);
                    intent.putExtra("goods_id", goodsId);
                    break;
                case "6": //6.商家套餐详情页面 (id : ["0","套餐id"])
                    intent = new Intent(mContext, MTComboDetailsActivity.class);
                    intent.putExtra("package_id", content.get(1));
                    break;
                case "7": //7.本地商家详情页面 (id : ["0","商家id"])
                    intent = new Intent(mContext, MTMerchantDetailActivity.class);
                    intent.putExtra("merchant_id", content.get(1));
                    break;
                case "8": //8.旗舰店详情页面 (id : ["0","旗舰店id"])
                    intent = new Intent(mContext, JPFlagshipListActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("index_id", content.get(1));
                    break;
                case "9":
                    switch (content.get(0)) {
                        case "0":
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                            break;
                        case "2":
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                            break;
                        case "3":
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                            break;
                        case "4":
                            intent = new Intent(mContext, V3MoneyActivity.class);
                            intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
                            break;
                        case "5"://暂时改成奖券页面跳转 iOS不支持 2017年8月21日16:56:42修改
                            intent = new Intent(mContext, V3MoneyActivity.class);
                            intent.putExtra("type", V3MoneyActivity.TYPE_INTEGRAL);
                            break;
                        case "6"://暂时改成奖券页面跳转 iOS不支持 2017年8月21日16:56:42修改
                            intent = new Intent(mContext, V3MoneyActivity.class);
                            intent.putExtra("type", V3MoneyActivity.TYPE_INTEGRAL);
                            break;
                        case "7":
                            intent = new Intent(mContext, FavoriteActivity.class);
                            break;
                        case "8":
                            intent = new Intent(mContext, WebViewActivity.class);
                            String yyCardUrl = sp.getString("yyCardUrl", "");
                            intent.putExtra(Constants.SPKEY_URL, yyCardUrl);
                            break;
                        case "9":
                            if (isLogin()) {
                                intent = new Intent(mContext, JPInvitePrizeActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "10":
//                            intent = new Intent(mContext, NMemberChargeActivity.class);
//                            intent.putExtra("type", "chooseCharge");

                            intent = new Intent(mContext, RechargeActivity.class);
                            break;
                        case "11":
                            intent = new Intent(mContext, MembersLevel3.class);
                            break;
                        case "12":
                            if (isLogin()) {
                                intent = new Intent(mContext, MipcaActivityCapture.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "13":
                            intent = new Intent(mContext, SearchCommodityActivity.class);
                            break;
                        case "14":
                            intent = new Intent(mContext, MTFindActivity.class);
                            break;
                        case "15":
                            if (isLogin()) {
                                intent = new Intent(mContext, InformationActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "16":
                            intent = new Intent(mContext, MerchantSearchActivity.class);
                            break;
                        case "17"://意见反馈

                            break;
                        case "21":
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT);
                            break;
                        case "24":
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT);
                            break;
                        case "25":
                            if (isLogin()) {
                                intent = new Intent(mContext, JPInvitePrizeActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "26":
                            if (isLogin()) {
                                intent = new Intent(mContext, JTakeCashActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "27":
                            intent = new Intent(mContext, MallMainActivity.class);
                            intent.putExtra("type", "1");
                            break;
                        case "28":
                            intent = new Intent(mContext, MallMainActivity.class);
                            intent.putExtra("type", "2");
                            break;
                        case "29":
                            if (isLogin()) {
                                intent = new Intent(mContext, MerchantCenterActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "30":
                            if (isLogin()) {
                                intent = new Intent(mContext, MerchantComboManageActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case "10":
                    if (isLogin()) {
                        switch (content.get(0)) {
                            case "0":
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "0");
                                break;
                            case "1":
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "1");
                                break;
                            case "2":
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "2");
                                break;
                            case "3":
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "3");
                                break;
                            case "4":
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "4");
                                break;
                            case "5":
                                intent = new Intent(mContext, AfterSaleActivity.class);
                                break;
                            default:
                                break;
                        }
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "12":
                    intent = new Intent(mContext, JPOrderActivity.class);
                    intent.putExtra("order_index", content.get(1));
                    break;
                case "13":
                    intent = new Intent(mContext, MTOrderListActivity.class);
                    switch (content.get(0)) {
                        case "0":
                            intent.putExtra("comboType", 0);
                            break;
                        case "1":
                            intent.putExtra("comboType", 1);
                            break;
                        case "2":
                            intent.putExtra("comboType", 2);
                            break;
                        case "3":
                            intent.putExtra("comboType", 3);
                            break;
                        case "4":
                            intent.putExtra("comboType", 4);
                            break;
                        default:
                            break;
                    }
                    break;
                case "14":
                    intent = new Intent(mContext, MTOrderDetailActivity.class);
                    intent.putExtra("index_id", content.get(1));
                    break;
                case "18": //0.新闻公告活动web页面 (id : ["0","url"])//不需要登录
                    if (isLogin()) {
                        intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("url", content.get(1));
                        intent.putExtra("title_name", pushMsg.title);
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }

                    break;
                case "19":
                    break;
                case "20":
                    break;
                case "21":
                    intent = new Intent(mContext, JPNewCommDetailActivity.class);
                    intent.putExtra("goods_id", content.get(1));
                    intent.putExtra("classify", content.get(2));
                    break;
                case "22":
                    if (isLogin()) {
                        intent = new Intent(mContext, MerchantComboManageActivity.class);
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "23":
                case "24":
                    if (isLogin()) {
                        intent = new Intent(mContext, AfterSaleOneActivity.class);
                        intent.putExtra("orderId", content.get(1));
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "25":
                    if (isLogin()) {
                        intent = new Intent(mContext, InformationActivity.class);
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "26":
                    if (isLogin()) {
                        intent = new Intent(mContext, MerchantOrderDetailsActivity.class);
                        intent.putExtra("orderIndex", content.get(0));
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "27":
                    if (isLogin()) {
                        intent = new Intent(mContext, MerchantAfterSaleDetailsActivity.class);
                        intent.putExtra(Constants.SERVICE_INDEX, content.get(0));
                        intent.putExtra(Constants.SERVICE_TYPE, "0");
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "28":
                    if (isLogin()) {
                        intent = new Intent(mContext, MerchantAfterSaleDetailsActivity.class);
                        intent.putExtra(Constants.SERVICE_INDEX, content.get(0));
                        intent.putExtra(Constants.SERVICE_TYPE, "1");
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                default:
                    intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    break;
            }

            if (intent == null) {
                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
            }
            startActivity(intent);
        }
    }

    private void initWindow() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void getCategoryNameList() {//获取新版商城分类名称
        SharedPreferencesUtils spUtils = new SharedPreferencesUtils(mContext, Constants.SPKEY_JP_CATEGORY_OBJECT);
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        jpNetRequest.getJPLevel1Category(new RequestCallBack<JPLevel1CategoryEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPLevel1CategoryEntity> responseInfo) {

//                JPLevel1Category result = responseInfo.result;
                JPLevel1CategoryEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        jpLevel1CategoryList = result.JPLevel1CategoryList;
                        JPLevel1CategoryEntity.ListBean firstCategory = new JPLevel1CategoryEntity.ListBean();
                        firstCategory.JPLevel1CategoryName = "上新";
                        jpLevel1CategoryList.add(0, firstCategory);
//                        sp存数组，把集合数据存到一个新数组中
                        jpLevel1CategoryArray = new JPLevel1CategoryEntity.ListBean[jpLevel1CategoryList.size()];
                        for (int i = 0, count = jpLevel1CategoryList.size(); i < count; i++) {
                            jpLevel1CategoryArray[i] = jpLevel1CategoryList.get(i);
                        }

                        spUtils.setObject(Constants.SPKEY_JP_CATEGORY_OBJECT, jpLevel1CategoryArray);
                        break;
                    default:
                        Logger.i("返回码:" + result.code);
                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Logger.i("请求失败:" + s);
//请求失败后则从本地获取分类，如果没有则手动new，再存入本地，如果有说明本地已存有分类，不做任何操作
                jpLevel1CategoryArray = (JPLevel1CategoryEntity.ListBean[]) spUtils.getObject(Constants.SPKEY_JP_CATEGORY_OBJECT, Object.class);

                if (jpLevel1CategoryArray == null || jpLevel1CategoryArray.length <= 0) {//从网络获取失败，且从本地没有取到，则手动new并存到本地
                    showToast("分类获取异常,请检查您的网络状况后,刷新页面重试");//获取失败，且从本地没有取到，才进行该提示
                    jpLevel1CategoryArray = new JPLevel1CategoryEntity.ListBean[10];
                    JPLevel1CategoryEntity.ListBean listBean;
                    for (int i = 0; i < 10; i++) {
                        listBean = new JPLevel1CategoryEntity.ListBean();
                        listBean.JPLevel1CategoryId = String.valueOf(i);
                        switch (i) {
                            case 0:
                                listBean.JPLevel1CategoryName = "上新";
                                break;
                            case 1:
                                listBean.JPLevel1CategoryName = "全球购";
                                break;
                            case 2:
                                listBean.JPLevel1CategoryName = "美妆";
                                break;
                            case 3:
                                listBean.JPLevel1CategoryName = "数码家电";
                                break;
                            case 4:
                                listBean.JPLevel1CategoryName = "女装";
                                break;
                            case 5:
                                listBean.JPLevel1CategoryName = "男装";
                                break;
                            case 6:
                                listBean.JPLevel1CategoryName = "鞋包";
                                break;
                            case 7:
                                listBean.JPLevel1CategoryName = "美食";
                                break;
                            case 8:
                                listBean.JPLevel1CategoryName = "母婴";
                                break;
                            case 9:
                                listBean.JPLevel1CategoryName = "家居家纺";
                                break;
                            default:
                                break;

                        }
                        jpLevel1CategoryArray[i] = listBean;
                    }
                    spUtils.setObject("categoryObject", jpLevel1CategoryArray);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_PERMISSION_CODE:
                    initThirdConfig();
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //语言包
                        initLanguagePackage();
                        registerDevice();
                    }
                    //处理完权限申请逻辑后，再进行页面跳转
                    jumpIntoInnerPage();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 如果是首次启动进行设备注册
     * 如果不是直接执行 startApp()
     */
    private void initConfig() {
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_PHONE_STATE", "com.yilian.mall"));
        readPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        writeExternalPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!readPhonePermission || !writeExternalPermission) {
            requestPermission();
        } else {
            initThirdConfig();
            //语言包
            initLanguagePackage();
            jumpIntoInnerPage();
            registerDevice();
        }
    }

    /**
     * 初始化第三方功能配置
     */
    private void initThirdConfig() {
        GetuiPush();
    }

    /**
     * 注册设备，获取device_index
     */
    private void registerDevice() {
        Logger.i("权限通过");
        // 如果是首次打开，注册设备
        if (true) {
            //之后在程序中通过以下代码获取设备屏幕分辨率UserInfor
            dm = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(dm);
            Logger.i("注册设备开始");
            if (netRequset == null) {
                netRequset = new AccountNetRequest(getApplicationContext());
            }

            netRequset.registerDevice(dm.widthPixels, dm.heightPixels, RegisterDevice.class, new RequestCallBack<RegisterDevice>() {

                @Override
                public void onSuccess(ResponseInfo<RegisterDevice> arg0) {
                    Logger.i("开始写入deviceIndex:" + arg0.result.deviceIndex + "  arg0.result.code" + arg0.result.code + "  arg0.result_deviceindex " + arg0.result.deviceIndex + "  msg  " + arg0.result.msg);
                    switch (arg0.result.code) {
                        case 1: // 请求成功
                            PreferenceUtils.writeStrConfig(Constants.SPKEY_DEVICE_INDEX, arg0.result.deviceIndex, getApplicationContext());
                            startApp();
                            break;

                        case -12: // 设备已注册
                            PreferenceUtils.writeStrConfig(Constants.SPKEY_DEVICE_INDEX, arg0.result.deviceIndex, getApplicationContext());
                            startApp();
                            break;

                        case -3: // 系统繁忙，3-10秒重试
                            ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
//                                    initConfig();
                                }
                            });
                            break;
                        default:
                            break;
                    }

                }

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    // 请求失败的原因如果是请求超时则重新执行
                    if (arg0.getExceptionCode() == 408) {
                        initConfig();
                    }
                }
            });
        } else {
            startApp();
        }
    }

    /**
     * APP 每次启动的时候调用
     * 获取 盐值，验证 Token
     */
    private void startApp() {
        Logger.i("push_id : " + "startApp");
        // 各推推送标示 id
        String push_id = sp.getString(Constants.SPKEY_CLIENTID, "0");

        Logger.i("push_id : " + push_id);
        // 网络请求
        int versionCode = 0;
        try {
            versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.i("startapp走了这里1");
        if (netRequset == null) {
            netRequset = new AccountNetRequest(getApplicationContext());
        }
//test tag
        netRequset.appStart(String.valueOf(versionCode), "0", push_id, Start_app.class, new RequestCallBack<Start_app>() {

            @Override
            public void onSuccess(ResponseInfo<Start_app> arg0) {
                Logger.i("startApp请求回调：" + arg0.result.code);
                switch (arg0.result.code) {
                    case 1: //  请求成功
                    /* 存储服务器返回的盐值 */
                        String serverSalt = arg0.result.serverSalt;
                        Logger.i("startApp成功，启动返回的盐值:" + serverSalt);
                        PreferenceUtils.writeStrConfig(Constants.SPKEY_SERVER_SALT, serverSalt, mContext);
                        PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_VERSION, arg0.result.newestVersion, mContext);

					/* arg0.result.loginStatus 登录状态,0表示失效,1表示有效
                     存储本地用于做是否登录状态判断
					 */
                        if (arg0.result.loginStatus.equals("1")) {
                            PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, true, getApplicationContext());
                            RequestOftenKey.getUserInfor(getApplicationContext(), sp);
                            // 如果是登录状态则获取ali的userid,用于兼容更新前没有客服的版本
                            // 登录阿里客服账号
//                            try {
//                                RetrofitUtils.getInstance().setContext(mContext).getAliAccountInfo();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            ;//如果登录，则获取融云IMToken
                        } else {
                            PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, false, getApplicationContext());
                        }
                        break;
                    default:

                        if (arg0.result.loginStatus.equals("1")) {
                            PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, true, getApplicationContext());
                            RequestOftenKey.getUserInfor(mContext, sp);
                        } else {
                            PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, false, getApplicationContext());
                        }
                        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);////  -3时系统繁忙，3-10秒重试
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                startApp();
                            }
                        });
                        break;
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Logger.i("startApp请求回调：" + arg0.getMessage());
                // 请求失败的原因如果是请求超时则重新执行
                if (arg0.getExceptionCode() == 408) {
                    startApp();
                }
            }
        });
    }
}
