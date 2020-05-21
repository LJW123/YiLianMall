package com.yilian.mall.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.amap.api.location.AMapLocation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.yilian.mall.R;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.MyLoading;
import com.yilian.mall.widgets.VersionDialog;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/6/3.
 */
public abstract class BaseFragment extends Fragment {

    protected static Context mContext;
    protected static ImageLoader imageLoader;
    protected static DisplayImageOptions options;
    public View rootView;
    /**
     * Fragment已经初始化完成
     */
    public boolean isPrepared = false;
    /**
     * 该值用于子类Fragment使用activity对象的地方使用，防止getActivity为null的情况
     */
    protected AppCompatActivity mAppCompatActivity;
    protected SharedPreferences sp;
    protected MyLoading myloading;
    protected int screenWidth;
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AMapLocation aMapLocation = intent.getParcelableExtra("aMapLocation");
            subOnReceive(aMapLocation);
        }
    };
    protected onLoadDataSuccessListener onloadListener;
    protected isDataListener dataListener;
    /**
     * 是否可见
     */
    protected boolean isVisble;
    protected CompositeSubscription compositeSubscription;

    /**
     * 收到广播后执行该方法
     * 不需要所有页面都实现，写成空方法，需要的子类进行实现
     *
     * @param aMapLocation
     */
    public void subOnReceive(AMapLocation aMapLocation) {
    }

    /**
     * 判断是否登录
     */
    public boolean isLogin() {
        return PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, mContext, false);
    }

    /**
     * dialog 启动
     */
    public void startMyDialog() {
        if (isAdded() && getActivity() != null && !getActivity().isFinishing()) {
            if (myloading == null) {
                myloading = MyLoading.createLoadingDialog(getActivity());
                Logger.i(this.getClass().getName() + "  创建了了dialog  " + this.toString());
            }
            try {//捕获异常，处理 is your activity running的异常
                myloading.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(20000);
                            if (myloading != null && myloading.isShowing()) {
                                mAppCompatActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stopMyDialog();
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
            } catch (Exception e) {
                Logger.i("异常信息：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {
        if (myloading != null && getActivity() != null && !getActivity().isFinishing() && isAdded()) {
            try {
                myloading.dismiss();
                Logger.i(this.getClass().getName() + "  取消了dialog  " + this.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Logger.i(this.getClass().getName() + "  取消dialog时异常了  " + this.toString());
            }
        }
    }

    public void startMyDialog(boolean isCancel) {
        if (isAdded() && mAppCompatActivity != null && !mAppCompatActivity.isFinishing()) {
            if (myloading == null) {
                myloading = MyLoading.createLoadingDialog(mAppCompatActivity, isCancel);
                Logger.i(this.getClass().getName() + "  创建了了dialog  " + this.toString());
            }
            try {//捕获异常，处理 is your activity running的异常
                myloading.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(20000);
                            if (myloading != null && myloading.isShowing()) {
                                mAppCompatActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stopMyDialog();
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
            } catch (Exception e) {
                Logger.i("异常信息：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void showToast(String msg) {
        if (isAdded()) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(int strId) {
        if (isAdded()) {
            Toast.makeText(getActivity(), getResources().getString(strId), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示提示框
     *
     * @param title        标题
     * @param msg          内容
     * @param detailMsg    详细内容
     * @param iconId       内容 图标 没有就传0
     * @param gravity      图标和内容位置
     * @param positiveText 确定字样
     * @param negativetext 取消字样
     * @param cancelable   点击屏幕意外或者返回键是否消失
     * @param listener     按键监听
     */
    public void showDialog(@Nullable String title, @Nullable String msg, @Nullable String detailMsg,
                           int iconId, int gravity, @Nullable String positiveText, @Nullable String negativetext,
                           boolean cancelable, DialogInterface.OnClickListener listener, Context context
    ) {
        VersionDialog.Builder builder = new VersionDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(msg, iconId, gravity);
        builder.setDetailMessage(detailMsg);
        builder.setPositiveButton(positiveText, listener);
        builder.setNegativeButton(negativetext, listener);
        builder.setCancelable(cancelable);
        if (!getActivity().isFinishing()) {
            builder.create().show();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisble = true;
            onVisible();
        } else {
            isVisble = false;
            onInVisible();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mAppCompatActivity == null) {
            mAppCompatActivity = (AppCompatActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mContext == null) {
            mContext = getActivity();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yilian.mall.aMapLocation");
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sp = mContext.getSharedPreferences("UserInfor", 0);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        imageLoader = ImageLoader.getInstance();
        screenWidth = ScreenUtils.getScreenWidth(mContext);
        if (rootView == null) {
            rootView = createView(inflater, container, savedInstanceState);
        }
        isPrepared = true;
        loadData();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("Lifecycle:onResume" + this.getClass().getSimpleName());
        GlideUtil.resumeRequests(getActivity().getApplicationContext());
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.i("Lifecycle:onPause" + this.getClass().getSimpleName());
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        GlideUtil.pauseRequests(getActivity().getApplicationContext());
        GlideUtil.pauseRequests(mAppCompatActivity.getApplicationContext());
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    /**
     * 加载化布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 数据加载
     */
    protected abstract void loadData();

    protected void onVisible() {
        //加载数据
        lazyLoadData();
    }

    protected void onInVisible() {
    }

    /**
     * 在这里加载数据可以实现懒加载
     */
    protected void lazyLoadData() {
    }

    public void setOnLoadDataSuccessListener(onLoadDataSuccessListener onLoadDataSuccessListener) {
        this.onloadListener = onLoadDataSuccessListener;
    }

    public void setOnIsDataListener(isDataListener isDataListener) {
        this.dataListener = isDataListener;
    }

    protected void dismissInputMethod() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        0);
            }
        }

        Logger.i("隐藏软键盘");
    }

    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            imm.toggleSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

    }

    /**
     * fragment获取网络数据是否成功
     */
    public interface onLoadDataSuccessListener {
        void isLoadDataSuccess(boolean isSuccess);
    }

    /**
     * fragment获取网络数据是否成功
     */
    public interface isDataListener {
        void isData(boolean isSuccess);
    }
}
