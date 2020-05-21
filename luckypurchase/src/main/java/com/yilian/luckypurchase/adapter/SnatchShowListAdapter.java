package com.yilian.luckypurchase.adapter;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyRecordActivity;
import com.yilian.luckypurchase.activity.LuckyUnboxingActivity;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.entity.SnatchShowListEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author Created by LYQ on 2017/10/28.
 */

public class SnatchShowListAdapter extends BaseQuickAdapter<SnatchShowListEntity.SnatchInfoBean, BaseViewHolder> {
    private int posX, posY, curPosX, curPosY;

    public SnatchShowListAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final SnatchShowListEntity.SnatchInfoBean item) {
        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showCirImage(mContext, item.photo, iv);
        RxView.clicks(iv)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, mContext, false)) {
                            Intent intent = new Intent(mContext, LuckyRecordActivity.class);
                            intent.putExtra("userId", item.userId);
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            mContext.startActivity(intent);
                        }
                    }
                });
        helper.setText(R.id.tv_name, TextUtils.isEmpty(item.userName) ? "暂无昵称" : item.userName);
        helper.setText(R.id.tv_number, item.snatchIssue + "期");
        helper.setText(R.id.tv_date, DateUtils.luckyTimeNoYearNoSecond(item.time));
        TextView tvSku = helper.getView(R.id.tv_goods_sku);
        tvSku.setText(Html.fromHtml("<font color=\"#999999\">商品信息： </font><font color=\"#333333\">" + item.snatchName + "</font>"));

        TextView tvContent = helper.getView(R.id.tv_remark);
        if (TextUtils.isEmpty(item.commentContent)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(item.commentContent);
        }

        RecyclerView imageRecycle = helper.getView(R.id.horizontal_recycleView);

        imageRecycle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        posX = (int) event.getRawX();
                        posY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        curPosX = (int) event.getRawX();
                        curPosY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        int x = Math.abs(curPosX - posX);
                        int y = Math.abs(curPosY - posY);
                        Logger.i("2017年11月10日 11:14:56-" + x);
                        Logger.i("2017年11月10日 11:14:56-" + y);
                        if (x < 10 && y < 10) {
                            Intent intent = new Intent(mContext, LuckyUnboxingActivity.class);
                            intent.putExtra("activity_id", item.commentIndex);
                            mContext.startActivity(intent);
                        } else {
                            return false;
                        }
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        String[] imageUrls = item.commentImages.split(",");
        Logger.i("imageUerls  " + imageUrls.length);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageRecycle.setLayoutManager(layoutManager);
        imageRecycle.setAdapter(new ImageAdapter(R.layout.lucky_item_show_image, item.commentIndex, new ArrayList<String>(Arrays.asList(imageUrls))));
    }
}
