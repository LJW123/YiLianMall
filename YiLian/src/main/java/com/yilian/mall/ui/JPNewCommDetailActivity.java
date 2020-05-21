package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.OrderSubmitGoods;
import com.yilian.mall.http.InvateNetRequest;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.ShoppingCartNetRequest;
import com.yilian.mall.ui.fragment.JPCommDetailBottomFragment;
import com.yilian.mall.ui.fragment.JPCommDetailTopFragment;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.AliCustomerServiceChatUtils;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.DragLayout;
import com.yilian.mall.widgets.FlowLayout;
import com.yilian.mall.widgets.GoodView.GoodView;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.pay.PayFrom;
import com.yilian.networkingmodule.entity.GoodsSkuPrice;
import com.yilian.networkingmodule.entity.GoodsSkuProperty;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilian.mall.widgets.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author Created by Ray_L_Pain on 2016/11/1 0001.
 *         二次修改的商品详情
 */

public class JPNewCommDetailActivity extends BaseFragmentActivity {
    public static final int OPERATE_USER = 0;
    public static final int OPERATE_CART = 1;
    public static final int OPERATE_BUY = 2;
    public static final String SIMPLE_GOODS = "0";
    public static final String YHS_GOODS = "1";
    public static final String JFG_GOODS = "2";
    /**
     * 普通送益豆
     */
    public static final String LE_DOU_GOODS = "3";
    /**
     * 送益豆专区
     */
    public static final String LE_DOU_GOODS_2 = "4";
    /**
     * 触发属性选择框的用户操作，
     * {@link #OPERATE_USER}-用户手动打开选择框,
     * {@link #OPERATE_CART}-加入购物车，
     * {@link #OPERATE_BUY}-立即购买
     */
    public int mOperate;
    @ViewInject(R.id.draglayout)
    public DragLayout contentView;
    public ShoppingCartNetRequest cartNetRequest;
    public String goodsPrice, goodsCost, goodsReturnIntegral, goodsReturnBean, goodsIntegral, goodsRetailPrice;
    public float gavePower;
    /**
     * 头部
     */
    @ViewInject(R.id.layout_title)
    public RelativeLayout layoutTitle;
    @ViewInject(R.id.layout_title_bottom)
    public RelativeLayout layoutTitleBottom;
    /**
     * 底部
     */
    @ViewInject(R.id.tv_contact_customer_service)
    public TextView tvContactCustomerService;
    @ViewInject(R.id.tv_collect)
    public TextView tvCollect;
    public JPCommDetailTopFragment topFragment;
    public JPCommDetailBottomFragment bottomFragment;
    public String goodsId, filialeId, supplierId, urlOne, goodsType;
    //下滑加载哪一个页面
    public int which = 0;
    //pop框背后的阴影部分
    @ViewInject(R.id.view_shadow)
    public View mShadowView;
    /**
     * 下方的按钮
     */
    @ViewInject(R.id.layout_normal)
    public LinearLayout layoutNormal;
    @ViewInject(R.id.layout_question)
    public LinearLayout layoutQuestion;
    @ViewInject(R.id.tv_ask_question)
    public TextView tvAskQuestion;
    @ViewInject(R.id.et_question)
    public EditText etAskQuestion;
    public DisplayImageOptions options;
    //分享有关
    public String share_type; // 2.商品详情分享:#
    public PropertyAdapter propertyAdapter;
    public LocationAdapter locationAdapter;
    /**
     * 商品类别 0 普通商品 1yhs 2jfg 34区块益豆
     */
    public String type;
    /**
     * 该字段用于控制按钮显示和隐藏
     */
    public String from;
    public GoodView goodView;
    public float skuGavePower;//初始化，对应该SKU的平台加赠益豆
    /**
     * 内容
     */
    @ViewInject(R.id.layout_all)
    RelativeLayout rlContent;
    PopupWindow mPropertyWindow, mGuessResultWindow, mTouchResultWindow, mExplainWindow, mGuessWindow, mLocationWindow;
    TextView priceTv, costTv, juanTv, ledouTv, voucherTv, tv_price_jfg, tv_jifen_jfg;
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.iv_more)
    ImageView ivMore;
    @ViewInject(R.id.iv_cart)
    ImageView ivCart;
    @ViewInject(R.id.iv_back_bottom)
    ImageView ivBackBottom;
    @ViewInject(R.id.iv_more_bottom)
    ImageView ivMoreBottom;
    @ViewInject(R.id.iv_cart_bottom)
    ImageView ivCartBottom;
    @ViewInject(R.id.tv_put_cart)
    TextView tvPutCart;
    @ViewInject(R.id.tv_buy)
    TextView tvBuy;
    @ViewInject(R.id.menu_bottom)
    RelativeLayout rlMenuBottom;
    /**
     * 加载错误的View
     */
    @ViewInject(R.id.ll_error_view)
    LinearLayout llErrorView;
    @ViewInject(R.id.tv_update_error)
    TextView tvUpdate;
    String share_title, share_content, share_img, share_url, shareImg;
    private int mCount = 1;//选择商品的数量
    private UmengDialog mShareDialog;
    private InvateNetRequest request;
    private JPNetRequest jpNetRequest;
    private String inventory = "";
    private boolean hasInventory;
    private TextView repertoryTv;
    private TextView confirmTv;
    private LinearLayout repertoryLayout;
    private String skuCostPrice = "";//选择SKU之后，对应该SKU的价格
    private String skuReturnIntegral = "";//选择SKU之后，对应的销售奖券
    private String skuReturnBean = "";//选择SKU之后，对应的益豆
    private String skuMarketPrice = "";//选择SKU之后，对应该SKU的价格
    private String skuIntegral = "";//选择sku之后，对应的SKU奖券
    private String skuRetailPrice = ""; //选择sku之后，对应的市场价（type = 2 时 现金价格）
    private int allInventory;
    private String finalSKU = "";
    private String firstSKU = "";
    private String[] Str;
    private String firstStr;
    private String secondStr;
    private boolean isHandClick = true;
    private String customerServiceGroupId;
    private String customerServicePersonId;
    private View llGavePowerProperty;
    private TextView tvGavePowerProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_commdetail_jp);
        ViewUtils.inject(this);
        llErrorView.setVisibility(View.GONE);
        startMyDialog();
        topFragment = new JPCommDetailTopFragment();
        bottomFragment = new JPCommDetailBottomFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.view_first, topFragment)
                .add(R.id.view_second, bottomFragment)
                .commit();


        goodView = new GoodView(context);
        initContent();
        initTop();

