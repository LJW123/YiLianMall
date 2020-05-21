package com.yilian.mall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.MTCodesEntity;
import com.yilian.mall.entity.MTRefundDetailEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/12/6 0006.
 * 美团-申请退款
 */

public class MTRefundActivity extends BaseActivity {
    public static final int REFUND_SUCCESS = 0;

    @ViewInject(R.id.v3Layout)
    LinearLayout v3Layout;
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    /**
     * goods
     */
    @ViewInject(R.id.iv_goods)
    ImageView ivGoods;
    @ViewInject(R.id.tv_goods_name)
    TextView tvGoodsName;
    @ViewInject(R.id.tv_goods_explain)
    TextView tvGoodsExplain;
    @ViewInject(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @ViewInject(R.id.tv_goods_gift)
    TextView tvGoodsGift;
    @ViewInject(R.id.ll_goods_assure)
    LinearLayout assureLayout;
    @ViewInject(R.id.tv_assure1)
    TextView tvAssure1;
    @ViewInject(R.id.tv_assure2)
    TextView tvAssure2;
    @ViewInject(R.id.tv_assure3)
    TextView tvAssure3;
    @ViewInject(R.id.tv_assure4)
    TextView tvAssure4;
    @ViewInject(R.id.ratingBar)
    RatingBar ratingBar;
    @ViewInject(R.id.ll_go_eva)
    LinearLayout evaLayout;
    /**
     * 退款
     */
    @ViewInject(R.id.tv_refund_money)
    TextView tvRefundMoney;
    @ViewInject(R.id.ticketListView)
    NoScrollListView ticketListView;
    @ViewInject(R.id.styleListView)
    NoScrollListView styleListView;
    @ViewInject(R.id.reasonListView)
    NoScrollListView reasonListView;
    @ViewInject(R.id.et)
    EditText et;
    @ViewInject(R.id.btn_ok)
    Button btn;
    //提示语
    @ViewInject(R.id.tv_hint)
    TextView tvHint;

    private MTNetRequest mtNetRequest;
    private MTRefundTicketAdapter ticketAdapter;
    private MTRefundStyleAdapter styleAdapter;
    private MTRefundReasonAdapter reasonAdapter;
    private ArrayList<MTCodesEntity> codesList;
    private ArrayList<String> ticketStr = new ArrayList<>();
    private ArrayList<String> reasonStr = new ArrayList<>();
    ArrayList<MTRefundDetailEntity.ReasonBean> reasonList;
    private String reasonStrAll;
    String id;
    private String imgUrl;  //图
    private String type;
    private float onePrice = 0; //单个价格
    private float allPrice = 0; //总价格
    private float refundVoucher; //零购券

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_mt);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {

        /**
         * 现在不赠送奖券
         */
        tvHint.setText("申请退款成功后，所有支付款将退回奖励中，可在奖励余额中查看。");
        v3Layout.setFocusable(true);
        v3Layout.setFocusableInTouchMode(true);

        id = getIntent().getStringExtra("index_id");
        imgUrl = getIntent().getStringExtra("img_url");
        type = getIntent().getStringExtra("type");
        Logger.i("2016-12-20:" + id);
        Logger.i("2016-12-20:" + imgUrl);
        Logger.i("2016-12-20:" + type); // 1到店消费 2订单外卖

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MTRefundActivity.this.finish();
            }
        });
        tvTitle.setText("申请退款");
        assureLayout.setVisibility(View.GONE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == codesList || codesList.size() <= 0) {
                    showToast(R.string.network_module_net_work_error);
                    return;
                }
                for (int i = 0; i < codesList.size(); i++) {
                    ticketStr.add(codesList.get(i).code);
                }
                //退款券码 只有在进行网络请求时才为排序条件添加上逗号，其余地方获取的排序值都要去除逗号再使用
                StringBuffer ticketSb = new StringBuffer();
                for (int i = 0, count = ticketStr.size(); i < count; i++) {
                    if (i < count - 1) {
                        ticketSb.append(ticketStr.get(i) + ",");
                    } else {
                        ticketSb.append(ticketStr.get(i));
                    }
                }

                /**
                 * 由于需求从单选变为多选，所以暂时不要了
                 */
                //退款原因
