package com.leshan.ylyj.view.activity.bankmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.eventmodel.FirstEvent;
import com.leshan.ylyj.presenter.BankPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.RxBus;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.CardPublicDetailsInfo;
import rxfamily.entity.PublicCardDetailsEntity;


/**
 * 对公卡详情
 */
public class CardPublicDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView mToolbarTitle;

    private TextView mCardNameTv;

    private TextView mCardNumTv;

    private TextView mCardTypeTv;

    private TextView mNameTv;

    private TextView mTaxTv;

    private TextView mBankTv;

    private TextView mPrivanceTv;

    private TextView mAdressTv;

    private TextView mPhoneNumTv;

    private ImageView mAlowIv;

    private TextView mRelieveTv;

    private BankPresenter presenter;

    private ImageView icon_iv;
    private TextView com_address_tv;

    private ImageView mIconIv;

    private String cardId;
    private final int BACK_CARD_LIST=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_card_details);
        presenter = new BankPresenter(mContext, this);
        initToolbar();
        setToolbarTitle("银行卡详情");
        hasBack(true);
        initView();
        initListener();
        initData();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {

        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mCardNameTv = (TextView) findViewById(R.id.card_name_tv);
        mCardNumTv = (TextView) findViewById(R.id.card_num_tv);
        mCardTypeTv = (TextView) findViewById(R.id.card_type_tv);
        mNameTv = (TextView) findViewById(R.id.name_tv);
        mTaxTv = (TextView) findViewById(R.id.tax_tv);
        mBankTv = (TextView) findViewById(R.id.bank_tv);
        mPrivanceTv = (TextView) findViewById(R.id.privance_tv);
        mPhoneNumTv = (TextView) findViewById(R.id.phone_num_tv);
        mAlowIv = (ImageView) findViewById(R.id.alow_iv);
        mRelieveTv = (TextView) findViewById(R.id.relieve_tv);
        icon_iv = findViewById(R.id.icon_iv);
        com_address_tv = findViewById(R.id.com_address_tv);
        mToolbarTitle.setOnClickListener(this);
        mCardNameTv.setOnClickListener(this);
        mCardNumTv.setOnClickListener(this);
        mCardTypeTv.setOnClickListener(this);
        mNameTv.setOnClickListener(this);
        mTaxTv.setOnClickListener(this);
        mBankTv.setOnClickListener(this);
        mPrivanceTv.setOnClickListener(this);
        mAdressTv = (TextView) findViewById(R.id.adress_tv);
        mPhoneNumTv.setOnClickListener(this);
        mAlowIv.setOnClickListener(this);

    }

    @Override
    protected void initListener() {
        mRelieveTv.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        cardId = getIntent().getStringExtra("card_id");
        getPublicCardInfo(mContext, cardId);
    }

    private BankPresenter mPresent;

    /**
     * 获取公卡信息
     *
     * @param context
     * @param cardIndex
     */
    private void getPublicCardInfo(Context context, String cardIndex) {
        startMyDialog(false);
        presenter.getCardPublicDetailsInfo(context, cardIndex);

    }

    @Override
    public void onCompleted() {
        stopMyDialog();
    }

    @Override
    public void onErrors(int flag, Throwable e) {
        stopMyDialog();
        showToast(e.getMessage());
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("获取数据结果");
        if (baseEntity instanceof CardPublicDetailsInfo) {
            CardPublicDetailsInfo info = (CardPublicDetailsInfo) baseEntity;
            GlideUtil.showImage(this, info.data.bankLogo, icon_iv);
            mCardNameTv.setText(info.data.cardBank);
            mCardNumTv.setText("尾号为"+info.data.cardNumberR4);
            mCardTypeTv.setText(info.data.cardTypeCn);
            mNameTv.setText(info.data.cardHolder);
            mTaxTv.setText(info.data.taxCode);
            mBankTv.setText(info.data.cardBank);
            mPrivanceTv.setText(info.data.provinceCn+info.data.cityCn);
            mAdressTv.setText(info.data.branchBank);
            mPhoneNumTv.setText(info.data.obligatePhone);
            com_address_tv.setText(info.data.companyAddress);
            GlideUtil.showImageRadius(this, info.data.certImage, mAlowIv,3);
        }else {
            showToast(baseEntity.msg);
            setResult(BACK_CARD_LIST);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
       int id=v.getId();
       if (id==R.id.relieve_tv){
           showV7dialog();
       }
    }
    /**
     * 解除银行卡
     *
     * @param context
     * @param cardIndex
     */
    private void bankCardRelieved(Context context, String cardIndex) {
        startMyDialog(false);
        presenter.bankCardRelieved(context, cardIndex);
    }
    private void showV7dialog(){
        showSystemV7Dialog(null, "您确定要解除绑定该银行卡么", "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    bankCardRelieved(mContext,cardId);
                } else {
                    dialogInterface.dismiss();
                }
            }
        });
    }
}