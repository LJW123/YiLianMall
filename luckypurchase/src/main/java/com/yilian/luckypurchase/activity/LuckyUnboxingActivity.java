package com.yilian.luckypurchase.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.LuckySimpleImgAdapter;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.luckypurchase.widget.NoScrollRecyclerView;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.LuckyShowListInfoEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Ray_L_Pain
 *         晒单详情
 */
public class LuckyUnboxingActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivMore;

    private ScrollView normalLayout;
    private ImageView iv;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvDetailName;
    private TextView tvDetailIssue;
    private TextView tvDetailCount;
    private TextView tvDetailNum;
    private TextView tvDetailTime;
    private TextView tvContent;
    private NoScrollRecyclerView recyclerView;

    private LinearLayout layoutNetError;
    private TextView tvRefresh;

    private String id;
    private LuckySimpleImgAdapter adapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_unboxing);
        initView();
        initData();
    }

    private void initView() {
        id = getIntent().getStringExtra("activity_id");

        ivBack = (ImageView) findViewById(R.id.v3Back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.v3Title);
        tvTitle.setText("晒单详情");

        ivMore = (ImageView) findViewById(R.id.v3Shop);
        ivMore.setImageResource(R.mipmap.library_module_v3_more_bottom);
        ivMore.setVisibility(View.VISIBLE);
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        normalLayout = (ScrollView) findViewById(R.id.layout_normal);
        normalLayout.smoothScrollTo(0, 20);
        iv = (ImageView) findViewById(R.id.iv);
        iv.setOnClickListener(this);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDetailName = (TextView) findViewById(R.id.tv_detail_name);
        tvDetailIssue = (TextView) findViewById(R.id.tv_detail_issue);
        tvDetailCount = (TextView) findViewById(R.id.tv_detail_count);
        tvDetailNum = (TextView) findViewById(R.id.tv_detail_num);
        tvDetailTime = (TextView) findViewById(R.id.tv_detail_time);
        tvContent = (TextView) findViewById(R.id.tv_content);
        recyclerView = (NoScrollRecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        if (adapter == null) {
            adapter = new LuckySimpleImgAdapter(R.layout.lucky_simple_img);
        }
        recyclerView.setAdapter(adapter);

        layoutNetError = (LinearLayout) findViewById(R.id.layout_net_error);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initData() {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext)).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext))
                .luckyShowListInfo(id, new Callback<LuckyShowListInfoEntity>() {
                    @Override
                    public void onResponse(Call<LuckyShowListInfoEntity> call, Response<LuckyShowListInfoEntity> response) {
                        stopMyDialog();
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                LuckyShowListInfoEntity entity = response.body();
                                switch (entity.code) {
                                    case 1:
                                        userId = entity.user_id;
                                        GlideUtil.showCirImage(mContext, entity.photo, iv);
                                        tvName.setText(TextUtils.isEmpty(entity.user_name) ? "暂无昵称" : entity.user_name);
                                        tvTime.setText(DateUtils.luckyTimeNoYearNoSecond(entity.time));

                                        tvDetailName.setText(Html.fromHtml("<font color=\"#666666\">商品信息： </font><font color=\"#333333\">" + entity.snatch_name + "</font>"));
                                        tvDetailIssue.setText(Html.fromHtml("<font color=\"#666666\">商品期号： </font><font color=\"#333333\">" + entity.snatch_issue + "</font>"));
                                        tvDetailCount.setText(Html.fromHtml("<font color=\"#666666\">本期参与： </font><font color=\"#fe5062\">" + entity.snatch_count + "</font> <font color=\"#666666\">人次</font>"));
                                        tvDetailNum.setText(Html.fromHtml("<font color=\"#666666\">中奖号码： </font><font color=\"#fe5062\">" + entity.win_num + "</font>"));
                                        tvDetailTime.setText(Html.fromHtml("<font color=\"#666666\">揭晓时间： </font><font color=\"#333333\">" + DateUtils.luckyTime(entity.snatch_announce_time) + "</font>"));

                                        if (TextUtils.isEmpty(entity.comment_content)) {
                                            tvContent.setVisibility(View.GONE);
                                        } else {
                                            tvContent.setVisibility(View.VISIBLE);
                                            tvContent.setText(entity.comment_content);
                                        }

                                        final String[] img = entity.comment_images.split(",");
                                        ArrayList<String> list = new ArrayList<>(Arrays.asList(img));

                                        adapter.setNewData(list);

                                        normalLayout.setVisibility(View.VISIBLE);
                                        layoutNetError.setVisibility(View.GONE);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LuckyShowListInfoEntity> call, Throwable t) {
                        stopMyDialog();
                        normalLayout.setVisibility(View.GONE);
                        layoutNetError.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.v3Shop);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            intent.setComponent(new ComponentName(LuckyUnboxingActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckyUnboxingActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckyUnboxingActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckyUnboxingActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv) {
            RxView.clicks(iv)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            if (isLogin()) {
                                Intent intent = new Intent(mContext, LuckyRecordActivity.class);
                                intent.putExtra("userId", userId);
                                mContext.startActivity(intent);
                            } else {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                                startActivity(intent);
                            }
                        }
                    });
        } else {
        }
    }
}
