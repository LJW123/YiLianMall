package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.SnatchDetailsEntity;
import com.yilian.mall.http.SnatchNetRequest;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.NoScrollListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 */
public class SnatchSuccess extends BaseActivity {

    @ViewInject(R.id.tv_number)
    private TextView tvLuckNumber;

    @ViewInject(R.id.tv_phone)
    private TextView tvPhone;

    @ViewInject(R.id.activity_describe)
    private TextView describe;

    @ViewInject(R.id.listView)
    private NoScrollListView listView;

    @ViewInject(R.id.scrollview)
    private PullToRefreshScrollView scrollView;

    private SnatchNetRequest request;
    private String activityId;
    private int total;

    private int join;
    private String time;
    private String phone;
    private ArrayList<SnatchDetailsEntity.Log2> list;

    private ArrayList<SnatchDetailsEntity.Log2> listsub = new ArrayList<>();

    private ArrayList<Integer> sortlist=new ArrayList<>();
    private String goodname, status;
    private String luckNumber;
    private int page;
    private SnatchSuccessAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snatch_success);

        ViewUtils.inject(this);

        initScrollView();

        initView();

        Listener();
    }

    private void Listener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SnatchDetailsEntity.Log2 snatchDetails = (SnatchDetailsEntity.Log2) parent.getItemAtPosition(position);

                //跳转到夺宝记录
                Intent intent = new Intent(SnatchSuccess.this, PartRecordSuccess.class);
                intent.putExtra("luck_number", list.get(position).luckNumber);
                intent.putExtra("activity_id", activityId);
                startActivity(intent);
            }
        });
    }

    private void initScrollView() {

        Intent intent = getIntent();
        activityId = intent.getStringExtra("activityId");
//        total = Integer.parseInt(intent.getStringExtra("totalCount"));
        goodname = intent.getStringExtra("goods_name");
        status = intent.getStringExtra("status");
        list = new ArrayList<>();

        listView.setFocusable(false);

        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        scrollView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ScrollView>() {

            @Override
            public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                // TODO Auto-generated method stub

            }
        });
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                page = 0;
                scrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                page++;
                if (list.size() >= 40) {
                    if (list.size() / 40 > page) {
                        Logger.i("list.size()" + list.size() / 40 + "page" + page);
                        listsub.addAll(list.subList(40 * page, 40 * (page + 1)));
                        adapter.notifyDataSetChanged();
                    } else if (list.size() / 40 == page) {
                        listsub.addAll(list.subList(40 * page, list.size()));
                        adapter.notifyDataSetChanged();
                    }
                }
                scrollView.onRefreshComplete();
            }
        });

    }

    private void initView() {

        if (request == null) {
            request = new SnatchNetRequest(mContext);
        }
        startMyDialog();
        request.snatchDetailNet(activityId, new RequestCallBack<SnatchDetailsEntity>() {
            @Override
            public void onSuccess(ResponseInfo<SnatchDetailsEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        scrollView.onRefreshComplete();
                        join = Integer.parseInt(responseInfo.result.joinCount);
                        time = responseInfo.result.joinTime;
                        luckNumber = responseInfo.result.luckNumber;
                        tvLuckNumber.setText(luckNumber);
                        phone = responseInfo.result.phone;
                        if (phone != null && phone.length() == 11) {
                            tvPhone.setText(phone.replace(phone.substring(3, 7), "****"));
                        }
                        describe.setText("夺宝成功参与" + join + "次 " + " " + StringFormat.formatDate(time, "MM/dd HH:mm:ss"));
                        list = responseInfo.result.logList;

                        //排序获取total、
                        for (SnatchDetailsEntity.Log2 log2:list){
                            sortlist.add(log2.joinCount);
                        }
                        sort(sortlist);
                        if (list.size() >= 40) {
                            listsub.addAll(list.subList(0, 40));
                        } else {
                            listsub.addAll(list);
                        }
                        adapter = new SnatchSuccessAdapter(mContext, listsub, luckNumber);
                        listView.setAdapter(adapter);
                        stopMyDialog();
                        break;
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                scrollView.onRefreshComplete();
            }
        });
    }
    private void sort(ArrayList<Integer> list) {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {

                if (lhs > rhs) {
                    return -1;
                } else if (lhs == rhs) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };
        Collections.sort(list, comparator);
        total=list.get(0);
    }

    public void onBack(View view) {
        finish();
    }

    public void jumpToMyRecord(View view) {
        Intent intent = new Intent(SnatchSuccess.this, SnatchMyRecording.class);
        intent.putExtra("goods_name", goodname);
        intent.putExtra("activity_id", activityId);
        intent.putExtra("status", status);
        startActivity(intent);
    }

    //成功夺宝适配器
    public class SnatchSuccessAdapter extends BaseAdapter {

        private Context context;
        private List<SnatchDetailsEntity.Log2> list;
        private String luckNumber;

        public SnatchSuccessAdapter(Context context, List<SnatchDetailsEntity.Log2> list, String luckNumber) {
            this.context = context;
            this.list = list;
            this.luckNumber = luckNumber;
        }

        @Override
        public int getCount() {
            if (list.size() != 0) {
                return list.size();
            }
            return 0;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_snatch_success, null);
                holder.item_pro = (ProgressBar) convertView.findViewById(R.id.item_pro);
                holder.tv_join_count = (TextView) convertView.findViewById(R.id.tv_join_count);
                holder.tv_luck_number = (TextView) convertView.findViewById(R.id.tv_luck_number);
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null) {
                holder.tv_luck_number.setText(list.get(position).luckNumber);
                if (position % 2 == 0) {
                    holder.tv_luck_number.setBackgroundColor(getResources().getColor(R.color.cf8f8fc));
                } else {
                    holder.tv_luck_number.setBackgroundColor(getResources().getColor(R.color.white));
                }
                holder.tv_join_count.setText(list.get(position).joinCount + "人次");
                holder.item_pro.setMax(total);
                holder.item_pro.setProgress(list.get(position).joinCount);
                if (luckNumber.equals(list.get(position).luckNumber)) {
                    holder.imageView.setVisibility(View.VISIBLE);
                } else {
                    holder.imageView.setVisibility(View.GONE);
                }
            }

            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_luck_number;
        ProgressBar item_pro;
        TextView tv_join_count;
        ImageView imageView;
    }
}
