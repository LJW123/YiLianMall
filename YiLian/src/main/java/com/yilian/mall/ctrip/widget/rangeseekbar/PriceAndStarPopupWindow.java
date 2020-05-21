package com.yilian.mall.ctrip.widget.rangeseekbar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author xiaofo on 2018/8/18.
 */

public class PriceAndStarPopupWindow {
    private static volatile PriceAndStarPopupWindow mInstance = null;
    private ArrayList<PriceRange> mPriceRanges = new ArrayList<>();
    private RangeSeekBar rangeSeekBar;
    private RecyclerView recyclerViewPrice;
    private RecyclerView recyclerViewStar;
    private Button btnComplete;
    private Button btnReset;
    private ArrayList<Star> mStars;
    private PopupWindow popupWindow;

    private PriceAndStarPopupWindow() {
    }

    public static PriceAndStarPopupWindow getInstance() {
        if (mInstance == null) {
            synchronized (PriceAndStarPopupWindow.class) {
                if (mInstance == null) {
                    mInstance = new PriceAndStarPopupWindow();
                }
            }
        }

        return mInstance;
    }


    public PriceAndStarPopupWindow setPriceRanges(ArrayList<PriceRange> priceRanges) {
        mPriceRanges = priceRanges;
        return this;
    }

    public PriceAndStarPopupWindow setStars(ArrayList<Star> stars) {
        mStars = stars;
        return this;
    }

    public PopupWindow getPopupWindow(Context context) {
        View popupView = View.inflate(context, R.layout.view_popup_window_price_star, null);
        rangeSeekBar = popupView.findViewById(R.id.range_seek_bar);
        rangeSeekBar.setRange(0,1000);
        recyclerViewPrice = popupView.findViewById(R.id.recycler_view_price);
        if (mPriceRanges != null) {
            recyclerViewPrice.setLayoutManager(new GridLayoutManager(context, 4));
            recyclerViewPrice.setAdapter(new MyPriceAdapter(mPriceRanges));
        }
        recyclerViewStar = popupView.findViewById(R.id.recycler_view_star);
        if (mStars != null) {
            recyclerViewStar.setLayoutManager(new GridLayoutManager(context, 4));
            recyclerViewStar.setAdapter(new MyStarAdapter(mStars));
        }
        btnReset = popupView.findViewById(R.id.btn_reset);
        btnComplete = popupView.findViewById(R.id.btn_complete);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    ;
                    popupWindow = null;
                }
            }
        });
        return popupWindow;
    }


    class MyPriceAdapter extends BaseQuickAdapter<PriceRange, BaseViewHolder> {

        public MyPriceAdapter(@Nullable List<PriceRange> data) {
            super(R.layout.item_price_range, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PriceRange item) {
            helper.setText(R.id.tv_price_range, item.price);
        }
    }

    class MyStarAdapter extends BaseQuickAdapter<Star, BaseViewHolder> {
        public MyStarAdapter(@Nullable List<Star> data) {
            super(R.layout.item_price_range, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Star item) {
            helper.setText(R.id.tv_price_range, item.star);
        }
    }

}
