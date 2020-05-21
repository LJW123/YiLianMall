package com.yilian.mall.suning.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.BaseDialogFragment;
import com.yilian.mall.R;
import com.yilian.mall.jd.enums.JdCommitOrderNoStockGoodsCount;
import com.yilian.mall.suning.module.SuNingCommitOrderGoodsModule;
import com.yilian.mall.widgets.MaxHeightRecyclerView;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RxUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author xiaofo on 2018/7/30.
 */

public class SnCommitOrderNoStockFragment extends BaseDialogFragment {
    public static final String TAG = "SnCommitOrderNoStockFragment";
    public static final String TAG_TYPE = "SnCommitOrderNoStockFragment_TYPE";
    private AppCompatTextView tvTitle;
    private MaxHeightRecyclerView recyclerView;
    private AppCompatTextView tvLeft;
    private AppCompatTextView tvRight;
    private JdCommitOrderNoStockGoodsCount mType;
    private ArrayList<SuNingCommitOrderGoodsModule> mNoStockDatas;
    private NoStockListener mNoStockListener;

    public static SnCommitOrderNoStockFragment getInstance(ArrayList<SuNingCommitOrderGoodsModule> datas, JdCommitOrderNoStockGoodsCount type) {
        SnCommitOrderNoStockFragment snCommitOrderNoStockFragment = new SnCommitOrderNoStockFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, datas);
        args.putSerializable(TAG_TYPE, type);
        snCommitOrderNoStockFragment.setArguments(args);
        return snCommitOrderNoStockFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_sn_commit_order_no_stock_list, container);
        initView(view);
        Bundle arguments = getArguments();
        initData(arguments);
        setView();
        return view;
    }

    private void initView(View view) {
        tvTitle = (AppCompatTextView) view.findViewById(R.id.tv_title);
        recyclerView = (MaxHeightRecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvLeft = (AppCompatTextView) view.findViewById(R.id.tv_left);
        tvRight = (AppCompatTextView) view.findViewById(R.id.tv_right);
    }

    @SuppressWarnings("unchecked")
    private void initData(Bundle arguments) {
        mType = (JdCommitOrderNoStockGoodsCount) arguments.getSerializable(TAG_TYPE);
        mNoStockDatas = (ArrayList<SuNingCommitOrderGoodsModule>) arguments.getSerializable(TAG);
        recyclerView.setAdapter(new MyAdapter(mNoStockDatas));
    }

    private void setView() {
        switch (mType) {
            case PARTLY_NO_STOCK:
                tvTitle.setText("您订单中存在的商品，在所选城市暂时无货");
                tvLeft.setText("关闭");
                RxUtil.clicks(tvLeft, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismiss();

                    }
                });
                tvRight.setText("移除并继续购买");
                RxUtil.clicks(tvRight, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismiss();
                        if (mNoStockListener != null) {
                            mNoStockListener.removeNoStockGoodsAndCommitOrder(mNoStockDatas);
                        }
                    }
                });
                break;
            case ALL_NO_STOCK:
                tvTitle.setText("抱歉，您本单购买的商品全部无货");
                tvLeft.setText("关闭");
                RxUtil.clicks(tvLeft, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismiss();

                    }
                });
                tvRight.setText("返回购物车");
                RxUtil.clicks(tvRight, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismiss();
                        if (mNoStockListener != null) {
                            mNoStockListener.backToShoppingCart();
                        }
                    }
                });
                break;
            default:
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNoStockListener = (NoStockListener) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.gravity = Gravity.BOTTOM;
            // 宽度持平
            attributes.width =WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(attributes);
        }
    }

    public interface NoStockListener {
        /**
         * 回到购物车
         */
        void backToShoppingCart();

        /**
         * 移除无货商品并购买
         *
         * @param noStockDatas
         */
        void removeNoStockGoodsAndCommitOrder(ArrayList<SuNingCommitOrderGoodsModule> noStockDatas);


    }

    private final class MyAdapter extends BaseQuickAdapter<SuNingCommitOrderGoodsModule, BaseViewHolder> {

        public MyAdapter(@Nullable List<SuNingCommitOrderGoodsModule> data) {
            super(R.layout.item_jd_commit_order_no_stock, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SuNingCommitOrderGoodsModule item) {
            GlideUtil.showImage(getContext(), item.imgUrl, helper.getView(R.id.iv_no_stock_goods));
            helper.setText(R.id.tv_content, item.goodsName);
            helper.setText(R.id.tv_count, "×" + item.count);
        }
    }
}
