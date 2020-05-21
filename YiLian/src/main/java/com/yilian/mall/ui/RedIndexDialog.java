package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.RedIndexAdapter;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.RedPacketIndexEntity;
import com.yilian.networkingmodule.entity.ScoreExponent;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 消费指数弹窗
 *
 * @author Ray_L_Pain
 * @date 2017/11/23 0023
 */

public class RedIndexDialog extends BaseActivity {
    @ViewInject(R.id.iv_img)
    ImageView ivImg;
    @ViewInject(R.id.tv_merchant_count)
    TextView tvMerchantCount;
    @ViewInject(R.id.tv_member_count)
    TextView tvMemberCount;
    @ViewInject(R.id.recycleView)
    RecyclerView recyclerView;
    @ViewInject(R.id.iv_close)
    ImageView ivClose;

    private int screenWidth;
    private ArrayList<ScoreExponent> list = new ArrayList<>();
    private RedIndexAdapter adapter;
    /**
     * 新增最外层布局的显隐性，优化界面交互
     */
    private View llContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_red_index);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        llContent = findViewById(R.id.ll_content);
        screenWidth = ScreenUtils.getScreenWidth(mContext);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivImg.getLayoutParams();
        params.width = (int) (screenWidth * 0.75);
        params.height = (int) ((261 * 0.1) * params.width / (836 * 0.1));
        Logger.i("ray-" + params.width);
        Logger.i("ray-" + params.height);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (adapter == null) {
            adapter = new RedIndexAdapter(R.layout.item_red_index_list);
        }
        recyclerView.setAdapter(adapter);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        RetrofitUtils2.getInstance(mContext).redPacketIndex(new Callback<RedPacketIndexEntity>() {
            @Override
            public void onResponse(Call<RedPacketIndexEntity> call, Response<RedPacketIndexEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        switch (body.code) {
                            case 1:
                                RedPacketIndexEntity.DataBean data = response.body().data;
                                tvMerchantCount.setText(String.valueOf(data.merchantNum));
                                tvMemberCount.setText(String.valueOf(data.userNum));

                                ArrayList<ScoreExponent> newList = data.integralNumberArr;
                                adapter.setNewData(newList);
                                llContent.setVisibility(View.VISIBLE);
                                adapter.loadMoreEnd(true);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RedPacketIndexEntity> call, Throwable t) {
                finish();
                showToast(R.string.net_work_not_available);
            }
        });

    }
}
