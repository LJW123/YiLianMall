package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ContactOpersonAdapter;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.ContactSperson;
import com.yilian.networkingmodule.entity.MemberGiftEntity1;
import com.yilian.networkingmodule.entity.MyBalanceBeanEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author ZYH
 * @date 2017/12/8 0008
 */

public class V3MemberGiftActivity extends BaseAppCompatActivity implements View.OnClickListener {
    public static final int TO_DONATION_REQUEST_CODE = 0;
    private EditText etContactCode;
    private RecyclerView rvContactPerson;
    private ImageView ivIconContact;
    private List<ContactSperson.Data> contactPerson = new ArrayList<>();
    private boolean sucecess = false;
    private List<ContactSperson.Data> showPersonList = new ArrayList<>();
    private ContactOpersonAdapter mAdapter;
    private ContactOpersonAdapter mPupAdapter;
    private boolean isEmpty = true;
    private boolean isClickItem = false;
    private Button btNext;
    private TextView v3Title;
    private ImageView v3Back;
    private PopupWindow mPopupWindow;
    private RecyclerView mPupRecycleView;
    private TextView v3Left, tvNotice;
    private boolean canNext = false;
    /**
     * 奖券转赠
     */
    public static final int TYPE_INTEGRAL = 0;
    /**
     * 益豆转赠
     */
    public static final int TYPE_STRESS = 1;
    /**
     * 代购券转赠
     */
    public static final int TYPE_DAI_GOU_QUAN=2;
    /**
     * 0 奖券转赠
     * 1 益豆转赠
     * 默认为奖券转赠
     */
    private int type = TYPE_INTEGRAL;
    private MyBalanceBeanEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_gift_v3);
        initView();
        initListner();
        getContact();
    }

    private void initListner() {
        v3Back.setOnClickListener(this);
        btNext.setOnClickListener(this);
        ivIconContact.setOnClickListener(this);
        etContactCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!sucecess) {
                    getContact();
                }
                String code = editable.toString().trim();
                if (isClickItem) {
                    //光标放在末尾
                    Selection.setSelection(editable, code.length());
                    isClickItem = false;
                }
                if (TextUtils.isEmpty(code)) {
                    isEmpty = true;
                } else {
                    isEmpty = false;
                }
                updateShowContact(code);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ContactSperson.Data data = (ContactSperson.Data) adapter.getItem(position);
                isClickItem = true;
                String code = data.phone;
                etContactCode.setText(code);
                showPersonList.clear();
                mAdapter.setNewData(showPersonList);
                rvContactPerson.setVisibility(View.GONE);

            }
        });
    }

    private void checkCode(String code) {
        if (CommonUtils.isPhoneNumer(code)) {
            btNext.setBackgroundResource(R.drawable.bg_red_coner_5);
            canNext = true;
        } else {
            btNext.setBackgroundResource(R.drawable.bg_dim_red_coner_5);
            canNext = false;
        }
    }

    private void updateShowContact(String code) {
        showPersonList.clear();
        for (ContactSperson.Data data : contactPerson) {
            if (!TextUtils.isEmpty(data.phone) && data.phone.contains(code)) {
                showPersonList.add(data);
            }
        }
        //更新
        mAdapter.setNewData(showPersonList);
        if (isEmpty) {
            //设置为删除叉号图标
            ivIconContact.setImageResource(R.mipmap.icon_address_book);
            rvContactPerson.setVisibility(View.GONE);

        } else {
            rvContactPerson.setVisibility(View.VISIBLE);
            ivIconContact.setImageResource(R.mipmap.icon_wrong);
        }
        checkCode(code);
    }

    private void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        tvNotice = findViewById(R.id.tv_notice);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", TYPE_INTEGRAL);
        entity = intent.getParcelableExtra("balance_bean");
        if (type == TYPE_STRESS) {
            v3Title.setText(Constants.APP_PLATFORM_DONATE_NAME+"转赠");
            tvNotice.setText("注："+Constants.APP_PLATFORM_DONATE_NAME+"将实时转入对方账户，无法退回，请仔细核对。");
        } else {
            v3Title.setText("奖券转赠");
        }
        v3Title.setVisibility(View.VISIBLE);

        btNext = (Button) findViewById(R.id.btn_next);
        etContactCode = (EditText) findViewById(R.id.et_contact_code);
        rvContactPerson = (RecyclerView) findViewById(R.id.rv_contact_person);
        ivIconContact = (ImageView) findViewById(R.id.iv_icon_contact);
        mAdapter = new ContactOpersonAdapter(R.layout.item_contact_person, showPersonList);
        rvContactPerson.setLayoutManager(new LinearLayoutManager(mContext));
        rvContactPerson.setAdapter(mAdapter);
    }

    /**
     * 奖券转赠获取通讯录
     */
    @SuppressWarnings("unchecked")
    private void getIntergralContact() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).getContactsPerson("account/getContacts")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContactSperson>() {
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
                    public void onNext(ContactSperson person) {
                        List<ContactSperson.Data> data = person.data;
                        contactPerson.clear();
                        sucecess = true;
                        if (data != null || data.size() > 0) {
                            contactPerson.addAll(data);
                            if (mPupAdapter != null) {
                                mPupAdapter.setNewData(contactPerson);
                            }
                        }

                    }
                });
        addSubscription(subscription);

    }

    /**
     * 益豆转赠获取通讯录
     */
    @SuppressWarnings("unchecked")
    private void getStressDonationContact() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).getContactsPerson("account/getContractsBean")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContactSperson>() {
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
                    public void onNext(ContactSperson person) {
                        List<ContactSperson.Data> data = person.data;
                        contactPerson.clear();
                        sucecess = true;
                        if (data != null || data.size() > 0) {
                            contactPerson.addAll(data);
                            if (mPupAdapter != null) {
                                mPupAdapter.setNewData(contactPerson);
                            }
                        }

                    }
                });
        addSubscription(subscription);

    }


    /**
     * 获取通讯录方法
     */
    private void getContact() {
        if (type == TYPE_INTEGRAL) {
            getIntergralContact();
        } else {
            getStressDonationContact();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_icon_contact) {
            if (isEmpty) {
                //跳转到联系人
                showContactPup();
            } else {
                etContactCode.setText(null);
            }

        } else if (view.getId() == R.id.btn_next) {
            //下一步操作
            if (canNext) {
                String code = etContactCode.getText().toString().trim();
                if (code.equals(sp.getString("phone", ""))) {
                    showToast("请勿给自己转赠");
                } else {
                    getGiveManInfo(code);
                }

            }
        } else if (view.getId() == R.id.v3Back) {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
                return;
            }
            finish();
        } else if (view.getId() == R.id.tv_left) {
            dismissPup();

        }
    }

    @SuppressWarnings("unchecked")
    private void getGiveManInfo(String phone) {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getGiveManInfo("user_info1", phone)
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
                        Intent intent = new Intent(mContext, IntegralGiveAwayActivity.class);
                        intent.putExtra("type", type);
                        //跳转到奖券转赠界面
                        intent.putExtra("name", memberGiftEntity1.name);
                        intent.putExtra("photo", memberGiftEntity1.photo);
                        intent.putExtra("code", phone);
                        //比率
                        if (type==TYPE_INTEGRAL){
                            intent.putExtra("poundagePercent", memberGiftEntity1.poundagePercent);
                            intent.putExtra("avilableIntegral", memberGiftEntity1.availableIntegral);
                        }else {
                            intent.putExtra("balance_bean",entity);
                        }
                        intent.putExtra("be_userid", memberGiftEntity1.beUserid);
                        startActivityForResult(intent, TO_DONATION_REQUEST_CODE);
                        stopMyDialog();
                    }
                });
        addSubscription(subscription);
    }

    private void showContactPup() {
        if (mPopupWindow == null) {
            View contentView = View.inflate(mContext, R.layout.pup_contacts_layout, null);
            mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setAnimationStyle(R.style.anim_botom_2_top);
            (contentView.findViewById(R.id.v3Back)).setVisibility(View.GONE);
            TextView contontViewTitile = (TextView) contentView.findViewById(R.id.v3Title);
            contontViewTitile.setText("最近联系人");
            v3Left = (TextView) contentView.findViewById(R.id.tv_left);
            v3Left.setText("取消");
            v3Left.setVisibility(View.VISIBLE);
            v3Left.setOnClickListener(this);
            mPupRecycleView = (RecyclerView) contentView.findViewById(R.id.rv_contact_list);
            mPupRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
            mPupAdapter = new ContactOpersonAdapter(R.layout.item_pup_contacts_list, contactPerson);
            mPupAdapter.setPupContact(true);
            mPupRecycleView.setAdapter(mPupAdapter);
            mPupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    ContactSperson.Data data = (ContactSperson.Data) adapter.getItem(position);
                    dismissPup();
                    isClickItem = true;
                    etContactCode.setText(data.phone);
                    rvContactPerson.setVisibility(View.GONE);
                }
            });

        }
        mPupAdapter.setNewData(contactPerson);
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    protected void onDestroy() {
        dismissPup();
        super.onDestroy();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IntegralGiveAwayActivity.INTERGRAL_GIVE_RESULT_CODE) {
            finish();
        }
    }
}