//        getCustomerServiceInfo();
        initBottom();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) layoutTitle.getLayoutParams();
            titleParams.height += StatusBarUtils.getStatusBarHeight(context);
            layoutTitle.setPadding(20, StatusBarUtils.getStatusBarHeight(context), 20, 0);

            RelativeLayout.LayoutParams titleBottomParams = (RelativeLayout.LayoutParams) layoutTitleBottom.getLayoutParams();
            titleBottomParams.height += StatusBarUtils.getStatusBarHeight(context);
            layoutTitleBottom.setPadding(0, StatusBarUtils.getStatusBarHeight(context), 0, 0);

            StatusBarUtil.setTranslucentForImageViewInFragment(JPNewCommDetailActivity.this, 60, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
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

    private void initTop() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JPNewCommDetailActivity.this.finish();
            }
        });
        ivBackBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JPNewCommDetailActivity.this.finish();
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

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重新请求数据
                topFragment.getCommodityDetails();
                switchContentFragment();
            }
        });

        getShareUrl();
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
                            startActivity(new Intent(JPNewCommDetailActivity.this, InformationActivity.class));
                            return;
                        }
                        break;
                    case ITEM2:
                        Intent intentHome = new Intent(JPNewCommDetailActivity.this, JPMainActivity.class);
                        intentHome.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        startActivity(intentHome);
                        break;
                    case ITEM3:
                        shareGood();
                        break;
                    case ITEM4:
                        Intent intent = new Intent(JPNewCommDetailActivity.this, JPMainActivity.class);
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 分享商品
     */
    public void shareGood() {
        Logger.i("2018年1月4日 16:13:13-" + shareImg);
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(context, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            context,
                            ((IconModel) arg4).getType(),
                            share_title,
                            shareImg,
                            share_url).share();
                }
            });

        }
        mShareDialog.show();
    }

    private void initContent() {
        goodsId = getIntent().getStringExtra("goods_id");
        type = getIntent().getStringExtra("classify");
        filialeId = getIntent().getStringExtra("filiale_id");
        if (filialeId == null) {
            filialeId = "";
        }
        if (type == null) {
            type = "0";
        }
        from = getIntent().getStringExtra("from");
        if (from != null && "merchant".equals(from)) {
            rlMenuBottom.setVisibility(View.GONE);
            ivMore.setVisibility(View.GONE);
            ivCart.setVisibility(View.GONE);
            ivMoreBottom.setVisibility(View.GONE);
            ivCartBottom.setVisibility(View.GONE);
        }

        Logger.i("goods_id:" + goodsId);
        Logger.i("filiale_id:" + filialeId);
        Logger.i("type:" + type);

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
        contentView.setNextPageListener(next);

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
        contentView.setTopPageListener(top);
    }

    private void switchContentFragment() {

        topFragment.setOnUpLoadErorrListener(new JPCommDetailTopFragment.onUpLoadErrorListener() {
            @Override
            public void isError(boolean isError) {
                if (isError) {
                    Logger.i("是否显示加载错误的图片 " + isError);
                    contentView.setVisibility(View.GONE);
                    llErrorView.setVisibility(View.VISIBLE);
                    rlMenuBottom.setVisibility(View.GONE);
                } else {
                    contentView.setVisibility(View.VISIBLE);
                    llErrorView.setVisibility(View.GONE);
                    if (from != null && "merchant".equals(from)) {
                        rlMenuBottom.setVisibility(View.GONE);
                    } else {
                        rlMenuBottom.setVisibility(View.VISIBLE);
                    }
                }
                stopMyDialog();
            }
        });
    }

    /**
     * 获取分享链接
     */
    private void getShareUrl() {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(context);
        }
        switch (type) {
            case "1"://易划算
                share_type = "15";
                break;
            case "2"://奖券购
                share_type = "16";
                break;
            case LE_DOU_GOODS:
            case LE_DOU_GOODS_2:
            case GoodsType.CAL_POWER:
                share_type = "17";
                break;
            default:
                share_type = "2";
                break;
        }
        Logger.i("商品详情type:" + share_type);
        jpNetRequest.getShareUrl(share_type, goodsId, new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        share_title = responseInfo.result.title;
                        String sub_title = responseInfo.result.subTitle;
                        share_content = responseInfo.result.content;
                        share_img = responseInfo.result.imgUrl;
                        share_url = responseInfo.result.url;
                        if (TextUtils.isEmpty(share_img)) {
                            shareImg = "";
                        } else {
                            if (share_img.contains("http://") || share_img.contains("https://")) {
                                shareImg = share_img;
                            } else {
                                shareImg = Constants.ImageUrl + share_img;
                            }
                        }
                        Logger.i("share_title" + share_title);
                        Logger.i("share_content" + share_content);
                        Logger.i("share_img" + share_img);
                        Logger.i("share_url" + share_url);
                        Logger.i("shareImg:   " + shareImg);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
                finish();
            }
        });
    }

    private void initBottom() {
        tvContactCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AliCustomerServiceChatUtils.openServiceChatByActivity(context, customerServicePersonId, customerServiceGroupId, goodsId, filialeId);

            }
        });
        //点击收藏
        tvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin()) {
                    startActivity(new Intent(JPNewCommDetailActivity.this, LeFenPhoneLoginActivity.class));
                    return;
                }
                if (topFragment.isCollectTop) {
                    cancelCollectCommodity();
                } else {
                    collectCommodity();
                }
            }
        });
        //加入购物车
        tvPutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOperate = OPERATE_CART;
                showPropertyWindow();
                //putCart();
            }
        });
        //立即购买
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOperate = OPERATE_BUY;
                showPropertyWindow();
                //buyImmediately();
            }
        });
        //咨询问答提问
        tvAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etAskQuestion.getText().toString().trim())) {
                    showToast(R.string.please_write_question);
                    return;
                }
                if (jpNetRequest == null) {
                    jpNetRequest = new JPNetRequest(context);
                }
                jpNetRequest.askAndAnswer(goodsId, etAskQuestion.getText().toString().trim(), new RequestCallBack<BaseEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        startMyDialog();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                        stopMyDialog();
                        switch (responseInfo.result.code) {
                            case 1:
                                etAskQuestion.setText("");
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(etAskQuestion.getWindowToken(), 0);
                                bottomFragment.initListViewQuestion(true);
                                showToast(responseInfo.result.msg);
                                break;
                            default:
                                showToast(responseInfo.result.msg);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        stopMyDialog();
                    }
                });
            }
        });
    }
    //加入购物车/立即购买底部弹出框
    public void showPropertyWindow() {
        if (mPropertyWindow == null) {
            View contentView = getLayoutInflater().inflate(R.layout.good_property_select_layout_jp, null);
            mPropertyWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPropertyWindow.setContentView(contentView);
            mPropertyWindow.setAnimationStyle(R.style.AnimationSEX);
            repertoryTv = (TextView) contentView.findViewById(R.id.tv_repertory);
            priceTv = (TextView) contentView.findViewById(R.id.tv_price);
            llGavePowerProperty = contentView.findViewById(R.id.ll_gave_power_property);
            tvGavePowerProperty = contentView.findViewById(R.id.tv_gave_power_property);
            costTv = (TextView) contentView.findViewById(R.id.tv_cost);
            juanTv = (TextView) contentView.findViewById(R.id.tv_juan);
            ledouTv = (TextView) contentView.findViewById(R.id.tv_ledou);
            voucherTv = (TextView) contentView.findViewById(R.id.tv_voucher);
            TextView propertyTv = (TextView) contentView.findViewById(R.id.tv_select_property);
            ImageView imgClose = (ImageView) contentView.findViewById(R.id.imgView_close);
            ImageView imgIcon = (ImageView) contentView.findViewById(R.id.imgView_good_icon);
            final Button decreaseBtn = (Button) contentView.findViewById(R.id.btn_decrease);
            Button increaseBtn = (Button) contentView.findViewById(R.id.btn_increase);
            final TextView amountTv = (TextView) contentView.findViewById(R.id.tv_amount);
            amountTv.setText(String.valueOf(mCount));//对选择数量进行初始化赋值
            NoScrollListView propertyLv = (NoScrollListView) contentView.findViewById(R.id.lv_commodity_property);
            TextView cartTv = (TextView) contentView.findViewById(R.id.tv_put_cart);
            TextView buyTv = (TextView) contentView.findViewById(R.id.tv_buy);
            confirmTv = (TextView) contentView.findViewById(R.id.tv_confirm);
            View placeholderView = contentView.findViewById(R.id.view_placeholder);
            repertoryLayout = (LinearLayout) contentView.findViewById(R.id.ll_repertory);

            LinearLayout ll_jifen = (LinearLayout) contentView.findViewById(R.id.ll_jifen);
            tv_price_jfg = (TextView) contentView.findViewById(R.id.tv_price_jfg);
            tv_jifen_jfg = (TextView) contentView.findViewById(R.id.tv_jifen_jfg);

            LinearLayout addAndSubLayout = (LinearLayout) contentView.findViewById(R.id.layout_add_and_sub);

            costTv.setVisibility(View.VISIBLE);
            juanTv.setVisibility(View.VISIBLE);
            addAndSubLayout.setVisibility(View.VISIBLE);
            confirmTv.setText("确定");
            //根据商品是否有规格来判断库存是否显示
            if ("0:0".equals(topFragment.sku)) {
                propertyTv.setText(topFragment.noskuPropertyTxt(1));

                repertoryLayout.setVisibility(View.VISIBLE);
                repertoryTv.setText(topFragment.skuCount);
            } else {
                repertoryLayout.setVisibility(View.VISIBLE);

                //下面这两句是初始化商品选择规格和库存 应该为在adapter每次选择后取出id后的selectSkuList（如：2:2047,3:16）
                propertyTv.setText(topFragment.staticPropertyTxt(topFragment.selectSkuList));
                repertoryTv.setText(topFragment.skuCount);
            }
            if (from != null && "merchant".equals(from)) {
                confirmTv.setVisibility(View.GONE);
            } else {
                confirmTv.setVisibility(View.VISIBLE);
            }
            //这个拿到的是总的库存
            if ("0".equals(topFragment.skuCount)) {
                confirmTv.setText("暂时无货");
                confirmTv.setBackgroundColor(getResources().getColor(R.color.btn_unclick));
                confirmTv.setEnabled(false);
            } else {
                confirmTv.setText("确定");
                confirmTv.setBackgroundColor(getResources().getColor(R.color.myinfo_red_bg));
                confirmTv.setEnabled(true);
            }

            /**
             * 2017-2-8 Ray_L_Pain修改
             */
            if (topFragment != null && topFragment.sku != null) {
                firstSKU = topFragment.sku.toString();
                if (firstSKU.contains(",")) {
                    Str = firstSKU.split(",");
                    firstStr = Str[0];
                    secondStr = Str[1];
                }
            }

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.imgView_close:
                        case R.id.view_placeholder:
                            hidePropertyWindow();
                            break;
                        case R.id.btn_decrease: //-号
                            if (mCount == 1) {
                                decreaseBtn.setEnabled(false);
                            } else {
                                --mCount;
                                amountTv.setText(mCount + "");
                            }
                            if ("0:0".equals(topFragment.sku)) {
                                ((TextView) mPropertyWindow.getContentView().findViewById(R.id.tv_select_property)).setText(topFragment.noskuPropertyTxt(mCount));
                            } else {
                                ((TextView) mPropertyWindow.getContentView().findViewById(R.id.tv_select_property)).setText(topFragment.getPropertyTxt(mCount));
                            }
                            break;
                        case R.id.btn_increase: //+号
                            Logger.i("选择++库存数量   " + inventory);
                            if (mCount >= Integer.parseInt(inventory)) {
                                showToast("超出当前最大库存");
                                return;
                            }
                            decreaseBtn.setEnabled(true);
                            ++mCount;
                            amountTv.setText(mCount + "");
                            if ("0:0".equals(topFragment.sku)) {
                                ((TextView) mPropertyWindow.getContentView().findViewById(R.id.tv_select_property)).setText(topFragment.noskuPropertyTxt(mCount));
                            } else {
                                ((TextView) mPropertyWindow.getContentView().findViewById(R.id.tv_select_property)).setText(topFragment.getPropertyTxt(mCount));
                            }
                            break;
                        case R.id.tv_confirm:
                            Logger.i("最后的SKU点击：" + finalSKU);
                            /**
                             * 2017-2-8 Ray_L_Pain修改
                             */
                            Logger.i("初始的SKU：" + firstSKU);
                            if (finalSKU.contains("L")) {
//                                showToast("请选择完整属性");
//                                return;
                                if (finalSKU.startsWith("L")) { //第一个属性改变
                                    if (firstStr != null) {
                                        finalSKU = finalSKU.replace("L", firstStr);
                                    } else {
                                        break;
                                    }
                                } else { //第二个属性改变
                                    finalSKU = finalSKU.replace("L", secondStr);
                                }
                            }
                            Logger.i("最后的SKU点击2：" + finalSKU);

                            if ("0".equals(inventory) && !hasInventory) {
                                showToast("暂时无货");
                                return;
                            }

                            if (mOperate == OPERATE_CART) {
                                putCart();
                            } else if (mOperate == OPERATE_BUY) {
                                buyImmediately();
                            }
                            break;
                        default:
                            break;
                    }
                }
            };

            if (topFragment.goodInfo != null) {
                GlideUtil.showImageWithSuffix(context, topFragment.goodInfo.goodsAlbum.get(0), imgIcon);
                //规格
                //propertyTv.setText(topFragment.getPropertyTxt(mCount));
                Logger.i("规格改变1");

                if (LE_DOU_GOODS.equals(topFragment.regional_type)
                        || GoodsType.CAL_POWER.equals(topFragment.regional_type)) {
                    if (!TextUtils.isEmpty(skuCostPrice)) {
                        priceTv.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(skuCostPrice)));
                    } else {
                        priceTv.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(topFragment.skuCostPriceFirst)));
                    }
                    if (skuGavePower > 0) {
                        tvGavePowerProperty.setText("平台加赠"+MoneyUtil.getLeXiangBi(skuGavePower));
                        llGavePowerProperty.setVisibility(View.VISIBLE);
                    } else {
                        if (topFragment.skuGavePowerFirst > 0) {
                            tvGavePowerProperty.setText("平台加赠"+MoneyUtil.getLeXiangBi(topFragment.skuGavePowerFirst));
                            llGavePowerProperty.setVisibility(View.VISIBLE);
                        } else {
                            llGavePowerProperty.setVisibility(View.GONE);
                        }
                    }

                    if (!TextUtils.isEmpty(skuMarketPrice)) {
                        costTv.setText("¥" + MoneyUtil.getLeXiangBi(skuMarketPrice));
                    } else {
                        costTv.setText("¥" + MoneyUtil.getLeXiangBi(topFragment.skuMarketPriceFirst));
                    }
                    costTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    costTv.setVisibility(View.VISIBLE);

                    juanTv.setVisibility(View.GONE);
                    ledouTv.setVisibility(View.VISIBLE);
                    ledouTv.setText("送  " + MoneyUtil.getLeXiangBiNoZero(topFragment.skuReturnBeanFirst));
                } else if (LE_DOU_GOODS_2.equals(topFragment.regional_type)) {
                    if (!TextUtils.isEmpty(skuCostPrice)) {
                        priceTv.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(skuCostPrice)));
                    } else {
                        priceTv.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(topFragment.skuCostPriceFirst)));
                    }
                    if (skuGavePower > 0) {
                        tvGavePowerProperty.setText("平台加赠"+MoneyUtil.getLeXiangBi(skuGavePower));
                        llGavePowerProperty.setVisibility(View.VISIBLE);
                    } else {
                        llGavePowerProperty.setVisibility(View.GONE);
                    }
                    costTv.setVisibility(View.GONE);
                    juanTv.setVisibility(View.GONE);
                    ledouTv.setVisibility(View.VISIBLE);
                    ledouTv.setText("送  " + MoneyUtil.getLeXiangBiNoZero(topFragment.skuReturnBeanFirst));
                }

                voucherTv.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(inventory)) {
                    repertoryLayout.setVisibility(View.VISIBLE);
                    repertoryTv.setText(inventory);
                }
            }
            imgClose.setOnClickListener(clickListener);
            placeholderView.setOnClickListener(clickListener);
            decreaseBtn.setOnClickListener(clickListener);
            increaseBtn.setOnClickListener(clickListener);
            cartTv.setOnClickListener(clickListener);
            buyTv.setOnClickListener(clickListener);
            confirmTv.setOnClickListener(clickListener);
            if (propertyAdapter == null && topFragment.goodInfo != null) {
                propertyAdapter = new PropertyAdapter(topFragment.selectSkuList);
            }
            propertyLv.setAdapter(propertyAdapter);
        }

        if (!mPropertyWindow.isShowing()) {
            mPropertyWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            mShadowView.setVisibility(View.VISIBLE);
            mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_in));
            propertyAdapter.switchGoodsNormsData(firstSKU);
            Logger.i("selectSkuResultinventory显示" + inventory);
        } else {
            //重新给pop控件赋值
            if (topFragment == null) {
                return;
            }
            //库存

            if (hasInventory && !"0".equals(inventory)) {
                repertoryLayout.setVisibility(View.VISIBLE);
                repertoryTv.setText(inventory);
                Logger.i("selectSkuResultinventory显示" + inventory);
                confirmTv.setText("确定");
                confirmTv.setBackgroundColor(getResources().getColor(R.color.myinfo_red_bg));
                confirmTv.setEnabled(true);
            } else if ("0".equals(inventory)) {
                repertoryLayout.setVisibility(View.VISIBLE);
                repertoryTv.setText(inventory);
                confirmTv.setText("暂时无货");
                confirmTv.setBackgroundColor(getResources().getColor(R.color.btn_unclick));
                confirmTv.setEnabled(false);
                //根据规格的选择参数来判断是否要给全部的库存
            } else if (finalSKU.contains("L")) {
                repertoryLayout.setVisibility(View.VISIBLE);
                repertoryTv.setText("共" + allInventory);
                confirmTv.setBackgroundColor(getResources().getColor(R.color.myinfo_red_bg));
                confirmTv.setEnabled(true);
            }

            //更新价格
            if (hasInventory) { //有货
                if (LE_DOU_GOODS.equals(topFragment.regional_type)
                        || LE_DOU_GOODS_2.equals(topFragment.regional_type)
                        || GoodsType.CAL_POWER.equals(topFragment.regional_type)) {
                    priceTv.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(skuCostPrice)));
                    if (skuGavePower > 0) {
                        tvGavePowerProperty.setText("平台加赠"+MoneyUtil.getLeXiangBi(skuGavePower));
                        llGavePowerProperty.setVisibility(View.VISIBLE);
                    } else {
                        if (topFragment.skuGavePowerFirst > 0) {
                            tvGavePowerProperty.setText("平台加赠"+MoneyUtil.getLeXiangBi(topFragment.skuGavePowerFirst));
                            llGavePowerProperty.setVisibility(View.VISIBLE);
                        } else {
                            llGavePowerProperty.setVisibility(View.GONE);
                        }
                    }
                    costTv.setText("¥" + MoneyUtil.getLeXiangBi(skuMarketPrice));
                    costTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    juanTv.setVisibility(View.GONE);
                    ledouTv.setVisibility(View.VISIBLE);
                    ledouTv.setText("送  " + MoneyUtil.getLeXiangBiNoZero(skuReturnBean));
                    //Activity里的价格一起变化
                    topFragment.tv_container_price.setText(MoneyUtil.set¥Money(String.format("%.2f", NumberFormat.convertToDouble(skuCostPrice, 0.0) / 100)));
                    topFragment.tv_container_marker.setText("¥" + MoneyUtil.getLeXiangBi(skuMarketPrice));
                    topFragment.tv_container_marker.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    topFragment.tv_container_ledou.setText("送  " + MoneyUtil.getLeXiangBiNoZero(skuReturnBean));
                }
                voucherTv.setVisibility(View.GONE);
                repertoryTv.setText(inventory);
            }
        }
    }

    /**
     * 加入购物车
     */
    private void putCart() {
        if (cartNetRequest == null) {
            cartNetRequest = new ShoppingCartNetRequest(context);
        }
        if (isLogin()) {
            cartNetRequest.setToken(RequestOftenKey.getToken(context));
        } else {
            cartNetRequest.setToken("0");
        }
        if (topFragment.goodInfo == null) {
            return;
        }
        String goodSku = "";
        String goodsNorms = "";
        if (topFragment.goodInfo.goodsSkuProperty.size() == 0) {
            goodSku = "0:0";
            goodsNorms = "";
            goodsPrice = topFragment.skuMarketPriceFirst;
        } else {
            Logger.i("mSelectedProperty:" + topFragment.mSelectedProperty.size());
            Logger.i("data.goodInfo.goodsSkuProperty:" + topFragment.goodInfo.goodsSkuProperty.size());
            if (topFragment.mSelectedProperty.size() < topFragment.goodInfo.goodsSkuProperty.size()) {
                showPropertyWindow();
                showToast("请选择商品属性");
                return;
            }
            for (GoodsSkuProperty property : topFragment.goodInfo.goodsSkuProperty) {
                if (!TextUtils.isEmpty(goodSku)) {
                    goodSku += ";";
                }
                GoodsSkuProperty goodsSkuProperty = topFragment.mSelectedProperty.get(property.skuId);
                goodSku += property.skuId + ":" + goodsSkuProperty.skuId;
                goodsNorms += goodsSkuProperty.skuName;
            }
            for (GoodsSkuPrice skuPrice : topFragment.goodInfo.goodsSkuPrice) {
                String sku = skuPrice.skuInfo.replace(",", ";");
                if (sku.equals(goodSku)) {
                    if (topFragment.goodInfo.goodsType.equals(LE_DOU_GOODS)
                            || GoodsType.CAL_POWER.equals(topFragment.goodInfo.goodsType)) {
                        goodsPrice = skuPrice.skuIntegralPrice;
                    } else {
                        goodsPrice = skuPrice.skuMarketPrice;
                    }
                    //topFragment.tvContainerPrice.setText(goodsPrice);
                }
            }
        }

        cartNetRequest.addShoppingCart(topFragment.goodInfo.goodsId,
                mCount + "", goodSku, goodsNorms, topFragment.goodInfo.goodsRegion,
                topFragment.goodInfo.goodsFiliale, topFragment.goodInfo.goodsSupplier, topFragment.goodInfo.type,
                new RequestCallBack<BaseEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        startMyDialog();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                        stopMyDialog();
                        if (CommonUtils.NetworkRequestReturnCode(context, responseInfo.result.code + "")) {
                            showToast("加入购物车成功");
                            hidePropertyWindow();
                            sp.edit().putBoolean(Constants.REFRESH_SHOP_FRAGMENT, true).commit();
                        } else {
                            showToast("加入购物车失败：" + responseInfo.result.code);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        stopMyDialog();
                        showToast("加入购物车失败");
                    }
                });
    }

    /**
     * 立即购买
     */
    private void buyImmediately() {
        if (!isLogin()) {
            startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
            return;
        }
        if (topFragment.goodInfo != null) {
            int propertyCount = topFragment.goodInfo.goodsSkuProperty.size();

            if (propertyCount == 0 || topFragment.mSelectedProperty.size() == propertyCount) { //商品没有属性的时候 或是有属性但是已经选择过了属性
//                hidePropertyWindow();
                String goodSku = "";
                String goodsNorms = "";
                if (topFragment.goodInfo.goodsSkuProperty.size() == 0) {
                    goodSku = "0:0";
                    goodsPrice = topFragment.skuMarketPriceFirst;
                    goodsCost = topFragment.skuCostPriceFirst;
                    goodsReturnIntegral = topFragment.skuReturnIntegralFirst;
                    goodsReturnBean = topFragment.skuReturnBeanFirst;
                    goodsIntegral = topFragment.skuIntegralFirst;
                    goodsRetailPrice = topFragment.skuRetailPriceFirst;
                } else {
                    if (topFragment.mSelectedProperty.size() < topFragment.goodInfo.goodsSkuProperty.size()) {
//                        showPropertyWindow();
                        showToast("请选择商品属性");
                        return;
                    }
                    for (GoodsSkuProperty property : topFragment.goodInfo.goodsSkuProperty) {
                        if (!TextUtils.isEmpty(goodSku)) {
                            goodSku += ";";
                        }
                        goodSku += property.skuId + ":" + topFragment.mSelectedProperty.get(property.skuId).skuId;
                        goodsNorms += topFragment.mSelectedProperty.get(property.skuId).skuName + " ";
                    }
                }

                Intent intent = new Intent(context, JPCommitOrderActivity.class);

                ArrayList<OrderSubmitGoods> orderSubmitGoodses = new ArrayList<OrderSubmitGoods>();
                OrderSubmitGoods orderSubmitGoods = new OrderSubmitGoods();
                Logger.i(goodSku + "---" + goodsNorms);
                orderSubmitGoods.subsidy = topFragment.goodInfo.subsidy;
                orderSubmitGoods.goodsId = topFragment.goodInfo.goodsId;
                orderSubmitGoods.goodsType = Integer.parseInt(topFragment.goodInfo.goodsType);
                orderSubmitGoods.goodsName = topFragment.goodInfo.goodsName;
                orderSubmitGoods.name = topFragment.goodInfo.supplierInfo.supplierName;
                orderSubmitGoods.goodsPic = topFragment.goodInfo.goodsAlbum.get(0);
                for (GoodsSkuPrice skuPrice : topFragment.goodInfo.goodsSkuPrice) {
                    String sku = skuPrice.skuInfo.replace(",", ";");
                    if (sku.equals(goodSku)) {
                        goodsPrice = skuPrice.skuMarketPrice;
                        gavePower = skuPrice.subsidy;
                        goodsCost = skuPrice.skuCostPrice;
                        goodsReturnIntegral = skuPrice.returnIntegral;
                        goodsReturnBean = skuPrice.returnBean;
                        goodsIntegral = skuPrice.skuIntegralPrice;
                        skuRetailPrice = skuPrice.skuRetailPrice;
                        goodsRetailPrice = skuPrice.skuRetailPrice;
                    }
                }
                orderSubmitGoods.goodsPrice = NumberFormat.convertToDouble(goodsPrice, 0.00);
                orderSubmitGoods.goodsCost = NumberFormat.convertToDouble(goodsCost, 0.00);
                orderSubmitGoods.goodsReturnIntegral = NumberFormat.convertToDouble(goodsReturnIntegral, 0.00);
                orderSubmitGoods.goodsReturnBean = NumberFormat.convertToDouble(goodsReturnBean, 0.00);
                orderSubmitGoods.goodsNorms = goodSku;
                orderSubmitGoods.goodsProperty = goodsNorms;
                orderSubmitGoods.goodsCount = mCount;
                orderSubmitGoods.type = goodsType + "";
                orderSubmitGoods.filiale_id = filialeId;
                orderSubmitGoods.supplier_id = topFragment.goodInfo.goodsSupplier;
                orderSubmitGoods.region_id = topFragment.goodInfo.goodsRegion;
                orderSubmitGoods.goods_integral = NumberFormat.convertToDouble(goodsIntegral, 0.00);
                orderSubmitGoods.goods_type = topFragment.goodInfo.type;
                orderSubmitGoods.retailPrice = NumberFormat.convertToDouble(skuRetailPrice, 0.00);
                orderSubmitGoods.subsidy = gavePower;
                orderSubmitGoodses.add(orderSubmitGoods);
                Logger.i("2016-12-15orderSubmitGoods:" + orderSubmitGoods);
                intent.putExtra("OrderType", PayFrom.GOODS_DETAIL);
                intent.putExtra("orderSubmitGoods", orderSubmitGoodses);
                startActivity(intent);
                hidePropertyWindow();

            } else if (topFragment.mSelectedProperty.size() < propertyCount) { //选择的属性个数小于所有属性个数，就是属性没有选择全面
                showPropertyWindow();
                showToast("请选择商品属性");
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (topFragment.mFregihtChargeWindow != null && topFragment.mFregihtChargeWindow.isShowing()) {
            topFragment.mFregihtChargeWindow.dismiss();
            mShadowView.setVisibility(View.GONE);
            mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_out));
            return;
        }

        if (hidePropertyWindow()) {
            if ("0:0".equals(topFragment.sku)) {
                topFragment.tv_container_choose.setText(topFragment.noskuPropertyTxt(mCount));
            } else {
                topFragment.tv_container_choose.setText(topFragment.getPropertyTxt(mCount));
            }
            return;
        }

        super.onBackPressed();
    }

    /**
     * 隐藏属性选择窗口
     *
     * @return 成功进行窗口隐藏返回true, 失败或者窗口未显示返回false
     */
    public boolean hidePropertyWindow() {
        if (mPropertyWindow != null && mPropertyWindow.isShowing()) {
            if ("0:0".equals(topFragment.sku)) {
                topFragment.tv_container_choose.setText(topFragment.noskuPropertyTxt(mCount));
            } else {
                topFragment.tv_container_choose.setText(topFragment.getPropertyTxt(mCount));
            }
            mPropertyWindow.dismiss();
            mShadowView.setVisibility(View.GONE);
            mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_out));
            return true;
        }
        return false;
    }

    /**
     * 收藏
     */
    private void collectCommodity() {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(context);
        }
        String collectType = topFragment.goodInfo.type;
        if (LE_DOU_GOODS_2.equals(collectType)) {
            collectType = LE_DOU_GOODS;
        }
        tvCollect.setClickable(false);

        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .collectV3("goods_collect_add_v2", filialeId, goodsId, collectType, topFragment.collectType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        tvCollect.setClickable(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvCollect.setClickable(true);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean o) {
                        Drawable topDrawable = getResources().getDrawable(R.mipmap.star_solid);
                        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                        tvCollect.setCompoundDrawables(null, topDrawable, null, null);
                        tvCollect.setText(R.string.collected);
                        topFragment.isCollectTop = true;
                        showToast("收藏成功");
                        //刷新个人页面标识
                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                    }
                });
        addSubscription(subscription);