//                for (int i = 0; i<reasonListView.getCount(); i++) {
//                    View child = reasonListView.getChildAt(i);
//                    RadioButton radioBtn = (RadioButton) child.findViewById(R.id.rb);
//                    if (radioBtn.isChecked()) {
//                        reasonStrAll = reasonList.get(i).reasonMsg;
//                    }
//                }
                StringBuffer reasonSb = new StringBuffer();
                for (int i = 0, count = reasonStr.size(); i < count; i++) {
                    if (i < count - 1) {
                        reasonSb.append(reasonStr.get(i) + "0X");
                    } else {
                        reasonSb.append(reasonStr.get(i));
                    }
                }

                //还有不爽
                String etStr = et.getText().toString();

                if (TextUtils.isEmpty(ticketSb)) {
                    showToast("请选择您要退的券码！");
                    return;
                }
                if (TextUtils.isEmpty(reasonSb)) {
                    showToast("请选择一个退款原因吧！");
                    return;
                }
//                if (TextUtils.isEmpty(reasonStrAll)) {
//                    showToast("请选择一个退款原因吧~");
//                    return;
//                }

                mtNetRequest.comboRefund(id, String.valueOf(ticketSb), String.valueOf(reasonSb), etStr, new RequestCallBack<BaseEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        startMyDialog();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                        stopMyDialog();
                        switch (responseInfo.result.code) {
                            case 1:
                                //刷新个人页面
                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);

                                Intent intent = new Intent(MTRefundActivity.this, MTRefundSucceedActivity.class);
                                startActivityForResult(intent, REFUND_SUCCESS);
                                break;
                            case -75:
                                showToast("券码失效，无法退款！");
                                MTRefundActivity.this.finish();
                                break;
                            default:
                                showToast(responseInfo.result.msg);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        showToast(R.string.net_work_not_available);
                        stopMyDialog();
                    }
                });
