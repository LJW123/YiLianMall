package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.OrderSubmitGoods;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.listener.onLoadErrorListener;
import com.yilian.mall.ui.fragment.SpellGroupDetailsBottomFragment;
import com.yilian.mall.ui.fragment.SpellGroupDetailsTopFragment;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.DragLayout;
import com.yilian.mall.widgets.FlowLayout;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mall.widgets.PopupMenu;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.SpellGroupDetailsEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 拼团详情
 */
public class SpellGroupDetailsActivity extends BaseFragmentActivity implements View.OnClickListener {
    public static final int BUY_OPEN_GROUP = 1;
    private FrameLayout bottomContent;
    private ImageView ivBackTop;
    private ImageView ivMoreTop;
    private ImageView ivShopTop;
    private RelativeLayout rlBtnTop;
    private LinearLayout rlBtnBottom;
    private ImageView ivMySellGroup;
    private TextView tvPrice;
    private TextView tvCost;
    private TextView tvHasCount;
    private LinearLayout llErrorView;
    private TextView tvRefresh;
    private SpellGroupDetailsTopFragment topFragment;
    private SpellGroupDetailsBottomFragment bottomFragment;
    private DragLayout dragLayout;
    public String goodsInfoUrl, goodsId, filialeId;
    public int which = 0;//默认下滑到第一个界面
    private LinearLayout llNowBuy;
    private LinearLayout llOpenGroup;
    private LinearLayout llBottom;
    private String shareContent;
    private String shareImagUrl;
    private String shareTitle;
    private String shareUrl;
    private UmengDialog umengDialog;
    private String goodsPrice;
    private String amount;
    private PopupWindow mPropertyWindow;
    private int allInventory;
    private String finalSKU = "";
    private String firstSKU = "";
    private String skuStr = "";
    private String[] Str;
    private String firstStr;
    private String secondStr;
    public PropertyAdapter propertyAdapter;
    private boolean isHandClick = true;
    private boolean isNothing;
    private View mShadowView;
    private int mCount, mOperate;
    private boolean hasInventory;
    private String skuIntegralPrice, skuMarketPrice, skuIntegralMoney, inventory;
    private LinearLayout repertoryLayout;
    private TextView repertoryTv;

