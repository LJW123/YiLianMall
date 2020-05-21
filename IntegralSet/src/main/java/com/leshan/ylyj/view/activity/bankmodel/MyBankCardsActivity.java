package com.leshan.ylyj.view.activity.bankmodel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.MyBankCardsAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.yilian.networkingmodule.entity.CardMyBankCardsListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 我的银行卡---个人
 *
 * @author Ray_L_Pain
 */
public class MyBankCardsActivity extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView bank_card_rv;
    private MyBankCardsAdapter myBankCardsAdapter;
    private FrameLayout bottomLayout;

    private View emptyView, errorView;
    private TextView tvRefresh;
    private TextView tvAddBankCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_cards);
        initToolbar();
        setToolbarTitle("我的银行卡");
        hasBack(true);
        initView();
        initListener();
        initData();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        bank_card_rv = findViewById(R.id.bank_card_rv);
        bottomLayout = findViewById(R.id.layout_bottom);
        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeRefreshLayout.setRefreshing(true);
                    refreshData();
                }
            });
        }
        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_bank_card, null);
            tvAddBankCard = emptyView.findViewById(R.id.add_bank_card_tv);
            tvAddBankCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChooseDialog();
                }
            });
        }
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        bank_card_rv.setLayoutManager(manager);
        myBankCardsAdapter = new MyBankCardsAdapter(R.layout.item_my_bank_cards);
        bank_card_rv.setAdapter(myBankCardsAdapter);
    }

    /**
     * 刷新数据
     */

    private void refreshData() {
        getData();
    }

    @Override
    protected void initListener() {
        myBankCardsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CardMyBankCardsListEntity.DataBean cardEntity = (CardMyBankCardsListEntity.DataBean) adapter.getItem(position);
                int accountType = cardEntity.accountType;
                if ("0".equals(cardEntity.carIindex)) {
//                    老卡 该卡在未实名认证情况下绑定的 不能再使用 点击跳转到绑定银行卡 重新进行绑定
                    Intent intent = new Intent(mContext, BindPersonBankCardActivity.class);
                    intent.putExtra("bank_card_number", cardEntity.cardNumberShow);
                    startActivityForResult(intent, 0);
                } else {
                    switch (accountType) {
                        case 0:
                            //私人卡
                            Intent intent = new Intent(mContext, CardPrivateDetaiInfolActivity.class);
                            intent.putExtra("card_id", cardEntity.carIindex);
                            startActivityForResult(intent, 0);
                            break;
                        case 1:
                            //对公卡
                            Intent intent1 = new Intent(mContext, CardPublicDetailsActivity.class);
                            intent1.putExtra("card_id", cardEntity.carIindex);
                            startActivityForResult(intent1, 0);
                            break;
                    }
                }

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseDialog();
            }
        });
    }


    private PopupWindow popupWindow;

    private void showChooseDialog() {
        if (!isCert()) {
            SkipUtils.toCert(mContext);
            return;
        }
        if (null != popupWindow) {
            popupWindow.dismiss();
        } else {
            View v = getLayoutInflater().inflate(R.layout.library_module_popupwindow_choosebankcard, null, false);
            popupWindow = new PopupWindow(v, -1, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setAnimationStyle(R.style.AnimationFade);
            backgroundAlpha(0.2f);
            // 点击其他地方消失
            ColorDrawable cd = new ColorDrawable(0x000000);
            popupWindow.setBackgroundDrawable(cd);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
        }
        popupWindow.showAtLocation(MyBankCardsActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 弹窗点击进入绑定私卡
     *
     * @param v
     */
    public void cardSelf(View v) {
        Intent intent = new Intent(mContext, BindPersonBankCardActivity.class);
        startActivityForResult(intent, 0);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 弹窗点击进入绑定公卡
     * @param v
     */
    public void cardPublic(View v) {
        Intent intent = new Intent(mContext, BindPublicBindCardActivity.class);
        startActivityForResult(intent, 1);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    public void cancel(View v) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    @Override
    protected void initData() {
        swipeRefreshLayout.setRefreshing(true);
        getData();
    }

    /**
     * 获取银行卡列表数据
     */
    @SuppressWarnings("unchecked")
    private void getData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMyBankCardsList("userBank/my_user_bank")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CardMyBankCardsListEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        myBankCardsAdapter.setEmptyView(errorView);
                        bottomLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        stopMyDialog();
                    }

                    @Override
                    public void onNext(CardMyBankCardsListEntity entity) {
                        ArrayList<CardMyBankCardsListEntity.DataBean> newList = entity.data;
                        if (null == newList || newList.size() <= 0) {
                            myBankCardsAdapter.setNewData(new ArrayList<CardMyBankCardsListEntity.DataBean>());
                            myBankCardsAdapter.setEmptyView(emptyView);
                            bottomLayout.setVisibility(View.GONE);
                        } else {
                            myBankCardsAdapter.setNewData(newList);
                            bottomLayout.setVisibility(View.VISIBLE);
                        }
                        myBankCardsAdapter.notifyDataSetChanged();

                    }
                });

        addSubscription(subscription);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        swipeRefreshLayout.setRefreshing(true);
        refreshData();
    }
}
