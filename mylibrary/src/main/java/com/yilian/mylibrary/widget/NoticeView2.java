package com.yilian.mylibrary.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.yilian.mylibrary.R;

import java.util.ArrayList;

public class NoticeView2 extends LinearLayout {
    private static final String TAG = "LILITH";
    private Context mContext;
    private ViewFlipper viewFlipper;
    private View scrollTitleView;
    private int type;
    private boolean flag = true;
    private ArrayList jpNews;
    public SetNew setNew;

    /**
     * 构造
     *
     * @param context
     */
    public NoticeView2(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public NoticeView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public interface SetNew {
        /**
         * 设置公告内容
         *
         * @param view
         * @param position
         */
        void setNews(View view, int position);
    }

    /**
     * 获取V3版首页公告栏信息，并进行轮播
     *
     * @param jpNews
     */
    public void getJPMainNewsNotices(final ArrayList jpNews) {
        for (int i = 0, count = jpNews.size(); i < count; i++) {
            View view = View.inflate(mContext, R.layout.library_module_layout_jp_main_notice, null);
            setNew.setNews(view, i);
            LayoutParams lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            viewFlipper.addView(view, lp);
        }
    }

    /**
     * 获取幸运购-中奖快报
     * 每三条滚动一次，所以此处position做缩小三倍处理，取数据时再做*3+i处理
     */
    public void getLuckyPrizeNotices(final ArrayList jpNews) {
        for (int i = 0, count = jpNews.size() / 3; i < count; i++) {
            View view = View.inflate(mContext, R.layout.library_module_lucky_notice_1, null);
            setNew.setNews(view, i);
            LayoutParams lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            viewFlipper.addView(view, lp);
        }
    }

    /**
     * 获取幸运购-发货快报
     * 每三条滚动一次，所以此处position做缩小三倍处理，取数据时再做*3+i处理
     */
    public void getLuckySendNotices(final ArrayList jpNews) {
        for (int i = 0, count = jpNews.size() / 3; i < count; i++) {
            View view = View.inflate(mContext, R.layout.library_module_lucky_notice_2, null);
            setNew.setNews(view, i);
            LayoutParams lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            viewFlipper.addView(view, lp);
        }
    }

    public void getMemberGroupNotices(final ArrayList jpNews) {
        for (int i = 0, count = jpNews.size() / 2; i < count; i++) {
            View view = View.inflate(mContext, R.layout.library_module_lucky_notice_2_2, null);
            setNew.setNews(view, i);
            LayoutParams lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            viewFlipper.addView(view, lp);
        }
    }

    /**
     * 网络请求后返回公告内容进行适配
     */
    protected void bindNotices() {
        // TODO Auto-generated method stub
        viewFlipper.removeAllViews();
        getJPMainNewsNotices(jpNews);
    }

    protected void bindPrizeNotices() {
        // TODO Auto-generated method stub
        viewFlipper.removeAllViews();
        getLuckyPrizeNotices(jpNews);
    }

    protected void bindSendNotices() {
        // TODO Auto-generated method stub
        viewFlipper.removeAllViews();
        getLuckySendNotices(jpNews);
    }

    public void bindMemberGroupNotices() {
        // TODO Auto-generated method stub
        viewFlipper.removeAllViews();
        getMemberGroupNotices(jpNews);
    }


    private void init() {
        bindLinearLayout();
    }

    /**
     * 初始化自定义的布局
     */
    public void bindLinearLayout() {
        scrollTitleView = LayoutInflater.from(mContext).inflate(
                R.layout.library_module_notice_title_activity, null);
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(scrollTitleView, layoutParams);

        viewFlipper = (ViewFlipper) scrollTitleView
                .findViewById(R.id.flipper_scrollTitle);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.library_module_layout_in_from_top));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.library_module_layout_out_to_bottom));
        viewFlipper.startFlipping();
        View v = viewFlipper.getCurrentView();

    }


    /**
     * 获取公告信息
     *
     * @param jpNews
     */
    public void getPublicNotices(ArrayList jpNews, SetNew setNew) {
        this.jpNews = jpNews;
        this.setNew = setNew;
        bindNotices();
    }


    public void getLuckyPrizeNotices(ArrayList jpNews, SetNew setNew) {
        this.jpNews = jpNews;
        this.setNew = setNew;
        bindPrizeNotices();
    }

    public void getLuckySendNotices(ArrayList jpNews, SetNew setNew) {
        this.jpNews = jpNews;
        this.setNew = setNew;
        bindSendNotices();
    }

    public void getMemberNotice(ArrayList jpNews, SetNew setNew) {
        this.jpNews = jpNews;
        this.setNew = setNew;
        bindMemberGroupNotices();
    }


    public void onStart() {
        flag = true;
    }

    public void onStop() {
        flag = false;
    }


}
