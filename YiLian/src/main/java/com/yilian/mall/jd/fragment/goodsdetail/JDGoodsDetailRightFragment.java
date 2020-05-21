package com.yilian.mall.jd.fragment.goodsdetail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.jd.utils.GoodsIntroduceUtil;
import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;

import java.util.ArrayList;
import java.util.List;

import static com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity.JD_GOODS_DETAIL_INFO_ENTITY;

/**
 * item页ViewPager里的详情Fragment
 */
public class JDGoodsDetailRightFragment extends Fragment implements View.OnClickListener {
    private LinearLayout ll_goods_detail, ll_goods_config, ll_goods_service;
    private TextView tv_goods_detail, tv_goods_config, tv_goods_service;
    private FrameLayout fl_content;
    /**
     * 选中Fragment 所在position
     */
    private int nowIndex;
    private float fromX;
    private List<TextView> tabTextList;
    private JDGoodsInfoConfigFragment goodsInfoConfigFragment;
    private JDGoodsDetailInfoWebFragment goodsInfoWebFragment;
    private JDGoodsInfoAfterSaleFragment goodsInfoServiceFragment;
    private Fragment nowFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private View rootView;

    public static JDGoodsDetailRightFragment newInstance(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity) {
        JDGoodsDetailRightFragment goodsDetailFragment = new JDGoodsDetailRightFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(JD_GOODS_DETAIL_INFO_ENTITY, jdGoodsDetailInfoEntity);
        goodsDetailFragment.setArguments(bundle);
        return goodsDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.jd_fragment_goods_detail, null);
        Bundle arguments = getArguments();
        JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity = (JDGoodsDetailInfoEntity) arguments.getSerializable(JD_GOODS_DETAIL_INFO_ENTITY);
        initView();
        initListener();
        initData(jdGoodsDetailInfoEntity);

        return rootView;
    }

    private void initView() {
        ll_goods_detail = rootView.findViewById(R.id.ll_goods_detail);
        ll_goods_config = rootView.findViewById(R.id.ll_goods_config);
        ll_goods_service = rootView.findViewById(R.id.ll_goods_service);
        tv_goods_detail = rootView.findViewById(R.id.tv_goods_detail);
        tv_goods_config = rootView.findViewById(R.id.tv_goods_config);
        tv_goods_service = rootView.findViewById(R.id.tv_goods_service);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_content);

//        v_tab_cursor = rootView.findViewById(R.id.v_tab_cursor);
//        ViewGroup.LayoutParams params = v_tab_cursor.getLayoutParams();
//        DisplayMetrics dm = RxDeviceTool.getDisplayMetrics(getActivity());
//        int width = dm.widthPixels / 3;
//        params.width = width;
//        v_tab_cursor.setLayoutParams(params);

        tabTextList = new ArrayList<>();
        tabTextList.add(tv_goods_detail);
        tabTextList.add(tv_goods_config);
        tabTextList.add(tv_goods_service);
    }

    private void initListener() {
        ll_goods_detail.setOnClickListener(this);
        ll_goods_config.setOnClickListener(this);
        ll_goods_service.setOnClickListener(this);
    }

    /**
     * 商品信息Fragment页获取完数据执行
     */
    public void initData(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity) {
        String appIntroduce = jdGoodsDetailInfoEntity.data.goodsInfo.appintroduce;
        String introduction = jdGoodsDetailInfoEntity.data.goodsInfo.introduction;
        goodsInfoWebFragment = JDGoodsDetailInfoWebFragment.newInstance(GoodsIntroduceUtil.getIntroduce(appIntroduce,introduction));
        goodsInfoConfigFragment = JDGoodsInfoConfigFragment.newInstance(jdGoodsDetailInfoEntity.data.goodsInfo.param);
        goodsInfoServiceFragment = JDGoodsInfoAfterSaleFragment.newInstance(jdGoodsDetailInfoEntity.data.goodsInfo.wareQD
                , jdGoodsDetailInfoEntity.data.goodsInfo.shouhou);

        nowFragment = goodsInfoWebFragment;
        fragmentManager = getChildFragmentManager();
        //默认显示商品详情tab
        fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_goods_detail:
                //商品详情tab
                switchFragment(nowFragment, goodsInfoWebFragment);
                nowIndex = 0;
                nowFragment = goodsInfoWebFragment;
                scrollCursor();
                break;

            case R.id.ll_goods_config:
                //规格参数tab
                switchFragment(nowFragment, goodsInfoConfigFragment);
                nowIndex = 1;
                nowFragment = goodsInfoConfigFragment;
                scrollCursor();
                break;
            case R.id.ll_goods_service:
                //包装售后tab
                switchFragment(nowFragment, goodsInfoServiceFragment);
                nowIndex = 2;
                nowFragment = goodsInfoServiceFragment;
                scrollCursor();
                break;
            default:
                break;
        }
    }

    /**
     * 切换Fragment
     * <p>(hide、show、add)
     *
     * @param fromFragment
     * @param toFragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        if (nowFragment != toFragment) {
            fragmentTransaction = fragmentManager.beginTransaction();
            if (!toFragment.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fromFragment).add(R.id.fl_content, toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到activity中
            } else {
                fragmentTransaction.hide(fromFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    /**
     * 滑动游标
     */
    private void scrollCursor() {
//        TranslateAnimation anim = new TranslateAnimation(fromX, nowIndex * v_tab_cursor.getWidth(), 0, 0);
//        anim.setFillAfter(true);//设置动画结束时停在动画结束的位置
//        anim.setDuration(50);
//        //保存动画结束时游标的位置,作为下次滑动的起点
//        fromX = nowIndex * v_tab_cursor.getWidth();
//        v_tab_cursor.startAnimation(anim);
        //设置Tab切换颜色
        for (int i = 0; i < tabTextList.size(); i++) {
            tabTextList.get(i).setTextColor(i == nowIndex ? Color.parseColor("#ec0f38") : Color.parseColor("#222222"));
        }
    }
}
