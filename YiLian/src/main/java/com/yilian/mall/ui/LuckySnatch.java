package com.yilian.mall.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BaseViewPagerAdapter;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.SnatchListEntity;
import com.yilian.mall.entity.SnatchPartEntity;
import com.yilian.mall.http.SnatchNetRequest;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.SnatchDialog;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.entity.UserAddressLists;

import java.util.ArrayList;
import java.util.List;


/**
 * 天天夺宝列表界面
 * Created by Administrator on 2016/4/5.
 */
public class LuckySnatch extends BaseActivity {

    @ViewInject(R.id.lucky_snatch_rg)
    private RadioGroup radioGroup;

    @ViewInject(R.id.lucky_snatch_going_rb)
    private RadioButton snatchGoing;

    @ViewInject(R.id.lucky_snatch_over_rb)
    private RadioButton snatchOver;

    @ViewInject(R.id.lucky_snatch_mypart_rb)
    private RadioButton myPart;

    @ViewInject(R.id.lucky_snatch_vp)
    private ViewPager viewPager;

    @ViewInject(R.id.tv_shape_my_part)
    private TextView tv_shape_my_part;

    private Drawable drawableBottom;

    private PagerAdapter pagerAdapter;
    private SnatchNetRequest request;
    private List<List<SnatchListEntity.Goods>> luckySnatchListsPagers;
    private List<SnatchListEntity.Goods> goingList = new ArrayList<>();
    private List<SnatchListEntity.Goods> overList = new ArrayList<>();
    private List<SnatchListEntity.Goods> myPartList = new ArrayList<>();

    private int type;
    private int page = 0;
    private String activityId;
    private GoodsListAdapter listAdapter;
    private String activityExpend;

    private List<SnatchListEntity.Goods> myPartListsign = new ArrayList<>();