//
//                Logger.i("2016-12-16:"+id);
//                Logger.i("2016-12-16:"+String.valueOf(ticketSb));
//                Logger.i("2016-12-16:"+String.valueOf(reasonSb));
//                Logger.i("2016-12-16:"+etStr);
//
//                //比较用户当前的零购券和退款所需的零购券，如果前者少则会提示
//                float myVoucher = Float.parseFloat(sp.getString("voucher", "0.00"));
//                boolean flag = refundVoucher - myVoucher > 0;
//                Logger.i("2017-2-15:" + myVoucher);
//                Logger.i("2017-2-15:" + refundVoucher);
//                Logger.i("2017-2-15:" + flag);
//                if (flag) {
//                    //显示提示框
//                    showDialog("温馨提示", "亲,由于您的"+getResources().getString(R.string.linggouquan)+"不足,系统将会根据订单数据扣除您部发奖励包抵扣商家赠送给您的"
//                            +getResources().getString(R.string.linggouquan), null, 0, Gravity.CENTER, "好的",
//                            "算了", true, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    switch (which) {
//                                        case DialogInterface.BUTTON_POSITIVE:
//                                            dialog.dismiss();
//                                            mtNetRequest.comboRefund(id, String.valueOf(ticketSb), String.valueOf(reasonSb), etStr, new RequestCallBack<BaseEntity>() {
//                                                @Override
//                                                public void onStart() {
//                                                    super.onStart();
//                                                    startMyDialog();
//                                                }
//
//                                                @Override
//                                                public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
//                                                    stopMyDialog();
//                                                    switch (responseInfo.result.code) {
//                                                        case 1:
//                                                            Intent intent = new Intent(MTRefundActivity.this, MTRefundSucceedActivity.class);
//                                                            startActivityForResult(intent, REFUND_SUCCESS);
//                                                            break;
//                                                        case -75:
//                                                            showToast("券码失效，无法退款！");
//                                                            MTRefundActivity.this.finish();
//                                                            break;
//                                                        default:
//                                                            showToast("错误吗：" + responseInfo.result.code);
//                                                            break;
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onFailure(HttpException e, String s) {
//                                                    showToast(R.string.net_work_not_available);
//                                                    stopMyDialog();
//                                                }
//                                            });
//                                            break;
//                                        case DialogInterface.BUTTON_NEGATIVE:
//                                            dialog.dismiss();
//                                            break;
//                                    }
//                                }
//                            }, mContext);
//                } else {
//                    mtNetRequest.comboRefund(id, String.valueOf(ticketSb), String.valueOf(reasonSb), etStr, new RequestCallBack<BaseEntity>() {
//                        @Override
//                        public void onStart() {
//                            super.onStart();
//                            startMyDialog();
//                        }
//
//                        @Override
//                        public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
//                            stopMyDialog();
//                            switch (responseInfo.result.code) {
//                                case 1:
//                                    Intent intent = new Intent(MTRefundActivity.this, MTRefundSucceedActivity.class);
//                                    startActivityForResult(intent, REFUND_SUCCESS);
//                                    break;
//                                case -75:
//                                    showToast("券码失效，无法退款！");
//                                    MTRefundActivity.this.finish();
//                                    break;
//                                default:
//                                    showToast("错误吗：" + responseInfo.result.code);
//                                    break;
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(HttpException e, String s) {
//                            showToast(R.string.net_work_not_available);
//                            stopMyDialog();
//                        }
//                    });
//                }
            }
        });

        ticketListView.setFocusable(false);
        ticketListView.setFocusableInTouchMode(false);
        styleListView.setFocusable(false);
        styleListView.setFocusableInTouchMode(false);
        reasonListView.setFocusable(false);
        reasonListView.setFocusableInTouchMode(false);
    }

    private void initData() {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.comboRefundDetail(id, new RequestCallBack<MTRefundDetailEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTRefundDetailEntity> responseInfo) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                        switch (responseInfo.result.code) {
                            case 1:
                                MTRefundDetailEntity info = responseInfo.result;
                                imageLoader.displayImage(imgUrl, ivGoods, options);
                                tvGoodsName.setText(info.name);
                                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(info.price)));
                                refundVoucher = info.voucher;
                                tvGoodsGift.setVisibility(View.VISIBLE);
                                tvGoodsGift.setText("可得" + getResources().getString(R.string.integral) + MoneyUtil.getLeXiangBiNoZero(info.returnIntegral));
                                onePrice = NumberFormat.convertToFloat(MoneyUtil.getLeXiangBiNoZero(info.price), 0);
                                tvRefundMoney.setText("¥0");

                                //退款券码
                                codesList = info.codes;
                                if ("2".equals(type) || "1".equals(type)) {
                                    tvRefundMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(codesList.size() * Integer.parseInt(info.price))));
                                    if (codesList.size() != 1) {
                                        showDialog("温馨提示", "亲,申请退款不能取消单个券码哦", null, 0, Gravity.CENTER, "知道了",
                                                null, true, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        switch (which) {
                                                            case DialogInterface.BUTTON_POSITIVE:
                                                                dialog.dismiss();
                                                                break;
                                                        }
                                                    }
                                                }, mContext);
                                    }
                                }
                                ticketAdapter = new MTRefundTicketAdapter(mContext, codesList);
                                ticketListView.setAdapter(ticketAdapter);

                                //退款路径（暂时只有一种，可能以后有选择 所以设置为Listview）
                                ArrayList<MTRefundDetailEntity.RefStyleBean> styleList = info.refStyle;
                                styleAdapter = new MTRefundStyleAdapter(mContext, styleList);
                                styleListView.setAdapter(styleAdapter);

                                //退款原因
                                reasonList = info.reason;
                                reasonAdapter = new MTRefundReasonAdapter(mContext, reasonList);
                                reasonListView.setAdapter(reasonAdapter);
                                reasonListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                                break;
                        }
                    }
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REFUND_SUCCESS) {
            setResult(RESULT_OK);
            MTRefundActivity.this.finish();
        }
    }

    class MTRefundTicketAdapter extends BaseAdapter {
        private Context context;
        private List<MTCodesEntity> list;

        public MTRefundTicketAdapter(Context context, List<MTCodesEntity> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list == null || list.size() == 0) {
                return 0;
            }
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TicketViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_refund_mt, null);
                holder = new TicketViewHolder();
                holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
                holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
                holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
                convertView.setTag(holder);
            } else {
                holder = (TicketViewHolder) convertView.getTag();
            }

            int num = position + 1;
            holder.tv1.setText("券码" + num + "：");
            holder.tv2.setText(list.get(position).code);
            if ("2".equals(type) || "1".equals(type)) {
                holder.cb.setChecked(true);
                holder.cb.setClickable(false);
            } else {
                holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ticketStr.add(list.get(position).code);
                            allPrice += onePrice;
                            tvRefundMoney.setText("¥" + allPrice);
                        } else {
                            ticketStr.remove(list.get(position).code);
                            allPrice -= onePrice;
                            tvRefundMoney.setText("¥" + allPrice);
                        }
                    }
                });
            }

            return convertView;
        }

        class TicketViewHolder {
            TextView tv1;
            TextView tv2;
            CheckBox cb;
        }
    }

    class MTRefundStyleAdapter extends BaseAdapter {
        private Context context;
        private List<MTRefundDetailEntity.RefStyleBean> list;

        public MTRefundStyleAdapter(Context context, List<MTRefundDetailEntity.RefStyleBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list == null || list.size() == 0) {
                return 0;
            }
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TicketViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_refund_mt, null);
                holder = new TicketViewHolder();
                holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
                holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
                holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
                convertView.setTag(holder);
            } else {
                holder = (TicketViewHolder) convertView.getTag();
            }

            holder.tv1.setText(list.get(position).styleMsg);
            holder.tv2.setVisibility(View.GONE);
            holder.cb.setVisibility(View.INVISIBLE);

            return convertView;
        }

        class TicketViewHolder {
            TextView tv1;
            TextView tv2;
            CheckBox cb;
        }
    }

    class MTRefundReasonAdapter extends BaseAdapter {
        private Context context;
        private List<MTRefundDetailEntity.ReasonBean> list;

        public MTRefundReasonAdapter(Context context, List<MTRefundDetailEntity.ReasonBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list == null || list.size() == 0) {
                return 0;
            }
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TicketViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_refund_mt, null);
                holder = new TicketViewHolder();
                holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
                holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
                holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
                convertView.setTag(holder);
            } else {
                holder = (TicketViewHolder) convertView.getTag();
            }

            holder.tv1.setText(list.get(position).reasonMsg);
            holder.tv2.setVisibility(View.GONE);
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        reasonStr.add(list.get(position).reasonMsg);
                    } else {
                        reasonStr.remove(list.get(position).reasonMsg);
                    }
                }
            });

            return convertView;
        }

        class TicketViewHolder {
            TextView tv1;
            TextView tv2;
            CheckBox cb;
        }
    }

