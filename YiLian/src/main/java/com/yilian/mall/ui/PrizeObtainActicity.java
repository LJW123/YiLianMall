package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.UserDefaultAddressEntity;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.NoScrollRecyclerView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.entity.UserAddressLists;
import com.yilian.networkingmodule.entity.PrizeGetResultEntity;
import com.yilian.networkingmodule.entity.WheelOfFortuneAwardListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/4/28 0028.
 * 中奖订单领取
 */
public class PrizeObtainActicity extends BaseActivity implements View.OnClickListener {
    private static final int NOLOAIN_HINT = 1;
    private static final int NOADDRESS_HINT = 2;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private TextView tvContentStr;
    private TextView tvAddress;
    private LinearLayout llAddress;
    private NoScrollRecyclerView recyclerView;
    private MallNetRequest mallNetRequest;
    private String address_id;
    private PrizeGoodsAdapter adapter;
    private String remarkStr;
    private Button btnGetPrize;
    private String goods_id;
    private String sku_index;
    private String orderId;
    private WheelOfFortuneAwardListEntity.DataBean.ListBean prizeGoodsList;
    private String contentStr;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prize_obtain_activity);
        mContext = this;
        prizeGoodsList = (WheelOfFortuneAwardListEntity.DataBean.ListBean) getIntent().getSerializableExtra("prizeGoodsList");
        initView();
        getDefuateAddress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPrizeGoods();
    }

    private void initPrizeGoods() {
        Logger.i("type   "+prizeGoodsList.type +"  goods_price "+prizeGoodsList.goods_price+"  cost "+prizeGoodsList.goods_cost+"sku  "+prizeGoodsList.skuStr);
        if (adapter == null) {
            adapter = new PrizeGoodsAdapter();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("中奖订单领取");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.INVISIBLE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.INVISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tvContentStr = (TextView) findViewById(R.id.tv_identity);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        llAddress = (LinearLayout) findViewById(R.id.ll_address);
        recyclerView = (NoScrollRecyclerView) findViewById(R.id.recycler_view);
        btnGetPrize = (Button) findViewById(R.id.btn_get_prize);

        v3Back.setOnClickListener(this);
        btnGetPrize.setOnClickListener(this);
        llAddress.setOnClickListener(this);
    }

    public void getDefuateAddress() {

        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(mContext);
        }
        //如果有默认地址，则返回默认地址，如果没有默认地址，服务器会自动取地址列表第一条，
        startMyDialog();
        mallNetRequest.getDefaultUserAddress(UserDefaultAddressEntity.class, new RequestCallBack<UserDefaultAddressEntity>() {
            @Override
            public void onSuccess(ResponseInfo<UserDefaultAddressEntity> responseInfo) {
                int type = -1;
                switch (responseInfo.result.code) {
                    case 1:
                        //先判断是否是默认收货地址
                        UserAddressLists info = responseInfo.result.info;
                        address_id = info.address_id;
                        if (info.is_Default.equals("1")) {
                            Spanned spanned = Html.fromHtml("<font color  ='#f75a53'>[默认]</font>" + info.province_name +
                                    info.city_name + info.county_name + info.fullAddress + info.address);
                            tvAddress.setText(spanned);
                        } else {
                            address = info.province_name + info.city_name + info.county_name + info.fullAddress + info.address;
                            tvAddress.setText(address);
                        }
                        contentStr = info.contacts + "    " + info.phone;
                        tvContentStr.setText(contentStr);
                        stopMyDialog();
                        break;
                    case -4:
                        type = NOLOAIN_HINT;
                        showMyDialog("提示", "登录状态失效", "可能您的账号在其他设备登录，请重新登录", type);
                        stopMyDialog();
                        break;
                    case -21://地址列表没有数据
                        type = NOADDRESS_HINT;
                        showMyDialog("温馨提示", "请设置收货地址", null, type);
                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
                stopMyDialog();
            }
        });
    }

    private void showMyDialog(String title, String msg, String detailMsg, int type) {

        showDialog(title, msg, detailMsg, 0, Gravity.CENTER, "确定", "取消", false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        finish();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        if (type == NOADDRESS_HINT) {
                            jumpToAddressManageActivity();
                        } else if (type == NOLOAIN_HINT) {
                            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                        }
                        dialog.dismiss();
                        break;
                }
            }
        }, mContext);
    }

    private void jumpToAddressManageActivity() {
        Intent intent = new Intent(mContext, AddressManageActivity.class);
        intent.putExtra("FLAG", "orderIn");
        if (null != address_id) {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, address_id);//选中的标识
        } else {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
        }
        startActivityForResult(intent, 0);

        Logger.i(" jumpToAddressManageActivity  ");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Logger.i("onActivityResult  111111111 " + requestCode + "    " + RESULT_OK);
                    if (null != data.getExtras()) {
                        UserAddressLists userAddressList = (UserAddressLists) data.getExtras().getSerializable("USE_RADDRESS_LIST");
                        address_id = userAddressList.address_id;
                        contentStr = userAddressList.contacts + "    " + userAddressList.phone;
                        tvContentStr.setText(contentStr);
                        Logger.i("default_address   " + userAddressList.default_address + "  isDefault  " + userAddressList.is_Default);
                        if (userAddressList.default_address==1) {
                            Spanned spanned = Html.fromHtml("<font color  ='#f75a53'>[默认]</font>" + userAddressList.province_name + userAddressList.city_name
                                    + userAddressList.county_name + userAddressList.fullAddress + userAddressList.address);
                            tvAddress.setText(spanned);
                        } else {
                            tvAddress.setText(userAddressList.province_name + userAddressList.city_name + userAddressList.county_name
                                    + userAddressList.fullAddress + userAddressList.address);
                        }
                    }
                }
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.ll_address:
                jumpToAddressManageActivity();
                break;
            case R.id.btn_get_prize:
                getPrizeGoods();
                break;
        }
    }

    //确认领取
    public void getPrizeGoods() {
        Logger.e("goods_id " + goods_id + " remarkStr " + remarkStr + "  address_id " + address_id + " orderiD " + orderId + "  sku_index " + sku_index + " mContext " + mContext);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext)).
                getPrizeGoodsResult(goods_id, remarkStr, address_id, orderId, sku_index, new Callback<PrizeGetResultEntity>() {
                    @Override
                    public void onResponse(Call<PrizeGetResultEntity> call, Response<PrizeGetResultEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                PrizeGetResultEntity.DataBean dataBean = response.body().data;
                                Intent intent = new Intent(mContext, CounversionSucceedActivity.class);
                                intent.putExtra("downTime", dataBean.doneTime);
                                intent.putExtra("orderId", dataBean.orderId);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PrizeGetResultEntity> call, Throwable t) {
                        showToast(R.string.net_work_not_available);
                    }
                });
    }


    class PrizeGoodsAdapter extends RecyclerView.Adapter<PrizeGoodsAdapter.PrizeGoodsHoler> {

        @Override
        public PrizeGoodsAdapter.PrizeGoodsHoler onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = View.inflate(mContext, R.layout.list_item_goods_content, null);
            PrizeGoodsHoler holer = new PrizeGoodsHoler(view);
            return holer;
        }

        @Override
        public void onBindViewHolder(PrizeGoodsHoler holder, int position) {
            goods_id = prizeGoodsList.goods_id;
            sku_index = prizeGoodsList.sku_index;
            orderId = prizeGoodsList.orderId;
            imageLoader.displayImage(Constants.ImageUrl + prizeGoodsList.goods_icon + Constants.ImageSuffix, holder.ivGoodsIcon);
            holder.tvSupplicerName.setText(prizeGoodsList.supplier_name);
            holder.tvGoodsName.setText(prizeGoodsList.goodName);
            holder.tvSku.setText(sku_index);
            remarkStr = holder.etRemark.getText().toString().trim();
            //判断当前的物品是零购区的还是抵扣券区
            //零购券
            if ("5".equals(prizeGoodsList.goods_type)) {
                holder.llFen.setVisibility(View.VISIBLE);
                holder.tvGivePrice.setText(MoneyUtil.getLeXiangBi(prizeGoodsList.goods_price));
            } else {
                holder.llQuan.setVisibility(View.VISIBLE);
                holder.tvQuanPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(prizeGoodsList.goods_cost)));
                if (null != prizeGoodsList.goods_cost && null != prizeGoodsList.goods_price){
                    int valuePrice = (Integer.parseInt(prizeGoodsList.goods_price) - Integer.parseInt(prizeGoodsList.goods_cost));
                    holder.tvQuanCost.setText("+"+MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(valuePrice)));
                }
            }

        }


        @Override
        public int getItemCount() {
            return 1;
        }

        class PrizeGoodsHoler extends RecyclerView.ViewHolder {
            TextView tvSupplicerName;
            ImageView ivGoodsIcon;
            TextView tvGoodsName;
            TextView tvSku;
            TextView tvCount;
            TextView tvGivePrice;
            EditText etRemark;
            LinearLayout llFen;
            LinearLayout llQuan;
            TextView tvQuanPrice;
            TextView tvQuanCost;

            public PrizeGoodsHoler(View itemView) {
                super(itemView);
                this.ivGoodsIcon = (ImageView) itemView.findViewById(R.id.iv_goods_icon);
                this.tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
                this.tvSupplicerName = (TextView) itemView.findViewById(R.id.supplicer_name);
                this.tvSku = (TextView) itemView.findViewById(R.id.tv_sku);
                this.tvCount = (TextView) itemView.findViewById(R.id.tv_count);
                this.tvGivePrice = (TextView) itemView.findViewById(R.id.tv_give_price);
                this.etRemark = (EditText) itemView.findViewById(R.id.et_remark);
                this.llFen = (LinearLayout) itemView.findViewById(R.id.ll_fen);
                this.llQuan = (LinearLayout) itemView.findViewById(R.id.ll_quan);
                this.tvQuanCost = (TextView) itemView.findViewById(R.id.tv_quan_coust);
                this.tvQuanPrice = (TextView) itemView.findViewById(R.id.tv_quan_price);
            }
        }
    }


}
