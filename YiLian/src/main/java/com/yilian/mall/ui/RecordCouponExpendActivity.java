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
import com.yilian.mall.entity.CouponExpendListBean;
import com.yilian.mall.entity.CouponExpendListBean.CouponExpendList;
import com.yilian.mall.http.UserdataNetRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class RecordCouponExpendActivity extends BaseActivity {

    private List<CouponExpendList> couponExpendLists;

    private UserdataNetRequest userdataNetRequest;

    @ViewInject(R.id.tv_back)
    private TextView tv_back;
    @ViewInject(R.id.no_exchangejifen)
    private ImageView no_expend_lequan;
    @ViewInject(R.id.lv_exchange_jifenrecord_activity)
    private PullToRefreshListView lv_coupon_expend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_exchangejifen_list);
        ViewUtils.inject(this);
        tv_back.setText(getResources().getString(R.string.dikouquan)+"支出记录明细");
        initListView();
        if (userdataNetRequest == null) {
            userdataNetRequest = new UserdataNetRequest(mContext);
        }
        getCouponExpendRecordList();

    }

    private void initListView() {
        lv_coupon_expend.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_coupon_expend.getLoadingLayoutProxy(true, false).setLoadingDrawable(getResources().getDrawable(R.drawable.pull_down_refresh));

        lv_coupon_expend.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getCouponExpendRecordList();
            }
        });
    }

    private void getCouponExpendRecordList() {
        startMyDialog();
        userdataNetRequest.getCouponExpendList(CouponExpendListBean.class, new RequestCallBack<CouponExpendListBean>() {
            @Override
            public void onSuccess(ResponseInfo<CouponExpendListBean> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        couponExpendLists = responseInfo.result.list;

                        if (couponExpendLists.size() == 0 || couponExpendLists == null) {
                            no_expend_lequan.setVisibility(View.VISIBLE);
                            lv_coupon_expend.setVisibility(View.GONE);
                        } else {
                            no_expend_lequan.setVisibility(View.GONE);
                            lv_coupon_expend.setVisibility(View.VISIBLE);
                        }

                        couponExpendAdapter = new CouponExpendAdapter(mContext, couponExpendLists);
                        lv_coupon_expend.setAdapter(couponExpendAdapter);
                        break;
                    default:
                        break;
                }

                lv_coupon_expend.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
                lv_coupon_expend.onRefreshComplete();
            }
        });
    }

    private CouponExpendAdapter couponExpendAdapter;

    class CouponExpendAdapter extends BaseAdapter {
        private List<CouponExpendList> couponExpendLists;
        private Context context;

        public CouponExpendAdapter(Context context, List<CouponExpendList> couponExpendLists) {
            this.context = context;
            this.couponExpendLists = couponExpendLists;
        }

        @Override
        public int getCount() {
            if (couponExpendLists.size() != 0) {
                return couponExpendLists.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return couponExpendLists.get(position);
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
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_record_exchangejifen_item, null);
                holder.exchange_lefen_tv = (TextView) convertView.findViewById(R.id.exchange_lefen_tv);
                holder.expend_lequan_tv = (TextView) convertView.findViewById(R.id.expend_lequan_tv);
                holder.exchange_lefen_tv.setVisibility(View.GONE);
                holder.expend_lequan_tv.setVisibility(View.VISIBLE);

                holder.filiale_name = (TextView) convertView.findViewById(R.id.filiale_name);
                holder.sell_total_price = (TextView) convertView.findViewById(R.id.sell_cash);
                holder.sell_total_coupon = (TextView) convertView.findViewById(R.id.sell_total_integral);
                holder.sell_time = (TextView) convertView.findViewById(R.id.sell_time);
                holder.title_time = (TextView) convertView.findViewById(R.id.time_title);
                holder.line = (ImageView) convertView.findViewById(R.id.line);
                holder.linetop = convertView.findViewById(R.id.linetop);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (couponExpendLists != null) {

                CouponExpendList couponExpendList = couponExpendLists.get(position);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH : mm : ss");
                String time = sdf.format(new Date(Long.valueOf(couponExpendList.sell_time + "000")));
                String oldtime;
                try {
                    oldtime = sdf.format(new Date(Long.valueOf(couponExpendLists.get(position - 1).sell_time + "000"))).substring(0, 14);

                } catch (ArrayIndexOutOfBoundsException e) {
                    oldtime = "";
                }
                if (holder.filiale_name != null) {
                    holder.filiale_name.setText(couponExpendList.filiale_name);
                }
                if (holder.sell_total_price != null) {
                    holder.sell_total_price.setText("- " + Float.parseFloat(couponExpendList.sell_total_price)/100);

                }
                if (holder.sell_total_coupon != null){
                    holder.sell_total_coupon.setText("- " + Float.parseFloat(couponExpendList.sell_total_coupon)/100);
                }

                if (holder.sell_time != null) {
                    holder.sell_time.setText(time.subSequence(15, time.length()));
                }
                if (holder.title_time != null) {
                    holder.title_time.setText(time.subSequence(0, 14));
                }

                if (oldtime.equals(time.subSequence(0, 14))) {
                    holder.title_time.setVisibility(View.GONE);
                    holder.linetop.setVisibility(View.GONE);
                } else {
                    holder.title_time.setVisibility(View.VISIBLE);
                    holder.linetop.setVisibility(View.VISIBLE);
                }
            }

            return convertView;
        }

        class ViewHolder {
            TextView filiale_name;
            TextView sell_total_price;
            TextView sell_total_coupon;
            TextView sell_time;
            TextView title_time;
            ImageView line;
            View linetop;

            TextView exchange_lefen_tv;
            TextView expend_lequan_tv;
        }
    }

}
