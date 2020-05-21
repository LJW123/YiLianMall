package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.JPGoodsAdapter;
import com.yilian.mall.adapter.RayViewHolder;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPSignBeforeEntity;
import com.yilian.mall.entity.JPSignGVEntity;
import com.yilian.mall.entity.JPSignInEntity;
import com.yilian.mall.entity.JPTurnTableEntity;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.widgets.HorizontalListView;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mall.widgets.RadioGroup;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import static com.yilian.mall.R.id.imgBtn_share;
import static com.yilian.mall.R.id.tv_sign_day;
import static com.yilian.mall.R.id.txt_score_expend;


/**
 * Created by Ray_L_Pain on 2016/11/3 0003.
 */

public class JPSignActivity extends BaseActivity implements View.OnClickListener {
    private static final long ONE_WHEEL_TIME = 300; //指针转一圈所需要的时间，现设置为300毫秒
    /**
     * 签到
     */
    @ViewInject(R.id.top_layout)
    LinearLayout topLayout;
    //标题
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    //返回
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    //签到规则
    @ViewInject(R.id.v3Right)
    TextView tvRight;
    //签到按钮
    @ViewInject(R.id.tv_sign_button)
    TextView tvSign;
    //签到姓名
    @ViewInject(R.id.tv_sign_name)
    TextView tvName;
    //签到登陆按钮
    @ViewInject(R.id.tv_sign_login)
    TextView tvLogin;
    //登陆后的会员等级
    @ViewInject(R.id.tv_login_in)
    TextView tvLv;
    //签到天数
    @ViewInject(tv_sign_day)
    TextView tvDay;
    //签到券
    @ViewInject(R.id.tv_sign_juan)
    TextView tvJuan;
    //layout  未登录时隐藏
    @ViewInject(R.id.layout)
    LinearLayout layout;
    //水平listview
    @ViewInject(R.id.hListView)
    HorizontalListView listView;
    JPSignActivity.SignHozlvAdapter adapter;
    JPNetRequest jpNetRequest;
    JPSignBeforeEntity.DataBean signBeforeEntity;
    String cycle_count="0", cycle_juan="0", new_cycle_juan="0";
    int turntable_count; //大转盘次数
    List<Integer> prizeQuanList;//周一到周日签到获取的抵扣券集合
    /**
     * 大转盘
     */
    //转盘指针
    @ViewInject(R.id.imgView_dial_pointer)
    ImageView ivPointBtn;
    //免费抽奖的tv
    @ViewInject(R.id.txt_count)
    TextView tvPointFree;
    //每次消耗奖券数
    @ViewInject(txt_score_expend)
    TextView tvPointNum;
    /**
     * 为您推荐
     */
    @ViewInject(R.id.ll_recommend)
    LinearLayout recommendLayout;
    @ViewInject(R.id.girdView)
    NoScrollGridView girdView;


    @ViewInject(R.id.tv_sing_days)
    TextView singDays;

