package com.yilian.mall.jd.fragment.commitorder;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.BaseDialogFragment;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerItemDecoration;
import com.yilian.mall.jd.activity.JDCommitOrderActivity;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author xiaofo on 2018/7/3.
 */

public class CommitOrderGoodsListInfoFragment extends BaseDialogFragment {
    public static final String TAG = "CommitOrderGoodsListInfoFragment";

    private ArrayList<JDCommitOrderEntity> mJdCommitOrderEntities;
    private AppCompatTextView tvGoodsCount;
    private LinearLayout llTitle;
    private RecyclerView recyclerView;
    private AppCompatTextView tvPriceDetail;
    private ArrayList<JDCommitOrderEntity> mDatas;
    private JDCommitOrderActivity activity;

    public static CommitOrderGoodsListInfoFragment getInstance(ArrayList<JDCommitOrderEntity> jdCommitOrderEntities) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, jdCommitOrderEntities);
        CommitOrderGoodsListInfoFragment commitOrderGoodsListInfoFragment = new CommitOrderGoodsListInfoFragment();
        commitOrderGoodsListInfoFragment.setArguments(bundle);
        return commitOrderGoodsListInfoFragment;
    }

    public void initParentActivity(JDCommitOrderActivity activity) {
        this.activity = activity;

    }


    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.dialog_fragment_commit_order_goods_list, container);
        initView(inflate);
        initData();
        setData();
        return inflate;
    }

    private void initView(View inflate) {
        tvGoodsCount = (AppCompatTextView) inflate.findViewById(R.id.tv_goods_count);
        llTitle = (LinearLayout) inflate.findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration((new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL_LIST, 1, Color.parseColor("#e7e7e7"))));
        tvPriceDetail = (AppCompatTextView) inflate.findViewById(R.id.tv_price_detail);
        RxUtil.clicks(llTitle, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                dismiss();
            }
        });
        RxUtil.clicks(tvPriceDetail, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                activity.showPriceInstructionDialog("知道了", "因可能存在系统缓存、页面更新导致价格变动异常等不确定性情况出现，" +
                        "商品售价以本结算页商品价格为准；\n\n" +
                        "如有疑问，请立即联系销售商咨询。", "价格说明");
//                Spanned spanned = Html.fromHtml("因可能存在系统缓存、页面更新导致价" +
//                        "格变动异常等不确定性情况出现，商品" +
//                        "售价以本结算页商品价格为准；<br>" +
//                        "<br>" +
//                        "如有疑问，请立即联系客服。<br>" +
//                        "客服电话：<font color='F72D42'>400-152-5159</color></font>");
//                showSystemV7Dialog("价格说明", spanned, "我知道了", null, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        Bundle arguments = getArguments();
        mDatas = (ArrayList<JDCommitOrderEntity>) arguments.getSerializable(TAG);
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        int goodsTotalCount = 0;
        for (JDCommitOrderEntity mData : mDatas) {
            goodsTotalCount += mData.goodsCount;
        }
        tvGoodsCount.setText("共" + goodsTotalCount + "件");
        recyclerView.setAdapter(new MyAdapter(mDatas));
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomToTopAnim);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            params.height = (int) (ScreenUtils.getScreenHeight(getContext()) / 1.3);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    class MyAdapter extends BaseQuickAdapter<JDCommitOrderEntity, BaseViewHolder> {

        MyAdapter(@Nullable List<JDCommitOrderEntity> data) {
            super(R.layout.item_jd_commit_order_fragment_dialog_goods_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, JDCommitOrderEntity item) {
            GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N4(item.imagePath), helper.getView(R.id.iv_goods));
            helper.setText(R.id.tv_goods_name, item.goodsName);
            helper.setText(R.id.tv_goods_price, MoneyUtil.add¥Front(item.goodsJdPrice));
            helper.setText(R.id.tv_return_bean, item.returnBean);
            helper.setText(R.id.tv_goods_count, "×" + String.valueOf(item.goodsCount));
        }
    }
}
