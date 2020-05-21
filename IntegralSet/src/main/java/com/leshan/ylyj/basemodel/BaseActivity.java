package com.leshan.ylyj.basemodel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.leshan.ylyj.base.BaseView;
import com.leshan.ylyj.testfor.R;

import rxfamily.entity.BaseEntity;


public abstract class BaseActivity extends RxAppCompatActivity implements BaseView {

    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = false;
    /**
     * 是否允许全屏
     **/
    private boolean mAllowFullScreen = false;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isAllowScreenRoate = false;

    private Toolbar toolbar;
    public TextView title_tv;
    private TextView title_sub_tv;
    //private ImageButton back_btn;
    private Boolean hasMenu = false;
    private int menu_layout;

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        if (isSetStatusBar) {
            steepStatusBar();
        }

        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mContext = this;
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    @Override
    protected void onResume() {
        super.onResume();
        //callback 1
        //   Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //callback 2
        //  Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //callback 3
        //   Bugtags.onDispatchTouchEvent(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /* [是否允许全屏]
     *
     * @param allowFullScreen
    */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    /**
     * [是否设置沉浸状态栏]
     *
     * @param isSetStatusBar
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * [是否允许屏幕旋转]
     *
     * @param isAllowScreenRoate
     */
    public void setScreenRoate(boolean isAllowScreenRoate) {
        this.isAllowScreenRoate = isAllowScreenRoate;
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //使能app bar的导航功能
            actionBar.setDisplayHomeAsUpEnabled(false);
            //必须设置
            actionBar.setDisplayShowTitleEnabled(false);
        }

        title_tv = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setNavigationIcon(int icon) {
        toolbar.setNavigationIcon(icon);
    }

    public void hasBack(Boolean is_back) {
        if (is_back) {

            toolbar.setNavigationIcon(R.mipmap.library_module_v3back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setToolbarBack();
                }
            });
            /*
            back_btn.setVisibility(View.VISIBLE);
            RxView.clicks(back_btn)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            setToolbarBack();
                        }
                    });*/
        }
    }

    public void hasWhiteBack(Boolean is_back) {
        if (is_back) {

            toolbar.setNavigationIcon(R.mipmap.back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setToolbarBack();
                }
            });
            /*
            back_btn.setVisibility(View.VISIBLE);
            RxView.clicks(back_btn)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            setToolbarBack();
                        }
                    });*/
        }
    }

    protected void setToolbarBack() {
        finish();
    }

    /*
     * 设置背景色
     */
    protected void setToolbarBackground(int color) {
        toolbar.setBackgroundColor(getResources().getColor(color));
    }

    /*
     * 设置背景透明度 0-100
     */
    protected void setToolbarBackgroundAlpha(int alpha) {
        toolbar.getBackground().setAlpha(alpha);
    }

    protected void setToolbarClickListener(View.OnClickListener listener) {
        /*if (back_btn != null) {
            back_btn.setVisibility(View.VISIBLE);
            back_btn.setOnClickListener(listener);
        } else {
            Log.e("ToolbarClickListener", "back is null ,please check out");
        }*/
        toolbar.setNavigationOnClickListener(listener);
    }

    protected void setToolbarLogo(int icon) {
        toolbar.setLogo(icon);//设置app logo R.drawable.icon
    }

    //设置主标题
    protected void setToolbarTitle(String title) {
        title_tv.setText(title);
        setToolbarTitlePosition("center");
    }

    protected void setToolbarTitleTextColor(int color) {
        if (title_tv != null) {
            title_tv.setTextColor(getResources().getColor(color));
        } else {
            Log.e("ToolbarTitleTextColor", "title_tv is null ,please check out");
        }
    }

    protected void setToolbarTitleTextSize(float size) {
        if (title_tv != null) {
            title_tv.setTextSize(size);
        }
    }

    protected void setToolbarTitlePosition(String position) {
        if (position.equals("left")) {
            title_tv.setGravity(Gravity.LEFT);
        } else if (position.equals("right")) {
            title_tv.setGravity(Gravity.RIGHT);
        } else {
            title_tv.setGravity(Gravity.CENTER);
        }
    }

    protected void setToolbarSubTitle(String sub_title) {
        title_sub_tv.setText(sub_title);
        title_sub_tv.setVisibility(View.VISIBLE);
    }

    protected void setToolbarSubTitleTextColor(int color) {
        if (title_sub_tv != null) {
            title_sub_tv.setTextColor(getResources().getColor(color));
        }
    }

    protected void setToolbarSubTitlePosition(String position) {
        if (position.equals("left")) {
            title_sub_tv.setGravity(Gravity.LEFT);
        } else if (position.equals("right")) {
            title_sub_tv.setGravity(Gravity.RIGHT);
        } else {
            title_sub_tv.setGravity(Gravity.CENTER);
        }
    }

    protected void setHasMenu(Boolean show) {
        this.hasMenu = show;
    }

    protected Boolean getHasMenu() {
        return this.hasMenu;
    }

    /**
     * 设置菜单layout
     */
    protected void setMenuLayout(int layout_id) {
        this.menu_layout = layout_id;
    }

    protected int getMenuLayout() {
        return this.menu_layout;
    }

    /*
     *  右侧菜单(可自定义菜单layout)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getHasMenu()) {
            getMenuInflater().inflate(getMenuLayout(), menu);
            return true;
        }

        return false;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(BaseEntity baseEntity) {

    }

    @Override
    public void onErrors(int flag, Throwable e) {

    }



}
