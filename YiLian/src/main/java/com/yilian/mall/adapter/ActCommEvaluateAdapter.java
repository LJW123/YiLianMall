package com.yilian.mall.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.yilian.mall.ui.ActEvaDetailActivity;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ActGoodsEvaListEntity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ray_L_Pain on 2017/12/13 0013.
 */

public class ActCommEvaluateAdapter extends BaseQuickAdapter<ActGoodsEvaListEntity.ListBean, com.chad.library.adapter.base.BaseViewHolder> {

    public ActCommEvaluateAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActGoodsEvaListEntity.ListBean item) {
        helper.addOnClickListener(R.id.tv_zan_count);

        ImageView iv = helper.getView(R.id.title_img);
        GlideUtil.showCirImageNoLit(mContext, item.userPhoto, iv, item.commentIndex);

        if (TextUtils.isEmpty(item.commentConsumer)) {
            helper.setText(R.id.tv_name, "暂无昵称");
        } else {
            helper.setText(R.id.tv_name, item.commentConsumer);
        }

        helper.setText(R.id.tv_time, StringFormat.formatDate(item.commentTime, "yyyy-MM-dd"));

        float grade = (float) (Math.round((item.commentScore / 10.0f) * 10)) / 10;
        helper.setRating(R.id.ratingBar, grade);

        helper.setText(R.id.tv_content, item.commentContent);

        final String[] img = item.commentImages.split(",");

        RecyclerView recyclerView = helper.getView(R.id.recycler_view);

        for (int i = 0; i < img.length; i++) {
            if (!TextUtils.isEmpty(img[i])) {
                img[i] = Constants.ImageUrl + img[i];
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        ImageRecycleAdapter bigAdapter = new ImageRecycleAdapter(R.layout.lucky_item_show_image_big, new ArrayList<String>(Arrays.asList(img)));
        recyclerView.setAdapter(bigAdapter);
        bigAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                imageBrower(position, img);
            }
        });

        helper.setText(R.id.tv_goods_name, item.goodsName);

        helper.setText(R.id.tv_time_buy, "购买日期：" + StringFormat.formatDate(item.orderTime, "yyyy-MM-dd"));
        TextView tvZanCount = helper.getView(R.id.tv_zan_count);
        TextView tvEvaCount = helper.getView(R.id.tv_evaluate_count);

        Drawable drawable;
        if ("0".equals(item.isParise)) {
            drawable = mContext.getResources().getDrawable(R.mipmap.act_zan_off);
        } else {
            drawable = mContext.getResources().getDrawable(R.mipmap.act_zan_on);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvZanCount.setCompoundDrawables(drawable, null, null, null);
        tvZanCount.setText(String.valueOf(item.commentPraise));

        tvEvaCount.setText(item.countReply);
        tvEvaCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActEvaDetailActivity.class);
                intent.putExtra("comment_index", item.commentIndex);
                mContext.startActivity(intent);
            }
        });
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }
}
