package com.yilian.luckypurchase.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.utils.MoneyUtil;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.entity.LuckyPayEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Ray_L_Pain
 *         幸运购-提交订单页面
 */
public class LuckySubmitOrderActivity extends BaseAppCompatActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivMore;
    private TextView tvAllCount;
    private TextView tvName;
    private TextView tvCount;
    private TextView tvIntegral;
    private TextView tvSubmitIntegral;
    private TextView tvSubmitBtn;
    private LinearLayout layoutJoinNext;
    private LinearLayout bottomLayout;

    private String id, nameStr, countStr, integralStr;
    private int prizeListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_submit_order);

        initView();
        initData();
    }

    private void initView() {
        id = getIntent().getStringExtra("activity_id");
        nameStr = getIntent().getStringExtra("activity_name");
        countStr = getIntent().getStringExtra("activity_count");
        integralStr = getIntent().getStringExtra("activity_integral");
        prizeListPosition = getIntent().getIntExtra("prizeListPosition", -1);
        ivBack = (ImageView) findViewById(R.id.v3Back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.v3Title);
        tvTitle.setText("确认订单");
        ivMore = (ImageView) findViewById(R.id.v3Shop);
        ivMore.setImageResource(R.mipmap.library_module_v3_more_bottom);
        ivMore.setVisibility(View.VISIBLE);
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });
        tvAllCount = (TextView) findViewById(R.id.tv_all_count);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvSubmitIntegral = (TextView) findViewById(R.id.tv_submit_integral);
        tvSubmitBtn = (TextView) findViewById(R.id.tv_submit_btn);
        tvSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    pay();
                } else {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(LuckySubmitOrderActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    startActivity(intent);
                }
            }
        });
        layoutJoinNext = (LinearLayout) findViewById(R.id.layout_join_next);
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
    }

    private void initData() {
        tvAllCount.setText("共 " + 1 + " 件商品");
        tvName.setText(nameStr);
        tvCount.setText(countStr + "人次");
        tvIntegral.setText(MoneyUtil.getLeXiangBiNoZero(NumberFormat.convertToInt(integralStr,0) * NumberFormat.convertToInt(countStr,0)));
        tvSubmitIntegral.setText(MoneyUtil.getLeXiangBiNoZero(NumberFormat.convertToInt(integralStr,0) * NumberFormat.convertToInt(countStr,0)));
    }

    void updateLuckyPrizeList() {
//        使用EventBus更新活动列表
    }

    private void pay() {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext)).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext))
                .luckyPayOrder(id, countStr, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext), PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext), new Callback<LuckyPayEntity>() {
                    @Override
                    public void onResponse(Call<LuckyPayEntity> call, Response<LuckyPayEntity> response) {
                        stopMyDialog();
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                LuckyPayEntity entity = response.body();
                                switch (entity.code) {
                                    case 1:
                                        Intent intent = new Intent(LuckySubmitOrderActivity.this, LuckyPayResultActivity.class);
                                        intent.putExtra("activity_id", entity.activity_id);
                                        intent.putExtra("activity_issue", entity.snatch_issue);
                                        intent.putExtra("activity_man_time", entity.snatch_man_time);
                                        intent.putExtra("activity_num", entity.snatch_num);
                                        intent.putExtra("activity_name", entity.snatch_name);
                                        intent.putExtra("activity_url", entity.snatch_goods_url);
                                        intent.putExtra("user_id", entity.snatch_index);
                                        startActivity(intent);
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_LUCKY_DETAIL, true, mContext);
                                        break;
                                    default:
                                        showToast(entity.msg);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LuckyPayEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.lucky_system_busy);
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
                            intent.setComponent(new ComponentName(LuckySubmitOrderActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckySubmitOrderActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckySubmitOrderActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckySubmitOrderActivity.this, "com.yilian.mall.ui.JPMainActivity"));
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
