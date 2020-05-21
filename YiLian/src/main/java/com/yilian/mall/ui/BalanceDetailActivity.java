package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BaseListAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.AssetsRecordList;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 奖励/奖券明细
 */
public class BalanceDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private static final int REQUEST_SCREEN = 99;//跳转筛选界面
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private TextView tvMonthLast;
    private PullToRefreshListView listView;
    private String type, screenType = "", startTime = "", endTime = "";
    private int page = 0;
    private ArrayList<AssetsRecordList.AssetsList> myBalanceList = new ArrayList<>();
    private MyBalanceListAdapter adapter;
    private LinearLayout llScreen;
    private TextView tvSelectScreenType;
    private TextView tvSelectTime;
    private TextView tvExpenditure;
    private TextView tvIncome;
    private ImageView ivNothing;
    private String screenTypeValue;
    private int dayTime;
    private LinearLayout llExpend;
    private String defaultStartTime = "", defaultEndTime = "";//请求数据时的传参
    private HashMap<Integer, Boolean> flageMonth = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_detail);
        type = getIntent().getStringExtra("type");
        dayTime = 24 * 60 * 60 - 1;
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        switchScreenViewIsShow();
        initListener();
    }

    private void initListener() {
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                switchInitData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                switchInitData();

            }
        });

        listView.setOnScrollListener(new ListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (myBalanceList != null && myBalanceList.size() > 0) {
                    if (firstVisibleItem >= 1) {
                        firstVisibleItem--;
//                        if (flageMonth.get(firstVisibleItem)) { //第0条数据item 是1
//                            tvMonthLast.setText(myBalanceList.get(firstVisibleItem).month + "月");
//                        } else {
//                            tvMonthLast.setText(myBalanceList.get(firstVisibleItem).month + "月");
//                        }

                        String month = myBalanceList.get(firstVisibleItem).month;
                        String presentMonth = getPresentMonth();
                        Logger.i("当前月份：" + presentMonth + " month:" + month);
                        if (month.equals(String.valueOf(getPresentMonth()))) {
                            tvMonthLast.setText("本月");
                        } else {
                            tvMonthLast.setText(month + "月");
                        }

                    }
                }
            }
        });

    }

    /**
     * 获取当前月份 小于10月份的添“0”
     *
     * @return
     */
    @NonNull
    private String getPresentMonth() {
        String presentMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        if (presentMonth.length() < 2) {
            presentMonth = "0" + presentMonth;
        }
        return presentMonth;
    }

    private void switchInitData() {
        switch (type) {
            case "2":
                initCashChangeData();
                break;
            case "0":
            case "1":
                initBalanceOrIntegralData();
                break;
        }
    }

    private void switchScreenViewIsShow() {
        Logger.i("isShow    startTime  " + startTime + " endTime  " + endTime + " screenType  " + screenType + "  ScreenType " + screenType);
        if (!TextUtils.isEmpty(screenType) || !TextUtils.isEmpty(startTime) || !TextUtils.isEmpty(endTime)) {
            llScreen.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(screenType)) {
                tvSelectScreenType.setVisibility(View.VISIBLE);
                tvSelectScreenType.setText(screenTypeValue);
            } else {
                tvSelectScreenType.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                defaultStartTime = startTime;
                //有开始和结束的时间就把当前系统的时间传递过去
                defaultEndTime = getEndTime(endTime, dayTime);
                tvSelectTime.setVisibility(View.VISIBLE);
                tvSelectTime.setText(DateUtils.formatDate(Long.parseLong(startTime)) + "至" +
                        DateUtils.formatDate(Long.parseLong(endTime)));
                llExpend.setVisibility(View.VISIBLE);
            } else if (TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime)) {
                defaultStartTime = "";
                defaultEndTime = "";
                tvSelectTime.setVisibility(View.GONE);
                llExpend.setVisibility(View.GONE);
            } else {
                if (!TextUtils.isEmpty(endTime)) {
                    //有结束，没有开始
                    defaultStartTime = endTime;
                    defaultEndTime = getEndTime(endTime, dayTime);
                    tvSelectTime.setVisibility(View.VISIBLE);
                    tvSelectTime.setText(DateUtils.formatDate(Long.parseLong(endTime)));
                    llExpend.setVisibility(View.VISIBLE);
                } else {
                    //有开始没有结束
                    defaultStartTime = startTime;
                    defaultEndTime = getEndTime(startTime, dayTime);
                    tvSelectTime.setVisibility(View.VISIBLE);
                    tvSelectTime.setText(DateUtils.formatDate(Long.parseLong(startTime)));
                    llExpend.setVisibility(View.VISIBLE);
                }
            }
        } else {
            llScreen.setVisibility(View.GONE);
            tvSelectScreenType.setText("");
            tvSelectTime.setText("");
            defaultStartTime = "";
            defaultEndTime = "";
        }
        page = 0;//重刷界面
        switchInitData();
    }

    private void initCashChangeData() {
        startMyDialog(false);
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .getCashChange(String.valueOf(page), defaultStartTime, defaultEndTime, new Callback<AssetsRecordList>() {
                    @Override
                    public void onResponse(Call<AssetsRecordList> call, Response<AssetsRecordList> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        ArrayList<AssetsRecordList.AssetsList> cashChangeLists = response.body().lists;
                                        initListData(cashChangeLists, response);
                                        break;
                                }
                            }
                            stopMyDialog();
                            listView.onRefreshComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<AssetsRecordList> call, Throwable t) {
                        stopMyDialog();
                        listView.onRefreshComplete();
                    }
                });
    }

    /**
     * 因为获取的时间默认是零点的时间
     *
     * @param endTime
     * @param dayTime
     * @return
     */
    private String getEndTime(String endTime, int dayTime) {
        return String.valueOf(Long.parseLong(endTime) + dayTime);
    }

    private void initBalanceOrIntegralData() {
        startMyDialog(false);
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .getMyAsstsRecordList(String.valueOf(page), type, screenType, defaultStartTime, defaultEndTime, new Callback<AssetsRecordList>() {
                    @Override
                    public void onResponse(Call<AssetsRecordList> call, Response<AssetsRecordList> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        ArrayList<AssetsRecordList.AssetsList> balanceOrIntegralLists = response.body().lists;
                                        initListData(balanceOrIntegralLists, response);
                                        break;
                                }
                            }
                            stopMyDialog();
                            listView.onRefreshComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<AssetsRecordList> call, Throwable t) {
                        stopMyDialog();
                        listView.onRefreshComplete();
                    }
                });
    }

    private void initListData(ArrayList<AssetsRecordList.AssetsList> lists, Response<AssetsRecordList> response) {
        switch (type) {
            case "0"://奖励
                tvIncome.setText("领奖励:" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(response.body().income)));
                tvExpenditure.setText("扣除:" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(response.body().expend)));
                break;
            case "1":
                tvIncome.setText("获得:" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(response.body().income)));
                tvExpenditure.setText("扣除:" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(response.body().expend)));
                break;
            case "2":
                tvIncome.setText("累计领奖励:" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(response.body().income)));
                tvExpenditure.setText("");
                break;
        }
        Logger.i("list  " + lists.size());
        if (null != lists && lists.size() > 0) {
            tvMonthLast.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            ivNothing.setVisibility(View.GONE);
            if (page > 0) {
                myBalanceList.addAll(lists);
            } else {
                myBalanceList.clear();
                myBalanceList.addAll(lists);
                if (null == adapter) {
                    adapter = new MyBalanceListAdapter(myBalanceList);
                    listView.setAdapter(adapter);
                }
            }
            setFlagMonth(myBalanceList);
            adapter.notifyDataSetChanged();
        } else {
            if (page > 0) {
                showToast("暂无更多");
                tvMonthLast.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.GONE);
                ivNothing.setVisibility(View.VISIBLE);
                tvMonthLast.setVisibility(View.GONE);
            }
        }
    }

    private void setFlagMonth(ArrayList<AssetsRecordList.AssetsList> myBalanceList) {
        for (int i = 0; i < myBalanceList.size(); i++) {
            flageMonth.put(i, false);
        }
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.GONE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.GONE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tvMonthLast = (TextView) findViewById(R.id.tv_month_last);

        listView = (PullToRefreshListView) findViewById(R.id.listView);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        switch (type) {
            case "0"://奖励
                v3Title.setText("奖励明细");
                tvRight.setText("筛选");
                //直接打开筛选界面
                jumpToScreenActivity();
                break;
            case "1"://奖券
                v3Title.setText("奖券明细");
                tvRight.setText("筛选");
                //直接打开筛选界面
                jumpToScreenActivity();
                break;
            case "2":
                v3Title.setText("领奖励明细");
                v3Left.setVisibility(View.INVISIBLE);
                tvRight.setText("选择时间");
                break;
        }
        llScreen = (LinearLayout) findViewById(R.id.ll_screen);
        tvSelectScreenType = (TextView) findViewById(R.id.tv_select_screen_type);
        tvSelectTime = (TextView) findViewById(R.id.tv_select_time);
        tvExpenditure = (TextView) findViewById(R.id.tv_expenditure);
        tvIncome = (TextView) findViewById(R.id.tv_income);
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);
        llExpend = (LinearLayout) findViewById(R.id.ll_expend);


        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvSelectScreenType.setOnClickListener(this);
        tvSelectTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                jumpToScreenActivity();
                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.tv_select_screen_type:
                jumpToScreenActivity();
                break;
            case R.id.tv_select_time:
                jumpToScreenActivity();
                break;
        }
    }

    public void jumpToScreenActivity() {
        Intent intent = new Intent(mContext, BalanceScreenActivity.class);
        intent.putExtra("type", type);
        //传递数据为了下个界面有选择过筛选条件的显示状态，没有就为空
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("screenType", screenType);
        intent.putExtra("screenTypeValue", screenTypeValue);
        startActivityForResult(intent, REQUEST_SCREEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("RequestCode  " + requestCode + "  resultCode  " + resultCode);
        switch (requestCode) {
            case REQUEST_SCREEN:
                if (resultCode == RESULT_OK) {
                    startTime = data.getStringExtra("startTime");
                    endTime = data.getStringExtra("endTime");
                    screenType = data.getStringExtra("screenType");
                    screenTypeValue = data.getStringExtra("screenTypeValue");
                    Logger.i("RESULTCODE  starTime  " + startTime + "  endTIme  " + endTime + " screenType " + screenType + "  screenVALUE  " + screenTypeValue);
                }
                break;
        }
    }

    public class MyBalanceListAdapter extends BaseListAdapter<AssetsRecordList.AssetsList> {
        private String lastMonth;
        BalanceHolder holder;

        public MyBalanceListAdapter(List<AssetsRecordList.AssetsList> lists) {
            super(lists);
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(context, R.layout.item_my_balance, null);
                holder = new BalanceHolder(view);
                view.setTag(holder);
            } else {
                holder = (BalanceHolder) view.getTag();
            }
            AssetsRecordList.AssetsList assetsList = datas.get(position);
            holder.tvMsg.setText(assetsList.typeMsg);
            holder.tvTime.setText(com.yilian.mall.utils.DateUtils.timeStampToStr(Long.parseLong(assetsList.payTime)));
            //收支判断
            if (!TextUtils.isEmpty(assetsList.type)) {
                if (Integer.parseInt(assetsList.type) > 100) {
                    //减少
                    holder.tvMoney.setText("-" + com.yilian.mall.utils.MoneyUtil.getLeXiangBi(assetsList.payCount));
                    holder.tvMoney.setTextColor(context.getResources().getColor(R.color.color_my_bule));
                    holder.ivIcon.setImageResource(R.mipmap.ic_balance_item_bule);

                } else {
                    //增加
                    holder.tvMoney.setText("+" + com.yilian.mall.utils.MoneyUtil.getLeXiangBi(assetsList.payCount));
                    holder.tvMoney.setTextColor(context.getResources().getColor(R.color.myinfo_red_bg));
                    holder.ivIcon.setImageResource(R.mipmap.ic_balance_item_red);
                }
            }
            //领奖励状态判断
            if ("108".equals(assetsList.type)) {//已驳回
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText(assetsList.balanceText);
                switch (assetsList.balanceStatus) {
                    case "2"://已驳回
                        holder.tvStatus.setTextColor(Color.WHITE);
                        holder.tvStatus.setBackgroundResource(R.drawable.balance_screen_item_bg);
                        holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialog("温馨提示", assetsList.refuseText, "", 0, Gravity.CENTER, "知道了", null, false, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                dialog.dismiss();
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }, mContext);
                            }
                        });

                        break;
                    default:
                        holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_detail_status));
                        holder.tvStatus.setBackgroundResource(R.drawable.balance_screen_item_bg_white);
                        holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        break;
                }

            } else {
                holder.tvStatus.setVisibility(View.GONE);
            }
            holder.tvMonth.setText(assetsList.month + "月");
            //第一条默认显示
            if (position == 0) {
                holder.tvMonth.setVisibility(View.VISIBLE);
                if (assetsList.month.equals(getPresentMonth())) {
                    tvMonthLast.setText("本月");
                } else {
                    tvMonthLast.setText(assetsList.month + "月");
                }
            } else {
                //那当前的数据跟上一条的数据 position--  ;做对比如果不同显示月份
                if (position >= 1) {
                    position--;
                    lastMonth = datas.get(position).month;
                    if (!lastMonth.equals(assetsList.month)) {
                        holder.tvMonth.setVisibility(View.VISIBLE);
                        Logger.i("tvMonth  " + position);
                        flageMonth.put(position + 2, true);
                    } else {
                        holder.tvMonth.setVisibility(View.GONE);
                    }

                }
            }
            return view;
        }

        class BalanceHolder {
            ImageView ivIcon;
            TextView tvMsg;
            TextView tvMoney;
            TextView tvTime;
            TextView tvStatus;
            TextView tvMonth;

            public BalanceHolder(View itemView) {
                this.ivIcon = (ImageView) itemView.findViewById(R.id.iv);
                this.tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
                this.tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                this.tvMsg = (TextView) itemView.findViewById(R.id.tv_describe);
                this.tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
                this.tvMonth = (TextView) itemView.findViewById(R.id.tv_item_mouth);
            }
        }
    }

}
