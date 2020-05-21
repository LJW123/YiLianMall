package com.yilian.mall.jd.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.yilian.mall.jd.fragment.JDOrderListFragment;
import com.yilian.mall.jd.fragment.JdBrandSelectedFragment;
import com.yilian.mall.jd.fragment.JdGoodsClassifyFragment;
import com.yilian.mall.jd.fragment.JdHomePageFragment;
import com.yilian.mall.jd.fragment.JdShoppingCarFragment;
import com.yilian.mylibrary.AnimationController;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarEntity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Consumer;

/**
 * 京东首页
 *
 * @author Created by zhaiyaohua on 2018/5/22.
 */

public class JdHomePageActivity extends BaseFragmentActivity {
    public static final int COUNT_RETURN_TOP = 10;
    /**
     * 当前fragment的角标
     */
    public static final String FRAGMENT_INDEX = "index";
    public JdShoppingCarFragment jdShoppingCarFragment;
    public JdBrandSelectedFragment jdBrandSelectedFragment;
    public JdGoodsClassifyFragment jdGoodsClassifyFragment;
    public int titleHeight = 0;
    public TextView jdTvRight;
    private ImageView ivBack;
    private ImageView jdIvTitle;
    private TextView jdTvTitle;
    private FrameLayout frameLayoutContent;
    private FragmentManager mFragmentManager;
    private JDOrderListFragment jdOrderListFragment;
    private JdHomePageFragment jdHomePageFragment;
    private Fragment curFragment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RadioGroup jdRadioGroup;
    private RadioButton jdCurSelectedRadioButton;
    private int fragmentIndex = 0;
    private View jdHomePageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_home_page);
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
            titleHeight = jdHomePageTitle.getMeasuredHeight();
        }
    }


    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        jdHomePageTitle = findViewById(R.id.jd_home_page_title);
        jdTvTitle = findViewById(R.id.jd_tv_title);
        ivBack = findViewById(R.id.iv_back);
        jdIvTitle = findViewById(R.id.jd_iv_title);
        jdTvRight = findViewById(R.id.tv_jd_right);
        jdRadioGroup = (RadioGroup) findViewById(R.id.jd_radioGroup);
        initFragment();
        Intent intent = getIntent();
        if (null != intent) {
            fragmentIndex = intent.getIntExtra(FRAGMENT_INDEX, 0);
            String skuString = intent.getStringExtra("sku_string");
            if (!TextUtils.isEmpty(skuString)) {
                jdShoppingCarFragment.setAccountBeanList(skuString);
            }
        }
        switch (fragmentIndex) {
            case 0:
                jdCurSelectedRadioButton = findViewById(R.id.jd_radio_home_page);
                break;
            case 1:
                jdCurSelectedRadioButton = findViewById(R.id.jd_radio_classify);
                break;
            case 2:
                jdCurSelectedRadioButton = findViewById(R.id.jd_radio_brand_selected);
                break;
            case 3:
                jdCurSelectedRadioButton = findViewById(R.id.jd_radio_shopping_car);
                break;
            case 4:
                jdCurSelectedRadioButton = findViewById(R.id.jd_radio_order);
                break;
            default:
                break;

        }
        setCurRadioButtonSelectd(jdCurSelectedRadioButton.getId());
    }

    private void initListener() {
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                onBackPressed();
            }
        });
        jdRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.jd_radio_order == checkedId) {
                    if (!isLogin()) {
                        context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                        jdRadioGroup.check(jdCurSelectedRadioButton.getId());
                        return;
                    }
                }
                if (jdCurSelectedRadioButton.getId() != checkedId) {
                    initRadioButton(jdCurSelectedRadioButton.getId());
                    jdCurSelectedRadioButton.setTextColor(ContextCompat.getColor(context, R.color.color_333));
                    jdCurSelectedRadioButton = findViewById(checkedId);
                    AnimationController.tableBarAnimation(context, jdCurSelectedRadioButton);
                    setCurRadioButtonSelectd(checkedId);
                }
            }
        });
    }

    private void initFragment() {
        jdHomePageFragment = new JdHomePageFragment();
        curFragment = jdHomePageFragment;
        jdGoodsClassifyFragment = new JdGoodsClassifyFragment();
        jdBrandSelectedFragment = new JdBrandSelectedFragment();
        jdShoppingCarFragment = new JdShoppingCarFragment();
        jdOrderListFragment = new JDOrderListFragment();
        frameLayoutContent = (FrameLayout) findViewById(R.id.frame_layout_content);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.frame_layout_content, jdShoppingCarFragment);
        transaction.add(R.id.frame_layout_content, jdBrandSelectedFragment);
        transaction.add(R.id.frame_layout_content, jdOrderListFragment);
        transaction.add(R.id.frame_layout_content, jdGoodsClassifyFragment);
        transaction.add(R.id.frame_layout_content, jdHomePageFragment);
        transaction.hide(jdGoodsClassifyFragment);
        transaction.hide(jdBrandSelectedFragment);
        transaction.hide(jdShoppingCarFragment);
        transaction.hide(jdOrderListFragment);
        transaction.hide(jdHomePageFragment);
        transaction.commit();
    }

    private void initRadioButton(int checkedId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(curFragment);
        transaction.commit();
        switch (checkedId) {
            case R.id.jd_radio_shopping_car:
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_shopping_car), null, null);
                break;
            case R.id.jd_radio_classify:
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_classify), null, null);
                break;
            case R.id.jd_radio_order:
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_order), null, null);
                break;
            case R.id.jd_radio_home_page:
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_home), null, null);
                break;
            case R.id.jd_radio_brand_selected:
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_brand), null, null);
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
        if (fragment == jdOrderListFragment) {
            jdRadioGroup.check(R.id.jd_radio_order);
        } else if (fragment == jdBrandSelectedFragment) {
            jdRadioGroup.check(R.id.jd_radio_brand_selected);
        } else if (fragment == jdGoodsClassifyFragment) {
            jdRadioGroup.check(R.id.jd_radio_classify);
        } else if (fragment == jdHomePageFragment) {
            jdRadioGroup.check(R.id.jd_radio_home_page);
        } else if (fragment == jdShoppingCarFragment) {
            jdRadioGroup.check(R.id.jd_radio_shopping_car);
        }
    }

    private void setCurRadioButtonSelectd(int checkedId) {
        jdCurSelectedRadioButton.setTextColor(ContextCompat.getColor(context, R.color.red));
        switch (checkedId) {
            case R.id.jd_radio_classify:
                curFragment = jdGoodsClassifyFragment;
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_selected_classify), null, null);
                break;
            case R.id.jd_radio_order:
                curFragment = jdOrderListFragment;
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_selectd_order), null, null);
                break;
            case R.id.jd_radio_home_page:
                curFragment = jdHomePageFragment;
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_selected_home), null, null);
                break;
            case R.id.jd_radio_brand_selected:
                curFragment = jdBrandSelectedFragment;
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_selected_brand), null, null);
                break;
            case R.id.jd_radio_shopping_car:
                curFragment = jdShoppingCarFragment;
                jdCurSelectedRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.mipmap.jd_tab_selected_shopping_car), null, null);
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
        if (curFragment == jdGoodsClassifyFragment) {
            jdIvTitle.setVisibility(View.GONE);
            jdTvTitle.setText("分类");
            jdTvTitle.setVisibility(View.VISIBLE);
        } else if (curFragment == jdOrderListFragment) {
            ivBack.setVisibility(View.VISIBLE);
            jdIvTitle.setVisibility(View.GONE);
            jdTvTitle.setText("京东订单");
            jdTvTitle.setVisibility(View.VISIBLE);
        } else if (curFragment == jdBrandSelectedFragment) {
            ivBack.setVisibility(View.VISIBLE);
            jdIvTitle.setVisibility(View.VISIBLE);
            jdIvTitle.setImageResource(R.mipmap.jd_icon_title_brand_seleted);
            jdTvTitle.setVisibility(View.GONE);
        } else if (curFragment == jdHomePageFragment) {
            jdIvTitle.setVisibility(View.VISIBLE);
            jdIvTitle.setImageResource(R.mipmap.jd_icon_home_title);
            jdTvTitle.setVisibility(View.GONE);
        } else if (curFragment == jdShoppingCarFragment) {
            jdIvTitle.setVisibility(View.GONE);
            jdTvTitle.setVisibility(View.VISIBLE);
            jdTvTitle.setText("购物车");
            ivBack.setVisibility(View.GONE);
        }
    }

    public void setJdTvRightMark(boolean isEdit) {
        if (isEdit) {
            jdTvRight.setText("编辑");
        } else {
            jdTvRight.setText("完成");
        }
    }

    @Override
    public void onBackPressed() {
        if (null != jdShoppingCarFragment) {
            if (jdShoppingCarFragment.hidePop()) {
                return;
            }
        }
        if (null != curFragment && curFragment != jdHomePageFragment) {
            jdRadioGroup.check(R.id.jd_radio_home_page);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra(FRAGMENT_INDEX, 0);
        String skuString = intent.getStringExtra("sku_string");
        switch (index) {
            case 3:
                if (!TextUtils.isEmpty(skuString)) {
                    jdShoppingCarFragment.setAccountBeanList(skuString);
                    jdShoppingCarFragment.getShoppingCarData();
                    /**
                     * {@link com.yilian.mall.jd.fragment.JdShoppingCarFragment#updateShoppingCarData(JdShoppingCarEntity entity)}
                     */
                    EventBus.getDefault().post(new JdShoppingCarEntity());
                }
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
                jdRadioGroup.check(R.id.jd_radio_home_page);
                break;
            case 1:
                jdRadioGroup.check(R.id.jd_radio_classify);
                break;
            case 2:
                jdRadioGroup.check(R.id.jd_radio_brand_selected);
                break;
            case 3:
                jdRadioGroup.check(R.id.jd_radio_shopping_car);
                break;
            case 4:
                jdRadioGroup.check(R.id.jd_radio_order);
                break;
            default:
                break;
        }
    }
}
