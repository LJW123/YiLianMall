package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.DefaultAdapter;
import com.yilian.mall.adapter.ViewHolder;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mall.widgets.CircleImageView3;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.WheelOfFortuneAwardListEntity;
import com.yilian.networkingmodule.entity.WheelOfFortunePrizeListEntity;
import com.yilian.networkingmodule.entity.WheelOfFortuneResultEntity;
import com.yilian.networkingmodule.entity.WheelPrizeEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WheelOfFortuneActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final int SLEEP_TIME = -1;
    public static final int MESSAGE_WHAT = 20;
    private ImageView ivNetworkError;
    private ImageView ivNowPrize;
    private ImageButton btnStart;
    private TextView tvCost;
    private ImageButton btnRule;
    private ImageButton btnMyPrize;
    private TextView tvTitle;
    private ListView listviewPrizeList;
    private NoScrollListView listviewRule;
    private ScrollView scrollView;
    private CircleImageView3 ivPrize1;
    private ImageView ivNowPrize1;
    private CircleImageView3 ivPrize2;
    private ImageView ivNowPrize2;
    private CircleImageView3 ivPrize3;
    private ImageView ivNowPrize3;
    private CircleImageView3 ivPrize4;
    private ImageView ivNowPrize4;
    private CircleImageView3 ivPrize5;
    private ImageView ivNowPrize5;
    private CircleImageView3 ivPrize6;
    private ImageView ivNowPrize6;
    private CircleImageView3 ivPrize7;
    private ImageView ivNowPrize7;
    private CircleImageView3 ivPrize8;
    private ImageView ivNowPrize8;
    //    8个奖品控件集合
    private ArrayList<ImageView> prizeImageViewList = new ArrayList<>();
    //    8个奖品控件的父控件集合，用于控制奖品图片宽高
    private ArrayList<View> prizeViewList = new ArrayList<>();

    public static final int MESSAGE_1 = 1;
    public static final int MESSAGE_2 = 2;
    public static final int MESSAGE_3 = 3;
    public static final int MESSAGE_4 = 4;
    public static final int MESSAGE_5 = 5;
    public static final int MESSAGE_6 = 6;
    public static final int MESSAGE_7 = 7;
    public static final int MESSAGE_8 = 8;
    public static final int MESSAGE_STOP = 10;
    int number = 8;
    private View view1;
    private View view2;
    /**
     * 奖品列表
     */
    private ArrayList<WheelPrizeEntity> prizeList;
    /**
     * 中奖奖品的位置 逆时针1-8；
     */
    private int prizeId;
    private String version;
    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private Runnable runnable;
    private View icCludePrizeTitle;
    private WheelOfFortuneAwardListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_of_fortune);
        startMyDialog();
        initView();
        initData();
    }

    private void initData() {
        LinearLayout.LayoutParams params = null;
        getPrizeList();
        getWheelOfFortuneAwardList();
        for (int i = 0; i < prizeViewList.size(); i++) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(3, 3, 3, 3);
            prizeViewList.get(i).setLayoutParams(params);
        }
        btnStart.setLayoutParams(params);
    }

    /**
     * 获取大转盘获奖名单列表，并进行循环滚动
     */
    private void getWheelOfFortuneAwardList() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getWheelOfFortuneAwardList("1", "", "", "", new Callback<WheelOfFortuneAwardListEntity>() {
                    @Override
                    public void onResponse(Call<WheelOfFortuneAwardListEntity> call, Response<WheelOfFortuneAwardListEntity> response) {
                        stopMyDialog();
                        WheelOfFortuneAwardListEntity body = response.body();
                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                            showToast(R.string.service_exception);
                            return;
                        }
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                            switch (body.code) {
                                case 1:
                                    WheelOfFortuneAwardListEntity.DataBean data = body.data;
                                    ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean> list = data.list;
                                    if (list.size() <= 0) {
                                        view1.setVisibility(View.GONE);
                                        listviewPrizeList.setVisibility(View.GONE);
                                        icCludePrizeTitle.setVisibility(View.GONE);
                                        return;
                                    }

                                    Iterator<WheelOfFortuneAwardListEntity.DataBean.ListBean> iterator = list.iterator();
                                    ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean> newList = new ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean>();
                                    if (list.size() < 4) {//如果数据小于4条，那么对数量进行翻倍
                                        Logger.i("list adapter 走了这里1");
                                        while (iterator.hasNext()) {
                                            WheelOfFortuneAwardListEntity.DataBean.ListBean next = iterator.next();
                                            newList.add(next);
                                        }
                                    }
                                    if (newList.size() ==1) { //如果只有一条数据，那么加三遍
                                        list.addAll(newList);
                                        list.addAll(newList);
                                        Logger.i("list adapter 走了这里2");
                                        list.addAll(newList);
                                    } else {
                                        list.addAll(newList);
                                    }

                                    Logger.i("list adapter 走了这里3");
                                    adapter = new WheelOfFortuneAwardListAdapter(list);
                                    listviewPrizeList.setAdapter(adapter);
                                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT,1000);//延迟1S发送消息
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<WheelOfFortuneAwardListEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.net_work_not_available);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i("handler回收了所有message");
        mHandler.removeMessages(MESSAGE_WHAT);
    }




    private void initView() {
        ivNetworkError = (ImageView) findViewById(R.id.iv_network_error);
        ivNowPrize = (ImageView) findViewById(R.id.iv_now_prize);
        btnStart = (ImageButton) findViewById(R.id.btn_start);
        tvCost = (TextView) findViewById(R.id.tv_cost);
        btnRule = (ImageButton) findViewById(R.id.btn_rule);
        btnMyPrize = (ImageButton) findViewById(R.id.btn_my_prize);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("大转盘");
        listviewPrizeList = (ListView) findViewById(R.id.listview_prize_list);
        listviewRule = (NoScrollListView) findViewById(R.id.listview_rule);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        icCludePrizeTitle = findViewById(R.id.include_prize_title);
        view1 = findViewById(R.id.view1);
        ((TextView) (view1.findViewById(R.id.tv_title))).setText("获奖名单");
        view2 = findViewById(R.id.view2);
        ((TextView) (view2.findViewById(R.id.tv_title))).setText("活动规则");

        View include1 = findViewById(R.id.include1);
        ivPrize1 = (CircleImageView3) include1.findViewById(R.id.iv_prize);
        prizeImageViewList.add(ivPrize1);
        ivNowPrize1 = (ImageView) include1.findViewById(R.id.iv_now_prize);
        prizeViewList.add(include1);
        //默认第一个高亮显示
        setNowPrice(ivPrize1, ivNowPrize1);

        View include2 = findViewById(R.id.include2);
        ivPrize2 = (CircleImageView3) include2.findViewById(R.id.iv_prize);
        prizeImageViewList.add(ivPrize2);
        ivNowPrize2 = (ImageView) include2.findViewById(R.id.iv_now_prize);
        prizeViewList.add(include2);

        View include3 = findViewById(R.id.include3);
        ivPrize3 = (CircleImageView3) include3.findViewById(R.id.iv_prize);
        prizeImageViewList.add(ivPrize3);
        ivNowPrize3 = (ImageView) include3.findViewById(R.id.iv_now_prize);
        prizeViewList.add(include3);

        View include4 = findViewById(R.id.include4);
        ivPrize4 = (CircleImageView3) include4.findViewById(R.id.iv_prize);
        prizeImageViewList.add(ivPrize4);
        ivNowPrize4 = (ImageView) include4.findViewById(R.id.iv_now_prize);
        prizeViewList.add(include4);

        View include5 = findViewById(R.id.include5);
        ivPrize5 = (CircleImageView3) include5.findViewById(R.id.iv_prize);
        prizeImageViewList.add(ivPrize5);
        ivNowPrize5 = (ImageView) include5.findViewById(R.id.iv_now_prize);
        prizeViewList.add(include5);

        View include6 = findViewById(R.id.include6);
        ivPrize6 = (CircleImageView3) include6.findViewById(R.id.iv_prize);
        prizeImageViewList.add(ivPrize6);
        ivNowPrize6 = (ImageView) include6.findViewById(R.id.iv_now_prize);
        prizeViewList.add(include6);

        View include7 = findViewById(R.id.include7);
        ivPrize7 = (CircleImageView3) include7.findViewById(R.id.iv_prize);
        prizeImageViewList.add(ivPrize7);
        ivNowPrize7 = (ImageView) include7.findViewById(R.id.iv_now_prize);
        prizeViewList.add(include7);

        View include8 = findViewById(R.id.include8);
        ivPrize8 = (CircleImageView3) include8.findViewById(R.id.iv_prize);
        prizeImageViewList.add(ivPrize8);
        ivNowPrize8 = (ImageView) include8.findViewById(R.id.iv_now_prize);
        prizeViewList.add(include8);

        btnStart.setOnClickListener(this);
        btnRule.setOnClickListener(this);
        btnMyPrize.setOnClickListener(this);
        scrollView.fullScroll(0);
        scrollView.scrollTo(0, 0);
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        tvLeft1.setOnClickListener(this);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        rlTvLeft1.setOnClickListener(this);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        ivLeft1.setImageResource(R.mipmap.iv_back);
        ivLeft1.setOnClickListener(this);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        rlIvLeft1.setOnClickListener(this);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        ivLeft2.setOnClickListener(this);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        rlIvLeft2.setOnClickListener(this);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        ivTitle.setOnClickListener(this);
        ivTitle.setVisibility(View.GONE);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight1.setOnClickListener(this);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        ivRight2.setOnClickListener(this);
        ivRight2.setVisibility(View.GONE);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        rlIvRight2.setOnClickListener(this);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setOnClickListener(this);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        rlTvRight.setOnClickListener(this);
        viewLine = (View) findViewById(R.id.view_line);
        viewLine.setOnClickListener(this);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        llJpTitle.setOnClickListener(this);
    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WHAT:
                    scrollListView();
                    break;
                case MESSAGE_1:
//                    initAllIcon();
                    setNowPrice(ivNowPrize8, ivNowPrize1);
                    break;
                case MESSAGE_2:
                    setNowPrice(ivNowPrize1, ivNowPrize2);
                    break;
                case MESSAGE_3:
                    setNowPrice(ivNowPrize2, ivNowPrize3);
                    break;
                case MESSAGE_4:
                    setNowPrice(ivNowPrize3, ivNowPrize4);
                    break;
                case MESSAGE_5:
                    setNowPrice(ivNowPrize4, ivNowPrize5);
                    break;
                case MESSAGE_6:
                    setNowPrice(ivNowPrize5, ivNowPrize6);
                    break;
                case MESSAGE_7:
                    setNowPrice(ivNowPrize6, ivNowPrize7);
                    break;
                case MESSAGE_8:
                    setNowPrice(ivNowPrize7, ivNowPrize8);
                    break;

                case MESSAGE_STOP:
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    int i = bundle.getInt("msg");
                    String imgUrl = bundle.getString("imgUrl");
                    WheelOfFortuneResultEntity.DataBean data = (WheelOfFortuneResultEntity.DataBean) bundle.getSerializable("data");
                    switch (i % 8) {
                        case 0:
                            getResult(8, data);
                            break;
                        case 1:
                            getResult(1, data);
                            break;
                        case 2:
                            getResult(2, data);
                            break;
                        case 3:
                            getResult(3, data);
                            break;
                        case 4:
                            getResult(4, data);
                            break;
                        case 5:
                            getResult(5, data);
                            break;
                        case 6:
                            getResult(6, data);
                            break;
                        case 7:
                            getResult(7, data);
                            break;

                        default:
                            break;
                    }

                    break;

            }
        }
    };

    /**
     * 滑动listview
     */
    private void scrollListView() {
        if (listviewPrizeList != null) {
            int position = listviewPrizeList.getFirstVisiblePosition() + 5;
            Logger.i("当前position："+listviewPrizeList.getFirstVisiblePosition()+"  滑动到position：" + position+"  listviewPrizeList:"+listviewPrizeList.hashCode());
            listviewPrizeList.smoothScrollToPositionFromTop(position, 0, 1000);
            mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT,2000);
        }
    }

    /**
     * 设置高亮奖项
     *
     * @param imageView1 高亮奖项位置前一位
     * @param imageView2 高亮奖项位置
     */
    private void setNowPrice(ImageView imageView1, ImageView imageView2) {
        imageView1.setBackgroundResource(R.mipmap.bg_wheel_prize_black);
        imageView2.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 旋转结束
     * @param index
     * @param data
     */
    public void getResult(int index, WheelOfFortuneResultEntity.DataBean data) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WheelOfFortuneActivity.this, WheelOfFortuneDialogActivity.class);
                intent.putExtra("data", data);
                startActivityForResult(intent, 0);
                btnStart.setClickable(true);
                btnRule.setClickable(true);
                btnMyPrize.setClickable(true);
            }
        }, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:
                break;
            case 200:
                getPrizeResult();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if (prizeList != null) {
                    if (prizeList.size() < 8) {
                        showToast("奖品异常");
                        return;
                    }

                    getPrizeResult();
                }
                break;
            case R.id.btn_rule:
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                break;
            case R.id.btn_my_prize:
                if (isLogin()) {
                    startActivity(new Intent(mContext, PrizeGoodsListActivity.class));
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
                break;
            case R.id.iv_left1:
                if (!isRotating) {
                    finish();
                } else {
                    showToast();
                }
                break;
            case R.id.tv_right:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isRotating) {
                showToast();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnStart.setClickable(true);//保证跳转登录页面回来后，能够点击开始按钮
    }

    /**
     * 获取中奖结果并开始旋转
     */
    public void getPrizeResult() {
        btnStart.setClickable(false);
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getWheelOfFortuneResult(version, new Callback<WheelOfFortuneResultEntity>() {
                    @Override
                    public void onResponse(Call<WheelOfFortuneResultEntity> call, Response<WheelOfFortuneResultEntity> response) {
                        stopMyDialog();
                        WheelOfFortuneResultEntity body = response.body();
                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                            showToast(R.string.service_exception);
                            return;
                        }
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                            switch (body.code) {
                                case 1:
                                    Logger.i("返回中奖ID：" + body.data.prizeId);
                                    if (body.data.goods.size() == 8) {
                                        prizeList.clear();
                                        prizeList = body.data.goods;
                                        setPrizeImage();
                                    }
                                    for (int i = 0; i < prizeList.size(); i++) {
                                        WheelPrizeEntity listBean = prizeList.get(i);
                                        Logger.i("body.data.prizeId:" + body.data.prizeId + "  listBean.id:" + listBean.prizeId);
                                        if (body.data.prizeId == listBean.prizeId) {
                                            prizeId = i + 6;
                                            startWheel(body.data);
                                            return;
                                        }
                                    }
                                    break;
                            }
                        } else {
                            btnStart.setClickable(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<WheelOfFortuneResultEntity> call, Throwable t) {
                        btnStart.setClickable(true);
                        stopMyDialog();
                        showToast(R.string.net_work_not_available);
                    }
                });
    }

    private boolean isRotating;//转盘是否正在旋转

    /**
     * 开始旋转
     *
     * @param data
     */
    private void startWheel(WheelOfFortuneResultEntity.DataBean data) {
        Logger.i("开始旋转");
        btnRule.setClickable(false);
        btnMyPrize.setClickable(false);
        isRotating = true;
        scrollView.scrollTo(0, 0);
        Random random = new Random();
        number = random.nextInt((8)) + 1;
        Log.i("chz", "" + number);//2
        runnable = new Runnable() {
            public void run() {
                Random random = new Random();
//                                                    int num = random.nextInt((8));
//                                                    Log.i("chz", "" + (num + 3));//9
                Log.i("chz", "" + (prizeId + 3));//9
//                                                    while (number < 60 + num) {
                while (number < 60 + prizeId) {
                    Message message = new Message();
                    Log.i("消息源：", "" + number % 8 + "  number:" + number);
                    switch (number % 8) {
                        case 0:
                            message.what = MESSAGE_8;
                            break;
                        case 1:
                            message.what = MESSAGE_1;
                            break;
                        case 2:
                            message.what = MESSAGE_2;
                            break;
                        case 3:
                            message.what = MESSAGE_3;
                            break;
                        case 4:
                            message.what = MESSAGE_4;
                            break;
                        case 5:
                            message.what = MESSAGE_5;
                            break;
                        case 6:
                            message.what = MESSAGE_6;
                            break;
                        case 7:
                            message.what = MESSAGE_7;
                            break;
                        case 8:
                            message.what = MESSAGE_8;
                            break;
                        default:
                            break;
                    }
                    number++;//4
                    mHandler.sendMessage(message);
                    Log.i("发送的消息：", "" + message.what);
                    if (number < 50) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (number > 50 && number < 60) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                Message message = new Message();
                message.what = MESSAGE_STOP;
                Bundle bundle = new Bundle();
                bundle.putInt("msg", (prizeId + 3));
                bundle.putString("imgUrl", data.image);
                bundle.putSerializable("data", data);
                message.setData(bundle);
                mHandler.sendMessage(message);
                isRotating = false;
            }
        };
        new Thread(runnable).start();
    }


    /**
     * 获取奖品数据，并把奖品图片设置到对应的ImageView上面
     */
    private void getPrizeList() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getWheelOfFortunePrizeList(new Callback<WheelOfFortunePrizeListEntity>() {
                    @Override
                    public void onResponse(Call<WheelOfFortunePrizeListEntity> call, Response<WheelOfFortunePrizeListEntity> response) {
                        stopMyDialog();
                        WheelOfFortunePrizeListEntity body = response.body();
                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                            showToast(R.string.service_exception);
                            return;
                        }
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, LeFenPhoneLoginActivity.class)) {
                            switch (body.code) {
                                case 1:
                                    version = body.version;
                                    tvCost.setText(body.data.desc);
                                    ivNetworkError.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);
                                    WheelOfFortunePrizeListEntity.DataBean data = body.data;
                                    //奖品列表，应该是8个奖品
                                    prizeList = data.list;
                                    if (prizeList.size() != 8) {
                                        showToast("奖品异常");
                                        return;
                                    }
                                    setPrizeImage();

                                    listviewRule.setAdapter(new DefaultAdapter<String>(mContext, body.data.rules, R.layout.item_wheel_rule) {
                                        @Override
                                        public void convert(ViewHolder helper, String item) {
                                            helper.setText(R.id.tv_rule, item);
                                        }
                                    });
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WheelOfFortunePrizeListEntity> call, Throwable t) {
                        ivNetworkError.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                        showToast(R.string.net_work_not_available);
                        stopMyDialog();
                    }
                });
    }

    private void setPrizeImage() {
        //                                    设置奖品图片
        for (int i = 0; i < prizeImageViewList.size(); i++) {
            ImageView imageView = prizeImageViewList.get(i);
            GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(prizeList.get(i).image), imageView);
        }

    }

    public void refreshData(View view) {
        initData();
    }

    class WheelOfFortuneAwardListAdapter extends BaseAdapter {
        private final ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean> list;

        public WheelOfFortuneAwardListAdapter(ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position % list.size());
        }

        @Override
        public long getItemId(int position) {
            return position % list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wheel_of_fortune_award_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            listviewPrizeList.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertView.getHeight() * 4));
            WheelOfFortuneAwardListEntity.DataBean.ListBean listBean = list.get(position % list.size());
            viewHolder.tvWinnerName.setText(TextUtils.isEmpty(listBean.winnerName) ? "暂无昵称" : listBean.winnerName);
            viewHolder.tvWinnerGood.setText(listBean.goodName);
            viewHolder.tvWinnerTime.setText(DateUtils.timeStampToStr(NumberFormat.convertToLong(listBean.winningTime, 0L)));
            if (position % 2 == 0) {
                viewHolder.llWheelOfFortuneAwardList.setBackgroundColor(Color.parseColor("#e21c3e"));
            } else {
                viewHolder.llWheelOfFortuneAwardList.setBackgroundColor(Color.parseColor("#fc3658"));
            }
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public TextView tvWinnerName;
            public TextView tvWinnerGood;
            public TextView tvWinnerTime;
            public LinearLayout llWheelOfFortuneAwardList;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tvWinnerName = (TextView) rootView.findViewById(R.id.tv_winner_name);
                this.tvWinnerGood = (TextView) rootView.findViewById(R.id.tv_winner_good);
                this.tvWinnerTime = (TextView) rootView.findViewById(R.id.tv_winner_time);
                this.llWheelOfFortuneAwardList = (LinearLayout) rootView.findViewById(R.id.ll_wheel_of_fortune_award_list);
            }

        }
    }


    private void showToast() {
        showToast("正在抽奖,请稍等");
    }
}
