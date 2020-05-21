package com.yilian.mall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.yilian.mall.adapter.BaseViewPagerAdapter;
import com.yilian.mall.entity.DeleteOrderEntity;
import com.yilian.mall.entity.MTEntity;
import com.yilian.mall.entity.MTOrderListEntity;
import com.yilian.mall.entity.ShowMTPackageTicketEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.utils.BitmapUtil;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Ray_L_Pain on 2016/12/5 0005.
 * 美团-订单列表
 */

public class MTOrderListActivity extends BaseActivity {
    /**
     * title有关
     */
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    /**
     * radioGroup有关
     */
    @ViewInject(R.id.radioGroup)
    RadioGroup radioGroup;
    @ViewInject(R.id.rb_all)
    RadioButton rbAll;
    @ViewInject(R.id.rb_pay)
    RadioButton rbPay;
    @ViewInject(R.id.rb_use)
    RadioButton rbUse;
    @ViewInject(R.id.rb_eva)
    RadioButton rbEva;
    @ViewInject(R.id.rb_reback)
    RadioButton rbReBack;

    private int comboType;
    private int page = 0;
    private Drawable drawableBottom;
    private Drawable drawableWhite;//该值得存在是为了增加RadioButton的点击区域，优化用户体验
    /**
     * vp
     */
    @ViewInject(R.id.viewPager)
    ViewPager viewPager;

    private List<List> orderListPages;
    private ArrayList<MTOrderListEntity.DataBean> allList = new ArrayList<>();
    private ArrayList<MTOrderListEntity.DataBean> payList = new ArrayList<>();
    private ArrayList<MTOrderListEntity.DataBean> useList = new ArrayList<>();
    private ArrayList<MTOrderListEntity.DataBean> evaList = new ArrayList<>();
    private ArrayList<MTOrderListEntity.DataBean> rebackList = new ArrayList<>();
    private OrderPagerAdapter orderPagerAdapter;
    private MTNetRequest mtNetRequest;
    private String imgUrl;  //图

