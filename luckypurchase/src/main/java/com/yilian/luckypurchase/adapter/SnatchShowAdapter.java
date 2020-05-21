package com.yilian.luckypurchase.adapter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyUnboxingActivity;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.networkingmodule.entity.SnatchShowListEntity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Created by LYQ on 2017/10/28.
 */

public class SnatchShowAdapter extends BaseQuickAdapter<SnatchShowListEntity.SnatchInfoBean, BaseViewHolder> {
    private int posX, posY, curPosX, curPosY;

    public SnatchShowAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final SnatchShowListEntity.SnatchInfoBean item) {
        helper.setText(R.id.tv_name, item.snatchName);
        helper.setText(R.id.tv_number, "期号: " + item.snatchIssue);
        helper.setText(R.id.tv_date, DateUtils.timeStampToStr(Long.parseLong(item.time)));

        helper.setText(R.id.tv_remark, item.commentContent);
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
