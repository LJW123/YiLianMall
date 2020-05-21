package com.yilian.mylibrary.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.R;
import com.yilian.mylibrary.entity.NoticeViewEntity;

import java.util.ArrayList;

public class NoticeView extends LinearLayout {
    private static final String TAG = "LILITH";
    private Context mContext;
    private ViewFlipper viewFlipper;
    private View scrollTitleView;
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
            View view = View.inflate(mContext, R.layout.library_module_layout_jp_main_notice, null);
            TextView tvMoreNotice = (TextView) view.findViewById(R.id.tv_more_notice);
//            更多跳转
            tvMoreNotice.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            TextView tvNotice = (TextView) view.findViewById(R.id.tv_notice);
            tvNotice.setText(activityNews.msg);
            Logger.i("activityNewsContent:" + activityNews.content);
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
        int i = 0;
        switch (type) {
            case 2:
                getJPMainNewsNotices(jpNews);
                break;
        }
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
     * 显示notice的具体内容
     *
     * @param context
     * @param titleid
     */
    public void disPlayNoticeContent(Context context, String titleid) {
        // TODO Auto-generated method stub
        Toast.makeText(context, titleid, Toast.LENGTH_SHORT).show();
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

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            disPlayNoticeContent(context, titleid);
        }

    }


}