    OrderListAdapter adapter;
    OrderListAdapter.ViewHolder holder;
    private MallNetRequest mallRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_mt);
        ViewUtils.inject(this);

        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("getOrderList: 118");
        getOrderList(comboType, null);
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MTOrderListActivity.this.finish();
            }
        });

        comboType = getIntent().getIntExtra("comboType", 0);
        Logger.i("getOrderList: 131");
        getOrderList(comboType, null);
        drawableBottom = getResources().getDrawable(R.drawable.line_red2dp);
        drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        //该值得存在是为了增加RadioButton的点击区域，优化用户体验
        drawableWhite = ContextCompat.getDrawable(mContext, R.color.white);

        orderListPages = new ArrayList<>(5);
        orderListPages.add(0, allList);
        orderListPages.add(1, payList);
        orderListPages.add(2, useList);
        orderListPages.add(3, evaList);
        orderListPages.add(4, rebackList);

        if (orderPagerAdapter == null) {
            orderPagerAdapter = new OrderPagerAdapter(mContext, orderListPages);
            viewPager.setAdapter(orderPagerAdapter);
            viewPager.setCurrentItem(comboType);
        }

        switchOrderType(comboType);
    }

    private void switchOrderType(int type) {
        switch (type) {
            case 0:
            case R.id.rb_all:
                viewPager.setCurrentItem(0);
                rbAll.setCompoundDrawables(null, null, null, drawableBottom);
                rbPay.setCompoundDrawables(null, null, null, drawableWhite);
                rbUse.setCompoundDrawables(null, null, null, drawableWhite);
                rbEva.setCompoundDrawables(null, null, null, drawableWhite);
                rbReBack.setCompoundDrawables(null, null, null, drawableWhite);
                rbAll.setChecked(true);
                break;
            case 1:
            case R.id.rb_pay:
                viewPager.setCurrentItem(1);
                rbAll.setCompoundDrawables(null, null, null, drawableWhite);
                rbPay.setCompoundDrawables(null, null, null, drawableBottom);
                rbUse.setCompoundDrawables(null, null, null, drawableWhite);
                rbEva.setCompoundDrawables(null, null, null, drawableWhite);
                rbReBack.setCompoundDrawables(null, null, null, drawableWhite);
                rbPay.setChecked(true);
                break;
            case 2:
            case R.id.rb_use:
                viewPager.setCurrentItem(2);
                rbAll.setCompoundDrawables(null, null, null, drawableWhite);
                rbPay.setCompoundDrawables(null, null, null, drawableWhite);
                rbUse.setCompoundDrawables(null, null, null, drawableBottom);
                rbEva.setCompoundDrawables(null, null, null, drawableWhite);
                rbReBack.setCompoundDrawables(null, null, null, drawableWhite);
                rbUse.setChecked(true);
                break;
            case 3:
            case R.id.rb_eva:
                viewPager.setCurrentItem(3);
                rbAll.setCompoundDrawables(null, null, null, drawableWhite);
                rbPay.setCompoundDrawables(null, null, null, drawableWhite);
                rbUse.setCompoundDrawables(null, null, null, drawableWhite);
                rbEva.setCompoundDrawables(null, null, null, drawableBottom);
                rbReBack.setCompoundDrawables(null, null, null, drawableWhite);
                rbEva.setChecked(true);
                break;
            case 4:
            case R.id.rb_reback:
                viewPager.setCurrentItem(4);
                rbAll.setCompoundDrawables(null, null, null, drawableWhite);
                rbPay.setCompoundDrawables(null, null, null, drawableWhite);
                rbUse.setCompoundDrawables(null, null, null, drawableWhite);
                rbEva.setCompoundDrawables(null, null, null, drawableWhite);
                rbReBack.setCompoundDrawables(null, null, null, drawableBottom);
                rbReBack.setChecked(true);
                break;
        }
    }

    private void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchOrderType(checkedId);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvTitle.setText("全部订单");
                        radioGroup.check(R.id.rb_all);
                        comboType = 0;
                        if (allList.size() == 0) {
                            Logger.i("getOrderList:231");
                            getOrderList(comboType, null);
                        }
                        break;
                    case 1:
                        tvTitle.setText("待付款订单");
                        radioGroup.check(R.id.rb_pay);
                        comboType = 1;
                        if (payList.size() == 0) {
                            Logger.i("getOrderList: 240");
                            getOrderList(comboType, null);
                        }
                        break;
                    case 2:
                        tvTitle.setText("待使用订单");
                        radioGroup.check(R.id.rb_use);
                        comboType = 2;
                        if (useList.size() == 0) {
                            Logger.i("getOrderList: 249");
                            getOrderList(comboType, null);
                        }
                        break;

                    case 3:
                        tvTitle.setText("待评价订单");
                        radioGroup.check(R.id.rb_eva);
                        comboType = 3;
                        if (evaList.size() == 0) {
                            Logger.i("getOrderList: 259");
                            getOrderList(comboType, null);
                        }
                        break;
                    case 4:
                        tvTitle.setText("退款订单");
                        radioGroup.check(R.id.rb_reback);
                        comboType = 4;
                        if (rebackList.size() == 0) {
                            Logger.i("getOrderList: 268");
                            getOrderList(comboType, null);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 获取订单列表数据
     */
    private void getOrderList(final int comboType, final PullToRefreshListView listView) {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }

        mtNetRequest.getMTOrderList(comboType, page, new RequestCallBack<MTOrderListEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTOrderListEntity> responseInfo) {
                stopMyDialog();
                if (listView != null) {
                    listView.onRefreshComplete();
                }
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,responseInfo.result)){
                    if (CommonUtils.serivceReturnCode(mContext,responseInfo.result.code,responseInfo.result.msg)){
                        MTOrderListEntity result = responseInfo.result;
                        switch (result.code) {
                            case 1:
                                ArrayList<MTOrderListEntity.DataBean> data = result.data;
                                if (comboType == 0) {
                                    tvTitle.setText("全部订单");
                                    if (page == 0) {
                                        allList.clear();
                                    }
                                    allList.addAll(data);
                                    if (allList.size() == 0) {
                                        orderPagerAdapter.setDataView(0, false);
                                    } else {
                                        orderPagerAdapter.setDataView(0, true);
                                        orderPagerAdapter.notifyDataSetChanged(0);
                                    }
                                }
                                if (comboType == 1) {
                                    tvTitle.setText("待付款订单");
                                    if (page == 0) {
                                        payList.clear();
                                    }
                                    payList.addAll(data);
                                    if (payList.size() == 0) {
                                        orderPagerAdapter.setDataView(1, false);
                                    } else {
                                        orderPagerAdapter.setDataView(1, true);
                                        orderPagerAdapter.notifyDataSetChanged(1);
                                    }
                                }
                                if (comboType == 2) {
                                    tvTitle.setText("待使用订单");
                                    if (page == 0) {
                                        useList.clear();
                                    }
                                    useList.addAll(data);
                                    if (useList.size() == 0) {
                                        orderPagerAdapter.setDataView(2, false);
                                    } else {
                                        orderPagerAdapter.setDataView(2, true);
                                        orderPagerAdapter.notifyDataSetChanged(2);
                                    }
                                }
                                if (comboType == 3) {
                                    tvTitle.setText("待评价订单");
                                    if (page == 0) {
                                        evaList.clear();
                                    }
                                    evaList.addAll(data);
                                    if (evaList.size() == 0) {
                                        orderPagerAdapter.setDataView(3, false);
                                    } else {
                                        orderPagerAdapter.setDataView(3, true);
                                        orderPagerAdapter.notifyDataSetChanged(3);
                                    }
                                }
                                if (comboType == 4) {
                                    tvTitle.setText("退款订单");
                                    if (page == 0) {
                                        rebackList.clear();
                                    }
                                    rebackList.addAll(data);
                                    if (rebackList.size() == 0) {
                                        orderPagerAdapter.setDataView(4, false);
                                    } else {
                                        orderPagerAdapter.setDataView(4, true);
                                        orderPagerAdapter.notifyDataSetChanged(4);
                                    }
                                }
                                break;
                        }
                    }
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                if (listView != null) {
                    listView.onRefreshComplete();
                }
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * viewpager的适配器
     */
    class OrderPagerAdapter extends BaseViewPagerAdapter {
        private Context context;

        private List<List> lists;
        private List<PullToRefreshListView> mListViews;
        private List<BaseAdapter> baseAdapterList;

        public OrderPagerAdapter(Context context, List<List> lists) {
            this.context = context;
            this.lists = lists;
            mListViews = new ArrayList<>();
            baseAdapterList = new ArrayList<>();
            getItemViews();
        }

        public void setDataView(int position, boolean hasData) {
            PullToRefreshListView list = mListViews.get(position);
            ImageView imgNoData = (ImageView) mViewList.get(position).findViewById(R.id.no_data);
            if (hasData) {
                list.setVisibility(View.VISIBLE);
                imgNoData.setVisibility(View.GONE);
            } else {
                list.setVisibility(View.GONE);
                imgNoData.setVisibility(View.VISIBLE);
            }
        }

        public void notifyDataSetChanged(int position) {
            baseAdapterList.get(position).notifyDataSetChanged();
        }

        @Override
        public void getItemViews() {
            int count = lists.size();
            for (int i = 0; i < count; ++i) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_mall_order_viewpager, null);
                final PullToRefreshListView refreshListView = (PullToRefreshListView) view.findViewById(R.id.refresh_listview);

                refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        page = 0;
                        Logger.i("getOrderList: 439");
                        getOrderList(comboType, refreshListView);
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        page++;
                        Logger.i("getOrderList: 446");
                        getOrderList(comboType, refreshListView);
                    }
                });

                adapter = new OrderListAdapter(context, lists.get(i));
                refreshListView.setAdapter(adapter);
                mViewList.add(view);
                mListViews.add(refreshListView);
                baseAdapterList.add(adapter);
            }
        }
    }

    /**
     * 订单pulltorefreshlistview的适配器
     */
    class OrderListAdapter extends BaseAdapter {
        private Context context;
        private List<MTOrderListEntity.DataBean> list;
        MTOrderListEntity.DataBean bean;

        public OrderListAdapter(Context context, List<MTOrderListEntity.DataBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null) {
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_order_mt, null);
                holder.item = (LinearLayout) convertView.findViewById(R.id.item);
                holder.tvShopName = (TextView) convertView.findViewById(R.id.tv_shop_name);
                holder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv_goods);
                holder.tvOrderName = (TextView) convertView.findViewById(R.id.tv_order_name);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tvNum = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tvOrderState = (TextView) convertView.findViewById(R.id.tv_order_state);
                holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_order_money);
                holder.tvBtnFirst = (TextView) convertView.findViewById(R.id.tv_btn_first);
                holder.tvBtnSecond = (TextView) convertView.findViewById(R.id.tv_btn_second);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (list != null) {
                bean = list.get(position);

                imgUrl  = Constants.ImageUrl + bean.packageIcon + BitmapUtil.getBitmapSuffix(context);
                if (imgUrl.equals(holder.iv.getTag())) {

                } else {
                    imageLoader.displayImage(imgUrl, holder.iv, options);
                    holder.iv.setTag(imgUrl);
                }

                //套餐状态 0待付款 1未使用待接单 2待配送 3配送中 4已使用已送达 5已退款 6已取消
                final String status = bean.status;
                //是否配送 1配送，0不配送
                final String isDel = bean.isDelivery;
                //是否评价  0未评价 1已经评价
                final String isEva = bean.isEvaluate;
                //order id
                final String id = bean.orderId;
                //packet id
                final String packetId = bean.packageId;

                holder.tvShopName.setText(bean.merchantName);
                holder.tvOrderName.setText(bean.packageName);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(new Date(Long.valueOf(bean.usableTime + "000")));
                holder.tvTime.setText("有效期限至："+time);
                holder.tvNum.setText("数量："+bean.count);

                String state = "";
                String orderState = "";
                String money = bean.price;
                ShowMTPackageTicketEntity entity = new ShowMTPackageTicketEntity();
                entity.packageName = bean.packageName;
                entity.isDelivery = bean.isDelivery;
                entity.isEvaluate = bean.isEvaluate;
                entity.usableTime = bean.usableTime;
                entity.status = bean.status;
                entity.codes = bean.codes;
                entity.packageOrderId = bean.orderId;
                switch (status) {
                    case "0":
                        state = "待付款";
                        holder.tvOrderState.setVisibility(View.INVISIBLE);
                        holder.tvBtnFirst.setVisibility(View.GONE);
                        holder.tvBtnSecond.setVisibility(View.VISIBLE);
                        holder.tvBtnSecond.setText("去付款");
                        holder.tvBtnSecond.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pay(id, money,bean.count);
                            }
                        });
                        break;
                    case "1":
                        state = "待使用";
                        holder.tvBtnSecond.setVisibility(View.VISIBLE);


                        if ("0".equals(isDel)) {
                            holder.tvBtnSecond.setText("查看券码");
                            holder.tvOrderState.setVisibility(View.INVISIBLE);
                            holder.tvBtnFirst.setVisibility(View.GONE);
                            holder.tvBtnSecond.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    check(entity);
                                }
                            });
                        } else {
                            holder.tvBtnSecond.setText("查看详情");
                            holder.tvOrderState.setVisibility(View.VISIBLE);
                            orderState = "配送状态：待接单";
                            holder.tvBtnFirst.setVisibility(View.VISIBLE);
                            holder.tvBtnFirst.setText("取消配送");
                            holder.tvBtnFirst.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(null, "您确定取消配送吗？", null, 0, Gravity.CENTER, "是",
                                            "否", true, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which) {
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                            dialog.dismiss();
                                                            translateOrder(id, "0", "0", "2", "");
                                                            break;
                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                            dialog.dismiss();
                                                            break;
                                                    }
                                                }
                                            }, context);
                                }
                            });
                            holder.tvBtnSecond.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showOrderDetail(entity);
                                }
                            });
                        }
                        break;
                    case "2":
                        state = "待配送";
                        holder.tvBtnSecond.setVisibility(View.VISIBLE);


                        holder.tvBtnFirst.setVisibility(View.GONE);
                        if ("0".equals(isDel)) {
                            holder.tvBtnSecond.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    check(entity);
                                }
                            });
                            holder.tvBtnSecond.setText("查看券码");
                            holder.tvOrderState.setVisibility(View.INVISIBLE);
                        } else {
                            holder.tvBtnSecond.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showOrderDetail(entity);
                                }
                            });
                            holder.tvBtnSecond.setText("查看详情");
                            holder.tvOrderState.setVisibility(View.VISIBLE);
                            orderState = "配送状态：待配送";
                            holder.tvBtnFirst.setText("取消配送");
                            holder.tvBtnFirst.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(null, "您确定取消配送吗？", null, 0, Gravity.CENTER, "是",
                                            "否", true, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which) {
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                            dialog.dismiss();
                                                            translateOrder(id, "0", "0", "2", "");
                                                            break;
                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                            dialog.dismiss();
                                                            break;
                                                    }
                                                }
                                            }, context);
                                }
                            });
                        }
                        break;
                    case "3":
                        state = "配送中";
                        holder.tvBtnFirst.setVisibility(View.GONE);
                        holder.tvBtnSecond.setVisibility(View.VISIBLE);


                        if ("0".equals(isDel)) {
                            holder.tvBtnSecond.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    check(entity);
                                }
                            });
                            holder.tvBtnSecond.setText("查看券码");
                            holder.tvOrderState.setVisibility(View.INVISIBLE);
                        } else {
                            holder.tvBtnSecond.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showOrderDetail(entity);
                                }
                            });
                            holder.tvBtnSecond.setText("查看详情");
                            holder.tvOrderState.setVisibility(View.VISIBLE);
                            orderState = "配送状态：配送中";
                        }
                        break;
                    case "4":
                        if ("0".equals(isDel)) {
                            holder.tvOrderState.setVisibility(View.INVISIBLE);
                        } else {
                            holder.tvOrderState.setVisibility(View.VISIBLE);
                            orderState = "配送状态：已送达";
                        }
                        holder.tvBtnFirst.setVisibility(View.VISIBLE);
                        holder.tvBtnFirst.setText("再来一单");
                        holder.tvBtnFirst.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                again(id, packetId);
                            }
                        });
                        if ("0".equals(isEva)) {
                            holder.tvBtnSecond.setVisibility(View.VISIBLE);
                            holder.tvBtnSecond.setText("去评价");
                            holder.tvBtnSecond.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    evaluate(id);
                                }
                            });
                            state = "已使用";
                        } else {
                            holder.tvBtnSecond.setVisibility(View.GONE);
                            state = "已评价";
                        }
                        break;
                    case "5":
                        state = "已退款";
                        holder.tvOrderState.setVisibility(View.INVISIBLE);
                        holder.tvBtnFirst.setVisibility(View.VISIBLE);
                        holder.tvBtnFirst.setText("再来一单");
                        holder.tvBtnFirst.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                again(id, packetId);
                            }
                        });
                        holder.tvBtnSecond.setVisibility(View.GONE);
                        break;
                    case "6":
                        state = "已取消";
                        holder.tvOrderState.setVisibility(View.INVISIBLE);
                        holder.tvBtnFirst.setVisibility(View.VISIBLE);
                        holder.tvBtnFirst.setText("再来一单");
                        holder.tvBtnFirst.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                again(id, packetId);
                            }
                        });
                        holder.tvBtnSecond.setVisibility(View.VISIBLE);
                        holder.tvBtnSecond.setText("删除订单");
                        holder.tvBtnSecond.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialog(null, "您确定删除订单吗?", null, 0, Gravity.CENTER, "是",
                                        "否", true, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        dialog.dismiss();
                                                        deleteOrder(id);
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        dialog.dismiss();
                                                        break;
                                                }
                                            }
                                        }, context);

                            }
                        });
                        break;
                }
                holder.tvOrderState.setText(orderState);
                holder.tvState.setText(state);
                holder.tvMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(bean.price)));

                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MTOrderListActivity.this, MTOrderDetailActivity.class);
                        intent.putExtra("index_id", id);
                        //intent.putExtra("img_url", imgUrl);
                        intent.putExtra("status", status);
                        intent.putExtra("is_del", isDel);
                        intent.putExtra("is_eva", isEva);
                        startActivity(intent);
                    }
                });
            }

            return convertView;
        }

        class ViewHolder {
            private LinearLayout item;
            private TextView tvShopName;
            private TextView tvState;
            private ImageView iv;
            private TextView tvOrderName;
            private TextView tvTime;
            private TextView tvNum;
            private TextView tvOrderState;
            private TextView tvMoney;
            private TextView tvBtnFirst;
            private TextView tvBtnSecond;
        }
    }

    /**
     * 删除订单
     * @param packetId
     */
    private void deleteOrder(String packetId) {
        if (mallRequest == null) {
            mallRequest = new MallNetRequest(mContext);
        }
        mallRequest.deleteOrder(packetId, "1", new RequestCallBack<DeleteOrderEntity>() {
            @Override
            public void onStart() {
                startMyDialog();
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<DeleteOrderEntity> responseInfo) {
                DeleteOrderEntity result = responseInfo.result;
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,responseInfo.result)){
                    if(CommonUtils.serivceReturnCode(mContext,result.code,result.msg)){
                        switch (result.code) {
                            case 1:
                                Logger.i("getOrderList: 827");
                                getOrderList(comboType, null);
                                showToast(R.string.delete_order_success);
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


    /**
     * 支付
     */
    public void pay(String id, String money,String count) {
        Intent intent = new Intent(MTOrderListActivity.this, CashDeskActivity.class);
        intent.putExtra("orderIndex", id);
        intent.putExtra("type", "MTPackage");
        intent.putExtra("payType", "6");
        intent.putExtra("order_total_lebi", money);
        intent.putExtra("order_total_coupon", "0");
        intent.putExtra("orderNumber",count );
        startActivity(intent);
    }

    /**
     * 取消配送
     */
    private void translateOrder(String id, String count, String address, final String type, String pwd) {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.switchMT(id, count, address, type, pwd, new RequestCallBack<MTEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
//                        holder.tvBtnFirst.setVisibility(View.GONE);
//                        holder.tvOrderState.setVisibility(View.INVISIBLE);
                        showToast("取消配送成功，您可前往店内消费使用");
//                        adapter.notifyDataSetChanged();

                        Logger.i("getOrderList: 885");
                        getOrderList(comboType, null);
                        break;
                    case -81:
                        showToast("商家已接单，请耐心等待商家配送哦");
                        break;
                    default:
                        showToast(""+ responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 查看券码
     */
    public void check(ShowMTPackageTicketEntity entity) {

        Intent intent = new Intent(MTOrderListActivity.this, MTPackageTicketActivity.class);
        intent.putExtra("ShowMTPackageTicketEntity", entity);
        Logger.i("2017-1-4:" + entity.toString());
        startActivity(intent);
    }

    /**
     * 订单详情
     * @param bean
     */
    private void showOrderDetail(ShowMTPackageTicketEntity bean) {
        Intent intent = new Intent(MTOrderListActivity.this, MTOrderDetailActivity.class);
        intent.putExtra("index_id", bean.packageOrderId);
        //intent.putExtra("img_url", imgUrl);
        intent.putExtra("status", bean.status);
        intent.putExtra("is_del", bean.isDelivery);
        intent.putExtra("is_eva", bean.isEvaluate);
        startActivity(intent);
    }
    /**
     * 再来一次
     */
    public void again(String id, String packetId) {
        Intent intent = new Intent(MTOrderListActivity.this, MTComboDetailsActivity.class);
        intent.putExtra("package_id", packetId);
        intent.putExtra("merchant_id", id);
        startActivity(intent);
    }

    /**
     * 评价
     */
    public void evaluate(String id) {
        Intent intent = new Intent(MTOrderListActivity.this, MTPackageCommentActivity.class);
        intent.putExtra(Constants.SPKEY_MT_PACKAGE_ORDER_ID, id);
        startActivity(intent);
        finish();
    }

}
