package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.entity.ActivityInfo;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.GetActivityInfoResult;
import com.yilian.mall.entity.ShakeResult;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengDialog.OnListItemClickListener;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.widgets.CountDownClock;
import com.yilian.mall.widgets.CountDownClock.CountDownListener;
import com.yilian.mall.widgets.NoticeView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

import java.io.IOException;
import java.util.Date;

public class YaoYiYaoActivity extends BaseActivity {


    private static final int SENSOR_SHAKE = 10;
    @ViewInject(R.id.vf_shake)
    ViewFlipper mVfShake;
    @ViewInject(R.id.txt_go_shake)
    TextView mTxtGoShake;
    @ViewInject(R.id.llayout_count_down)
    View mViewCountDown;
    @ViewInject(R.id.clock_count_down)
    CountDownClock mClock;
    @ViewInject(R.id.llayout_quiet)
    View mViewQuiet;
    @ViewInject(R.id.llayout_floating)
    View mViewFloating;
    @ViewInject(R.id.imgView_up)
    ImageView mImgUp;
    @ViewInject(R.id.imgView_down)
    View mImgDown;
    @ViewInject(R.id.imgView_shake_middle)
    ImageView mImgMiddle;
    @ViewInject(R.id.noticeView)
    NoticeView noticeView;
    @ViewInject(R.id.tv_go_prize_list)
    TextView tvGoPrizeList;
    @ViewInject(R.id.tv_go_prize_list2)
    TextView tvGoPrizeList2;
    String activityIndex;
    @ViewInject(R.id.tv_back)
    private TextView tvBack;
    @ViewInject(R.id.right_textview)
    private TextView right_textview;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private AlertDialog mResultDialog;
    private ShakeResult mResult;
    private BitmapUtils mBitmapUtils;
    private MediaPlayer mPlayer;
    //	private boolean mStart=false;//活动是否开始
    private ActivityInfo mActivityInfo;
    private ActivityNetRequest mActivityNetRequest;
    private Animation animBottom;
    OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.imgView_close_lose:
                    hideShakeResultDialog();
                    break;
                case R.id.imgView_close_win:
                    hideShakeResultDialog();
                    break;
                case R.id.imgBtn_get_prize:

                    hideShakeResultDialog();
                    startActivity(new Intent(YaoYiYaoActivity.this, PrizeVoucherListActivity.class));
                    break;
                case R.id.imgBtn_shake_again:
                    hideShakeResultDialog();
                    break;
                case R.id.imgBtn_see_other:
                    finish();
                    break;
                case R.id.tv_go_prize_list:
                case R.id.tv_go_prize_list2:
                    hideShakeResultDialog();
                    Intent intent = new Intent(YaoYiYaoActivity.this, PrizeListActivity.class);
                    intent.putExtra("activity_index", activityIndex);
                    startActivity(intent);
                    break;
                case R.id.imgBtn_get_share:
                    animBottom = AnimationUtils.loadAnimation(YaoYiYaoActivity.this, R.anim.anim_wellcome_bottom);
                    UmengDialog dialog1 = new UmengDialog(mContext, animBottom, R.style.DialogControl,
                            new UmengGloble().getAllIconModels());
                    dialog1.setItemLister(new OnListItemClickListener() {

                        @Override
                        public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                            String content = "我在益联益家摇一摇活动中抽中了" + mResult.prizeName + ",你也赶快来试试吧…好运天天有，精美好礼，等你来拿";
                            new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, Ip.SHARE_URL)
                                    .share();

                        }
                    });

                    dialog1.show();

                    break;
                default:
                    break;
            }
        }
    };
    private boolean mShakeFlag = true;//摇奖网络请求开关,当前请求返回结果后才能进行下次请求
    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 14;// 如果不敏感请自行调低该数值,低于10的话就不行了,因为z轴上的加速度本身就已经达到10了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    /**
     * 动作执行
     */
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    hideShakeResultDialog();
                    unregisterShakeListener();//检测到摇晃之后不再处理摇晃，动画结束后再处理下次摇晃
                    startShakeAnimation();
                    playShakeSound();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaoyiyao);
        ViewUtils.inject(this);
        init();
        registerEvents();
        getActivityInfo();


    }

    private void init() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        right_textview.setText("活动规则");

        activityIndex = getIntent().getStringExtra("activity_index");
    }

    private void registerEvents() {
        mTxtGoShake.setOnClickListener(mClickListener);
        tvGoPrizeList.setOnClickListener(mClickListener);
        tvGoPrizeList2.setOnClickListener(mClickListener);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mVfShake.getDisplayedChild() == 1) {//如果当前展示的是摇奖界面
            registerShakeListener();
        }
    }

    @Override
    protected void onPause() {
        unregisterShakeListener();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mClock.isRun()) {
            mClock.stopRun();
        }
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();
    }

    public void registerShakeListener() {
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    public void unregisterShakeListener() {
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    public void onBack(View view) {
        onBackPressed();
    }


    /**
     * 进入摇一摇
     */
    private void goShake() {
        mVfShake.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_in_from_right));
        mVfShake.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_out_to_left));
        mVfShake.showNext();

        noticeView.getPublicNotices(0,activityIndex);

        if (!isLogin()) {
            showToast("请先登录");
            startActivity(new Intent(YaoYiYaoActivity.this, LeFenPhoneLoginActivity.class));
            finish();
            return;
        }
        try {
            isShake();
        }catch (Exception e){

            registerShakeListener();

        }

    }

    /**
     * 退出摇一摇
     */
    private void exitShake() {
        mVfShake.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_in_from_left));
        mVfShake.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_out_to_right));
        mVfShake.showPrevious();


        unregisterShakeListener();
    }

    // 摇一摇声音
    public void playShakeSound() {
        if (mPlayer == null) return;

        if (mPlayer.isPlaying()) mPlayer.stop();

        mPlayer.reset();
        AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(R.raw.shark);
        if (afd == null) return;

        try {
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mPlayer.prepare();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mPlayer.start();
    }

    // 调用中奖声音
    public void playWinSound() {
        if (mPlayer.isPlaying()) mPlayer.stop();

        mPlayer.reset();
        AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(R.raw.shake_win);
        if (afd == null) return;

        try {
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mPlayer.prepare();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mPlayer.start();

    }

    /**
     * 仿微信摇一摇动画
     */
    private void startShakeAnimation() {
        float yValue = mImgMiddle.getHeight() / 2;
        mViewQuiet.setVisibility(View.GONE);
        mViewFloating.setVisibility(View.VISIBLE);
        mImgMiddle.setVisibility(View.VISIBLE);

        AnimationSet animup = new AnimationSet(true);
        TranslateAnimation mup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE, -yValue);
        mup0.setDuration(500);
        TranslateAnimation mup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE, +yValue);
        mup1.setDuration(500);
        // 延迟执行1秒
        mup1.setStartOffset(1000);
        animup.addAnimation(mup0);
        animup.addAnimation(mup1);
        // 上图片的动画效果的添加
        mImgUp.startAnimation(animup);

        AnimationSet animdn = new AnimationSet(true);
        TranslateAnimation mdn0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE, +yValue);
        mdn0.setDuration(500);
        TranslateAnimation mdn1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE, -yValue);
        mdn1.setDuration(500);
        // 延迟执行1秒
        mdn1.setStartOffset(1000);
        animdn.addAnimation(mdn0);
        animdn.addAnimation(mdn1);
        animdn.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                mViewQuiet.setVisibility(View.VISIBLE);
                mViewFloating.setVisibility(View.GONE);
                mImgMiddle.setVisibility(View.GONE);
                getShakeResult();
                registerShakeListener();
            }
        });

        // 下图片动画效果的添加
        mImgDown.startAnimation(animdn);
    }

    private void getShakeResult() {
        if (!mShakeFlag) return;

        if (mActivityNetRequest == null) {
            mActivityNetRequest = new ActivityNetRequest(mContext);
        }

        mActivityNetRequest.getShakeActivityResult(activityIndex, new RequestCallBack<ShakeResult>() {

            @Override
            public void onStart() {
                super.onStart();
                mShakeFlag = false;
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<ShakeResult> arg0) {
                mShakeFlag = true;
                stopMyDialog();
                mResult = arg0.result;
                Logger.i("中奖结果");
                switch (mResult.code) {
                    case 1:
                        if (mResult.prizeVoucher == 0) {
                            showLoseDialog();
                        } else {
                            showWinDialog();
                            playWinSound();
                        }
                        break;

                    default:
                        CommonUtils.NetworkRequestReturnCode(YaoYiYaoActivity.this, String.valueOf(mResult.code));
                        break;
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                mShakeFlag = true;
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    public void getActivityInfo() {

        if (mActivityNetRequest == null) {
            mActivityNetRequest = new ActivityNetRequest(mContext);
        }
        mActivityNetRequest.getActivityInfo(activityIndex, new RequestCallBack<GetActivityInfoResult>() {

            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                stopMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<GetActivityInfoResult> arg0) {
                stopMyDialog();
                GetActivityInfoResult result = arg0.result;
                if (result == null || result.activity == null) {
                    return;
                }
                mActivityInfo = result.activity;
                switch (result.code) {
                    case 1:
                        tvBack.setText(result.activity.activityName);
                        if (result.activity.startTime > result.activity.serverTime) {
                            mViewCountDown.setVisibility(View.VISIBLE);
                            mClock.setTimes(result.activity.startTime - result.activity.serverTime);
                            mClock.setCountDownListener(new CountDownListener() {

                                @Override
                                public void onStop() {
                                    mViewCountDown.setVisibility(View.INVISIBLE);
                                    goShake();
                                }
                            });
                            if (!mClock.isRun()) {
                                mClock.beginRun();
                            }
                        } else {
                            mViewCountDown.setVisibility(View.INVISIBLE);
                            goShake();
                        }
                        break;

                    default:
                        break;
                }
            }
        });
    }

    /**
     * 显示中奖弹框
     */
    private void showWinDialog() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int scrWidth = dm.widthPixels;
        int scrHeight = dm.heightPixels;

        View view = getLayoutInflater().inflate(R.layout.dialog_shake_win, null);
        ImageView prizeIcon = (ImageView) view.findViewById(R.id.imgView_prize_icon);
        TextView txtPrizeName = (TextView) view.findViewById(R.id.txt_prize_name);
        TextView txtPrizeDeadline = (TextView) view.findViewById(R.id.txt_prize_deadline);
        ImageButton imgBtnGetPrize = (ImageButton) view.findViewById(R.id.imgBtn_get_prize);
        ImageButton imgBtn_get_share = (ImageButton) view.findViewById(R.id.imgBtn_get_share);
        ImageView imgClose = (ImageView) view.findViewById(R.id.imgView_close_win);
        imgBtnGetPrize.setOnClickListener(mClickListener);
        imgClose.setOnClickListener(mClickListener);
        imgBtn_get_share.setOnClickListener(mClickListener);
        CharSequence startTime = DateFormat.format("yyyy-MM-dd", new Date(Long.parseLong(mResult.voucherValidTime) * 1000));

        if (mResult != null) {
            txtPrizeName.setText(mResult.prizeName);
            txtPrizeDeadline.setText(startTime + "之前兑换");

            if (mBitmapUtils == null) {
                mBitmapUtils = new BitmapUtils(this);
                mBitmapUtils.display(prizeIcon, Constants.ImageUrl + mResult.prizeImgUrl + Constants.ImageSuffix);
            }
        }
        if (mResultDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mResultDialog = builder.create();
            // mWinDialog.setView(view);
            mResultDialog.setCanceledOnTouchOutside(false);
        }
        mResultDialog.show();
        Window window = mResultDialog.getWindow();
        window.setContentView(view);
        //window.setLayout((int) (scrWidth * 1f), (int) (scrHeight * 0.7f));
    }

    /**
     * 未中奖弹框
     */
    private void showLoseDialog() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int scrWidth = dm.widthPixels;
        int scrHeight = dm.heightPixels;

        View view = getLayoutInflater().inflate(R.layout.dialog_shake_lose, null);
        ImageButton imgBtnShakeAgain = (ImageButton) view.findViewById(R.id.imgBtn_shake_again);
        ImageButton imgBtnOther = (ImageButton) view.findViewById(R.id.imgBtn_see_other);
        ImageView imgClose = (ImageView) view.findViewById(R.id.imgView_close_lose);
        imgBtnShakeAgain.setOnClickListener(mClickListener);
        imgBtnOther.setOnClickListener(mClickListener);
        imgClose.setOnClickListener(mClickListener);
        if (mResultDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mResultDialog = builder.create();
            mResultDialog.setCanceledOnTouchOutside(false);
        }
        mResultDialog.show();
        Window window = mResultDialog.getWindow();
        window.setContentView(view);
        window.setLayout((int) (scrWidth * 0.9f), (int) (scrHeight * 0.7f));
    }

    /**
     * 隐藏摇奖结果弹框
     */
    private void hideShakeResultDialog() {
        if (mResultDialog != null && mResultDialog.isShowing()) {
            mResultDialog.dismiss();
        }
    }

    public void rightTextview(View v) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("title_name", getString(R.string.activity_rule));
        intent.putExtra("url", Constants.ActivityRules);
        startActivity(intent);
    }

    public void isShake() {

        String locateCityId = sp.getString(Constants.SPKEY_LOCATION_CITY_ID, "0");
        String locateCountyId = sp.getString(Constants.SPKEY_LOCATION_COUNTY_ID, "0");

        MyApplication app = (MyApplication) getApplication();

        mActivityNetRequest.isShake(activityIndex, locateCityId, locateCountyId, app.getLatitude(), app.getLongitude(), new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case -47:
                        showDialog("温馨提示", "您当前的位置不在活动发布区域范围内,不能参加此活动", null, 0, Gravity.LEFT, "确定", null, false, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                if (DialogInterface.BUTTON_POSITIVE == which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                        }, mContext);
                        return;

                    default:
                        registerShakeListener();
                        break;

                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }
}
