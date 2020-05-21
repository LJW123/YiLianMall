package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
import com.yilian.mall.adapter.PrizeListAdapter;
import com.yilian.mall.entity.ActivityInfo;
import com.yilian.mall.entity.GetActivityInfoResult;
import com.yilian.mall.entity.TurnTablePrize;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengDialog.OnListItemClickListener;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.widgets.MyLoading;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

/**
 * 大转盘活动详情界面
 */
public class DaZhuanPanDetailActivity extends BaseActivity {

    // 设置一个时间常量，此常量有两个作用，1.圆灯视图显示与隐藏中间的切换时间；2.指针转一圈所需要的时间，现设置为300毫秒
    private static final long ONE_WHEEL_TIME = 200;
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
    /**
     * 每次消耗奖券数
     */
    @ViewInject(R.id.txt_score_expend)
    TextView mTxtScoreExpend;
    /**
     * 奖品列表
     */
    @ViewInject(R.id.list_prize_type)
    NoScrollListView mListPrizeType;
    @ViewInject(R.id.txt_note1)
    TextView mTxtNote1;
    @ViewInject(R.id.txt_note2)
    TextView mTxtNote2;
    @ViewInject(R.id.txt_note3)
    TextView mTxtNote3;
    @ViewInject(R.id.txt_note4)
    TextView mTxtNote4;
    @ViewInject(R.id.txt_note5)
    TextView mTxtNote5;
    private MyLoading myloading;
    // 开始转动时候的角度，初始值为0
    private int startDegree = 0;
    // 指针转圈圈数数据源
    private int[] laps = {5, 7, 10, 15};
    // 外维数组的顺序0-5分别代表指针当前的位置(1-6等奖)，内维数组位置0-5下的值代表指针在当前位置下转到1-6等奖需要转动的角度
    private int[][] angles = {{0, 300, 240, 180, 60, 120}, {60, 0, 300, 240, 120, 180},
            {120, 60, 0, 300, 180, 240}, {180, 120, 60, 0, 240, 300}, {300, 240, 180, 120, 0, 60},
            {240, 180, 120, 60, 300, 0}};
    // 转盘内容数组
    private String[] lotteryStr = {"索尼PSP", "10元奖励", "谢谢参与", "DNF钱包", "OPPO MP3", "5元奖励",};
    private int mPointerPosition = 0;// 当前指针的位置(0-5),初始位置是0
    private TurnTablePrize mPrize;// 中奖信息
    private View mDialogContent;
    private AlertDialog mResultDialog;
    private String mActivityIndex;//活动id
    private String mActivityName;//活动名称
    private ActivityNetRequest mActivityNetRequest;
    private MediaPlayer mTurnSoundPlayer;
    private Animation animBottom;
    private OnClickListener mOnClickListener=new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.imgView_dial_pointer:
                    if (isLogin()) {
                        mImgDailPointer.setClickable(false);
                        getPrizeResult();
                    }else {
                        showToast(R.string.login_first);
                        startActivity(new Intent(DaZhuanPanDetailActivity.this, LeFenPhoneLoginActivity.class));
                    }
                    break;
                case R.id.imgBtn_option:
                    if (mPrize.prizeLevel!=5) {
                        if (mPrize.prizeType==0) {
                            Intent intent = new Intent(DaZhuanPanDetailActivity.this, V3MoneyActivity.class);
                            intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
                            startActivity(intent);
                        }else if (mPrize.prizeType==1) {
                            startActivity(new Intent(DaZhuanPanDetailActivity.this, PrizeVoucherListActivity.class));
                        }
                    }
                    hideResultDialog();
                    break;
                case R.id.imgView_close_lose:
                    hideResultDialog();
                    break;
                case R.id.imgBtn_share:

                    animBottom = AnimationUtils.loadAnimation(DaZhuanPanDetailActivity.this, R.anim.anim_wellcome_bottom);
                    UmengDialog dialog1 = new UmengDialog(mContext, animBottom, R.style.DialogControl,
                            new UmengGloble().getAllIconModels());
                    dialog1.setItemLister(new OnListItemClickListener() {

                        @Override
                        public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                            String content = "我在益联益家大转盘活动中抽中了" + mPrize.prizeName +",你也赶快来试试吧…好运天天有，精美好礼，等你来拿";
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
            // String name = lotteryStr[startDegree % 360 / 60];
//			Toast.makeText(DaZhuanPanDetailActivity.this, "恭喜您获得" + mPrize.prizeName, Toast.LENGTH_LONG).show();
            pauseTurnSound();
            showResultDialog();
            mTxtCount.setText(mPrize.turnTableCount + "");
            mImgDailPointer.setClickable(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dazhuanpan_detail);
        ViewUtils.inject(this);
        init();
        registerEvents();
        getActicityInfo();
        mListPrizeType.setFocusable(false);
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
        if (mTurnSoundPlayer!=null) {
            if (mTurnSoundPlayer.isPlaying()) {
                mTurnSoundPlayer.stop();
            }
            mTurnSoundPlayer.release();
            mTurnSoundPlayer=null;
        }
        super.onDestroy();
    }

    public void rightTextview(View v){
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("title_name", getString(R.string.activity_rule));
        intent.putExtra("url", Constants.ActivityRules);
        startActivity(intent);
    }

    private void init() {
        mActivityNetRequest=new ActivityNetRequest(mContext);
        mActivityIndex=getIntent().getStringExtra("activity_index");
//		mActivityName = getIntent().getStringExtra("activity_name");
//		mTxtBack.setText(mActivityName);
        mTxtRightTitle.setText(R.string.activity_rule);
        mTxtRightTitle.setTextColor(getResources().getColor(R.color.c247df7));
        mTxtCount.setText(Integer.toString(sp.getInt(Constants.SPKEY_TURN_TABLE_COUNT+mActivityIndex, 0)));
        mTurnSoundPlayer=MediaPlayer.create(this, R.raw.turntable);
        mTurnSoundPlayer.setLooping(true);
    }

    private void registerEvents() {
        mImgDailPointer.setOnClickListener(mOnClickListener);
    }

    private void getActicityInfo() {

        mActivityNetRequest.getActivityInfo(mActivityIndex,new RequestCallBack<GetActivityInfoResult>() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<GetActivityInfoResult> arg0) {
                // TODO Auto-generated method stub
                stopMyDialog();
                GetActivityInfoResult result = arg0.result;
                if (result == null || result.activity == null) {
                    return;
                }
                switch (result.code) {
                    case 1:
                        ActivityInfo activity = result.activity;
                        mTxtBack.setText(activity.activityName);
//					mTxtCount.setText(activity.playCount+"");
                        mTxtScoreExpend.setText(activity.expend + "");

                        mTxtNote1.setText(Html.fromHtml("1.获得奖品后请尽快在"+activity.timeLimit / (24 * 60 * 60)+
                                "个工作日内到兑换中心领取，过期作废！每次抽奖消耗"+"<font color='#fe6682'>" + activity.expend+"乐分币。"+ "</font>"));
                        mTxtNote2.setText(getString(R.string.turntable_notes2, activity.contacts));
                        mTxtNote3.setText(getString(R.string.turntable_notes3, activity.phone));
                        mTxtNote4.setText(getString(R.string.turntable_notes4, activity.tel, activity.phone));
                        mTxtNote5.setText(getString(R.string.turntable_notes5, activity.address));
                        PrizeListAdapter adapter = new PrizeListAdapter(DaZhuanPanDetailActivity.this, result.activity);
                        mListPrizeType.setAdapter(adapter);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                stopMyDialog();
            }
        });
    }