//    class MTRefundReasonAdapter extends BaseAdapter {
//        private Context mContext;
//        private List<MTRefundDetailEntity.ReasonBean> list;
//        HashMap<String, Boolean> states = new HashMap<String, Boolean>();
//
//        public MTRefundReasonAdapter(Context mContext, List<MTRefundDetailEntity.ReasonBean> list) {
//            this.mContext = mContext;
//            this.list = list;
//        }
//
//        @Override
//        public int getCount() {
//            if (list == null || list.size() == 0) {
//                return 0;
//            }
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            final TicketViewHolder holder;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_refund_mt2, null);
//                holder = new TicketViewHolder();
//                holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
//                holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
//                //holder.rb = (RadioButton) convertView.findViewById(R.id.rb);
//                convertView.setTag(holder);
//            } else {
//                holder = (TicketViewHolder) convertView.getTag();
//            }
//
//            holder.tv1.setText(list.get(position).reasonMsg);
//            holder.tv2.setVisibility(View.GONE);
//
//            final RadioButton radioBtn = (RadioButton) convertView.findViewById(R.id.rb);
//            holder.rb = radioBtn;
//            holder.rb.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    for (String key : states.keySet()) {
//                        states.put(key, false);
//                    }
//                    states.put(String.valueOf(position), radioBtn.isChecked());
//                    MTRefundReasonAdapter.this.notifyDataSetChanged();
//                }
//            });
//
//            boolean res = false;
//            if (states.get(String.valueOf(position)) == null || states.get(String.valueOf(position)) == false) {
//                res = false;
//                states.put(String.valueOf(position), false);
//            } else {
//                res = true;
//            }
//            holder.rb.setChecked(res);
//
//            return convertView;
//        }
//
//        class TicketViewHolder {
//            TextView tv1;
//            TextView tv2;
//            RadioButton rb;
//        }
//    }
}
