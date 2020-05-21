package com.yilian.mall.jd.fragment.commitorder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.BaseDialogFragment;
import com.yilian.mall.R;
import com.yilian.mall.jd.enums.JdCommitOrderNoStockGoodsCount;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author xiaofo on 2018/7/3.
 */

public class CommitOrderNoStockFragment extends BaseDialogFragment {
    public static final String TAG = "CommitOrderNoStockFragment";
    public static final String TAG_TYPE = "CommitOrderNoStockFragment_TYPE";
    private static final int MY_MAX_HEIGHT = 500;
    ArrayList<JDCommitOrderEntity> mNoStockDatas;
    private RecyclerView recyclerView;
    private AppCompatTextView tvLeft;
    private AppCompatTextView tvRight;
    private JdCommitOrderNoStockGoodsCount mType;
    private NoStockListener mNoStockListener;
    private TextView tvTitle;

    public static CommitOrderNoStockFragment getInstance(ArrayList<JDCommitOrderEntity> noStockDatas, JdCommitOrderNoStockGoodsCount type) {
        CommitOrderNoStockFragment commitOrderNoStockFragment = new CommitOrderNoStockFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, noStockDatas);
        args.putSerializable(TAG_TYPE, type);
        commitOrderNoStockFragment.setArguments(args);
        return commitOrderNoStockFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mNoStockListener = (NoStockListener) context;
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_jd_commit_order_no_stock_list, container);
        initView(view);
        Bundle arguments = getArguments();
        initData(arguments);
        setView();
        return view;
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvLeft = (AppCompatTextView) view.findViewById(R.id.tv_left);
        tvRight = (AppCompatTextView) view.findViewById(R.id.tv_right);
    }

    @SuppressWarnings("unchecked")
    private void initData(Bundle arguments) {
        mType = (JdCommitOrderNoStockGoodsCount) arguments.getSerializable(TAG_TYPE);
        mNoStockDatas = (ArrayList<JDCommitOrderEntity>) arguments.getSerializable(TAG);
        recyclerView.setAdapter(new MyAdapter(mNoStockDatas));
    }

    private void setView() {
        switch (mType) {
            case PARTLY_NO_STOCK:
                tvTitle.setText("抱歉，您本单购买的以下商品或赠品在所选择的地址下暂时无货");
                tvLeft.setText("返回购物车");
                RxUtil.clicks(tvLeft, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismiss();
                        if (mNoStockListener != null) {
                            mNoStockListener.backToShoppingCart();
                        }
                    }
                });
                tvRight.setText("移除无货商品");
                RxUtil.clicks(tvRight, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismiss();
                        if (mNoStockListener != null) {
                            mNoStockListener.removeNoStockGoods(mNoStockDatas);
                        }
                    }
                });
                break;
            case ALL_NO_STOCK:
                tvTitle.setText("抱歉，您本单购买的商品全部无货");
                tvLeft.setText("更改收货地址");
                RxUtil.clicks(tvLeft, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismiss();
                        if (mNoStockListener != null) {
                            mNoStockListener.changeShippingAddress();
                        }
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

    public interface NoStockListener {
        /**
         * 回到购物车
         */
        void backToShoppingCart();

        /**
         * 移除无货商品
         *
         * @param noStockDatas
         */
        void removeNoStockGoods(ArrayList<JDCommitOrderEntity> noStockDatas);

        /**
         * 改变收货地址
         */
        void changeShippingAddress();
    }


    private final class MyAdapter extends BaseQuickAdapter<JDCommitOrderEntity, BaseViewHolder> {

        public MyAdapter(@Nullable List<JDCommitOrderEntity> data) {
            super(R.layout.item_jd_commit_order_no_stock, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, JDCommitOrderEntity item) {
            GlideUtil.showImage(getContext(), JDImageUtil.getJDImageUrl_N4(item.imagePath), helper.getView(R.id.iv_no_stock_goods));
            helper.getView(R.id.iv_no_stock_goods_cover).setBackgroundResource(R.mipmap.icon_jd_no_stock_little);
            helper.setText(R.id.tv_content, item.goodsName);
            helper.setText(R.id.tv_count, "×" + item.goodsCount);
        }
    }
}
