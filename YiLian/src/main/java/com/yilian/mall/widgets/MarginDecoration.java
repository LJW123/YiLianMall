package com.yilian.mall.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yilian.mall.R;


public class MarginDecoration extends RecyclerView.ItemDecoration {
  private int left_margin,top_marg,right_margin,buttom_margin;

  public MarginDecoration(Context context) {
    left_margin = context.getResources().getDimensionPixelSize(R.dimen.day_text_padding);
    top_marg = context.getResources().getDimensionPixelSize(R.dimen.day_text_padding);
    right_margin = context.getResources().getDimensionPixelSize(R.dimen.day_text_padding);
    buttom_margin = context.getResources().getDimensionPixelSize(R.dimen.day_text_padding);
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    outRect.set(left_margin, top_marg, right_margin, buttom_margin);
  }
}