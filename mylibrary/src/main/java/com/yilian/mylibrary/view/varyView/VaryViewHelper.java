package com.yilian.mylibrary.view.varyView;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yilian.mylibrary.R;


/**
 * 作者： Zg   时间： 2018/5/23.
 * Description 空白，错误，加载中，网络设置等view切换
 * 修改：
 */
public class VaryViewHelper {
    /**
     * 切换不同视图的帮助类
     */
    public OverlapViewHelper mViewHelper;
    /**
     * 错误页面
     */
    public View mErrorView;
    /**
     * 正在加载页面
     */
    public View mLoadingView;
    /**
     * 数据为空的页面
     */
    public View mEmptyView;
    /**
     * 正在加载页面的进度环
     */
    public ProgressBar mLoadingProgress;


    public VaryViewHelper(View view) {
        this(new OverlapViewHelper(view));
    }

    public VaryViewHelper(OverlapViewHelper helper) {
        this.mViewHelper = helper;
    }


    public void setUpEmptyView(View view, View.OnClickListener listener) {
        mEmptyView = view;
        mEmptyView.setClickable(true);

        View refresh = view.findViewById(R.id.vv_error_refresh);
        if (refresh != null) {
            refresh.setOnClickListener(listener);
        }
    }
    public void setToHomeEmptyView(View view, View.OnClickListener listener) {
        mEmptyView = view;
        mEmptyView.setClickable(true);

        View refresh = view.findViewById(R.id.vv_to_home);
        if (refresh != null) {
            refresh.setOnClickListener(listener);
        }
    }

    public void setUpErrorView(View view, View.OnClickListener listener) {
        mErrorView = view;
        mErrorView.setClickable(true);

        View refresh = view.findViewById(R.id.vv_error_refresh);
        if (refresh != null) {
            refresh.setOnClickListener(listener);
        }
    }

    public void setUpLoadingView(View view) {
        mLoadingView = view;
        mLoadingView.setClickable(true);
//        mLoadingProgress = (ProgressBar) view.findViewById(R.id.loading);
//        TextView loading_tips_show = (TextView) mLoadingView.findViewById(R.id.loading_tips_show);
//        loading_tips_show.setText(mLoadingTip);
    }

    public void showEmptyView() {
        mViewHelper.showCaseLayout(mEmptyView);
//        stopProgressLoading();
    }

    public void showErrorView() {
        mViewHelper.showCaseLayout(mErrorView);
//        stopProgressLoading();
    }

    public void showLoadingView() {
        mViewHelper.showCaseLayout(mLoadingView);
//        startProgressLoading();
    }

    public void showDataView() {
        mViewHelper.restoreLayout();
//        stopProgressLoading();
    }


//    private void stopProgressLoading() {
//        if (mLoadingProgress != null && mLoadingProgress.isSpinning) {
//            mLoadingProgress.stopSpinning();
//        }
//    }
//
//    private void startProgressLoading() {
//        if (mLoadingProgress != null && !mLoadingProgress.isSpinning()) {
//            mLoadingProgress.spin();
//        }
//    }

    public void releaseVaryView() {
        mErrorView = null;
        mLoadingView = null;
        mEmptyView = null;
    }

    public static class Builder {
        private View mErrorView;
        private View mLoadingView;
        private View mEmptyView;
        private View mDataView;
        private View.OnClickListener mRefreshListener;
        private View.OnClickListener mToHomeListener;

        public Builder setErrorView(View errorView) {
            mErrorView = errorView;
            return this;
        }

        public Builder setLoadingView(View loadingView) {
            mLoadingView = loadingView;
            return this;
        }

        /**
         * 空页面布局
         *
         * @param emptyView  空页面文件
         * @param imageResId 图片提示 资源文件
         * @param emptyTips  文字提示
         * @return
         */
        public Builder setEmptyView(View emptyView, int imageResId, String emptyTips) {
            mEmptyView = emptyView;
            ImageView empty_img_show = (ImageView) mEmptyView.findViewById(R.id.empty_img_show);
            if (empty_img_show != null) {
                empty_img_show.setImageResource(imageResId);
            }
            TextView empty_tips_show = (TextView) mEmptyView.findViewById(R.id.empty_tips_show);
            if (empty_tips_show != null && !TextUtils.isEmpty(emptyTips)) {
                empty_tips_show.setText(emptyTips);
            }
            return this;
        }

        public Builder setDataView(View dataView) {
            mDataView = dataView;
            return this;
        }

        public Builder setRefreshListener(View.OnClickListener refreshListener) {
            mRefreshListener = refreshListener;
            return this;
        }
        public Builder setToHomeListener(View.OnClickListener toHomeListener) {
            mToHomeListener = toHomeListener;
            return this;
        }

        public VaryViewHelper build() {
            VaryViewHelper helper = new VaryViewHelper(mDataView);
            if (mEmptyView != null) {
                helper.setUpEmptyView(mEmptyView, mRefreshListener);
                helper.setToHomeEmptyView(mEmptyView, mToHomeListener);
            }
            if (mErrorView != null) {
                helper.setUpErrorView(mErrorView, mRefreshListener);
            }
            if (mLoadingView != null) {
                helper.setUpLoadingView(mLoadingView);
            }
            return helper;
        }
    }

}
