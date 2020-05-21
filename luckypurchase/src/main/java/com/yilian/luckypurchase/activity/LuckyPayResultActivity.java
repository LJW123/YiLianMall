package com.yilian.luckypurchase.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.LuckyGridViewAdapter;
import com.yilian.luckypurchase.widget.NoScrollGridView;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yilian.luckypurchase.R.id.v3Back;


/**
 * @author Ray_L_Pain
 * 幸运购-支付结果页面
 */
public class LuckyPayResultActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView ivMore;
    private ImageView ivBack;
    private TextView tvBtnAgain;
    private TextView tvBtnRecord;
    private TextView tvJoinNum;
    private ImageView ivActivityPic;
    private TextView tvActivityName;
    private TextView tvActivityNumber;
    private LinearLayout lookMoreLayout;
    private TextView tvLookMore;
    private NoScrollGridView gridView;

    private LuckyGridViewAdapter adapter;

    private String id, issue, manTime, snatchNum, name, url, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_pay_result);
        initView();
        initData();
    }

    private void initView() {
        id = getIntent().getStringExtra("activity_id");
        issue = getIntent().getStringExtra("activity_issue");
        manTime = getIntent().getStringExtra("activity_man_time");
        snatchNum = getIntent().getStringExtra("activity_num");
        name = getIntent().getStringExtra("activity_name");
        url = getIntent().getStringExtra("activity_url");
        userId = getIntent().getStringExtra("user_id");

        tvTitle = (TextView) findViewById(R.id.v3Title);
        tvTitle.setText("支付结果");
        ivMore = (ImageView) findViewById(R.id.v3Shop);
        ivMore.setImageResource(R.mipmap.library_module_v3_more_bottom);
        ivMore.setVisibility(View.VISIBLE);
        ivBack = (ImageView) findViewById(v3Back);
        tvBtnAgain = (TextView) findViewById(R.id.tv_btn_again);
        tvBtnRecord = (TextView) findViewById(R.id.tv_btn_record);
        tvJoinNum = (TextView) findViewById(R.id.tv_join_num);
        ivActivityPic = (ImageView) findViewById(R.id.iv_activity_pic);
        tvActivityName = (TextView) findViewById(R.id.tv_activity_name);
        tvActivityNumber = (TextView) findViewById(R.id.tv_activity_number);
        lookMoreLayout = (LinearLayout) findViewById(R.id.layout_look_more);
        tvLookMore = (TextView) findViewById(R.id.tv_look_more);
        gridView = (NoScrollGridView) findViewById(R.id.gridView);

        final String[] img = snatchNum.split(",");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(img));
        Logger.i("2017年10月25日 16:28:05-" + list.size());
        Logger.i("2017年10月25日 16:28:05-" + list.toString());

        if (list.size() >= 12) {
            List<String> newList = list.subList(0, 11);
            newList.add("......");
            Logger.i("2017年10月25日 16:28:05-" + newList.toString());
            adapter = new LuckyGridViewAdapter(mContext, newList);
            tvLookMore.setText("查看全部");
        } else {
            adapter = new LuckyGridViewAdapter(mContext, list);
            tvLookMore.setText("");
        }
        gridView.setAdapter(adapter);

        ivBack.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        tvBtnAgain.setOnClickListener(this);
        tvBtnRecord.setOnClickListener(this);
        lookMoreLayout.setOnClickListener(this);
    }

    private void initData() {
        tvJoinNum.setText(Html.fromHtml("<font color=\"#666666\">成功参与 </font><font color=\"#fe5062\">" + manTime + "</font> <font color=\"#666666\">人次 </font>"));

        GlideUtil.showImageWithSuffix(mContext, url, ivActivityPic);

        tvActivityName.setText(name);

        tvActivityNumber.setText("期号：" + issue);
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
                            intent.setComponent(new ComponentName(LuckyPayResultActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckyPayResultActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckyPayResultActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckyPayResultActivity.this, "com.yilian.mall.ui.JPMainActivity"));
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
        Intent intent = null;
        int i = v.getId();
        if (i == R.id.v3Back) {
            AppManager.getInstance().killActivity(LuckyPayResultActivity.class);
            AppManager.getInstance().killActivity(LuckySubmitOrderActivity.class);
            finish();
        } else if (i == R.id.v3Shop) {
            showMenu();
        } else if (i == R.id.tv_btn_again) {
            intent = new Intent(LuckyPayResultActivity.this, LuckyActivityDetailActivity.class);
            intent.putExtra("activity_id", id);
            intent.putExtra("type", "0");
            intent.putExtra("show_window", "0");
            startActivity(intent);

            AppManager.getInstance().killActivity(LuckyPayResultActivity.class);
            AppManager.getInstance().killActivity(LuckySubmitOrderActivity.class);
        } else if (i == R.id.tv_btn_record) {
            intent = new Intent(LuckyPayResultActivity.this, LuckyMyRecordActivity.class);
            intent.putExtra("current_position", 0);
            startActivity(intent);

            AppManager.getInstance().killActivity(LuckyPayResultActivity.class);
            AppManager.getInstance().killActivity(LuckySubmitOrderActivity.class);
            AppManager.getInstance().killActivity(LuckyActivityDetailActivity.class);
        } else if (i == R.id.layout_look_more) {
            intent = new Intent(LuckyPayResultActivity.this, LuckyNumberRecordActivity.class);
            intent.putExtra("activity_id", userId);
            intent.putExtra("from", "pay");
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        AppManager.getInstance().killActivity(LuckyPayResultActivity.class);
        AppManager.getInstance().killActivity(LuckySubmitOrderActivity.class);
        finish();
    }
}
