package com.yilian.mall.ui;

import android.content.Context;
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

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.SnatchPartEntity;
import com.yilian.mall.http.SnatchNetRequest;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class PartRecord extends BaseActivity {

    @ViewInject(R.id.listView)
    private PullToRefreshListView listView;

    @ViewInject(R.id.no_data)
    private ImageView imageView;

    private ArrayList<SnatchPartEntity.Record> list;
    private SnatchNetRequest request;
    private int page;
    private String activityId;
    private String status;
    private String goodsname;
    private String activityid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_record);

        ViewUtils.inject(this);
        list = new ArrayList<>();
        Intent intent = getIntent();
        activityId = intent.getStringExtra("activity_id");
        initListener();
        initData();

    }

    private void initListener() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initData();
            }
        });
    }

    //初始化数据
    private void initData() {

        if (request == null) {
            request = new SnatchNetRequest(mContext);
        }
        startMyDialog();
        request.snatchingNet(activityId, page, new RequestCallBack<SnatchPartEntity>() {
            @Override
            public void onSuccess(ResponseInfo<SnatchPartEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        Logger.json(responseInfo.result.toString());
                        if (page==0){
                            list.clear();
                        }
                        list.addAll(responseInfo.result.log0) ;
                        status = responseInfo.result.status;
                        goodsname = responseInfo.result.activity.goodsName;
                        activityid = responseInfo.result.activity.activityId;
                        MyAdapter adapter = new MyAdapter(mContext, list);
                        if (list.size() == 0) {
                            adapter.setDataView(false);
                        } else {
                            adapter.setDataView(true);
                            listView.setAdapter(adapter);
                        }
                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {
                showToast("请检查网络");
                stopMyDialog();
            }
        });
    }

    public void onBack(View view) {
        finish();
    }

    //跳转到我的记录
    public void jumpToMyRecord(View view) {
        if (isLogin()){
            Intent intent = new Intent(PartRecord.this, SnatchMyRecording.class);
            intent.putExtra("activity_id", activityid);
            intent.putExtra("status", status);
            intent.putExtra("goods_name", goodsname);
            startActivity(intent);
        }else {
            startActivity(new Intent(PartRecord.this, LeFenPhoneLoginActivity.class));
        }
    }
    /**
     * listview适配器
     */
    public class MyAdapter extends BaseAdapter {

        ViewHolder holder;
        private List<SnatchPartEntity.Record> list;
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

        public void setDataView(boolean hasData) {
            if (hasData) {
                listView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_part_record, null);
                holder.textView = convertView.findViewById(R.id.view);
                holder.imageView = (ImageView) convertView.findViewById(R.id.img_photo);
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
                }

                String phone = list.get(position).phone;
                if (phone != null) {
                    String newphone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
                    holder.tvPhone.setText(newphone);
                }

                holder.tvTime.setText(StringFormat.formatDateS(list.get(position).joinTime));
//                if ((position + 1) % 3 != 0) {
//                    holder.textView.setBackgroundColor(0x7fffd4);
//                } else {
//                    holder.textView.setBackgroundColor(0xe9967a);
//                }
            }
            return convertView;
        }
    }

    class ViewHolder {
        View textView;
        ImageView imageView;
        TextView tvPhone;
        TextView tvTime;
    }
}
