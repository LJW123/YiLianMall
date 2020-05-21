package com.leshan.ylyj.view.activity.Integraltreasuremodel;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.presenter.IntegralTreasurePresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.CountNumberView;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.mylibrary.widget.MyBigDecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.IntegralTreasureEntity;


/**
 * 集分宝
 */
public class IntegralTreasureActivity extends BaseActivity implements View.OnClickListener {

    //标题栏
    private LinearLayout back_ll, detail_ll;//返回,明细
    private TextView toolbar_title;//标题
    //奖券总额
    private CountNumberView integral_total_tv;
    //累计兑换,兑换指数,昨日兑换
    private LinearLayout exchange_total_ll, exchange_exponent;
    private TextView exchange_total_tv, exchange_exponent_tv, exchange_yesterday_tv;
    //转入 转出
    private TextView shift_to_tv, roll_out_tv;

    //立即开启（开启自动转入）
    private LinearLayout auto_open;
    private TextView auto_open_tv;
    //是否自动转入状态
    private TextView auto_name_tv, auto_info_tv;

    private IntegralTreasurePresenter mPresent;

    private IntegralTreasureEntity.DataBean mData;//奖券宝首页数据
    /**
     * 是否成功获取网络数据
     */
    private Boolean getNetSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_treasure);
        StatusBarUtil.setColor(this, Color.parseColor("#FF532A"));
        initView();
        initListener();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();

        getNetSuccess = false;
        startMyDialog(false);
        //绑定model网络请求所要回调的view控件
        if (mPresent == null)
            mPresent = new IntegralTreasurePresenter(mContext, this);
        //执行网络请求发法
        mPresent.getIntegralTreasureData();
    }

    @Override
    protected void initView() {
        back_ll = findViewById(R.id.back_ll);
        detail_ll = findViewById(R.id.detail_ll);
        toolbar_title = findViewById(R.id.toolbar_title);

        integral_total_tv = findViewById(R.id.integral_total_tv);//奖券总额

        exchange_total_ll = findViewById(R.id.exchange_total_ll); //累计兑换
        exchange_exponent = findViewById(R.id.exchange_exponent); //兑换指数
        exchange_total_tv = findViewById(R.id.exchange_total_tv); //累计兑换
        exchange_exponent_tv = findViewById(R.id.exchange_exponent_tv); //兑换指数
        exchange_yesterday_tv = findViewById(R.id.exchange_yesterday_tv); //昨日兑换

        shift_to_tv = findViewById(R.id.shift_to_tv); //转入
        roll_out_tv = findViewById(R.id.roll_out_tv); //转出

        auto_open = findViewById(R.id.auto_open); //立即开启（开启自动转入）
        auto_open_tv = findViewById(R.id.auto_open_tv); //立即开启（开启自动转入）
        auto_name_tv = findViewById(R.id.auto_name_tv);
        auto_info_tv = findViewById(R.id.auto_info_tv);
    }

    @Override
    protected void initListener() {
        back_ll.setOnClickListener(this);//返回键
        detail_ll.setOnClickListener(this);//明细

        exchange_total_ll.setOnClickListener(this);//累计兑换
        exchange_exponent.setOnClickListener(this);//兑换指数

        shift_to_tv.setOnClickListener(this);//转入
        roll_out_tv.setOnClickListener(this);//转出

        auto_open.setOnClickListener(this);//立即开启（开启自动转入）
    }

    @Override
    protected void initData() {
        toolbar_title.setText("集分宝");
    }

    protected void setData() {
        float a = (float) MyBigDecimal.div(Double.parseDouble(mData.getJfbIntegral()), 100, 2);
        integral_total_tv.showNumberWithAnimation(a, CountNumberView.FLOATREGEX);
//        integral_total_tv.showNumberWithAnimation(new BigDecimal(mData.getJfbIntegral()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).floatValue(), CountNumberView.FLOATREGEX);//集分宝奖券余额

        if (TextUtils.isEmpty(mData.getAllExchange()) || Double.valueOf(mData.getAllExchange()) == 0) {
            exchange_total_tv.setText("- - -");
        } else {
            exchange_total_tv.setText(MoneyUtil.getTwoDecimalPlaces(mData.getAllExchange()));
        }
        if (TextUtils.isEmpty(mData.getExchangeIndex())) {
            exchange_exponent_tv.setText("- - -");
        } else {
            exchange_exponent_tv.setText(new BigDecimal(mData.getExchangeIndex()).divide(BigDecimal.valueOf(10000), 4, RoundingMode.HALF_UP) + "");
        }
        if (TextUtils.isEmpty(mData.getYesterdayExchange()) || Double.valueOf(mData.getYesterdayExchange()) == 0) {
            exchange_yesterday_tv.setText("- - -");
        } else {
            exchange_yesterday_tv.setText(MoneyUtil.getTwoDecimalPlaces(mData.getYesterdayExchange()));
        }


        if (mData.getJfbAutoTransfer().equals("1")) {// 0未开启  1开启自动转入
            if (TextUtils.isEmpty(mData.getJfbAutoTime())) {//每日转入时间
                auto_info_tv.setText("");
            } else {
                auto_info_tv.setText("次日" + mData.getJfbAutoTime() + "从奖券余额自动转入集分宝");
            }
            auto_open_tv.setText("编辑自动转入");
        } else {
            auto_info_tv.setText("省时省心，多点时间享受美好生活");
            auto_open_tv.setText("立即开启");
        }
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (!getNetSuccess && i != R.id.back_ll) {
            startMyDialog(false);
            //绑定model网络请求所要回调的view控件
            if (mPresent == null)
                mPresent = new IntegralTreasurePresenter(mContext, this);
            //执行网络请求发法
            mPresent.getIntegralTreasureData();
            return;
        }
        if (i == R.id.back_ll) {//返回
            finish();
        } else if (i == R.id.detail_ll) {//明细
            SkipUtils.toIntegralRecord(this, 0);
        } else if (i == R.id.exchange_total_ll) {//累计兑换
            SkipUtils.toIntegralRecord(this, 1);
        } else if (i == R.id.exchange_exponent) {//兑换指数
            SkipUtils.toExchangeIndex(this, mData.getExchangeIndex());
        } else if (i == R.id.shift_to_tv) {//转入
            SkipUtils.toShiftTo(this, mData.getIntegral(), mData.getLimitExplainShow(), mData.getLimitExplainUrl(), mData.getPlanGiveRed(), mData.getIsTransferLimit(), mData.getJfbTransferLimit(), mData.getRemainJfbIntegral());
        } else if (i == R.id.roll_out_tv) {//转出
            SkipUtils.toTurnOut(this, mData.getJfbIntegral());
        } else if (i == R.id.auto_open) {
            // 0未开启  1开启自动转入
            if (mData.getJfbAutoTransfer().equals("1")) {//编辑自动转入
                BigDecimal integral = new BigDecimal(mData.getJfbHoldIntegral());
                integral = integral.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                SkipUtils.toSetIntegraNum(this, mData.getJfbAutoTime(), integral + "", mData.getLimitExplainShow(), mData.getLimitExplainUrl(), mData.getIsTransferLimit(), mData.getJfbTransferLimit());
            } else {//开启自动转入
                SkipUtils.toSetRetainedIntegra(this, 0, mData.getJfbAutoTime(), mData.getLimitExplainShow(), mData.getLimitExplainUrl(), mData.getIsTransferLimit(), mData.getJfbTransferLimit());
            }
        }
    }


    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
    }

    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(mContext, e.getMessage());
        stopMyDialog();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("获取数据结果");
        if (baseEntity instanceof rxfamily.entity.IntegralTreasureEntity) {
            mData = ((IntegralTreasureEntity) baseEntity).data;
            getNetSuccess = true;
            setData();
            Logger.i("获取数据成功");
        } else {
            Logger.i("获取数据失败");
        }
    }
}
