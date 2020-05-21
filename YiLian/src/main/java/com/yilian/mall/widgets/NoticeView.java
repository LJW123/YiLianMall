package com.yilian.mall.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yilian.mall.R;
import com.yilian.mall.entity.NoticeViewEntity;
import com.yilian.mall.entity.OneBuyRotateAwards;
import com.yilian.mall.entity.ShakeWinners;
import com.yilian.mall.entity.ShakeWinnersList;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.http.OneBuyNetRequest;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.Toast;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

import static com.yilian.mall.MyApplication.sp;

public class NoticeView extends LinearLayout {
    private static final String TAG = "LILITH";
    ActivityNetRequest activityNetRequest;
    OneBuyNetRequest oneBuyNetRequest;
    private Context mContext;
    private ViewFlipper viewFlipper;
    private View scrollTitleView;
    private ArrayList<ShakeWinners> mShakeWinnersList = new ArrayList();
    private ArrayList<OneBuyRotateAwards.ListBean> mOneBuyRotateAwards = new ArrayList();
    private int type;
    private boolean flag = true;
    private ArrayList<NoticeViewEntity> jpNews;

    /**
     * 构造
     *
     * @param context
     */
    public NoticeView(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public NoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();

    }

    /**
     * 获取V3版首页公告栏信息，并进行轮播
     *
     * @param jpNews
     */
    public void getJPMainNewsNotices(final ArrayList<NoticeViewEntity> jpNews) {
        for (int i = 0, count = jpNews.size(); i < count; i++) {
            final NoticeViewEntity activityNews = jpNews.get(i);
            View view = View.inflate(mContext, R.layout.layout_jp_main_notice, null);
            TextView tvMoreNotice = (TextView) view.findViewById(R.id.tv_more_notice);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(activityNews.type, activityNews.content);
                }
            });
            TextView tvNotice = (TextView) view.findViewById(R.id.tv_notice);
            tvNotice.setText(activityNews.msg);
            Logger.i("activityNewsContent:" + activityNews.content);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            viewFlipper.addView(view, lp);
        }
    }

    //拼团的公告
    private void getSpellGroupNotice(ArrayList<NoticeViewEntity> jpNews) {
        viewFlipper.removeAllViews();
        for (int i = 0; i < jpNews.size(); i++) {
            View view = View.inflate(mContext, R.layout.item_spell_group_notice, null);
            TextView tvConten = (TextView) view.findViewById(R.id.tv_spell_group_success_content);
            TextView tvTime = (TextView) view.findViewById(R.id.tv_spell_group_success_time);
            tvTime.setText(DateUtils.convertTimeToFormat(Long.parseLong(jpNews.get(i).voucharTime)));
            String userName = jpNews.get(i).userName;
            String goodsName = jpNews.get(i).goodsName;
            tvConten.setText(Html.fromHtml(userName + "<font color=\"#4A4A4A\">成功拼到</font>" + goodsName));
            viewFlipper.addView(view);
        }
    }

    /**
     * 判断是否登录
     */
    public boolean isLogin() {
        return PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, mContext, true);
    }

    /**
     * 网络请求后返回公告内容进行适配
     */
    protected void bindNotices() {
        // TODO Auto-generated method stub
        viewFlipper.removeAllViews();
        int i = 0;

        switch (type) {
            case 0:
                while (i < mOneBuyRotateAwards.size()) {
                    TextView textView = new TextView(mContext);
                    textView.setTextColor(getResources().getColor(R.color.red));
                    textView.setMaxLines(1);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
                    textView.setText(Html.fromHtml(" <font color=\"#000000\">中奖名单: </font>" +
                            mShakeWinnersList.get(i).userPhone.replace(
                                    mShakeWinnersList.get(i).userPhone.subSequence(3, 7), "****")
                            + "<font color=\"#000000\"> 在摇一摇获得"
                            + mShakeWinnersList.get(i).voucherPrizeName + "</font>"));
                    LayoutParams lp = new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    viewFlipper.addView(textView, lp);
                    i++;

                }
                break;

            case 2:
                getJPMainNewsNotices(jpNews);
                break;
            case 3:
                getSpellGroupNotice(jpNews);
                break;
        }


    }

    private void init() {
        bindLinearLayout();

        //mHandler.sendMessageDelayed(msg);

    }

    /**
     * 初始化自定义的布局
     */
    public void bindLinearLayout() {
        scrollTitleView = LayoutInflater.from(mContext).inflate(
                R.layout.notice_title_activity, null);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(scrollTitleView, layoutParams);

        viewFlipper = (ViewFlipper) scrollTitleView
                .findViewById(R.id.flipper_scrollTitle);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.layout_in_from_top));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.layout_out_to_bottom));
        viewFlipper.startFlipping();
        View v = viewFlipper.getCurrentView();

    }

    /**
     * 获取公告资讯
     *
     * @param type         0 摇一摇数据  1 幸运购数据
     * @param activitIndex
     */
    public void getPublicNotices(final int type, final String activitIndex) {
        this.type = type;
        switch (type) {
            case 0:
                getYaoYiYao(activitIndex);
                break;

            case 1:
                getOneBuy();
                break;
        }
    }

    /**
     * 获取公告信息
     *
     * @param type
     * @param jpNews
     */
    public void getPublicNotices(int type, ArrayList<NoticeViewEntity> jpNews) {
        this.type = type;
        this.jpNews = jpNews;
        sp = mContext.getSharedPreferences("UserInfor", 0);
        bindNotices();
    }


    public void getYaoYiYao(final String activitIndex) {


        if (activityNetRequest == null) {
            activityNetRequest = new ActivityNetRequest(mContext);
        }
        activityNetRequest.getShakeWinners(activitIndex, new RequestCallBack<ShakeWinnersList>() {

            @Override
            public void onSuccess(ResponseInfo<ShakeWinnersList> arg0) {
                switch (arg0.result.code) {
                    case 1:
                        mShakeWinnersList.clear();
                        mShakeWinnersList = arg0.result.list;
                        bindNotices();
                        System.out.println(mShakeWinnersList.size());

                        if (flag) {
                            if (mShakeWinnersList.size() < 3) {
                                new Thread(
                                        new Runnable() {
                                            public void run() {
                                                try {
                                                    Thread.sleep(5000);
                                                    getYaoYiYao(activitIndex);
                                                } catch (InterruptedException e) {
                                                    getYaoYiYao(activitIndex);
                                                }
                                            }
                                        }).start();

                            } else {
                                new Thread(
                                        new Runnable() {
                                            public void run() {
                                                try {
                                                    System.out.println(mShakeWinnersList.size() * 2500);
                                                    Thread.sleep(mShakeWinnersList.size() * 2500);
                                                    getYaoYiYao(activitIndex);
                                                } catch (InterruptedException e) {
                                                    getYaoYiYao(activitIndex);
                                                }
                                            }
                                        }).start();
                            }
                        }


                        break;

                    default:
                        System.out.println(arg0.result.code);
                        break;
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub

            }
        });

    }

    public void getOneBuy() {


        if (oneBuyNetRequest == null) {
            oneBuyNetRequest = new OneBuyNetRequest(mContext);
        }
        oneBuyNetRequest.oneBuyRotateAwards(new RequestCallBack<OneBuyRotateAwards>() {

            @Override
            public void onSuccess(ResponseInfo<OneBuyRotateAwards> arg0) {
                switch (arg0.result.code) {
                    case 1:
                        mOneBuyRotateAwards.clear();
                        mOneBuyRotateAwards = arg0.result.list;
                        bindNotices();
                        System.out.println(mOneBuyRotateAwards.size());
                        if (flag) {
                            if (mOneBuyRotateAwards.size() < 3) {
                                new Thread(
                                        new Runnable() {
                                            public void run() {
                                                try {
                                                    Thread.sleep(5000);
                                                    getOneBuy();
                                                } catch (InterruptedException e) {
                                                    getOneBuy();
                                                }
                                            }
                                        }).start();

                            } else {
                                new Thread(
                                        new Runnable() {
                                            public void run() {
                                                try {
                                                    System.out.println(mOneBuyRotateAwards.size() * 2500);
                                                    Thread.sleep(mOneBuyRotateAwards.size() * 2500);
                                                    getOneBuy();
                                                } catch (InterruptedException e) {
                                                    getOneBuy();
                                                }
                                            }
                                        }).start();
                            }

                        }
                        break;

                    default:
                        System.out.println(arg0.result.code);
                        break;
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub

            }
        });


    }

    /**
     * 显示notice的具体内容
     *
     * @param context
     * @param titleid
     */
    public void disPlayNoticeContent(Context context, String titleid) {
        // TODO Auto-generated method stub
        Toast.makeText(context, titleid, Toast.LENGTH_SHORT).show();
        //	        intent = new Intent(mContext, InformationContentActivity.class);
        //	        intent.putExtra("tag", titleid);
        //	        ((Activity)mContext).startActivity(intent);
    }

    public void onStart() {
        flag = true;
    }

    public void onStop() {
        flag = false;
    }

    /**
     * 公告title监听
     *
     * @author Nono
     */
    class NoticeTitleOnClickListener implements OnClickListener {
        private Context context;
        private String titleid;

        public NoticeTitleOnClickListener(Context context, String whichText) {
            this.context = context;
            this.titleid = whichText;
        }

        public void onClick(View v) {
            // TODO Auto-generated method stub
            disPlayNoticeContent(context, titleid);
        }

    }


}
