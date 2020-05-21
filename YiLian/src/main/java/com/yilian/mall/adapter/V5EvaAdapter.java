package com.yilian.mall.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.mylibrary.adapter.ImageHozAdapter;
import com.yilian.networkingmodule.entity.V5MerchantDetailEntity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  V5版本商家详情中评论列表
 * @author Ray_L_Pain
 * @date 2017/12/25 0025
 */

public class V5EvaAdapter extends BaseQuickAdapter<V5MerchantDetailEntity.InfoBean.CommentsBean, com.chad.library.adapter.base.BaseViewHolder> {
    private String count;

    public V5EvaAdapter(@LayoutRes int layoutResId, String count) {
        super(layoutResId);
        this.count = count;
    }

    @Override
    protected void convert(BaseViewHolder helper, V5MerchantDetailEntity.InfoBean.CommentsBean item) {
        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showImage(mContext, item.imageurl, iv);

        helper.setText(R.id.tv_name, item.name);

        helper.setText(R.id.tv_time, DateUtils.timeStampToStr2(Long.parseLong(item.commitDate)));

        ImageView ivState = helper.getView(R.id.iv_state);
        TextView tvState = helper.getView(R.id.tv_state);
        int graded = Integer.parseInt(item.evaluate) / 10;
        if (graded <= 1) {
            ivState.setImageResource(R.mipmap.v5_eva_state_list_1);
            tvState.setText("不满");
        } else if (graded <= 2 && graded > 1) {
            ivState.setImageResource(R.mipmap.v5_eva_state_list_2);
            tvState.setText("一般");
        } else if (graded <= 3 && graded > 2) {
            ivState.setImageResource(R.mipmap.v5_eva_state_list_3);
            tvState.setText("满意");
        } else {
            ivState.setImageResource(R.mipmap.v5_eva_state_list_4);
            tvState.setText("超赞");
        }

        helper.setText(R.id.tv_content, item.consumerComment);

        TextView tvReply = helper.getView(R.id.tv_reply);
        if (TextUtils.isEmpty(item.merchantComment)) {
            tvReply.setVisibility(View.GONE);
        } else {
            tvReply.setVisibility(View.VISIBLE);
            tvReply.setText("商家回复：" +item.merchantComment);
        }

        RecyclerView recyclerView = helper.getView(R.id.recycler_view);
        if (TextUtils.isEmpty(item.album)) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            String[] img = item.album.split(",");
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            ImageHozAdapter adapter = new ImageHozAdapter(R.layout.lucky_item_show_image_big, new ArrayList<String>(Arrays.asList(img)));
            recyclerView.setAdapter(adapter);
            if (Integer.parseInt(count) > 2 && adapter.getFooterLayoutCount() == 0) {
                View footView = View.inflate(mContext, R.layout.foot_v5_eva, null);
                adapter.addFooterView(footView);
                TextView tvGoAll = (TextView) footView.findViewById(R.id.tv_go_all);
                tvGoAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showMessage(mContext, "点击了查看全部评论");
                    }
                });
            }

            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    imageBrower(position, img);
                }
            });
        }
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }
}
