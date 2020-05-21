package com.yilian.mall.suning.activity.shippingaddress;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerItemDecoration;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SnShippingAddressListActivity extends BaseAppCompatActivity implements View.OnClickListener {
    public static final String CHECKED_ADDRESS_ID = "checked_address_id";
    public static final int NO_ADDRESS = 0;
    public static final int REQUEST_CODE = 2;
    public static final int RESULT_CODE = 1;
    public static final String TAG_CHOOSE_SHIPPING_ADDRESS = "tag_choose_shipping_address";
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
    private RecyclerView recyclerView;
    private AppCompatButton btnAddSnAddress;
    private ArrayList<SnShippingAddressInfoEntity.DataBean> shippingAddressDatas = new ArrayList<>();
    private SnShippingAddressAdapter adapter;
    private String oldCheckedAddressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sn_shipping_address_list);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("地址管理");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,1, 1,Color.parseColor("#e7e7e7")));

        btnAddSnAddress = (AppCompatButton) findViewById(R.id.btn_add_sn_address);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnAddSnAddress.setOnClickListener(this);
    }

    private void initData() {
        oldCheckedAddressId = getIntent().getStringExtra(CHECKED_ADDRESS_ID);
        adapter = new SnShippingAddressAdapter(shippingAddressDatas, oldCheckedAddressId);
        recyclerView.setAdapter(adapter);
        getSnShippingAddressListData();
    }

    private void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnShippingAddressInfoEntity.DataBean item = (SnShippingAddressInfoEntity.DataBean) adapter.getItem(position);
                Intent data = new Intent();
                data.putExtra(TAG_CHOOSE_SHIPPING_ADDRESS, item);
                setResult(RESULT_CODE, data);
                finish();
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_edit_shipping_address) {
                    SnShippingAddressInfoEntity.DataBean item = (SnShippingAddressInfoEntity.DataBean) adapter.getItem(position);
                    JumpSnActivityUtils.toSnEditShippingAddressActivity(
                            SnShippingAddressListActivity.this, SnEditShippingAddressActivity.EditType.EDIT, item);
                }

            }
        });
    }

    @SuppressWarnings("unchecked")
    private void getSnShippingAddressListData() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnShippingAddress("suning_address/suning_useraddress_list")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnShippingAddressInfoEntity>() {
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
                    public void onNext(SnShippingAddressInfoEntity snShippingAddressInfoEntity) {
                        List<SnShippingAddressInfoEntity.DataBean> data = snShippingAddressInfoEntity.data;
                        if (data.size() > NO_ADDRESS) {
                            adapter.setNewData(snShippingAddressInfoEntity.data);
                        } else {
                            RelativeLayout emptyView = new RelativeLayout(mContext);
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            emptyView.setLayoutParams(layoutParams);
                            emptyView.setBackgroundColor(Color.WHITE);

                            TextView textView = new TextView(mContext);
                            textView.setText("您尚未维护送货地址，现在去设置？");
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.CENTER_IN_PARENT);
                            textView.setLayoutParams(params);

                            emptyView.addView(textView);
                            textView.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mContext, R.mipmap.icon_sn_big), null, null);
                            adapter.setNewData(new ArrayList<>());
                            adapter.setEmptyView(emptyView);
                        }
                    }
                });
        addSubscription(subscription);
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
            case R.id.btn_add_sn_address:
                JumpSnActivityUtils.toSnEditShippingAddressActivity(this, SnEditShippingAddressActivity.EditType.NEW);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SnEditShippingAddressActivity.RESULT_CODE) {
            setResult(RESULT_CODE);
            getSnShippingAddressListData();
        }
    }


    class SnShippingAddressAdapter extends BaseQuickAdapter<SnShippingAddressInfoEntity.DataBean, BaseViewHolder> {

        private final String mCheckedAddressId;

        public SnShippingAddressAdapter(@Nullable List<SnShippingAddressInfoEntity.DataBean> data, @Nullable String checkedAddressId) {
            super(R.layout.item_sn_shipping_address, data);
            mCheckedAddressId = checkedAddressId;
        }

        @Override
        protected void convert(BaseViewHolder helper, SnShippingAddressInfoEntity.DataBean item) {
//            选中状态
            if (item.id.equals(mCheckedAddressId)) {
                helper.getView(R.id.iv_check).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.iv_check).setVisibility(View.INVISIBLE);
            }
//          姓名和电话
            helper.setText(R.id.tv_name_phone, String.format("%s    %s", item.name, PhoneUtil.formatPhoneMiddle4Asterisk(item.mobile)));
//            地址
            if (item.defaultAddress == SnShippingAddressInfoEntity.DataBean.DEFAULT_ADDRESS) {
                SpannableString spannableString = new SpannableString("  " + item.fullAddress);
                ImageSpan imageSpan = new ImageSpan(mContext, R.mipmap.icon_sn_default_shipping_address, DynamicDrawableSpan.ALIGN_BOTTOM);
                spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_shipping_address, spannableString);
            } else {
                helper.setText(R.id.tv_shipping_address, item.fullAddress);
            }

//            点击编辑地址
            helper.addOnClickListener(R.id.iv_edit_shipping_address);
        }
    }
}