    private TextView confirmTv;
    private TextView costTv;
    private TextView priceTv;
    public String index;
    private boolean isJoinOpenGroup = true;
    public String goodsIndex;
    private String[] split;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_group_details);
        index = getIntent().getStringExtra("index");
        mCount = 1;//默认
        initView();
        initFragment();
        getShareUrl();
    }


    private void initViewData() {
        llErrorView.setVisibility(View.GONE);
        dragLayout.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
        goodsInfoUrl = topFragment.goodsInfoUrl;
        goodsId = topFragment.goodsId;
        goodsIndex = topFragment.groupData.goodsIndex;
        filialeId = String.valueOf(topFragment.goodsFiliale);
        Logger.i("goodsInfoUrl " + goodsInfoUrl + "\ngoodsId" + goodsId + "\nfilialeID" + filialeId);
        if (null == topFragment.groupData) {
            return;
        }
        if (null == topFragment.groupData.amount) {
        } else {
            tvPrice.setText(MoneyUtil.setNoSmallMoney(MoneyUtil.getLeXiangBi(topFragment.groupData.goodsPrice)));
        }

        Logger.i("goodsPrice  " + topFragment.groupData.goodsPrice + " cost   " + topFragment.groupData.amount);
        if (null == topFragment.groupData.goodsPrice) {
        } else {
            tvCost.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBi(topFragment.groupData.amount)));
        }
        if (null == topFragment.groupData.groupCondition) {
        } else {
            tvHasCount.setText(topFragment.groupData.groupCondition + "人团");
        }
        if (!TextUtils.isEmpty(topFragment.groupData.isDirectBuy)) {
            if (topFragment.groupData.isDirectBuy.equals("0")) {
                llNowBuy.setBackgroundColor(getResources().getColor(R.color.color_666));
                llNowBuy.setEnabled(false);
                llNowBuy.setClickable(false);
            }
        } else {
            llNowBuy.setBackgroundColor(getResources().getColor(R.color.swipeLoadstart));
            llNowBuy.setEnabled(true);
            llNowBuy.setClickable(true);
        }
        if (!TextUtils.isEmpty(topFragment.groupData.isJoinOpenGroup)) {
            if (topFragment.groupData.isJoinOpenGroup.equals("0")) {
                isJoinOpenGroup = false;
                llOpenGroup.setEnabled(false);
                llOpenGroup.setClickable(false);
            }
        } else {
            isJoinOpenGroup = true;
            llOpenGroup.setEnabled(true);
            llOpenGroup.setClickable(true);
        }

    }

    private void initFragment() {
        startMyDialog();
        topFragment = new SpellGroupDetailsTopFragment();
        bottomFragment = new SpellGroupDetailsBottomFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.top_content, topFragment)
                .add(R.id.bottom_content, bottomFragment).commit();

        DragLayout.ShowNextPageNotifier next = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                bottomFragment.initWiew(which);
                rlBtnTop.setVisibility(View.GONE);
            }
        };
        dragLayout.setNextPageListener(next);
        DragLayout.ShowTopPageNotifier top = new DragLayout.ShowTopPageNotifier() {
            @Override
            public void onDragTop() {
                rlBtnTop.setVisibility(View.VISIBLE);
            }
        };
        dragLayout.setTopPageListener(top);

        topFragment.setOnLoadErrorListener(new onLoadErrorListener() {
            @Override
            public void isError(boolean isError) {
                Logger.i("isError    " + isError);
                if (isError) {
                    llErrorView.setVisibility(View.VISIBLE);
                    stopMyDialog();
                } else {
                    stopMyDialog();
                    initViewData();
                }
            }
        });
    }

    private void initView() {
        dragLayout = (DragLayout) findViewById(R.id.draglayout);
        dragLayout.setVisibility(View.GONE);
        bottomContent = (FrameLayout) findViewById(R.id.bottom_content);
        ivBackTop = (ImageView) findViewById(R.id.iv_back_top);
        ivMoreTop = (ImageView) findViewById(R.id.iv_more_top);
        ivShopTop = (ImageView) findViewById(R.id.iv_shop_top);
        rlBtnTop = (RelativeLayout) findViewById(R.id.rl_btn_top);
        ivMySellGroup = (ImageView) findViewById(R.id.iv_my_sell_group);
        llNowBuy = (LinearLayout) findViewById(R.id.ll_now_buy);
        llOpenGroup = (LinearLayout) findViewById(R.id.ll_now_open_group);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvCost = (TextView) findViewById(R.id.tv_cost);
        tvHasCount = (TextView) findViewById(R.id.tv_has_count);
        llErrorView = (LinearLayout) findViewById(R.id.ll_error_view);
        llErrorView.setVisibility(View.GONE);

        tvRefresh = (TextView) findViewById(R.id.tv_update_error);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mShadowView = findViewById(R.id.view_shadow);


        ivBackTop.setOnClickListener(this);
        ivMoreTop.setOnClickListener(this);
        ivShopTop.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);
        ivMySellGroup.setOnClickListener(this);
        llNowBuy.setOnClickListener(this);
        llOpenGroup.setOnClickListener(this);
        llBottom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back_top:
                finish();
                break;
            case R.id.iv_more_top:
                menuShow();
                break;
            case R.id.iv_shop_top:
                intent = new Intent(context, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                startActivity(intent);
                break;
            case R.id.tv_update_error:
                topFragment.loadData();
                Logger.i("topFragment.loadData11111111111111111111");
                break;
            case R.id.iv_my_sell_group:
                if (!isLogin()) {
                    startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                    ;
                } else {
                    intent = new Intent(context, MySpellGroupActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_now_buy:
                jumpGoodsDetailsActivity();
                break;
            case R.id.ll_now_open_group:
                if (!isJoinOpenGroup) {
                    showToast("暂不支持拼团");
                }
                mOperate = BUY_OPEN_GROUP;
                showPropertyWindow();
                break;
        }
    }

    public void showPropertyWindow() {
        if (mPropertyWindow == null) {
            View contentView = getLayoutInflater().inflate(R.layout.good_property_select_layout_jp, null);
            mPropertyWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPropertyWindow.setContentView(contentView);
            mPropertyWindow.setAnimationStyle(R.style.AnimationSEX);
            repertoryTv = (TextView) contentView.findViewById(R.id.tv_repertory);
            priceTv = (TextView) contentView.findViewById(R.id.tv_price);
            costTv = (TextView) contentView.findViewById(R.id.tv_cost);
            costTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            TextView juanTv = (TextView) contentView.findViewById(R.id.tv_juan);
            juanTv.setVisibility(View.GONE);
            TextView voucherTv = (TextView) contentView.findViewById(R.id.tv_voucher);
            TextView propertyTv = (TextView) contentView.findViewById(R.id.tv_select_property);
            ImageView imgClose = (ImageView) contentView.findViewById(R.id.imgView_close);
            ImageView imgIcon = (ImageView) contentView.findViewById(R.id.imgView_good_icon);
            final Button decreaseBtn = (Button) contentView.findViewById(R.id.btn_decrease);
            decreaseBtn.setVisibility(View.GONE);
            Button increaseBtn = (Button) contentView.findViewById(R.id.btn_increase);
            increaseBtn.setVisibility(View.GONE);
            final TextView amountTv = (TextView) contentView.findViewById(R.id.tv_amount);
            amountTv.setText(String.valueOf("1"));//对选择数量进行初始化赋值
            NoScrollListView propertyLv = (NoScrollListView) contentView.findViewById(R.id.lv_commodity_property);
            TextView cartTv = (TextView) contentView.findViewById(R.id.tv_put_cart);
            TextView buyTv = (TextView) contentView.findViewById(R.id.tv_buy);
            confirmTv = (TextView) contentView.findViewById(R.id.tv_confirm);
            View placeholderView = contentView.findViewById(R.id.view_placeholder);
            repertoryLayout = (LinearLayout) contentView.findViewById(R.id.ll_repertory);

            //根据商品是否有规格来判断库存是否显示
            if ("0:0".equals(topFragment.sku)) {
                propertyTv.setText(topFragment.noskuPropertyTxt(topFragment.groupData.goodsSkuCount));
                repertoryLayout.setVisibility(View.VISIBLE);
                repertoryTv.setText(topFragment.skuCount);
            } else {
                repertoryLayout.setVisibility(View.VISIBLE);

                //下面这两句是初始化商品选择规格和库存 应该为在adapter每次选择后取出id后的selectSkuList（如：2:2047,3:16）
                propertyTv.setText(topFragment.staticPropertyText(topFragment.selectSkuList));
                repertoryTv.setText(topFragment.skuCount);
            }
            //这个拿到的是总的库存
            if ("0".equals(topFragment.skuCount)) {
                isNothing = true;
                confirmTv.setText("暂时无货");
                confirmTv.setBackgroundColor(getResources().getColor(R.color.btn_unclick));
                confirmTv.setEnabled(false);
            } else {
                isNothing = false;
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
                            break;
                        case R.id.btn_increase: //+号

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
                            Logger.i("最后的SKU点击222：" + finalSKU);

                            if ("0".equals(inventory) && !hasInventory) {
                                showToast("暂时无货");
                                return;
                            }
                            openGrupBuy();//跳转支付界面
                            break;
                        default:
                            break;
                    }
                }
            };
            if (topFragment.groupData != null) {
                GlideUtil.showImage(context, Constants.ImageUrl + topFragment.groupData.goodsAlbum.get(0) + Constants.ImageSuffix, imgIcon);
                //规格
                //价格的赋值
                Logger.i("skuIntegralPrice  " + skuIntegralPrice + "\namount " + topFragment.groupData.amount + "\n " + MoneyUtil.getLeXiangBi(topFragment.groupData.amount) + "\ngoodsPrice  " + topFragment.groupData.goodsPrice);
                if (!TextUtils.isEmpty(skuIntegralPrice)) {
                    priceTv.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBi(topFragment.groupData.amount)));
                    costTv.setText(MoneyUtil.setNoSmallMoney(MoneyUtil.getLeXiangBi(topFragment.groupData.goodsPrice)));
                } else {
                    priceTv.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeFenBi(topFragment.groupData.amount)));
                    costTv.setText(MoneyUtil.setNoSmallMoney(MoneyUtil.getLeXiangBi(topFragment.groupData.goodsPrice)));
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
            if (propertyAdapter == null && topFragment.groupData != null) {
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
            if (hasInventory && !isNothing && !"0".equals(inventory)) {
                repertoryLayout.setVisibility(View.VISIBLE);
                repertoryTv.setText(inventory);
                Logger.i("selectSkuResultinventory显示22222" + inventory);
                confirmTv.setText("确定");
                confirmTv.setBackgroundColor(getResources().getColor(R.color.myinfo_red_bg));
                confirmTv.setEnabled(true);
            } else if ("0".equals(inventory) || isNothing) {
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
            if (!TextUtils.isEmpty(skuMarketPrice)) {
                priceTv.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBi(topFragment.groupData.amount)));
                costTv.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBi(topFragment.groupData.goodsPrice)));

            }
        }
    }

    private void jumpGoodsDetailsActivity() {
        Intent intent = new Intent(context, JPNewCommDetailActivity.class);
        intent.putExtra("goods_id", topFragment.groupData.goodsIndex);
        intent.putExtra("filiale_id", filialeId);
        Logger.i("跳转前的数据  goodsIndex  " + topFragment.groupData.goodsIndex + "\nfiliale_id  " + filialeId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("onResume   211212121");
    }

    /**
     * 立即开团
     */
    private void openGrupBuy() {
        if (!isLogin()) {
            startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
            return;
        }
        if (topFragment.groupData != null) {
            int propertyCount = topFragment.groupData.goodsSkuProperty.size();

            if (propertyCount == 0 || topFragment.mSelectedProperty.size() == propertyCount) { //商品没有属性的时候 或是有属性但是已经选择过了属性
                String goodSku = "";
                String goodsNorms = "";
                if (topFragment.groupData.goodsSkuProperty.size() == 0) {
                    goodSku = "0:0";
                    goodsPrice = topFragment.groupData.amount;
                    String goodsCost = topFragment.groupData.goodsPrice;
                } else {
                    if (topFragment.mSelectedProperty.size() < topFragment.groupData.goodsSkuProperty.size()) {
                        showToast("请选择商品属性");
                        return;
                    }
                    for (SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean property : topFragment.groupData.goodsSkuProperty) {
                        if (!TextUtils.isEmpty(goodSku)) {
                            goodSku += ";";
                        }
                        goodSku += property.skuIndex + ":" + topFragment.mSelectedProperty.get(property.skuIndex).skuIndex;
                        goodsNorms += topFragment.mSelectedProperty.get(property.skuIndex).skuName + " ";
                    }
                }
                if (!isLogin()) {
                    startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                } else {
                    //跳转支付界面
                    switch (mOperate) {
                        case BUY_OPEN_GROUP:
                            Intent intent = new Intent(context, TakeOutFlashSalePayActivity.class);
                            OrderSubmitGoods orderSubmitGoods = new OrderSubmitGoods();
                            orderSubmitGoods.goodsPrice = Double.parseDouble(topFragment.groupData.amount);
                            orderSubmitGoods.goodsPic = Constants.ImageUrl + topFragment.groupData.goodsAlbum.get(0);
                            orderSubmitGoods.goodsCount = mCount;
                            orderSubmitGoods.name = topFragment.groupData.goodsName;
                            orderSubmitGoods.goodsId = goodsId;
                            orderSubmitGoods.type = Constants.SPELL_GROUP;
                            orderSubmitGoods.label = "抽奖团";
                            orderSubmitGoods.sku = firstSKU;
                            orderSubmitGoods.fregihtPrice = topFragment.groupData.orderFregiht;
                            orderSubmitGoods.activityId = topFragment.groupData.index;
                            orderSubmitGoods.payType = String.valueOf(BUY_OPEN_GROUP);
                            orderSubmitGoods.sku = skuStr;
                            Logger.i("operate finalSku2222 " + firstSKU + "\nIndexID ");
                            intent.putExtra("orderSubmitGoods", orderSubmitGoods);
                            startActivity(intent);
                            hidePropertyWindow();
                            break;
                    }
                }
            } else if (topFragment.mSelectedProperty.size() < propertyCount) { //选择的属性个数小于所有属性个数，就是属性没有选择全面
                showPropertyWindow();
                showToast("请选择商品属性");
            }
        }
    }

    /**
     * 隐藏属性选择窗口
     *
     * @return 成功进行窗口隐藏返回true, 失败或者窗口未显示返回false
     */
    public boolean hidePropertyWindow() {
        if (mPropertyWindow != null && mPropertyWindow.isShowing()) {
            mPropertyWindow.dismiss();
            mShadowView.setVisibility(View.GONE);
            mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_out));
            return true;
        }
        return false;
    }

    //右上角的popupWindow
    private void menuShow() {
        PopupMenu popupMenu = new PopupMenu(this);
        popupMenu.showLocation(R.id.iv_more_top);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                switch (item) {
                    case ITEM1:
                        startActivity(new Intent(context, JPMainActivity.class));
                        break;
                    case ITEM2:
                        if (!isLogin()){
                            startActivity(new Intent(context,LeFenPhoneLoginActivity.class));
                        }else{
                            startActivity(new Intent(context, FavoriteActivity.class));
                        }
                        break;
                    case ITEM3:
                        shareGoods();
                        break;
                }
            }
        });
    }

    private void shareGoods() {
        if (umengDialog == null) {
            umengDialog = new UmengDialog(context, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom),
                    R.style.DialogControl, new UmengGloble().getAllIconModels());
            umengDialog.setItemLister(new UmengDialog.OnListItemClickListener() {
                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            context,
                            ((IconModel) arg4).getType(),
                            shareContent,
                            shareTitle,
                            shareImagUrl,
                            shareUrl).share();
                }
            });
        }
        umengDialog.show();
    }

    public void getShareUrl() {
        JPNetRequest netRequest = new JPNetRequest(context);
        String shareType = "2";
        netRequest.getShareUrl(shareType, goodsId, new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        shareContent = responseInfo.result.content;
                        shareImagUrl = responseInfo.result.imgUrl;
                        if (shareContent.contains("http://") || shareContent.contains("https://")) {
                            shareImagUrl = shareImagUrl + Constants.ImageSuffix;
                        } else {
                            shareImagUrl = Constants.ImageUrl + shareImagUrl + Constants.ImageSuffix;
                        }
                        shareTitle = responseInfo.result.title;
                        shareUrl = responseInfo.result.url;
                        break;
                    default:
                        Logger.i(getClass().getSimpleName() + responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 选择规格的弹窗界面的adapter
     */
    class PropertyAdapter extends BaseAdapter {

        private List<SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean> mPropertys = topFragment.groupData.goodsSkuProperty;
        private List<SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean> mValues = topFragment.groupData.goodsSkuValues;
        private List<SpellGroupDetailsEntity.DataBean.GoodsSkuPriceBean> mPrice = topFragment.groupData.goodsSkuPrice;
        private ArrayList<String> skuList = new ArrayList<>();
        private ArrayList<String> skuList2 = new ArrayList<>();
        private List<String> selectSkuList;

        public PropertyAdapter(List<String> selectSkuList) {
            this.selectSkuList = selectSkuList;
        }

        @Override
        public int getCount() {
//            skuList.clear();
            Logger.i("getCount被调用了");
            if (mPropertys != null) {
                for (int i = 0, count = mPropertys.size(); i < count; i++) {
                    String s = "L";
                    skuList.add(s);
                }
                Logger.i("2017-2-7:" + skuList.size());
                Logger.i("2017-2-7:" + skuList.toString());
                return mPropertys.size();
            }
            return 0;
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

            final SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean property = mPropertys.get(position);
            if (property != null) {
                tvPropertyName.setText(property.skuName);
                final List<SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean> mValueFilter = new ArrayList<>();
                for (SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean value : mValues) {
                    Logger.i("goodsSkuProperty  " + mValues.size());
                    if (property.skuIndex == null || value == null) {
                        continue;
                    }
                    if (property.skuIndex.equals(value.skuParentId)) {
                        mValueFilter.add(value);
                    }

                    if (isHandClick) {
                        if (selectSkuList.size() >= 1) {
                            for (int m = 0; m < selectSkuList.size(); m++) {
                                String select = selectSkuList.get(m);
//                                if (m < 1) {
//                                    finalSKU = selectSkuList.get(m);
//                                } else {
//                                    if (m < selectSkuList.size() - 1) {
//                                        finalSKU += selectSkuList.get(m) + ",";
//                                    } else {
//                                        finalSKU += selectSkuList.get(m);
//                                    }
//                                }

                                String[] selectStr = select.split(":");
                                Logger.i("2016-12-15[]:" + selectStr[0]);
                                Logger.i("2016-12-15[]:" + selectStr[1]);
                                if (selectStr[0].equals(value.skuParentId) && selectStr[1].equals(value.skuIndex)) {
                                    topFragment.mSelectedProperty.put(selectStr[0], value);
                                    break;
                                }
                            }
                        } else {
                            String select = selectSkuList.get(0);
                            finalSKU = select;
                            if (select.equals(value.skuParentId) && select.equals(value.skuIndex)) {
                                topFragment.mSelectedProperty.put(select, value);
                            }
                        }
                        //把默认显示的SKU赋值给集合 未点击数据
                        //kuList.add(finalSKU);
                        Logger.i("2017-2-81" + finalSKU);
//                        switchGoodsNormsData(finalSKU);
                    }
                }
                Logger.i("mValueFilter size" + mValueFilter.size());
                for (int i = 0; i < mValueFilter.size(); i++) {
                    final SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean value = mValueFilter.get(i);
                    final TextView valueView = getValueView(value.skuName, value.skuParentId);
                    if (value == topFragment.mSelectedProperty.get(property.skuIndex)) {
                        valueView.setBackgroundResource(R.drawable.commodity_property_item_select_bg_jp);
                        valueView.setTextColor(getResources().getColor(R.color.white));
                        valueView.setTextSize(12);
                    }
                    switchSkuStr(topFragment.sku, mValues);

                    Logger.e("mValueFilter   " + mValueFilter.size() + "mPrice " + mPrice.size() + "VALUE " + value);
                    valueView.setOnClickListener(new View.OnClickListener() {

                        private String sku;

                        @Override
                        public void onClick(View view) {
                            isHandClick = false;

                            //重新更新服务器返回的sku属性值

                            for (int j = 0; j < mValues.size(); j++) {
                                SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean goodsSkuProperty = mValues.get(j);
                                if (valueView.getText().toString().equals(goodsSkuProperty.skuName)) {
                                    sku = goodsSkuProperty.skuParentId + ":" + goodsSkuProperty.skuIndex;
                                    Logger.i("2017-2-6:" + sku);
                                }
                            }
                            skuList.set(position, sku);//把选中的SKU值赋值到SKU列表里面
                            Logger.i("2017-2-82" + skuList.toString() + "value TEXT" + value);
                            if (topFragment.mSelectedProperty.containsValue(value)) {
                                topFragment.mSelectedProperty.remove(property.skuIndex);

                                valueView.setEnabled(true);
                                valueView.setTextColor(getResources().getColor(R.color.white));
                                valueView.setTextSize(12);
                                //有默认显示的SKU所以不能根据L来判断点击没有
                                skuList.set(position, "L");
                            } else {
                                valueView.setEnabled(false);
                                valueView.setTextColor(Color.parseColor("#E0E0E0"));
                                valueView.setTextSize(12);
                                topFragment.mSelectedProperty.put(property.skuIndex, value);
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

                            //计算当前sku的选中的内容
                            switchSkuStr(firstSKU, mValues);

                            ((TextView) mPropertyWindow.getContentView().findViewById(R.id.tv_select_property)).setText(topFragment.getPropertyTxt(mCount));

                            notifyDataSetChanged();
                        }
                    });
                    layoutPropertyValue.addView(valueView);
                }
            }
            return convertView;
        }

        private void switchGoodsNormsData(String selectSkuResult) {
            Logger.i("2017-2-6:" + "走了这里1");
            Logger.i("2017-2-8:" + selectSkuResult);
            // 拿到现在的sku去判断当前的服务器返回的sku是否相等 是否有货，
            if (selectSkuResult.contains("L")) { //如果包含“L” L是SKU的初始化字符串，这个时候说明SKU还没有选择完毕，不进行按钮能否点击的判断 也不进行库存的判断
                ((TextView) mPropertyWindow.getContentView().findViewById(R.id.tv_repertory)).setText(topFragment.skuCount);
                return;
            }
            allInventory = 0;
            if (!TextUtils.isEmpty(selectSkuResult)) {
                for (int i = 0; i < mPrice.size(); i++) {
                    String skuInfo = mPrice.get(i).skuInfo;
                    Logger.i("skuInfo" + skuInfo);
                    Logger.i("2017-2-6:" + skuInfo);
                    if (skuInfo.equals(selectSkuResult)) {
                        //有货 取出当前的库存,还有价格
                        hasInventory = true;
                        inventory = mPrice.get(i).skuInventory;
                        String skuCostPrice = mPrice.get(i).skuCostPrice;
                        skuIntegralPrice = mPrice.get(i).skuIntegralPrice;
                        skuMarketPrice = mPrice.get(i).skuMarketPrice;
                        skuIntegralMoney = mPrice.get(i).skuIntegralMoney;
                        Logger.i("selectSkuResultinventory" + inventory);
                        Logger.i("2017-2-6:" + inventory);

                        break;
                    } else if (!skuInfo.contains(selectSkuResult)) {
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

        class ViewHolder {
            TextView tvPropertyName;
            FlowLayout layoutPropertyValue;
        }
    }

    private void switchSkuStr(String firstSKU, List<SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean> mValues) {

        String[] splitSku = firstSKU.split(",");
        Logger.i("firstSku  " + firstSKU);
        String startStr = null;
        String endStr = null;
        //去第一个数据
        for (int i = 0; i < splitSku.length; i++) {
            split = splitSku[i].split(":");
            if (i==splitSku.length-1){
                endStr=split[1];
            }else{
                startStr = split[1];
            }
        }
        for (int i = 0; i < mValues.size(); i++) {
            if (mValues.get(i).skuIndex.equals(startStr)) {
                skuStr = mValues.get(i).skuName;
            } else if (mValues.get(i).skuIndex.equals(endStr)) {
                skuStr += mValues.get(i).skuName;
            }
        }
        Logger.i( "\n startStr " + startStr + "\n endStr " + endStr+" MvALUES  "+mValues);
    }
}
