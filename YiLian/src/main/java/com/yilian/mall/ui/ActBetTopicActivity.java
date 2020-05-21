package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author
 *         参与押注话题界面
 */
public class ActBetTopicActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private TextView tvTitle,tvNoticeStakeNum,tvNoticeStakeIntegral;
    private TextView tvSide;
    private Button btnDecrease;
    private EditText etNumber;
    private Button btnIncrease;
    private TextView tvTotalIntegral;
    private TextView tvUserIntegral;
    private Button btnBet;
    private String side;//正反方
    private int redCount;
    private int blueCount;
    /**
     * 每次押注上限
     */
    private int betLimit;
    private String topicId;
    /**
     * 单注消耗奖券
     */
    private int betConsume;
    private double userIntegral;
    private boolean isCanStake=false;
    private boolean isSuccess=false;
    private boolean hasBet=false;
    private String topicTitle;
    private int kaiTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_act_bet_topic);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        kaiTime=getIntent().getIntExtra("kaiTime",12);
        topicTitle=getIntent().getStringExtra("topicTitle");
        side = getIntent().getStringExtra("side");
        betLimit = getIntent().getIntExtra("betLimit", 0);
        topicId = getIntent().getStringExtra("topicId");
        betConsume = getIntent().getIntExtra("betConsume", 0);
        tvTotalIntegral.setText(String.valueOf(betConsume));
        tvNoticeStakeNum.setText("每一方最多押注"+betLimit+"注");
        switch (side) {
            case "0":
                tvSide.setTextColor(ContextCompat.getColor(this, R.color.color_red));
                tvSide.setText("押红方(注)");
                btnBet.setBackgroundColor(ContextCompat.getColor(this, R.color.color_red));
                break;
            case "1":
                tvSide.setTextColor(Color.parseColor("#5481FF"));
                tvSide.setText("押蓝方(注)");
                btnBet.setBackgroundColor(Color.parseColor("#5481FF"));
                tvNoticeStakeNum.setTextColor(Color.parseColor("#5481FF"));
                tvNoticeStakeIntegral.setTextColor(Color.parseColor("#5481FF"));
                break;
            default:
                break;
        }
        getUserIntegral();
    }

    private void initView() {
        tvNoticeStakeNum= (TextView) findViewById(R.id.tv_notice_stake_num);
        tvNoticeStakeIntegral= (TextView) findViewById(R.id.tv_notice_stake_integral);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSide = (TextView) findViewById(R.id.tv_side);
        btnDecrease = (Button) findViewById(R.id.btn_decrease);
        etNumber = (EditText) findViewById(R.id.et_number);
        etNumber.setSelection(etNumber.getText().toString().length());
        etNumber.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        btnIncrease = (Button) findViewById(R.id.btn_increase);
        tvTotalIntegral = (TextView) findViewById(R.id.tv_total_integral);
        tvUserIntegral = (TextView) findViewById(R.id.tv_user_integral);
        btnBet = (Button) findViewById(R.id.btn_bet);

        tvTitle.setOnClickListener(this);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        btnBet.setOnClickListener(this);
    }

    private void initListener() {
        etNumber.addTextChangedListener(new TextWatcher() {

            private int oldValue;
            private String newValue;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                setNeedIntegrals(s.toString());
            }
        });
    }
    private void setNeedIntegrals(String count){
        //计算需要的奖券数量
        int stakeCounts=NumberFormat.convertToInt(count.toString(), 0);
        double totalIntegral = stakeCounts* betConsume;
        tvTotalIntegral.setText(formatIntegrals(totalIntegral+""));
        //剩余的奖券
        double overIntegral=userIntegral-totalIntegral;
        tvUserIntegral.setText(formatIntegrals(overIntegral+""));
        if (overIntegral<=0){
            //余额不足
            tvNoticeStakeIntegral.setVisibility(View.VISIBLE);
            isCanStake=false;
        }else {
            tvNoticeStakeIntegral.setVisibility(View.GONE);
            isCanStake=true;
        }
        if (stakeCounts==betLimit){
            //大于押注限制
            tvNoticeStakeNum.setVisibility(View.VISIBLE);
        }else {
            tvNoticeStakeNum.setVisibility(View.GONE);
        }
        checkIsStake();
    }
    //检查是否能够进行押注
    private void checkIsStake(){
        if (isCanStake){
            switch (side){
                case "0":
                    //红色可以提交按钮
                    btnBet.setBackgroundColor(getResources().getColor(R.color.red));
                    break;
                case "1":
                    //蓝色可以提交按钮
                    btnBet.setBackgroundColor(Color.parseColor("#5481FF"));
                    break;
            }
        }else {
            btnBet.setBackgroundColor(getResources().getColor(R.color.ccccccc));
        }
    }


    void getUserIntegral() {
        startMyDialog();
        RetrofitUtils.getInstance(this).getMyBalance(new Callback<MyBalanceEntity2>() {
            @Override
            public void onResponse(Call<MyBalanceEntity2> call, Response<MyBalanceEntity2> response) {
                stopMyDialog();
                MyBalanceEntity2 body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(ActBetTopicActivity.this, body)) {
                    if (CommonUtils.serivceReturnCode(ActBetTopicActivity.this, body.code, body.msg)) {
                        String userInregrals = MoneyUtil.getLeXiangBiNoZero(NumberFormat.convertToDouble(body.integral, 0d));
                        userIntegral = NumberFormat.convertToDouble(userInregrals, 0d);
                        double overIntegrals = userIntegral - betConsume;
                        tvUserIntegral.setText(formatIntegrals(overIntegrals + ""));
                        isSuccess = true;
                        if (overIntegrals <= 0) {
                            //余额不足
                            tvNoticeStakeIntegral.setVisibility(View.VISIBLE);
                            isCanStake = false;
                        } else {
                            tvNoticeStakeIntegral.setVisibility(View.GONE);
                            isCanStake = true;
                        }
                        checkIsStake();
                    }
            }
            }

            @Override
            public void onFailure(Call<MyBalanceEntity2> call, Throwable t) {
                stopMyDialog();

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_title){
            finish();
        }else {
            if (!isSuccess) {
                return;
            }
            switch (v.getId()) {
                case R.id.btn_decrease:
                    int newNumber = NumberFormat.convertToInt(etNumber.getText().toString(), 0) -1;
                    if (newNumber < 1) {
                        //数量矫正，输入数量不能小于1
                        newNumber = 1;
                    }
                    Logger.i("点击了减号：" + newNumber);
                    etNumber.setText(String.valueOf(newNumber));
                    break;
                case R.id.btn_increase:
                    int newNumber2 = NumberFormat.convertToInt(etNumber.getText().toString(), 0) + 1;
                    if (newNumber2>betLimit){
                        newNumber2=betLimit;
                    }
                    etNumber.setText(String.valueOf(newNumber2));
                    break;
                case R.id.btn_bet:
                    if (isCanStake){
                        submit();
                    }
                    break;
                default:
                    break;
            }

        }

    }

    private void submit() {
        // validate
        String number = etNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "押注数量不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NumberFormat.convertToInt(tvTotalIntegral.getText().toString(), 0) > userIntegral) {
            Toast.makeText(this, "奖券不足", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO validate success, do something
        betTopic();
    }

    @SuppressWarnings("unchecked")
    private void betTopic() {
        startMyDialog();
        switch (side) {
            case "0":
                redCount = NumberFormat.convertToInt(etNumber.getText().toString(), 0);
                break;
            case "1":
                blueCount = NumberFormat.convertToInt(etNumber.getText().toString(), 0);
                break;
            default:
                break;
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(this).betTopic("do_mortgage_topic", topicId, redCount, blueCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                        finish();
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
                        if (httpResultBean.code!=1){
                            finish();
                            return;
                        }
                        //押注成功
                        hasBet=true;
                        if (hasBet){
                            Intent intent1=new Intent();
                            intent1.putExtra("num",etNumber.getText().toString().trim());
                            setResult(1,intent1);
                        }
                        stopMyDialog();
                        Intent intent=new Intent(mContext,ActBetTopicSuccessActivity.class);
                        intent.putExtra("side",side);
                        intent.putExtra("kaiTime",kaiTime);
                        intent.putExtra("count",etNumber.getText().toString().trim());
                        intent.putExtra("topicTitle",topicTitle);
                        startActivity(intent);
                        finish();
                    }
                });
        addSubscription(subscription);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        //加入押注成功的话通知签到页面刷新数据
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    /**
     * 格式化数字--截取后两位小数
     * 负数计算得到
     * @param integral
     * @return
     */
    private String formatIntegrals(String integral) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        if (TextUtils.isEmpty(integral)) {
            return "0.00";
        } else {
            if (integral.contains("-")){

            }else {
                if (integral.contains(".")) {
                    int index = integral.indexOf(".");
                    if (index == 0) {//首位
                        integral = 0 + integral + 0;
                    } else {
                        if (index == integral.length() - 1) {//末尾
                            integral = integral + 0;
                        }
                    }
                }

            }

            BigDecimal bd = new BigDecimal(integral);
            integral = df.format(bd.setScale(2, BigDecimal.ROUND_DOWN));//截取末尾两位小数而不四舍5入
            return integral;
        }
    }


}
