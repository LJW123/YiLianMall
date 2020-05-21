package com.yilian.mall.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.CouponGainListBean;
import com.yilian.mall.http.UserdataNetRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.mall.entity.CouponGainListBean.CouponGainList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class RecordCouponGainActivity extends BaseActivity {

    private List<CouponGainList> couponGainLists;
    private UserdataNetRequest userdataNetRequest;

    @ViewInject(R.id.tv_back)
    private TextView tv_back;
    @ViewInject(R.id.no_jifenobtain)
    private ImageView no_lequangain;
    @ViewInject(R.id.lv_jifenrecord_activity)
    private PullToRefreshListView lv_lequangain_record_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_obtainjifen_list);
        ViewUtils.inject(this);
        tv_back.setText(getResources().getString(R.string.dikouquan)+"获得记录明细");
        initListView();

        if (userdataNetRequest == null) {
            userdataNetRequest = new UserdataNetRequest(mContext);
        }
        getCouponGainRecordList();
    }

    private void initListView() {
        lv_lequangain_record_activity.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_lequangain_record_activity.getLoadingLayoutProxy(true, false).setLoadingDrawable(getResources().getDrawable(R.drawable.pull_down_refresh));

        lv_lequangain_record_activity.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getCouponGainRecordList();
            }
        });
    }

    private void getCouponGainRecordList() {
        startMyDialog();
        userdataNetRequest.getCouponGainList(CouponGainListBean.class, new RequestCallBack<CouponGainListBean>() {
            @Override
            public void onSuccess(ResponseInfo<CouponGainListBean> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:

                        couponGainLists = responseInfo.result.list;
                        if (couponGainLists.size() == 0 || couponGainLists == null) {
                            no_lequangain.setVisibility(View.VISIBLE);
                            lv_lequangain_record_activity.setVisibility(View.GONE);
                        } else {
                            no_lequangain.setVisibility(View.GONE);
                            lv_lequangain_record_activity.setVisibility(View.VISIBLE);
                        }

                        couponGainAdapter = new CouponGainAdapter(mContext, couponGainLists);
                        lv_lequangain_record_activity.setAdapter(couponGainAdapter);

                        break;
                    default:
                        break;
                }
                lv_lequangain_record_activity.onRefreshComplete();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
                lv_lequangain_record_activity.onRefreshComplete();
            }
        });
    }

    private CouponGainAdapter couponGainAdapter;

    class CouponGainAdapter extends BaseAdapter {
        private List<CouponGainList> couponGainLists;
        private Context context;
        public CouponGainAdapter(Context context) {
            this.context = context;
        }
        public CouponGainAdapter(Context context, List<CouponGainList> couponGainLists) {
            this.context = context;
            this.couponGainLists = couponGainLists;
        }

        @Override
        public int getCount() {
            if (couponGainLists != null) {
                return couponGainLists.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return couponGainLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_record_obtainjifen_item, null);
                holder.lefen_icon_iv = (ImageView) convertView.findViewById(R.id.lefen_icon_iv);
                holder.lefen_tv = (TextView) convertView.findViewById(R.id.lefen_tv);
                holder.lequan_tv = (TextView) convertView.findViewById(R.id.lequan_tv);
                holder.lefen_icon_iv.setVisibility(View.GONE);
                holder.lefen_tv.setVisibility(View.GONE);
                holder.lequan_tv.setVisibility(View.VISIBLE);

                holder.merchant_name = (TextView) convertView.findViewById(R.id.merchant_name);
                holder.merchant_goods = (TextView) convertView.findViewById(R.id.merchant_goods);
                holder.merchant_buy_money = (TextView) convertView.findViewById(R.id.merchant_income);
                holder.merchant_give_coupon = (TextView) convertView.findViewById(R.id.merchant_give_integral);
                holder.tv_data = (TextView) convertView.findViewById(R.id.tv_data);
                holder.time_title = (TextView) convertView.findViewById(R.id.time_title);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (couponGainLists != null){
                CouponGainList couponGainList = couponGainLists.get(position);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH : mm : ss");
                String time = sdf.format(new Date(Long.valueOf(couponGainList.merchant_give_time + "000")));
                String oldtime;
                try {
                    oldtime = sdf.format(new Date(Long.valueOf(couponGainLists.get(position - 1).merchant_give_time + "000"))).substring(0, 14);
                } catch (ArrayIndexOutOfBoundsException e) {
                    oldtime = "";
                }

                if (holder.merchant_name != null){
                    holder.merchant_name.setText(couponGainList.merchant_name);
                }
                if (holder.merchant_goods != null){
                    holder.merchant_goods.setText("( " + couponGainList.merchant_goods + " )");
                }
                if (holder.merchant_buy_money  != null){
                    if (couponGainList.merchant_buy_money.equals("0")
                            || couponGainList.merchant_buy_money.equals(" ")
                            || couponGainList.merchant_buy_money.equals("")) {
                        holder.merchant_buy_money.setText("- 0.00");
                    } else {
                        holder.merchant_buy_money.setText("- " + Float.parseFloat(couponGainList.merchant_buy_money)/100);
                    }
                }
                if (holder.merchant_give_coupon  != null){
                    holder.merchant_give_coupon.setText(Float.parseFloat(couponGainList.merchant_give_coupon)/100 + "");
                }
                if (holder.tv_data != null) {
                    holder.tv_data.setText(time.subSequence(15, time.length()));
                }
                if (holder.time_title != null) {
                    holder.time_title.setText(time.subSequence(0, 14));
                }
                if (oldtime.equals(time.subSequence(0, 14))) {
                    holder.time_title.setVisibility(View.GONE);
                } else {
                    holder.time_title.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        }

        class ViewHolder {

            TextView merchant_name;
            TextView merchant_goods;
            TextView merchant_buy_money;
            TextView merchant_give_coupon;
            TextView tv_data;
            TextView time_title;

            ImageView lefen_icon_iv;
            TextView lefen_tv;
            TextView lequan_tv;
        }
    }
}
