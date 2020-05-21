package com.yilian.luckypurchase.activity;

import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.GoodsExpressListAdapter;
import com.yilian.luckypurchase.umeng.IconModel;
import com.yilian.luckypurchase.umeng.ShareUtile;
import com.yilian.luckypurchase.umeng.UmengDialog;
import com.yilian.luckypurchase.umeng.UmengGloble;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.entity.GoodsExpressItemEntity;
import com.yilian.mylibrary.entity.UserAddressLists;
import com.yilian.networkingmodule.entity.SnatchAwardDetailsEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author LYQ 中奖详情
 */
public class LuckyPrizeDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 7;
    private String activityId;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private RecyclerView recycleView;
    private SwipeRefreshLayout refreshLayout;
    private GoodsExpressListAdapter adapter;
    private List<GoodsExpressItemEntity> goodStatusList = new ArrayList<>();
    private TextView btnShowLine, btnShare;
    private String activityIdValue;

    private String awardStatusOld = "0";
    private String awardStatusNew;

    private UmengDialog mShareDialog;
    private String share_title, share_content, shareImg, share_url, goodsName, goodsImg;

    SnatchAwardDetailsEntity.SnatchInfoBean snatchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_prize_details2);
        activityId = getIntent().getStringExtra("activityId");
        initView();
        initData();
        initListener();
    }

    /**
     * 分享
     */
    private void share() {
        share_title = "幸运GO,GO精彩! 揣上点儿奖券进来转转吧,说不定就有惊喜等着你呢!";
        share_content = "我在幸运GO成功夺了 —" + goodsName;
        share_url = Constants.LUCKY_SHARE_URL;
        shareImg = goodsImg;
        if (shareImg.startsWith("http://") || shareImg.startsWith("https://")) {

        } else {
            shareImg = Constants.ImageUrl + shareImg;
        }

        Logger.i("share-" + share_title);
        Logger.i("share-" + share_content);
        Logger.i("share-" + shareImg);
        Logger.i("share-" + share_url);

        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.lucky_anim_wellcome_bottom), R.style.lucky_DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            share_title,
                            share_content,
                            shareImg,
                            share_url)
                            .share();
                }
            });
        }
        mShareDialog.show();
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    private void initData() {
        refreshLayout.setRefreshing(true);
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .snatchAwardDetails(activityId, new Callback<SnatchAwardDetailsEntity>() {
                    @Override
                    public void onResponse(Call<SnatchAwardDetailsEntity> call, Response<SnatchAwardDetailsEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        snatchInfo = response.body().snatchInfo;
                                        if (null == snatchInfo) {
                                            adapter.setEmptyView(getEmptyView());
                                        } else {
                                            activityIdValue = snatchInfo.activityId;
                                            awardStatusNew = snatchInfo.awardStatus;
                                            if (TextUtils.isEmpty(goodsName)) {
                                                goodsName = snatchInfo.prizeGoodsName;
                                            }
                                            if (TextUtils.isEmpty(goodsImg)) {
                                                goodsImg = snatchInfo.prizeGoodsUrl;
                                            }

                                            initHead();
                                            initGoodsList(snatchInfo);
                                            initBottom(snatchInfo);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<SnatchAwardDetailsEntity> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                        adapter.setEmptyView(getErrorView());
                    }
                });
    }

    private void initHead() {
        TextView headView = (TextView) View.inflate(mContext, R.layout.lucky_prize_details_head, null);
        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }
    }

    private void initBottom(SnatchAwardDetailsEntity.SnatchInfoBean snatchInfo) {
        View bottomView = View.inflate(mContext, R.layout.lucky_prize_details_bottom, null);
        LinearLayout llGoodsContent = (LinearLayout) bottomView.findViewById(R.id.ll_goods_content);
        btnShowLine = (TextView) bottomView.findViewById(R.id.tv_show_line);
        btnShare = (TextView) bottomView.findViewById(R.id.tv_share);
        TextView tvLogisticsName = (TextView) bottomView.findViewById(R.id.tv_logistics_name);
        TextView tvLogisticsNumber = (TextView) bottomView.findViewById(R.id.tv_logistics_number);
        ImageView goodsIcon = (ImageView) bottomView.findViewById(R.id.iv_icon);
        TextView tvName = (TextView) bottomView.findViewById(R.id.tv_name);
        TextView tvDate = (TextView) bottomView.findViewById(R.id.tv_date);
        TextView tvEnterCount = (TextView) bottomView.findViewById(R.id.tv_enter_count);
        TextView tvLuckNumber = (TextView) bottomView.findViewById(R.id.tv_luck_number);
        TextView tvAnnounceNumber = (TextView) bottomView.findViewById(R.id.tv_announce_number);
        TextView tvLookLogistics = (TextView) bottomView.findViewById(R.id.tv_look_logistics);
        TextView tvCopy = (TextView) bottomView.findViewById(R.id.tv_copy);
        TextView tvRemark = (TextView) bottomView.findViewById(R.id.tv_remark);

        LinearLayout addressLayout = (LinearLayout) bottomView.findViewById(R.id.layout_address);
        TextView tvAddressName = (TextView) bottomView.findViewById(R.id.tv_address_name);
        TextView tvAddressLocation = (TextView) bottomView.findViewById(R.id.tv_address_location);

        tvCopy.setOnClickListener(this);
        tvLookLogistics.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnShowLine.setOnClickListener(this);
        llGoodsContent.setOnClickListener(this);

        /**
         * 商品状态 0标识未开奖 1表示未设置收货地址 2待备货 3已发货 4已完成 5待发货
         */
        if (snatchInfo.awardStatus.equals("1")) {
            btnShare.setVisibility(View.GONE);
            btnShowLine.setVisibility(View.VISIBLE);
            btnShowLine.setText("收货地址");
            addressLayout.setVisibility(View.GONE);
            tvCopy.setVisibility(View.GONE);
        } else if (snatchInfo.awardStatus.equals("2")) {
            btnShare.setVisibility(View.GONE);
            btnShowLine.setVisibility(View.VISIBLE);
            btnShowLine.setText("收货地址");
            addressLayout.setVisibility(View.VISIBLE);
            tvCopy.setVisibility(View.GONE);
        } else if (snatchInfo.awardStatus.equals("3")) {
            btnShare.setVisibility(View.GONE);
            btnShowLine.setVisibility(View.VISIBLE);
            btnShowLine.setText("确认收货");
            addressLayout.setVisibility(View.VISIBLE);
            tvCopy.setVisibility(View.VISIBLE);
        } else if (snatchInfo.awardStatus.equals("4")) {
            btnShare.setVisibility(View.VISIBLE);
            btnShare.setText("分享");
            btnShowLine.setVisibility(View.VISIBLE);
            btnShowLine.setText("晒单");
            addressLayout.setVisibility(View.VISIBLE);
            tvCopy.setVisibility(View.VISIBLE);
        } else if (snatchInfo.awardStatus.equals("5")) {
            btnShare.setVisibility(View.GONE);
            btnShowLine.setVisibility(View.VISIBLE);
            btnShowLine.setText("待收货");
            addressLayout.setVisibility(View.VISIBLE);
            btnShowLine.setTextColor(getResources().getColor(R.color.library_module_gray));
            btnShowLine.setBackgroundResource(R.drawable.library_module_bg_style_grey_empty_15);
            tvCopy.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(snatchInfo.awardUserNick)) {
            tvAddressName.setText(Html.fromHtml("<font color=\"#999999\">收件人:  </font><font color=\"#666666\">" + "暂无</font>"));
        } else {
            tvAddressName.setText(Html.fromHtml("<font color=\"#999999\">收件人:  </font><font color=\"#666666\">" + snatchInfo.awardUserNick + "   " + snatchInfo.awardPhone + "</font>"));
        }

        if (TextUtils.isEmpty(snatchInfo.awardAddress)) {
            tvAddressLocation.setText(Html.fromHtml("<font color=\"#999999\">配送至:  </font><font color=\"#666666\">" + "暂无</font>"));
        } else {
            tvAddressLocation.setText(Html.fromHtml("<font color=\"#999999\">配送至:  </font><font color=\"#666666\">" + snatchInfo.awardAddress + "</font>"));
        }


        if (TextUtils.isEmpty(snatchInfo.expressName)) {
            tvLogisticsName.setText(Html.fromHtml("<font color=\"#999999\">物流信息:  </font><font color=\"#666666\">" + "暂无</font>"));
            tvLookLogistics.setVisibility(View.GONE);
            tvLookLogistics.setClickable(false);
            tvCopy.setVisibility(View.GONE);
        } else {
            tvLogisticsName.setText(Html.fromHtml("<font color=\"#999999\">物流信息:  </font><font color=\"#666666\">" + snatchInfo.expressName + "</font>"));
            tvLookLogistics.setVisibility(View.VISIBLE);
            tvLookLogistics.setClickable(true);
            tvCopy.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(snatchInfo.expressNum)) {
            tvLogisticsNumber.setText(Html.fromHtml("<font color=\"#999999\">运单编号:  </font><font color=\"#666666\">" + "暂无</font>"));
        } else {
            tvLogisticsNumber.setText(Html.fromHtml("<font color=\"#999999\">运单编号:  </font><font color=\"#666666\">" + snatchInfo.expressNum + "</font>"));
        }

        if (TextUtils.isEmpty(snatchInfo.sendRemark)) {
            tvRemark.setText(Html.fromHtml("<font color=\"#999999\">备注信息:  </font><font color=\"#666666\">" + "无备注</font>"));
        } else {
            tvRemark.setText(Html.fromHtml("<font color=\"#999999\">备注信息:  </font><font color=\"#666666\">" + snatchInfo.sendRemark + "</font>"));
        }

        GlideUtil.showImageWithSuffix(mContext, snatchInfo.prizeGoodsUrl, goodsIcon);
        tvName.setText(snatchInfo.prizeGoodsName);
        tvDate.setText("期号:  " + snatchInfo.snatchIssue);
        Spanned spanned = Html.fromHtml("<font color=\"#999999\">参与人次: </font><font color=\"#333333\">" + snatchInfo.joinCount + "人次</font>");
        tvEnterCount.setText(spanned);
        tvLuckNumber.setText(Html.fromHtml("<font color=\"#999999\">幸运号码: </font><font color=\"#fe5062\">" + snatchInfo.winNum + "</font>"));
        if (!TextUtils.isEmpty(snatchInfo.snatchAnnounceTime)) {
            tvAnnounceNumber.setText("揭晓时间:  " + DateUtils.timeStampToStr(Long.parseLong(snatchInfo.snatchAnnounceTime)));
        } else {
            tvAnnounceNumber.setText("揭晓时间:   暂无");
        }

        if (adapter.getFooterLayoutCount() == 0) {
            adapter.addFooterView(bottomView);
        }

        Logger.i("award-" + awardStatusOld);
        Logger.i("award-" + awardStatusNew);
        if (!awardStatusNew.equals(awardStatusOld)) {
            Logger.i("award-走了这里");
            adapter.removeAllFooterView();
            adapter.addFooterView(bottomView);
            awardStatusOld = awardStatusNew;
        }
    }

    private void initGoodsList(SnatchAwardDetailsEntity.SnatchInfoBean snatchInfo) {
        int listSize = 0;
        goodStatusList.clear();
        /**
         * 商品状态 0标识未开奖 1表示未设置收货地址 2待备货 3已发货 4已完成 5待发货
         */

        if (snatchInfo.awardStatus.equals("0")) {
            goodStatusList.clear();
        } else if (snatchInfo.awardStatus.equals("1") || snatchInfo.awardStatus.equals("2") || snatchInfo.awardStatus.equals("5")) {
            listSize = 1;
        } else if (snatchInfo.awardStatus.equals("3")) {
            listSize = 3;
        } else if (snatchInfo.awardStatus.equals("4")) {
            listSize = 4;
        }
        GoodsExpressItemEntity entity = null;
        for (int i = 0; i < listSize; i++) {
            if (i == 0) {
                //判断是awardStatus是1 还是2
                entity = new GoodsExpressItemEntity();
                if (snatchInfo.awardStatus.equals("1")) {
                    entity.goodStatus = "未设置收货地址";
                } else if (snatchInfo.awardStatus.equals("2")) {
                    entity.goodStatus = "等待备货";
                } else if (snatchInfo.awardStatus.equals("5")) {
                    entity.goodStatus = "等待发货";
                } else {
                    entity.goodStatus = "等待发货";
                }
                entity.date = snatchInfo.snatchAnnounceTime;
                goodStatusList.add(entity);
            } else if (i == 1) {
                entity = new GoodsExpressItemEntity();
                entity.goodStatus = "已发货";
                entity.date = snatchInfo.expressTime;
                goodStatusList.add(entity);
            } else if (i == 2) {
                entity = new GoodsExpressItemEntity();
                entity.goodStatus = "等待收货";
                entity.date = snatchInfo.expressTime;
                goodStatusList.add(entity);
            } else if (i == 3) {
                entity = new GoodsExpressItemEntity();
                entity.goodStatus = "已完成";
                entity.date = snatchInfo.confirmTime;
                goodStatusList.add(entity);
            }
        }
        Logger.i("goodsList ToString " + goodStatusList.toString() + "  size " + goodStatusList.size());
        Collections.reverse(goodStatusList);
        Logger.i("goodsList ToString " + goodStatusList.toString() + "  size " + goodStatusList.size());
        adapter.setNewData(goodStatusList);
    }

    private void initView() {
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("中奖详情");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new GoodsExpressListAdapter(R.layout.lucky_include_item_prize_goods_status);
        recycleView.setAdapter(adapter);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.lucky_color_red));
        refreshLayout.setRefreshing(false);
        v3Back.setOnClickListener(this);
        v3Shop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_share) {
            //分享
            share();
        } else if (i == R.id.tv_show_line) {
            String contentStr = btnShowLine.getText().toString().trim();
            if (contentStr.equals("晒单")) {
                Intent intent = new Intent(mContext, LuckyShowOffListActivity.class);
                intent.putExtra("activityId", activityIdValue);
                startActivityForResult(intent, Constants.LUCKY_SHOW);
            } else if (contentStr.equals("确认收货")) {
                confirmGoods();
            } else if (contentStr.equals("收货地址")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.AddressManageActivity"));
                intent.putExtra("FLAG", "orderIn");
                intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
                startActivityForResult(intent, REQUEST_CODE);
            } else if (contentStr.equals("待收货")) {

            }
        } else if (i == R.id.tv_refresh) {
            initData();
        } else if (i == R.id.v3Shop) {
            showMenu();
        } else if (i == R.id.ll_goods_content) {
            Intent intent = new Intent(mContext, LuckyActivityDetailActivity.class);
            intent.putExtra("activity_id", activityIdValue);
            intent.putExtra("type", "0");
            intent.putExtra("show_window", "0");
            startActivity(intent);
        } else if (i == R.id.tv_look_logistics) {
//            Intent intent = new Intent();
//            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
//            intent.putExtra("url", Ip.getWechatURL(mContext) + "m/expressInfo.html?com=" + snatchInfo.expressName + "&nu=" + snatchInfo.expressNum);
//            startActivity(intent);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.LookExpressActivity"));
            intent.putExtra("express_number", snatchInfo.expressNum);
            intent.putExtra("express_company", snatchInfo.expressName);
            intent.putExtra("express_img", snatchInfo.prizeGoodsUrl);
            startActivity(intent);
        } else if (i == R.id.tv_copy) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(snatchInfo.expressNum);
            showToast("复制成功");
        }
    }

    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.v3Shop);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (sp.getBoolean(Constants.SPKEY_STATE, false)) {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }

    private void confirmGoods() {
        showSystemV7Dialog("提醒", "请确定您已经收到货物，是否确认收货？", "确认", "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        refreshLayout.setRefreshing(true);
                        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                                .snatchConfirmGoods(activityIdValue, new Callback<HttpResultBean>() {
                                    @Override
                                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                                switch (response.body().code) {
                                                    case 1:
                                                        showToast(response.body().msg);
                                                        initData();
                                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_PRZIE_DETAIL, true, mContext);
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        }
                                        refreshLayout.setRefreshing(false);
                                    }

                                    @Override
                                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                                        refreshLayout.setRefreshing(false);
                                        showToast(R.string.aliwx_net_null_setting);
                                    }
                                });
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (null != data) {
                        UserAddressLists lists = (UserAddressLists) data.getSerializableExtra("USE_RADDRESS_LIST");
                        final String address_id = lists.address_id;

                        if (!TextUtils.isEmpty(address_id) && !TextUtils.isEmpty(activityIdValue)) {
                            showSystemV7Dialog(null, "我们会将您的夺宝商品发送到该地址，请耐心等待~", "确定", "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            break;
                                        case DialogInterface.BUTTON_POSITIVE:
                                            setAddress(address_id, activityId);
                                            dialog.dismiss();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void setAddress(String address_id, String activityId) {
        refreshLayout.setRefreshing(true);
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .snatchSetAddress(activityId, address_id, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        showToast(response.body().msg);
                                        initData();
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_PRZIE_DETAIL, true, mContext);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    public View getEmptyView() {
        View emptyView = null;
        if (null == emptyView) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }
        return emptyView;
    }

    public View getErrorView() {
        View errorView = null;
        if (null == errorView) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            TextView tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(this);
        }
        return errorView;
    }
}
