package com.yilian.mall.jd.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerItemDecoration;
import com.yilian.mall.adapter.decoration.DividerItemDecorationNoLastLines;
import com.yilian.mall.jd.adapter.JDShippingAddressListAdapter;
import com.yilian.mall.jd.fragment.goodsdetail.JDGoodsInfoLeftFragment;
import com.yilian.mall.jd.presenter.JDShippingAddressManagerPresenter;
import com.yilian.mall.jd.presenter.impl.JDShippingAddressManagerPresenterImpl;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Subscription;

import static com.yilian.mall.jd.activity.JDEditShippingAddressActivity.ADD_ADDRESS_REQUEST_CODE;
import static com.yilian.mall.jd.activity.JDEditShippingAddressActivity.ADD_ADDRESS_RESULT_CODE;
import static com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity.DataBean.DEFAULT_JD_ADDRESS;
import static com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity.DataBean.UN_DEFAULT_JD_ADDRESS;

/**
 * 添加收货地址
 */
public class JDEditShippingAddressListActivity extends BaseAppCompatActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, JDShippingAddressManagerPresenter.View {
    public static final String TAG = "JDEditAddressListActivity";
    public static final int GET_JD_ADDRESS = 99;
    public static final int JD_ADDRESS_RESULT = 999;
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
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout btnAddJdAddress;
    private JDShippingAddressListAdapter jdShippingAddressListAdapter;
    private JDShippingAddressManagerPresenterImpl jdShippingAddressManagerPresenter;
    private List<JDShippingAddressInfoEntity.DataBean> shippingAddressList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jdedit_address_list);
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white), 60);
        jdShippingAddressManagerPresenter = new JDShippingAddressManagerPresenterImpl(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("收货地址管理");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration decoration = new DividerItemDecorationNoLastLines(
                mContext, LinearLayoutManager.HORIZONTAL, 30, R.color.color_666);
        recyclerView.addItemDecoration(decoration);
        jdShippingAddressListAdapter = new JDShippingAddressListAdapter(R.layout.item_jd_edit_address);
        recyclerView.setAdapter(jdShippingAddressListAdapter);
        jdShippingAddressListAdapter.bindToRecyclerView(recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        btnAddJdAddress = findViewById(R.id.btn_add_jd_address);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    private void initData() {
        getShippingAddressList();
    }

    private void initListener() {
        jdShippingAddressListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                选择地址返回上一页
                Intent data = new Intent();
                JDShippingAddressInfoEntity.DataBean item = (JDShippingAddressInfoEntity.DataBean) adapter.getItem(position);
                data.putExtra(TAG, item);
                setResult(JD_ADDRESS_RESULT, data);
                finish();
            }
        });
        jdShippingAddressListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                JDShippingAddressInfoEntity.DataBean item = (JDShippingAddressInfoEntity.DataBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_edit_jd_address:
//                        编辑地址
                        editJDAddress(item);
                        break;
                    case R.id.tv_delete_jd_address:
//                        删除地址
                        showSystemV7Dialog(null, "确认删除地址吗?", "确认", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        jdShippingAddressManagerPresenter.deleteShippingAddress(mContext, item.id);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                        break;
                    case R.id.check_box_jd_default_address:
                        if (item != null) {
                            jdShippingAddressManagerPresenter.setDefaultAddress(mContext, item.id, item.defaultAddress == DEFAULT_JD_ADDRESS ? UN_DEFAULT_JD_ADDRESS : DEFAULT_JD_ADDRESS);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        RxUtil.clicks(btnAddJdAddress, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                addJDAddress();
            }
        });
    }

    @Override
    public void getShippingAddressList() {
        Subscription subscription = jdShippingAddressManagerPresenter.getShippingAddressList(mContext);
        addSubscription(subscription);
    }

    private void editJDAddress(JDShippingAddressInfoEntity.DataBean dataBean) {
        JumpJdActivityUtils.toJDEditAddressActivity(this, dataBean);
    }

    private void addJDAddress() {
        JumpJdActivityUtils.toJDEditAddressActivity(this, null);
    }

    @Override
    public void startRefresh() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void updateShippingAddressStatus(String addressId, int isDefault) {
        for (JDShippingAddressInfoEntity.DataBean dataBean : shippingAddressList) {
            Logger.i("dateBean.id:" + dataBean.id + "  CheckBoxAddressId:" + addressId);
            if (isDefault == DEFAULT_JD_ADDRESS) {
//                设置默认地址时
                if (dataBean.id.equals(addressId)) {
//                    将新默认地址属性改为默认
                    dataBean.defaultAddress = DEFAULT_JD_ADDRESS;
                } else {
//                    旧默认地址属性改为非默认
                    if (dataBean.defaultAddress == DEFAULT_JD_ADDRESS) {
                        dataBean.defaultAddress = UN_DEFAULT_JD_ADDRESS;
                    }
                }
            } else {
//                取消默认地址时
                if (dataBean.id.equals(addressId)) {
//                    将旧默认地址属性改为非默认
                    if (dataBean.defaultAddress == DEFAULT_JD_ADDRESS) {
                        dataBean.defaultAddress = UN_DEFAULT_JD_ADDRESS;
                    }
                }
            }
//刷新列表状态
            jdShippingAddressListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除地址时,发送全局发送消息,所有使用该地址的地方都要清除该地址
     * @param addressId
     * {@link JDCommitOrderActivity#deleteAddress(JDShippingAddressInfoEntity.DataBean)}
     * {@link JDGoodsInfoLeftFragment#deleteAddress(JDShippingAddressInfoEntity.DataBean)}
     */
    @Override
    public void deleteShippingAddress(String addressId) {
        JDShippingAddressInfoEntity.DataBean deleteAddressBean = null;
        for (JDShippingAddressInfoEntity.DataBean dataBean : shippingAddressList) {
            if (dataBean.id.equals(addressId)) {
                deleteAddressBean = dataBean;
                EventBus.getDefault().post(dataBean);
                break;
            }
        }
        shippingAddressList.remove(deleteAddressBean);
        refreshData(shippingAddressList);
    }

    @Override
    public void stopRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void refreshData(List<JDShippingAddressInfoEntity.DataBean> data) {
        if (data != null && data.size() > 0) {
            shippingAddressList = data;
            jdShippingAddressListAdapter.setNewData(shippingAddressList);
        } else {
            setEmptyView();
        }
    }

    /**
     * 无数据时页面显示
     */
    private void setEmptyView() {
        jdShippingAddressListAdapter.setEmptyView(R.layout.view_no_data_jd_shipping_address);
        jdShippingAddressListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ADDRESS_REQUEST_CODE && resultCode == ADD_ADDRESS_RESULT_CODE) {
            getShippingAddressList();
        }
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
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        getShippingAddressList();
    }
}
