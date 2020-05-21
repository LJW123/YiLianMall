package com.yilian.luckypurchase.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.CalculateEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Ray_L_Pain
 *         计算详情
 */
public class LuckyCountDetailActivity extends BaseAppCompatActivity {
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivMore;

    private ScrollView normalLayout;
    private TextView tvCalculate;
    private TextView tvNumberA;
    private TextView tvGoDetail;
    private TextView tvLuckyNum;

    private LinearLayout layoutNetError;
    private TextView tvRefresh;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_count_detail);
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
        tvTitle.setText("计算详情");
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
        tvCalculate = (TextView) findViewById(R.id.tv_calculate);
        tvNumberA = (TextView) findViewById(R.id.tv_number_a);
        tvGoDetail = (TextView) findViewById(R.id.tv_go_detail);
        tvGoDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LuckyCountDetailActivity.this, LuckyTimeDetailsActivity.class);
                intent.putExtra("activity_id", id);
                startActivity(intent);
            }
        });
        tvLuckyNum = (TextView) findViewById(R.id.tv_lucky_num);

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
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .snatchCalculate(id, new Callback<CalculateEntity>() {
                    @Override
                    public void onResponse(Call<CalculateEntity> call, Response<CalculateEntity> response) {
                        stopMyDialog();
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        CalculateEntity entity = response.body();
                                        tvCalculate.append(entity.baseNum);
                                        tvNumberA.setText(entity.resTime);
                                        tvLuckyNum.setText(Html.fromHtml("<font color=\"#666666\">幸运号码： </font><font color=\"#fe5062\">" + entity.winNum + "</font>"));

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
                    public void onFailure(Call<CalculateEntity> call, Throwable t) {
                        normalLayout.setVisibility(View.GONE);
                        layoutNetError.setVisibility(View.VISIBLE);
                        showToast(R.string.lucky_system_busy);
                        stopMyDialog();
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
                            intent.setComponent(new ComponentName(LuckyCountDetailActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckyCountDetailActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckyCountDetailActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckyCountDetailActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }
}
