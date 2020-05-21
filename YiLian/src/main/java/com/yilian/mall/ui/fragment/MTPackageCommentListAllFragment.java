package com.yilian.mall.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.adapter.MTPackageCommentListAdapter;
import com.yilian.mall.entity.MTPackageCommentListEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.widgets.FlowLayout;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 套餐评论 全部评论/晒图评论
 */
public class MTPackageCommentListAllFragment extends BaseFragment {

    private PullToRefreshListView ptrlvMtPackageCommentList;
    private View rootView;
    private View headerView;
    private RatingBar rbPackageTotalScore;
    private TextView tvPackageTotalScore;
    private FlowLayout flCommentLabel;
    private MTNetRequest mtNetRequest;
    private String packageId;
    private String merchantId;
    private int page = 0;
    private String totalCount;
    private String picCount = "获取失败";
    private ArrayList<MTPackageCommentListEntity.DataBean> commentList = new ArrayList<>();
    private MTPackageCommentListAdapter adapter;
    private boolean isHideHeader;
    private boolean isHideHeader1;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mtpackage_comment_list_all, container, false);
        }
        getIntentData();
        initView(rootView);
        initData();
        initListener();
        return rootView;
    }

    @Override
    protected void loadData() {

    }

    private void getIntentData() {

        Intent intent = getActivity().getIntent();
        isHideHeader1 = getArguments().getBoolean("isHideHeader");
        packageId = intent.getStringExtra("packageId");
        merchantId = intent.getStringExtra("merchantId");
        Logger.i("这个评论列表的内存地址值"+this.toString()+" 类名："+this.getClass().getSimpleName()+" isHideHeader1:"+isHideHeader1);

    }

    private void initListener() {
        ptrlvMtPackageCommentList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getData();
            }
        });

    }

    private void initData() {
        getData();
    }

    /**
     * 获取评论
     */
    private void getData() {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.getMTPackageCommentAllList(packageId, merchantId, "0", page, new RequestCallBack<MTPackageCommentListEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTPackageCommentListEntity> responseInfo) {
                MTPackageCommentListEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        String count = result.count;//评论数量
                        ArrayList<MTPackageCommentListEntity.TagsBean> tags = result.tags;//评论标签
                        String evaluate = result.evaluate;//总评分
                        if (!isHideHeader) {
                            initCommentHeader(count, tags, evaluate);
                        }
                        ArrayList<MTPackageCommentListEntity.DataBean> data = result.data;//评论列表内容
                        if (page == 0) {
                            commentList.clear();
                        }else{
                            showToast("加载完毕");
                        }
                        commentList.addAll(data);
                        adapter.notifyDataSetChanged();
                        break;
                    case 0:
                        break;
                    case -17:
                        break;
                    case -4:
                        break;
                    case -3:
                        showToast(R.string.system_busy);
                        break;
                    case -23:
                        break;
                    default:
                        showToast("" + result.code);
                        break;

                }
                stopMyDialog();
                ptrlvMtPackageCommentList.onRefreshComplete();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
                ptrlvMtPackageCommentList.onRefreshComplete();
            }
        });
    }

    /**
     * 初始化评论界面头部内容
     *
     * @param count    评论数量
     * @param tags     评论标签
     * @param evaluate 评论总分
     */
    private void initCommentHeader(String count, ArrayList<MTPackageCommentListEntity.TagsBean> tags, String evaluate) {
        flCommentLabel.removeAllViews();//每次初始化前先清空子控件，避免重复添加
        this.totalCount = count;
        float v = NumberFormat.convertToFloat(evaluate, 50f) / 10;
        rbPackageTotalScore.setRating(v);//总体评价 星星数量
        Logger.i("套餐商家评论总分数:"+evaluate+"  计算过的分数："+v);
        tvPackageTotalScore.setText(v + "分");//数字分数
        //标签最少返回三个 2016.12.7下午兵权确定的需求，且只有前三个加颜色
        TextView tvTag;

        for (int i = 0, subCount = tags.size(); i < subCount; i++) {//流式布局 TextView 显示标签
            tvTag = new TextView(mContext);
            //            使用自定义的流式布局要重写 属性代码 否则他不知道自己的位置 VewGroup.MarginLayoutParams
            ViewGroup.MarginLayoutParams mlp=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mlp.setMargins(DPXUnitUtil.dp2px(mContext,15f), DPXUnitUtil.dp2px(mContext,10f),
                    DPXUnitUtil.dp2px(mContext,7f), DPXUnitUtil.dp2px(mContext,10f));
            tvTag.setLayoutParams(mlp);
            MTPackageCommentListEntity.TagsBean tagsBean = tags.get(i);
            Logger.i("评价标签："+tagsBean.toString());
            tvTag.setText(tagsBean.tagName + " " + tagsBean.tagCount);
//            #FFB033  #999999
            if (i < 3) {//前三个字体颜色FFB033  边框颜色 FFB033
                tvTag.setTextColor(Color.parseColor("#FFB033"));
                tvTag.setBackgroundResource(R.drawable.shape_bg_mt_comment_list_tags_yellow);
            } else {//三个后面的 字体颜色999999 边框颜色 999999
                tvTag.setTextColor(Color.parseColor("#999999"));
                tvTag.setBackgroundResource(R.drawable.shape_bg_mt_comment_list_tags_gray);
            }
            flCommentLabel.addView(tvTag);
        }
    }

    private void initView(View rootView) {
        ptrlvMtPackageCommentList = (PullToRefreshListView) rootView.findViewById(R.id.ptrlv_mt_package_comment_list);
        ptrlvMtPackageCommentList.setMode(PullToRefreshBase.Mode.BOTH);
        if (!isHideHeader1) {//如果不隐藏头部，则为fragment添加头部
            headerView = View.inflate(mContext, R.layout.item_header_mt_package_comment_list, null);
            ptrlvMtPackageCommentList.getRefreshableView().addHeaderView(headerView);
            initHeaderView(headerView);
        }
        adapter = new MTPackageCommentListAdapter(mContext, commentList, imageLoader, options);
        ptrlvMtPackageCommentList.setAdapter(adapter);

    }

    /**
     * 获取listview的头部子控件
     *
     * @param headerView
     */
    private void initHeaderView(View headerView) {
        rbPackageTotalScore = (RatingBar) headerView.findViewById(R.id.rb_package_total_score);
        tvPackageTotalScore = (TextView) headerView.findViewById(R.id.tv_package_total_score);
        flCommentLabel = (FlowLayout) headerView.findViewById(R.id.fl_comment_label);
    }


}
