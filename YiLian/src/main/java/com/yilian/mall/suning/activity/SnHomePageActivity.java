package com.yilian.mall.suning.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.suning.fragment.SnBrandSelectedFragment;
import com.yilian.mall.suning.fragment.SnGoodsClassifyFragment;
import com.yilian.mall.suning.fragment.SnHomePageFragment;
import com.yilian.mall.suning.fragment.SnOrderListFragment;
import com.yilian.mall.suning.fragment.SnShoppingCarFragment;
import com.yilian.mylibrary.AnimationController;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.suning.SnShoppingCarEntity;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.functions.Consumer;


/**
 * 苏宁首页
 *
 * @author Created by zhaiyaohua on 2018/7/14.
 */

public class SnHomePageActivity extends BaseFragmentActivity {
    public static final int COUNT_RETURN_TOP = 10;

    /**
     * 苏宁首页底部tab首页角标
     */
    public static final int TAB_HOME = 0;

    /**
     * 苏宁首页底部tab分类角标
     */
    public static final int TAB_CLASSIFY = 1;
    /**
     * 苏宁首页底部tab品牌精选角标
     */
    public static final int TAB_BRAND_SELECTED = 2;
    /**
     * 苏宁首页底部tab购物车角标
     */
    public static final int TAB_CART = 3;

