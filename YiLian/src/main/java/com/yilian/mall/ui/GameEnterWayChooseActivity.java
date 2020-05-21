package com.yilian.mall.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.service.DownloadGameService;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.event.EventGameDownloadStatusChanged;
import com.yilian.networkingmodule.entity.GameEntity_Android;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class GameEnterWayChooseActivity extends Activity implements View.OnClickListener {


    private TextView tvEnterH5;
    private TextView tvEnterApk;
    private TextView tvCancel;
    private GameEntity_Android gameEntity_android;
    private String h5Url;
    private String downloadUrl;
    private String gameName;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_game_enter_way_choose);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, true, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, false, this);
    }
    private void initData() {
        gameEntity_android = (GameEntity_Android) getIntent().getSerializableExtra("gameEntity_android");
        gameName = getIntent().getStringExtra("game_name");
        position = getIntent().getIntExtra("position", -1);
        h5Url = getIntent().getStringExtra("h5Url");
        if (gameEntity_android == null && TextUtils.isEmpty(h5Url)) {
            android.widget.Toast.makeText(this, "游戏信息异常", android.widget.Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        downloadUrl = gameEntity_android.url;
        if (!TextUtils.isEmpty(h5Url) && !TextUtils.isEmpty(downloadUrl)) {
//            如果同时存在下载地址和h5页面，则弹窗提示
        } else if (!TextUtils.isEmpty(h5Url) && TextUtils.isEmpty(downloadUrl)) {
//            如果有h5 没有下载地址，则直接打开h5页面
            openUrl(h5Url);
        } else if (TextUtils.isEmpty(h5Url) && !TextUtils.isEmpty(downloadUrl)) {
//             如果没有h5 有下载地址,则弹窗提示是否打开下载地址
            tvEnterH5.setVisibility(View.GONE);
            findViewById(R.id.view1).setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(h5Url) && TextUtils.isEmpty(downloadUrl)) {
            android.widget.Toast.makeText(this, "游戏信息异常", android.widget.Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {
        tvEnterH5 = (TextView) findViewById(R.id.tv_enter_h5);
        tvEnterH5.setOnClickListener(this);
        tvEnterApk = (TextView) findViewById(R.id.tv_enter_apk);
        tvEnterApk.setOnClickListener(this);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter_h5:
                openUrl(h5Url);
                break;
            case R.id.tv_enter_apk:
                openSystemChrome(downloadUrl);
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    /**
     * 打开内部浏览器 进入游戏界面
     *
     * @param h5Url
     */
    private void openUrl(String h5Url) {
        Intent downloadIntent = new Intent(this, WebViewActivity.class);
        downloadIntent.putExtra(Constants.SPKEY_URL, h5Url);
        startActivity(downloadIntent);
        finish();
    }

    /**
     * 打开系统自带浏览器下载apk
     *
     * @param downloadUrl
     */
    private void openSystemChrome(String downloadUrl) {
        Logger.i("下载走了这里1");
        if (downloadUrl.endsWith(".apk")) {//如果是apk包，则直接打开下载服务进行下载
            Logger.i("下载走了这里2");
            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            Logger.i("下载走了这里3");
            List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
            Logger.i("下载走了这里4");
            if (runningServices.size() > 0) {
                Logger.i("下载走了这里5");
                for (int i = 0; i < runningServices.size(); i++) {
                    Logger.i("服务名称:" + runningServices.get(i).service.getClassName());
                    if ((runningServices.get(i)).service.getClassName().equals("com.yilian.mall.service.DownloadGameService")) {
                        Logger.i("下载走了这里6");
                        Toast.makeText(GameEnterWayChooseActivity.this, "游戏正在下载中,请稍后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            EventBus.getDefault().post(new EventGameDownloadStatusChanged(false));
            Intent service = new Intent(this, DownloadGameService.class);
            service.putExtra("game_download_url", downloadUrl);
            service.putExtra("game_name", gameName);
            service.putExtra("position", position);
            startService(service);
        } else {//不是apk包（一般是下载页面链接），则打开系统自带浏览器进行下载
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(downloadUrl);
            intent.setData(content_url);
            startActivity(intent);
        }
        finish();
    }
}
