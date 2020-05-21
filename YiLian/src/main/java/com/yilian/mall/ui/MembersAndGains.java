package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.AllImcomeEntity;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.ChangeDateDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 会员及领奖励界面
 * Created by Administrator on 2016/3/23.
 */
public class MembersAndGains extends BaseActivity {

    @ViewInject(R.id.tv_time1)
    private TextView tvTime1;

    @ViewInject(R.id.tv_time2)
    private TextView tvTime2;

    @ViewInject(R.id.refresh_listview)
    private PullToRefreshListView listView;

    @ViewInject(R.id.no_data)
    private ImageView no_data;

    @ViewInject(R.id.view_line)
    private TextView view_line;

    ChangeDateDialog changeDateDialog;

    private LayoutInflater inflater;

    private String begin, end;

    private MyIncomeNetRequest request;
    private int page;
    private ArrayList<AllImcomeEntity.MemberIncome> list=new ArrayList<>();
    private String type;
    private AllIncomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_gains);

        ViewUtils.inject(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        begin = "2014-9-26";
        end = StringFormat.formatDate((System.currentTimeMillis() + "").substring(0, 10),"yyyy-MM-dd");
        tvTime2.setText(end);
        end="2018-12-12";
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        initView();
        getList();
    }

    private void initView() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                getList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getList();
            }
        });
    }

    private void getList() {
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }
        startMyDialog();
        request.AllIncomeNet(StringFormat.formatDateL(begin), StringFormat.formatDateL(end), type, page, new RequestCallBack<AllImcomeEntity>() {
            @Override
            public void onSuccess(ResponseInfo<AllImcomeEntity> responseInfo) {
                Logger.json(responseInfo.result.toString());
                switch (responseInfo.result.code) {
                    case 1:
                        stopMyDialog();
                        if (page==0){
                            list.clear();
                        }
                        if (responseInfo.result.list != null && responseInfo.result.list.size() != 0) {
                            list.addAll(responseInfo.result.list);
                        }
                        if (list.size()==0) {
                            listView.setVisibility(View.GONE);
                            no_data.setVisibility(View.VISIBLE);
                        } else {
                            adapter = new AllIncomeAdapter(inflater, list);
                            listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                        }
                        listView.onRefreshComplete();
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                listView.onRefreshComplete();
            }
        });
    }

    public void onBack(View view) {
        finish();
    }

    /**
     * 左侧时间
     *
     * @param view
     */
    public void showLeftDate(View view) {
        changeDateDialog = new ChangeDateDialog(mContext, inflater, new ChangeDateDialog.OnDateCListener() {
            @Override
            public void onClick(int mYear, int mMonth, int mDay) {
                tvTime1 = (TextView) findViewById(R.id.tv_time1);
                begin = mYear + "-" + mMonth + "-" + mDay;
                tvTime1.setText(begin);
                getList();
            }
        });
        changeDateDialog.show();
    }

    /**
     * 右侧时间
     *
     * @param view
     */
    public void showRightDate(View view) {
        changeDateDialog = new ChangeDateDialog(mContext, inflater, new ChangeDateDialog.OnDateCListener() {
            @Override
            public void onClick(int mYear, int mMonth, int mDay) {
                tvTime2 = (TextView) findViewById(R.id.tv_time2);
                end = mYear + "-" + mMonth + "-" + mDay;

                long a = Long.parseLong(StringFormat.formatDateL(end));
                long b = Long.parseLong((System.currentTimeMillis() + "").substring(0, 10));
                if (a - b <= 0) {
                    tvTime2.setText(end);
                }else {
                    tvTime2.setText(StringFormat.formatDate((System.currentTimeMillis() + "").substring(0, 10),"yyyy-MM-dd"));
                }
                getList();
            }
        });
        changeDateDialog.show();
    }
    //适配器
    public class AllIncomeAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<AllImcomeEntity.MemberIncome> list;

        public AllIncomeAdapter(LayoutInflater inflater, ArrayList<AllImcomeEntity.MemberIncome> list) {
            this.inflater = inflater;
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_member_income, null);
                holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (list != null) {
                if (list.get(position).member_lev != null) {
                    if (list.get(position).member_lev.equals("1")) {
                        holder.tv_name.setText("乐友");
                    } else if (list.get(position).member_lev.equals("2")) {
                        holder.tv_name.setText("从友");
                    } else if (list.get(position).member_lev.equals("3")) {
                        holder.tv_name.setText("众友");
                    }
                }

                Logger.i("------" + String.format("%.2f", Float.parseFloat(list.get(position).member_income) / 100));
                if (!TextUtils.isEmpty(list.get(position).member_income)){
                    holder.tv_number.setText("+" + String.format("%.2f", Float.parseFloat(list.get(position).member_income) / 100));
                }else {
                    holder.tv_number.setText("0");
                }

                holder.tv_time.setText(StringFormat.formatDate(list.get(position).deal_time));
            }
            return convertView;
        }
    }

    public class ViewHolder {
        public TextView tv_name, tv_level, tv_number, tv_time;

    }
}














