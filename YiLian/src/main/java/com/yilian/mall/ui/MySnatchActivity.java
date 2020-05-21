package com.yilian.mall.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.MySnatchEntity;
import com.yilian.mall.http.SnatchNetRequest;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.entity.UserAddressLists;

import java.util.ArrayList;

/**
 * 我的夺宝界面
 * Created by Administrator on 2016/4/18.
 */
public class MySnatchActivity extends BaseActivity {
    @ViewInject(R.id.listview)
    private ListView listView;

    @ViewInject(R.id.no_data)
    private ImageView no_data;

    private int page;
    private int type = 3;

    private ArrayList<MySnatchEntity.MySnatch> list = new ArrayList<>();
    private SnatchNetRequest request;
    private String activityId;


    //收货完成时刷新UI
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            pagerAdapter.notifyDataSetChanged(0);
//            pagerAdapter.notifyDataSetChanged(1);
//            pagerAdapter.notifyDataSetChanged(2);
//            listAdapter.notifyDataSetChanged();
            startMyDialog();
            page = 0;
            getSnatchList(type);
            adapter.notifyDataSetChanged();
            stopMyDialog();
        }
    };
    private MySnatchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysnatch);

        ViewUtils.inject(this);

        initView();
        getSnatchList(type);
    }

    private void initView() {
//        listView.setMode(PullToRefreshBase.Mode.BOTH);
//        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                page = 0;
//                getSnatchList(type);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                page++;
//                getSnatchList(type);
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MySnatchEntity.MySnatch snatch = (MySnatchEntity.MySnatch) parent.getItemAtPosition(position);

                if (snatch.hasAddress == 0) {
                    //设置收货地址
                    Intent intent = new Intent(MySnatchActivity.this, AddressManageActivity.class);
                    intent.putExtra("FLAG", "orderId");
                    intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
                    intent.putExtra("activityId", snatch.activity_id);
                    startActivityForResult(intent, 0);
                } else {
                    Intent intent = new Intent(MySnatchActivity.this, ProductDetails.class);
                    intent.putExtra("activity_id", snatch.activity_id);
                    startActivity(intent);
                }
            }
        });
    }

    private void getSnatchList(int type) {
        if (request == null) {
            request = new SnatchNetRequest(mContext);
        }
        startMyDialog();
        request.MySnatchNet(type, page, new RequestCallBack<MySnatchEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MySnatchEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {

                    case 1:
                        if (page == 0) {
                            list.clear();
                        }
                        list=(responseInfo.result.list);
                        if (list.size() == 0) {
                            listView.setVisibility(View.GONE);
                            no_data.setVisibility(View.VISIBLE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                        }
                        adapter = new MySnatchAdapter(mContext, list);
                        listView.setAdapter(adapter);
//                        listView.onRefreshComplete();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
//                listView.onRefreshComplete();
                showToast(R.string.net_work_not_available);
            }
        });
    }


    private class MySnatchAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<MySnatchEntity.MySnatch> datas;

        public MySnatchAdapter(Context context, ArrayList<MySnatchEntity.MySnatch> datas) {
            this.context = context;
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Logger.i(datas.get(position).luck_number + "");
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_mysnatch, null);
                holder.img_goods_icon = (ImageView) convertView.findViewById(R.id.img_goods_icon);
                holder.img_yizj_icon= (ImageView) convertView.findViewById(R.id.img_goods);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
                holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
                holder.tv_my_part = (TextView) convertView.findViewById(R.id.tv_my_part);
                holder.line= (TextView) convertView.findViewById(R.id.line);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final MySnatchEntity.MySnatch data = datas.get(position);
            new BitmapUtils(context).display(holder.img_goods_icon, Constants.ImageUrl + data.goods_icon + Constants.ImageSuffix);
            holder.tv_name.setText(data.goodsname);
            holder.tv_time.setText("中奖时间：" + StringFormat.formatDate(data.open_time));
            holder.tv_number.setText("成功夺宝号码:" + data.luck_number + "");
            holder.tv_count.setText("参与人次：" + data.join_count.toString() + "次");
            if (data.hasAddress == 0) {
                holder.tv_my_part.setText("收货");
                holder.tv_my_part.setTextColor(context.getResources().getColor(R.color.zhuijia));
                holder.tv_my_part.setBackgroundResource(R.drawable.round_corner_blue);
                holder.tv_my_part.setClickable(true);
                holder.tv_my_part.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设置收货地址
                        Intent intent = new Intent(MySnatchActivity.this, AddressManageActivity.class);
                        intent.putExtra("FLAG", "orderId");
                        intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
                        intent.putExtra("activityId", data.activity_id);
                        startActivityForResult(intent, 1);
                    }
                });
            }
            if (data.hasAddress == 1) {
                holder.tv_my_part.setText("完成");
                holder.tv_my_part.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_my_part.setBackgroundResource(R.drawable.round_corner_over);
                holder.tv_my_part.setClickable(false);
            }
            if (data.status.equals("3")) {
                holder.tv_status.setText("已发货");
            }

            return convertView;
        }
    }

    /**
     * 设置中奖收货地址
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (request == null) {
                    request = new SnatchNetRequest(mContext);
                }
                UserAddressLists userAddressList = (UserAddressLists) data.getExtras().getSerializable("USE_RADDRESS_LIST");
                String addressId = userAddressList.address_id;
                activityId = data.getStringExtra("activityId");
                Logger.i(activityId + "-------" + addressId + "------");
                DeliveryDialog dialog = new DeliveryDialog(mContext, activityId, addressId);
                dialog.show();
                break;
        }
    }

    public class ViewHolder {
        ImageView img_goods_icon, img_yizj_icon;
        TextView tv_name, tv_number, tv_time, tv_count, tv_status, tv_my_part,line;
    }

    /**
     * 收货完成后弹出的对话框
     */
    public class DeliveryDialog extends Dialog implements View.OnClickListener {
        private Button btnCancle;
        private Button btnSure;

        private String activityId;
        private String addressId;
        private Context context;

        private SnatchNetRequest request;

        public DeliveryDialog(Context context, String activityId, String addressId) {
            super(context, R.style.ShareDialog);
            this.activityId = activityId;
            this.addressId = addressId;
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_delivery);

            initView();
        }

        private void initView() {
            btnCancle = (Button) findViewById(R.id.btn_cancle);
            btnSure = (Button) findViewById(R.id.btn_sure);

            btnSure.setOnClickListener(this);
            btnCancle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sure:
                    if (request == null) {
                        request = new SnatchNetRequest(context);
                    }
                    request.AddressNet(activityId, addressId, new RequestCallBack<BaseEntity>() {
                        @Override
                        public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                            Logger.json(responseInfo.result.toString());
                            switch (responseInfo.result.code) {
                                case 1:
                                    //刷新界面
                                    IntentFilter filter = new IntentFilter();
                                    filter.addAction(Constants.ADDRESS_ID_SELECTED);
                                    filter.setPriority(Integer.MAX_VALUE - 3);
                                    context.registerReceiver(mReceiver, filter);
                                    sendBroadcast(new Intent(Constants.ADDRESS_ID_SELECTED));
                                    break;
                                case -20:
                                    Toast.makeText(context, "数据不完整", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    });
                    dismiss();
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    break;
            }
        }
    }
}
