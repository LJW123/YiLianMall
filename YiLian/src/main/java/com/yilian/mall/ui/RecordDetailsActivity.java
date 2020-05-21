package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.RecordDetailsAdapter;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.JumpYlActivityUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.RecordListRetention;
import com.yilian.networkingmodule.entity.ExchangeTicketRecordEntity;
import com.yilian.networkingmodule.entity.RecordDetailsEntity;
import com.yilian.networkingmodule.entity.V3MoneyDetailEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mall.ui.V3MemberGiftActivity.TYPE_DAI_GOU_QUAN;

/**
 * @author Zg
 *         兑换券明细列表 详情
 */
public class RecordDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    /**
     * 收入支出分界 大于100是支出 小于100是收入
     */
    public static final int INT_100 = 100;
    private ImageView v3Back;
    private TextView v3Title;
    private FrameLayout v3Layout;
    //        头部
    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvAmount;
    private TextView tvDetail;
    private RecyclerView recyclerView;
    private RecordDetailsAdapter mRecordDetailsAdapter;

    private ExchangeTicketRecordEntity.ListBean angelChange;
    /**
     * 记录列表类型
     */
    private int recordType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details_comm);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.merchant_v3back);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setBackgroundColor(Color.WHITE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecordDetailsAdapter = new RecordDetailsAdapter();
        mRecordDetailsAdapter.bindToRecyclerView(recyclerView);
        initHeader();
    }

    private void initData() {
        v3Title.setText("明细详情");
        recordType = getIntent().getIntExtra("recordType", -1);
        if (recordType == -1) {
            showToast("数据处理异常");
            return;
        }
        switch (recordType) {
            case RecordListRetention.EXCHANGE_MINE:
                ExchangeTicketRecordEntity.ListBean mData = (ExchangeTicketRecordEntity.ListBean) getIntent().getSerializableExtra("data");
                setExchangeTicketRecordData(mData);
                break;
            case RecordListRetention.EXCHANGE_VERIFICATION:
                break;
            case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                angelChange = (ExchangeTicketRecordEntity.ListBean) getIntent().getSerializableExtra("data");
                if (angelChange != null) {
                    getAngelChangeDetail();
                }
                break;
            default:
                break;
        }
    }

    private void initListener() {

        v3Back.setOnClickListener(this);

    }

    private void initHeader() {
        //        头部
        View headerView = View.inflate(mContext, R.layout.header_v3_money_detail_1, null);
        ivIcon = (ImageView) headerView.findViewById(R.id.iv_icon);
        tvName = (TextView) headerView.findViewById(R.id.tv_name);
        tvAmount = (TextView) headerView.findViewById(R.id.tv_amount);
        tvDetail = (TextView) headerView.findViewById(R.id.tv_detail);
        if (mRecordDetailsAdapter.getHeaderLayoutCount() <= 0) {
            mRecordDetailsAdapter.addHeaderView(headerView);
        }
    }

    /**
     * 兑换券详情
     *
     * @param entity
     */
    private void setExchangeTicketRecordData(ExchangeTicketRecordEntity.ListBean entity) {
        String amount = entity.amount;
        String img = entity.img;
        String topName = entity.typeMsg;
        String tradeStatus = "交易成功";
        int categoryType = entity.type;

        if (!TextUtils.isEmpty(img)) {
            com.yilian.mylibrary.GlideUtil.showCirIconNoSuffix(mContext, img, ivIcon);
        }
        tvName.setText(topName);
        List<RecordDetailsEntity.DataBean> list = new ArrayList<>();

        if (categoryType < INT_100) {
            tvAmount.setText("+" + MoneyUtil.getLeXiangBiNoZero(amount));
            list.add(new RecordDetailsEntity.DataBean("交易类型", "兑换券获得", 0));
            list.add(new RecordDetailsEntity.DataBean("账单分类", "获得", 0));
        } else {
            tvAmount.setText("-" + MoneyUtil.getLeXiangBiNoZero(amount));

            list.add(new RecordDetailsEntity.DataBean("交易类型", "兑换券使用", 0));
            list.add(new RecordDetailsEntity.DataBean("账单分类", "使用", 0));  }
        tvDetail.setText(tradeStatus);


        list.add(new RecordDetailsEntity.DataBean("创建时间", DateUtils.timeStampToStr(entity.time), 1));
        list.add(new RecordDetailsEntity.DataBean("关联订单号", entity.order, 0));
        if (entity.type == RecordListRetention.TYPE_4 || entity.type == RecordListRetention.TYPE_105) {
            TextView showGivenFooter = new TextView(mContext);
            showGivenFooter.setBackgroundColor(Color.WHITE);
            showGivenFooter.setText("查看来往记录");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, DPXUnitUtil.dp2px(mContext, 10), 0, 0);
            showGivenFooter.setLayoutParams(layoutParams);
            showGivenFooter.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
            showGivenFooter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            showGivenFooter.setPadding(DPXUnitUtil.dp2px(mContext, 15f), DPXUnitUtil.dp2px(mContext, 18f)
                    , DPXUnitUtil.dp2px(mContext, 15f), DPXUnitUtil.dp2px(mContext, 18f));
            showGivenFooter.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.jiantou_order), null);
            mRecordDetailsAdapter.addFooterView(showGivenFooter);
            RxUtil.clicks(showGivenFooter, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    JumpYlActivityUtils.toV3MoneyDealingsActivity(mContext, entity.userPhone,
                            entity.userName, 1, entity.userId,
                            "integral_give_away", TYPE_DAI_GOU_QUAN);
                }
            });
        }
        mRecordDetailsAdapter.setNewData(list);
    }

    @SuppressWarnings("unchecked")
    private void getAngelChangeDetail() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getAngelChangeDetail("account/get_angel_change_detail", angelChange.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<V3MoneyDetailEntity>() {
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
                    public void onNext(V3MoneyDetailEntity entity) {
                        setAngelChangeDetail(entity);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 天使详情
     *
     * @param entity
     */
    private void setAngelChangeDetail(V3MoneyDetailEntity entity) {
        String amount = entity.amount;
        String img = angelChange.img;
        String topName = angelChange.typeMsg;
        String tradeStatus = entity.tradeStatus;
        int categoryType = entity.type;

        if (!TextUtils.isEmpty(img)) {
            com.yilian.mylibrary.GlideUtil.showCirIconNoSuffix(mContext, img, ivIcon);
        }
        tvName.setText(topName);
        if (categoryType < INT_100) {
            tvAmount.setText("+" + amount);
            if (categoryType == 1) {
                View footerView = View.inflate(mContext, R.layout.activity_record_details_footer, null);
                TextView tvDescribe = footerView.findViewById(R.id.tv_describe);
                tvDescribe.setText("产生时的天使价格为：¥" + entity.angelValue);
                if (mRecordDetailsAdapter.getFooterLayoutCount() <= 0) {
                    mRecordDetailsAdapter.addFooterView(footerView);
                }
            }
        } else {
            tvAmount.setText("-" + amount);
        }
        tvDetail.setText(tradeStatus);
        List<RecordDetailsEntity.DataBean> list = new ArrayList<>();
        for (V3MoneyDetailEntity.DataBean item : entity.data) {
            list.add(new RecordDetailsEntity.DataBean(item.title, item.content, item.lineType));
        }
        mRecordDetailsAdapter.setNewData(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            default:
                break;
        }
    }
}
