package com.yilian.mall.shoppingcard.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.shoppingcard.fragment.CardCommodityButtomPageFragment;
import com.yilian.mall.shoppingcard.fragment.CardCommodityTopPageFragment;
import com.yilian.mall.ui.InformationActivity;
import com.yilian.mall.ui.JPMainActivity;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.DragLayout;
import com.yilian.mall.widgets.GoodView.GoodView;
import com.yilian.mall.widgets.PopupMenu;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.StatusBarUtils;

/**
 * @auther 马铁超 on 2018/11/21 14:48
 * 购物卡商品详情
 */

public class CardCommodityDetailActivity extends BaseFragmentActivity implements View.OnClickListener {

    private FrameLayout viewFirst;
    private FrameLayout viewSecond;
    private DragLayout draglayout;
    private ImageView ivBack;
    private ImageView ivMore;
    private ImageView ivCart;
    private RelativeLayout layoutTitle;
    private ImageView ivBackBottom;
    private ImageView ivMoreBottom;
    private ImageView ivCartBottom;
    private RelativeLayout layoutTitleBottom;
    private View viewShadow;
    private TextView tvUpdateError;
    private LinearLayout llErrorView;
    private RelativeLayout layoutAll;
    private TextView tvContactCustomerService;
    private TextView tvCollect;
    private TextView tvPutCart;
    private TextView tvActBuy;
    private TextView tvBuy;
    private TextView tvGuess;
    public LinearLayout layoutNormal;
    private EditText etQuestion;
    private TextView tvAskQuestion;
    public LinearLayout layoutQuestion;
    private RelativeLayout menuBottom;
    public CardCommodityTopPageFragment topFragment;
    public CardCommodityButtomPageFragment bottomFragment;
    public DisplayImageOptions options;
    public GoodView goodView;
    /**
     * 该字段用于控制按钮显示和隐藏
     */
    public String from,goodsId;
    /**
     * 下滑加载哪一个页面
     */
    public int which = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_commdetail_jp);
        initView();
        getIntentData();
        initTitle();
        initFragment();
        initData();
    }

    private void getIntentData() {
        goodsId = getIntent().getStringExtra("goods_id");
    }

    private void initFragment() {
        topFragment = new CardCommodityTopPageFragment();
        bottomFragment = new CardCommodityButtomPageFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.view_first, topFragment)
                .add(R.id.view_second, bottomFragment)
                .commit();
    }

    private void initTitle() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) layoutTitle.getLayoutParams();
            titleParams.height += StatusBarUtils.getStatusBarHeight(context);
            layoutTitle.setPadding(20, StatusBarUtils.getStatusBarHeight(context), 20, 0);
            RelativeLayout.LayoutParams titleBottomParams = (RelativeLayout.LayoutParams) layoutTitleBottom.getLayoutParams();
            titleBottomParams.height += StatusBarUtils.getStatusBarHeight(context);
            layoutTitleBottom.setPadding(0, StatusBarUtils.getStatusBarHeight(context), 0, 0);
            StatusBarUtil.setTranslucentForImageViewInFragment(CardCommodityDetailActivity.this, 60, null);
        }
    }


    private void initData() {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.login_module_default_jp)
                .showImageForEmptyUri(R.mipmap.login_module_default_jp)
                .showImageOnFail(R.mipmap.login_module_default_jp)
                // 这里的三张状态用一张替代
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();
        switchContentFragment();

        DragLayout.ShowNextPageNotifier next = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                bottomFragment.initView(which);
                layoutTitle.setVisibility(View.GONE);
                layoutTitleBottom.setVisibility(View.VISIBLE);
            }
        };
        draglayout.setNextPageListener(next);

        DragLayout.ShowTopPageNotifier top = new DragLayout.ShowTopPageNotifier() {
            @Override
            public void onDragTop() {
                layoutTitle.setVisibility(View.VISIBLE);
                layoutTitleBottom.setVisibility(View.GONE);

                if (layoutNormal.getVisibility() == View.GONE) {
                    layoutNormal.setVisibility(View.VISIBLE);
                }
                if (layoutQuestion.getVisibility() == View.VISIBLE) {
                    layoutQuestion.setVisibility(View.GONE);
                }
            }
        };
        draglayout.setTopPageListener(top);
    }


    private void initView() {
        viewFirst = (FrameLayout) findViewById(R.id.view_first);
        viewSecond = (FrameLayout) findViewById(R.id.view_second);
        draglayout = (DragLayout) findViewById(R.id.draglayout);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivMore = (ImageView) findViewById(R.id.iv_more);
        ivCart = (ImageView) findViewById(R.id.iv_cart);
        ivCart.setImageResource(R.mipmap.icon_card_shopping_inventory);
        layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        ivBackBottom = (ImageView) findViewById(R.id.iv_back_bottom);
        ivMoreBottom = (ImageView) findViewById(R.id.iv_more_bottom);
        ivCartBottom = (ImageView) findViewById(R.id.iv_cart_bottom);
        layoutTitleBottom = (RelativeLayout) findViewById(R.id.layout_title_bottom);
        viewShadow = (View) findViewById(R.id.view_shadow);
        tvUpdateError = (TextView) findViewById(R.id.tv_update_error);
        llErrorView = (LinearLayout) findViewById(R.id.ll_error_view);
        layoutAll = (RelativeLayout) findViewById(R.id.layout_all);
        tvContactCustomerService = (TextView) findViewById(R.id.tv_contact_customer_service);
        tvCollect = (TextView) findViewById(R.id.tv_collect);
        tvPutCart = (TextView) findViewById(R.id.tv_put_cart);
        tvActBuy = (TextView) findViewById(R.id.tv_act_buy);
        tvBuy = (TextView) findViewById(R.id.tv_buy);
        tvGuess = (TextView) findViewById(R.id.tv_guess);
        layoutNormal = (LinearLayout) findViewById(R.id.layout_normal);
        etQuestion = (EditText) findViewById(R.id.et_question);
        tvAskQuestion = (TextView) findViewById(R.id.tv_ask_question);
        layoutQuestion = (LinearLayout) findViewById(R.id.layout_question);
        menuBottom = (RelativeLayout) findViewById(R.id.menu_bottom);
        llErrorView.setVisibility(View.GONE);
        goodView = new GoodView(context);

        tvUpdateError.setOnClickListener(this);
    }


    //当详情上半部分页面加载失败回调监听
    private void switchContentFragment() {
        topFragment.setOnUpLoadErorrListener(new CardCommodityTopPageFragment.onUpLoadErrorListener() {
            @Override
            public void isError(boolean isError) {
                if (isError) {
                    Logger.i("是否显示加载错误的图片 " + isError);
                    draglayout.setVisibility(View.GONE);
                    llErrorView.setVisibility(View.VISIBLE);
                    menuBottom.setVisibility(View.GONE);
                } else {
                    draglayout.setVisibility(View.VISIBLE);
                    llErrorView.setVisibility(View.GONE);
                    if (from != null && "merchant".equals(from)) {
                        menuBottom.setVisibility(View.GONE);
                    } else {
                        menuBottom.setVisibility(View.VISIBLE);
                    }
                }
                stopMyDialog();
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update_error:

                break;
        }
    }

    private void initTopListenter() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardCommodityDetailActivity.this.finish();
            }
        });
        ivBackBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardCommodityDetailActivity.this.finish();
            }
        });
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                startActivity(intent);
            }
        });
        ivCartBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                startActivity(intent);
            }
        });
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuShow();
            }
        });
        ivMoreBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuShow();
            }
        });

    }

    /**
     * 右上角更多
     */
    private void menuShow() {
        final PopupMenu popupMenu = new PopupMenu(this);
        popupMenu.showLocation(R.id.iv_more);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                switch (item) {
                    case ITEM1:
                        if (!isLogin()) {
                            startActivity(new Intent(CardCommodityDetailActivity.this, InformationActivity.class));
                            return;
                        }
                        break;
                    case ITEM2:
                        Intent intentHome = new Intent(CardCommodityDetailActivity.this, JPMainActivity.class);
                        intentHome.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        startActivity(intentHome);
                        break;
                    case ITEM3:


                        break;
                    case ITEM4:
                        Intent intent = new Intent(CardCommodityDetailActivity.this, JPMainActivity.class);
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void submit() {
        String question = etQuestion.getText().toString().trim();
        if (TextUtils.isEmpty(question)) {
            Toast.makeText(this, "向商家提问，50字以内", Toast.LENGTH_SHORT).show();
            return;
        }
    }


}
