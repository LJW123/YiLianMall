package com.yilian.mall.ui.transfer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.transfer.impl.TransferDaiGouQUanCategoryImpl;
import com.yilian.mall.ui.transfer.impl.TransferDaiGouQuanImpl;
import com.yilian.mall.ui.transfer.inter.ITransferAccount;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.JumpYlActivityUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.MemberGiftEntity1;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransferAccountActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static final String TAG = "TransferAccountActivity";
    public static final String TAG_TRANSFER = "TransferAccountActivityTransfer";
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private EditText etContactCode;
    private ImageView ivIconContact;
    private TextView tvRemind;
    private Button btnNext;
    private RecyclerView rvContactPerson;
    private ITransferAccount transferAccount;
    private ArrayList<Donee> mTransferAccountBeans;
    private PopupWindow mPopupWindow;
    private RecyclerView mPupRecycleView;
    private TransferAccountListAdapter mPupAdapter;
    private ArrayList<Donee> showPersonList = new ArrayList<>();
    private TransferAccountListAdapter mAdapter;
    private Donee transferAccountBean;
    private TransferDaiGouQUanCategoryImpl transferCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_account);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        etContactCode = (EditText) findViewById(R.id.et_contact_code);
        ivIconContact = (ImageView) findViewById(R.id.iv_icon_contact);
        tvRemind = (TextView) findViewById(R.id.tv_remind);
        btnNext = (Button) findViewById(R.id.btn_next);
        rvContactPerson = (RecyclerView) findViewById(R.id.rv_contact_person);
        rvContactPerson.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new TransferAccountListAdapter(R.layout.item_contact_person, showPersonList);
        rvContactPerson.setAdapter(mAdapter);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    private void initData() {
        transferAccount = (ITransferAccount) getIntent().getSerializableExtra(TAG);
        transferCategory = (TransferDaiGouQUanCategoryImpl) getIntent().getSerializableExtra(TAG_TRANSFER);
        transferAccount.getTransferAccountList(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Donee>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Donee> transferAccountBeans) {
                        mTransferAccountBeans = transferAccountBeans;
                    }
                });
        tvRemind.setText(transferAccount.getTransferRemind());
        v3Title.setText(transferAccount.getTitle());
    }

    private void initListener() {

        RxUtil.clicks(ivIconContact, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (TextUtils.isEmpty(etContactCode.getText().toString().trim())) {
                    showContactPup(mTransferAccountBeans);
                } else {
                    etContactCode.setText("");
                }
            }
        });

        etContactCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputContent = editable.toString().trim();
                showPersonList.clear();
                if (mTransferAccountBeans != null) {
                    for (Donee mTransferAccountBean : mTransferAccountBeans) {
                        if (!TextUtils.isEmpty(mTransferAccountBean.phone) &&
                                mTransferAccountBean.phone.contains(inputContent)) {
                            showPersonList.add(mTransferAccountBean);
                        }
                    }
                    mAdapter.setNewData(showPersonList);

                }
                if (TextUtils.isEmpty(inputContent)) {
                    //设置为删除叉号图标
                    ivIconContact.setImageResource(R.mipmap.icon_address_book);
                    rvContactPerson.setVisibility(View.GONE);
                } else {
                    rvContactPerson.setVisibility(View.VISIBLE);
                    ivIconContact.setImageResource(R.mipmap.icon_wrong);
                }
                checkCode(inputContent);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                transferAccountBean = (Donee) adapter.getItem(position);
                etContactCode.setText(transferAccountBean.phone);
//                showPersonList.clear();
//                mAdapter.setNewData(showPersonList);
                rvContactPerson.setVisibility(View.GONE);

            }
        });
    }

    private void showContactPup(ArrayList<Donee> transferAccountBeans) {
        if (mPopupWindow == null) {
            View contentView = View.inflate(mContext, R.layout.pup_contacts_layout, null);
            mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setAnimationStyle(R.style.anim_botom_2_top);
            (contentView.findViewById(R.id.v3Back)).setVisibility(View.GONE);
            TextView contontViewTitile = (TextView) contentView.findViewById(R.id.v3Title);
            contontViewTitile.setText("最近联系人");
            TextView tvLeft = (TextView) contentView.findViewById(R.id.tv_left);
            tvLeft.setText("取消");
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissPup();
                }
            });
            mPupRecycleView = (RecyclerView) contentView.findViewById(R.id.rv_contact_list);
            mPupRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
            mPupAdapter = new TransferAccountListAdapter(R.layout.item_pup_contacts_list, transferAccountBeans);
            mPupRecycleView.setAdapter(mPupAdapter);
            mPupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    transferAccountBean = (Donee) adapter.getItem(position);
                    dismissPup();
                    if (transferAccountBean != null) {
                        etContactCode.setText(transferAccountBean.phone);
                    }
                    rvContactPerson.setVisibility(View.GONE);
                }
            });

        }
        mPupAdapter.setNewData(transferAccountBeans);
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private boolean checkCode(String inputContent) {
        if (CommonUtils.isPhoneNumer(inputContent)) {
            btnNext.setBackgroundResource(R.drawable.bg_red_coner_5);
            return true;
        } else {
            btnNext.setBackgroundResource(R.drawable.bg_dim_red_coner_5);
            return false;
        }
    }

    private void dismissPup() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }
        super.onBackPressed();
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
            case R.id.btn_next:
//
                String inputPhone = etContactCode.getText().toString().trim();
                if (TextUtils.isEmpty(inputPhone)) {
                    showToast("请输入对方手机号");
                    return;
                }
                if (inputPhone.equals(sp.getString("phone", ""))) {
                    showToast("请勿给自己转赠");
                    return;
                }
                if (!CommonUtils.isPhoneNumer(inputPhone)) {
                    showToast("手机格式错误");
                    return;
                }
                checkTransferAccount(inputPhone);
                break;
            default:
                break;
        }
    }

    private void checkTransferAccount(String inputPhone) {
        startMyDialog();
        Subscription subscription = transferAccount.checkTransferAccount(mContext, inputPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MemberGiftEntity1>() {
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
                    public void onNext(MemberGiftEntity1 memberGiftEntity1) {
                        JumpYlActivityUtils.toTransferActivity(mContext,
                                new TransferDaiGouQuanImpl(
                                        new Donee(memberGiftEntity1.beUserid, memberGiftEntity1.name,
                                                inputPhone, memberGiftEntity1.photo)
                                        , transferCategory));
                    }
                });
        addSubscription(subscription);
    }

}
