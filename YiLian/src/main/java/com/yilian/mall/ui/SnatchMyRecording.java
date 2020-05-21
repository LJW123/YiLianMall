package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.SnatchMyPartEntity;
import com.yilian.mall.http.SnatchNetRequest;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.ListViewForScrollView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的记录
 * Created by Administrator on 2016/4/8.
 */
public class SnatchMyRecording extends BaseActivity {


    @ViewInject(R.id.activity_name)
    private TextView activityName;

    @ViewInject(R.id.tv_number)
    private TextView luckNumber;

    @ViewInject(R.id.scrollview)
    private ScrollView scrollview;

    @ViewInject(R.id.tv_congratulation)
    private TextView congratulation;

    @ViewInject(R.id.listView)
    private ListViewForScrollView listview;

    @ViewInject(R.id.ll_no_data)
    private LinearLayout ll_no_data;

    private ArrayList<SnatchMyPartEntity.Log> list;

    private SnatchNetRequest request;
    private String activityId;
    private String status;
    private String number;
    private String goodsname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snatch_myrecording);

        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();

        Intent intent = getIntent();
        activityId = intent.getStringExtra("activity_id");
        status = intent.getStringExtra("status");
        goodsname = intent.getStringExtra("goods_name");
        Logger.i(goodsname+"+++++++++");
        if (goodsname == null) {
            activityName.setText("夺宝商品：");
        } else {
            activityName.setText("夺宝商品：" + goodsname);
        }
        if (request == null) {
            request = new SnatchNetRequest(mContext);
        }
        startMyDialog();

        String token = "0";
        if (isLogin())
            token = RequestOftenKey.getToken(mContext);

        request.MyPartNet(token,activityId, status, new RequestCallBack<SnatchMyPartEntity>() {
            @Override
            public void onSuccess(ResponseInfo<SnatchMyPartEntity> responseInfo) {
                Logger.json(responseInfo.result.toString());
                switch (responseInfo.result.code) {
                    case 1:
                        list.addAll(responseInfo.result.logslist);
                        if (list.size()==0){
                            ll_no_data.setVisibility(View.VISIBLE);
                            listview.setVisibility(View.GONE);
                        }else {
                            ll_no_data.setVisibility(View.GONE);
                            listview.setVisibility(View.VISIBLE);
                        }
                        MyRecordingAdapter adapter = new MyRecordingAdapter(list, mContext);
                        listview.setAdapter(adapter);
                        scrollview.smoothScrollTo(0,0);
                        number = responseInfo.result.luck_number;
                        if (status.equals("1")) {
                            luckNumber.setText("?");
                        } else if (status.equals("2")) {
                            luckNumber.setText(number);
                        }
                        if (list!=null){
                            for (int i=0;i<list.size();i++){
                                if (list.get(i).luck_number.equals(number)){
                                    congratulation.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        break;
                }
                stopMyDialog();
            }
            @Override
            public void onFailure(HttpException e, String s) {
                showToast("请检查网络");
                stopMyDialog();
            }
        });
    }
    public void onBack(View view) {
        finish();
    }

    public class MyRecordingAdapter extends BaseAdapter {

        private List<SnatchMyPartEntity.Log> list;
        private Context context;

        public MyRecordingAdapter(List<SnatchMyPartEntity.Log> list, Context context) {
            this.list = list;
            this.context = context;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_myrecord, null);
                holder.my_number_tv = (TextView) convertView.findViewById(R.id.myrecord_my_number_tv);
                holder.number_times = (TextView) convertView.findViewById(R.id.myrecord_number_times);
                holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null) {
                holder.my_number_tv.setText(list.get(position).luck_number);
                holder.number_times.setText(list.get(position).joinCount);
                holder.time_tv.setText(StringFormat.formatDateS(list.get(position).joinTime));
            }
            return convertView;
        }

        private class ViewHolder {
            TextView my_number_tv;
            TextView number_times;
            TextView time_tv;
        }
    }

}
