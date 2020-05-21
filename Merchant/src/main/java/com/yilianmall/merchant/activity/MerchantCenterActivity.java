package com.yilianmall.merchant.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.MerchantEnterType;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.utils.RecordListRetention;
import com.yilian.mylibrary.widget.CustomViewPager;
import com.yilian.networkingmodule.entity.MerchantCenterInfo;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.MTGridViewAdapter;
import com.yilianmall.merchant.adapter.MTViewPagerAdapter;
import com.yilianmall.merchant.dialog.ExpireWarnPopwindow;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.widget.OverDueDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilianmall.merchant.activity.MerchantChooseQrCodeActivity.IS_OPEN_POWER;
import static com.yilianmall.merchant.activity.MerchantQrCodeActivity.GIFT_TYPE;

public class MerchantCenterActivity extends BaseActivity implements View.OnClickListener {


    public static final int LOCATION_PERMISSION = 999;
    boolean isRefresh = true;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvMerchantManager;
    private Button merchantBtnExplainMerchant;
    private TextView tvTodayStream;
    private TextView tvTodayCoupon;
    private TextView tvTodayProfit;
    private CustomViewPager customViewPager;
    private TextView tvFlagShipManager;
    private Button merchantBtnExplainFlagShipStore;
    private TextView tvFlagShipStoreTodayStream;
    private TextView tvFlagShipStoreTodayCoupon;
    private TextView tvFlagShipStoreTodayProfit;
    private LinearLayout llFlagShipStoreManager1;
    private TextView tvPendingSendOrder;
    private TextView tvAfterSaleOrder;
    private TextView tvPendingReceiveOrder;
    private LinearLayout llFlagShipStoreManager2;
    private RelativeLayout rlNoFlagShipStore;
    private TextView tvNoFlagShipStore;
    private boolean hasFlagShip;
    private OverDueDialog.Builder builder;
    private OverDueDialog overDueDialog;
    private String merchantNotice;
    private String supplierNotice;
    private List<View> mPagerList;
    private LinearLayout llContent;
    private ImageView ivNothing;
    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout rlNothing;
    private Button btnUpgrade;
    private String merchantId;
    private int isOpenPower;
    /**
     * 到期提醒
     */
    private ExpireWarnPopwindow expireWarnPopwindow;
    /**
     * 提醒相关
     */
    private View includeNotice;
    private TextView tvNotice;
    private LinearLayout llNoticeCancel;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 8;
    /**
     * 总的页数
     */
    private int pageCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_center);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        Logger.i("走了这里1");
        super.onResume();
        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_MERCHANT_CENTER, mContext, true);
        if (isRefresh) {
            Logger.i("走了这里2");
            getMerchantCenterInfo();
            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_CENTER, isRefresh, mContext);
        }
    }

    /**
     * 获取商家中心数据
     */
    private void getMerchantCenterInfo() {
//        startMyDialog();
        String appVersion = "android_" + CommonUtils.getAppVersion(mContext) + ",ios_0";
        Logger.i("获得当前的版本号  " + appVersion);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantCenterInfo(appVersion, new Callback<MerchantCenterInfo>() {
                    @Override
                    public void onResponse(Call<MerchantCenterInfo> call, Response<MerchantCenterInfo> response) {
                        swipeRefreshLayout.setRefreshing(false);
//                        stopMyDialog();
                        MerchantCenterInfo body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        showData();
                                        // 益联商家中心通知共享文件key值 是否显示 && intro字段不为空
                                        if (PreferenceUtils.readBoolConfig(Constants.KEY_MERCHANT_NOTICE, mContext) && !TextUtils.isEmpty(body.intro)) {
                                            includeNotice.setVisibility(View.VISIBLE);
                                            tvNotice.setText(body.intro);
                                        } else {
                                            includeNotice.setVisibility(View.GONE);
                                        }

//                                        if (body.isExpire == 1) {
//                                            overdue(DateUtils.formatDate2(body.merchantInfo.merchantDueTime));
//                                        }
                                        isOpenPower = body.merchantInfo.isOpenPower;
                                        setMerchantOfflineShopStatus(body);
                                        MerchantCenterInfo.MerchantInfoBean merchantInfo = body.merchantInfo;//门店信息
                                        PreferenceUtils.writeStrConfig(Constants.MERCHANT_TYPE, merchantInfo.merchantType, mContext);
                                        if (merchantInfo.merchantType.equals("0")) {
                                            btnUpgrade.setVisibility(View.VISIBLE);
                                        } else {
                                            btnUpgrade.setVisibility(View.GONE);
                                        }
                                        MerchantCenterInfo.MerchantInfoBean.MerchantTradeBean merchantTrade = merchantInfo.merchantTrade;//门店领奖励信息
                                        tvTodayStream.setText(formatMoney("今日交易", merchantTrade.tradAmount));
                                        tvTodayCoupon.setText(formatMoney("今日让利", merchantTrade.discountAmount));
                                        tvTodayProfit.setText(formatMoney2("今日销售" + Constants.APP_PLATFORM_DONATE_NAME, merchantTrade.beanAmount, null));
                                        //门店管理介绍URL
                                        merchantNotice = body.merchantNotice;
                                        List<MerchantCenterInfo.MerchantInfoBean.IconBean> icon = merchantInfo.icon;//图标
                                        setIcons(icon);
                                        MerchantCenterInfo.SupplierInfoBean supplierInfo = body.supplierInfo;//旗舰店信息
                                        MerchantCenterInfo.SupplierInfoBean.SupplierTradeBean supplierTrade = supplierInfo.supplierTrade;
                                        if (supplierInfo.isExist == 1) {
                                            hasFlagShipStore(supplierTrade);
                                        } else {
                                            noFlagShipStore(body.supplierUrl);
                                        }
                                        //旗舰店管理介绍Url
                                        supplierNotice = body.supplierNotice;
                                        merchantId = merchantInfo.merchantId;

                                        //是否弹出 到期提醒
                                        expireWarnPop(body.last_days, body.merchantInfo.merchantDueTime);
                                        break;
                                    default:
                                        break;
                                }
                            }

                            if (body.code != 1) {
                                hideData();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantCenterInfo> call, Throwable t) {
                        showToast(R.string.network_module_net_work_error);
                        hideData();
                        swipeRefreshLayout.setRefreshing(false);
//                        stopMyDialog();
                    }
                });
    }

    private void showData() {
        llContent.setVisibility(View.VISIBLE);
        rlNothing.setVisibility(View.GONE);
    }

    /**
     * 商家资料审核状态提示
     *
     * @param body
     */
    private void setMerchantOfflineShopStatus(MerchantCenterInfo body) {
        if (body != null && PreferenceUtils.readBoolConfig(Constants.SHOW_MERCHANT_REFUSE_REASON, mContext, true)) {
            if (body.merchantInfo.merchantStatus == 3) {
                showDialog("审核被拒绝",
                        "拒绝原因:" + body.merchantInfo.refundReason, "您的线下门店被审核拒绝了,将不会显示在首页“附近商家”列表。如需显示在“附近商家”,请参照拒绝原因完成相应操作!", 0, Gravity.CENTER, "下次提醒", "忽略", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        PreferenceUtils.writeBoolConfig(Constants.SHOW_MERCHANT_REFUSE_REASON, false, mContext);//下次不再提醒
                                        dialog.dismiss();
                                        break;
                                    case DialogInterface.BUTTON_POSITIVE:
                                        PreferenceUtils.writeBoolConfig(Constants.SHOW_MERCHANT_REFUSE_REASON, true, mContext);//下次继续提醒
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        }, mContext);
            }
        }
    }

    Spanned formatMoney(String text, String money) {
        return Html.fromHtml(text + "<br><small>¥</small><big><big>" + MoneyUtil.getLeXiangBiNoZero(money) + "</big></big>");
    }

    Spanned formatMoney2(String text, String money1, String money2) {
        if (TextUtils.isEmpty(money2)) {
            return Html.fromHtml(text + "<br><big><big>" + MoneyUtil.getLeXiangBiNoZero(money1) + "</big></big>");
        } else {
            return Html.fromHtml(text + "<br><big><big>" + MoneyUtil.getLeXiangBiNoZero(money1) + "/" + MoneyUtil.getLeXiangBiNoZero(money2) + "</big></big>");
        }
    }

    /**
     * 设置图标
     *
     * @param icon
     */
    private void setIcons(final List<MerchantCenterInfo.MerchantInfoBean.IconBean> icon) {
        pageCount = (int) Math.ceil(icon.size() * 1.0 / pageSize);
        mPagerList = new ArrayList();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例
            com.yilianmall.merchant.widget.NoScrollGridView gridView = (com.yilianmall.merchant.widget.NoScrollGridView) getLayoutInflater().inflate(R.layout.merchant_mt_home_page_gridview, customViewPager, false);
            gridView.setAdapter(new MTGridViewAdapter(mContext, icon, i, pageSize));
            mPagerList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MerchantCenterInfo.MerchantInfoBean.IconBean iconBean = icon.get(position);

                    switch (iconBean.status) {
                        case 0:
                            if (iconBean.type == 7) {
                                jumpToPage(iconBean);
                            }
                            break;
                        case 1:
                            jumpToPage(iconBean);
                            break;
                        case 2:
                            startActivity(new Intent(mContext, MerchantAddressActivity.class));
                            showToast("请先设置门店地址");
                            break;
                        case 3:
                            startActivity(new Intent(mContext, MerchantChangeDiscountActivity.class));
                            showToast("请先设置门店折扣");
                            break;
                        case 4:
                            startActivity(new Intent(mContext, MerchantEditInformationActivity.class));
                            showToast("请先设置门店资料");
                            break;
                    }
                }
            });
        }
        customViewPager.setAdapter(new MTViewPagerAdapter(mPagerList));
    }

    /**
     * 有线上商城旗舰店时隐藏 没有线上商城旗舰店提示
     *
     * @param supplierTrade
     */
    public void hasFlagShipStore(MerchantCenterInfo.SupplierInfoBean.SupplierTradeBean supplierTrade) {
        llFlagShipStoreManager1.setVisibility(View.VISIBLE);
        llFlagShipStoreManager2.setVisibility(View.VISIBLE);
        rlNoFlagShipStore.setVisibility(View.GONE);
        tvNoFlagShipStore.setVisibility(View.GONE);
//        tvTodayProfit.setText(formatMoney("今日销售奖券", merchantTrade.salesBonus));
        tvFlagShipStoreTodayStream.setText(formatMoney("今日交易", supplierTrade.tradeAmount));
        tvFlagShipStoreTodayCoupon.setText(formatMoney("今日让利", supplierTrade.discountAmount));
        tvFlagShipStoreTodayProfit.setText(formatMoney2("今日销售" + Constants.APP_PLATFORM_DONATE_NAME, supplierTrade.salesBonus, null));

//        return Html.fromHtml(text + "<br><small>¥</small><big><big>" + MoneyUtil.getLeXiangBi(money) + "</big></big>");

        tvPendingSendOrder.setText(formatOrderNumber("待发货订单", supplierTrade.readySend));
        tvAfterSaleOrder.setText(formatOrderNumber("售后订单", supplierTrade.service));
        tvPendingReceiveOrder.setText(formatOrderNumber("商品管理", supplierTrade.goodsNum));
    }

    /**
     * 没有线上商城旗舰店时，显示没有线上商城旗舰店提示
     *
     * @param supplierUrl
     */
    public void noFlagShipStore(String supplierUrl) {
        llFlagShipStoreManager1.setVisibility(View.GONE);
        llFlagShipStoreManager2.setVisibility(View.GONE);
        rlNoFlagShipStore.setVisibility(View.VISIBLE);
        tvNoFlagShipStore.setVisibility(View.VISIBLE);
        tvNoFlagShipStore.setText("如果需要开通线上店铺，请在电脑上登陆\n" + supplierUrl + "\n完善相关资料并上传店铺商品信息");
    }

    /**
     * 到期提醒提醒 弹出
     *
     * @param last_days       剩余有效期
     * @param merchantDueTime 到期时间
     */
    private void expireWarnPop(long last_days, long merchantDueTime) {
        //是否过期标示
        final boolean isExpire;
        //有效期是否小于等于10天
        if (last_days < 11) {
            //判断是否已过期
            if (last_days <= 0) {
                //已过期
                isExpire = true;
            } else {
                //未过期
                isExpire = false;
            }

            if (expireWarnPopwindow == null) {
                expireWarnPopwindow = new ExpireWarnPopwindow(mContext);
            }
            expireWarnPopwindow.setContent(last_days, merchantDueTime);
            expireWarnPopwindow.renew(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    expireWarnPopwindow.dismiss();
                    //去续费
                    JumpToOtherPageUtil.getInstance().jumpToMerchantPayActivity(mContext, MerchantEnterType.RENEW);
                    if (isExpire) {
                        finish();
                    }
                }
            });

            expireWarnPopwindow.cancel(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    expireWarnPopwindow.dismiss();
                    if (isExpire) {
                        finish();
                    }
                }
            });
            expireWarnPopwindow.showPop(swipeRefreshLayout);
        }
    }

    private void hideData() {
        llContent.setVisibility(View.GONE);
        rlNothing.setVisibility(View.VISIBLE);
    }

    private void jumpToPage(MerchantCenterInfo.MerchantInfoBean.IconBean iconBean) {
        Intent intent = null;
        switch (iconBean.type) {
            case 0:
                intent = new Intent(mContext, MerchantQrCodeActivity.class);
                intent.putExtra(GIFT_TYPE, MerchantQrCodeActivity.TYPE_2);
                intent.putExtra(IS_OPEN_POWER, isOpenPower);
                break;
            case 1:
                intent = new Intent(mContext, MerchantOffLineDealActivity.class);
                break;
            case 2:
                intent = new Intent(mContext, MerchantChangeDiscountActivity.class);
                break;
            case 3:
                intent = new Intent(mContext, MerchantMoneyRecordActivity2.class);
                break;
            case 4:
                intent = new Intent(mContext, MerchantEditInformationActivity.class);
                break;
            case 5:
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    intent = new Intent(mContext, MerchantAddressActivity.class);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
                    return;
                }
                break;
            case 6:
                JumpToOtherPageUtil.getInstance().jumpToJTakeCashActivity(mContext);
                return;
            case 7:
//年费缴存 续费
                intent = new Intent(mContext, MerchantPayActivity.class);
                intent.putExtra(Constants.MERCHANT_STATUS_FLAG, PreferenceUtils.readStrConfig(Constants.MERCHANT_LEV, mContext));
                intent.putExtra("merchantPayType", "1");//判断当前是续费
                break;
            case 8:
                //商超零售
                intent = new Intent(mContext, MerchantChooseDealTypeActivity.class);
                intent.putExtra("showText1", "条码交易");
                intent.putExtra("showText2", "整单交易");
//                showResaleDialog();
                break;
            case 9:
                intent = new Intent(mContext, MerchantComboManageActivity.class);
                break;
            case 10:
                JumpToOtherPageUtil.getInstance().jumpToMerchantMyRevneue(mContext);
                break;
            case 11:
                //待提取乐天使
                intent = new Intent(mContext, MerchantTicketActivity.class);
                intent.putExtra("recordType", RecordListRetention.WAIT_EXTRACT_LE_ANGEL);
                break;
            default:
                break;
        }
        if (intent == null) {
//            showToast("intent为空" + iconBean.type);
            return;
        }
        startActivity(intent);
    }

    Spanned formatOrderNumber(String text, String orderNumber) {
        return Html.fromHtml(text + "<br><big><big><font color='#333333'>" + orderNumber + "</big></big>");
    }

    /**
     * 年费到期 弹窗
     *
     * @param s
     */
    public void overdue(String s) {
        if (builder == null) {
            overDueDialog = new OverDueDialog.Builder(mContext)
                    .setOverDueTime(s)
                    .create();
        }
        if (!overDueDialog.isShowing()) {
            overDueDialog.show();
        }
        overDueDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMerchantCenterInfo();
            }
        });
    }

    private void initView() {
        btnUpgrade = (Button) findViewById(R.id.btn_upgrade);
        rlNothing = (RelativeLayout) findViewById(R.id.rl_nothing);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("商家中心");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setImageResource(R.mipmap.merchant_ic_mall_scanning);
        v3Share.setVisibility(View.VISIBLE);
        tvMerchantManager = (TextView) findViewById(R.id.tv_merchant_manager);
        merchantBtnExplainMerchant = (Button) findViewById(R.id.merchant_btn_explain_merchant);
        tvTodayStream = (TextView) findViewById(R.id.tv_today_stream);
        tvTodayCoupon = (TextView) findViewById(R.id.tv_today_coupon);
        tvTodayProfit = (TextView) findViewById(R.id.tv_today_profit);
        customViewPager = (CustomViewPager) findViewById(R.id.custom_view_pager);
        tvFlagShipManager = (TextView) findViewById(R.id.tv_flag_ship_manager);
        merchantBtnExplainFlagShipStore = (Button) findViewById(R.id.merchant_btn_explain_flag_ship_store);
        tvFlagShipStoreTodayStream = (TextView) findViewById(R.id.tv_flag_ship_store_today_stream);
        tvFlagShipStoreTodayCoupon = (TextView) findViewById(R.id.tv_flag_ship_store_today_coupon);
        tvFlagShipStoreTodayProfit = (TextView) findViewById(R.id.tv_flag_ship_store_today_profit);
        llFlagShipStoreManager1 = (LinearLayout) findViewById(R.id.ll_flag_ship_store_manager1);
        tvPendingSendOrder = (TextView) findViewById(R.id.tv_pending_send_order);
        tvAfterSaleOrder = (TextView) findViewById(R.id.tv_after_sale_order);
        tvPendingReceiveOrder = (TextView) findViewById(R.id.tv_pending_receive_order);
        llFlagShipStoreManager2 = (LinearLayout) findViewById(R.id.ll_flag_ship_store_manager2);
        rlNoFlagShipStore = (RelativeLayout) findViewById(R.id.rl_no_flag_ship_store);
        tvNoFlagShipStore = (TextView) findViewById(R.id.tv_no_flag_ship_store);

        includeNotice = (View) findViewById(R.id.include_notice);
        includeNotice.setVisibility(View.GONE);
        tvNotice = (TextView) findViewById(R.id.tv_notice);
        llNoticeCancel = (LinearLayout) findViewById(R.id.ll_notice_cancel);


        tvAfterSaleOrder.setOnClickListener(this);
        tvPendingSendOrder.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        v3Share.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvMerchantManager.setOnClickListener(this);
        merchantBtnExplainMerchant.setOnClickListener(this);
        tvTodayStream.setOnClickListener(this);
        tvTodayCoupon.setOnClickListener(this);
        tvFlagShipManager.setOnClickListener(this);
        merchantBtnExplainFlagShipStore.setOnClickListener(this);
        btnUpgrade.setOnClickListener(this);
        tvPendingReceiveOrder.setOnClickListener(this);
        llNoticeCancel.setOnClickListener(this);
    }

    private void initData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getMerchantCenterInfo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(mContext, MerchantAddressActivity.class));
                    } else {
                        showToast(getString(R.string.merchant_remind_location_permission));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showResaleDialog() {
        AlertDialog builder = new AlertDialog.Builder(mContext).create();
        builder.setMessage("请选择交易类型：");
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "条码交易", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, MerchantShopActivity.class);
                intent.putExtra("merchantId", merchantId);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "整单交易", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, MerchantResaleActivity.class);
                intent.putExtra("merchantId", merchantId);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_right) {
        } else if (i == R.id.tv_merchant_manager) {
        } else if (i == R.id.merchant_btn_explain_merchant) {
            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, merchantNotice, false);
        } else if (i == R.id.tv_today_stream) {
        } else if (i == R.id.tv_today_coupon) {
        } else if (i == R.id.tv_flag_ship_manager) {
        } else if (i == R.id.merchant_btn_explain_flag_ship_store) {
            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, supplierNotice, false);
        } else if (i == R.id.btn_upgrade) {
            Intent intent = new Intent(this, MerchantPayActivity.class);
            intent.putExtra(Constants.MERCHANT_STATUS_FLAG, "3");//升级时，是升级为实体商家
            intent.putExtra("merchantPayType", "0");//判断当前是缴费 个体升级为实体
            startActivity(intent);
            finish();
        } else if (i == R.id.v3Share) {
            Intent intent = new Intent();
            JumpToOtherPageUtil.getInstance().jumToMipcaActivityCapture(mContext, intent);
        } else if (i == R.id.tv_pending_receive_order) {
            Intent intent = new Intent(this, MerchantManagerActivity.class);
            startActivity(intent);
        } else if (i == R.id.tv_after_sale_order) {
            //售后订单列表
            startActivity(new Intent(mContext, MerchantAfterSaleListActivity.class));
        } else if (i == R.id.tv_pending_send_order) {
            //待备货订单
            Intent intent = new Intent(mContext, MerchantOrderListActivity.class);
            intent.putExtra("index", 0);
            startActivity(intent);
        } else if (i == R.id.tv_pending_receive_order) {
            //待收货订单
            Intent intent = new Intent(mContext, MerchantOrderListActivity.class);
            intent.putExtra("index", 2);
            startActivity(intent);
        } else if (i == R.id.ll_notice_cancel) {
            //关闭提醒
            //关闭提示 改变 （益联商家中心通知 展示）值为 false
            PreferenceUtils.writeBoolConfig(Constants.KEY_MERCHANT_NOTICE, false, mContext);
            includeNotice.setVisibility(View.GONE);
        }
    }


}
