package com.yilian.mall.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.Constants;

/**
 * 店内消费 支付结果
 */
public class MTPayForStoreResult extends BaseActivity implements View.OnClickListener {

    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private TextView tvPayMoney;
    private Button btnShareComment;
    private LinearLayout activityMtpayForStoreResult;
    private float orderPrice;
    private UmengDialog mShareDialog;
    JPNetRequest jpRequest;
    String share_type = "10"; //1.旗舰店分享:#
    String getedId = "0";
    String share_title, share_content, share_img, share_url, shareImg;
    String orderId; //分享回调时需要的参数
    private String voucher;//该笔订单支付后，赠送的零购券
    private String backCoupon;//该笔订单支付后，赠送的抵扣券
    private String orderTime;
    private TextView tvOrderId;
    private TextView tvGouwuquan;
    private TextView tvOrderTime;
    private TextView tvXianjinquan;
    private String payType;
    private String costCoupon;
    private TextView tvMoneyType;
    private TextView tvIconGouWuQuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtpay_for_store_result);
        initView();
        initData();
    }

    private void initView() {
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        tvPayMoney = (TextView) findViewById(R.id.tv_pay_money);
        btnShareComment = (Button) findViewById(R.id.btn_share_comment);
        activityMtpayForStoreResult = (LinearLayout) findViewById(R.id.activity_mtpay_for_store_result);
        tvOrderId = (TextView) findViewById(R.id.tv_order_id);
        tvGouwuquan = (TextView) findViewById(R.id.tv_gouwuquan);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvXianjinquan = (TextView) findViewById(R.id.tv_xianjinquan);
        tvMoneyType = (TextView) findViewById(R.id.tv_money_type);//零购券和抵扣券是赠送还是消耗，payType=4时是消耗，payType=5时是赠送
        tvIconGouWuQuan = (TextView) findViewById(R.id.tv_icon_gouwuquan);

        //        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.pay_result));
        ivRight2.setVisibility(View.GONE);

        ivLeft1.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnShareComment.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        //4.付款给商家支付（套餐店内消费） 5兑换中心扫码支付
        payType = intent.getStringExtra("payType");
        orderPrice = intent.getFloatExtra("orderPrice", 0);//支付金额
        orderId = intent.getStringExtra("order_id");//订单号
        costCoupon = intent.getStringExtra("costCoupon"); //订单消耗的抵扣券，在payType=5时有值
        backCoupon = intent.getStringExtra("backCoupon");//订单赠送抵扣券,在payType=4时有值
        voucher = intent.getStringExtra("voucher");//订单赠送零购券,在payType=4时有值
        orderTime = intent.getStringExtra("orderTime");//订单交易时间
        SharedPreferences sp = mContext.getSharedPreferences("UserInfor", 0);
        sp.edit().putString(Constants.Order_Id, orderId).commit();
        Logger.i("2017-1-17:" + orderId);

        tvPayMoney.setText("成功支付" + MoneyUtil.set¥Money(String.valueOf(orderPrice)) + "元");//获取的是处理过后的金额，这里不再处理
        tvOrderTime.setText(DateUtils.formatDate(NumberFormat.convertToLong(orderTime, 0L)));//交易时间;
        tvOrderId.setText(orderId);//订单号

        if("4".equals(payType)){//付款给商家，显示赠送零购券和抵扣券的金额
            tvGouwuquan.setText(MoneyUtil.getLeXiangBiNoZero(voucher));//赠送零购券数量
            tvXianjinquan.setText("+" + MoneyUtil.getLeXiangBiNoZero(backCoupon));//赠送抵扣券数量
            tvMoneyType.setText("赠送");
        }else if("5".equals(payType)){//付款给兑换中心，显示消耗抵扣券的金额
            tvGouwuquan.setVisibility(View.INVISIBLE);//零购券金额隐藏，但是要占位置，保证界面不混乱
            tvIconGouWuQuan.setVisibility(View.GONE);//零购券图标隐藏
            if(TextUtils.isEmpty(costCoupon)){
                costCoupon="0";
            }
            tvXianjinquan.setText(MoneyUtil.getLeXiangBiNoZero(costCoupon));
            tvMoneyType.setText("扣除");
        }

        getShareUrl();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left1:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.btn_share_comment:
                share();
                break;
        }
    }

    /**
     * 分享
     */
    private void share() {
        sp.edit().putBoolean(Constants.SPKEY_PAY_SUCCESS, true).commit();

        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
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
                            share_url,
                            orderId,
                            "pay_success")
                            .share();
                    finish();
                }
            });
        }
        mShareDialog.show();
    }

    private void getShareUrl() {
        if (jpRequest == null) {
            jpRequest = new JPNetRequest(mContext);
        }
        jpRequest.getShareUrl(share_type, getedId, new RequestCallBack<JPShareEntity>() {
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
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
            }
        });
    }
}
