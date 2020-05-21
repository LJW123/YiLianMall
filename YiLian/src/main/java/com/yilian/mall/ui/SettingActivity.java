package com.yilian.mall.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.http.ServiceNetRequest;
import com.yilian.mall.utils.DataCleanManager;
import com.yilian.mall.utils.FileUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.UpdateUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.io.File;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.tv_back)
    private TextView tv_back;
    @ViewInject(R.id.tv_on_off_voice)
    private TextView tvOnOffVoice;
    @ViewInject(R.id.tv_version)
    private TextView tv_version;

    @ViewInject(R.id.clear_cache)
    private TextView clearCache;

    private ServiceNetRequest mAppNetRequest;
    private Boolean onOffVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ViewUtils.inject(this);
        tv_back.setText("设置");
        initView();
        initListener();
        try {
            tv_version.setText(this.getPackageManager().getPackageInfo("com.yilian.mall", 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            clearCache.setText(FileUtils.getFileSizes("/com.yilian/") + "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initView() {
        setVoiceIcon();
    }

    /**
     * 根据语音播报开关的值 设置语音播报按钮图片
     */
    private void setVoiceIcon() {
        onOffVoice = PreferenceUtils.readBoolConfig(Constants.ON_OFF_VOICE, mContext, false);
        if (onOffVoice) {
            tvOnOffVoice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.cash_desk_on), null);
        } else {
            tvOnOffVoice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.cash_desk_off), null);
        }
    }

    /**
     * 开启或关闭语音播报
     */
    @SuppressWarnings("unchecked")
    private void setOnOffVoice() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).setVoiceOnOff("account/setVoiceButt", onOffVoice ? 0 : 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopMyDialog();
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
                        PreferenceUtils.writeBoolConfig(Constants.ON_OFF_VOICE, !onOffVoice, mContext);
                        setVoiceIcon();
                    }
                });
        addSubscription(subscription);
    }

    private void initListener() {
        RxUtil.clicks(tvOnOffVoice, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                setOnOffVoice();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RequestOftenKey.getUserInfor(SettingActivity.this, sp);
    }

    /**
     * 常见问题
     *
     * @param view
     */
    public void question(View view) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", com.yilian.mylibrary.Constants.APP_FAQ);
        startActivity(intent);
    }

//	/**
//	 * 关于幸运购
//	 * @param view
//     */
//	public void aboutOneBuy(View view){
//		Intent intent = new Intent(SettingActivity.this,WebViewActivity.class);
//		intent.putExtra("url", Ip.HELP_URL+"agreementforios/aboutsnatch.html");
//		startActivity(intent);
//	}

    /**
     * 意见反馈
     *
     * @param view
     */
    public void feedback(View view) {
        startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
    }

    /**
     * 关于我们
     *
     * @param view
     */
    public void versionUpdate(View view) {
        startActivity(new Intent(SettingActivity.this, VersionUpdateActivity.class));
    }

    public void goAppStore(View view) {

        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            showToast("未检测到应用市场");
        }

    }

    /**
     * 推荐应用
     *
     * @param view
     */
    public void recommendApp(View view) {
        startActivity(new Intent(this, RecommendAppActivity.class));
    }

    /**
     * 检查更新
     */
    public void checkUpdata(View view) {
        UpdateUtil.checkUpDate(mContext, true);
    }

    /**
     * 清除缓存
     *
     * @param view
     */
    public void clearCache(View view) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "com.yilian");
            FileUtils.deleteDirectory(file);
            FileUtils.deleteDirectory(this.getCacheDir());
            DataCleanManager.cleanSharedPreference(this);
            clearCache.setText("暂无缓存数据");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
