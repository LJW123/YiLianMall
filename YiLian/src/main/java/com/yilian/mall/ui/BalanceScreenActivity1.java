package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BalanceScreenEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 奖励/奖券筛选
 */
public class BalanceScreenActivity1 extends BaseActivity implements View.OnClickListener {

    private LinearLayout date_ll;//隐藏时间筛选

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private RecyclerView recyclerView;
    private TextView tvClear;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private String type;
    private List<BalanceScreenEntity.ScreenEntity> screenList;
    private GridView gridView;
    private ScreenGridAdapter adapter;
    private Button btnQuery;
    private HashMap<Integer, Boolean> flags = new HashMap<>();
    private String startTime;
    private String endTime;
    private String screenTypeValue, screenType;
    private String selectScreenType = "", selectScreenTypeValue = "";
    private LinearLayout llScreenGrid;
    private boolean isShowSelectType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_screen);
        type = getIntent().getStringExtra("type");
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        selectScreenType = getIntent().getStringExtra("screenType");
        if (!TextUtils.isEmpty(selectScreenType)) {
            isShowSelectType = true;
        }
        selectScreenTypeValue = getIntent().getStringExtra("screenTypeValue");
        Logger.i("接受的数据 type " + type + " startTime  " + startTime + "  end Time " + endTime + " selectScreenType " + selectScreenType);
        initView();
        initListener();
    }

    private void initView() {
        date_ll = (LinearLayout) findViewById(R.id.date_ll);
        date_ll.setVisibility(View.GONE);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("筛选");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvClear = (TextView) findViewById(R.id.tv_clear);
        tvStartTime = (TextView) findViewById(R.id.tv_time_start);
        tvEndTime = (TextView) findViewById(R.id.tv_time_end);
        gridView = (GridView) findViewById(R.id.grid_view);
        btnQuery = (Button) findViewById(R.id.btn_query);
        if (!TextUtils.isEmpty(startTime)) {
            tvStartTime.setText(DateUtils.formatDate(Long.parseLong(startTime)));
        }
        if (!TextUtils.isEmpty(endTime)) {
            tvEndTime.setText(DateUtils.formatDate(Long.parseLong(endTime)));
        }
        llScreenGrid = (LinearLayout) findViewById(R.id.ll_screen_grid);
        switch (type) {
            case "2"://领奖励
                llScreenGrid.setVisibility(View.GONE);
                break;
            default:
                llScreenGrid.setVisibility(View.VISIBLE);
                initData();
                break;
        }
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
    }

    private void initListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //可以点击选择或不选择，必须单选
                if (screenList != null && screenList.size() > 0) {
                    Logger.i("setOnItemClickListener position  " + position + "  :: " + flags.get(position));
                    //点击当前条目的时候去从集合中取出当前的这个下标对应的数值
                    if (flags.get(position)) {
                        //选中变非选中
                        flags.put(position, false);
                    } else {
                        setFlags(screenList);
                        flags.put(position, true);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData() {
        startMyDialog(false);
        //判断是否为代购券
        if (type.equals(String.valueOf(V3MoneyDetailActivity.TYPE_3))) {
            //为代购券时  服务器要求传类型 2
            type = "2";
        }
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getScreenType(type, new Callback<BalanceScreenEntity>() {
                    @Override
                    public void onResponse(Call<BalanceScreenEntity> call, Response<BalanceScreenEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        screenList = response.body().data;
                                        if (null == screenList && screenList.size() <= 0) {
                                            showToast("暂无数据");
                                            return;
                                        }
                                        setFlags(screenList);//先存储标记
                                        if (adapter == null) {
                                            adapter = new ScreenGridAdapter(mContext, screenList);
                                        }

                                        gridView.setAdapter(adapter);
                                        break;
                                }
                            }
                            stopMyDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<BalanceScreenEntity> call, Throwable t) {
                        stopMyDialog();
                    }
                });


    }

    //设置默认都没有选中
    public void setFlags(List<BalanceScreenEntity.ScreenEntity> screenList) {
        for (int i = 0; i < screenList.size(); i++) {
            flags.put(i, false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.tv_time_start:
                showDataPicker(tvStartTime);
                break;
            case R.id.tv_time_end:
                showDataPicker(tvEndTime);
                break;
            case R.id.tv_clear:
                clear();
                break;
            case R.id.btn_query:
                query();
                break;
        }
    }

    /**
     * 时间选择的滚轮
     *
     * @param view
     */
    private void showDataPicker(TextView view) {
//        开始时间限制2017年7月1日
        Calendar calendarStart = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendarStart.set(Calendar.MONTH, 7 - 1);
        calendarStart.set(Calendar.YEAR, 2017);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
//        Calendar calendarEnd= Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
//        calendarEnd.set(Calendar.MONTH, 11);
//        calendarEnd.set(Calendar.YEAR,2037);
//        默认和截止时间限制 当天
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendar.set(Calendar.MINUTE, 0);
        //设置默认当前的时间
        String title = "选择时间";

        new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中时间回调
                getDate(date, view);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDividerColor(Color.DKGRAY)
                .setTitleText(title)
                .setContentSize(20)
                .setDate(calendar)
                .setRangDate(calendarStart, calendar)//设置起止时间
                .build()
                .show();
    }

    public void clear() {
        tvStartTime.setText("开始时间");
        tvEndTime.setText("结束时间");
        tvEndTime.setTextColor(mContext.getResources().getColor(R.color.color_999));
        tvStartTime.setTextColor(mContext.getResources().getColor(R.color.color_999));
        startTime = "";
        endTime = "";
    }

    private void query() {
        getFlagsData(); //获取查询的标签信息
        Logger.i("查询参数  startTime  " + startTime + "  endTIme  " + endTime + " screenType  " + screenType + " screenValue " + screenTypeValue);
        //查询的时候可以什么都不选返回查询的就是全部的数据，所以只需要判断选择两个日期的时候前后日期的顺序
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            if (Long.parseLong(startTime) > Long.parseLong(endTime)) {
                showToast("结束时间必须大于开始时间");
                return;
            } else {
                setActivityResult();
            }
        }
        setActivityResult();
    }

    private void getDate(Date date, TextView view) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(date);
        view.setText(dateStr);
        view.setTextColor(mContext.getResources().getColor(R.color.color_333));
        Logger.i("选择日期的date   " + date + " screenType  " + screenType + "  时间戳  " + date.getTime());
        switch (view.getId()) {
            case R.id.tv_time_start:
                //毫秒转为秒
                startTime = String.valueOf(date.getTime() / 1000);
                break;
            case R.id.tv_time_end:
//                该控件获取的Date是选择Date的0点，实际需求应该是24点，因此增加一天处理
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date tomorrow = calendar.getTime();
                endTime = String.valueOf(tomorrow.getTime() / 1000);
                break;
            default:
                break;
        }

    }

    /**
     * 获取当前选中下标的内容
     */
    public void getFlagsData() {
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i)) {
                screenType = screenList.get(i).type;
                screenTypeValue = screenList.get(i).typeValue;
            }
        }
    }

    private void setActivityResult() {
        Intent intent = new Intent();
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("screenType", screenType);
        intent.putExtra("screenTypeValue", screenTypeValue);
        Logger.i("查询参数传递前  startTime  " + startTime + "  endTIme  " + endTime + " screenType  " + screenType + " screenValue " + screenTypeValue);
        setResult(RESULT_OK, intent);

        finish();
    }

    public class ScreenGridAdapter extends com.yilianmall.merchant.adapter.BaseListAdapter<BalanceScreenEntity.ScreenEntity> {

        public ScreenGridAdapter(Context mContext, List<BalanceScreenEntity.ScreenEntity> screenList) {
            super(mContext, (ArrayList) screenList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_screen_type, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvScreenType.setText(datas.get(position).typeValue);
            //再次开启界面的时候判断是否有已经选中过的值
            //从上个界面传递过来的数据
            if (!TextUtils.isEmpty(selectScreenType)) {
                if (selectScreenType.equals(datas.get(position).type) && isShowSelectType) {
                    flags.put(position, true);
                    isShowSelectType = false;
                }
            }
            if (flags.get(position)) {
                holder.tvScreenType.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.select_time));
                holder.tvScreenType.setTextColor(Color.WHITE);
            } else {
                holder.tvScreenType.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.unselect_time));
                holder.tvScreenType.setTextColor(mContext.getResources().getColor(R.color.color_333));

            }
            Logger.i("getView  position  " + position + " selectScreenType  " + selectScreenType + " flags " + flags.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView tvScreenType;

        public ViewHolder(View view) {
            this.tvScreenType = (TextView) view.findViewById(R.id.tv_screen_value);
        }
    }
}