    private ActivityNetRequest activityNetRequest;
    private MediaPlayer turnSoundPlayer;
    private JPTurnTableEntity prizeEntity;
    private int turnTableType; //0首次玩 1直接扣币玩游戏
    private int[] laps = {5, 7, 10, 15}; //指针转圈圈数数据源
    // 外维数组的顺序0-5分别代表指针当前的位置(1-6等奖)，内维数组位置0-5下的值代表指针在当前位置下转到1-6等奖需要转动的角度
    private int[][] angles = {{0, 180, 300, 120, 60, 240}, {180, 0, 120, 300, 240, 60},
            {60, 240, 0, 180, 120, 300}, {240, 60, 180, 0, 300, 120},
            {300, 120, 240, 60, 0, 180}, {120, 300, 60, 240, 180, 0}};
    private int startDegree = 0; //开始转动时候的角度，初始值为0
    private int mPointerPosition = 0; //当前指针的位置(0-5),初始位置是0
    private AlertDialog prizeResultDialog; //抽奖结果的Dialog
    //分享
    private Animation animBottom;
    private ArrayList<JPGoodsEntity> list;
    private JPGoodsAdapter jpAdapter;
    //每次转盘获取的券
    int turnScore;
    /**
     * 监听动画状态的监听器
     */
    private Animation.AnimationListener al = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            playTurnSound();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            pauseTurnSound();
            //showResultDialog();
            showV3ResultDialog();
            ivPointBtn.setClickable(true);
        }
    };

    private int is_sign;

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //返回
            case R.id.v3Back:
                JPSignActivity.this.finish();
                break;
            //规则
            case R.id.v3Right:
                intent = new Intent(JPSignActivity.this, WebViewActivity.class);
                intent.putExtra("url" , Constants.signRule);
                startActivity(intent);
                break;
            //签到按钮
            case R.id.tv_sign_button:
                if (!isLogin()) {
                    showToast("您尚未登录");
                }
                signIn();
                break;
            //未登录时的登陆按钮
            case R.id.tv_sign_login:
                intent = new Intent(JPSignActivity.this, LeFenPhoneLoginActivity.class);
                startActivity(intent);
                break;
            //抽奖按钮
            case R.id.imgView_dial_pointer:
                if (isLogin()) {
                    if (is_sign == 0) {
                        showToast("请签到后再抽奖");
                    } else {
                        if (turnTableType == 1 && turntable_count == 1) {
                            showDialog("是否抽奖？", "每次抽奖需消耗10"+getResources().getString(R.string.dikouquan), null, 0, Gravity.CENTER, "是",
                                    "否", false, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    dialog.dismiss();
                                                    ivPointBtn.setClickable(false);
                                                    getPrizeResult(turnTableType);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    dialog.dismiss();
                                                    break;
                                            }
                                        }
                                    }, mContext);
                        } else {
                            ivPointBtn.setClickable(false);
                            getPrizeResult(turnTableType);
                        }
                    }
                } else {
                    startActivity(new Intent(JPSignActivity.this, LeFenPhoneLoginActivity.class));
                }
                break;
            //抽奖结果里的查看详情btn
            case R.id.imgBtn_option:
                intent = new Intent(JPSignActivity.this, V3MoneyActivity.class);
                intent.putExtra("type", V3MoneyActivity.TYPE_INTEGRAL);
                startActivity(intent);
                hideResultDialog();
                break;
            //抽奖结果里的分享btn
            case imgBtn_share:
                animBottom = AnimationUtils.loadAnimation(JPSignActivity.this, R.anim.anim_wellcome_bottom);
                UmengDialog dialog1 = new UmengDialog(mContext, animBottom, R.style.DialogControl,
                        new UmengGloble().getAllIconModels());
                dialog1.setItemLister(new UmengDialog.OnListItemClickListener() {

                    @Override
                    public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                        String content = "我在益联益家大转盘活动中抽中了" + prizeEntity.result + ",你也赶快来试试吧…好运天天有，精美好礼，等你来拿";
                        new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, Ip.SHARE_URL)
                                .share();
                    }
                });
                dialog1.show();
                break;
            //抽奖结果里的关闭btn
            case R.id.imgView_close_lose:
                hideResultDialog();
                if (turntable_count < 2) {
                    signBefore();
                }
                break;
            case R.id.ll_result:
                hideResultDialog();
                if (turntable_count < 2) {
                    signBefore();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_jp);
        ViewUtils.inject(this);

        initView();
        initCircle();
        initGirdView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initGirdView();
    }

    @Override
    protected void onPause() {
        pauseTurnSound();
        super.onPause();
    }

    /**
     * 初始化签到
     */
    private void initView() {

        singDays.setText("连续签到7天收获更多"+getResources().getString(R.string.dikouquan));


        tvTitle.setText("每日签到");
        ivBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvSign.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
//        if (!isLogin()) {
//            layout.setVisibility(View.GONE);
//            tvSign.setText("点击\n签到");
//            tvName.setText("您尚未登录");
//            tvLogin.setVisibility(View.VISIBLE);
//            listView.setAdapter(new SignHozlvAdapter(cycle_count));
//            tvPointFree.setVisibility(View.GONE);
//            tvPointNum.setText("0");
//            topLayout.setVisibility(View.VISIBLE);
//        } else {
//            layout.setVisibility(View.VISIBLE);
//            signBefore();
//        }
        signBefore();
    }

    /**
     * 签到之前，检测是否签到,起到刷新页面作用
     */
    private void signBefore() {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        jpNetRequest.signBefore("0", new RequestCallBack<JPSignBeforeEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<JPSignBeforeEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        layout.setVisibility(View.VISIBLE);
                        signBeforeEntity = responseInfo.result.data;
                        is_sign = signBeforeEntity.isSign;
                        String cycleCount = signBeforeEntity.cycleCount;
                        if (null ==cycleCount){
                            cycleCount="0";
                        }
                        cycle_count =cycleCount ; //签到天数
                        cycle_juan = String.valueOf(Integer.parseInt(signBeforeEntity.cycleCoupons) / 100); //签到获得抵扣券
                        Logger.i("cycle_juan:   "+cycle_juan+"cycle_count  "+cycle_count);
                        if (signBeforeEntity.name.length() == 0) {
                            tvName.setText("暂无昵称");
                        } else if (signBeforeEntity.name.length() < 6) {
                            tvName.setText(signBeforeEntity.name);
                        } else {
                            tvName.setText(signBeforeEntity.name.substring(0,5) + "...");
                        }
                        String level = "";
                        switch (signBeforeEntity.level) {
                            case 1:
                                level = "普通会员";
                                break;
                            case 2:
                                level = "vip会员";
                                break;
                            case 3:
                                level = "至尊会员";
                                break;
                        }
                        tvLogin.setVisibility(View.GONE);
                        tvLv.setVisibility(View.VISIBLE);
                        tvLv.setText(level);
                        prizeQuanList = signBeforeEntity.prizeMoney;
                        listView.setAdapter(new SignHozlvAdapter(cycle_count, prizeQuanList));
                        tvDay.setText(cycle_count);
                        tvJuan.setText(cycle_juan + " "+getResources().getString(R.string.dikouquan));
                        switch (is_sign) {
                            case 0:
                                tvSign.setText("点击\n签到");
                                tvSign.setClickable(true);
                                ivPointBtn.setClickable(true);
                                break;
                            case 1:
                                tvSign.setText("已签到");
                                tvSign.setClickable(false);
                                ivPointBtn.setClickable(true);

                                turntable_count = Integer.parseInt(signBeforeEntity.dailyTurntableCount);
                                if (turntable_count < 1) {
                                    tvPointFree.setVisibility(View.VISIBLE);
                                    turnTableType = 0;
                                } else {
                                    tvPointFree.setVisibility(View.GONE);
                                    turnTableType = 1;
                                }
                                break;
                        }
                        //转盘次数
                        tvPointNum.setText(turntable_count+"");
                        topLayout.setVisibility(View.VISIBLE);
                        break;
                    case -4:
                    case -23:
                        layout.setVisibility(View.GONE);
                        tvSign.setText("点击\n签到");
                        tvName.setText("您尚未登录");
                        tvLogin.setVisibility(View.VISIBLE);
                        prizeQuanList = responseInfo.result.sendMoney;
                        listView.setAdapter(new SignHozlvAdapter(cycle_count, prizeQuanList));
                        tvPointFree.setVisibility(View.GONE);
                        tvPointNum.setText("0");
                        topLayout.setVisibility(View.VISIBLE);
                        break;

                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                tvLogin.setVisibility(View.GONE);
                tvName.setText("用户名");
                tvDay.setText("0");
                tvJuan.setText("0");
                listView.setVisibility(View.INVISIBLE);
                tvPointNum.setText("0");
                topLayout.setVisibility(View.VISIBLE);
                showToast(R.string.net_work_not_available);
                stopMyDialog();
            }
        });
    }

    /**
     * 签到
     */
    private void signIn() {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        jpNetRequest.signIn("1", new RequestCallBack<JPSignInEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<JPSignInEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        JPSignInEntity.DataBean data = responseInfo.result.data;
                        new_cycle_juan = data.luckMoney / 100 + "";
//                        tvDay.setText(cycle_count);
//                        tvJuan.setText((Integer.parseInt(cycle_juan)+Integer.parseInt(new_cycle_juan))+" 抵扣券");
//                        listView.setAdapter(new SignHozlvAdapter(cycle_count));
                        signBefore();
                        showDialog("签到成功", "您有一次免费的抽奖机会", "是否现在抽奖？", 0, Gravity.CENTER, "是",
                                "否", false, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                dialog.dismiss();
                                                ivPointBtn.setClickable(false);
                                                getPrizeResult(0);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                dialog.dismiss();
                                                //initView();
                                                //signBefore();
                                                break;
                                        }
                                    }
                                }, mContext);
                        break;
                    case -3:
                        showToast(R.string.system_busy);
                        break;
                    case -72:
                        showToast(R.string.already_sign);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("签到失败");
            }
        });
    }

    /**
     * 初始化大转盘
     */
    private void initCircle() {
        ivPointBtn.setOnClickListener(this);
        turnSoundPlayer = MediaPlayer.create(this, R.raw.turntable);
        turnSoundPlayer.setLooping(true);
    }

    boolean isCancle = false;

    /**
     * 获取参加转盘抽奖的结果
     */
    private void getPrizeResult(final int turn_count) {
        if (activityNetRequest == null) {
            activityNetRequest = new ActivityNetRequest(mContext);
        }
        activityNetRequest.signTurnTable(turn_count + "", new RequestCallBack<JPTurnTableEntity>() {
            @Override
            public void onStart() {
                startMyDialog(isCancle);
            }

            @Override
            public void onSuccess(ResponseInfo<JPTurnTableEntity> responseInfo) {
                JPTurnTableEntity entity = responseInfo.result;
                if (entity == null) {
                    return;
                }

                float xianjinquan = Float.parseFloat(sp.getString("coupon", "0.00")) / 100;
                Logger.i("xianjinquan:"+xianjinquan);
                turnScore = prizeToJuan(entity.prize);
                if (turn_count == 0){
                    xianjinquan = xianjinquan + (float) turnScore + Float.parseFloat(new_cycle_juan);
                    sp.edit().putString("coupon", String.valueOf(Float.parseFloat(String.valueOf(xianjinquan * 100)))).commit();
                } else {
                    xianjinquan = xianjinquan - (float)10 + (float)turnScore;
                    sp.edit().putString("coupon", String.valueOf(Float.parseFloat(String.valueOf(xianjinquan * 100)))).commit();
                }
                Logger.i("xianjinquan:"+xianjinquan);

                //转盘次数
                turntable_count ++ ;
                tvPointNum.setText(turntable_count+"");

                prizeEntity = entity;
                switch (entity.code) {
                    case 1:
                        startRotate();
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
                ivPointBtn.setClickable(true);
            }
        });
    }

    /**
     * 根据几等奖来判断获取多少抵扣券
     */
    private int prizeToJuan(int prize) {
        int back = 0;
        if (prize == 1) {
            back = 20;
        }
        if (prize == 2) {
            back = 15;
        }
        if (prize == 3) {
            back = 8;
        }
        if (prize == 4) {
            back = 5;
        }
        if (prize == 5) {
            back = 2;
        }
        if (prize == 0) {
            back = 50;
        }
        return back;
    }

    /**
     * 开始旋转
     */
    public void startRotate() {
        int lap = laps[(int) (Math.random() * 4)];
        int angle = angles[mPointerPosition][prizeEntity.prize];// angles[(int)
        // (Math.random()
        // * 6)];
        // 每次转圈角度增量
        int increaseDegree = lap * 360 + angle;
        // 初始化旋转动画，后面的四个参数是用来设置以自己的中心点为圆心转圈
        RotateAnimation rotateAnimation = new RotateAnimation(startDegree, startDegree + increaseDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        // 将最后的角度赋值给startDegree作为下次转圈的初始角度
        startDegree += increaseDegree;
        // 计算动画播放总时间
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        // 设置动画播放时间
        rotateAnimation.setDuration(time);
        // 设置动画播放完后，停留在最后一帧画面上
        rotateAnimation.setFillAfter(true);
        // 设置动画的加速行为，是先加速后减速
//		rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setInterpolator(JPSignActivity.this,
                android.R.anim.accelerate_decelerate_interpolator);
        // 设置动画的监听器
        rotateAnimation.setAnimationListener(al);
        // 开始播放动画
        ivPointBtn.startAnimation(rotateAnimation);
        mPointerPosition = prizeEntity.prize;
    }

    /**
     * v3版抽奖结果Dialog
     */
    private void showV3ResultDialog() {

        View view = getLayoutInflater().inflate(R.layout.dialog_turntable_result_jp, null);
        LinearLayout resultLayout = (LinearLayout) view.findViewById(R.id.ll_result);
        TextView tvResultScore = (TextView) view.findViewById(R.id.tv_result_score);
        TextView tvResultTitle = (TextView) view.findViewById(R.id.tv_result_title);
        TextView tvHint= (TextView) view.findViewById(R.id.tv_hint);
        resultLayout.setOnClickListener(this);
        tvResultScore.setText(turnScore+"");
        tvResultTitle.setText("恭喜您！获得了 "+turnScore+" "+getResources().getString(R.string.dikouquan));
        tvHint.setText("可以在我的"+getResources().getString(R.string.dikouquan)+"中查看获得记录");

        if (prizeResultDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            prizeResultDialog = builder.create();
            prizeResultDialog.setCanceledOnTouchOutside(false);
        }
        prizeResultDialog.show();
        WindowManager.LayoutParams params = prizeResultDialog.getWindow().getAttributes();
        params.width = RadioGroup.LayoutParams.MATCH_PARENT;
        params.height = RadioGroup.LayoutParams.MATCH_PARENT;
        prizeResultDialog.getWindow().setAttributes(params);
        Window window = prizeResultDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setContentView(view);
    }

    private void hideResultDialog() {
        if (prizeResultDialog != null && prizeResultDialog.isShowing()) {
            prizeResultDialog.dismiss();
        }
    }

    public void playTurnSound() {
        if (turnSoundPlayer == null) {
            return;
        }
        turnSoundPlayer.start();
    }

    public void pauseTurnSound() {
        if (turnSoundPlayer == null) {
            return;
        }
        if (turnSoundPlayer.isPlaying()) {
            turnSoundPlayer.pause();
        }
    }

    /**
     * 初始化为您推荐
     */
    private void initGirdView() {
        girdView.setFocusable(false);
        girdView.setFocusableInTouchMode(false);
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        jpNetRequest.signGridView(new RequestCallBack<JPSignGVEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPSignGVEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        list = responseInfo.result.data.goodsList;
                        jpAdapter = new JPGoodsAdapter(mContext, list);
                        girdView.setAdapter(jpAdapter);
                        girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(JPSignActivity.this, JPNewCommDetailActivity.class);
                                intent.putExtra("goods_id", list.get(position).JPGoodsId);
                                intent.putExtra("tags_name", list.get(position).JPTagsName);
                                startActivity(intent);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (turnSoundPlayer != null) {
            if (turnSoundPlayer.isPlaying()) {
                turnSoundPlayer.stop();
            }
            turnSoundPlayer.release();
            turnSoundPlayer = null;
        }
        super.onDestroy();
    }

    class SignHozlvAdapter extends BaseAdapter {
        String cycle_count;
        List<Integer> prizeQuanList;

        public SignHozlvAdapter(String cycle_count, List<Integer> prizeQuanList) {
            this.cycle_count = cycle_count;
            this.prizeQuanList = prizeQuanList;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_signin_record, null);
            }
            TextView tv_reward = RayViewHolder.get(convertView, R.id.tv_reward);
            ImageView iv_singin_status = RayViewHolder.get(convertView, R.id.iv_singin_status);
            TextView tv_singin_day = RayViewHolder.get(convertView, R.id.tv_singin_day);
            View view_line_left = RayViewHolder.get(convertView, R.id.view_line_left);
            View view_line_right = RayViewHolder.get(convertView, R.id.view_line_right);
            View view1 = RayViewHolder.get(convertView, R.id.view1);
            View view2 = RayViewHolder.get(convertView, R.id.view2);

            switch (position) {
                case 0:
                    tv_reward.setText("" + prizeQuanList.get(0)/100);
                    view_line_left.setVisibility(View.GONE);
                    view1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tv_reward.setText("" + prizeQuanList.get(1)/100);
                    break;
                case 2:
                    tv_reward.setText("" + prizeQuanList.get(2)/100);
                    break;
                case 3:
                    tv_reward.setText("" + prizeQuanList.get(3)/100);
                    break;
                case 4:
                    tv_reward.setText("" + prizeQuanList.get(4)/100);
                    break;
                case 5:
                    tv_reward.setText("" + prizeQuanList.get(5)/100);
                    break;
                case 6:
                    String seven = String.valueOf(prizeQuanList.get(6)/100 + prizeQuanList.get(7)/100);
                    tv_reward.setText("+" + seven);
                    tv_reward.setBackgroundResource(R.drawable.bg_solid_gold_circile);
                    tv_reward.setTextColor(mContext.getResources().getColor(R.color.white));
                    view_line_right.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    break;
            }

            if (isLogin()) {
                switch (cycle_count) {
                    case "1":
                        if (position == 0) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }

                        view_line_left.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 18, 4));
                        view_line_right.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 18, 4));
                        view1.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 18, 4));
                        view2.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 18, 4));
                        break;
                    case "2":
                        if (position == 0) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 1) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }

                        view_line_left.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 19, 4));
                        view_line_right.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 19, 4));
                        view1.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 19, 4));
                        view2.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 19, 4));
                        break;
                    case "3":
                        if (position == 0) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 1) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 2) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }

                        view_line_left.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 20, 4));
                        view_line_right.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 20, 4));
                        view1.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 20, 4));
                        view2.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 20, 4));
                        break;
                    case "4":
                        if (position == 0) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 1) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 2) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 3) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }

                        view_line_left.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 21, 4));
                        view_line_right.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 21, 4));
                        view1.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 21, 4));
                        view2.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 21, 4));
                        break;
                    case "5":
                        if (position == 0) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 1) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 2) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 3) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 4) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }

                        view_line_left.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 21, 4));
                        view_line_right.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 21, 4));
                        view1.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 21, 4));
                        view2.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 21, 4));
                        break;
                    case "6":
                        if (position == 0) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 1) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 2) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 3) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 4) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }
                        if (position == 5) {
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            tv_reward.setTextSize(15);
                            tv_reward.setTextColor(getResources().getColor(R.color.white));
                            iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);
                        }

                        view_line_left.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 22, 4));
                        view_line_right.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 22, 4));
                        view1.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 22, 4));
                        view2.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 22, 4));
                        break;
                    case "7":
                        tv_reward.setTextSize(15);
                        tv_reward.setTextColor(getResources().getColor(R.color.white));
                        iv_singin_status.setBackgroundResource(R.mipmap.jp_no_signed);

                        view_line_left.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 23, 4));
                        view_line_right.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 23, 4));
                        view1.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 23, 4));
                        view2.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 23, 4));
                        break;
                    default:
                        iv_singin_status.setBackgroundResource(R.mipmap.jp_signed);

                        view_line_left.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 17, 4));
                        view_line_right.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 17, 4));
                        view1.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 17, 4));
                        view2.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 17, 4));
                        break;
                }
            } else {
                iv_singin_status.setBackgroundResource(R.mipmap.jp_signed);

                view_line_left.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 17, 4));
                view_line_right.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 17, 4));
                view1.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 17, 4));
                view2.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 17, 4));
            }

            tv_singin_day.setText((position + 1) + "天");

            return convertView;
        }
    }
}