//        jpNetRequest.collectV3(goodsId, topFragment.collectType, filialeId, collectType, new RequestCallBack<BaseEntity>() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                tvCollect.setClickable(false);
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
//
//                Logger.i("code:" + responseInfo.result.code);
//                switch (responseInfo.result.code) {
//                    case 1:
//                        tvCollect.setClickable(true);
//                        Drawable topDrawable = getResources().getDrawable(R.mipmap.star_solid);
//                        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
//                        tvCollect.setCompoundDrawables(null, topDrawable, null, null);
//                        tvCollect.setText(R.string.collected);
//                        topFragment.isCollectTop = true;
//                        showToast("收藏成功");
//                        //刷新个人页面标识
//                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
//                        break;
//                    default:
//                        showToast("收藏返回码:" + responseInfo.result.code);
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                tvCollect.setClickable(true);
//                showToast("收藏失败");
//            }
//        });
    }

    /**
     * 取消收藏
     */
    private void cancelCollectCommodity() {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(context);
        }
        String collectType = topFragment.goodInfo.type;
        if (LE_DOU_GOODS_2.equals(collectType)) {
            collectType = LE_DOU_GOODS;
        }

        jpNetRequest.cancelCollectV3(goodsId, topFragment.collectType, filialeId, collectType, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                tvCollect.setClickable(false);
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                Logger.i("code:" + responseInfo.result.code);
                tvCollect.setClickable(true);
                Drawable topDrawable = getResources().getDrawable(R.mipmap.star_empty);
                topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                tvCollect.setCompoundDrawables(null, topDrawable, null, null);
                tvCollect.setText(R.string.click_collect);
                topFragment.isCollectTop = false;
                showToast("取消收藏成功");
                //刷新个人页面标识
                sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                tvCollect.setClickable(true);
                showToast("取消收藏失败");
            }
        });
    }

    /**
     * 选择规格的弹窗界面的adapter
     */
    class PropertyAdapter extends BaseAdapter {

        private List<GoodsSkuProperty> mPropertys = topFragment.goodInfo.goodsSkuProperty;
        private List<GoodsSkuProperty> mValues = topFragment.goodInfo.goodsSkuValues;
        private List<GoodsSkuPrice> mPrice = topFragment.goodInfo.goodsSkuPrice;
        private ArrayList<String> skuList = new ArrayList<>();
        private ArrayList<String> skuList2 = new ArrayList<>();
        private List<String> selectSkuList;

        public PropertyAdapter(List<String> selectSkuList) {
            this.selectSkuList = selectSkuList;
        }

        @Override
        public int getCount() {
            if (null == mPropertys || mPropertys.size() == 0) {
                return 0;
            } else {
                for (int i = 0, count = mPropertys.size(); i < count; i++) {
                    String s = "L";
                    skuList.add(s);
                }
                Logger.i("2017-2-7:" + skuList.size());
                Logger.i("2017-2-7:" + skuList.toString());
                return mPropertys.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mPropertys != null) {
                return mPropertys.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_commodity_property_list, null);
            final TextView tvPropertyName = (TextView) convertView.findViewById(R.id.tv_property_name);
            final FlowLayout layoutPropertyValue = (FlowLayout) convertView.findViewById(R.id.flayout_property_values);

            Logger.i("2016-12-15:" + mPropertys);
            Logger.i("2016-12-15:" + mValues);
            Logger.i("2016-12-15:" + mPrice);
            Logger.i("2016-12-15:" + selectSkuList.toString());

            final GoodsSkuProperty property = mPropertys.get(position);
            if (property != null) {
                tvPropertyName.setText(property.skuName);
                final List<GoodsSkuProperty> mValueFilter = new ArrayList<>();
                for (GoodsSkuProperty value : mValues) {
                    if (property.skuId == null || value == null) {
                        continue;
                    }
                    if (property.skuId.equals(value.skuParentId)) {
                        mValueFilter.add(value);
                    }

                    //未点击的情况走这里
                    if (isHandClick) {
                        if (selectSkuList.size() >= 1) {
                            for (int m = 0; m < selectSkuList.size(); m++) {
                                String select = selectSkuList.get(m);

                                String[] selectStr = select.split(":");
//                                Logger.i("2016-12-15[]:" + selectStr[0]);
//                                Logger.i("2016-12-15[]:" + selectStr[1]);
                                if (selectStr[0].equals(value.skuParentId) && selectStr[1].equals(value.skuId)) {
                                    topFragment.mSelectedProperty.put(selectStr[0], value);
                                    break;
                                }
                            }
                        } else {
                            String select = selectSkuList.get(0);
                            finalSKU = select;
                            if (select.equals(value.skuParentId) && select.equals(value.skuId)) {
                                topFragment.mSelectedProperty.put(select, value);
                            }
                        }
                        //把默认显示的SKU赋值给集合 未点击数据
                        //kuList.add(finalSKU);
                        Logger.i("2017-2-81" + finalSKU);
//                        switchGoodsNormsData(finalSKU);
                    }
                }
                for (int i = 0; i < mValueFilter.size(); i++) {
                    final GoodsSkuProperty value = mValueFilter.get(i);
                    final GoodsSkuPrice price = mPrice.get(i);
                    final TextView valueView = getValueView(value.skuName, value.skuParentId);
                    if (value == topFragment.mSelectedProperty.get(property.skuId)) {
                        valueView.setBackgroundResource(R.drawable.commodity_property_item_select_bg_jp);
                        valueView.setTextColor(getResources().getColor(R.color.white));
                        valueView.setTextSize(12);
                    }


                    valueView.setOnClickListener(new View.OnClickListener() {

                        private String sku;

                        @Override
                        public void onClick(View view) {
                            isHandClick = false;

                            //重新更新服务器返回的sku属性值

                            for (int j = 0; j < mValues.size(); j++) {
                                GoodsSkuProperty goodsSkuProperty = mValues.get(j);
                                if (valueView.getText().toString().equals(goodsSkuProperty.skuName)) {
                                    sku = goodsSkuProperty.skuParentId + ":" + goodsSkuProperty.skuId;
                                    Logger.i("2017-2-6:" + sku);
                                }
                            }
                            skuList.set(position, sku);//把选中的SKU值赋值到SKU列表里面
                            Logger.i("2017-2-82" + skuList.toString());
                            if (topFragment.mSelectedProperty.containsValue(value)) {
                                topFragment.mSelectedProperty.remove(property.skuId);
                                topFragment.mSelectedPropertyPrice.remove(property.skuId + ":" + topFragment.mSelectedProperty.get(property.skuId));

                                valueView.setEnabled(true);
                                valueView.setTextColor(getResources().getColor(R.color.white));
                                valueView.setTextSize(12);
                                //有默认显示的SKU所以不能根据L来判断点击没有
                                skuList.set(position, "L");
                            } else {
                                valueView.setEnabled(false);
                                valueView.setTextColor(Color.parseColor("#E0E0E0"));
                                valueView.setTextSize(12);
                                topFragment.mSelectedProperty.put(property.skuId, value);
                                topFragment.mSelectedPropertyPrice.put(property.skuId + ":" + topFragment.mSelectedProperty.get(property.skuId).skuId, price);
                            }

                            skuList2.clear();
                            for (int j = 0, count = mPropertys.size(); j < count; j++) {
                                String s = skuList.get(j);
                                skuList2.add(s);
                            }

                            Logger.i("2017-2-6sku211:" + skuList2.size());
                            Logger.i("2017-2-6sku21111:" + skuList2.toString());
                            finalSKU = "";
                            for (int s = 0, count = skuList2.size(); s < count; s++) {
                                if (s < count - 1) {
                                    finalSKU += skuList2.get(s) + ",";
                                } else {
                                    finalSKU += skuList2.get(s);
                                    Logger.i("2017-2-6sku211111:" + finalSKU);
                                }
                            }
                            if (finalSKU.contains("L")) {
                                if (!firstSKU.contains(",")) {
                                    return;
                                }
                                if (finalSKU.startsWith("L")) { //第一个属性改变
                                    finalSKU = finalSKU.replace("L", firstStr);
                                } else { //第二个属性改变
                                    finalSKU = finalSKU.replace("L", secondStr);
                                }
                            }
                            //更新过的sku赋值给之前发回显时的sku
                            firstSKU = finalSKU;
                            Logger.i("2017-2-6sku21111111111:" + finalSKU + " firstStr  " + firstStr);
                            switchGoodsNormsData(finalSKU);
                            Logger.i("循环SkuList2取出的finalSKU" + finalSKU);
                            ((TextView) mPropertyWindow.getContentView().findViewById(R.id.tv_select_property)).setText(topFragment.getPropertyTxt(mCount));

                            notifyDataSetChanged();
                        }
                    });
                    layoutPropertyValue.addView(valueView);
                }
            }
            return convertView;
        }

        private TextView getValueView(String name, String parentId) {
            TextView tv = new TextView(context);
            ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    CommonUtils.dip2px(context, 27));
            mlp.setMargins(0, CommonUtils.dip2px(context, 10F), CommonUtils.dip2px(context, 15F),
                    CommonUtils.dip2px(context, 10F));
            tv.setLayoutParams(mlp);
            tv.setTextSize(12);
            tv.setBackgroundResource(R.drawable.norms_btn_enable_bg);
            tv.setTextColor(getResources().getColor(R.color.color_h1));
            tv.setText(name);
            tv.setTag(parentId);
            tv.setBackgroundResource(R.drawable.commodity_property_item_bg);
            return tv;
        }

        private void switchGoodsNormsData(String selectSkuResult) {
            Logger.i("2017-2-6:" + "走了这里1");
            Logger.i("2017-2-8:" + selectSkuResult);
            // 拿到现在的sku去判断当前的服务器返回的sku是否相等 是否有货，
            if (selectSkuResult.contains("L")) { //如果包含“L” L是SKU的初始化字符串，这个时候说明SKU还没有选择完毕，不进行按钮能否点击的判断 也不进行库存的判断
                ((TextView) mPropertyWindow.getContentView().findViewById(R.id.tv_repertory)).setText(inventory);
                return;
            }
            allInventory = 0;
            if (!TextUtils.isEmpty(selectSkuResult)) {
                for (int i = 0; i < mPrice.size(); i++) {
                    String skuInfo = mPrice.get(i).skuInfo;
                    Logger.i("skuInfo" + skuInfo);
                    Logger.i("2017-2-6:" + skuInfo);
                    if (skuInfo.equals(selectSkuResult)) {
                        Logger.i("rng-" + "走了这里1");
                        //有货 取出当前的库存,还有价格
                        hasInventory = true;
                        inventory = mPrice.get(i).skuInventory;
                        skuCostPrice = mPrice.get(i).skuCostPrice;
                        skuGavePower = mPrice.get(i).subsidy;
                        skuReturnIntegral = mPrice.get(i).returnIntegral;
                        skuReturnBean = mPrice.get(i).returnBean;
                        skuMarketPrice = mPrice.get(i).skuMarketPrice;
                        skuIntegral = mPrice.get(i).skuIntegralPrice;
                        skuRetailPrice = mPrice.get(i).skuRetailPrice;
                        Logger.i("selectSkuResultinventory" + inventory);
                        Logger.i("2017-2-6:" + inventory);
                        break;
                    } else if (!skuInfo.contains(selectSkuResult)) {
                        Logger.i("rng-" + "走了这里2");
                        //不包含，没有库存没有对象直接给0
                        inventory = "0";
                        hasInventory = false;
                        Logger.i("2017-2-6:" + "走了这里2");
                    }
                }
                Logger.i("selectSkuResultinventory循环外" + inventory);
            } else if (!TextUtils.isEmpty(selectSkuResult) && finalSKU.contains("L")) {
                //取出全部的库存
                for (int i = 0; i < mPrice.size(); i++) {
                    allInventory += Integer.parseInt(mPrice.get(i).skuInventory);
                }
            }
            showPropertyWindow();
        }

        class ViewHolder {
            TextView tvPropertyName;
            FlowLayout layoutPropertyValue;
        }
    }
}
