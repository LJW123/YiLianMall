package com.leshan.ylyj.view.activity.bankmodel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.BankPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.CardPrivateDetailsInfo;
import rxfamily.entity.PublicCardDetailsEntity;


/**
 * 银行卡详情
 *
 * @author Ray_L_Pain
 */
public class CardPrivateDetaiInfolActivity extends BaseActivity implements View.OnClickListener {

    private TextView relieve_tv;
    private TextView cancel_tv, confirm_tv;
    private Dialog dialog;

    private BankPresenter presenter;
    private TextView mToolbarTitle;
    private ImageView mIconIv;
    /**
     * 中国银行
     */
    private TextView mCardNameTv;
    /**
     * 尾号1456
     */
    private TextView mCardNumTv;
    /**
     * 储蓄卡
     */
    private TextView mCardTypeTv;
    /**
     * 振玉
     */
    private TextView mNameTv;
    /**
     * 中国银行
     */
    private TextView mBankTv;
    /**
     * 河南郑州
     */
    private TextView mPrivanceTv;
    /**
     * 郑州花园路支行
     */
    private TextView mBranchTv;
    /**
     * 130****5277
     */
    private TextView mPhoneTv;
    /**
     * 解除绑定
     */
    private TextView mRelieveTv;


    private String cardId;

    //返回银行卡列表
    private final int BACK_CARD_LIST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_cards_detail);
        initView();
        initToolbar();
        setToolbarTitle("银行卡详情");
        hasBack(true);
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        relieve_tv = findViewById(R.id.relieve_tv);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mIconIv = (ImageView) findViewById(R.id.icon_iv);
        mCardNameTv = (TextView) findViewById(R.id.card_name_tv);
        mCardNumTv = (TextView) findViewById(R.id.card_num_tv);
        mCardTypeTv = (TextView) findViewById(R.id.card_type_tv);
        mNameTv = (TextView) findViewById(R.id.name_tv);
        mBankTv = (TextView) findViewById(R.id.bank_tv);
        mPrivanceTv = (TextView) findViewById(R.id.privance_tv);
        mBranchTv = (TextView) findViewById(R.id.branch_tv);
        mPhoneTv = (TextView) findViewById(R.id.phone_tv);
        mRelieveTv = (TextView) findViewById(R.id.relieve_tv);
    }

    @Override
    protected void initListener() {
        relieve_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        presenter = new BankPresenter(mContext, this);
        cardId = getIntent().getStringExtra("card_id");
        if (cardId != null) {
            startMyDialog(false);
            getPrivateCardDetailsInfo(cardId);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel_tv) {
            dialog.dismiss();

        } else if (i == R.id.confirm_tv) {
            dialog.dismiss();

        } else if (i == R.id.relieve_tv) {
            showV7dialog();
        }
    }

    @Override
    public void onCompleted() {
        stopMyDialog();
    }


    @Override
    public void onErrors(int flag, Throwable e) {
        showToast(e.getMessage());
        stopMyDialog();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        if (baseEntity instanceof CardPrivateDetailsInfo) {
            CardPrivateDetailsInfo info = (CardPrivateDetailsInfo) baseEntity;
            mCardNameTv.setText(info.data.cardBank);
            mBankTv.setText(info.data.cardBank);
            mPrivanceTv.setText(info.data.provinceCn+info.data.cityCn);
            mBranchTv.setText(info.data.branchBank);
            mPhoneTv.setText(info.data.obligatePhone);
            mNameTv.setText(info.data.cardHolder);
            GlideUtil.showImage(this, info.data.bankLogo, mIconIv);
            mCardTypeTv.setText(info.data.cardTypeCn);
            mCardNumTv.setText("尾号" + info.data.cardNumberR4);
        } else {
            showToast(baseEntity.msg);
            setResult(BACK_CARD_LIST);
            finish();
        }
    }
    /**
     * 获取私人银行卡信息
     *
     * @param cardIndex
     */
    private void getPrivateCardDetailsInfo(String cardIndex) {
        presenter.getPrivateCardDetailsInfo(mContext, cardIndex);
    }

    /**
     * 解除银行卡
     *
     * @param context
     * @param cardIndex
     */
    private void bankCardRelieved(Context context, String cardIndex) {
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