    /**
     * 获取参加转盘抽奖的结果
     */
    private void getPrizeResult() {

        mActivityNetRequest.getTurnTableActivityResult(mActivityIndex, new RequestCallBack<TurnTablePrize>() {

            @Override
            public void onSuccess(ResponseInfo<TurnTablePrize> arg0) {
                TurnTablePrize prize = arg0.result;
                if (prize == null) {
                    return;
                }
                sp.edit().putString("integral",Integer.toString(prize.integral)).commit();
                mPrize = prize;
                switch (prize.code) {
                    case 1:
                        sp.edit().putInt(Constants.SPKEY_TURN_TABLE_COUNT+mActivityIndex, prize.turnTableCount).commit();
                        startRotate();
                        break;

                    default:
                        CommonUtils.NetworkRequestReturnCode(DaZhuanPanDetailActivity.this, String.valueOf(prize.code));
                        mImgDailPointer.setClickable(true);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                mImgDailPointer.setClickable(true);
            }
        });
    }

    /**
     * 开始旋转
     */
    public void startRotate() {
        int lap = laps[(int) (Math.random() * 4)];
        int angle = angles[mPointerPosition][mPrize.prizeLevel];// angles[(int)
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
        rotateAnimation.setInterpolator(DaZhuanPanDetailActivity.this,
                android.R.anim.accelerate_decelerate_interpolator);
        // 设置动画的监听器
        rotateAnimation.setAnimationListener(al);
        // 开始播放动画
        mImgDailPointer.startAnimation(rotateAnimation);
        mPointerPosition = mPrize.prizeLevel;
    }

    public void playTurnSound() {
        if (mTurnSoundPlayer==null) {
            return;
        }
        mTurnSoundPlayer.start();
    }

    public void pauseTurnSound() {
        if (mTurnSoundPlayer==null) {
            return;
        }
        if (mTurnSoundPlayer.isPlaying()) {
            mTurnSoundPlayer.pause();
        }
    }

    private void showResultDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_turntable_result, null);
        TextView txtTitle=(TextView) view.findViewById(R.id.txt_title);
        TextView txtResult=(TextView) view.findViewById(R.id.txt_result_text);
        ImageView exImageView=(ImageView) view.findViewById(R.id.imgView_expression);
        ImageButton optionImageBtn=(ImageButton) view.findViewById(R.id.imgBtn_option);
        ImageView closeImgView=(ImageView) view.findViewById(R.id.imgView_close_lose);
        ImageButton imgBtn_share = (ImageButton) view.findViewById(R.id.imgBtn_share);
        if (mPrize.prizeLevel==5) {
            txtTitle.setText("很抱歉！");
            txtResult.setText("再来一次说不定会有好运气哦");
            exImageView.setBackgroundResource(R.mipmap.shibai_ren);
            optionImageBtn.setBackgroundResource(R.mipmap.shibai_font);
            imgBtn_share.setVisibility(View.GONE);
        }else {
            txtTitle.setText("恭喜您！");
            txtResult.setText("您获得"+mPrize.prizeName);
            exImageView.setBackgroundResource(R.mipmap.zhongjiang_ren);
            optionImageBtn.setBackgroundResource(R.mipmap.zhongjiang_into);
            imgBtn_share.setBackgroundResource(R.mipmap.dazhuanpan_share);
        }
        closeImgView.setOnClickListener(mOnClickListener);
        optionImageBtn.setOnClickListener(mOnClickListener);
        imgBtn_share.setOnClickListener(mOnClickListener);
        if (mResultDialog==null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mResultDialog = builder.create();
            mResultDialog.setCanceledOnTouchOutside(false);
        }
        mResultDialog.show();
        Window window = mResultDialog.getWindow();
        window.setContentView(view);
    }


    private void hideResultDialog() {
        if (mResultDialog!=null&&mResultDialog.isShowing()) {
            mResultDialog.dismiss();
        }
    }
}
