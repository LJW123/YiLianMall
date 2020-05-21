package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.DailyId;
import com.yilian.mall.entity.DailyTurntable;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.widgets.MyLoading;
import com.yilian.mylibrary.Constants;

/**
 * 大转盘活动详情界面
 */
public class LotteryFreeActivity extends BaseActivity {

    // 设置一个时间常量，此常量有两个作用，1.圆灯视图显示与隐藏中间的切换时间；2.指针转一圈所需要的时间，现设置为300毫秒
    private static final long ONE_WHEEL_TIME = 200;
    boolean flag = false;
    /**
     * 右边title
     */
    @ViewInject(R.id.right_textview)
    TextView mTxtRightTitle;
    /**
     * 返回键
     */
    @ViewInject(R.id.tv_back)
    TextView mTxtBack;
    // 外维数组的顺序0-5分别代表指针当前的位置(1-6等奖)，内维数组位置0-5下的值代表指针在当前位置下转到1-6等奖需要转动的角度
//    private int[][] angles = {{0, 300, 240, 180, 60, 120}, {60, 0, 300, 240, 120, 180},
//            {120, 60, 0, 300, 180, 240}, {180, 120, 60, 0, 240, 300}, {300, 240, 180, 120, 0, 60},
//            {240, 180, 120, 60, 300, 0}};
    /**
     * 转盘指针
     */
    @ViewInject(R.id.imgView_dial_pointer)
    ImageView mImgDailPointer;
    /**
     * 抽奖次数
     */
    @ViewInject(R.id.txt_count)
    TextView mTxtCount;
    int joinCount = 0;
    int i = 0;
    private MyLoading myloading;
    // 开始转动时候的角度，初始值为0
    private int startDegree = 0;
    // 指针转圈圈数数据源
    private int[] laps = {5, 7, 10, 15};
    private int[][] angles = {{0, 300, 240, 180, 60, 120}, {60, 0, 300, 240, 120, 180},
            {120, 60, 0, 300, 180, 240}, {180, 120, 60, 0, 240, 300}, {300, 240, 180, 120, 0, 60},
            {240, 180, 120, 60, 300, 0}};
    private int mPointerPosition = 0;// 当前指针的位置(0-5),初始位置是0
    private AlertDialog mResultDialog;
    private String mActivityIndex;//活动id
    private ActivityNetRequest netRequest;
    private MediaPlayer mTurnSoundPlayer;
    private int prize;
    // 监听动画状态的监听器
    private AnimationListener al = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
            playTurnSound();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            pauseTurnSound();
            showResultDialog();
            mTxtCount.setText("抽奖次数: " + joinCount + " 次");
            mImgDailPointer.setClickable(true);
        }
    };
    private Animation animBottom;
    private int activityState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_free_detail);
        ViewUtils.inject(this);
        init();
        registerEvents();

        mImgDailPointer.setClickable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getActivityInfo();
    }

    private void getActivityInfo() {
        startMyDialog();
        netRequest.getDailyId(new RequestCallBack<DailyId>() {
            @Override
            public void onSuccess(ResponseInfo<DailyId> responseInfo) {
                stopMyDialog();
                activityState = responseInfo.result.code;
                switch (responseInfo.result.code) {
                    case 1:

                        mTxtCount.setText("抽奖次数: " + responseInfo.result.joinCount + " 次");
                        joinCount = responseInfo.result.joinCount;
                        mActivityIndex = responseInfo.result.dailyId;
                        mImgDailPointer.setClickable(true);
                        break;

                    case -54:
                        mImgDailPointer.setClickable(true);
                        showToast("活动未开始");
                        break;

                    default:
                        mImgDailPointer.setClickable(true);
                        break;

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                getActivityInfo();
            }
        });

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        pauseTurnSound();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mTurnSoundPlayer != null) {
            if (mTurnSoundPlayer.isPlaying()) {
                mTurnSoundPlayer.stop();
            }
            mTurnSoundPlayer.release();
            mTurnSoundPlayer = null;
        }
        super.onDestroy();
    }

    public void rightTextview(View v) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("title_name", getString(R.string.activity_rule));
        intent.putExtra("url", Constants.ActivityRules);
        startActivity(intent);
    }

    private void init() {
        netRequest = new ActivityNetRequest(mContext);
        mActivityIndex = getIntent().getStringExtra("activity_index");
//		mActivityName = getIntent().getStringExtra("activity_name");
        mTxtBack.setText("每日大转盘");
        mTxtRightTitle.setText(R.string.activity_rule);
        mTxtCount.setText(Integer.toString(sp.getInt(Constants.SPKEY_TURN_TABLE_COUNT + mActivityIndex, 0)));
        mTurnSoundPlayer = MediaPlayer.create(this, R.raw.turntable);
        mTurnSoundPlayer.setLooping(true);
    }

    private void registerEvents() {
        mImgDailPointer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    if (joinCount != 0){
                        showDialog("温馨提示", "本次抽奖使用您 20 "+getResources().getString(R.string.dikouquan), "",
                                R.mipmap.icon_warning, Gravity.CENTER, "不玩了", "有钱，任性", false,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                dialog.dismiss();

                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                mImgDailPointer.setClickable(false);
                                                getPrizeResult();
                                                dialog.dismiss();
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }, mContext);
                    } else {

                        if (activityState == 1 ){
                            mImgDailPointer.setClickable(false);
                            getPrizeResult();
                        }else {
                            showToast("活动未开始");
                        }


                    }


                } else {
                    showToast(R.string.login_first);
                    startActivity(new Intent(LotteryFreeActivity.this, LeFenPhoneLoginActivity.class));
                }
            }
        });
    }

    /**
     * 获取抽奖结果
     */
    private void getPrizeResult() {

        netRequest.getLotteryFree(mActivityIndex, String.valueOf(joinCount), new RequestCallBack<DailyTurntable>() {

            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onSuccess(ResponseInfo<DailyTurntable> responseInfo) {

                switch (responseInfo.result.code) {
                    case 1:
                        prize = responseInfo.result.prize;
                        startRotate();
                        break;

                    case -13:
                        showToast("账户奖励不足");
                        break;
                    case -54:
                        showToast("活动停止");
                        break;

                    default:
                        mImgDailPointer.setClickable(true);
                        break;
                }
                joinCount = responseInfo.result.joinCount;
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mImgDailPointer.setClickable(true);
            }
        });

//		netRequest.getTurnTableActivityResult(mActivityIndex, TurnTablePrize.class, new RequestCallBack<TurnTablePrize>() {
//			@Override
//			public void onSuccess(ResponseInfo<TurnTablePrize> arg0) {
//				TurnTablePrize prize = arg0.result;
//				if (prize == null) {
//					return;
//				}
//				sp.edit().putString("integral",Integer.toString(prize.integral)).commit();
//				mPrize = prize;
//				switch (prize.code) {
//				case 1:
//					sp.edit().putInt(Constants.SPKEY_TURN_TABLE_COUNT+mActivityIndex, prize.turnTableCount).commit();
//					startRotate();
//					break;
//
//				default:
//					CommonUtils.NetworkRequestReturnCode(aZhuanPanDetailActivity.this, String.valueOf(prize.code));
//					mImgDailPointer.setClickable(true);
//					break;
//				}
//			}
//
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				// TODO Auto-generated method stub
//				log.e(arg0.getExceptionCode() + "--" + arg1);
//				mImgDailPointer.setClickable(true);
//			}
//		});
    }

    /**
     * 开始旋转
     */
    public void startRotate() {
        int lap = laps[(int) (Math.random() * 4)];
        int angle = angles[mPointerPosition][prize];// angles[(int)
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
        rotateAnimation.setInterpolator(LotteryFreeActivity.this,
                android.R.anim.accelerate_decelerate_interpolator);
        // 设置动画的监听器
        rotateAnimation.setAnimationListener(al);
        // 开始播放动画
        mImgDailPointer.startAnimation(rotateAnimation);
        mPointerPosition = prize;
    }

    public void playTurnSound() {
        if (mTurnSoundPlayer == null) {
            return;
        }
        mTurnSoundPlayer.start();
    }

    public void pauseTurnSound() {
        if (mTurnSoundPlayer == null) {
            return;
        }
        if (mTurnSoundPlayer.isPlaying()) {
            mTurnSoundPlayer.pause();
        }
    }

    private void showResultDialog() {

        String prize = "";
        String type = "";
        switch (this.prize) {
            case 0:
                prize="1";
                type = "元";
                break;
            case 1:
                prize="2";
                type = "乐分币";
                break;

            case 2:
                prize="1";
                type = "乐分币";
                break;

            case 3:
                prize="15";
                type = getResources().getString(R.string.dikouquan);
                break;

            case 4:
                prize="8";
                type = getResources().getString(R.string.dikouquan);
                break;

            case 5:
                prize="2";
                type = getResources().getString(R.string.dikouquan);
                break;

        }

        final Dialog dialog = new Dialog(this, R.style.Dialog);
        View view = View.inflate(this, R.layout.dialog_given_jifen, null);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(view);
        TextView tv_huodejifen = (TextView) view.findViewById(R.id.tv_huodejifen);
        tv_huodejifen.setText(prize);
        TextView tv_huodejifen2 = (TextView) view.findViewById(R.id.tv_huodejifen2);
        tv_huodejifen2.setText("恭喜您！获得" + prize + type);
        TextView tvType = (TextView) view.findViewById(R.id.tv_type);
        tvType.setText(type);

        dialog.show();
        view.findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });


    }


    private void hideResultDialog() {
        if (mResultDialog != null && mResultDialog.isShowing()) {
            mResultDialog.dismiss();
        }
    }
}
