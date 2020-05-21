package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.SnatchPartRecord;
import com.yilian.mall.http.SnatchNetRequest;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class PartRecordSuccess extends BaseActivity {
    @ViewInject(R.id.listView)
    private PullToRefreshListView listView;

    private ArrayList<SnatchPartRecord.Log3> list;
    private SnatchNetRequest request;
    private String activityId;
    private String luckNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_record_success);

        ViewUtils.inject(this);
        list = new ArrayList<>();
        listener();
        initData();
    }

    private void listener() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });
    }

//    //    跳转到我的界面
//    public void jumpToMyRecord(View view) {
//        startActivity(new Intent(PartRecordSuccess.this, SnatchMyRecording.class));
//    }

    //初始化数据
    private void initData() {
        Intent intent = getIntent();
        activityId = intent.getStringExtra("activity_id");
        luckNumber = intent.getStringExtra("luck_number");
        if (request == null) {
            request = new SnatchNetRequest(mContext);
        }
        request.partRecordNet(activityId, luckNumber, new RequestCallBack<SnatchPartRecord>() {
            @Override
            public void onSuccess(ResponseInfo<SnatchPartRecord> responseInfo) {
                Logger.json(responseInfo.result.listLog.toString());
                switch (responseInfo.result.code) {
                    case 1:
                        Logger.json(responseInfo.result.toString());
                        list.clear();
                        list.addAll(responseInfo.result.listLog) ;
                        MyAdapter adapter = new MyAdapter(mContext, list);
                        listView.setAdapter(adapter);
                        listView.onRefreshComplete();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listView.onRefreshComplete();
            }
        });
    }

    public void onBack(View view) {
        finish();
    }

    /**
     * listview适配器
     */
    public class MyAdapter extends BaseAdapter {

        private List<SnatchPartRecord.Log3> list;
        private Context context;

        public MyAdapter(Context context, List list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null)
                return list.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (list != null) {
                return list.get(position);
            }
            return null;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_part_record, null);
                holder.textView = (TextView) convertView.findViewById(R.id.view);
                holder.imageView = (JHCircleView) convertView.findViewById(R.id.img_photo);
                holder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null) {
                if (!TextUtils.isEmpty(list.get(position).photo)) {
                    if (list.get(position).photo.contains("http://")||list.get(position).photo.contains("https://")) {
                        new BitmapUtils(context).display(holder.imageView, list.get(position).photo);
                    } else {
                        new BitmapUtils(context).display(holder.imageView, Constants.ImageUrl + list.get(position).photo + Constants.ImageSuffix);
                    }

                    Logger.i(Constants.ImageUrl + list.get(position).photo + Constants.ImageSuffix);
                }

                if (list.get(position).phone == null) {
                    holder.tvPhone.setText("暂无电话");
                } else if (list.get(position).phone != null) {
                    String phone = list.get(position).phone;
                    String newphone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
                    holder.tvPhone.setText(newphone);
                }

                holder.tvTime.setText(StringFormat.formatDateS(list.get(position).joinTime));
//                if ((position + 1) % 3 != 0) {
//                holder.textView.setBackgroundColor(0x7fffd4);
//                }else {
//                    holder.textView.setBackgroundColor(0xe9967a);
//                }
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView;
        JHCircleView imageView;
        TextView tvPhone;
        TextView tvTime;
    }
}