    //支付dialog消失时刷新UI数据
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == Constants.EXECUTE_SUCCESS) {
                getSnatchList(0, null);
            }

        }
    };
    private String addressId;
    //收货完成时刷新UI
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getSnatchList(0, null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckysnatch);
        ViewUtils.inject(this);
        initView();
        getExpend();
        listener();
        getSnatchList(1, null);

    }

    /**
     * 获取夺宝乐享币数量
     */
    private void getExpend() {
        request.snatchingNet(activityId, 0, new RequestCallBack<SnatchPartEntity>() {
            @Override
            public void onSuccess(ResponseInfo<SnatchPartEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        activityExpend = responseInfo.result.activity.activity_expend;
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public void jumpToRule(View view) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("title_name", "夺宝说明");
        intent.putExtra("url", Ip.getHelpURL()+ "agreementforios/snatch.html");
        startActivity(intent);
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
                addressId = userAddressList.address_id;
                activityId = data.getStringExtra("activityId");
                Logger.i(activityId + "-------" + addressId + "------");
                DeliveryDialog dialog = new DeliveryDialog(mContext, activityId, addressId);
                dialog.show();
                break;
        }
    }

    //可交互时刷新数据
    @Override
    protected void onResume() {
        super.onResume();
        getSnatchList(0, null);
        getSnatchList(1, null);
        getSnatchList(2, null);
        pagerAdapter.notifyDataSetChanged(0);
        pagerAdapter.notifyDataSetChanged(1);
        pagerAdapter.notifyDataSetChanged(2);
        listAdapter.notifyDataSetChanged();
    }

    private void initView() {
//        drawableBottom = getResources().getDrawable(R.drawable.line_blue);
//        drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        request = new SnatchNetRequest(mContext);

        luckySnatchListsPagers = new ArrayList<>(3);
        luckySnatchListsPagers.add(0, goingList);
        luckySnatchListsPagers.add(1, overList);
        luckySnatchListsPagers.add(2, myPartList);
        if (pagerAdapter == null) {
            pagerAdapter = new PagerAdapter(mContext, luckySnatchListsPagers, handler);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(0);
        }
    }

    private void listener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.lucky_snatch_going_rb:
                        viewPager.setCurrentItem(0);
//                        snatchGoing.setCompoundDrawables(null, null, null, drawableBottom);
//                        snatchOver.setCompoundDrawables(null, null, null, null);
//                        myPart.setCompoundDrawables(null, null, null, null);
                        break;
                    case R.id.lucky_snatch_over_rb:
                        viewPager.setCurrentItem(1);
//                        snatchGoing.setCompoundDrawables(null, null, null, null);
//                        snatchOver.setCompoundDrawables(null, null, null, drawableBottom);
//                        myPart.setCompoundDrawables(null, null, null, null);
                        break;
                    case R.id.lucky_snatch_mypart_rb:
                        if (!isLogin()) {
                            startActivity(new Intent(LuckySnatch.this, LeFenPhoneLoginActivity.class));
                            radioGroup.check(R.id.lucky_snatch_over_rb);
                            return;
                        } else {
                            viewPager.setCurrentItem(2);
                        }
//                        snatchGoing.setCompoundDrawables(null, null, null, null);
//                        snatchOver.setCompoundDrawables(null, null, null, null);
//                        myPart.setCompoundDrawables(null, null, null, drawableBottom);
                        break;
                }
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.lucky_snatch_going_rb);
                        type = 1;
                        if (goingList.size() == 0) {
                            getSnatchList(type, null);
                        }
                        break;
                    case 1:
                        radioGroup.check(R.id.lucky_snatch_over_rb);
                        type = 2;
                        if (overList.size() == 0) {
                            getSnatchList(type, null);
                        }
                        break;
                    case 2:
                        if (!isLogin()) {
                            startActivity(new Intent(LuckySnatch.this, LeFenPhoneLoginActivity.class));
                            viewPager.setCurrentItem(1);
                            return;
                        } else {
                            radioGroup.check(R.id.lucky_snatch_mypart_rb);
                            type = 0;
                            if (myPartList.size() == 0) {
                                getSnatchList(type, null);
                            }
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
     * 获取夺宝列表
     *
     * @param type     1 ：进行中  2：已揭晓  0：我参与的
     * @param listView 下拉刷新listview
     */
    private void getSnatchList(final int type, final PullToRefreshListView listView) {
        String token = "0";
        if (isLogin())
            token = RequestOftenKey.getToken(mContext);

        startMyDialog();
        request.snatchListNet(token, type, page, new RequestCallBack<SnatchListEntity>() {
            @Override
            public void onSuccess(ResponseInfo<SnatchListEntity> responseInfo) {
                if (listView != null) {
                    listView.onRefreshComplete();
                }
                if (responseInfo.result == null) {
                    return;
                }
                switch (responseInfo.result.code) {
                    case 1:
                        Logger.json(responseInfo.result.toString());
                        stopMyDialog();
                        switch (type) {
                            case 0:  // 我参与的
                                if (page == 0) {
                                    myPartList.clear();
                                }
                                myPartList.addAll(responseInfo.result.goodsList);
                                if (myPartList.size() == 0) {
                                    tv_shape_my_part.setVisibility(View.GONE);
                                    pagerAdapter.setDataView(2, false);
                                } else {
                                    myPartListsign.clear();

                                    for (int i = 0; i < myPartList.size(); i++) {
                                        if (myPartList.get(i).hasAddress == 1 || myPartList.get(i).hasEnd.equals("2")) {
                                            myPartListsign.add(myPartList.get(i));
                                        }
                                    }
                                    if (myPartList.size() - myPartListsign.size() == 0) {
                                        tv_shape_my_part.setVisibility(View.GONE);
                                    } else {
                                        tv_shape_my_part.setText(myPartList.size() - myPartListsign.size() + "");
                                        tv_shape_my_part.setVisibility(View.VISIBLE);
                                    }
                                    pagerAdapter.setDataView(2, true);
                                    pagerAdapter.notifyDataSetChanged(2);
                                }
                                break;
                            case 1: // 进行中的
                                if (page == 0) {
                                    goingList.clear();
                                }
                                goingList.addAll(responseInfo.result.goodsList);
                                if (goingList.size() == 0) {
                                    pagerAdapter.setDataView(0, false);
                                } else {
                                    pagerAdapter.setDataView(0, true);
                                    pagerAdapter.notifyDataSetChanged(0);
                                }

                                break;
                            case 2: // 已揭晓
                                if (page == 0) {
                                    overList.clear();
                                }
                                overList.addAll(responseInfo.result.goodsList);
                                if (overList.size() == 0) {
                                    pagerAdapter.setDataView(1, false);
                                } else {
                                    pagerAdapter.setDataView(1, true);
                                    pagerAdapter.notifyDataSetChanged(1);
                                }
                                break;
                        }
                        break;

                    case -4:
                        sp.edit().putBoolean("state", false).commit();
                        getSnatchList(1, listView);
                        break;
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
     * listview的适配器
     */
    public class GoodsListAdapter extends BaseAdapter {
        Drawable drawable = null;
        ViewHolder holder;
        private String activityExpend;
        private Context context;
        private List<SnatchListEntity.Goods> goodsLists;
        private int i;
        private Handler handler;

        public GoodsListAdapter(Context context, String activityExpend, int i, List<SnatchListEntity.Goods> goodsLists, Handler handler) {
            this.context = context;
            this.goodsLists = goodsLists;
            this.i = i;
            this.activityExpend = activityExpend;
            this.handler = handler;
            this.activityExpend = activityExpend;
        }

        @Override
        public int getCount() {
            if (goodsLists != null) {
                return goodsLists.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return goodsLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_lucky_snatch, null);
                holder.activity_prize_url = (ImageView) convertView.findViewById(R.id.activity_prize_url);
                holder.img_yizhongjiang= (ImageView) convertView.findViewById(R.id.img_yizhongjiang);
                holder.activity_name = (TextView) convertView.findViewById(R.id.activity_name);
                holder.activity_start_time = (TextView) convertView.findViewById(R.id.activity_start_time);
                holder.item_pro = (ProgressBar) convertView.findViewById(R.id.item_pro);
                holder.activity_play_count = (TextView) convertView.findViewById(R.id.activity_play_count);
                holder.activity_total_count = (TextView) convertView.findViewById(R.id.activity_total_count);
                holder.activity_other_count = (TextView) convertView.findViewById(R.id.activity_other_count);
                //已揭晓
                holder.yinzhang_iv = (ImageView) convertView.findViewById(R.id.yinzhang_iv);

                //我参与的progress
                holder.ll_my_part_pro = (LinearLayout) convertView.findViewById(R.id.ll_my_part_pro);
                holder.item_pro_my = (ProgressBar) convertView.findViewById(R.id.item_pro_my);
                holder.tv_my_part = (TextView) convertView.findViewById(R.id.tv_my_part);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (null != goodsLists) {
                final SnatchListEntity.Goods data = goodsLists.get(position);
                if (!TextUtils.isEmpty(data.goodsIcon))
                    new BitmapUtils(context).display(holder.activity_prize_url, Constants.ImageUrl + data.goodsIcon + Constants.ImageSuffix, null, new BitmapLoadCallBack<ImageView>() {
                        @Override
                        public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                            imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onLoading(ImageView container, String uri, BitmapDisplayConfig config, long total, long current) {
                            super.onLoading(container, uri, config, total, current);
                            container.setImageResource(R.mipmap.login_module_default_jp);
                        }

                        @Override
                        public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                            imageView.setImageResource(R.mipmap.login_module_default_jp);
                        }
                    });

                if (goodsLists.get(position).goodsName==null){
                    holder.activity_name.setText("活动名称");
                }else {
                    holder.activity_name.setText(goodsLists.get(position).goodsName);
                }

                holder.activity_start_time.setText(StringFormat.formatDate(data.activity_submit_time, "yyyy-MM-dd"));
                Logger.i(data.activity_submit_time);
                holder.item_pro.setMax(data.totalCount);
                holder.item_pro.setProgress(data.joinCount);
                holder.activity_play_count.setText(data.joinCount + "");
                holder.activity_total_count.setText(data.totalCount + "");
                holder.activity_other_count.setText(data.totalCount - data.joinCount + "");


                switch (i) {
                    //根据listview位置判断
                    case 0://正在进行
                        holder.item_pro.setVisibility(View.VISIBLE);
                        holder.yinzhang_iv.setVisibility(View.GONE);
                        holder.ll_my_part_pro.setVisibility(View.GONE);
                        break;
                    case 1://已揭晓

                        holder.item_pro.setVisibility(View.VISIBLE);
                        holder.yinzhang_iv.setVisibility(View.VISIBLE);
                        holder.ll_my_part_pro.setVisibility(View.GONE);
                        break;
                    case 2://我参与的
                        holder.item_pro.setVisibility(View.GONE);
                        holder.yinzhang_iv.setVisibility(View.GONE);
                        holder.ll_my_part_pro.setVisibility(View.VISIBLE);
                        holder.item_pro_my.setMax(data.totalCount);
                        holder.item_pro_my.setProgress(data.joinCount);

                        //是否结束  1 未结束 2 结束
                        if (data.hasEnd.equals("1")) {
                            holder.tv_my_part.setText("追加");
                            holder.tv_my_part.setTextColor(context.getResources().getColor(R.color.zhuijia));
                            holder.tv_my_part.setBackgroundResource(R.drawable.round_corner_blue);
                            holder.img_yizhongjiang.setVisibility(View.GONE);
                            holder.tv_my_part.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (myPartList.size() != 0) {
                                        activityId = myPartList.get(position).activityId;
                                        SnatchDialog dialog = new SnatchDialog(context, activityId, data.goodsName, data.joinCount, handler);
                                        dialog.show();
                                    }
                                }
                            });
                        }
                        if (data.hasEnd.equals("2")) {
                            //是否中奖  0未中奖  1中奖
                            if (data.hasPrize == 0) {
                                holder.tv_my_part.setText("结束");
                                holder.tv_my_part.setClickable(false);
                                holder.tv_my_part.setTextColor(context.getResources().getColor(R.color.white));
                                holder.tv_my_part.setBackgroundResource(R.drawable.round_corner_over);
                                holder.img_yizhongjiang.setVisibility(View.GONE);
                            }
                            if (data.hasPrize == 1) {
                                //  0未设置收货地址  1已设置
                                if (data.hasAddress == 0) {
                                    holder.tv_my_part.setText("收货");
                                    holder.tv_my_part.setTextColor(context.getResources().getColor(R.color.zhuijia));
                                    holder.tv_my_part.setBackgroundResource(R.drawable.round_corner_blue);
                                    holder.img_yizhongjiang.setVisibility(View.GONE);
                                    holder.tv_my_part.setClickable(true);
                                    holder.tv_my_part.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //设置收货地址
                                            Intent intent = new Intent(LuckySnatch.this, AddressManageActivity.class);
                                            intent.putExtra("FLAG", "orderId");
                                            intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
                                            intent.putExtra("activityId", activityId = myPartList.get(position).activityId);
                                            startActivityForResult(intent, 0);
                                        }
                                    });
                                }
                                if (data.hasAddress == 1) {
                                    holder.tv_my_part.setText("完成");
                                    holder.tv_my_part.setTextColor(context.getResources().getColor(R.color.white));
                                    holder.tv_my_part.setBackgroundResource(R.drawable.round_corner_over);
                                    holder.img_yizhongjiang.setVisibility(View.VISIBLE);
                                    holder.tv_my_part.setClickable(false);
                                }
                            }
                        }
                        break;
                }
            }
            return convertView;
        }


        class ViewHolder {
            ImageView activity_prize_url,img_yizhongjiang;
            TextView activity_name;
            TextView activity_start_time;
            ProgressBar item_pro;
            TextView activity_play_count;
            TextView activity_total_count;
            TextView activity_other_count;
            ImageView yinzhang_iv;
            LinearLayout ll_my_part_pro;
            ProgressBar item_pro_my;
            TextView tv_my_part;
        }
    }

    /**
     * viewpager的适配器
     */
    class PagerAdapter extends BaseViewPagerAdapter {
        public List<PullToRefreshListView> mListViews;
        private Context context;
        private List<List<SnatchListEntity.Goods>> lists;
        private List<BaseAdapter> baseAdapterList;
        private Handler handler;

        public PagerAdapter(Context context, List<List<SnatchListEntity.Goods>> lists) {
            this.context = context;
            this.lists = lists;
            mListViews = new ArrayList<>();
            baseAdapterList = new ArrayList<>();
            getItemViews();
        }

        public PagerAdapter(Context context, List<List<SnatchListEntity.Goods>> lists, Handler handler) {
            this.context = context;
            this.lists = lists;
            mListViews = new ArrayList<>();
            baseAdapterList = new ArrayList<>();
            this.handler = handler;
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
            for (int z = 0; z < count; z++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_mall_order_viewpager, null);
                final PullToRefreshListView refreshListView = (PullToRefreshListView) view.findViewById(R.id.refresh_listview);

                refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        page = 0;
                        getSnatchList(type, refreshListView);
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        page++;
                        getSnatchList(type, refreshListView);
                    }
                });

                listAdapter = new GoodsListAdapter(context, activityExpend, z, lists.get(z), handler);
                refreshListView.setAdapter(listAdapter);
                final int finalZ = z;
                refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String activityId = ((SnatchListEntity.Goods) adapterView.getItemAtPosition(i)).activityId;
                        Intent intent = new Intent(context, ProductDetails.class);
                        intent.putExtra("activity_id", activityId);
                        context.startActivity(intent);
                    }
                });
                mViewList.add(view);
                mListViews.add(refreshListView);
                baseAdapterList.add(listAdapter);
            }
        }
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
                                    filter.addAction(Constants.ACTIVITY_STATE_END);
                                    filter.setPriority(Integer.MAX_VALUE - 3);
                                    context.registerReceiver(mReceiver, filter);
                                    sendBroadcast(new Intent(Constants.ACTIVITY_STATE_END));
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
