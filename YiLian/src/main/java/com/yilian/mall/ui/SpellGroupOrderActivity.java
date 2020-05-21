package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.SpellGroupGridViewAdapter;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPSignGVEntity;
import com.yilian.mall.entity.UserDefaultAddressEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.entity.UserAddressLists;
import com.yilian.networkingmodule.entity.SpellGroupOrderInfoEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 拼团的订单详情
 */
public class SpellGroupOrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private TextView tvContent;
    private TextView tvAddress;
    private LinearLayout llAddress;
    private TextView tvTag;
    private ImageView ivIcon;
    private RelativeLayout rlContent;
    private TextView tvGoodsName;
    private TextView tvSku;
    private TextView tvGoodsCount;
    private TextView tvPrice;
    private ImageView ivWining;
    private TextView tvActualPrice;
    private TextView tvStatus1;
    private TextView tvOrderId;
    private TextView tvPayType;
    private TextView tvPayTime;
    private TextView tvJoinTime;
    private TextView tvConsulePhone;
    private NoScrollGridView gridView;
    private String orderId;
    private MallNetRequest mallNetRequest;
    private String addressId;
    private String phone;
    private String imageUrl;
    private JPNetRequest jpNetRequest;
    private SpellGroupGridViewAdapter adapter;
    private PullToRefreshScrollView scrollView;
    private String groupId;
    private LinearLayout llTel;
    private String telPhone;
    private String activityId;
    private String groupOrderId;
    private RelativeLayout rlSpellGroupContent;
    private String orderIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_group_order);
        orderId = getIntent().getStringExtra("orderId");
        initView();
        getDefaultAddress();
        initData();
        initListener();
    }

    private void initListener() {

        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                JPGoodsEntity itemView = (JPGoodsEntity) adapter.getItem(position);
                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);

                intent.putExtra("goods_id", itemView.JPGoodsId);
                intent.putExtra("filiale_id", itemView.JPFilialeId);
                intent.putExtra("tags_name", itemView.JPTagsName);
                startActivity(intent);
            }
        });
    }

    public void getDefaultAddress() {
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(mContext);
        }
        startMyDialog();
        mallNetRequest.getDefaultUserAddress(UserDefaultAddressEntity.class, new RequestCallBack<UserDefaultAddressEntity>() {
            @Override
            public void onSuccess(ResponseInfo<UserDefaultAddressEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        UserAddressLists addressLists = responseInfo.result.info;
                        addressId = addressLists.address_id;
                        phone = addressLists.phone;
                        tvContent.setText(addressLists.contacts + "    " + phone);
                        tvAddress.setText(addressLists.province_name + addressLists.city_name + addressLists.county_name + addressLists.fullAddress + addressLists.address);
                        stopMyDialog();
                        break;
                    case -3:
                        showToast("系统繁忙请稍后重试");
                        stopMyDialog();
                        break;
                    case -4:
                        new Intent(mContext, LeFenPhoneLoginActivity.class);
                        stopMyDialog();
                        break;
                    case -21:
                        showDialog("温馨提示", "请设置收货地址", null, 0, Gravity.CENTER, "确定", "取消", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        dialog.dismiss();
                                        selectAddress();
                                        break;
                                    case DialogInterface.BUTTON_NEUTRAL:
                                        dialog.dismiss();
                                        finish();
                                        break;
                                }
                            }
                        }, mContext);
                        stopMyDialog();
                        break;
                    default:
                        showToast(responseInfo.result.code);
                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    //跳转选择地址界面
    private void selectAddress() {
        Intent intent = new Intent(mContext, AddressManageActivity.class);
        intent.putExtra("FLAG", "orderIn");
        if (null == addressId) {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
        } else {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, addressId);
        }
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    UserAddressLists useAddressList = (UserAddressLists) data.getSerializableExtra("USE_RADDRESS_LIST");
                    Logger.i("useAddressList  " + useAddressList.address_id);
                    if (null != useAddressList.address_id) {

                    }
                    addressId = useAddressList.address_id;
                    phone = useAddressList.phone;
                    tvContent.setText(useAddressList.contacts + "    " + phone);
                    tvAddress.setText(useAddressList.province_name + useAddressList.city_name + useAddressList.county_name + useAddressList.fullAddress + useAddressList.address);
                }

                break;
        }
    }


    private void initData() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSpellGroupOrderInfoData(orderId, new Callback<SpellGroupOrderInfoEntity>() {
                    @Override
                    public void onResponse(Call<SpellGroupOrderInfoEntity> call, Response<SpellGroupOrderInfoEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        scrollView.onRefreshComplete();
                                        stopMyDialog();
                                        SpellGroupOrderInfoEntity.DataBean dataBean = response.body().data;
                                        groupId = dataBean.groupId;
                                        orderIndex = dataBean.orderIndex;
                                        activityId = dataBean.activityId;
                                        groupOrderId = dataBean.orderId;
                                        telPhone = dataBean.tel;
                                        initViewData(dataBean);
                                        break;
                                    default:
                                        scrollView.onRefreshComplete();
                                        Logger.i(getClass().getSimpleName() + response.body().code);
                                        stopMyDialog();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SpellGroupOrderInfoEntity> call, Throwable t) {
                        stopMyDialog();
                        scrollView.onRefreshComplete();
                    }
                });

        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        //获取的每日精品推荐
        jpNetRequest.signGridView(new RequestCallBack<JPSignGVEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPSignGVEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        ArrayList<JPGoodsEntity> goodsList = responseInfo.result.data.goodsList;
                        if (null != goodsList || goodsList.size() > 0) {
                            adapter = new SpellGroupGridViewAdapter(goodsList);
                            gridView.setAdapter(adapter);
                        } else {
                            gridView.setVisibility(View.GONE);
                        }
                        scrollView.onRefreshComplete();
                        stopMyDialog();
                        break;
                    default:
                        Logger.i(responseInfo.result.code + responseInfo.result.msg);
                        stopMyDialog();
                        scrollView.onRefreshComplete();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                scrollView.onRefreshComplete();
            }
        });
    }


    private void initViewData(SpellGroupOrderInfoEntity.DataBean dataBean) {
        tvTag.setText(dataBean.label);
        tvGoodsName.setText(dataBean.goodsName);
        imageUrl = dataBean.goodsIcon;
        if (imageUrl.contains("http://") || imageUrl.contains("https:.//")) {
            imageUrl = imageUrl + Constants.ImageSuffix;
        } else {
            imageUrl = Constants.ImageUrl + imageUrl + Constants.ImageSuffix;
        }
        GlideUtil.showImage(mContext, imageUrl, ivIcon);
        tvSku.setText(dataBean.goodsSkuName);
        tvGoodsCount.setText("×"+dataBean.goodsCount);
        tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(dataBean.amount)));
        tvActualPrice.setText(Html.fromHtml("实付：<font color=\"#FA5753\">" +MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(dataBean.amount))  + "</font>       （免运费）"));
        tvOrderId.setText(dataBean.orderId);
        tvPayType.setText(dataBean.payTypeName);
        if (!TextUtils.isEmpty(dataBean.payTime)){
            tvPayTime.setText(DateUtils.timeStampToStr(Long.parseLong(dataBean.payTime)));
        }
        if (!TextUtils.isEmpty(dataBean.orderTime)){
            tvJoinTime.setText(DateUtils.timeStampToStr(Long.parseLong(dataBean.orderTime)));
        }
        tvConsulePhone.setText("咨询电话：" + dataBean.tel);

    }

    private void initView() {
        scrollView = (com.pulltorefresh.library.PullToRefreshScrollView) findViewById(R.id.scrollView);
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("订单详情");
        tvRight = (TextView) findViewById(R.id.tv_right);

        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        llAddress = (LinearLayout) findViewById(R.id.ll_address);
        tvTag = (TextView) findViewById(R.id.tv_tag);
        ivIcon = (ImageView) findViewById(R.id.imageView4);
        rlContent = (RelativeLayout) findViewById(R.id.rl_content);
        tvGoodsName = (TextView) findViewById(R.id.tv_goods_name);
        tvSku = (TextView) findViewById(R.id.tv_sku);
        tvGoodsCount = (TextView) findViewById(R.id.tv_goods_count);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        ivWining = (ImageView) findViewById(R.id.iv_wining);
        tvActualPrice = (TextView) findViewById(R.id.tv_actual_price);
        tvStatus1 = (TextView) findViewById(R.id.tv_status1);
        tvOrderId = (TextView) findViewById(R.id.tv_order_id);
        tvPayType = (TextView) findViewById(R.id.tv_pay_type);
        tvPayTime = (TextView) findViewById(R.id.tv_pay_time);
        tvJoinTime = (TextView) findViewById(R.id.tv_join_time);
        tvConsulePhone = (TextView) findViewById(R.id.tv_consule_phone);
        gridView = (NoScrollGridView) findViewById(R.id.grid_view);
        llTel = (LinearLayout) findViewById(R.id.ll_tel);
        rlSpellGroupContent = (RelativeLayout) findViewById(R.id.rl_spell_group_content);

        v3Back.setOnClickListener(this);
        tvStatus1.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        llTel.setOnClickListener(this);
        rlSpellGroupContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.ll_address:
                selectAddress();
                break;
            case R.id.tv_status1:
                intent=new Intent(mContext,SpellGroupResultStatusActivity.class);
                intent.putExtra("groupId",groupId);
                intent.putExtra("activityId",activityId);
                intent.putExtra("orderId",groupOrderId);
                startActivity(intent);
                break;
            case R.id.ll_tel:
                callTel();
                break;
            case R.id.rl_spell_group_content:
                intent=new Intent(mContext,SpellGroupDetailsActivity.class);
                intent.putExtra("index",orderIndex);
                startActivity(intent);
                break;
        }
    }

    private void callTel() {
        if (telPhone == null) {
            return;
        }

        if (telPhone != null && telPhone.length() > 0) {
            showDialog(null, telPhone, null, 0, Gravity.CENTER, "拨打", "取消", true,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telPhone));
                                    startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    }, this.mContext);

        } else {
            showToast("亲,这家太懒了,没留电话哦!");
        }

    }
}