    /**
     * 苏宁首页底部tab  订单
     */
    public static final int TAB_ORDER = 4;
    /**
     * 当前fragment的角标
     */
    public static final String FRAGMENT_INDEX = "index";
    public com.yilian.mall.suning.fragment.SnShoppingCarFragment SnShoppingCarFragment;
    public SnBrandSelectedFragment snBrandSelectedFragment;
    public SnGoodsClassifyFragment snGoodsClassifyFragment;
    public int titleHeight = 0;
    public TextView snTvRight;
    private ImageView ivBack;
    private ImageView snIvTitle;
    private TextView snTvTitle;
    private FrameLayout frameLayoutContent;
    private FragmentManager mFragmentManager;
    private SnOrderListFragment snOrderListFragment;
    private SnHomePageFragment snHomePageFragment;
    private Fragment curFragment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RadioGroup snRadioGroup;
    private RadioButton snCurSelectedRadioButton;
    private int fragmentIndex = 0;
    private View snHomePageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_activity_home_page);
        initView();
        initListener();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getJdHomePageTitleHeight();
    }

    /**
     * 获取首页title高度
     */
    public void getJdHomePageTitleHeight() {
        if (titleHeight == 0) {
            titleHeight = snHomePageTitle.getMeasuredHeight();
        }
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        snHomePageTitle = findViewById(R.id.sn_home_page_title);
        snTvTitle = findViewById(R.id.sn_tv_title);
        ivBack = findViewById(R.id.iv_back);
        snIvTitle = findViewById(R.id.sn_iv_title);
        snTvRight = findViewById(R.id.tv_sn_right);
        snRadioGroup = (RadioGroup) findViewById(R.id.sn_radioGroup);

        initFragment();
        /**
         Intent intent = getIntent();
         if (null != intent) {
         fragmentIndex = intent.getIntExtra(FRAGMENT_INDEX, 0);
         String skuString = intent.getStringExtra("sku_string");
         if (!TextUtils.isEmpty(skuString)) {
         SnShoppingCarFragment.setAccountBeanList(skuString);
         }
         }
         */

        switch (fragmentIndex) {
            case 0:
                snCurSelectedRadioButton = findViewById(R.id.sn_radio_home_page);
                break;
            case 1:
                snCurSelectedRadioButton = findViewById(R.id.sn_radio_classify);
                break;
            case 2:
                snCurSelectedRadioButton = findViewById(R.id.sn_radio_brand_selected);
                break;
            case 3:
                snCurSelectedRadioButton = findViewById(R.id.sn_radio_shopping_car);
                break;
            case 4:
                snCurSelectedRadioButton = findViewById(R.id.sn_radio_order);
                break;
            default:
                break;

        }
        setCurRadioButtonSelectd(snCurSelectedRadioButton.getId());
    }

    private void initListener() {
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                //按返回键当前非主页时展示主页，否则finish
                onBackPressed();
            }
        });
        snRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //点击我的订单进行登录判断
                if (R.id.sn_radio_order == checkedId) {
                    if (!isLogin()) {
                        context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                        snRadioGroup.check(snCurSelectedRadioButton.getId());
                        return;
                    }
                }
                if (snCurSelectedRadioButton.getId() != checkedId) {
                    initRadioButton(snCurSelectedRadioButton.getId());
                    snCurSelectedRadioButton.setTextColor(ContextCompat.getColor(context, R.color.color_333));
                    snCurSelectedRadioButton = findViewById(checkedId);
                    AnimationController.tableBarAnimation(context, snCurSelectedRadioButton);
                    setCurRadioButtonSelectd(checkedId);
                }
            }
        });
    }

    private void initFragment() {
        snHomePageFragment = new SnHomePageFragment();
        curFragment = snHomePageFragment;
        snGoodsClassifyFragment = new SnGoodsClassifyFragment();
        snBrandSelectedFragment = new SnBrandSelectedFragment();
        SnShoppingCarFragment = new SnShoppingCarFragment();
        snOrderListFragment = new SnOrderListFragment();
        frameLayoutContent = (FrameLayout) findViewById(R.id.frame_layout_content);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.frame_layout_content, SnShoppingCarFragment);
        transaction.add(R.id.frame_layout_content, snBrandSelectedFragment);
        transaction.add(R.id.frame_layout_content, snOrderListFragment);
        transaction.add(R.id.frame_layout_content, snGoodsClassifyFragment);
        transaction.add(R.id.frame_layout_content, snHomePageFragment);
        transaction.hide(snGoodsClassifyFragment);
        transaction.hide(snBrandSelectedFragment);
        transaction.hide(SnShoppingCarFragment);
        transaction.hide(snOrderListFragment);
        transaction.hide(snHomePageFragment);
        transaction.commit();
    }

    private void initRadioButton(int checkedId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(curFragment);
        transaction.commit();
        switch (checkedId) {
            case R.id.sn_radio_shopping_car:
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_shopping_car), null, null);
                break;
            case R.id.sn_radio_classify:
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_classify), null, null);
                break;
            case R.id.sn_radio_order:
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_order), null, null);
                break;
            case R.id.sn_radio_home_page:
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_home_page), null, null);
                break;
            case R.id.sn_radio_brand_selected:
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_brand), null, null);
                break;
            default:
                break;
        }
    }

    private void setCurRadioButtonSelectd(int checkedId) {
        snCurSelectedRadioButton.setTextColor(ContextCompat.getColor(context, R.color.red));
        switch (checkedId) {
            case R.id.sn_radio_classify:
                curFragment = snGoodsClassifyFragment;
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_selected_classify), null, null);
                break;
            case R.id.sn_radio_order:
                curFragment = snOrderListFragment;
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_selected_order), null, null);
                break;
            case R.id.sn_radio_home_page:
                curFragment = snHomePageFragment;
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_selected_home_page), null, null);
                break;
            case R.id.sn_radio_brand_selected:
                curFragment = snBrandSelectedFragment;
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_selectd_brand), null, null);
                break;
            case R.id.sn_radio_shopping_car:
                curFragment = SnShoppingCarFragment;
                snCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.sn_tab_selected_shopping_car), null, null);
                break;
            default:
                break;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.show(curFragment);
        transaction.commit();
        setCurFragmentTitle();
    }

    /**
     * 设置当前fragment的title
     */
    private void setCurFragmentTitle() {
        ivBack.setVisibility(View.VISIBLE);
        if (curFragment == snGoodsClassifyFragment) {
            snIvTitle.setVisibility(View.GONE);
            snTvTitle.setText("分类");
            snTvTitle.setVisibility(View.VISIBLE);
        } else if (curFragment == snOrderListFragment) {
            ivBack.setVisibility(View.VISIBLE);
            snIvTitle.setVisibility(View.GONE);
            snTvTitle.setText("我的订单");
            snTvTitle.setVisibility(View.VISIBLE);
        } else if (curFragment == snBrandSelectedFragment) {
            ivBack.setVisibility(View.VISIBLE);
            snIvTitle.setVisibility(View.VISIBLE);
            snIvTitle.setImageResource(R.mipmap.sn_brand_selectd_title);
            snTvTitle.setVisibility(View.GONE);
        } else if (curFragment == snHomePageFragment) {
            snIvTitle.setVisibility(View.GONE);
            snTvTitle.setVisibility(View.VISIBLE);
            snTvTitle.setText("苏宁易购");
        } else if (curFragment == SnShoppingCarFragment) {
            snIvTitle.setVisibility(View.GONE);
            snTvTitle.setVisibility(View.VISIBLE);
            snTvTitle.setText("购物车");
            ivBack.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        //购物车隐藏弹窗
        if (null != SnShoppingCarFragment) {
            if (SnShoppingCarFragment.hidePop()) {
                return;
            }
        }
        if (null != curFragment && curFragment != snHomePageFragment) {
            snRadioGroup.check(R.id.sn_radio_home_page);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 当前activity跳转当前activity时执行此方法
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra(FRAGMENT_INDEX, 0);
        String skuString = intent.getStringExtra("sku_string");
        switch (index) {
            case TAB_CART:
                if (!TextUtils.isEmpty(skuString)) {
                    SnShoppingCarFragment.setAccountBeanList(skuString);
                    SnShoppingCarFragment.getShoppingCarData();
                    /**
                     * {@link SnShoppingCarFragment#updateShoppingCarData(SnShoppingCarEntity entity)}
                     */
                    EventBus.getDefault().post(new SnShoppingCarEntity());
                }
                break;
            case TAB_ORDER:
                snOrderListFragment.setCurrentTab(5);
                break;
            default:
                break;
        }
        switchFragment(index);
    }

    /**
     * 该页面跳转使用
     *
     * @param index
     */
    public void switchFragment(int index) {
        switch (index) {
            case 0:
                snRadioGroup.check(R.id.sn_radio_home_page);
                break;
            case 1:
                snRadioGroup.check(R.id.sn_radio_classify);
                break;
            case 2:
                snRadioGroup.check(R.id.sn_radio_brand_selected);
                break;
            case 3:
                snRadioGroup.check(R.id.sn_radio_shopping_car);
                break;
            case 4:
                snRadioGroup.check(R.id.sn_radio_order);
                break;
            default:
                break;
        }
    }

    /**
     * 切换fragment
     *
     * @param fragment
     */
    public void switchFragment(Fragment fragment) {
        if (fragment == snOrderListFragment) {
            snRadioGroup.check(R.id.sn_radio_order);
        } else if (fragment == snBrandSelectedFragment) {
            snRadioGroup.check(R.id.sn_radio_brand_selected);
        } else if (fragment == snGoodsClassifyFragment) {
            snRadioGroup.check(R.id.sn_radio_classify);
        } else if (fragment == snHomePageFragment) {
            snRadioGroup.check(R.id.sn_radio_home_page);
        } else if (fragment == SnShoppingCarFragment) {
            snRadioGroup.check(R.id.sn_radio_shopping_car);
        }
    }

    public void setSnTvRightMark(boolean isEdit) {
        if (isEdit) {
            snTvRight.setText("编辑");
        } else {
            snTvRight.setText("完成");
        }
    }

    /**
     * @author Created by Zg on 2018/7/26.
     */
    @IntDef({TAB_HOME, TAB_CLASSIFY, TAB_BRAND_SELECTED, TAB_CART,TAB_ORDER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SnHomeTab {
        public class TAB_BRAND_SELECTED {
        }
    }
}
