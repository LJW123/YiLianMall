package com.yilian.mall.ui.mvp.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.CerResultEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.activity.MerchantRevenueBindCardActivity;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 实名认证成功页面 该页面逻辑简单 不适用mvp 直接MV处理
 */
public class CertificationResultImplViewActivity extends BaseAppCompatActivity implements ICertificationResultView, View.OnClickListener {
    /**
     * 是否是从个人中心直接来到该界面
     */
    public static final String IS_FROM_USER_INFO_ACTIVITY = "isFromUserInfoActivity";
    public static final int IS_MERCHANT = 1;
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private ImageView ivHead;
    private TextView tvName;
    private TextView tvIdCardNumber;
    private TextView tvIsBind;
    private LinearLayout llName;
    private LinearLayout llIdCardNumber;
    private LinearLayout llStatus;
    private View llAddPublicBankCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_result_impl_view);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @SuppressWarnings("unchecked")
    private void getData() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getCerResult("get_auth_info")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CerResultEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CerResultEntity cerResultEntity) {
                        if (getIntent().getBooleanExtra(IS_FROM_USER_INFO_ACTIVITY,false)) {
//                            若是从个人中心直接来到该界面 则添加银行卡view不显示
                            llAddPublicBankCard.setVisibility(View.GONE);
                        }else{
                            if (cerResultEntity.isMerchant == IS_MERCHANT) {
                                llAddPublicBankCard.setVisibility(View.VISIBLE);
                            } else {
                                llAddPublicBankCard.setVisibility(View.GONE);
                            }
                        }

                        CerResultEntity.InfoBean info = cerResultEntity.info;
                        tvName.setText(info.userName);
                        tvIdCardNumber.setText(info.cardId);
                        switch (info.cardStatus) {
                            case 0:
//                                待上传证件
                                tvIsBind.setText("点击上传");
                                tvIsBind.setTextColor(Color.parseColor("#FF359DF4"));
                                tvIsBind.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.arrows_right_gray), null);
                                RxUtil.clicks(llStatus, new Consumer() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        startUploadIdCardImgActivity(info);
                                    }
                                });
                                break;
                            case 1:
//                                表示待审核
                                tvIsBind.setText("证件审核中");
                                tvIsBind.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                tvIsBind.setTextColor(Color.parseColor("#FF6DB247"));
                                RxUtil.clicks(llStatus, new Consumer() {
                                    @Override
                                    public void accept(Object o) throws Exception {

                                    }
                                });
                                break;
                            case 2:
//                                表示审核通过
                                tvIsBind.setText("审核通过");
                                tvIsBind.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                tvIsBind.setTextColor(Color.parseColor("#FF6DB247"));
                                RxUtil.clicks(tvIsBind, new Consumer() {
                                    @Override
                                    public void accept(Object o) throws Exception {

                                    }
                                });
                                break;
                            case 3:
//                                表示审核拒绝
                                tvIsBind.setText("审核拒绝");
                                tvIsBind.setTextColor(ContextCompat.getColor(mContext, R.color.color_red));
                                tvIsBind.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.arrows_right_gray), null);
                                RxUtil.clicks(tvIsBind, new Consumer() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        startUploadIdCardImgActivity(info);

                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void startUploadIdCardImgActivity(CerResultEntity.InfoBean info) {
        Intent intent = new Intent(mContext, UploadIdCardViewImplActivity.class);
        intent.putExtra("userName", info.userName);
        intent.putExtra("cardName", info.cardName);
        startActivity(intent);
    }

    private void initView() {
        llAddPublicBankCard = findViewById(R.id.ll_add_public_bank_card);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("实名认证");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        ivHead = (ImageView) findViewById(R.id.iv_head);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvIdCardNumber = (TextView) findViewById(R.id.tv_id_card_number);
        tvIsBind = (TextView) findViewById(R.id.tv_is_bind);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        llName = (LinearLayout) findViewById(R.id.ll_name);
        llName.setOnClickListener(this);
        llIdCardNumber = (LinearLayout) findViewById(R.id.ll_id_card_number);
        llIdCardNumber.setOnClickListener(this);
        llStatus = (LinearLayout) findViewById(R.id.ll_status);
        llStatus.setOnClickListener(this);
    }

    private void initData() {
    }

    private void initListener() {
        RxUtil.clicks(llAddPublicBankCard, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                startActivity(new Intent(mContext, MerchantRevenueBindCardActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
        }
    }
}
